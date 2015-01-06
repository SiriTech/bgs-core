package com.red_folder.phonegap.plugin.backgroundservice;

import android.content.BroadcastReceiver;
import android.content.Context;

import android.os.BatteryManager;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.app.PendingIntent;
import android.util.Log;
import android.app.Notification;
import android.app.NotificationManager;
import android.annotation.TargetApi;

import android.R;

import android.util.Log;

public class PowerConnectionReceiver extends BroadcastReceiver {
    
    private int notif_id=100;
	private String notificationTitle = "App Service";
	private String notificationText = "Running";
	private Notification getActivityNotification(String title, String text){
		
        int icon = R.drawable.star_big_on;
        int normalIcon = getResources().getIdentifier("icon", "drawable", getPackageName());
        int notificationIcon = getResources().getIdentifier("notificationicon", "drawable", getPackageName());         
        if(notificationIcon != 0) {
        	Log.d("ONSTARTCOMMAND", "Found Custom Notification Icon!");
        	icon = notificationIcon;
        }
        else if(normalIcon != 0) {
        	Log.d("ONSTARTCOMMAND", "Found normal Notification Icon!");
        	icon = normalIcon;
        }

        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle(title);
        builder.setContentText(text);
        builder.setSmallIcon(icon);
        builder.setContentIntent(pendingIntent);        
        Notification notification;
        if (android.os.Build.VERSION.SDK_INT >= 16) {
            notification = buildForegroundNotification(builder);
        } else {
            notification = buildForegroundNotificationCompat(builder);
        }
        notification.flags |= Notification.FLAG_ONGOING_EVENT | Notification.FLAG_FOREGROUND_SERVICE | Notification.FLAG_NO_CLEAR;
        return notification;
	}

	private void updateNotification(String title, String text) {
        Notification notification = getActivityNotification(title, text);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
        mNotificationManager.notify(notif_id, notification);
	}
	
	@TargetApi(16)
    private Notification buildForegroundNotification(Notification.Builder builder) {
        return builder.build();
    }

    @SuppressWarnings("deprecation")
    @TargetApi(15)
    private Notification buildForegroundNotificationCompat(Notification.Builder builder) {
        return builder.getNotification();
    }
    
    @Override
    public void onReceive(Context context, Intent intent) { 
        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                            status == BatteryManager.BATTERY_STATUS_FULL;
    
        int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
        if(isCharging){
            final NotificationManager mgr=
            (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification note=new Notification(R.drawable.stat_notify_chat,
                                                        "Android Example Status message!",
                                                        System.currentTimeMillis());
         
        // This pending intent will open after notification click
        PendingIntent i=PendingIntent.getActivity(this, 0,
                                                new Intent(this, NotifyMessage.class),
                                                0);
         
        note.setLatestEventInfo(this, "Android Example Notification Title",
                                "This is the android example notification message", i);
         
        //After uncomment this line you will see number of notification arrived
        //note.number=2;
        mgr.notify(NOTIFY_ME_ID, note);
        }
    }
}
