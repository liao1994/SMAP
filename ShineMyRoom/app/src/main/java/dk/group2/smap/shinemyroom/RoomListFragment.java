package dk.group2.smap.shinemyroom;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class RoomListFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.room_list_fragment, container, false);
    }
//Fundet https://stackoverflow.com/questions/6672066/fragment-inside-fragment
    public void OnViewCreated(View view, Bundle savedInstanceState){
        Fragment roomListFrag = new RoomListItem();
        FragmentTransaction roomItemTrans = getChildFragmentManager().beginTransaction();
        roomItemTrans.add(R.id.roomListView, roomListFrag).commit();
    }
}

