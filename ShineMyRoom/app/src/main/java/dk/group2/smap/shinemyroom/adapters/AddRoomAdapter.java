package dk.group2.smap.shinemyroom.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.philips.lighting.model.PHLight;

import java.util.ArrayList;
import java.util.List;

import dk.group2.smap.shinemyroom.R;
import dk.group2.smap.shinemyroom.generated.Light;

public class AddRoomAdapter extends BaseAdapter{
    private final Context c;
    private final List<PHLight> lights;

    public AddRoomAdapter(Context c, List<PHLight> lights){

        this.c = c;
        this.lights = lights;
    }
    @Override
    public int getCount() {
        return lights.size();
    }

    @Override
    public Object getItem(int position) {
        return lights.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            LayoutInflater inflator = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflator.inflate(R.layout.item_add_room_list, null);
        }

        TextView lampName = (TextView) convertView.findViewById(R.id.add_room_lamp_name);
        lampName.setText(lights.get(position).getName());

        return convertView;
    }
}
