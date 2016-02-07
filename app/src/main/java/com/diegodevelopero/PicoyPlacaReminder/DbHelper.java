package com.diegodevelopero.PicoyPlacaReminder;

import java.util.Random;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public class DbHelper extends SQLiteOpenHelper{
	//TODO how to use this constants in the rest of the project?
	/**attributes*/
	/**
	 * 1|Barranquilla|0
		2|Bogota|1
		3|Bucaramanga|0
		4|Cali|0
		5|Cartagena|0
		6|Medellin|0
	 *
	 */
	
	/**
	Version 2, 01/23/2012 cambio horario en cal y car
	Version 3, 02/04/2012 cambio horario en Med
	Version 4, 02/04/2012 cambio horario en Buc
	Version 5, 28/06/2012 cambio horario en Buc
	Version 6, 17/07/2012 cambio horario en medellin
	Version 9, 19/01/2013 cambio horario en Varias ciudades
	Version 10, 13/02/2013 cambio horario en medellin
	Version 11, 07/08/2013 cambio horario en bucaramanga
	Version 12, 19/01/2014 cambio horario en varias ciudades
	Version 13, 05/02/2014 cambio horario en Medellin
	Version 14, 01/08/2014 cambio horario en Varias Ciudades
	Version 15, 26/08/2015 cambio horario en Medellin
	*/
	
	protected static final String DB_NAME = "picoyplaca";
	protected static final int DB_VERSION = 25;
	/**Table City*/
	protected static final String DB_TABLE_CIUDAD = "ciudad";
	protected static final String ID_CIUDAD = "_id";
	protected static final String NAME = "name";
	protected static final String ACTIVO = "activo";
	/**Table PicoyPlaca*/
	protected static final String DB_TABLE_PICOYPLACA = "picoyplaca";
	protected static final String ID_PICOYPLACA = "_id";
	protected static final String CIUDAD = "ciudad";
	protected static final String NUMERO = "numero";
	protected static final String DIA = "dia";
	/**Table vehicle*/
	protected static final String DB_TABLE_VEHICLE = "vehiculos";
	protected static final String ID_VEHICLE = "_id";
	protected static final String NAME_VEHICLE = "nombre";
	protected static final String PLACA_VEHICLE = "placa";
	protected static final String ICON_VEHICLE = "icon";
	/**Table dbVersion*/
	protected static final String DB_TABLE_DBVERSION = "dbversion";
	protected static final String ID_VERSION = "_id";
	protected static final String VERSION = "version";
	/**Table uniqID*/
	protected static final String DB_TABLE_UNIQID = "tableuniqid";
	protected static final String ID_UNIQID = "uniqid";
	
	public DbHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		//Create table Ciudad
		String sql = "CREATE TABLE "+DB_TABLE_CIUDAD+"" +
					 	"("+ID_CIUDAD+" INTEGER PRIMARY KEY AUTOINCREMENT," +
					 			""+NAME+" TEXT, " +
			 					""+ACTIVO+" INTEGER)";
		db.execSQL(sql);
		
		String[] cities = {"Barranquilla","Bogota", "Bucaramanga", "Cali", "Cartagena", "Medellin"};
		SQLiteStatement stm = db.compileStatement("INSERT INTO "+DB_TABLE_CIUDAD+" VALUES (?,?,?)");

		for(String tmp:cities){
			stm.bindNull(1);
			stm.bindString(2, tmp);
			stm.bindString(3, tmp == "Bogota" ? "1" : "0");
			stm.executeInsert();
			stm.clearBindings();
		}
		
		//Create Table PicoyPlaca
		sql = "CREATE TABLE "+DB_TABLE_PICOYPLACA+"" +
			 	"("+ID_PICOYPLACA+" INTEGER PRIMARY KEY AUTOINCREMENT," +
			 			""+CIUDAD+" INTEGER, " +
			 			""+DIA+" INTEGER, " +
	 					""+NUMERO+" TEXT)";
		db.execSQL(sql);
		
		int[] days = {2, 3, 4, 5, 6, 7, 1};
		
		String[] picoyplacaBar = {"null", "null", "null", "null", "null", "null", "null"};
		String[] picoyplacaBog = {"5-6-7-8", "9-0-1-2", "3-4-5-6", "7-8-9-0", "1-2-3-4", "null", "null"};
		String[] picoyplacaBuc = {"3-4", "5-6", "7-8", "9-0", "1-2", "null", "null"};
		String[] picoyplacaCal = {"3-4", "5-6", "7-8", "9-0", "1-2", "null", "null"};
		String[] picoyplacaCar = {"1-2", "3-4", "5-6", "7-8", "9-0", "null", "null"};
		String[] picoyplacaMed = {"0-1-2-3", "4-5-6-7", "8-9-0-1", "2-3-4-5", "6-7-8-9", "null", "null"};
		String[][] citiesPP = {picoyplacaBar, picoyplacaBog, picoyplacaBuc, picoyplacaCal, picoyplacaCar, picoyplacaMed};
		
		int j=0;
		int i = 1;
		stm = db.compileStatement("INSERT INTO "+DB_TABLE_PICOYPLACA+" VALUES (?,?,?,?)");
		
		for(String[] tmp2:citiesPP){
			j = 0;
			for(String tmp3:tmp2){
				
				stm.bindNull(1);
				stm.bindLong(2, i);
				stm.bindLong(3, days[j]);
				stm.bindString(4, tmp3);
				stm.executeInsert();
				stm.clearBindings();
				j++;
			}
			i++;
		}
		
		//Create Table vehicle
		sql = "CREATE TABLE IF NOT EXISTS "+DB_TABLE_VEHICLE+"" +
			 	"("+ID_VEHICLE+" INTEGER PRIMARY KEY AUTOINCREMENT," +
			 			""+NAME_VEHICLE+" TEXT, " +
			 			""+PLACA_VEHICLE+" INTEGER, " +
	 					""+ICON_VEHICLE+" INTEGER)";
		db.execSQL(sql);
		
		//Create Table DbVersion
		sql = "CREATE TABLE IF NOT EXISTS "+DB_TABLE_DBVERSION+"" +
			 	"("+ID_VERSION+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
			 			""+VERSION+" INTEGER)";
		db.execSQL(sql);
		
		stm = db.compileStatement("INSERT INTO "+DB_TABLE_DBVERSION+" VALUES (?,?)");
		stm.bindNull(1);
		stm.bindLong(2, DB_VERSION);
		stm.executeInsert();
		stm.clearBindings();
		
		//Create Table vehicle
		sql = "CREATE TABLE IF NOT EXISTS "+DB_TABLE_UNIQID+"" +
			 	"("+ID_UNIQID+" INTEGER)";
		db.execSQL(sql);
		
		Random random = new Random();
		int tmpRand = random.nextInt(5000);
		
		stm = db.compileStatement("INSERT INTO "+DB_TABLE_UNIQID+" VALUES (?)");
		stm.bindLong(1, tmpRand);
		stm.executeInsert();
		stm.clearBindings();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String sql = "DROP TABLE IF EXISTS "+DB_TABLE_CIUDAD;
		db.execSQL(sql);
		sql = "DROP TABLE IF EXISTS "+DB_TABLE_PICOYPLACA;
		db.execSQL(sql);
		sql = "DROP TABLE IF EXISTS "+DB_TABLE_DBVERSION;
		db.execSQL(sql);
		this.onCreate(db);
	}
}