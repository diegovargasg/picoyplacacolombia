package com.diegodevelopero.PicoyPlacaReminder;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

public class WidgetPicoyPlaca extends AppWidgetProvider {
	
	public static final String ACTION_WIDGET_CONFIGURE = "ConfigureWidget";
	private WidgetUpdate widgetUpdate;
	
	/*
	 * this function binds the click event in the widget and stars the activity PicoyPlacaConEsteroides.class
	 */
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		Log.d("Test", "OnUpdate");
		/**update the widget*/
		widgetUpdate = new WidgetUpdate();
		widgetUpdate.updateWidget(context, true);
		
		/**Config intent*/
		/*Intent configIntent = new Intent(context, TabWidget.class);
		configIntent.setAction(ACTION_WIDGET_CONFIGURE);
		PendingIntent configPendingIntent = PendingIntent.getActivity(context, 0, configIntent, 0);*/
		
		RemoteViews myRemoteView = new RemoteViews(context.getPackageName(),R.layout.widget_layout);
		/*myRemoteView.setOnClickPendingIntent(R.id.widget_layout, configPendingIntent);*/
		appWidgetManager.updateAppWidget(appWidgetIds, myRemoteView);
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
	}
	
	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		Log.d("Test", "Receive");
		/**update the widget*/
		widgetUpdate = new WidgetUpdate();
		widgetUpdate.updateWidget(context, true);
	}
}