package dk.group2.smap.shinemyroom;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.philips.lighting.hue.listener.PHHTTPListener;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;

import java.util.HashMap;
import java.util.Map;

import dk.group2.smap.shinemyroom.generated.Body;
import dk.group2.smap.shinemyroom.generated.GroupDetails;

/**
 * Created by liao on 21-05-2017.
 */

public class LocalHueControl extends BaseHueControl {
    private String ipAddr;
    private String userName;
    private String url;
    private Boolean trst;

    public LocalHueControl(Context c) {
        super(c);
        HueSharedPreferences h = HueSharedPreferences.getInstance(c);
        this.ipAddr = h.getLastConnectedIPAddress();
        this.userName = h.getUsername();
        String remoteApiBase = "/api/";
        url = "http://" + ipAddr + remoteApiBase + userName + "/";
    }

    @Override
        Log.d("LOG","calling HueControl");
        RequestQueue queue = Volley.newRequestQueue(super.c);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url+"groups/"+groupId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listner.onGroupResult(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error.API", error.getMessage());
            }

        } );
    }

    public void getGroupDetails_V2(int groupId, final onGroupResponseListener listner1) {
        final Boolean[] grpState = new Boolean[1];
        RequestQueue queue = Volley.newRequestQueue(super.c);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url+"groups/"+groupId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                GroupResponse responseObj = gson.fromJson(response, GroupResponse.class);
                grpState[0] = responseObj.getState().getAllOn();
                listner1.onGroupResult(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error.API", error.getMessage());
            }

        } );
        queue.add(stringRequest);
    }



}
