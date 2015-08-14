package com.diegodevelopero.PicoyPlacaReminder;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.google.android.apps.analytics.GoogleAnalyticsTracker;

public class About extends Activity{
	
	private WebView mWebView;
	GoogleAnalyticsTracker tracker;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tracker = GoogleAnalyticsTracker.getInstance();
		tracker.startNewSession("UA-3974370-12", 30, this);
		tracker.trackPageView("/About");
		setContentView(R.layout.about);
		
		mWebView = (WebView) findViewById(R.id.webviewAbout);
	    mWebView.getSettings().setJavaScriptEnabled(true);
	    mWebView.loadUrl("http://diegoadvanced.com/android/picoyplaca/about.php");
	    
  		// Create the adView
  		// Please replace MY_BANNER_UNIT_ID with your AdMob Publisher ID
  		AdView adView = new AdView(this, AdSize.BANNER, "a14f6480b45f38a");

  		// Add the adView to it
  		mWebView.addView(adView);

  		// Initiate a generic request to load it with an ad
  		AdRequest request = new AdRequest();
  		//request.addTestDevice("0D22E81D0C61259DD8B6D26C156AA4B8");
  		//request.setTesting(true);

  		adView.loadAd(request);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		// Stop the tracker when it is no longer needed.
		tracker.stopSession();
	}
}
