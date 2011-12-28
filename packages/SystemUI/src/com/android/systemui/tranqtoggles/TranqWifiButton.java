package com.android.systemui.tranqtoggles;

import com.android.systemui.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.AttributeSet;
import android.view.View;
import android.net.wifi.WifiManager;


public class TranqWifiButton extends TranqToggleButton {
	
	private View mIndicator;
	private View mIcon;
	private WifiManager mWifiManager; 
	private BroadcastReceiver mBroadcastReciver;
	private Context mContext; 
	


	public TranqWifiButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		
		mContext = context;
		
		mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		
	}

	
	
	
	protected void onAttachedToWindow(){
		super.onAttachedToWindow();
		
		mIndicator = (View) getRootView().findViewById(R.id.indicator_1);
		mIcon = (View) getRootView().findViewById(R.id.wifi_icon);	
		
	    final IntentFilter mFilter = new IntentFilter();
	    mFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
	    mBroadcastReciver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
            	updateResources();
            }
        };

        mContext.registerReceiver(mBroadcastReciver, mFilter);
		updateResources();
	}

	
	protected void onDetachedFromWindow(){
		mContext.unregisterReceiver(mBroadcastReciver);
	}


	@Override
	protected boolean getStatusOn(){
		return mWifiManager.isWifiEnabled();
	}

	@Override
	void updateResources() {

		if (mWifiManager.isWifiEnabled()) {
			mIndicator.setBackgroundColor(0xffffbb33);
			mIcon.setBackgroundResource(R.drawable.tranqtoggle_wifi_on);
			setTextColor(0xffffbb33);
			
		} else {
			mIcon.setBackgroundResource(R.drawable.tranqtoggle_wifi_off);
			mIndicator.setBackgroundColor(0xff292929);
			setTextColor(0xffffffff);
		}
	}



	@Override
	void toggleOn() {
		
		mWifiManager.setWifiEnabled(true);
		updateResources();
	}


	@Override
	void toggleOff() {
		
		mWifiManager.setWifiEnabled(false);
		updateResources();
	}


	@Override
	void showSettings() {

	    Intent i = new Intent();
	    i.setAction("android.intent.action.CLOSE_SYSTEM_DIALOGS");
	    mContext.sendBroadcast(i);
	    i.setAction("android.settings.WIFI_SETTINGS");
        i.setFlags(i.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(i);
	}





}  // 
