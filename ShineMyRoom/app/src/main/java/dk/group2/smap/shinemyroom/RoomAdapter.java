package dk.group2.smap.shinemyroom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.philips.lighting.model.PHGroup;

import java.util.ArrayList;
import java.util.List;

import dk.group2.smap.shinemyroom.generated.GroupResponse;
import dk.group2.smap.shinemyroom.generated.Room;




public class RoomAdapter extends BaseAdapter {
    private ArrayList<PHGroup> lightGroups;
    private IHueControl hueControl;
    private Context c;
    private LayoutInflater layoutInflater;
    GroupResponse obj;


    public RoomAdapter(Context c, ArrayList<PHGroup> lightGroups){
        this.c = c;
        this.lightGroups = lightGroups;
        this.hueControl = new LocalHueControl(c);
        hueControl.setOnGroupResponseListener(new LocalHueControl.onGroupResponseListener(){
            @Override
            public void onGroupResult(String response) {

            }
        });

    }
    public RoomAdapter(Context c, ArrayList<PHGroup> lightGroups,IHueControl hueControl){
        this.c = c;
        this.lightGroups = lightGroups;
        this.hueControl = hueControl;
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
        // not going over 10 items anyway no need for viewholder

        layoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.room_list_item, null);//set layout for displaying items
        TextView tv = (TextView) convertView.findViewById(R.id.room_name_list_item);

//        hueControl.getGroupDetails(Integer.parseInt(lightGroups.get(0).getIdentifier()));
//        hueControl.getGroupDetails_V2(Integer.parseInt(lightGroups.get(0).getIdentifier()), new IHueControl.onGroupResponseListener() {
//            @Override
//            public void onGroupResult(String response) {
//                obj = new Gson().fromJson(response, GroupResponse.class);
//
//            }
//        });
        tv.setText(lightGroups.get(position).getName());

        return convertView;
    }
}
