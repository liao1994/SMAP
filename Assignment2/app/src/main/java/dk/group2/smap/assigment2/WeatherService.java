package dk.group2.smap.assigment2;
import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import dk.group2.smap.assigment2.generatedfiles.RootObject;
import dk.group2.smap.assigment2.generatedfiles.Weather;


public class WeatherService extends IntentService {
    WeatherDatabase weatherDB;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public WeatherService(String name) {
        super(name);
        Log.d("LOG", "Background service onCreate");

    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        sendRequest();
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

                        WeatherInfo wi = new WeatherInfo(w.getId(),w.getDescription(),r.getMain().getTemp(),w.getIcon());
                        weatherDB.InsertWeatherInfo(wi);
                        broadcastTaskResult(wi);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error.API", error.getMessage());
            }
        });

        queue.add(stringRequest);

    }
    private void broadcastTaskResult(WeatherInfo result){
        Gson gson = new Gson();
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("WEATHER_RESULT");
        broadcastIntent.putExtra(getString(R.string.weather_info_json),gson.toJson(result));
        Log.d("LOG", "Broadcasting:" + result);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
    }

    @Override
    public void onDestroy() {
        Log.d("LOG", "Intent service destroyed");
        super.onDestroy();
    }


}
