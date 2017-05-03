package com.vahn.cordova.onbootservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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

public class OnBootManagerPlugin extends CordovaPlugin {
    
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
        this.sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        this.editor = this.sharedPref.edit();
        
        if(action.equals("app_started")) {
            
            if(this.sharedPref.getBoolean("startedFromSW", false)) {
                this.editor.putBoolean("startedFromSW",false);
                this.editor.putBoolean("appIsRunning",true);
                this.editor.commit();
                sendActionToJS("route_start");
            }
            
        } else if(action.equals("route_started")) {
            
            this.editor.putBoolean("routeStarted",true);
            this.editor.commit();
            
        } else if(action.equals("route_stopped")){
            
            this.editor.putBoolean("routeStarted",false);
            this.editor.commit();
            
        } else if(action.equals("route_trying_to_stop")){
            sendActionToJS("stop_anyway");
        }
        
        Log.d("calling_java", "executing");
        return true;
    }
    
    public BroadcastReceiver bootServiceActionReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
    String action;
    
    if(sharedPref.getBoolean("routeStarted", false)) {
        action = "route_stop";
    } else {
        action = "route_start";
    }
    sendActionToJS(action);
}
};


public void sendActionToJS(String action){
Log.d("calling_js", "callback");
JSONObject parameter = new JSONObject();

try {
parameter.put("ignition", action);
} catch (JSONException e) {

Log.e("ERRORE SMARTPHONERS", e.toString());
}

result = new PluginResult(PluginResult.Status.OK, parameter);
result.setKeepCallback(true);
cbContext.sendPluginResult(result);

}

@Override
public void onDestroy() {
Log.d("DESTROYING APP", "COMEON");
this.editor.putBoolean("routeStarted",false);
this.editor.putBoolean("startedFromSW",false);
this.editor.putBoolean("appIsRunning",false);
this.editor.putString("routeKms", "0.00");
this.editor.commit();
super.onDestroy();
}
}
