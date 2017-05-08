package dk.group2.smap.assigment2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;


public class WeatherAdapter extends BaseAdapter{

    ArrayList<WeatherInfo> _weatherinfo;
    Context _context;
    static LayoutInflater _layoutInflater = null;
    public WeatherAdapter(Context c, ArrayList<WeatherInfo> weatherinfo){
        _weatherinfo = weatherinfo;
        _context = c;
    }
    @Override
    public int getCount() {
        if(_weatherinfo != null){
            return _weatherinfo.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(_weatherinfo!=null) {
            return _weatherinfo.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return 0;
        //return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            _layoutInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = _layoutInflater.inflate(R.layout.weather_list_item, null);//set layout for displaying items
        }

        TextView tvWeather = (TextView) convertView.findViewById(R.id.tv_weather);
        tvWeather.setText(_weatherinfo.get(position).getDescription());
        TextView tvDate = (TextView) convertView.findViewById(R.id.tv_date);
        tvDate.setText(_weatherinfo.get(position).getTimeStamp());
        TextView tvTime = (TextView) convertView.findViewById(R.id.tv_time);
        tvTime.setText(_weatherinfo.get(position).getTimeStamp());
        TextView tvTemp = (TextView) convertView.findViewById(R.id.tv_temp);
        tvTemp.setText(_weatherinfo.get(position).getTemp());
        return convertView;
    }
}
