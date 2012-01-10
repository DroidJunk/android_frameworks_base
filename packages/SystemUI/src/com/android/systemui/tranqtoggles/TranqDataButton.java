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
	private WifiManager mWifiManager; 
	private Cursor dataCursor;
	private ConnectivityManager mConnectivityManager; 
	private BroadcastReceiver mBroadcastReciver;
	Handler mHandler = new Handler();
	final DataModeObserver mDataModeObserver = new DataModeObserver(mHandler) ;
	

	
	// Data Mode observer
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
		
		mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
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
			mIndicator.setBackgroundColor(TranqToggleView.mToggleIndOnColor);
			mIcon.setBackgroundResource(R.drawable.tranqtoggle_data_on);
			setTextColor(TranqToggleView.mToggleTextOnColor);
			
		} else {
			mIcon.setBackgroundResource(R.drawable.tranqtoggle_data_off);
			mIndicator.setBackgroundColor(TranqToggleView.mToggleIndOffColor);
			setTextColor(TranqToggleView.mToggleTextOffColor);
		}
		
	}


	@Override
	void toggleOn() {
		
		if (!mWifiManager.isWifiEnabled()) {
		mConnectivityManager.setMobileDataEnabled(true);
		updateResources();
		}
	}


	@Override
	void toggleOff() {
		
		if (!mWifiManager.isWifiEnabled()) {
		mConnectivityManager.setMobileDataEnabled(false);
		updateResources();
		}
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
