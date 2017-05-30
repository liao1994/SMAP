package dk.group2.smap.shinemyroom;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.todddavies.components.progressbar.ProgressWheel;


/**
 * Created by liao on 30-05-2017.
 */

public class AuthenticationDialog extends AlertDialog {
    private final static String TAG = "LOG/" + AuthenticationDialog.class.getName();
    private final Builder builder;
    private Context context;
    private final AuthenticationDialogTask dialogTask;
    AlertDialog alertDialog;
    ProgressWheel pw;

    public AuthenticationDialog(Context context) {
        super(context);
        LayoutInflater inflater = getLayoutInflater();
        RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.authentication_progress_bar, null);
        pw = (ProgressWheel) relativeLayout.findViewById(R.id.pw_spinner);
        builder = new AlertDialog.Builder(context);
        this.context = context;
        dialogTask = new AuthenticationDialogTask();
        alertDialog = builder.setView(relativeLayout)
                .setTitle(context.getString(R.string.authentication_required))
                .setNegativeButton(R.string.enter_ip_btn_text, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();
                    }
                })
                .setPositiveButton(R.string.cancel_btn_txt, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();
                        boardCast();
                    }
                })
                .create();
    }

    public void run(){
        dialogTask.execute();
    }
    public void dismiss(){
        dialogTask.cancel(true);
    }

    private class AuthenticationDialogTask extends AsyncTask<Void,Integer,Void> {

        private final String TAG = "LOG/" + AuthenticationDialogTask.class.getName();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pw.startSpinning();
            alertDialog.show();
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
            Log.d(TAG, "task done");
            alertDialog.dismiss();
        }


        @Override
        protected void onCancelled() {
            super.onCancelled();
            Log.d(TAG, "task cancelled");
        }
    }
    private void boardCast(){
            Log.d(TAG, "Broadcasting:" + context.getString(R.string.authenticaion_failed_action));
            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent()
                    .setAction(context.getString(R.string.authenticaion_failed_action)));
    }
}
