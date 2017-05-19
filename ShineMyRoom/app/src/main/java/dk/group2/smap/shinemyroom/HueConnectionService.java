package dk.group2.smap.shinemyroom;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.gson.Gson;
import com.philips.lighting.hue.sdk.PHAccessPoint;
import com.philips.lighting.hue.sdk.PHBridgeSearchManager;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.hue.sdk.PHSDKListener;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHHueParsingError;

import java.util.List;

/**
 * Created by liao on 18-05-2017.
 */

public class HueConnectionService extends IntentService {

    public static final String ACTION_CONNECT = "dk.group.shinemyroom.connect";
    public static final String TAG = "serviceDescovery";
    private PHHueSDK phHueSDK;


    private PHSDKListener listener = new PHSDKListener() {
        @Override
        public void onAccessPointsFound(List<PHAccessPoint> list) {
            Log.d("Log","onAccessPointsFound");

            if(list.size() > 1)
                Log.d("Log","onAccessPointsFound: Multiple Bridge Found");

            phHueSDK.connect(list.get(0));

            // Handle your bridge search results here.  Typically if multiple results are returned you will want to display them in a list
            // and let the user select their bridge.   If one is found you may opt to connect automatically to that bridge.
        }
        @Override
        public void onAuthenticationRequired(PHAccessPoint phAccessPoint) {
            Log.d("Log","onAuthenticationRequired");
            phHueSDK.startPushlinkAuthentication(phAccessPoint);

            // Arriving here indicates that Pushlinking is required (to prove the User has physical access to the bridge).  Typically here
            // you will display a pushlink image (with a timer) indicating to to the user they need to push the button on their bridge within 30 seconds.
            broadcastAuthenticationRequired();
            //TODO GETTING A COUNTDOWN TIMER GUI
        }
        @Override
        public void onError(int i, String s) {
            //onError: 101 | link button not pressed
            //onError: 1158 | Authentication failed
            Log.d("Log","onError: " + i + " | " +s);

        }

        @Override
        public void onBridgeConnected(PHBridge phBridge, String s) {
            Log.d("Log","Bridge Connected");

            Gson gson = new Gson();

            SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("HueInfo",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("HueJsonData", gson.toJson(phBridge));
            editor.commit();
            phHueSDK.setSelectedBridge(phBridge);
            phHueSDK.enableHeartbeat(phBridge, PHHueSDK.HB_INTERVAL);
            //TODO

            broadcastBridgeConnected();
            // Here it is recommended to set your connected bridge in your sdk object (as above) and start the heartbeat.
            // At this point you are connected to a bridge so you should pass control to your main program/activity.
            // The username is generated randomly by the bridge.
            // Also it is recommended you store the connected IP Address/ Username in your app here.  This will allow easy automatic connection on subsequent use.

        }
        @Override
        public void onCacheUpdated(List<Integer> list, PHBridge phBridge) {
            Log.d("Log","onCacheUpdated");

            // Here you receive notifications that the BridgeResource Cache was updated. Use the PHMessageType to
            // check which cache was updated, e.g.
//            if (cacheNotificationsList.contains(PHMessageType.LIGHTS_CACHE_UPDATED)) {
//                System.out.println("Lights Cache Updated ");
//            }
        }


        @Override
        public void onConnectionResumed(PHBridge phBridge) {
            Log.d("Log","onConnectionResumed");

        }

        @Override
        public void onConnectionLost(PHAccessPoint phAccessPoint) {
            Log.d("Log","onConnectionLost");

        }

        @Override
        public void onParsingErrors(List<PHHueParsingError> list) {
            Log.d("Log","onParsingErrors");

        }
    };




    public HueConnectionService() {
        super("HueConnectionService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d("LOG", "Background service onHandleIntent");
        if (intent != null) {
            if(intent.getAction().equals(getString(R.string.authentication_required_action))){
                startService();
            }else if(intent.getAction().equals(getString(R.string.start_service_action))){
                startService();
            }
        }
    }

    private void startService(){

        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getActiveNetworkInfo();

        if (mWifi.isConnected()) {
            phHueSDK = PHHueSDK.getInstance();

            phHueSDK.setAppName(getApplicationContext().getString(R.string.app_name));
            phHueSDK.setDeviceName(android.os.Build.MODEL);
            phHueSDK.getNotificationManager().registerSDKListener(listener);
            PHBridgeSearchManager sm = (PHBridgeSearchManager) phHueSDK.getSDKService(PHHueSDK.SEARCH_BRIDGE);
            sm.search(true, true);    // Do whatever
        }else{
            // remote control when i get API
        }

    }
    private void broadcastAuthenticationRequired() {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(getString(R.string.authentication_required_action));
        Log.d("LOG", "Broadcasting:" + getString(R.string.authentication_required_action));
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
    }
    private void broadcastBridgeConnected() {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(getString(R.string.bridge_connected));
        Log.d("LOG", "Broadcasting:" + getString(R.string.bridge_connected));
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
    }


}
