package dk.group2.smap.shinemyroom;

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
import com.philips.lighting.model.PHLightState;

import java.util.ArrayList;

import dk.group2.smap.shinemyroom.generated.Room;




public class RoomAdapter extends ArrayAdapter<Room> {
    private ArrayList<Room> rooms;
    private Context c;

    public RoomAdapter(Context c, ArrayList<Room> rooms){
        super(c,R.layout.room_list_item,rooms);
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
    public Room getItem(int position) {
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
    static class ViewHolder {
        protected TextView roomName;
        protected Switch lightSwitch;
        protected SeekBar lightStrength;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            LayoutInflater inflator = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflator.inflate(R.layout.room_list_item, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.roomName = (TextView) view.findViewById(R.id.room_name_list_item);
            viewHolder.lightSwitch = (Switch) view.findViewById(R.id.room_switch_list_item);
            viewHolder.lightSwitch.setTag(rooms.get(position));
            viewHolder.lightSwitch
                    .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(CompoundButton buttonView,
                                                     boolean isChecked) {
                            Room element = (Room) viewHolder.lightSwitch.getTag();
                            element.setOn(isChecked);
                            PHBridge selectedBridge = PHHueSDK.getInstance().getSelectedBridge();
                            PHLightState phLightState = new PHLightState();
                            phLightState.setOn(isChecked);
                            selectedBridge.setLightStateForGroup(element.getPhGroup().getIdentifier(),phLightState);
                        }
                    });
            viewHolder.lightStrength = (SeekBar) view.findViewById(R.id.light_strenght_list_item);
            viewHolder.lightStrength.setTag(rooms.get(position));
            viewHolder.lightStrength.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    Room element = (Room) viewHolder.lightSwitch.getTag();
                    PHBridge selectedBridge = PHHueSDK.getInstance().getSelectedBridge();
                    PHLightState phLightState = new PHLightState();
                    phLightState.setBrightness(progress);
                    selectedBridge.setLightStateForGroup(element.getPhGroup().getIdentifier(),phLightState);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });

            view.setTag(viewHolder);

        } else {
            view = convertView;
            ((ViewHolder) view.getTag()).lightSwitch.setTag(rooms.get(position));
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.lightStrength.setMax(Global.LightStrengthMAX);
        holder.roomName.setText(rooms.get(position).getPhGroup().getName());
        holder.lightSwitch.setChecked(rooms.get(position).getOn());
        return view;
    }
}


