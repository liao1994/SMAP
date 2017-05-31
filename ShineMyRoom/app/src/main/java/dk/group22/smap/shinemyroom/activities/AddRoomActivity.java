package dk.group22.smap.shinemyroom.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHBridgeResourcesCache;

import com.philips.lighting.model.PHGroup;
import com.philips.lighting.model.PHLight;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import dk.group22.smap.shinemyroom.R;
import dk.group22.smap.shinemyroom.adapters.AddRoomAdapter;

public class AddRoomActivity extends AppCompatActivity {
    ListView lw;
    TextView roomName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);

        Intent intent = getIntent();

        String groupId = intent.getStringExtra("groupId");
        roomName = (TextView) findViewById(R.id.editRoomLabel);
        //roomName.setText(phGroup.getName());

        lw = (ListView) findViewById(R.id.add_room_list);
        //https://www.developers.meethue.com/documentation/java-sdk-getting-started
        PHBridge phbridge = PHHueSDK.getInstance().getSelectedBridge();
        PHBridgeResourcesCache resourceCache = phbridge.getResourceCache();
        Map<String, PHLight> allLights = resourceCache.getLights();
        Map<String, PHGroup> groups = resourceCache.getGroups();
        PHGroup phGroup;
        if(!Objects.equals(groupId, ""))
            phGroup = groups.get(groupId);
        else
        {
            List<String> strings = new ArrayList<>();
            phGroup = new PHGroup();
            phGroup.setLightIdentifiers(strings);
        }
        //https://developer.android.com/reference/android/widget/Adapter.html
        final AddRoomAdapter addRoomAdapter = new AddRoomAdapter(this, phGroup, allLights);
        lw.setAdapter(addRoomAdapter);

        Button saveBtn = (Button) findViewById(R.id.room_add_saveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PHGroup roomFromAdapter = addRoomAdapter.getRoomFromAdapter();
                PHHueSDK.getInstance().getSelectedBridge().updateGroup(roomFromAdapter, null);
            }
        });
        Button cancelBtn = (Button) findViewById(R.id.room_add_cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}

