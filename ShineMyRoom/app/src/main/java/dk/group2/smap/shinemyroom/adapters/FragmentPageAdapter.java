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

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Log.d("LOG/" + FragmentPageAdapter.class.getName(), "instantiateItem");
        Object o = super.instantiateItem(container, position);
        if (o instanceof Fragment){
            fragmentMap.append(position,(Fragment) o);
        }
        return o;
    }


    //force adapter to recreate
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return fragmentMap.size();
    }


}
