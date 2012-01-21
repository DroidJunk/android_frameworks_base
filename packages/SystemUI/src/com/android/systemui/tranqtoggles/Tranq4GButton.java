package com.android.systemui.tranqtoggles;

import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.systemui.R;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.database.Cursor;
import android.telephony.TelephonyManager;
import android.util.AttributeSet;
import android.view.View;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.INetworkManagementService;
import android.os.Looper;
import android.os.Message;
import android.os.ServiceManager;
import android.provider.Settings;








public class Tranq4GButton extends TranqToggleButton {
	
	private View mIndicator;
	private View mIcon;
	private WifiManager mWifiManager; 
	private Cursor dataCursor;
	private ConnectivityManager mConnectivityManager; 
	Handler mHandler = new Handler();
	final DataModeObserver mDataModeObserver = new DataModeObserver(mHandler) ;
	
	private Phone phone = null;
	private static final int EVENT_SET_PREFERRED_TYPE_DONE = 1001;
	private TelephonyManager mTelephonyManager;

	

	

	
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

	
	
	

	public Tranq4GButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		

		
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
		
		mTelephonyManager = (TelephonyManager)getContext().getSystemService(Service.TELEPHONY_SERVICE);
		updateResources();
	}

	
	protected void onDetachedFromWindow(){
		
		getContext().getContentResolver().unregisterContentObserver(mDataModeObserver);
	}


	@Override
	protected boolean getStatusOn(){
		
		return (Settings.Secure.getInt(getContext().getContentResolver(), Settings.Secure.PREFERRED_NETWORK_MODE, 4) == 7);

	}

	@Override
	void updateResources() {
		
		if ((Settings.Secure.getInt(getContext().getContentResolver(), Settings.Secure.PREFERRED_NETWORK_MODE, 4) == 7)) {
			mIndicator.setBackgroundColor(TranqToggleViewTop.mToggleIndOnColor);
			mIcon.setBackgroundResource(R.drawable.tranqtoggle_data_on);
			setTextColor(TranqToggleViewTop.mToggleTextOnColor);
			
		} else {
			mIcon.setBackgroundResource(R.drawable.tranqtoggle_data_off);
			mIndicator.setBackgroundColor(TranqToggleViewTop.mToggleIndOffColor);
			setTextColor(TranqToggleViewTop.mToggleTextOffColor);
		}
		
	}


	@Override
	void toggleOn() {
		
		Settings.Secure.putInt(getContext().getContentResolver(), Settings.Secure.PREFERRED_NETWORK_MODE, 7);
	    Intent i = new Intent();
	    i.setAction("TRANQ_SETTINGS");
	    i.putExtra("NetworkMode",7);
	    getContext().sendBroadcast(i);

	         
		updateResources();
		
	}


	@Override
	void toggleOff() {
		Settings.Secure.putInt(getContext().getContentResolver(), Settings.Secure.PREFERRED_NETWORK_MODE, 4);
	    Intent i = new Intent();
	    i.setAction("TRANQ_SETTINGS");
	    i.putExtra("NetworkMode",4);
	    getContext().sendBroadcast(i);
		
		
;

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
