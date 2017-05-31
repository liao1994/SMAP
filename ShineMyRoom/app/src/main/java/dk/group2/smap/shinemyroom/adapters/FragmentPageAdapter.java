package dk.group2.smap.shinemyroom.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.util.SparseArray;
import android.view.ViewGroup;

import dk.group2.smap.shinemyroom.fragments.EditRoomFragment;
import dk.group2.smap.shinemyroom.fragments.GeoFencingFragment;
import dk.group2.smap.shinemyroom.fragments.LoadingFragment;

//https://www.youtube.com/watch?v=bNpWGI_hGGg
public class FragmentPageAdapter extends FragmentStatePagerAdapter {

//    private ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
//    private ArrayList<String> fragmentTitleArrayList = new ArrayList<>();
    private SparseArray<Fragment> fragmentMap = new SparseArray<>();
    private SparseArray<String> tagMap = new SparseArray<>();
    private Context c;
    private FragmentManager fm;

    public FragmentPageAdapter(Context c, FragmentManager fm) {
        super(fm);
        this.c = c;
        this.fm = fm;

        fragmentMap.append(0, new LoadingFragment());
        fragmentMap.append(1, new GeoFencingFragment());
        fragmentMap.append(2, new EditRoomFragment());

    }

    //https://stackoverflow.com/questions/31260384/how-to-add-page-title-and-icon-in-android-fragmentpageradapter
//    @Override
//    public CharSequence getPageTitle(int position) {
//        Drawable image = iconMap.get(position);
//        image.setBounds(0, 0,50,50);
//        SpannableString sb = new SpannableString(" ");
//        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BASELINE);
//        sb.setSpan(imageSpan, 0, 0, Spannable.SPAN_USER);
//        return sb;
//    }

    public Fragment getFragment(int position){
        return fragmentMap.get(position);
    }

    public void replaceFragmentAt(int key, Fragment fragment) {
        fragmentMap.append(key, fragment);
        this.notifyDataSetChanged();
    }
    @Override
    public Fragment getItem(int position) {
        Log.d("LOG/" + FragmentPageAdapter.class.getName(), "getItem");
        return fragmentMap.get(position);


    }

//    @Override
//    public void destroyItem(ViewGroup container, int position, Object object) {
//        fragmentMap.delete(position);
//        super.destroyItem(container, position, object);
//
//    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Log.d("LOG/" + FragmentPageAdapter.class.getName(), "instantiateItem");
        Object o = super.instantiateItem(container, position);
        if (o instanceof Fragment){
            fragmentMap.append(position,(Fragment) o);
        }
        return o;
    }


    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return fragmentMap.size();
    }


}
