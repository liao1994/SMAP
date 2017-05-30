package dk.group2.smap.shinemyroom.storage;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Stack;

import dk.group2.smap.shinemyroom.R;
import dk.group2.smap.shinemyroom.RoomAdapter;
import dk.group2.smap.shinemyroom.generated.Room;

public class RoomClickedAdapter extends ArrayAdapter<Room> {
    private boolean sleeping;
    private ArrayList<Room> rooms;
    private Context c;

    public RoomClickedAdapter(Context c, ArrayList<Room> rooms) {
        super(c, R.layout.item_room_list, rooms);
        this.c = c;
        this.rooms = rooms;
    }

    @Override
    public Room getItem(int position) {
        if (rooms != null) {
            return rooms.get(position);
        } else {
            return null;
        }
    }
}
