package dk.group2.smap.shinemyroom.fragments;

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

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.fragment_edit_room, container, false);
            lw = (ListView) view.findViewById(R.id.edit_room_list_view);

            PHBridgeResourcesCache resourceCache = PHHueSDK.getInstance().getSelectedBridge().getResourceCache();
            List<PHGroup> allGroups = resourceCache.getAllGroups();
            return view;
        }
}
