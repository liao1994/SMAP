package dk.group2.smap.shinemyroom.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHBridgeResourcesCache;
import com.philips.lighting.model.PHGroup;
import com.philips.lighting.model.PHLight;

import java.util.ArrayList;
import java.util.Map;

import dk.group2.smap.shinemyroom.R;
import dk.group2.smap.shinemyroom.adapters.RoomClickedAdapter;

public class RoomClickedActivity extends AppCompatActivity {


    private static final String TAG = "LOG/" + RoomClickedActivity.class.getName();

    TextView roomTextView;
    ListView lw;
    Switch sw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,"onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_clicked);
        //init
        lw = (ListView)findViewById(R.id.room_click_listview);
        roomTextView = (TextView) findViewById(R.id.room_clicked_name);
        sw = (Switch) findViewById(R.id.room_clicked_lamp_switch);
        //data from intent
        final Intent intent = getIntent();
        String roomIdToClickedActivity = intent.getStringExtra("roomIdToClickedActivity");
        String roomNameToClickedActivity = intent.getStringExtra("roomNameToClickedActivity");
        ArrayList<String> lightIdArrayListToClickedActivity = intent.getStringArrayListExtra("lightIdArrayListToClickedActivity");

        //data from brigde
        ArrayList<PHLight> allLights = new ArrayList<>();
        PHBridge phbridge = PHHueSDK.getInstance().getSelectedBridge();
        PHBridgeResourcesCache resourceCache = phbridge.getResourceCache();

        Map<String, PHLight> lights = resourceCache.getLights();
        Map<String, PHGroup> groups = resourceCache.getGroups();

       //----------------
        //our room
        PHGroup ourRoom = groups.get(roomIdToClickedActivity);

        ArrayList<PHLight> lightForRoom = new ArrayList<>();
        for (String str : lightIdArrayListToClickedActivity)
        {
            PHLight phLight = lights.get(str);
            if(phLight.getLastKnownLightState().isOn())
                sw.setChecked(true);
            lightForRoom.add(phLight);
        }
        RoomClickedAdapter roomClickedAdapter = new RoomClickedAdapter(this, lightForRoom);
        lw.setAdapter(roomClickedAdapter);
        lw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG,"startNewActivity");
                Intent intent1 = new Intent(getApplicationContext(), LampActivity.class);
                startActivity(intent1);
            }
        });


        //our keys
        //lightIdArrayListToClickedActivity
        //phGroup.getLightIdentifiers();



        //set


        roomTextView.setText(roomNameToClickedActivity);
    }
}
