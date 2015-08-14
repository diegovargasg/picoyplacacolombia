package com.diegodevelopero.PicoyPlacaReminder;

import java.util.Calendar;
import java.util.Random;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.RemoteViews;

public class WidgetUpdate {
	
	private DbAdapter myDbAdapter;
	private RemoteViews myRemoteView;
	private Cursor allCarsCursor;
	private AppWidgetManager appWidgetManager;
	private Calendar calendar;
	private Car carObj;
	private Cursor picoyplaca;
	private String number;
	private int day;
	private Cursor currentCityCursor;
	private int currentCityId = 0;
	public static final String ACTION_WIDGET_CONFIGURE = "ConfigureWidget";
	private Context mContext;
	
	public void updateWidget(Context context, boolean serviceUpdate){
		mContext = context;
		//Get the instance of the widget
		myRemoteView = new RemoteViews(context.getPackageName(),R.layout.widget_layout);
		ComponentName thisWidget = new ComponentName(context, WidgetPicoyPlaca.class);
		appWidgetManager = AppWidgetManager.getInstance(context);
		
		/**Config intent*/
		Intent configIntent = new Intent(context, TabWidget.class);
		configIntent.setAction(ACTION_WIDGET_CONFIGURE);
		PendingIntent configPendingIntent = PendingIntent.getActivity(context, 0, configIntent, 0);
		myRemoteView = new RemoteViews(context.getPackageName(),R.layout.widget_layout);
		myRemoteView.setOnClickPendingIntent(R.id.widget_layout, configPendingIntent);
		
		//DbAdapter and Cursor
		myDbAdapter = new DbAdapter(context);
		allCarsCursor = myDbAdapter.getAllCars();
		
		int countCars = allCarsCursor.getCount();
		int carPos = 0;
		if(countCars > 0){
			Random random = new Random();
			carPos = random.nextInt(countCars);
		}else{
			carPos = countCars;
		}
		if(allCarsCursor.moveToPosition(carPos)){
			calendar = Calendar.getInstance();
    		int placa = Integer.parseInt(allCarsCursor.getString(allCarsCursor.getColumnIndex("placa")));
			int icon = Integer.parseInt(allCarsCursor.getString(allCarsCursor.getColumnIndex("icon")));
			day = calendar.get(Calendar.DAY_OF_WEEK); 
			carObj = new Car(icon, placa);
			
			//Get the currentCity
			currentCityCursor = myDbAdapter.getCurrentCity();
			currentCityCursor.moveToFirst();
			currentCityId = currentCityCursor.getInt(currentCityCursor.getColumnIndex("_id"));
			currentCityCursor.close();
			
			//Change the bk of the image city
			switch (currentCityId) {
			case 1:
				myRemoteView.setImageViewResource(R.id.cityImg, R.drawable.barranquilla);
				break;
			case 2:
				myRemoteView.setImageViewResource(R.id.cityImg, R.drawable.bogota);
				break;
			case 3:
				myRemoteView.setImageViewResource(R.id.cityImg, R.drawable.bucaramanga);
				break;
			case 4:
				myRemoteView.setImageViewResource(R.id.cityImg, R.drawable.cali);
				break;
			case 5:
				myRemoteView.setImageViewResource(R.id.cityImg, R.drawable.cartagena);
				break;
			case 6:
				myRemoteView.setImageViewResource(R.id.cityImg, R.drawable.medellin);
				break;
			default:
				myRemoteView.setImageViewResource(R.id.cityImg, R.drawable.bogota);
				break;
			}
			
			//Find the picoyPlaca for that city
			if(currentCityId==2){//For Bogota
				picoyplaca = myDbAdapter.findPicoyPlacaToday(day, currentCityId);
				if(picoyplaca.moveToFirst()){
					number = picoyplaca.getString(picoyplaca.getColumnIndex("numero"));
				}
				if(!number.equals("null")){
					boolean found = carObj.hasPicoBogota(placa);
					if(found){
						Calendar c = Calendar.getInstance(); 
						int hour = c.get(Calendar.HOUR_OF_DAY);
						int minute = c.get(Calendar.MINUTE);
						Log.d("Test", "hour: "+hour);
						Log.d("Test", "hour: "+minute);
						
						if(hour>=6 && hour<=8){
							if(hour==8){
								if(minute>=30){
									Log.d("Test", "No Pico Y placa 1");
									myRemoteView.setImageViewResource(R.id.car_block, 0);
								}else{
									Log.d("Test", "Pico Y placa 2");
									myRemoteView.setImageViewResource(R.id.car_block, R.drawable.block);
								}
							}else{
								Log.d("Test", "Pico Y placa 3");
								myRemoteView.setImageViewResource(R.id.car_block, R.drawable.block);
							}
						}else if(hour>=15 && hour<=19){
							if(hour==19){
								if(minute>=30){
									Log.d("Test", "No Pico Y placa 4");
									myRemoteView.setImageViewResource(R.id.car_block, 0);
								}else{
									Log.d("Test", "Pico Y placa 5");
									myRemoteView.setImageViewResource(R.id.car_block, R.drawable.block);
								}
							}else{
								Log.d("Test", "Pico Y placa 6");
								myRemoteView.setImageViewResource(R.id.car_block, R.drawable.block);
							}
						}else{
							Log.d("Test", "No Pico Y placa 7");
							myRemoteView.setImageViewResource(R.id.car_block, 0);
						}
					}else{
						Log.d("Test", "No Pico Y placa 8");
						myRemoteView.setImageViewResource(R.id.car_block, 0);
					}
					myRemoteView.setImageViewResource(R.id.car, carObj.getCarDraw());
					calendar = Calendar.getInstance();
					calendar.setTimeInMillis(System.currentTimeMillis());
					int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
					if(currentDay%2 == 0){
						myRemoteView.setTextViewText(R.id.numero, "Pares");
						myRemoteView.setFloat(R.id.numero, "setTextSize", 27);
					}else{
						myRemoteView.setTextViewText(R.id.numero, "Impares");
						myRemoteView.setFloat(R.id.numero, "setTextSize", 20);
					}
					myRemoteView.setTextViewText(R.id.touchBegin, "");
				}else{
					//There's no PicoyPlaca this day
					myRemoteView.setImageViewResource(R.id.car_block, 0);
					myRemoteView.setImageViewResource(R.id.car, carObj.getCarDraw());
					myRemoteView.setTextViewText(R.id.numero, "No hay PicoyPlaca");
					myRemoteView.setFloat(R.id.numero, "setTextSize", 14);
					myRemoteView.setTextViewText(R.id.touchBegin, "");
				}
			}else{
				picoyplaca = myDbAdapter.findPicoyPlacaToday(day, currentCityId);
				if(picoyplaca.moveToFirst()){
					number = picoyplaca.getString(picoyplaca.getColumnIndex("numero"));
				}
				if(!number.equals("null")){
					boolean found = carObj.hasPico(number);
					if(found){
						myRemoteView.setImageViewResource(R.id.car_block, R.drawable.block);
					}else{
						myRemoteView.setImageViewResource(R.id.car_block, 0);
					}
					myRemoteView.setImageViewResource(R.id.car, carObj.getCarDraw());
					myRemoteView.setTextViewText(R.id.numero, carObj.picoyplacaTodayParse(number));
					myRemoteView.setFloat(R.id.numero, "setTextSize", 27);
					myRemoteView.setTextViewText(R.id.touchBegin, "");
				}else{
					//There's no PicoyPlaca this day
					myRemoteView.setImageViewResource(R.id.car_block, 0);
					myRemoteView.setImageViewResource(R.id.car, carObj.getCarDraw());
					myRemoteView.setTextViewText(R.id.numero, "No hay PicoyPlaca");
					myRemoteView.setFloat(R.id.numero, "setTextSize", 14);
					myRemoteView.setTextViewText(R.id.touchBegin, "");
				}
			}
			picoyplaca.close();
		}else{
			//there are not vehicles saved in the DB
    		myRemoteView.setImageViewResource(R.id.car, 0);
    		myRemoteView.setTextViewText(R.id.numero, "");
    		myRemoteView.setImageViewResource(R.id.car_block, 0);
    		myRemoteView.setImageViewResource(R.id.cityImg, 0);
    		myRemoteView.setTextViewText(R.id.touchBegin, "Touch para empezar");
		}
		allCarsCursor.close();
		myDbAdapter.close();
		appWidgetManager.updateAppWidget(thisWidget, myRemoteView);
		if(serviceUpdate){
			updateAlarm();
		}
	}
	
	private void destroyAlarm(){
		Intent intentstop = new Intent(mContext, PicoyPlacaService.class);
        PendingIntent senderstop = PendingIntent.getService(mContext.getApplicationContext(), 1, intentstop, 0);
        AlarmManager alarmManagerstop = (AlarmManager) mContext.getSystemService(android.content.Context.ALARM_SERVICE);
        alarmManagerstop.cancel(senderstop);
        
        senderstop = PendingIntent.getService(mContext.getApplicationContext(), 2, intentstop, 0);
        alarmManagerstop = (AlarmManager) mContext.getSystemService(android.content.Context.ALARM_SERVICE);
        alarmManagerstop.cancel(senderstop);
        
        senderstop = PendingIntent.getService(mContext.getApplicationContext(), 3, intentstop, 0);
        alarmManagerstop = (AlarmManager) mContext.getSystemService(android.content.Context.ALARM_SERVICE);
        alarmManagerstop.cancel(senderstop);
        
        senderstop = PendingIntent.getService(mContext.getApplicationContext(), 4, intentstop, 0);
        alarmManagerstop = (AlarmManager) mContext.getSystemService(android.content.Context.ALARM_SERVICE);
        alarmManagerstop.cancel(senderstop);
        
        senderstop = PendingIntent.getService(mContext.getApplicationContext(), 5, intentstop, 0);
        alarmManagerstop = (AlarmManager) mContext.getSystemService(android.content.Context.ALARM_SERVICE);
        alarmManagerstop.cancel(senderstop);
        
        Log.d("Test", "Alarm destroyed");
	}
	
	private void updateAlarm(){
		destroyAlarm();
		calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		//service intent
		Intent serviceIntent = new Intent(mContext.getApplicationContext(), PicoyPlacaService.class);
		PendingIntent servicePendingIntent = PendingIntent.getService(mContext.getApplicationContext(), 1, serviceIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager mAlarmManager = (AlarmManager) mContext.getSystemService(android.content.Context.ALARM_SERVICE);
		
		switch (currentCityId) {
		case 1://Barranquilla - No Aplica
			//Calendar Obj
			/*calendar.set(Calendar.HOUR_OF_DAY, 24);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			
			//service intent
			Intent serviceIntent = new Intent(mContext.getApplicationContext(), PicoyPlacaService.class);
			PendingIntent servicePendingIntent = PendingIntent.getService(mContext.getApplicationContext(), 1, serviceIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			AlarmManager mAlarmManager = (AlarmManager) mContext.getSystemService(android.content.Context.ALARM_SERVICE);
			mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 28800000, servicePendingIntent);//8 hrs will call the service*/
			break;
		case 2:
			//Calendar Obj
			calendar.set(Calendar.HOUR_OF_DAY, 6);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			
			//service intent
			Intent serviceIntent1 = new Intent(mContext.getApplicationContext(), PicoyPlacaService.class);
			PendingIntent servicePendingIntent1 = PendingIntent.getService(mContext.getApplicationContext(), 2, serviceIntent1, PendingIntent.FLAG_UPDATE_CURRENT);
			AlarmManager mAlarmManager1 = (AlarmManager) mContext.getSystemService(android.content.Context.ALARM_SERVICE);
			mAlarmManager1.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 86400000, servicePendingIntent1);
			
			//Calendar Obj
			calendar.clear();
			calendar.set(Calendar.HOUR_OF_DAY, 8);
			calendar.set(Calendar.MINUTE, 31);
			calendar.set(Calendar.SECOND, 0);
			
			//service intent
			Intent serviceIntent2 = new Intent(mContext.getApplicationContext(), PicoyPlacaService.class);
			PendingIntent servicePendingIntent2 = PendingIntent.getService(mContext.getApplicationContext(), 3, serviceIntent2, PendingIntent.FLAG_UPDATE_CURRENT);
			AlarmManager mAlarmManager2 = (AlarmManager) mContext.getSystemService(android.content.Context.ALARM_SERVICE);
			mAlarmManager2.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 86400000, servicePendingIntent2);
			
			
			//Calendar Obj
			calendar.clear();
			calendar.set(Calendar.HOUR_OF_DAY, 15);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			
			//service intent
			Intent serviceIntent3 = new Intent(mContext.getApplicationContext(), PicoyPlacaService.class);
			PendingIntent servicePendingIntent3 = PendingIntent.getService(mContext.getApplicationContext(), 4, serviceIntent3, PendingIntent.FLAG_UPDATE_CURRENT);
			AlarmManager mAlarmManager3 = (AlarmManager) mContext.getSystemService(android.content.Context.ALARM_SERVICE);
			mAlarmManager3.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 86400000, servicePendingIntent3);
			
			//Calendar Obj
			calendar.clear();
			calendar.set(Calendar.HOUR_OF_DAY, 19);
			calendar.set(Calendar.MINUTE, 31);
			calendar.set(Calendar.SECOND, 0);
			
			//service intent
			Intent serviceIntent4 = new Intent(mContext.getApplicationContext(), PicoyPlacaService.class);
			PendingIntent servicePendingIntent4 = PendingIntent.getService(mContext.getApplicationContext(), 5, serviceIntent4, PendingIntent.FLAG_UPDATE_CURRENT);
			AlarmManager mAlarmManager4 = (AlarmManager) mContext.getSystemService(android.content.Context.ALARM_SERVICE);
			mAlarmManager4.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 86400000, servicePendingIntent4);
			
			break;
		case 3://Bucaramanga
			calendar.set(Calendar.HOUR_OF_DAY, 24);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			
			mAlarmManager = (AlarmManager) mContext.getSystemService(android.content.Context.ALARM_SERVICE);
			mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),  86400000, servicePendingIntent);//24 hrs will call the service
			break;
		case 4://Cali
			calendar.set(Calendar.HOUR_OF_DAY, 24);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			
			mAlarmManager = (AlarmManager) mContext.getSystemService(android.content.Context.ALARM_SERVICE);
			mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),  86400000, servicePendingIntent);//24 hrs will call the service
			break;
		case 5://Cartagena
			calendar.set(Calendar.HOUR_OF_DAY, 24);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			
			mAlarmManager = (AlarmManager) mContext.getSystemService(android.content.Context.ALARM_SERVICE);
			mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),  86400000, servicePendingIntent);//24 hrs will call the service
			break;
		case 6://Medellin
			calendar.set(Calendar.HOUR_OF_DAY, 24);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			
			mAlarmManager = (AlarmManager) mContext.getSystemService(android.content.Context.ALARM_SERVICE);
			mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),  86400000, servicePendingIntent);//24 hrs will call the service
			break;
		default:
			//myRemoteView.setImageViewResource(R.id.cityImg, R.drawable.bogota);
			Log.d("Test", "Bogota");
			break;
		}
	}
}