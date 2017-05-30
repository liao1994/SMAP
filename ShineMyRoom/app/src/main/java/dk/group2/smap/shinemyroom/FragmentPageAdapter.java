package dk.group2.smap.shinemyroom;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;

import dk.group2.smap.shinemyroom.fragments.EditRomFragment;
import dk.group2.smap.shinemyroom.fragments.GeoFencingFragment;
import dk.group2.smap.shinemyroom.fragments.LoadingFragment;

//https://www.youtube.com/watch?v=bNpWGI_hGGg
public class FragmentPageAdapter extends FragmentStatePagerAdapter {

//    private ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
//    private ArrayList<String> fragmentTitleArrayList = new ArrayList<>();
    private SparseArray<Fragment> fragmentMap = new SparseArray<>();
    private SparseArray<String> tagMap = new SparseArray<>();
    private HashMap<Class<? extends Fragment>,Integer> fragmentToKey = new HashMap<>();
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private Context c;
    public FragmentPageAdapter(Context c, FragmentManager fm) {
        super(fm);
        this.c = c;
        fragmentMap.append(0, new LoadingFragment());
        fragmentToKey.put(LoadingFragment.class,0);

        fragmentMap.append(1, new GeoFencingFragment());
        fragmentToKey.put(GeoFencingFragment.class,1);

        fragmentMap.append(2, new EditRomFragment());
        fragmentToKey.put(EditRomFragment.class,2);

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

    public boolean replaceFragmentWith(Class<? extends Fragment> oldFragment, Fragment newFragment){
        Integer integer;
        if(fragmentToKey.containsKey(oldFragment)) {
            integer = fragmentToKey.get(oldFragment);
            fragmentToKey.remove(oldFragment);
        }
        else return false;

        fragmentMap.append(integer, newFragment);
        fragmentToKey.put(newFragment.getClass(),integer);
        return true;
    }
    public void replaceFragmentAt(int key, Fragment fragment) {
        if(getCount() == key) {
            return;
        }
        fragmentMap.append(key, fragment);
        this.notifyDataSetChanged();

    }
    @Override
    public Fragment getItem(int position) {
        return fragmentMap.get(position);
    }
    SparseArray<View> views = new SparseArray<View>();

//    @Override
//    public Object instantiateItem(ViewGroup container, int position) {
//        Object o = super.instantiateItem(container, position);
//        if (o instanceof Fragment){
//            Fragment f = (Fragment) o;
//            String tag = f.getTag();
//            tagMap.put(position,tag);
//        }
//        return o;
//    }

    //    //https://stackoverflow.com/questions/7263291/viewpager-pageradapter-not-updating-the-view
//    @Override
//    public int getItemPosition(Object object) {
//        return POSITION_NONE;
//    }

    @Override
    public int getCount() {
        return fragmentMap.size();
    }


}
