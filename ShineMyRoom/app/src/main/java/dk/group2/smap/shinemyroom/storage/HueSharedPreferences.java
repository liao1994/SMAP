package dk.group2.smap.shinemyroom.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.philips.lighting.model.PHBridge;

import java.util.Objects;

// Found in offical Phillips Hue SDK quickStart app
// https://github.com/PhilipsHue/PhilipsHueSDK-Java-MultiPlatform-Android
public class HueSharedPreferences {
    private static final String TAG = "ERROR/" + HueSharedPreferences.class.getName();
    private static final String HUE_SHARED_PREFERENCES_STORE = "HueSharedPrefs";
    private static final String LAST_CONNECTED_USERNAME      = "LastConnectedUsername";
    private static final String LAST_CONNECTED_IP            = "LastConnectedIP";
    private static final String LAST_CONNECTED_BRIDGE_DATA = "LastConnectedBridgeData";
    private static HueSharedPreferences instance = null;
    private SharedPreferences mSharedPreferences = null;

    private SharedPreferences.Editor mSharedPreferencesEditor = null;

    public static HueSharedPreferences getInstance(Context ctx) {
        if (instance == null) {
            instance = new HueSharedPreferences(ctx);
        }
        return instance;
    }

    private HueSharedPreferences(Context appContext) {
        mSharedPreferences = appContext.getSharedPreferences(HUE_SHARED_PREFERENCES_STORE, 0); // 0 - for private mode
        mSharedPreferencesEditor = mSharedPreferences.edit();
    }


    public String getUsername() {
        String username = mSharedPreferences.getString(LAST_CONNECTED_USERNAME, "");
        return username;
    }

    public boolean setUsername(String username) {
        mSharedPreferencesEditor.putString(LAST_CONNECTED_USERNAME, username);
        return (mSharedPreferencesEditor.commit());
    }

    public String getLastConnectedIPAddress() {
        return mSharedPreferences.getString(LAST_CONNECTED_IP, "");
    }

    public boolean setLastConnectedIPAddress(String ipAddress) {
        mSharedPreferencesEditor.putString(LAST_CONNECTED_IP, ipAddress);
        return (mSharedPreferencesEditor.commit());
    }

    public boolean setLastConnectedBridgeData(PHBridge phBridge){
        Gson gson = new Gson();
        String json = gson.toJson(phBridge);
        mSharedPreferencesEditor.putString(LAST_CONNECTED_BRIDGE_DATA, json);
        return (mSharedPreferencesEditor.commit());
    }
    public PHBridge getLastConnectedBridgeData(){
        Gson gson = new Gson();
        PHBridge phBridge = null;
        String s = mSharedPreferences.getString(LAST_CONNECTED_IP, "");
        if(!Objects.equals(s, ""))
        {
            try {
                phBridge = gson.fromJson(s, PHBridge.class);
            }catch (Exception e)
            {
                Log.d(TAG,"error while converting from string to phBrigde");
            }
        }
        return phBridge;
    }

}
