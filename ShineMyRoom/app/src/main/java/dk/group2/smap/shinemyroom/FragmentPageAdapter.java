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
    private HashMap<Class<? extends Fragment>,Integer> fragmentToKey = new HashMap<>();
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private Context c;
    private SparseArray<Drawable> iconMap = new SparseArray<>();
    public FragmentPageAdapter(Context c, FragmentManager fm) {
        super(fm);
        this.c = c;
        fragmentMap.append(0, new LoadingFragment());
        fragmentToKey.put(LoadingFragment.class,0);

        fragmentMap.append(1, new GeoFencingFragment());
        fragmentToKey.put(GeoFencingFragment.class,1);

        fragmentMap.append(2, new EditRomFragment());
        fragmentToKey.put(EditRomFragment.class,2);

        iconMap.append(0, ContextCompat.getDrawable(c, R.drawable.home_icon));
        iconMap.append(1, ContextCompat.getDrawable(c, R.drawable.hue_geo_fencing));
        iconMap.append(2, ContextCompat.getDrawable(c, R.drawable.hue_icon));
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

    private boolean replaceFragmentWith(Class<? extends Fragment> oldFragment, Fragment newFragment){
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

    @Override
    public Fragment getItem(int position) {
        return fragmentMap.get(position);
    }

    @Override
    public int getCount() {
        return fragmentMap.size();
    }

}
