package com.android.systemui.tranqtoggles;

import com.android.systemui.R;
import com.android.systemui.tranqtoggles.TranqRotateButton.RotateModeObserver;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.database.Cursor;
import android.util.AttributeSet;
import android.view.View;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.provider.Settings;



public class TranqDataButton extends TranqToggleButton {
	
	private View mIndicator;
	private View mIcon;
	private Cursor dataCursor;
	private ConnectivityManager mConnectivityManager; 
	private BroadcastReceiver mBroadcastReciver;
	Handler mHandler = new Handler();
	final DataModeObserver mDataModeObserver = new DataModeObserver(mHandler) ;
	

	
	// Brightness settings observer
	class DataModeObserver extends ContentObserver{
		
		public DataModeObserver(Handler handler) {
			super(handler);
		}

	    @Override
	    public void onChange(boolean selfChange){

	    	updateResources();
	    }
	}	

	
	
	

	public TranqDataButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		
	}

	
	
	
	protected void onAttachedToWindow(){
		super.onAttachedToWindow();
		
		mIndicator = (View) getRootView().findViewById(R.id.indicator_9);
		mIcon = (View) getRootView().findViewById(R.id.data_icon);	
		
 
		dataCursor =  getContext().getContentResolver().query(Settings.Secure.CONTENT_URI, null,
                "(" + Settings.System.NAME + "=?)",
                new String[]{Settings.Secure.MOBILE_DATA},
                null);		
        
		dataCursor.registerContentObserver(mDataModeObserver);
		updateResources();
	}

	
	protected void onDetachedFromWindow(){
		
		getContext().getContentResolver().unregisterContentObserver(mDataModeObserver);
	}


	@Override
	protected boolean getStatusOn(){
		
		return mConnectivityManager.getActiveNetworkInfo() != null;
		
	}

	@Override
	void updateResources() {

		
		if (mConnectivityManager.getActiveNetworkInfo() == null) {
			mIndicator.setBackgroundColor(0xffffbb33);
			mIcon.setBackgroundResource(R.drawable.tranqtoggle_data_on);
			setTextColor(0xffffbb33);
			
		} else {
			mIcon.setBackgroundResource(R.drawable.tranqtoggle_data_off);
			mIndicator.setBackgroundColor(0xff000000);
			setTextColor(0xffffffff);
		}
	}


	@Override
	void toggleOn() {
		
		mConnectivityManager.setMobileDataEnabled(true);
		updateResources();
	}


	@Override
	void toggleOff() {
		
		mConnectivityManager.setMobileDataEnabled(false);
		updateResources();
	}


	@Override
	void showSettings() {

	    Intent i = new Intent();
	    i.setAction("android.intent.action.CLOSE_SYSTEM_DIALOGS");
	    getContext().sendBroadcast(i);
	    i.setAction("android.settings.DATA_ROAMING_SETTINGS");
        i.setFlags(i.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(i);
	}





}  // 
