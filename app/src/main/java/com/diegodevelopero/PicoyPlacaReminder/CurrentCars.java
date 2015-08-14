package com.diegodevelopero.PicoyPlacaReminder;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.TextView;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.google.android.apps.analytics.GoogleAnalyticsTracker;

public class CurrentCars extends Activity{
	
	private Button addCar;
	private Button btnCiudad;
	private DbAdapter myDbAdapter;
	private DbAdapter myDbCity;
	private Cursor allCarsCursor;
	private ListView listContent;
	private int flag = 0;
	private Cursor picoyplaca;
	private String number="";
	private WidgetUpdate myWidgetUpdate;
	private int numberOfCars;
	private TextView numberCurrentCars;
	private Cursor currentCityCursor;
	private int currentCityId;
	private String currentCityName;
	private WidgetUpdate widgetUpdate;
	GoogleAnalyticsTracker tracker;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.current_cars);
		
		// Lookup R.layout.main
		LinearLayout layout = (LinearLayout) findViewById(R.id.current_cars);

		// Create the adView
		// Please replace MY_BANNER_UNIT_ID with your AdMob Publisher ID
		AdView adView = new AdView(this, AdSize.BANNER, "a14f6480b45f38a");

		// Add the adView to it
		layout.addView(adView);

		// Initiate a generic request to load it with an ad
		AdRequest request = new AdRequest();
		//request.addTestDevice("0D22E81D0C61259DD8B6D26C156AA4B8");
		//request.setTesting(true);

		adView.loadAd(request);
		
		updateCurrentList();
		
		tracker = GoogleAnalyticsTracker.getInstance();
		tracker.startNewSession("UA-3974370-12", 30, this);
		tracker.trackPageView("/CurrentCars");
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK){
			myDbAdapter.close();
			updateCurrentList();
			//Update the widget
			myWidgetUpdate = new WidgetUpdate();
			myWidgetUpdate.updateWidget(CurrentCars.this, true);
		}
	}
	
	private void updateCurrentList(){
		
		listContent = (ListView)findViewById(R.id.contentlist);
		numberCurrentCars = (TextView) findViewById(R.id.number_curren_cars);
		
		//Instance of the object DBHelper/
		myDbAdapter = new DbAdapter(this);
		
		allCarsCursor = myDbAdapter.getAllCars();
		startManagingCursor(allCarsCursor);
		numberOfCars = allCarsCursor.getCount();
		numberCurrentCars.setText("Mostrando "+numberOfCars+" vehiculos");
		final Calendar c = Calendar.getInstance();
		int day = c.get(Calendar.DAY_OF_WEEK);
		
		//Get the currentCity
		currentCityCursor = myDbAdapter.getCurrentCity();
		currentCityCursor.moveToFirst();
		currentCityId = currentCityCursor.getInt(currentCityCursor.getColumnIndex("_id"));
		currentCityName = currentCityCursor.getString(currentCityCursor.getColumnIndex("name"));
		currentCityCursor.close();
		
		picoyplaca = myDbAdapter.findPicoyPlacaToday(day, currentCityId);
		startManagingCursor(picoyplaca);
		if(picoyplaca.moveToFirst()){
			number = picoyplaca.getString(picoyplaca.getColumnIndex("numero"));
		}
		
		//TODO here use constants
		String[] from = new String[] {"_id", "nombre", "placa", "icon", "icon"};
		int[] to = new int[] {R.id.id_entry, R.id.name_entry, R.id.plate_entry, R.id.car_block, R.id.car_entry};
		SimpleCursorAdapter mAdapter = new SimpleCursorAdapter(CurrentCars.this, R.layout.current_cars_entry, allCarsCursor, from, to);
		mAdapter.setViewBinder(new ViewBinder() {
			
			@Override
			public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
				if(columnIndex == cursor.getColumnIndex("icon")){
					Car carObj;
					int placa = Integer.parseInt(cursor.getString(cursor.getColumnIndex("placa")));
					int icon = Integer.parseInt(cursor.getString(cursor.getColumnIndex("icon")));
					carObj = new Car(icon, placa);
					ImageView imgView = (ImageView) view;
					imgView.setImageResource(carObj.getCarDraw());
					
					if(currentCityId==2){//Bogota
						
						if(!number.equals("null")){
							boolean found = carObj.hasPicoBogota(placa);
							if(flag == 0){
								if(found){
									imgView.setImageResource(R.drawable.stop);
								}else{
									imgView.setImageResource(R.drawable.go);
								}
								flag++;
							}else{
								imgView.setImageResource(carObj.getCarDraw());
								flag=0;
							}
							return true;
						}else{
							if(flag == 0){
								imgView.setImageResource(R.drawable.go);
								flag++;
							}else{
								imgView.setImageResource(carObj.getCarDraw());
								flag=0;
							}
							return true;
						}
						
					}else{
						if(!number.equals("null")){
							boolean found = carObj.hasPico(number);
							if(flag == 0){
								if(found){
									imgView.setImageResource(R.drawable.stop);
								}else{
									imgView.setImageResource(R.drawable.go);
								}
								flag++;
							}else{
								imgView.setImageResource(carObj.getCarDraw());
								flag=0;
							}
							return true;
						}else{
							if(flag == 0){
								imgView.setImageResource(R.drawable.go);
								flag++;
							}else{
								imgView.setImageResource(carObj.getCarDraw());
								flag=0;
							}
							return true;
						}
					}
					
					/*if(!number.equals("null")){
						boolean found = carObj.hasPico(number);
						if(flag == 0){
							if(found){
								imgView.setImageResource(R.drawable.stop);
							}else{
								imgView.setImageResource(R.drawable.go);
							}
							flag++;
						}else{
							imgView.setImageResource(carObj.getCarDraw());
							flag=0;
						}
						return true;
					}else{
						if(flag == 0){
							imgView.setImageResource(R.drawable.go);
							flag++;
						}else{
							imgView.setImageResource(carObj.getCarDraw());
							flag=0;
						}
						return true;
					}*/
				}
				return false;
			}
		});
		
		listContent.setAdapter(mAdapter);
		listContent.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				
				TextView selection;
				selection = (TextView) arg1.findViewById(R.id.id_entry);
				
				Intent intent = new Intent(CurrentCars.this, AddCar.class);
				intent.putExtra("cursorId", Integer.parseInt(selection.getText().toString()));
				startActivityForResult(intent, 0);
			}
		});
		
		addCar = (Button) findViewById(R.id.btnAddCar);
		addCar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				tracker.trackEvent("Clicks", "Button", "addCar", 0);
				startActivityForResult(new Intent(CurrentCars.this, AddCar.class), 0);
			}
		});
		
        btnCiudad = (Button) findViewById(R.id.btnciudad);
        btnCiudad.setText(currentCityName);
        btnCiudad.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				tracker.trackEvent("Clicks", "Button", "seleccionarCiudad", 0);
				final CharSequence[] items = {"Barranquilla", "Bogota", "Bucaramanga", "Cali", "Cartagena", "Medellin"};
				AlertDialog.Builder builder = new AlertDialog.Builder(CurrentCars.this);
				builder.setTitle("Seleccione una ciudad");
				builder.setItems(items, new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog, int item) {
				    	//Save here the new city
				    	ContentValues values = new ContentValues();
				    	values.put("activo", 1);
				    	tracker.trackEvent("Clicks", "Option", ""+items[item], 0);
				    	btnCiudad.setText(items[item]);
				    	item++;
				    	myDbCity = new DbAdapter(CurrentCars.this);
				    	myDbCity.updateCurrentCity(values, item);
				    	myDbCity.close();
				    	dialog.dismiss();
				    	//update the widget
				    	widgetUpdate = new WidgetUpdate();
				    	widgetUpdate.updateWidget(CurrentCars.this, true);
				    	//update the currentList
				    	myDbAdapter.close();
				    	updateCurrentList();
				    }
				});
				AlertDialog alert = builder.create();
				alert.show();
			}
		});
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		myDbAdapter.close();
		// Stop the tracker when it is no longer needed.
		tracker.stopSession();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		myDbAdapter.close();
	}
	
	@Override
	protected void onRestart() {
		super.onStart();
		myDbAdapter.close();
		updateCurrentList();
	}
}