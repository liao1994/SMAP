package dk.group2.smap.shinemyroom.activities;

import android.app.Dialog;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.todddavies.components.progressbar.ProgressWheel;

import dk.group2.smap.shinemyroom.Global;
import dk.group2.smap.shinemyroom.NoBridgeConnectFragment;
import dk.group2.smap.shinemyroom.RoomAdapter;
import dk.group2.smap.shinemyroom.services.PHHueService;
import dk.group2.smap.shinemyroom.R;
import dk.group2.smap.shinemyroom.fragments.RoomListFragment;
import dk.group2.smap.shinemyroom.fragments.TabFragment;
import dk.group2.smap.shinemyroom.fragments.LoadingFragment;

public class MainActivity extends AppCompatActivity {

    AlertDialog.Builder builder;
    Dialog d;
    ProgressWheel pw;
    RelativeLayout viewById;
    LayoutInflater minflator;
    myTask dialogTask;
    FragmentManager fragmentManager;
    TextView connectionStatus;


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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        this.startService(new Intent(this, PHHueService.class));

        builder = new AlertDialog.Builder(this);
        minflator = getLayoutInflater();
        viewById = (RelativeLayout) minflator.inflate(R.layout.authentication_progress_bar, null);
        pw = (ProgressWheel) viewById.findViewById(R.id.pw_spinner);
        dialogTask = new myTask();
        connectionStatus = (TextView) findViewById(R.id.connection_txt);
        updateConnectionStatus(getString(R.string.connection_status_waiting));

        fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.contentfragment, new LoadingFragment())
                .add(R.id.tabfragment, new TabFragment())
                .commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction(getString(R.string.authentication_required_action));
        filter.addAction(getString(R.string.bridge_connected_action));
        filter.addAction(getString(R.string.authenticaion_failed_action));
        filter.addAction(getString(R.string.remote_connected_action));
        filter.addAction(getString(R.string.no_connection_action));
//        filter.addAction(getString(R.string.start_service_action));
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
        this.stopService(new Intent(this,PHHueService.class));
        super.onDestroy();
    }


    private BroadcastReceiver onBackgroundServiceResult = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("LOG", "Broadcast received: " + intent.getAction());

            if(intent.getAction().equals(getString(R.string.authentication_required_action))){
                dialogTask.execute();
            }else if(intent.getAction().equals(getString(R.string.bridge_connected_action))){

                fragmentManager.beginTransaction()
                        .replace(R.id.contentfragment, new RoomListFragment())
                        .commit();
                updateConnectionStatus(getString(R.string.connection_status_bridge));
                if(d != null)
                    d.dismiss();

            }
            else if (intent.getAction().equals(getString(R.string.remote_connected_action))){
                updateConnectionStatus(getString(R.string.connection_status_remote));
                fragmentManager.beginTransaction()
                        .replace(R.id.contentfragment, new RoomListFragment())
                        .commit();
            }else if(intent.getAction().equals(getString(R.string.no_connection_action)))
            {
                updateConnectionStatus(getString(R.string.no_connection_found));
            }
            else if(intent.getAction().equals(getString(R.string.authenticaion_failed_action))){
                fragmentManager.beginTransaction()
                        .replace(R.id.contentfragment,new NoBridgeConnectFragment())
                        .commit();
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

        }
    };

    private void createDialog() {

        builder.setView(viewById)
                .setTitle(getString(R.string.authentication_required))
                .setNegativeButton(R.string.enter_ip_btn_text, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialogTask.cancel(true);
                    }
                })
                .setPositiveButton(R.string.cancel_btn_txt, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialogTask.cancel(true);
                    }
                });
        pw.startSpinning();
        d = builder.create();
        d.show();

    }
    private class myTask extends AsyncTask<Void,Integer,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            createDialog();
        }
        @Override
        protected Void doInBackground(Void... params) {
            try {
                for (int i = 30; i > 0; i--){
                    Thread.sleep(1000);
                    publishProgress(i);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            pw.setText(String.valueOf(values[0]));
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d("LOG", "task done");
            d.dismiss();
        }


        @Override
        protected void onCancelled() {
            super.onCancelled();
            Log.d("LOG", "task cancelled");
            fragmentManager.beginTransaction()
                    .replace(R.id.contentfragment,new NoBridgeConnectFragment())
                    .commit();

        }
    }


}
