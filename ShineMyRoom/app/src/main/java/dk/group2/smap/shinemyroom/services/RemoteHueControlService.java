package dk.group2.smap.shinemyroom.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import dk.group2.smap.shinemyroom.generated.Body;
import dk.group2.smap.shinemyroom.generated.ClipCommand;
import dk.group2.smap.shinemyroom.generated.HueRemoteMessageBody;
import dk.group2.smap.shinemyroom.storage.HueSharedPreferences;
//https://docs.apitools.com/blog/2015/02/25/hacking-apitools-during-the-3scale-internal-hackathon.html
public class RemoteHueControlService extends IntentService{

    private static final String TAG = "LOG/" + RemoteHueControlService.class.getName();
    private static String remoteApiBase = "https://www.meethue.com/api/";
    private static String token = "?token=NmlQc0RUZnRXeVpwK3NFRHB4ekRmMkUybEVxa21UaVlLeEdMY2lKdEt4MD0%3D"; //Get it from https://www.meethue.com/en-us/user/apps
    private static String remoteMessageUrl = remoteApiBase + "sendmessage" + token;
    private static String getBrigdeUrl =  remoteApiBase + "getbridge" + token;
    private static final String TRY_GET_REMOTE_ACESS = "try_get_remote_access_action";
    private static final String REMOTE_SET_LIGHT_FOR_GROUP_ACTION = "remote_set_light_for_group_action";
    private static final String setLightForGroupAction_identifier = "setLightForGroupAction.identifier";
    private static final String setLightForGroupAction_state = "setLightForGroupAction.state";
    public static final String remote_access_failed_action_result = "remote_access_failed_action_result";
    public static final String remote_access_sucess_action_result = "remote_access_sucess_action_result";
    private static String url =  "https://www.meethue.com/en-us/user/apps";

    private static RequestQueue requestQueue;

    private static synchronized RequestQueue getRequestQueue(Context c) {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(c);
        }
        return requestQueue;
    }

    public RemoteHueControlService() {
        super(RemoteHueControlService.class.getName());
    }
    //private static String remoteStatusUrl = remoteApiBase + "getbridge" + "?token=" + token;

   public static void startTryGetRemoteAccessAction(Context c){
           Intent intent = new Intent(c, RemoteHueControlService.class);
           intent.setAction(TRY_GET_REMOTE_ACESS);
           c.startService(intent);
       }

    private void tryGetRemoteAccess() {

        RequestQueue queue = getRequestQueue(this);
        //send request using Volley
        StringRequest stringRequest = new StringRequest(Request.Method.GET, getBrigdeUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        HueSharedPreferences instance = HueSharedPreferences.getInstance(getApplicationContext());
                        instance.setRemoteBridge(response);
                        Log.d("test", response);
                        //TODO handle result and store it
                        broadcastRemoteAccessSuccess();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error.API", error.getMessage());
                broadcastRemoteAccessFailed();
            }
        });
        queue.add(stringRequest);
    }

    private void broadcastRemoteAccessFailed() {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(remote_access_failed_action_result);
        Log.d(TAG, "Broadcasting:" + remote_access_failed_action_result);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
    }

    private void broadcastRemoteAccessSuccess() {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(remote_access_sucess_action_result);
        Log.d(TAG, "Broadcasting:" + remote_access_sucess_action_result);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent != null){
            if(intent.getAction().equals(TRY_GET_REMOTE_ACESS))
            {
                Log.d(TAG, "onHandleIntent: " + TRY_GET_REMOTE_ACESS);
                tryGetRemoteAccess();
            }else if(intent.getAction().equals(REMOTE_SET_LIGHT_FOR_GROUP_ACTION))
            {
                String stringExtra = intent.getStringExtra(setLightForGroupAction_identifier);
                boolean booleanExtra = intent.getBooleanExtra(setLightForGroupAction_state, false);
                setLightForGroup(stringExtra,booleanExtra);
            }
        }

    }
    //https://www.developers.meethue.com/documentation/groups-api
    public static void setLightForGroupAction(Context c, String identifier, boolean isChecked) {
        Intent intent = new Intent(c, RemoteHueControlService.class);
        intent.setAction(REMOTE_SET_LIGHT_FOR_GROUP_ACTION);
        intent.putExtra(setLightForGroupAction_identifier,identifier);
        intent.putExtra(setLightForGroupAction_state,isChecked);
        c.startService(intent);
    }
        private String makeBodyString(int i, boolean state){
        final Gson gson = new Gson();
        HueRemoteMessageBody body = new HueRemoteMessageBody();
        body.setClipCommand(new ClipCommand());
        body.getClipCommand().setUrl("/api/0/groups/" + i + "/action");

        Body b = new Body();
        b.setOn(state);
        body.getClipCommand().setBody(b);

        body.getClipCommand().setMethod("PUT");

        return "clipmessage=" + gson.toJson(body);
    }
    private void setLightForGroup(String identifier, boolean isChecked) {

        RequestQueue queue = getRequestQueue(this);
        final String bodyString = makeBodyString(Integer.valueOf(identifier),isChecked);
        //send request using Volley
        //http://stackoverflow.com/questions/17049473/how-to-set-custom-header-in-volley-request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, remoteMessageUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d(TAG, response);
                        //TODO handle result and store it
                        //error handlers here too, it will return 200 even on errors,
                        //broadcastRemoteAccessSuccess();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.getMessage());
                broadcastRemoteAccessFailed();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return bodyString.getBytes();
            }
        };
        queue.add(stringRequest);
    }






}
