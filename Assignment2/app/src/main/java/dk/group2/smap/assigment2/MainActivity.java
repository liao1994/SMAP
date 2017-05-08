package dk.group2.smap.assigment2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import dk.group2.smap.assigment2.generatedfiles.Weather;

public class MainActivity extends AppCompatActivity {

    WeatherDatabase wDB;
    FloatingActionButton refreshBtn;
    public void yo (){
        Intent mServiceIntent = new Intent(this, WeatherService.class);
        getApplicationContext().startService(mServiceIntent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent mServiceIntent = new Intent(this, WeatherService.class);
        this.startService(mServiceIntent);//

        refreshBtn = (FloatingActionButton) findViewById(R.id.refreshBtn);
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yo();
                Toast.makeText(MainActivity.this,"Updating Weather Info", Toast.LENGTH_SHORT).show();
            }
        });


        ArrayList<WeatherInfo> winfol;
        wDB = new WeatherDatabase(this);
        winfol = wDB.getWeatherInfoList();


        WeatherAdapter weatherAdapter = new WeatherAdapter(this, winfol);
        ListView lw = (ListView) findViewById(R.id.listWeather);
        lw.setAdapter(weatherAdapter);

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

    private void startWeatherService() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent backgroundServiceIntent = new Intent(MainActivity.this, WeatherService.class);
        PendingIntent pending = PendingIntent.getService(this, 0, backgroundServiceIntent, 0);
        alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_HALF_HOUR,
                pending);
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
            Log.d("LOG", "Broadcast reveiced from bg service");
            Toast.makeText(MainActivity.this, "Got result from service:\n", Toast.LENGTH_SHORT).show();

        }
    };

}
