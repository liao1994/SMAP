package dk.group2.smap.assigment2;

import android.app.IntentService;
import android.content.Intent;
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
    RequestQueue queue;
    WeatherDatabase weatherDB;
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public WeatherService(String name) {
        super(name);
        weatherDB.openDB();
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String dataString = intent.getDataString();
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

                        WeatherInfo wi = new WeatherInfo(w.getId(),w.getDescription(),r.getMain().getTemp());
                        weatherDB.InsertWeatherInfo(wi);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error.API", error.getMessage());
            }
        });

        queue.add(stringRequest);

    }


}
