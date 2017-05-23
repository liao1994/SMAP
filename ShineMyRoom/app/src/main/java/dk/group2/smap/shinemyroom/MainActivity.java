package dk.group2.smap.shinemyroom;

import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.todddavies.components.progressbar.ProgressWheel;

public class MainActivity extends AppCompatActivity {

    AlertDialog.Builder builder;
    Dialog d;
    ProgressWheel pw;
    RelativeLayout viewById;
    LayoutInflater minflator;
    myTask dialogTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        Intent mServiceIntent = new Intent(this, HueConnectionService.class);
        mServiceIntent.setAction(getString(R.string.start_service_action));
        this.startService(mServiceIntent);
        builder = new AlertDialog.Builder(this);
        minflator = getLayoutInflater();
        viewById = (RelativeLayout) minflator.inflate(R.layout.authentication_progress_bar, null);
        pw = (ProgressWheel) viewById.findViewById(R.id.pw_spinner);
        dialogTask = new myTask();

        FragmentManager fragmentManager = getFragmentManager();
        RoomsFragment roomfragment =  new RoomsFragment();
//        //TabFragment tabfragment = new TabFragment();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                .add(R.id.contentfragment, roomfragment);
        fragmentTransaction.commit();


    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction(getString(R.string.authentication_required_action));
        filter.addAction(getString(R.string.connected));
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
        super.onDestroy();
        Intent mServiceIntent = new Intent(this, HueConnectionService.class);
        mServiceIntent.setAction(getString(R.string.kill_app_action));
        this.startService(mServiceIntent);
    }

    private BroadcastReceiver onBackgroundServiceResult = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("LOG", "Broadcast received: " + intent.getAction());

            if(intent.getAction().equals(getString(R.string.authentication_required_action))){
                dialogTask.execute();
            }else if(intent.getAction().equals(getString(R.string.bridge_connected))){
                dialogTask.cancel(true);
            }else if(intent.getAction().equals("android.net.wifi.WIFI_STATE_CHANGED") || intent.getAction().equals( "android.net.wifi.STATE_CHANGE")) {
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
        builder.show();
        pw.startSpinning();
        d = builder.create();

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
            d.dismiss();
        }


        @Override
        protected void onCancelled() {
            super.onCancelled();
            d.dismiss();
        }
    }


}
