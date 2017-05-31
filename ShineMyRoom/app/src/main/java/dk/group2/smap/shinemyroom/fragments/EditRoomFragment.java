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

import dk.group2.smap.shinemyroom.R;
import dk.group2.smap.shinemyroom.activities.AddRoomActivity;
import dk.group2.smap.shinemyroom.activities.LampActivity;
import dk.group2.smap.shinemyroom.adapters.EditRoomAdapter;
import dk.group2.smap.shinemyroom.adapters.RoomClickedAdapter;
import dk.group2.smap.shinemyroom.generated.Room;


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
            PHBridge phbridge = PHHueSDK.getInstance().getSelectedBridge();
            PHBridgeResourcesCache resourceCache = phbridge.getResourceCache();

            // Inflate the layout for fragment editroom fragment
            view = inflater.inflate(R.layout.fragment_edit_room_list, container, false);
            lw = (ListView)view.findViewById(R.id.edit_room_list_view) ;

            List<PHGroup> roomlist = resourceCache.getAllGroups();
            final Map<String, PHLight> lightsMap = resourceCache.getLights();

            final ArrayList<Room> rooms = new ArrayList<>();

            for (int i = 0; i <roomlist.size(); i++) {
                if(roomlist.get(i).getLightIdentifiers().size() != 0 && !Objects.equals(roomlist.get(i).getName(),"Custom group for $group"))
                {
                    List<String> lightIdentifiers = roomlist.get(i).getLightIdentifiers();
                    ArrayList<PHLight> lights = new ArrayList<>();
                    for (String key : lightIdentifiers){
                        lights.add(lightsMap.get(key));
                    }
                    Room room = new Room(roomlist.get(i),lights);
                    rooms.add(room);
                }
            }
            EditRoomAdapter editRoomAdapter = new EditRoomAdapter(getActivity().getApplicationContext(), rooms);
            lw.setAdapter(editRoomAdapter);
            lw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intentaddroom = new Intent(getContext(), AddRoomActivity.class);
                    // More stuff


                    startActivity(intentaddroom);
                }
            });

            Button addBtn = (Button)view.findViewById(R.id.addRoomBtn);
            addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentaddroom = new Intent(getContext(), AddRoomActivity.class);
                    startActivity(intentaddroom);
                }
            });

        }
        return view;

    }
}

