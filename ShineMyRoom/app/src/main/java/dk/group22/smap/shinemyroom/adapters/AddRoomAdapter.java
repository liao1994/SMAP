package dk.group22.smap.shinemyroom.adapters;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.philips.lighting.model.PHGroup;
import com.philips.lighting.model.PHLight;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dk.group22.smap.shinemyroom.R;

//https://developer.android.com/reference/android/widget/Adapter.html
public class AddRoomAdapter extends BaseAdapter{

    private final Context c;
    private final PHGroup phGroup;
    private final Map<String, PHLight> allLights;
    List<PHLight> list;

    public AddRoomAdapter(Context c, PHGroup phGroup, Map<String, PHLight> allLights) {
        this.c = c;
        this.phGroup = phGroup;
        this.allLights = allLights;
        list = new ArrayList<>(allLights.values());
    }
    public PHGroup getRoomFromAdapter(){
        return phGroup;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static public class ViewHolder{
        protected TextView lamp;
        protected CheckBox checkBox;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        if (convertView == null){
            final ViewHolder viewHolder = new ViewHolder();
            LayoutInflater inflator = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflator.inflate(R.layout.item_add_room_list, null);
            viewHolder.lamp = (TextView) view.findViewById(R.id.edit_room_lamp_name);
            viewHolder.checkBox = (CheckBox) view.findViewById(R.id.edit_room_checkbox);
            viewHolder.lamp.setTag(list.get(position).getIdentifier());
            viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Log.d("LOG/onCheckedChanged","w00t");
                    if(isChecked){
                        phGroup.getLightIdentifiers().add((String)viewHolder.checkBox.getTag());
                    }else
                    {
                        String tag = (String) viewHolder.checkBox.getTag();
                        phGroup.getLightIdentifiers().remove(tag);

                    }
                }
            });
            view.setTag(viewHolder);
        }else{
            view = convertView;
            ((ViewHolder) view.getTag()).checkBox.setTag(list.get(position).getIdentifier());
        }
        ViewHolder holder = (ViewHolder) view.getTag();

        holder.lamp.setText(list.get(position).getName());
        String identifier = list.get(position).getIdentifier();
        List<String> lightIdentifiers = phGroup.getLightIdentifiers();
        boolean contains = lightIdentifiers.contains(identifier);
        holder.checkBox.setChecked(contains);


        return view;
    }
}
