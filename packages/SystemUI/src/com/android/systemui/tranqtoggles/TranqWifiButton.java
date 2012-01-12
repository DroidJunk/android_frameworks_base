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
	private View mDivider;
	private WifiManager mWifiManager; 
	private BroadcastReceiver mBroadcastReciver;
	


	public TranqWifiButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		mContext = context;
		
		mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		
	}

	
	
	
	protected void onAttachedToWindow(){
		super.onAttachedToWindow();
		
		mIndicator = (View) getRootView().findViewById(R.id.indicator_1);
		mIcon = (View) getRootView().findViewById(R.id.wifi_icon);	
		mDivider = (View) getRootView().findViewById(R.id.divider_1);
		
	    final IntentFilter mFilter = new IntentFilter();
	    mFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
	    mBroadcastReciver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
            	updateResources();
            }
        };

        getContext().registerReceiver(mBroadcastReciver, mFilter);
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
			mIndicator.setBackgroundColor(TranqToggleView.mToggleIndOnColor);
			mIcon.setBackgroundResource(R.drawable.tranqtoggle_wifi_on);
			setTextColor(TranqToggleView.mToggleTextOnColor);
			
		} else {
			mIcon.setBackgroundResource(R.drawable.tranqtoggle_wifi_off);
			mIndicator.setBackgroundColor(TranqToggleView.mToggleIndOffColor);
			setTextColor(TranqToggleView.mToggleTextOffColor);
		}
		
		mDivider.setBackgroundColor(TranqToggleView.mToggleDivColor);
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
