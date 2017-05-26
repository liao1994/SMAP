package dk.group2.smap.shinemyroom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.Gson;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHGroup;
import com.philips.lighting.model.PHLight;

import java.util.ArrayList;
import java.util.List;

import dk.group2.smap.shinemyroom.generated.GroupDetails;
import dk.group2.smap.shinemyroom.generated.Room;




public class RoomAdapter extends BaseAdapter {
    private ArrayList<Room> rooms;
    private Context c;
    private LayoutInflater layoutInflater;

    public RoomAdapter(Context c, ArrayList<Room> rooms){
        this.c = c;
        this.rooms = rooms;
    }

    @Override
    public int getCount() {
        if(rooms != null){
            return rooms.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(rooms!=null) {
            return rooms.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        layoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.room_list_item, null);//set layout for displaying items

        TextView tv = (TextView) convertView.findViewById(R.id.room_name_list_item);
        Switch sw = (Switch) convertView.findViewById(R.id.room_switch_list_item);
        ArrayList<PHLight> lights = rooms.get(position).getLights();
        Boolean grpState = false;
        for (PHLight light: lights) {
            if(light.getLastKnownLightState().isOn() && !grpState)
            {
                // if any light is on grpstate is true
                    grpState = true;
            }
        }
        sw.setChecked(grpState);
        tv.setText(rooms.get(position).getPhGroup().getName());

//        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                PHBridge selectedBridge = PHHueSDK.getInstance().getSelectedBridge();
//                selectedBridge.setLightStateForGroup()
//            }
//        });
        return convertView;
    }

}
