package com.red_folder.phonegap.plugin.backgroundservice;

import android.content.BroadcastReceiver;
import android.os.BatteryManager;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.R;
import android.widget.Toast;


public class PowerConnectionReceiver extends BroadcastReceiver {
    int notifyID = 11;
    public void showNotification(Context context, String title, String description){
    	try{
    	final NotificationManager mgr=
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification note=new Notification(R.drawable.star_big_on,
                                                            title,
                                                            System.currentTimeMillis());
		Intent contentIntent = new Intent();
             PendingIntent appIntent = PendingIntent.getActivity(context, 0, contentIntent, 0);
	        note.setLatestEventInfo(context, title, description, appIntent);
            //After uncomment this line you will see number of notification arrived
            //note.number=2;
            mgr.notify(notifyID, note);	
    	}
    	catch(Exception e){
    		Toast toast = Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT);
            	toast.show();
    	}
    }
    
    public static void cancelNotification(Context ctx, int notifyId) {
    	String ns = Context.NOTIFICATION_SERVICE;
    	NotificationManager nMgr = (NotificationManager) ctx.getSystemService(ns);
    	nMgr.cancel(notifyId);
    }

    @Override
    public void onReceive(Context context, Intent intent) { 
        Toast toast1 = Toast.makeText(context, "Safe Battery Enabled", Toast.LENGTH_SHORT);
        toast1.show();
        try{
       	String batteryStatus = "";
       	IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatusIntent = context.registerReceiver(null, ifilter);
        
        int status = batteryStatusIntent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL;
    	boolean isFull = status == BatteryManager.BATTERY_STATUS_FULL;
        int chargePlug = batteryStatusIntent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
        if(usbCharge)
        {
        	batteryStatus = "USB";
        }
        if(acCharge)
        {
        	batteryStatus = "AC Power";
        }
        if(isCharging){
        	Toast toast2 = Toast.makeText(context, "Safe Battery Enabled", Toast.LENGTH_SHORT).show();
        }
        else{
        	cancelNotification(context,notifyID);
        }
        if(isFull){
        	showNotification(context,"Safe Battery Enabled", "100% charged. Unplug Charger.");
        }
        }
        catch(Exception e){
            Toast toast = Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
