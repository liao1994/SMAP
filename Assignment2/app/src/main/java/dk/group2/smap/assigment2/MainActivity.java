package dk.group2.smap.assigment2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;

import dk.group2.smap.assigment2.generatedfiles.Weather;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startWeatherService();

        final ArrayList<WeatherInfo> _weatherinfo = new ArrayList<>();
        _weatherinfo.add(new WeatherInfo(1, "Sunny", 20.2, "Test"));
        _weatherinfo.add(new WeatherInfo(2, "Rain", 10, "Test2"));
        _weatherinfo.add(new WeatherInfo(3, "Snow", -5, "Test3"));

        WeatherAdapter weatherAdapter = new WeatherAdapter(this, _weatherinfo);
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

    }
}
