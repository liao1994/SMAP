package dk.group2.smap.assigment2;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;


public class WeatherAdapter extends BaseAdapter{

    private final WeatherDatabase db;
    private ArrayList<WeatherInfo> _weatherinfo;
    private Context _context;
    private static LayoutInflater _layoutInflater = null;
    public WeatherAdapter(Context c, ArrayList<WeatherInfo> weatherinfo, WeatherDatabase db){
        _weatherinfo = weatherinfo;
        _context = c;
        this.db = db;
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

//    static class ViewHolder(){
//    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            _layoutInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = _layoutInflater.inflate(R.layout.weather_list_item, null);//set layout for displaying items

        }
        ImageView imgView = (ImageView) convertView.findViewById(R.id.weather_icon);
        TextView tvWeather = (TextView) convertView.findViewById(R.id.tv_weather);
        TextView tvDate = (TextView) convertView.findViewById(R.id.tv_date);
        TextView tvTime = (TextView) convertView.findViewById(R.id.tv_time);
        TextView tvTemp = (TextView) convertView.findViewById(R.id.tv_temp);

        Bitmap tmp = db.getIcon(_weatherinfo.get(position).getIcon());
        if (tmp != null)
         imgView.setImageBitmap(tmp);
        String[] xx = _weatherinfo.get(position).getTimeStamp().split(" ");
        tvWeather.setText(_weatherinfo.get(position).getMain());
        tvDate.setText(xx[0]);
        tvTime.setText(xx[1]);
        tvTemp.setText( String.format( "%.2f", _weatherinfo.get(position).getTemp()) + _context.getString(R.string.degrees) +(char) 0x00B0 );

        return convertView;

    }

}
