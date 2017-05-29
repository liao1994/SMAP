package dk.group2.smap.shinemyroom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

import dk.group2.smap.shinemyroom.services.RemoteHueControlService;

/**
 * Created by liao on 28-05-2017.
 */

public class RemoteRoomAdapter extends ArrayAdapter<RemoteRoom> {
    private Context applicationContext;
    private ArrayList<RemoteRoom> remoteRooms;

    public RemoteRoomAdapter(Context applicationContext, ArrayList<RemoteRoom> remoteRoom) {
        super(applicationContext,R.layout.room_list_item,remoteRoom);
        this.applicationContext = applicationContext;
        this.remoteRooms = remoteRoom;
    }

    @Override
    public int getCount() {
        if(remoteRooms != null){
            return remoteRooms.size();
        }
        return 0;
    }

    @Override
    public RemoteRoom getItem(int position) {
        if(remoteRooms!=null) {
            return remoteRooms.get(position);
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
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            LayoutInflater inflator = (LayoutInflater) applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflator.inflate(R.layout.room_list_item, null);
            final RoomAdapter.ViewHolder viewHolder = new RoomAdapter.ViewHolder();
            viewHolder.roomName = (TextView) view.findViewById(R.id.room_name_list_item);
            viewHolder.lightSwitch = (Switch) view.findViewById(R.id.room_switch_list_item);
            viewHolder.lightSwitch.setTag(remoteRooms.get(position));
            viewHolder.lightSwitch
                    .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(CompoundButton buttonView,
                                                     boolean isChecked) {
                            //TODO handling turning on lights remotely
                            RemoteRoom element = (RemoteRoom) viewHolder.lightSwitch.getTag();
                            RemoteHueControlService.setLightForGroupAction(applicationContext,element.getGroup().getIdentifier(),isChecked);
                            //TODO should check for if remote call respond
                            element.setLightStateForGroup(isChecked);
//                            PHBridge selectedBridge = PHHueSDK.getInstance().getSelectedBridge();
//                            PHLightState phLightState = new PHLightState();
//                            phLightState.setOn(isChecked);
//                            selectedBridge.setLightStateForGroup(element.getPhGroup().getIdentifier(),phLightState);
                        }
                    });
            view.setTag(viewHolder);

        } else {
            view = convertView;
            ((RoomAdapter.ViewHolder) view.getTag()).lightSwitch.setTag(remoteRooms.get(position));
        }
        RoomAdapter.ViewHolder holder = (RoomAdapter.ViewHolder) view.getTag();
        holder.roomName.setText(remoteRooms.get(position).getGroup().getName());
        holder.lightSwitch.setChecked(remoteRooms.get(position).getLightStateForGroup());
        return view;
    }
}
