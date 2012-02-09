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

	private View showBluetooth;
	private View mIndicator;
	private View mIcon;
	private View mDivider;
	private BroadcastReceiver mBroadcastReciver;
	private BluetoothAdapter mBluetoothAdaper;	


	public TranqBluetoothButton(Context context, AttributeSet attrs) {
		super(context, attrs);

		mBluetoothAdaper = BluetoothAdapter.getDefaultAdapter();

	}




	protected void onAttachedToWindow(){
		super.onAttachedToWindow();

		showBluetooth = (View) getRootView().findViewById(R.id.button_3);
		mIndicator = (View) getRootView().findViewById(R.id.indicator_3);
		mIcon = (View) getRootView().findViewById(R.id.bluetooth_icon);	
		mDivider = (View) getRootView().findViewById(R.id.divider_3);	

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
			mIndicator.setBackgroundColor(TranqToggleViewTop.mToggleIndOnColor);
			mIcon.setBackgroundResource(R.drawable.tranqtoggle_bluetooth_on);
			setTextColor(TranqToggleViewTop.mToggleTextOnColor);

		} else {
			mIcon.setBackgroundResource(R.drawable.tranqtoggle_bluetooth_off);

			mIndicator.setBackgroundColor(TranqToggleViewTop.mToggleIndOffColor);
			setTextColor(TranqToggleViewTop.mToggleTextOffColor);
		}

		mDivider.setBackgroundColor(TranqToggleViewTop.mToggleDivColor);

		if (TranqToggleButton.mShowBluetooth) {
			showBluetooth.setVisibility(View.VISIBLE);
		} else {
			showBluetooth.setVisibility(View.GONE);
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
