package dk.group2.smap.shinemyroom;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.philips.lighting.model.PHLightState;

import java.util.ArrayList;
import java.util.List;

import dk.group2.smap.shinemyroom.generated.GroupDetails;
import dk.group2.smap.shinemyroom.generated.Room;



public class RoomAdapter extends BaseAdapter {
    //private static String TAG = "LOG/" + RoomAdapter.class.getName();
    private ArrayList<Room> rooms;
    private Context c;

    public RoomAdapter(Context c, ArrayList<Room> rooms){
        Log.d("test","hmm");
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

    static class RoomViewHolder {
        TextView room_name;
        Switch light_switch;
    }
    // ViewHolder
    // https://developer.android.com/training/improving-layouts/smooth-scrolling.html
    // http://www.vogella.com/tutorials/AndroidListView/article.html#listviewselection
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //ViewHolder pattern
        View view;
        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.room_list_item, null);
            final RoomViewHolder viewHolder = new RoomViewHolder();
            viewHolder.room_name = (TextView) view.findViewById(R.id.room_name_list_item);
            viewHolder.light_switch = (Switch) view.findViewById(R.id.room_switch_list_item);
            viewHolder.light_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    PHBridge selectedBridge = PHHueSDK.getInstance().getSelectedBridge();
                    PHLightState phLightState = new PHLightState();
                    phLightState.setOn(isChecked);
                    Room element = (Room) viewHolder.light_switch.getTag();

                    selectedBridge.setLightStateForGroup(element.getPhGroup().getName(),phLightState);

                    //Log.d(TAG,"light switched event: Room Name" + element.getPhGroup().getName() +"|On: "+ phLightState.isOn());

                }
            });
            view.setTag(viewHolder);
            viewHolder.light_switch.setTag(rooms.get(position));
        }else{
            view = convertView;
            ((RoomViewHolder) convertView.getTag()).light_switch.setTag(rooms.get(position));
        }
        RoomViewHolder holder = (RoomViewHolder) view.getTag();
        holder.room_name.setText(rooms.get(position).getPhGroup().getName());
        holder.light_switch.setChecked(rooms.get(position).getOn());
        return view;
    }
}
