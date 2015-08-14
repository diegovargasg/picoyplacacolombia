package com.diegodevelopero.PicoyPlacaReminder;

import java.util.Calendar;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;

public class PicoyPlacaService extends Service{

	private WidgetUpdate myWidgetUpdate;
	private Context context;
	private NotificationManager myNotification;
	private String notifications;
	private static final int PICOYPLACA = 1;
	private Cursor allCarsCursor;
	private DbAdapter myDbAdapter; 
	private Calendar calendar;
	private Car carObj;
	private Cursor picoyplaca;
	private UpdatePicoyPlaca updateDbObj;
	private Cursor currentCityCursor;
	private int currentCityId;
	
	@Override
	public void onCreate() {
		super.onCreate();
		//Service that checks if the schedule is updated
		updateDbObj = new UpdatePicoyPlaca(this);
		updateDbObj.execute("");
		
		//TODO the notification icon should depend of the vehicle has or not picoyPlaca
		//launch notification bar
		myDbAdapter = new DbAdapter(this);
		allCarsCursor = myDbAdapter.getAllCars();
		
		//Get the currentCity
		currentCityCursor = myDbAdapter.getCurrentCity();
		currentCityCursor.moveToFirst();
		currentCityId = currentCityCursor.getInt(currentCityCursor.getColumnIndex("_id"));
		currentCityCursor.close();
		
		if(allCarsCursor.moveToFirst()){
			do{
				calendar = Calendar.getInstance();
	    		int placa = Integer.parseInt(allCarsCursor.getString(allCarsCursor.getColumnIndex("placa")));
				int icon = Integer.parseInt(allCarsCursor.getString(allCarsCursor.getColumnIndex("icon")));
				String name = allCarsCursor.getString(allCarsCursor.getColumnIndex("nombre"));
				int day = calendar.get(Calendar.DAY_OF_WEEK); 
				String number="";
				carObj = new Car(icon, placa);
				
				picoyplaca = myDbAdapter.findPicoyPlacaToday(day, currentCityId);
				if(picoyplaca.moveToFirst()){
					number = picoyplaca.getString(picoyplaca.getColumnIndex("numero"));
				}
				picoyplaca.close();
				
				if(!number.equals("null")){
					if(currentCityId==2){
						Calendar c = Calendar.getInstance();
						int hour = c.get(Calendar.HOUR_OF_DAY);
						
						if(hour<8){
							boolean found = carObj.hasPicoBogota(placa);
							if(found){
								//Launch a notification
								notifications = Context.NOTIFICATION_SERVICE;
								myNotification = (NotificationManager) getSystemService(notifications);
								
								int iconNotification = R.drawable.stop;
								CharSequence tickerText = "PicoyPlaca";
								long when = System.currentTimeMillis();
								
								Notification notification = new Notification(iconNotification, tickerText, when);
								
								context = getApplicationContext();
								CharSequence contentTitle = "Tiene Pico y Placa";
								CharSequence contentText = "El vehiculo "+name+" tiene pico y placa";
								Intent notificationIntent = new Intent(this, CurrentCars.class);
								PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

								notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
								myNotification.notify(PICOYPLACA, notification);
							}
						}
						
					}else{
						boolean found = carObj.hasPico(number);
						if(found){
							//Launch a notification
							notifications = Context.NOTIFICATION_SERVICE;
							myNotification = (NotificationManager) getSystemService(notifications);
							
							int iconNotification = R.drawable.stop;
							CharSequence tickerText = "PicoyPlaca";
							long when = System.currentTimeMillis();
							
							Notification notification = new Notification(iconNotification, tickerText, when);
							
							context = getApplicationContext();
							CharSequence contentTitle = "Tiene Pico y Placa";
							CharSequence contentText = "El vehiculo "+name+" tiene pico y placa";
							Intent notificationIntent = new Intent(this, CurrentCars.class);
							PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

							notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
							myNotification.notify(PICOYPLACA, notification);
						}
					}
				}
			}while(allCarsCursor.moveToNext());
		}
		allCarsCursor.close();
		myDbAdapter.close();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStart(intent, startId);
        callUpdateWidget(this);
		stopSelf(startId);
		return START_STICKY;
	}
	
	private void callUpdateWidget(Context context) {
		myWidgetUpdate = new WidgetUpdate();
		myWidgetUpdate.updateWidget(context, false);
    	stopSelf();
    }
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
}