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

import java.io.ObjectStreamException;
import java.util.HashMap;
import java.util.Map;

import dk.group2.smap.shinemyroom.generated.Body;
import dk.group2.smap.shinemyroom.generated.ClipCommand;
import dk.group2.smap.shinemyroom.generated.HueRemoteMessageBody;


public class RemoteHueControl extends BaseHueControl {

    private static String remoteApiBase = "https://www.meethue.com/api/";
    private static String token = "NmlQc0RUZnRXeVpwK3NFRHB4ekRmMkUybEVxa21UaVlLeEdMY2lKdEt4MD0%3D"; //Get it from https://www.meethue.com/en-us/user/apps
    private static String remoteMessageUrl = remoteApiBase + "sendmessage" + "?token=" + token;
    //private static String remoteStatusUrl = remoteApiBase + "getbridge" + "?token=" + token;

    public RemoteHueControl(Context c){
        super(c);
    }



    /**
     *@param state ON or Off
     *@param i which light
     */
    private String makeBodyString(int i, boolean state){
        final Gson gson = new Gson();
        HueRemoteMessageBody body = new HueRemoteMessageBody();
        body.setClipCommand(new ClipCommand());
        body.getClipCommand().setUrl("/api/0/lights/" + i + "/state");

        Body b = new Body();
        b.setOn(state);
        body.getClipCommand().setBody(b);

        body.getClipCommand().setMethod("PUT");

        return "clipmessage=" + gson.toJson(body);
    }
    @Override
    public void setLight(int i, boolean state){
        //send request using Volley
        RequestQueue queue = Volley.newRequestQueue(super.c);
        final String bodyString = makeBodyString(i,state);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, remoteMessageUrl,
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

            //http://stackoverflow.com/questions/17049473/how-to-set-custom-header-in-volley-request
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(stringRequest);
    }

    private String makeBodyString2(int i, boolean state){
        final Gson gson = new Gson();
        HueRemoteMessageBody body = new HueRemoteMessageBody();
        body.setClipCommand(new ClipCommand());
        body.getClipCommand().setUrl("/api/0/groups/");

        Object obj = new Object(){
            String name="Test";
            String light="1";
        };
        Body b = new Body();
        b.setOn(state);
        body.getClipCommand().setBody(b);

        body.getClipCommand().setMethod("PUT");

        return "clipmessage=" + gson.toJson(body);
    }

    public void setGroupLight(boolean state){
        RequestQueue queue = Volley.newRequestQueue(super.c);
//        final String bodyString = makeBodyString(i,state);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, remoteMessageUrl,
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

            //http://stackoverflow.com/questions/17049473/how-to-set-custom-header-in-volley-request
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(stringRequest);
    }


}
