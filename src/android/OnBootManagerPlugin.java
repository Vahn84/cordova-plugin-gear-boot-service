package com.vahn.cordova.onbootservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.util.Log;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by vahn on 02/05/2017.
 */

public class AppManager extends CordovaPlugin {

    PluginResult result;
    Context context;
    CallbackContext cbContext;
    String filter = "com.vahn.cordova.onbootservice.BOOT_SERVICE_ACTION";
    IntentFilter intentFilter = new IntentFilter(filter);
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private boolean routeIgnited;
    private String routeKms;
    private String routeMissdeCalls;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        this.cbContext = callbackContext;
        this.context = this.cordova.getActivity().getApplicationContext();
        this.context.registerReceiver(bootServiceActionReceiver, intentFilter);
        this.sharedPref = context.getSharedPreferences(context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
        this.editor = this.sharedPref.edit();

        return true;
    }

    public BroadcastReceiver bootServiceActionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            sendActionToJS();
        }
    };


    public void sendActionToJS(){

            JSONObject parameter = new JSONObject();

            try {
                parameter.put("ignition", "start_stop");
            } catch (JSONException e) {

                Log.e("ERRORE SMARTPHONERS", e.toString());
            }

            result = new PluginResult(PluginResult.Status.OK, parameter);
            result.setKeepCallback(true);
            cbContext.sendPluginResult(result);

    }

}
