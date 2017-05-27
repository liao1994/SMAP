package dk.group2.smap.shinemyroom;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class RemoteHueControl {

    private static String remoteApiBase = "https://www.meethue.com/api/";
    private static String token = "?token=NmlQc0RUZnRXeVpwK3NFRHB4ekRmMkUybEVxa21UaVlLeEdMY2lKdEt4MD0%3D"; //Get it from https://www.meethue.com/en-us/user/apps
    private static String remoteMessageUrl = remoteApiBase + "sendmessage" + token;
    private static String getBrigdeUrl =  remoteApiBase + "getbridge" + token;
    private Context c;
    //private static String remoteStatusUrl = remoteApiBase + "getbridge" + "?token=" + token;

    public RemoteHueControl(Context c){
        this.c = c;
    }

    public boolean canGetBridge() {
        URL url;
        HttpsURLConnection connection = null;
        boolean result = false;
        try {
            url = new URL(getBrigdeUrl);
            connection = (HttpsURLConnection) url.openConnection();
            // Timeout for reading InputStream arbitrarily set to 3000ms.
            connection.setReadTimeout(3000);
            // Timeout for connection.connect() arbitrarily set to 3000ms.
            connection.setConnectTimeout(3000);
            // For this use case, set HTTP method to GET.
            connection.setRequestMethod("GET");
            // Already true by default but setting just in case; needs to be true since this request
            // is carrying an input (response) body.
            connection.setDoInput(true);
            // Open communications link (network traffic occurs here).
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK)
                result = true;
            // Retrieve the response body as an InputStream.
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return result;
    }


//    /**
//     *@param state ON or Off
//     *@param i which light
//     */
//    private String makeBodyString(int i, boolean state){
//        final Gson gson = new Gson();
//        HueRemoteMessageBody body = new HueRemoteMessageBody();
//        body.setClipCommand(new ClipCommand());
//        body.getClipCommand().setUrl("/api/0/lights/" + i + "/state");
//
//        Body b = new Body();
//        b.setOn(state);
//        body.getClipCommand().setBody(b);
//
//        body.getClipCommand().setMethod("PUT");
//
//        return "clipmessage=" + gson.toJson(body);
//    }
//    @Override
//    public void setLight(int i, boolean state){
//        //send request using Volley
//        RequestQueue queue = Volley.newRequestQueue(super.c);
//        final String bodyString = makeBodyString(i,state);
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, remoteMessageUrl,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        //TODO handling responses, doesn't really matter so low priority right now since the api doesn't respond with any useful info anyway
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.d("Error.API", error.getMessage());
//            }
//
//        }){
//            //http://stackoverflow.com/questions/17049473/how-to-set-custom-header-in-volley-request
//
//            @Override
//            public byte[] getBody() throws AuthFailureError {
//                return bodyString.getBytes();
//            }
//
//            //http://stackoverflow.com/questions/17049473/how-to-set-custom-header-in-volley-request
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String>  params = new HashMap<>();
//                params.put("Content-Type", "application/x-www-form-urlencoded");
//                return params;
//            }
//        };
//        queue.add(stringRequest);
//    }
//
//    private String makeBodyString2(int i, boolean state){
//        final Gson gson = new Gson();
//        HueRemoteMessageBody body = new HueRemoteMessageBody();
//        body.setClipCommand(new ClipCommand());
//        body.getClipCommand().setUrl("/api/0/groups/");
//
//        Object obj = new Object(){
//            String name="Test";
//            String light="1";
//        };
//        Body b = new Body();
//        b.setOn(state);
//        body.getClipCommand().setBody(b);
//
//        body.getClipCommand().setMethod("PUT");
//
//        return "clipmessage=" + gson.toJson(body);
//    }
//    @Override
//    public void setGroupLight(int groupId, boolean state){
//        RequestQueue queue = Volley.newRequestQueue(super.c);
//        final String bodyString = makeBodyString2(groupId,state);
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, remoteMessageUrl,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        //TODO handling responses, doesn't really matter so low priority right now since the api doesn't respond with any useful info anyway
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.d("Error.API", error.getMessage());
//            }
//
//        }){
//            //http://stackoverflow.com/questions/17049473/how-to-set-custom-header-in-volley-request
//
//            @Override
//            public byte[] getBody() throws AuthFailureError {
//                return bodyString.getBytes();
//            }
//
//            //http://stackoverflow.com/questions/17049473/how-to-set-custom-header-in-volley-request
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String>  params = new HashMap<>();
//                params.put("Content-Type", "application/x-www-form-urlencoded");
//                return params;
//            }
//        };
//        queue.add(stringRequest);
//    }





}
