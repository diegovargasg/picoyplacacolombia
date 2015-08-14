package com.diegodevelopero.PicoyPlacaReminder;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ListViewCar extends ListActivity{
	
	final static ArrayList<HashMap<String, ?>> data = new ArrayList<HashMap<String, ?>>();
	private Car carObj;
	GoogleAnalyticsTracker tracker;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tracker = GoogleAnalyticsTracker.getInstance();
		tracker.startNewSession("UA-3974370-12", 30, this);
		tracker.trackPageView("/ListViewCar");
		data.clear();
		String[] carColors = getResources().getStringArray(R.array.color_cars);
		HashMap<String, Object> row;
		int i = 0;
		for(String tmp:carColors){
			carObj = new Car(i, 0);
			row  = new HashMap<String, Object>();
	        row.put("icon", carObj.getCarDraw());
	        row.put("name", tmp);
	        data.add(row);
	        i++;
		}
		SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.icon_list_item, new String[] {"icon", "name"}, new int[]{R.id.icon_car, R.id.icon_name});
		setListAdapter(adapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Object o = this.getListAdapter().getItemId(position);
		String keyword = o.toString();
        Intent data=new Intent();
        data.putExtra("car", keyword);
        tracker.trackEvent("Clicks", "Option", "Vehiculo"+keyword, 0);
        setResult(RESULT_OK, data);
	    finish();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		// Stop the tracker when it is no longer needed.
		tracker.stopSession();
	}
}