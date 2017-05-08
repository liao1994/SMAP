package dk.group2.smap.assigment2;

import android.app.IntentService;
import android.content.Intent;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


public class WeatherService extends IntentService {
    RequestQueue queue;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public WeatherService(String name) {
        super(name);
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
                        //TODO write to DB
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        queue.add(stringRequest);

    }
}
