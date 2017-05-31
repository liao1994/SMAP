package dk.group2.smap.shinemyroom.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHBridgeResourcesCache;

import com.philips.lighting.model.PHLight;


import java.util.List;

import dk.group2.smap.shinemyroom.R;
import dk.group2.smap.shinemyroom.adapters.AddRoomAdapter;

public class AddRoomActivity extends AppCompatActivity {
    ListView lw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);

        lw = (ListView)findViewById(R.id.add_room_list);

        PHBridge phbridge = PHHueSDK.getInstance().getSelectedBridge();
        PHBridgeResourcesCache resourceCache = phbridge.getResourceCache();
        List<PHLight> allLights = resourceCache.getAllLights();

        AddRoomAdapter addRoomAdapter = new AddRoomAdapter(this, allLights);
        lw.setAdapter(addRoomAdapter);



        Button saveBtn = (Button)findViewById(R.id.room_add_saveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        Button cancelBtn = (Button)findViewById(R.id.room_add_cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
