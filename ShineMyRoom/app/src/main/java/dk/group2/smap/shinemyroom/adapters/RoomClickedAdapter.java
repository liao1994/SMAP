package dk.group2.smap.shinemyroom.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;

import java.util.ArrayList;

import dk.group2.smap.shinemyroom.R;


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
        Switch lightSwitch = (Switch) convertView.findViewById(R.id.room_clicked_lampSwitch);

        final PHLight lamp = phLightArrayList.get(position);
        if(lamp.getLastKnownLightState().isReachable())
            lampStatus.setText("Reachable");
        else
            lampStatus.setText("Out of Reach");
        lampName.setText(lamp.getName());
        lightSwitch.setChecked(lamp.getLastKnownLightState().isOn());

        lightSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PHBridge selectedBridge = PHHueSDK.getInstance().getSelectedBridge();
                PHLightState phLightState = new PHLightState();
                phLightState.setOn(isChecked);
                selectedBridge.updateLightState(lamp,phLightState);
            }
        });

        return convertView;
    }
}
