package dk.group2.smap.shinemyroom;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.todddavies.components.progressbar.ProgressWheel;

import java.net.URL;

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


    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction(getString(R.string.authentication_required_action));
        filter.addAction(getString(R.string.connected));
        LocalBroadcastManager.getInstance(this).registerReceiver(onBackgroundServiceResult,filter);
    }
    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(onBackgroundServiceResult);
    }
    private BroadcastReceiver onBackgroundServiceResult = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("LOG", "Broadcast received: " + intent.getAction());
            if(intent != null) {
                if(intent.getAction().equals(getString(R.string.authentication_required_action))){
                    dialogTask.execute();
                }else if(intent.getAction().equals(getString(R.string.connected))){

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
        protected void onCancelled() {
            super.onCancelled();
            d.dismiss();
        }
    }


}
