package dk.group2.smap.shinemyroom.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.philips.lighting.model.PHGroup;

import java.util.Map;

import dk.group2.smap.shinemyroom.R;

public class EditRoomAdapter extends BaseAdapter{

    private final Context c;
    private final Map<String, PHGroup> groups;

    public EditRoomAdapter(Context c, Map<String, PHGroup> groups){

        this.c = c;
        this.groups = groups;
    }
    @Override
    public int getCount() {
        return groups.size();
    }

    @Override
    public Object getItem(int position) {
        return groups.get(position);
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
        TextView roomName = (TextView) convertView.findViewById(R.id.editRoomName);

        return convertView;
    }
}
