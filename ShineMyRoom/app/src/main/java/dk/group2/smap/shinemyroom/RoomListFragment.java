package dk.group2.smap.shinemyroom;

import android.app.Fragment;
import android.app.ListFragment;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.internal.util.Predicate;
import com.google.gson.Gson;
import com.philips.lighting.hue.listener.PHHTTPListener;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHBridgeResourcesCache;
import com.philips.lighting.model.PHGroup;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dk.group2.smap.shinemyroom.generated.GroupDetails;
import dk.group2.smap.shinemyroom.generated.Room;

import static android.R.id.list;


public class RoomListFragment extends Fragment {
    private static final String TAG = "LOG/" + RoomListFragment.class.getName();
    IHueControl hueControl;
    ListView lw;
    RoomAdapter roomAdapter;
    @Override
    public void onStart() {
        Log.d(TAG,"RoomListFragment started");
        super.onStart();
        hueControl = new LocalHueControl(getActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.room_list_fragment, container, false);
        lw = (ListView) view.findViewById(R.id.room_listView);

        PHBridge phbridge = PHHueSDK.getInstance().getSelectedBridge();
        PHBridgeResourcesCache resourceCache = phbridge.getResourceCache();
        List<PHGroup> roomList = resourceCache.getAllGroups();
        Map<String, PHLight> lightsMap = resourceCache.getLights();

        ArrayList<Room> filteredRooms = new ArrayList<>();
        for (int i = 0; i <roomList.size(); i++) {
            if(roomList.get(i).getLightIdentifiers().size() != 0)
            {
                List<String> lightIdentifiers = roomList.get(i).getLightIdentifiers();
                ArrayList<PHLight> lights = new ArrayList<>();
                for (String key : lightIdentifiers){
                    lights.add(lightsMap.get(key));
                }

                filteredRooms.add(new Room(roomList.get(i),lights));
            }
        }

        Log.d(TAG, "inflating lw with "+ filteredRooms.size() +" items");
        // Inflate the layout for this fragment
        roomAdapter = new RoomAdapter(getActivity().getApplicationContext(),filteredRooms);
        lw.setAdapter(roomAdapter);
        return view;

    }
}


