package dk.group2.smap.shinemyroom;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import dk.group2.smap.shinemyroom.generated.Body;

/**
 * Created by liao on 21-05-2017.
 */

public class LocalHueControl extends BaseHueControl {
    private String ipAddr;
    private String userName;
    private String url;
    public LocalHueControl(Context c, String ipAddr, String userName) {
        super(c);
        this.ipAddr = ipAddr;
        this.userName = userName;
        String remoteApiBase = "/api/";
        url = "http://" + ipAddr + remoteApiBase + userName + "/";
    }

    @Override
    public void setLight(int light, boolean state) {
        RequestQueue queue = Volley.newRequestQueue(super.c);
//        final String bodyString = new
        Body b = new Body();
        b.setOn(state);
        Gson gson = new Gson();
        final String bodyString = gson.toJson(b);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url+"lights/"+light+"/state",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //TODO handling responses, doesn't really matter so low priority right now since the api doesn't respond with any useful info anyway
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error.API", error.getMessage());
            }

        }){
            //http://stackoverflow.com/questions/17049473/how-to-set-custom-header-in-volley-request
            @Override
            public byte[] getBody() throws AuthFailureError {
                return bodyString.getBytes();
            }



        };
        queue.add(stringRequest);

    }
}
