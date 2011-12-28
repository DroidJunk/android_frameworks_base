package com.android.systemui.tranqtoggles;


import com.android.systemui.R;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.AttributeSet;
import android.view.View;




public class TranqBluetoothButton extends TranqToggleButton {
	
	private View mIndicator;
	private View mIcon;
	private BroadcastReceiver mBroadcastReciver;
	private BluetoothAdapter mBluetoothAdaper;	


	public TranqBluetoothButton(Context context, AttributeSet attrs) {
		super(context, attrs);

		mBluetoothAdaper = BluetoothAdapter.getDefaultAdapter();
		
	}

	
	
	
	protected void onAttachedToWindow(){
		super.onAttachedToWindow();
		
		mIndicator = (View) getRootView().findViewById(R.id.indicator_3);
		mIcon = (View) getRootView().findViewById(R.id.bluetooth_icon);	
		
	    final IntentFilter mFilter = new IntentFilter();
	    mFilter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
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
		getContext().unregisterReceiver(mBroadcastReciver);
	}


	@Override
	protected boolean getStatusOn(){
		return mBluetoothAdaper.isEnabled();
	}

	@Override
	void updateResources() {

		if (mBluetoothAdaper.isEnabled()) {
			mIndicator.setBackgroundColor(0xffffbb33);
			mIcon.setBackgroundResource(R.drawable.tranqtoggle_bluetooth_on);
			setTextColor(0xffffbb33);
			
		} else {
			mIcon.setBackgroundResource(R.drawable.tranqtoggle_bluetooth_off);
			mIndicator.setBackgroundColor(0xff000000);
			setTextColor(0xffffffff);
		}
	}



	@Override
	void toggleOn() {
		
		mBluetoothAdaper.enable();
		updateResources();
	}


	@Override
	void toggleOff() {
		
		mBluetoothAdaper.disable();
		updateResources();
	}


	@Override
	void showSettings() {

	    Intent i = new Intent();
	    i.setAction("android.intent.action.CLOSE_SYSTEM_DIALOGS");
	    getContext().sendBroadcast(i);
	    i.setAction("android.settings.BLUETOOTH_SETTINGS");
        i.setFlags(i.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(i);
	}





}  // 
