package com.diegodevelopero.PicoyPlacaReminder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

public class UpdatePicoyPlaca extends AsyncTask<String, Void, String>{
	
	private String dbNewParams;
	private String flagUpdate = "false";
	private DbAdapter dbAdapter;
	private Context context;
	private ContentValues values = new ContentValues();
	private ContentValues updateValues = new ContentValues();
	private String dbVersionServer;
	private String dbVersionMobile;
	private Cursor dbVersionCursor;
	private WidgetUpdate widgetUpdate;
	
	public UpdatePicoyPlaca(Context context) {
		this.context = context;
	}

	@Override
	protected String doInBackground(String... params) {
		/*
		// We no longer support this update service
		//get the dbVersionMobile
		dbAdapter = new DbAdapter(this.context);
		dbVersionCursor = dbAdapter.getDbVersion();
		
		if(dbVersionCursor.moveToFirst()){
			dbVersionMobile = dbVersionCursor.getString(dbVersionCursor.getColumnIndex("version"));
		}
		dbVersionCursor.close();
		//Prepare the first http request which identifies if it is necessary to update
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost("http://diegoadvanced.com/android/picoyplaca/updateService.php");
		List<NameValuePair> paramsPost = new ArrayList<NameValuePair>();
		paramsPost.add(new BasicNameValuePair("checkDbVersion", "true"));
		
		try {
	        // Execute HTTP Post Request
			UrlEncodedFormEntity ent = new UrlEncodedFormEntity(paramsPost, HTTP.UTF_8);
			httpPost.setEntity(ent);
			HttpResponse response = httpClient.execute(httpPost);
			
			if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
	        	dbVersionServer = EntityUtils.toString(response.getEntity());
	        	if(!dbVersionServer.equals(dbVersionMobile)){
	        		//the DbVersion is different, update the current version
	        		List<NameValuePair> paramsPostUpdate = new ArrayList<NameValuePair>();
	        		paramsPostUpdate.add(new BasicNameValuePair("updateDbVersion", "true"));
	        		
	        		try {
	        			
	    				UrlEncodedFormEntity entUpdate = new UrlEncodedFormEntity(paramsPostUpdate, HTTP.UTF_8);
	    				httpPost.setEntity(entUpdate);
	    				HttpResponse responseUpdate = httpClient.execute(httpPost);
	    				if(responseUpdate.getStatusLine().getStatusCode() == HttpStatus.SC_OK){*/
	    		        	//update the dbVersionMobile with the string that we received from the service
	    					/**
	    					 * The answer of the service is as the following code
	    					"2|"
							."6-7-8-9," //lun
							."0-1-2-3,"//mar
							."4-5-6-7,"//mie
							."8-9-0-1,"//jue
							."2-3-4-5,"//vier
							."null,"//sab
							."null";//dom
							
							the first parameter is delimited with | which means the city and the rest of the values
							are organized by days. take into account that the week starts with Monday as 1
	    					 * */
	    					/*dbNewParams = EntityUtils.toString(responseUpdate.getEntity());
	    					String[] arr1 = dbNewParams.split("\\|");
	    					String cityId = arr1[0];
	    					String[] arr2 = arr1[1].split(",");
	    					
	    					dbAdapter.deletePicoyPlacaByCity(cityId);
	    					int i = 1;
	    					for(String tmp:arr2){
	    						values.put("ciudad", cityId);
	    						values.put("dia", i);
	    						values.put("numero", tmp);
	    						dbAdapter.saveNewPicoyPlaca(values);
	    						i++;
	    					}
	    					//update the tableDbVersion
	    					updateValues.put("version", dbVersionServer);
	    					dbAdapter.updateDbVersion(updateValues);
	    					
	    					flagUpdate = "true";
	    		        }else{
	    		        	flagUpdate = "Error";
	    		        }
	    		        
	    		    } catch (ClientProtocolException e) {
	    		    	flagUpdate = "Error";
	    		    } catch (IOException e) {
	    		    	flagUpdate = "Error";
	    		    }
	        	}else{
	        		flagUpdate = "False";
	        	}
	        	
	        }else{
	        	//Can't reach the service
	        	flagUpdate = "Error";
	        }
	        
	    } catch (ClientProtocolException e) {
	    	flagUpdate = "Error";
	    } catch (IOException e) {
	    	flagUpdate = "Error";
	    }
		dbAdapter.close();
		
		//TODO after the update, visit a static page with analytics in order to control the number of visitor an place, this is not working cause the javasrcript is never executed.
		HttpPost httpPostAnalytics = new HttpPost("http://picoyplaca.android.diegoadvanced.com?dbVersion="+dbVersionMobile);
		try {
	        // Execute HTTP Post Request
			httpClient.execute(httpPostAnalytics);
		} catch (ClientProtocolException e) {
	    } catch (IOException e) {
	    }*/
		
		return flagUpdate;
	}
	
	@Override
	protected void onPostExecute(String result) {
		if(result.equals("true")){
			widgetUpdate = new WidgetUpdate();
			widgetUpdate.updateWidget(this.context, true);
		}
	}
}