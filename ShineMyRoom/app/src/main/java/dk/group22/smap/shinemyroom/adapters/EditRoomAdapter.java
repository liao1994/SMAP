package dk.group22.smap.shinemyroom.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import dk.group2.smap.shinemyroom.R;
import dk.group22.smap.shinemyroom.generated.Room;

public class EditRoomAdapter extends BaseAdapter{

    private final Context c;
    private final ArrayList<Room> rooms;

    public EditRoomAdapter(Context c, ArrayList<Room> rooms){

        this.c = c;
        this.rooms = rooms;
    }
    @Override
    public int getCount() {
        return rooms.size();
    }

    @Override
    public Object getItem(int position) {
        return rooms.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            LayoutInflater inflator = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflator.inflate(R.layout.item_edit_room_list, null);
        }

        TextView roomName = (TextView) convertView.findViewById(R.id.room_name_list_item);
        roomName.setText(rooms.get(position).getPhGroup().getName());

        return convertView;
    }
}
