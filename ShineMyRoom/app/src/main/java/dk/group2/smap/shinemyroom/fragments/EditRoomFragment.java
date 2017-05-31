package dk.group2.smap.shinemyroom.fragments;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class EditRoomFragment extends Fragment {
    ListView lw;
    View view;

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
        }
        return view;

    }
}

