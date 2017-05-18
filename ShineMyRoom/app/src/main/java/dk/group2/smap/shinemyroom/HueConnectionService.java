package dk.group2.smap.shinemyroom;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
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
    private static PHHueSDK phHueSDK;

    private static Context context;
    private String cacheNotificationsList;
    private String brigdeId;
    private String ipAddress;
    private String macAddress;
    private String userName;
    private static PHSDKListener listener = new PHSDKListener() {
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
            //TODO GETTING A COUNTDOWN TIMER GUI
        }
        @Override
        public void onError(int i, String s) {
            Log.d("Log","onError: " + i + " | " +s);

        }

        @Override
        public void onBridgeConnected(PHBridge phBridge, String s) {
            Log.d("Log","Bridge Connected");

            Gson gson = new Gson();

            SharedPreferences sharedPref = context.getSharedPreferences("HueJsonData",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("HueJsonData", gson.toJson(phBridge));
            editor.commit();

            phHueSDK.setSelectedBridge(phBridge);
            phHueSDK.enableHeartbeat(phBridge, PHHueSDK.HB_INTERVAL);
            //TODO
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

    public static void startAction(Context context){
        phHueSDK = PHHueSDK.getInstance();
        phHueSDK.setAppName(context.getString(R.string.app_name));     // e.g. phHueSDK.setAppName("QuickStartApp");
        phHueSDK.setDeviceName(android.os.Build.MODEL);
        phHueSDK.getNotificationManager().registerSDKListener(listener);
        PHBridgeSearchManager sm = (PHBridgeSearchManager) phHueSDK.getSDKService(PHHueSDK.SEARCH_BRIDGE);
        sm.search(true, true);
//        Intent intent = new Intent(context, HueConnectionService.class);
//        intent.setAction(ACTION_CONNECT);
//        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d("LOG", "Background service onHandleIntent");


        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_CONNECT.equals(action)) {
            }
        }
    }


}
