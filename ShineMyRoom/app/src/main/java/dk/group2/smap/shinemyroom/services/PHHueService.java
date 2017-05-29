package dk.group2.smap.shinemyroom.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.philips.lighting.hue.sdk.PHAccessPoint;
import com.philips.lighting.hue.sdk.PHBridgeSearchManager;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.hue.sdk.PHSDKListener;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHHueParsingError;

import java.util.List;
import java.util.Objects;

import dk.group2.smap.shinemyroom.R;
import dk.group2.smap.shinemyroom.storage.HueSharedPreferences;

/**
 * Created by liao on 18-05-2017.
 */

public class PHHueService extends Service {
    public static final String ACTION_CONNECT = "dk.group.shinemyroom.connect";
    private static final String TAG = "LOG/" + PHHueService.class.getName();
    private PHHueSDK phHueSDK;
    private HueSharedPreferences prefs;
    private PHSDKListener listener = new PHSDKListener() {
        @Override
        public void onAccessPointsFound(List<PHAccessPoint> list) {
            Log.d("Log","onAccessPointsFound");

            if(list.size() > 1)
                Log.d("Log","onAccessPointsFound: Multiple Bridge Found");

            prefs = HueSharedPreferences.getInstance(getApplicationContext());
            String lastIpAddress   = prefs.getLastConnectedIPAddress();
            String lastUsername    = prefs.getUsername();
            PHAccessPoint phAccessPoint = list.get(0);
            // if not the accessIp as last time, request new conn otherwise just use last one
            if (lastIpAddress.equals(phAccessPoint.getIpAddress()))  {
                if (!phHueSDK.isAccessPointConnected(phAccessPoint)) {
                    phAccessPoint.setUsername(lastUsername);
                    phHueSDK.connect(phAccessPoint);
                }
            }else {
                phHueSDK.connect(phAccessPoint);
            }


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
        }
        @Override
        public void onError(int i, String s) {
            //onError: 101 | link button not pressed
            //onError: 1158 | Authentication failed
            //onError: 1157 | No bridge found


            Log.d(TAG,"onError: " + i + " | " +s);
            if(i == 1158)
                broadcastAuthenticationFailed();
            if(i == 1157 | i == 46)
                RemoteHueControlService.startTryGetRemoteAccessAction(getApplicationContext());
        }

        @Override
        public void onBridgeConnected(PHBridge phBridge, String s) {
            Log.d("Log","Bridge Connected");
            String ip = phBridge.getResourceCache().getBridgeConfiguration().getIpAddress();
            phHueSDK.getLastHeartbeat().put(ip, System.currentTimeMillis());
            prefs.setLastConnectedIPAddress(ip);
            prefs.setUsername(s);

            phHueSDK.setSelectedBridge(phBridge);
            phHueSDK.enableHeartbeat(phBridge, PHHueSDK.HB_INTERVAL);
            //s is username
            broadcastBridgeConnected(ip, s);
            // Here it is recommended to set your connected bridge in your sdk object (as above) and start the heartbeat.
            // At this point you are connected to a bridge so you should pass control to your main program/activity.
            // The username is generated randomly by the bridge.
            // Also it is recommended you store the connected IP Address/ Username in your app here.  This will allow easy automatic connection on subsequent use.

        }
        // Found in offical Phillips Hue SDK quickStart app
        // https://github.com/PhilipsHue/PhilipsHueSDK-Java-MultiPlatform-Android
        @Override
        public void onCacheUpdated(List<Integer> list, PHBridge phBridge) {
            Log.d(TAG,"onCacheUpdated");
            // Here you receive notifications that the BridgeResource Cache was updated. Use the PHMessageType to
            // check which cache was updated, e.g.
//            if (cacheNotificationsList.contains(PHMessageType.LIGHTS_CACHE_UPDATED)) {
//                System.out.println("Lights Cache Updated ");
//            }
        }

        // Found in offical Phillips Hue SDK quickStart app
        // https://github.com/PhilipsHue/PhilipsHueSDK-Java-MultiPlatform-Android
        @Override
        public void onConnectionResumed(PHBridge phBridge) {
            Log.d(TAG,"onConnectionResumed");
            phHueSDK.getLastHeartbeat().put(phBridge.getResourceCache().getBridgeConfiguration().getIpAddress(),  System.currentTimeMillis());
            for (int i = 0; i < phHueSDK.getDisconnectedAccessPoint().size(); i++) {

                if (phHueSDK.getDisconnectedAccessPoint().get(i).getIpAddress().equals(phBridge.getResourceCache().getBridgeConfiguration().getIpAddress())) {
                    phHueSDK.getDisconnectedAccessPoint().remove(i);
                }
            }
//            if(!prefs.setLastConnectedBridgeResourceCache(phBridge.getResourceCache()))
//                Log.d(TAG,"failed to store updated phBridge");
        }
        // Found in offical Phillips Hue SDK quickStart app
        // https://github.com/PhilipsHue/PhilipsHueSDK-Java-MultiPlatform-Android
        @Override
        public void onConnectionLost(PHAccessPoint phAccessPoint) {
            Log.d(TAG, "onConnectionLost : " + phAccessPoint.getIpAddress());
            if (!phHueSDK.getDisconnectedAccessPoint().contains(phAccessPoint)) {
                phHueSDK.getDisconnectedAccessPoint().add(phAccessPoint);
            }

        }

        @Override
        public void onParsingErrors(List<PHHueParsingError> list) {
            Log.d(TAG,"onParsingErrors");

        }

    };


    private BroadcastReceiver onIntentServiceResult = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("LOG", "Broadcast received from service");
            switch (intent.getAction()){
                case RemoteHueControlService.remote_access_sucess_action_result:
                    broadcastRemoteConnected();
                    break;
                case RemoteHueControlService.remote_access_failed_action_result:
                    boardcastNoConnectionFound();
                    break;
            }

        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int returnCode = super.onStartCommand(intent, flags, startId);
        startService();
        IntentFilter filter = new IntentFilter();
        filter.addAction(RemoteHueControlService.remote_access_failed_action_result);
        filter.addAction(RemoteHueControlService.remote_access_sucess_action_result);
        LocalBroadcastManager.getInstance(this).registerReceiver(onIntentServiceResult,filter);
        return returnCode;
    }

    @Override
    public void onDestroy() {

        stopHueBridgeHeartBeat();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(onIntentServiceResult);
        super.onDestroy();
    }

    private void startService(){

        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();

        if (networkInfo != null ? networkInfo.isConnected() : false) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                phHueSDK = PHHueSDK.getInstance();
                phHueSDK.setAppName(getApplicationContext().getString(R.string.app_name));
                phHueSDK.setDeviceName(android.os.Build.MODEL);
                phHueSDK.getNotificationManager().registerSDKListener(listener);
                PHBridgeSearchManager sm = (PHBridgeSearchManager) phHueSDK.getSDKService(PHHueSDK.SEARCH_BRIDGE);
                sm.search(true, true);    // Do whatever

            } else {
                RemoteHueControlService.startTryGetRemoteAccessAction(getApplicationContext());
            }


        } else {
            boardcastNoConnectionFound();
        }

    }
    private void stopHueBridgeHeartBeat() {
        phHueSDK.disableAllHeartbeat();
        PHBridge selectedBridge = phHueSDK.getSelectedBridge();
        if(selectedBridge != null)
        phHueSDK.disconnect(selectedBridge);
        //maybe more things i need to close myself add here later
    }
    private void boardcastNoConnectionFound() {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(getString(R.string.no_connection_action));
        Log.d(TAG, "Broadcasting:" + getString(R.string.no_connection_action));
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
    }
    private void broadcastAuthenticationRequired() {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(getString(R.string.authentication_required_action));
        Log.d("LOG", "Broadcasting:" + getString(R.string.authentication_required_action));
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
    }
    private void broadcastAuthenticationFailed() {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(getString(R.string.authenticaion_failed_action));
        Log.d(TAG, "Broadcasting:" + getString(R.string.authenticaion_failed_action));
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
    }
    private void broadcastBridgeConnected(String ip, String s) {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(getString(R.string.bridge_connected_action));
        broadcastIntent.putExtra(getString(R.string.username),s);
        broadcastIntent.putExtra(getString(R.string.ip_adress),ip);
        Log.d(TAG, "Broadcasting:" + getString(R.string.bridge_connected_action));
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
    }
    private void broadcastRemoteConnected() {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(getString(R.string.remote_connected_action));
        Log.d(TAG, "Broadcasting:" + getString(R.string.remote_connected_action));
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
    }

    private void broadcastBridgeNotRespoding() {
        //TODO finish implementing this
        Toast.makeText(getApplicationContext(),"External Error: Bridge not responding",Toast.LENGTH_SHORT).show();
    }


}
