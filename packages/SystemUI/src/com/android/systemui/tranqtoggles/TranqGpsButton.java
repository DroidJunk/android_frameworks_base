package com.android.systemui.tranqtoggles;


import com.android.systemui.R;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.util.AttributeSet;
import android.view.View;
import android.location.LocationManager;
import android.os.Handler;
import android.provider.Settings;




public class TranqGpsButton extends TranqToggleButton {
	
	private View mIndicator;
	private View mIcon;
	private Cursor gpsCursor;
	Handler mHandler = new Handler();
	final GpsObserver mGpsObserver = new GpsObserver(mHandler) ;

	
	// Gps settings observer
	class GpsObserver extends ContentObserver{
		
		public GpsObserver(Handler handler) {
			super(handler);
		}

	    @Override
	    public void onChange(boolean selfChange){

	    	updateResources();
	    }
	}



	public TranqGpsButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}
	
	
	
	protected void onAttachedToWindow(){
		super.onAttachedToWindow();
		
		mIndicator = (View) getRootView().findViewById(R.id.indicator_2);
		mIcon = (View) getRootView().findViewById(R.id.gps_icon);	

        gpsCursor = getContext().getContentResolver().query(Settings.Secure.CONTENT_URI, null,
                "(" + Settings.System.NAME + "=?)",
                new String[]{Settings.Secure.LOCATION_PROVIDERS_ALLOWED},
                null);

		gpsCursor.registerContentObserver(mGpsObserver);		
		updateResources();
	}

	
	protected void onDetachedFromWindow(){

		gpsCursor.unregisterContentObserver(mGpsObserver);
	}

	


	@Override
	protected boolean getStatusOn(){
	
		return Settings.Secure.isLocationProviderEnabled(getContext().getContentResolver(), LocationManager.GPS_PROVIDER);
	}

	@Override
	void updateResources() {
		
		ContentResolver contentResolver = getContext().getContentResolver();
	    boolean gpsStatus = Settings.Secure.isLocationProviderEnabled(contentResolver, LocationManager.GPS_PROVIDER);
	    if(gpsStatus){
			mIndicator.setBackgroundColor(0xffffbb33);
			mIcon.setBackgroundResource(R.drawable.tranqtoggle_gps_on);
			setTextColor(0xffffbb33);
	    }else{
	    	mIcon.setBackgroundResource(R.drawable.tranqtoggle_gps_off);
			mIndicator.setBackgroundColor(0xff000000);
			setTextColor(0xffffffff);
	    }
	}



	@Override
	void toggleOn() {

		ContentResolver contentResolver = getContext().getContentResolver();
	    Settings.Secure.setLocationProviderEnabled(contentResolver,"gps" ,true);
	   
		updateResources();
	}


	@Override
	void toggleOff() {
		ContentResolver contentResolver = getContext().getContentResolver();
	     Settings.Secure.setLocationProviderEnabled(contentResolver,"gps" ,false);		
		
		updateResources();
	}


	@Override
	void showSettings() {

	    Intent i = new Intent();
	    i.setAction("android.intent.action.CLOSE_SYSTEM_DIALOGS");
	    getContext().sendBroadcast(i);
	    i.setAction("android.settings.LOCATION_SOURCE_SETTINGS");
        i.setFlags(i.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(i);
	}





}  // 
