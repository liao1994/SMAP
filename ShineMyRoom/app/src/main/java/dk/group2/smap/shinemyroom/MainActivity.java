package dk.group2.smap.shinemyroom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        //Intent mServiceIntent = new Intent(this, HueConnectionService.class);
        HueConnectionService.startAction(this);
        //this.startService(mServiceIntent);

    }
}
