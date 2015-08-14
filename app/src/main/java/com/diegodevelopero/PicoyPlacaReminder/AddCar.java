package com.diegodevelopero.PicoyPlacaReminder;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.google.android.apps.analytics.GoogleAnalyticsTracker;

public class AddCar extends Activity {
	
	/**attributes*/
	private ImageView btnIcono;
	private DbAdapter myDbAdapter;
	private Cursor recordCursor;
	private Button btnSaveCar;
	private EditText editNombre, editPlaca;
	private int icon;
	private String idCar;
	private SharedPreferences carPref;
	private ContentValues values = new ContentValues();
	private Car carColor;
	private boolean flagEdit = false;
	private int cursorId;
	private Bundle extras;
	private Button btnDelete;
	private Button btnCancel;
	GoogleAnalyticsTracker tracker;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        tracker = GoogleAnalyticsTracker.getInstance();
		tracker.startNewSession("UA-3974370-12", 30, this);
		tracker.trackPageView("/AddCars");
        
        setContentView(R.layout.add_car);
        
        // Lookup R.layout.main
 		//LinearLayout layout = (LinearLayout) findViewById(R.id.add_car);

 		// Create the adView
 		// Please replace MY_BANNER_UNIT_ID with your AdMob Publisher ID
 		AdView adView = (AdView)this.findViewById(R.id.adView);
 	    adView.loadAd(new AdRequest());

 		// Initiate a generic request to load it with an ad
 		//AdRequest request = new AdRequest();
 		//request.addTestDevice("0D22E81D0C61259DD8B6D26C156AA4B8");
 		//request.setTesting(true);

 		//adView.loadAd(request);
        
        
        /**Instance of the object DBHelper*/
    	myDbAdapter = new DbAdapter(this);
        extras = getIntent().getExtras();
        btnDelete = (Button)findViewById(R.id.btnEliminarAddCar);
        btnDelete.setEnabled(false);
        
        //Edit option
        if(extras != null){
        	flagEdit = true;
        	cursorId = extras.getInt("cursorId");
        	recordCursor = myDbAdapter.getByCar(cursorId);
        	
        	if(recordCursor.moveToFirst()){
        		//Fill the data of the car already clicked
        		//TODO can we declarate these variables before and reused for the binds?
        		editNombre = (EditText) findViewById(R.id.nombre);
				editPlaca = (EditText) findViewById(R.id.placa);
				btnIcono = (ImageView) findViewById(R.id.btnChangeIcon);
				
				editNombre.setText(recordCursor.getString(recordCursor.getColumnIndex("nombre")));
				editPlaca.setText(recordCursor.getString(recordCursor.getColumnIndex("placa")));
				icon = Integer.parseInt(recordCursor.getString(recordCursor.getColumnIndex("icon")));
		        carColor = new Car(icon, 0);
		        btnIcono.setImageResource(carColor.getCarDraw());
        	}
        	recordCursor.close();
        	myDbAdapter.close();
        	
        	btnDelete.setEnabled(true);
        	btnDelete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					tracker.trackEvent("Clicks", "Button", "delete", 0);
					myDbAdapter = new DbAdapter(AddCar.this);
					if(myDbAdapter.deleteCar(cursorId) > 0){
						Toast.makeText(AddCar.this, "Vehiculo eliminado", Toast.LENGTH_SHORT).show();
						Intent data=new Intent();
				        data.putExtra("car", "nada");
				        setResult(RESULT_OK, data);
					    finish();
					}else{
						Toast.makeText(AddCar.this, "Problemas al eliminar, intente mas tarde", Toast.LENGTH_SHORT).show();
					}
					myDbAdapter.close();
				}
			});
        }
        
        carPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor prefEditor = carPref.edit();
        prefEditor.putString("id", "0").commit();
        
        /**btnIcono Listener*/
        btnIcono = (ImageView) findViewById(R.id.btnChangeIcon);
        btnIcono.setOnClickListener(new OnClickListener() {
    	
			@Override
			public void onClick(View arg0) { //starts the activity where the user can select the icon of the car
				//TODO implement alertDialog, Temporal Structure
				tracker.trackEvent("Clicks", "Button", "icon", 0);
				startActivityForResult(new Intent(AddCar.this, ListViewCar.class), 0);
			}
		});
        
        /**btnSaveCar*/
        btnSaveCar = (Button) findViewById(R.id.btnSaveAddCar);
        btnSaveCar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				tracker.trackEvent("Clicks", "Button", "save", 0);
				myDbAdapter = new DbAdapter(AddCar.this);
				editNombre = (EditText) findViewById(R.id.nombre);
				editPlaca = (EditText) findViewById(R.id.placa);
				idCar = carPref.getString("id", "");
				values.put("nombre", editNombre.getText().toString());
				values.put("placa", editPlaca.getText().toString());
				values.put("icon", idCar);
				//validate if the inputs were filled properly
				if(editNombre.length()>0 && editPlaca.length()>0){
					if(flagEdit == true){ //we are editing an existing car
						
						if(myDbAdapter.editCar(values, "_id = "+cursorId) > 0){
							Toast.makeText(AddCar.this, "El vehiculo se edito correctamente", Toast.LENGTH_LONG).show();
							Intent data=new Intent();
					        data.putExtra("car", "nada");
					        setResult(RESULT_OK, data);
						    finish();
						}else{
							Toast.makeText(AddCar.this, "Problemas al intentar guardar, intente mas tarde.", Toast.LENGTH_LONG).show();
						}
						
					}else{//we are saving a new car
						
						if(myDbAdapter.saveNewCar(values) > 0){
							Toast.makeText(AddCar.this, "El vehiculo se guardo correctamente", Toast.LENGTH_LONG).show();
							Intent data=new Intent();
					        data.putExtra("car", "nada");
					        setResult(RESULT_OK, data);
						    finish();
						}else{
							Toast.makeText(AddCar.this, "Problemas al intentar guardar, intente mas tarde.", Toast.LENGTH_LONG).show();
						}
					}
					myDbAdapter.close();
				}else{
					Toast.makeText(AddCar.this, "Debe llenar todos los campos.", Toast.LENGTH_SHORT).show();
				}
			}
		});
        
        btnCancel = (Button)findViewById(R.id.btnCancelAddCar);
        btnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				tracker.trackEvent("Clicks", "Button", "cancel", 0);
				finish();
			}
		});
        myDbAdapter.close();
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK){//Save the selected car in sharedPreferences
			SharedPreferences.Editor prefEditor = carPref.edit();
	        prefEditor.putString("id", data.getExtras().getString("car")).commit();
	        int icon = Integer.parseInt(data.getExtras().getString("car"));
	        carColor = new Car(icon, 0);
	        tracker.trackEvent("Clicks", "option", "carColor"+carColor.getCarDraw(), 0);
	        btnIcono.setImageResource(carColor.getCarDraw());
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		// Stop the tracker when it is no longer needed.
		tracker.stopSession();
	}
}