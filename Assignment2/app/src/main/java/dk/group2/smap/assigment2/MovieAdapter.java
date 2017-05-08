//package dk.group2.smap.assigment2;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.TextView;
//
//import java.util.ArrayList;
//
///**
// * Created by liao on 25-04-2017.
// */
//
//public class MovieAdapter extends BaseAdapter{
//
//    ArrayList<Demo> _demos;
//    Context _context;
//    static LayoutInflater _layoutInflater = null;
//    public MovieAdapter(Context c, ArrayList<Demo> demos){
//        _demos = demos;
//        _context = c;
//    }
//    @Override
//    public int getCount() {
//        if(_demos != null){
//            return _demos.size();
//        }
//        return 0;
//    }
//
//    @Override
//    public Object getItem(int position) {
//        if(_demos!=null) {
//            return _demos.get(position);
//        } else {
//            return null;
//        }
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return 0;
//        //return position;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        if (convertView == null){
//            _layoutInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//            convertView = _layoutInflater.inflate(R.layout.demo_list_item, null);//set layout for displaying items
//        }
//
//        TextView tv = (TextView) convertView.findViewById(R.id.demo_list_item_name);
//        tv.setText(_demos.get(position).Name());
//        return convertView;
//    }
//}
