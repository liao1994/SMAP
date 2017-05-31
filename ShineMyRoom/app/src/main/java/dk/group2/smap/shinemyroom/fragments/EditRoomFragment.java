package dk.group2.smap.shinemyroom.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHBridgeResourcesCache;
import com.philips.lighting.model.PHGroup;
import com.philips.lighting.model.PHLight;

import java.util.List;
import java.util.Map;

import dk.group2.smap.shinemyroom.R;
import dk.group2.smap.shinemyroom.activities.AddRoomActivity;
import dk.group2.smap.shinemyroom.activities.LampActivity;
import dk.group2.smap.shinemyroom.adapters.EditRoomAdapter;
import dk.group2.smap.shinemyroom.adapters.RoomClickedAdapter;


public class EditRoomFragment extends Fragment {
    ListView lw;
    View view;

    PHBridge phbridge = PHHueSDK.getInstance().getSelectedBridge();
    PHBridgeResourcesCache resourceCache = phbridge.getResourceCache();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        WifiManager wifi = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (wifi.isWifiEnabled() == false) {
            // Inflate the layout for fragment no wificonnection
            view = inflater.inflate(R.layout.fragment_edit_room_no_wifi, container, false);
        }
        else {
            // Inflate the layout for fragment editroom fragment
            view = inflater.inflate(R.layout.fragment_edit_room_list, container, false);
//            Map<String, PHGroup> groups = resourceCache.getGroups();
//            EditRoomAdapter editRoomAdapter = new EditRoomAdapter(getActivity().getApplicationContext(), groups);
//            lw.setAdapter(editRoomAdapter);
//            lw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    Intent intentaddroom = new Intent(getContext(), AddRoomActivity.class);
//                    startActivity(intentaddroom);
//                }
//            });
        }
        return view;

    }
}

