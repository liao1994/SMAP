package dk.group2.smap.shinemyroom.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHBridgeResourcesCache;
import com.philips.lighting.model.PHLight;

import java.util.List;

import dk.group2.smap.shinemyroom.R;

public class RoomClickedActivity extends AppCompatActivity {

    PHBridge phbridge = PHHueSDK.getInstance().getSelectedBridge();
    PHBridgeResourcesCache resourceCache = phbridge.getResourceCache();

    List<PHLight> allLights = PHHueSDK.getInstance()
            .getSelectedBridge()
            .getResourceCache()
            .getAllLights();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_clicked);


    }
}
