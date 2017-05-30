package dk.group2.smap.shinemyroom.storage;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;

import java.util.ArrayList;
import java.util.Stack;

import dk.group2.smap.shinemyroom.Global;
import dk.group2.smap.shinemyroom.R;
import dk.group2.smap.shinemyroom.RoomAdapter;
import dk.group2.smap.shinemyroom.generated.Room;

public class RoomClickedAdapter extends ArrayAdapter<Room> {
    private boolean sleeping;
    private ArrayList<Room> editrooms;
    private Context c;

    public RoomClickedAdapter(Context c, ArrayList<Room> editrooms) {
        super(c, R.layout.item_edit_room_list, editrooms);
        this.c = c;
        this.editrooms = editrooms;
    }

    @Override
    public Room getItem(int position) {
        if (editrooms != null) {
            return editrooms.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            LayoutInflater inflator = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflator.inflate(R.layout.item_room_list, null);
        }
        TextView LampName = (TextView) convertView.findViewById(R.id.room_clicked_lamp_name);
        //LampName.setText();
        return convertView;
    }
}

