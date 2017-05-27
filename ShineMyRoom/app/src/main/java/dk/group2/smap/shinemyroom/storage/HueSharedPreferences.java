package dk.group2.smap.shinemyroom.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHBridgeResourcesCache;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Arrays;
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

    public boolean setRemoteBridge(String bridge){
        mSharedPreferencesEditor.putString(LAST_CONNECTED_BRIDGE_DATA, bridge);
        return (mSharedPreferencesEditor.commit());
    }
    public String getRemoteBridge(){
        return mSharedPreferences.getString(LAST_CONNECTED_BRIDGE_DATA, "");
    }

}
