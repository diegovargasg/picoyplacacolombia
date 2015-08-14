package com.diegodevelopero.PicoyPlacaReminder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DbAdapter {
	
	/**Attributes*/
	private DbHelper myDbHelper;
	private SQLiteDatabase db;
	
	public DbAdapter(Context context){
		this.myDbHelper = new DbHelper(context);
		db = myDbHelper.getWritableDatabase();
	}
	
	public long fillPicoyPlaca(ContentValues values){
		return db.insert(myDbHelper.DB_TABLE_PICOYPLACA, null, values);
	}
	
	public void close(){
		this.myDbHelper.close();
	}
	
	public Cursor getAllCars(){
		return db.query(myDbHelper.DB_TABLE_VEHICLE,  null, null, null, null, null, null);
	}
	
	public Cursor getByCar(int cursorId){
		return db.query(myDbHelper.DB_TABLE_VEHICLE,  null, "_id"+"="+cursorId, null, null, null, null);
	}
	
	public long saveNewCar(ContentValues values){
		return db.insert(myDbHelper.DB_TABLE_VEHICLE, null, values);
	}
	
	public int editCar(ContentValues values, String whereIndex){
		return db.update(myDbHelper.DB_TABLE_VEHICLE, values, whereIndex, null);
	}
	
	public Cursor findPicoyPlacaToday(int arg_dia, int arg_ciudad){
		return db.query(myDbHelper.DB_TABLE_PICOYPLACA,  null, myDbHelper.DIA+"="+arg_dia+" AND "+myDbHelper.CIUDAD+"="+arg_ciudad, null, null, null, null);
	}
	
	public int deleteCar(int carId){
		return db.delete(myDbHelper.DB_TABLE_VEHICLE,"_id="+carId, null);
	}
	
	public int deletePicoyPlacaByCity(String cityId){
		return db.delete(myDbHelper.DB_TABLE_PICOYPLACA, myDbHelper.CIUDAD+"="+cityId, null); 
	}
	
	public long saveNewPicoyPlaca(ContentValues values){
		return db.insert(myDbHelper.DB_TABLE_PICOYPLACA, null, values);
	}
	
	public Cursor getDbVersion(){
		return db.query(myDbHelper.DB_TABLE_DBVERSION,  null, myDbHelper.ID_VERSION+"=1", null, null, null, null);
	}
	
	public int updateDbVersion(ContentValues values){
		return db.update(myDbHelper.DB_TABLE_DBVERSION, values, myDbHelper.ID_VERSION+"=1", null);
	}
	
	public Cursor getCurrentCity(){
		return db.query(myDbHelper.DB_TABLE_CIUDAD,  null, myDbHelper.ACTIVO+"=1", null, null, null, null);
	}
	
	public int updateCurrentCity(ContentValues values, int arg_id){
		//TODO look into another way apart of execSQL
		db.execSQL("UPDATE "+myDbHelper.DB_TABLE_CIUDAD+" SET "+myDbHelper.ACTIVO+"=0");
		return db.update(myDbHelper.DB_TABLE_CIUDAD, values, myDbHelper.ID_CIUDAD+"="+arg_id, null);
	}
}