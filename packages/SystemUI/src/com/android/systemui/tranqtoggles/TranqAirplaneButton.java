package com.android.systemui.tranqtoggles;


import com.android.systemui.R;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;




public class TranqAirplaneButton extends TranqToggleButton {

	private View mIndicator;
	private View mIcon;
	private View mDivider;
	private BroadcastReceiver mBroadcastReciver;
	private boolean mState;


	public TranqAirplaneButton(Context context, AttributeSet attrs) {
		super(context, attrs);

	}




	protected void onAttachedToWindow(){
		super.onAttachedToWindow();

		mIndicator = (View) getRootView().findViewById(R.id.indicator_5);
		mIcon = (View) getRootView().findViewById(R.id.airplane_icon);	
		mDivider = (View) getRootView().findViewById(R.id.divider_5);	


	    final IntentFilter mFilter = new IntentFilter();
	    mFilter.addAction("android.intent.action.AIRPLANE_MODE");
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
		return (Settings.System.getInt(getContext().getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0) == 1);
	}

	@Override
	void updateResources() {

		if (Settings.System.getInt(getContext().getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0) == 1) {
			mIndicator.setBackgroundColor(TranqToggleViewTop.mToggleIndOnColor);
			mIcon.setBackgroundResource(R.drawable.tranqtoggle_airplane_on);
			setTextColor(TranqToggleViewTop.mToggleTextOnColor);
			mState = true;

		} else {
			mIcon.setBackgroundResource(R.drawable.tranqtoggle_airplane_off);

			mIndicator.setBackgroundColor(TranqToggleViewTop.mToggleIndOffColor);
			setTextColor(TranqToggleViewTop.mToggleTextOffColor);
			mState = false;
		}

		mDivider.setBackgroundColor(TranqToggleViewTop.mToggleDivColor);

	}



	@Override
	void toggleOn() {
		Settings.System.putInt(getContext().getContentResolver(),Settings.System.AIRPLANE_MODE_ON, 1);
		updateResources();

	    Intent i = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
	    i.putExtra("state", mState);
	    getContext().sendBroadcast(i);
	}



	@Override
	void toggleOff() {

		Settings.System.putInt(getContext().getContentResolver(),Settings.System.AIRPLANE_MODE_ON, 0);
		updateResources();

	    Intent i = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
	    i.putExtra("state", mState);
	    getContext().sendBroadcast(i);
	}



	@Override
	void showSettings() {

	    Intent i = new Intent();
	    i.setAction("android.intent.action.CLOSE_SYSTEM_DIALOGS");
	    getContext().sendBroadcast(i);
	    i.setAction("android.settings.AIRPLANE_MODE_SETTINGS");
        i.setFlags(i.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(i);
	}





}  // 
