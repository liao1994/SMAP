package dk.group2.smap.assigment2;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.UUID;

import dk.group2.smap.assigment2.generatedfiles.RootObject;
import dk.group2.smap.assigment2.generatedfiles.Weather;


public class WeatherService extends IntentService {

    private final static String TAG = "LOG/" + WeatherService.class.getName();

    public static final String ACTION_WEATHER = "dk.group2.assigment2.action.weather";
    public static final String ACTION_ICON = "dk.group2.assigment2.action.icon";

    public WeatherService() {
        super("IntentService");
        Log.d(TAG, "Background service onCreate");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "Background service onHandleIntent");
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_WEATHER.equals(action)) {
                sendRequest();

            }
            if (ACTION_ICON.equals(action)) {
                loadIcon(intent.getStringExtra("iconID_KEY"));
            }
        }
    }

    public static void startAction(Context context){
        Intent intent = new Intent(context, WeatherService.class);
        intent.setAction(ACTION_WEATHER);
        context.startService(intent);
    }
    private static void startIconAction(Context context, String iconId){
        Intent intent = new Intent(context, WeatherService.class);
        intent.putExtra("iconID_KEY",iconId);
        intent.setAction(ACTION_ICON);
        context.startService(intent);
    }

    private void sendRequest(){
        RequestQueue queue = Volley.newRequestQueue(this);
        //send request using Volley
        if(queue==null){
            queue = Volley.newRequestQueue(this);
        }
        String url = Global.WEATHER_API_CALL;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        RootObject r = gson.fromJson(response,RootObject.class);
                        Weather w = r.getWeather().get(0);

                        WeatherInfo wi = new WeatherInfo(UUID.randomUUID(),w.getMain(),w.getDescription(),(r.getMain().getTemp()-275.15),w.getIcon());
                        WeatherDatabase database = new WeatherDatabase(getApplicationContext());
                        database.insertWeatherInfo(wi);
                        broadcastWeatherResult(wi);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error.API", error.getMessage());
            }
        });

        queue.add(stringRequest);

    }
    private void loadIcon(final String iconId){
        final WeatherDatabase database = new WeatherDatabase(getApplicationContext());
        if(database.iconIsNull(iconId)){
            RequestQueue queue = Volley.newRequestQueue(this);
            //send request using Volley
            if(queue==null){
                queue = Volley.newRequestQueue(this);
            }
            String url = Global.ICON_API_CALL + iconId + ".png";
            ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    int i = database.insertIcon(iconId,response);
                    Log.d("Log/test","result from iconloading: " + i);
                    broadcastIconResult(iconId);
                }
            }, 0, 0, null, null);
            queue.add(imageRequest);

        }
    }

    private void broadcastIconResult(String iconId) {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("ICON_RESULT");
        broadcastIntent.putExtra("NEW_ICON_DOWNLOADED",iconId);
        Log.d(TAG, "Broadcasting:" + "ICON_RESULT");
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
    }

    private void broadcastWeatherResult(WeatherInfo result){
        this.startIconAction(getApplicationContext(),result.getIcon());
        Gson gson = new Gson();
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("WEATHER_RESULT");
        broadcastIntent.putExtra(getString(R.string.weather_info_json),gson.toJson(result));
        Log.d(TAG, "Broadcasting:" + result);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Intent service destroyed");
        super.onDestroy();
    }


}
