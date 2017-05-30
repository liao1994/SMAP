package dk.group2.smap.shinemyroom.activities;

/**
 * Created by liao on 30-05-2017.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import dk.group2.smap.shinemyroom.AuthenticationDialog;
import dk.group2.smap.shinemyroom.CustomViewPager;
import dk.group2.smap.shinemyroom.FragmentPageAdapter;
import dk.group2.smap.shinemyroom.Global;
import dk.group2.smap.shinemyroom.NoBridgeConnectFragment;
import dk.group2.smap.shinemyroom.R;
import dk.group2.smap.shinemyroom.fragments.RoomListFragment;
import dk.group2.smap.shinemyroom.services.PHHueService;

//https://www.youtube.com/watch?v=bNpWGI_hGGg
public class MainActivity extends AppCompatActivity {
    TabLayout tabLayout;
    FragmentPageAdapter fragmentPageAdapter;
    TextView connectionStatus;
    AuthenticationDialog authenticationDialog;
    CustomViewPager viewPager;
    ImageView view1;
    ImageView view2;
    ImageView view3;
    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction(getString(R.string.authentication_required_action));
        filter.addAction(getString(R.string.bridge_connected_action));
        filter.addAction(getString(R.string.authenticaion_failed_action));
        filter.addAction(getString(R.string.remote_connected_action));
        filter.addAction(getString(R.string.no_connection_action));
        filter.addAction(getString(R.string.start_service_action));
        // http://stackoverflow.com/questions/10733121/broadcastreceiver-when-wifi-or-3g-network-state-changed
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        filter.addAction("android.net.wifi.STATE_CHANGE");
        LocalBroadcastManager.getInstance(this).registerReceiver(onBackgroundServiceResult,filter);
    }
    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(onBackgroundServiceResult);
    }
    @Override
    protected void onDestroy() {
        stopService(new Intent(this,PHHueService.class));
        super.onDestroy();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        startService(new Intent(this, PHHueService.class));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //for more info, look in declaration
        viewPager = (CustomViewPager) findViewById(R.id.pager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        connectionStatus = (TextView) findViewById(R.id.connection_status_txt);
        authenticationDialog = new AuthenticationDialog(this);
        fragmentPageAdapter = new FragmentPageAdapter(this, getSupportFragmentManager());
        viewPager.setPagingEnabled(false);
        viewPager.setAdapter(fragmentPageAdapter);
        tabLayout.setupWithViewPager(viewPager);
        View viewByLayout1 = getLayoutInflater().inflate(R.layout.custom_tab_icon, null);
        view1 = (ImageView) viewByLayout1.findViewById(R.id.tab_icon);
        View viewByLayout2 = getLayoutInflater().inflate(R.layout.custom_tab_icon, null);
        view2 = (ImageView) viewByLayout2.findViewById(R.id.tab_icon);
        View viewByLayout3 = getLayoutInflater().inflate(R.layout.custom_tab_icon, null);
        view3 = (ImageView) viewByLayout3.findViewById(R.id.tab_icon);
        view1.setImageResource(R.drawable.homeiconwhite);
        view2.setImageResource(R.drawable.geofenceicon);
        view3.setImageResource(R.drawable.hue_icon);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                fragmentPageAdapter.getItem(position).onResume();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        drawTabIcons();
    }
    
    private void drawTabIcons(){
        tabLayout.getTabAt(0).setCustomView(view1);
        tabLayout.getTabAt(1).setCustomView(view2);
        tabLayout.getTabAt(2).setCustomView(view3);
    }
    private BroadcastReceiver onBackgroundServiceResult = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("LOG", "Broadcast received: " + intent.getAction());

            if(intent.getAction().equals(getString(R.string.authentication_required_action))){
                authenticationDialog.run();
            }else if(intent.getAction().equals(getString(R.string.bridge_connected_action))){

                fragmentPageAdapter.replaceFragmentAt(0,new RoomListFragment());
                updateConnectionStatus(getString(R.string.connection_status_bridge));
                authenticationDialog.dismiss();

            }
            else if (intent.getAction().equals(getString(R.string.remote_connected_action))){
                updateConnectionStatus(getString(R.string.connection_status_remote));
                fragmentPageAdapter.replaceFragmentAt(0,new RoomListFragment());

            }else if(intent.getAction().equals(getString(R.string.no_connection_action)))
            {
                updateConnectionStatus(getString(R.string.no_connection_found));
            }
            else if(intent.getAction().equals(getString(R.string.authenticaion_failed_action))){
                updateConnectionStatus(getString(R.string.no_connection_found));
                fragmentPageAdapter.replaceFragmentAt(0,new NoBridgeConnectFragment());
                authenticationDialog.dismiss();
            }
            else if(intent.getAction().equals("android.net.wifi.WIFI_STATE_CHANGED") || intent.getAction().equals( "android.net.wifi.STATE_CHANGE")) {
                ConnectivityManager conMngr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
                // http://stackoverflow.com/questions/32547006/connectivitymanager-getnetworkinfoint-deprecated
                NetworkInfo netinfo = conMngr.getActiveNetworkInfo();
                if (netinfo != null) { // connected to the internet
                    if (netinfo.getType() == ConnectivityManager.TYPE_WIFI) {
                        // connected to wifi
                        Toast.makeText(context, netinfo.getTypeName(), Toast.LENGTH_SHORT).show();
                    } else if (netinfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                        // connected to the mobile provider's data plan
                        Toast.makeText(context, netinfo.getTypeName(), Toast.LENGTH_SHORT).show();
                    }
                } else {

                    //TODO SHOW THAT WE ARE NOT CONNECTED, tmp solution
                    Toast.makeText(context, "not connected to a network",Toast.LENGTH_SHORT).show();
                }

            }
            //Toast.makeText(MainActivity.this,"stuff", Toast.LENGTH_SHORT).show();
            drawTabIcons();
        }
    };
    private void updateConnectionStatus(String status){
        if(status.equals(getString(R.string.connection_status_waiting)))
        {
            connectionStatus.setText(status);
            connectionStatus.setBackgroundColor(Global.CONNECTING_COLOR);
        }else if(status.equals(getString(R.string.connection_status_bridge)))
        {
            connectionStatus.setText(status);
            connectionStatus.setBackgroundColor(Global.CONNECTED_COLOR);
        }else if(status.equals(getString(R.string.connection_status_remote)))
        {
            connectionStatus.setText(status);
            connectionStatus.setBackgroundColor(Global.CONNECTED_COLOR);
        }else if(status.equals(getString(R.string.no_connection_found))){
            connectionStatus.setText(status);
            connectionStatus.setBackgroundColor(Global.NO_CONNECTION_FOUND_COLOR);
        }
    }


}
