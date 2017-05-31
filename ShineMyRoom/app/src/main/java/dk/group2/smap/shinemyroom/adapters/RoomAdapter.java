package dk.group2.smap.shinemyroom.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
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
import dk.group2.smap.shinemyroom.generated.Room;

//https://developer.android.com/training/improving-layouts/smooth-scrolling.html
public class RoomAdapter extends ArrayAdapter<Room> {
    private boolean sleeping;
    private ArrayList<Room> rooms;
    private Context c;
    public RoomAdapter(Context c, ArrayList<Room> rooms){
        super(c, R.layout.item_room_list,rooms);
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
        protected Stack<Integer> stack;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            LayoutInflater inflator = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflator.inflate(R.layout.item_room_list, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.roomName = (TextView) view.findViewById(R.id.room_name_list_item);
            viewHolder.lightSwitch = (Switch) view.findViewById(R.id.room_switch_list_item);
            ColorFilter colorFilter = new ColorFilter();
            viewHolder.lightSwitch.getThumbDrawable().setColorFilter(0xf4b642, PorterDuff.Mode.SRC_ATOP);
            viewHolder.lightStrength = (SeekBar) view.findViewById(R.id.light_strenght_list_item);
            viewHolder.stack = new Stack<>();
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

            viewHolder.lightStrength.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                    if(!fromUser)
                        return;

                    viewHolder.stack.push(progress);
                    // limiting calls to bridge
                    if(sleeping)
                        return;

                    Thread myThread = new Thread(){
                        @Override
                        public void run() {
                            try {
                                sleeping = true;
                                sleep(Global.BRIDGE_COOLDOWN);
                                sleeping = false;
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    myThread.start();

                    Integer pop = viewHolder.stack.pop();
                    Room element = (Room) viewHolder.lightSwitch.getTag();
                    PHBridge selectedBridge = PHHueSDK.getInstance().getSelectedBridge();
                    PHLightState phLightState = new PHLightState();
                    phLightState.setBrightness(pop);
                    selectedBridge.setLightStateForGroup(element.getPhGroup().getIdentifier(),phLightState);
                    viewHolder.stack.removeAllElements();
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
        ArrayList<PHLight> lights = rooms.get(position).getLights();

        Integer brightness = Global.LightStrengthMAX;
        for (PHLight light : lights){
            brightness = light.getLastKnownLightState().getBrightness() < brightness ? light.getLastKnownLightState().getBrightness() : brightness;
        }
        holder.lightStrength.setProgress(brightness);
        return view;
    }
}


