package dk.group2.smap.assigment2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_frag);
        startWeatherService();
    }

    private void startWeatherService() {
        Intent backgroundServiceIntent = new Intent(MainActivity.this, WeatherService.class);
        backgroundServiceIntent.putExtra("", "");
        startService(backgroundServiceIntent);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}
