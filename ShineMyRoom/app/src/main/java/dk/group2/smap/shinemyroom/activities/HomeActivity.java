package dk.group2.smap.shinemyroom.activities;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import dk.group2.smap.shinemyroom.CustomViewPager;
import dk.group2.smap.shinemyroom.FragmentPageAdapter;
import dk.group2.smap.shinemyroom.R;

//https://www.youtube.com/watch?v=bNpWGI_hGGg
public class HomeActivity extends AppCompatActivity {
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //for more info, look in declaration
        CustomViewPager viewPager = (CustomViewPager) findViewById(R.id.pager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);

//        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
////        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.home_icon));
////        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.hue_geo_fencing));
////        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.hue_icon));
     //   tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager.setPagingEnabled(false);
        viewPager.setAdapter(new FragmentPageAdapter(this,getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        createTabIcons();
    }
    
    private void createTabIcons(){

        View view1 = getLayoutInflater().inflate(R.layout.custom_tab_icon, null);
        ((ImageView) view1.findViewById(R.id.tab_icon)).setImageResource(R.drawable.homeiconwhite);

        View view2 = getLayoutInflater().inflate(R.layout.custom_tab_icon, null);
        ((ImageView) view2.findViewById(R.id.tab_icon)).setImageResource(R.drawable.geofenceicon);


        View view3 = getLayoutInflater().inflate(R.layout.custom_tab_icon, null);
        ((ImageView) view3.findViewById(R.id.tab_icon)).setImageResource(R.drawable.hue_icon);


        tabLayout.getTabAt(0).setCustomView(view1);
        tabLayout.getTabAt(1).setCustomView(view2);
        tabLayout.getTabAt(2).setCustomView(view3);
    }

}
