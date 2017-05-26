package dk.group2.smap.shinemyroom;

import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.internal.util.Predicate;
import com.google.gson.Gson;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHGroup;
import com.philips.lighting.model.PHLightState;

import java.util.ArrayList;
import java.util.List;

import dk.group2.smap.shinemyroom.generated.Room;


public class RoomListFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        PHBridge phbridge = PHHueSDK.getInstance().getSelectedBridge();
        List<PHGroup> roomlist = phbridge.getResourceCache().getAllGroups();
        //http://www.concretepage.com/java/jdk-8/java-8-list-example-with-foreach-removeif-replaceall-and-sort
        //Predicate<PHGroup> personPredicate = p-> p.getLightIdentifiers().size() == 0;
        ArrayList<PHGroup> list = new ArrayList<>();
        for (int i = 0; i <roomlist.size(); i++) {

            if(roomlist.get(i).getLightIdentifiers().size() != 0)
            {
                list.add(roomlist.get(i));
            }
        }
        Log.d("LOG/Fragment", "inflating lw with "+ list.size() +" items");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.room_list_fragment, container, false);
        ListView lw = (ListView) view.findViewById(R.id.room_listView);
        RoomAdapter roomAdapter = new RoomAdapter(getActivity().getApplicationContext(),list);
        lw.setAdapter(roomAdapter);
        return view;

    }
//Fundet https://stackoverflow.com/questions/6672066/fragment-inside-fragment
//    public void OnViewCreated(View view, Bundle savedInstanceState){

//        Fragment roomListFrag = new RoomListItem();
//        FragmentTransaction roomItemTrans = getChildFragmentManager().beginTransaction();
//        roomItemTrans.add(R.id.roomListView, roomListFrag).commit();

}


