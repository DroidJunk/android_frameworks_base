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
import android.os.INetworkManagementService;
import android.os.ServiceManager;
import android.provider.Settings;



public class TranqDataButton extends TranqToggleButton {

	private View showData;
	private View mIndicator;
	private View mIcon;
	private WifiManager mWifiManager; 
	private ConnectivityManager mConnectivityManager; 
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

		showData = (View) getRootView().findViewById(R.id.button_9);
		mIndicator = (View) getRootView().findViewById(R.id.indicator_9);
		mIcon = (View) getRootView().findViewById(R.id.data_icon);


		getContext().getContentResolver().registerContentObserver(
                Settings.Secure.getUriFor(Settings.Secure.MOBILE_DATA), true,
                mDataModeObserver);
       

		updateResources();
	}


	protected void onDetachedFromWindow(){

		getContext().getContentResolver().unregisterContentObserver(mDataModeObserver);
	}


	@Override
	protected boolean getStatusOn(){

		return mConnectivityManager.getMobileDataEnabled();
	}

	@Override
	void updateResources() {

		if (mConnectivityManager.getMobileDataEnabled()) {
			mIndicator.setBackgroundColor(TranqToggleViewTop.mToggleIndOnColor);
			mIcon.setBackgroundResource(R.drawable.tranqtoggle_data_on);
			setTextColor(TranqToggleViewTop.mToggleTextOnColor);

		} else {
			mIcon.setBackgroundResource(R.drawable.tranqtoggle_data_off);
			mIndicator.setBackgroundColor(TranqToggleViewTop.mToggleIndOffColor);
			setTextColor(TranqToggleViewTop.mToggleTextOffColor);
		}

		if (TranqToggleButton.mShowData) {
			showData.setVisibility(View.VISIBLE);
		} else {
			showData.setVisibility(View.GONE);
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
