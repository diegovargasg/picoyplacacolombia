package com.diegodevelopero.PicoyPlacaReminder;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class TabWidget extends TabActivity{
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabs_host);
		Resources res = getResources(); // Resource object to get Drawables
	    TabHost tabHost = getTabHost();  // The activity TabHost
	    TabHost.TabSpec spec;  // Reusable TabSpec for each tab
	    Intent intent;  // Reusable Intent for each tab
	    
	    // Create an Intent to launch an Activity for the tab (to be reused)
	    intent = new Intent().setClass(this, CurrentCars.class);

	    // Initialize a TabSpec for each tab and add it to the TabHost
	    spec = tabHost.newTabSpec("vehiculos").setIndicator("Tus Vehiculos", res.getDrawable(R.drawable.tab_layout_currentcar)).setContent(intent);
	    tabHost.addTab(spec);
	    
	    // Do the same for the other tabs
	    intent = new Intent().setClass(this, Information.class);
	    spec = tabHost.newTabSpec("informacion").setIndicator("Informacion", res.getDrawable(R.drawable.tab_layout_info)).setContent(intent);
	    tabHost.addTab(spec);
	    
	    intent = new Intent().setClass(this, About.class);
	    spec = tabHost.newTabSpec("acerca").setIndicator("Acerca de...", res.getDrawable(R.drawable.tab_layout_about)).setContent(intent);
	    tabHost.addTab(spec);
	    
	    tabHost.setCurrentTab(0);
	}
}
