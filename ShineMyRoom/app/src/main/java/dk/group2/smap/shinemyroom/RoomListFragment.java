package dk.group2.smap.shinemyroom;

import android.app.Fragment;
import android.app.ListFragment;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
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
import java.util.Objects;

import dk.group2.smap.shinemyroom.generated.GroupDetails;
import dk.group2.smap.shinemyroom.generated.Room;

import static android.R.id.list;
import static android.app.Activity.RESULT_OK;


public class RoomListFragment extends Fragment {

    private static final String TAG = "LOG/" + RoomListFragment.class.getName();
    ListView lw;
    RoomAdapter roomAdapter;
    @Override
    public void onStart() {
        Log.d("LOG/Fragment","RoomListFragment started");
        super.onStart();
    }
    // get phbrigde, take the info we need, create a list, inflate adapter
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.room_list_fragment, container, false);
        lw = (ListView) view.findViewById(R.id.room_listView);
        PHBridge phbridge = PHHueSDK.getInstance().getSelectedBridge();
        PHBridgeResourcesCache resourceCache = phbridge.getResourceCache();
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

        Log.d("LOG/Fragment", "inflating lw with "+ rooms.size() +" items");
        // Inflate the layout for this fragment
        roomAdapter = new RoomAdapter(getActivity().getApplicationContext(),rooms);
        lw.setAdapter(roomAdapter);

        lw.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "starting new activity: " + LampActivity.class.getName());
                Intent intent = new Intent(view.getContext(), LampActivity.class);
            }
        });
        return view;

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == Global.LAMP_ACTIVITY_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                //TODO something
            }
        }
    }


}


