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

    IHueControl hueControl;
    ListView lw;
    RoomAdapter roomAdapter;
    @Override
    public void onStart() {
        Log.d("LOG/Fragment","RoomListFragment started");
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
        List<PHGroup> roomlist = resourceCache.getAllGroups();
        Map<String, PHLight> lightsMap = resourceCache.getLights();

        final ArrayList<Room> rooms = new ArrayList<>();
        for (int i = 0; i <roomlist.size(); i++) {
            if(roomlist.get(i).getLightIdentifiers().size() != 0)
            {
                Room room = new Room();
                List<String> lightIdentifiers = roomlist.get(i).getLightIdentifiers();
                ArrayList<PHLight> lights = new ArrayList<>();
                for (String key : lightIdentifiers){
                    lights.add(lightsMap.get(key));
                }
                room.setLights(lights);
                room.setPhGroup(roomlist.get(i));
                rooms.add(room);

                //just to allococate in compile time
//                final Room[] room = new Room[1];
//                room[0] = new Room();
//                room[0].setPhGroup(roomlist.get(i));
//
//                hueControl.getGroupDetails(Integer.parseInt(rooms.get(i).getPhGroup().getIdentifier()), new IHueControl.onGroupResponseListener() {
//                    @Override
//                    public void onGroupResult(String response) {
//                        //GroupDetails roomDetail = new Gson().fromJson(response, GroupDetails.class);
//                        Log.d("LOG","it got result:" + response);
////                        if(room[0] != null)
////                        {
////                            room[0].setDetails(roomDetail);
////                            if(roomAdapter != null)
////                                notifyAdapter();
////                        }
////                        else Log.d("LOG","something happened in trying to create a list to adapter");
//                    }
//                });
            }
        }

        Log.d("LOG/Fragment", "inflating lw with "+ rooms.size() +" items");
        // Inflate the layout for this fragment
        roomAdapter = new RoomAdapter(getActivity().getApplicationContext(),rooms);
        lw.setAdapter(roomAdapter);
        return view;

    }
    private void notifyAdapter(){
        roomAdapter.notifyDataSetChanged();
    }
//Fundet https://stackoverflow.com/questions/6672066/fragment-inside-fragment
//    public void OnViewCreated(View view, Bundle savedInstanceState){

//        Fragment roomListFrag = new RoomListItem();
//        FragmentTransaction roomItemTrans = getChildFragmentManager().beginTransaction();
//        roomItemTrans.add(R.id.roomListView, roomListFrag).commit();

}


