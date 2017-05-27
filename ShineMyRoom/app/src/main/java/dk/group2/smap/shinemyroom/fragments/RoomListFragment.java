package dk.group2.smap.shinemyroom.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHBridgeResourcesCache;
import com.philips.lighting.model.PHGroup;
import com.philips.lighting.model.PHLight;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import dk.group2.smap.shinemyroom.Global;
import dk.group2.smap.shinemyroom.R;
import dk.group2.smap.shinemyroom.RoomAdapter;
import dk.group2.smap.shinemyroom.activities.LampActivity;
import dk.group2.smap.shinemyroom.generated.Room;
import dk.group2.smap.shinemyroom.storage.HueSharedPreferences;

import static android.app.Activity.RESULT_OK;


public class RoomListFragment extends Fragment {

    private static final String ERR_TAG = "ERROR/" + RoomListFragment.class.getName();
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
        ConnectivityManager connManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI)
                createWifiView();
            else
                try {
                    return createRemoteView(view);
                } catch (JSONException e) {
                    Log.d(ERR_TAG, "error while creating remoteview see stackTrace");
                    e.printStackTrace();
                }
        }
        return view;

    }

    private View createRemoteView(View view) throws JSONException {
        HueSharedPreferences prefs = HueSharedPreferences.getInstance(getActivity().getApplicationContext());
        String remoteBridge = prefs.getRemoteBridge();
        JSONObject reader = new JSONObject(remoteBridge);
        JSONObject lights = reader.getJSONObject("lights");
        JSONObject groups = reader.getJSONObject("groups");
        //TODO SOME MAGIC JSON WORK
        return view;
    }


    private View createWifiView() {

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


