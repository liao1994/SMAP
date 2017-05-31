package dk.group2.smap.shinemyroom.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.philips.lighting.model.PHLight;

import java.util.ArrayList;

import dk.group2.smap.shinemyroom.R;

/**
 * Created by liao on 30-05-2017.
 */

public class RoomClickedAdapter extends BaseAdapter {

    private final Context c;
    private final ArrayList<PHLight> phLightArrayList;

    public RoomClickedAdapter(Context c, ArrayList<PHLight> phLightArrayList){

        this.c = c;
        this.phLightArrayList = phLightArrayList;
    }
    @Override
    public int getCount() {
        return phLightArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return phLightArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            LayoutInflater inflator = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflator.inflate(R.layout.item_room_clicked_lamp_list, null);
        }
        TextView lampName = (TextView) convertView.findViewById(R.id.room_clicked_lamp_name);
        TextView lampStatus = (TextView) convertView.findViewById(R.id.room_clicked_lamp_status);
        PHLight lamp = phLightArrayList.get(position);
        if(lamp.getLastKnownLightState().isReachable())
            lampStatus.setText("Reachable");
        else
            lampStatus.setText("Out of Reach");
        lampName.setText(lamp.getName());
        return convertView;
    }
}
