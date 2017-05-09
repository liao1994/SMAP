package dk.group2.smap.assigment2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.SystemClock;
import android.support.v4.content.LocalBroadcastManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;

import dk.group2.smap.assigment2.generatedfiles.Weather;

import static dk.group2.smap.assigment2.Global.ICON_API_CALL;

public class MainActivity extends AppCompatActivity {

    WeatherDatabase wDB;
    FloatingActionButton refreshBtn;
    ArrayList<WeatherInfo> winfol;
    WeatherInfo currentWeather;
    IconDatabaseHelper iconDB;

    public void refresh (){
        WeatherService.startAction(this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iconDB = new IconDatabaseHelper(this);
        wDB = new WeatherDatabase(this);
        startWeatherService();
        refreshBtn = (FloatingActionButton) findViewById(R.id.refreshBtn);
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();

            }
        });

        winfol = wDB.getWeatherInfoList();
        if (winfol == null){
                winfol.add(new WeatherInfo(1,"", "",0,""));
            Toast.makeText(this,"Could not get Weather from DB", Toast.LENGTH_LONG).show();
        }

        if(winfol.size() != 0)
            setUpdate();



//        lw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                Intent startDemoIntent = new Intent();
//                String action = demos.get(position).getIntentAction();
//                int resultCode = demos.get(position).getResultCode();
//                if(action != null && !action.equals("")){
//                    startDemoIntent.setAction(action);
//                    startActivityForResult(startDemoIntent,resultCode);
//                }
//            }
//        });
    }

    private void setUpdate() {
        if(currentWeather == null){
            currentWeather = winfol.get(0);
            winfol.remove(0);
        }
        ImageView imgv = (ImageView)findViewById(R.id.currentIcon);

//        Bitmap currentbitmap = iconDB.getIcon(currentWeather.getIcon());
//        if(currentbitmap != null)
//        imgv.setImageBitmap(currentbitmap);

        TextView currentInfo = (TextView) findViewById(R.id.tv_currentInfo);
        currentInfo.setText(currentWeather.getMain());
        TextView currentTemp = (TextView) findViewById(R.id.tv_currentDegrees);
        currentTemp.setText(String.format( "%.2f", currentWeather.getTemp()) + "C" +(char) 0x00B0 );
        TextView currentDescription = (TextView) findViewById(R.id.tv_currentDescription);
        currentDescription.setText(currentWeather.getDescription());

        WeatherAdapter weatherAdapter = new WeatherAdapter(this, winfol,iconDB);
        ListView lw = (ListView) findViewById(R.id.listWeather);
        lw.setAdapter(weatherAdapter);
        Toast.makeText(this, "refreshing", Toast.LENGTH_SHORT).show();
    }

    private void startWeatherService() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 6);
        calendar.set(Calendar.MINUTE, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent backgroundServiceIntent = new Intent(MainActivity.this, WeatherService.class);
        backgroundServiceIntent.setAction(WeatherService.ACTION_WEATHER);
        PendingIntent pending = PendingIntent.getService(this, 0, backgroundServiceIntent, 0);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                1000 * 60 * 30, pending);
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction("WEATHER_RESULT");
        LocalBroadcastManager.getInstance(this).registerReceiver(onBackgroundServiceResult,filter);

    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(onBackgroundServiceResult);
        wDB.close();
    }

    private BroadcastReceiver onBackgroundServiceResult = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("LOG", "Broadcast received from service");
            switch (intent.getAction()) {
                case "WEATHER_RESULT":
                    Gson gson = new Gson();
                    WeatherInfo tmp = gson.fromJson(intent.getStringExtra("WEATHER_INFO_JSON"), WeatherInfo.class);
                    if (currentWeather.getId() != tmp.getId())
                    {
                        winfol.add(0, currentWeather);
                        currentWeather = tmp;
                        setUpdate();
                    }


            }
            Toast.makeText(MainActivity.this, "Refreshed Weather:\n", Toast.LENGTH_SHORT).show();

        }
    };


}
