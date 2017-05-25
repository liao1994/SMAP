package dk.group2.smap.shinemyroom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.philips.lighting.model.PHGroup;

import java.util.ArrayList;
import java.util.List;

import dk.group2.smap.shinemyroom.generated.Room;

/**
 * Created by liao on 25-05-2017.
 */

public class RoomAdapter extends BaseAdapter {
    private ArrayList<PHGroup> lightGroups;
    private Context c;
    private LayoutInflater layoutInflater;


    public RoomAdapter(Context c, ArrayList<PHGroup> lightGroups){
        this.c = c;
        this.lightGroups = lightGroups;
    }
    @Override
    public int getCount() {
        if(lightGroups != null){
            return lightGroups.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(lightGroups!=null) {
            return lightGroups.get(position);
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
        if (convertView == null){
            layoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = layoutInflater.inflate(R.layout.room_list_item, null);//set layout for displaying items
        }

        TextView tv = (TextView) convertView.findViewById(R.id.room_name_list_item);
        tv.setText(lightGroups.get(position).getName());
        return convertView;
    }
}
