package dk.group2.smap.assigment2;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.SystemClock;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.BaseMenuPresenter;
import android.text.Layout;
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
import java.util.UUID;

import dk.group2.smap.assigment2.generatedfiles.Weather;

import static dk.group2.smap.assigment2.Global.ICON_API_CALL;

public class MainActivity extends AppCompatActivity {

    WeatherDatabase wDB;
    FloatingActionButton refreshBtn;
    ArrayList<WeatherInfo> winfol;
    WeatherInfo currentWeather;
    IconDatabaseHelper iconDB;
    WeatherAdapter weatherAdapter;
    AlertDialog.Builder dialog;

    public void refresh (){
        WeatherService.startAction(this);

          ImageView imgv = (ImageView)findViewById(R.id.currentIcon);
          Bitmap currentbitmap = iconDB.getIcon(currentWeather.getIcon());
           if(currentbitmap != null)
            imgv.setImageBitmap(currentbitmap);
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
                //winfol.add(new WeatherInfo(UUID.randomUUID(),"", "",0,""));
            Toast.makeText(this,R.string.no_connection, Toast.LENGTH_LONG).show();
        }

        if(winfol.size() != 0 && winfol != null)
            setUpdate();



    }

    private void setUpdate() {
        if(currentWeather == null){
            currentWeather = winfol.get(0);
            winfol.remove(0);
        }

        ImageView imgv = (ImageView)findViewById(R.id.currentIcon);
        Bitmap currentbitmap = iconDB.getIcon(currentWeather.getIcon());
        if(currentbitmap != null)
            imgv.setImageBitmap(currentbitmap);
        TextView currentInfo = (TextView) findViewById(R.id.tv_currentInfo);
        currentInfo.setText(currentWeather.getMain());
        TextView currentTemp = (TextView) findViewById(R.id.tv_currentDegrees);
        currentTemp.setText(String.format( "%.2f", currentWeather.getTemp()) + R.string.degrees +(char) 0x00B0 );
        TextView currentDescription = (TextView) findViewById(R.id.tv_currentDescription);
        currentDescription.setText(currentWeather.getDescription());

        weatherAdapter = new WeatherAdapter(this, winfol,iconDB);
        ListView lw = (ListView) findViewById(R.id.listWeather);
        lw.setAdapter(weatherAdapter);

        lw.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WeatherInfo obj = winfol.get(position);
                dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle(R.string.WeDe);
                dialog.setCancelable(true);
                ConstraintLayout viewById = (ConstraintLayout) MainActivity.this.getLayoutInflater().inflate(R.layout.dialog_box,null);
                TextView main = (TextView) viewById.findViewById(R.id.dialog_tv_main);
                TextView date = (TextView) viewById.findViewById(R.id.dialog_tv_date);
                TextView time = (TextView) viewById.findViewById(R.id.dialog_tv_time);
                TextView temp = (TextView) viewById.findViewById(R.id.dialog_tv_temp);
                TextView desp = (TextView) viewById.findViewById(R.id.dialog_tv_desp);
                ImageView img = (ImageView) viewById.findViewById(R.id.dialog_imgv_icon);
                main.setText(obj.getMain());
                String[] str = obj.getTimeStamp().split(" ");
                date.setText(str[0]);
                time.setText(str[1]);
                temp.setText(String.format( "%.2f", obj.getTemp()) + R.string.degrees +(char) 0x00B0);
                desp.setText(obj.getDescription());
                Bitmap tmp = iconDB.getIcon(obj.getIcon());
                if(tmp != null)
                 img.setImageBitmap(tmp);

                dialog.setView(viewById);

                dialog.create();
                dialog.show();
            }
        });
        Toast.makeText(this, R.string.refresh, Toast.LENGTH_SHORT).show();
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
               AlarmManager.INTERVAL_HALF_HOUR, pending);
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
                    break;
                case "ICON_RESULT":
                    weatherAdapter.notifyDataSetChanged();
                    break;
            }
            Toast.makeText(MainActivity.this, R.string.refresh_weather, Toast.LENGTH_SHORT).show();

        }
    };


}
