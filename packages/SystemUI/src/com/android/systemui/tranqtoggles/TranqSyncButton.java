package com.android.systemui.tranqtoggles;



import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.AttributeSet;
import android.view.View;
import com.android.systemui.R;


public class TranqSyncButton extends TranqToggleButton {
	
	private View mIndicator;
	private View mIcon;
	private View mDivider;
	private BroadcastReceiver mBroadcastReciver;
	


	public TranqSyncButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		


		
	}

	
	
	
	protected void onAttachedToWindow(){
		super.onAttachedToWindow();
		
		mIndicator = (View) getRootView().findViewById(R.id.indicator_8);
		mIcon = (View) getRootView().findViewById(R.id.sync_icon);	
		mDivider = (View) getRootView().findViewById(R.id.divider_8);
		
		
		
	    final IntentFilter mFilter = new IntentFilter();
	    mFilter.addAction(Intent.ACTION_SYNC_STATE_CHANGED);
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
		return ContentResolver.getMasterSyncAutomatically();

	}

	@Override
	void updateResources() {
		
		if (ContentResolver.getMasterSyncAutomatically()) {
			mIndicator.setBackgroundColor(TranqToggleView.mToggleIndOnColor);
			mIcon.setBackgroundResource(R.drawable.tranqtoggle_sync_on);
			setTextColor(TranqToggleView.mToggleTextOnColor);
			
		} else {
			mIcon.setBackgroundResource(R.drawable.tranqtoggle_sync_off);
			mIndicator.setBackgroundColor(TranqToggleView.mToggleIndOffColor);
			setTextColor(TranqToggleView.mToggleTextOffColor);
		}
		
		mDivider.setBackgroundColor(TranqToggleView.mToggleDivColor);
	}



	@Override
	void toggleOn() {
		
		ContentResolver.setMasterSyncAutomatically(true);
		updateResources();
	}


	@Override
	void toggleOff() {
		
		ContentResolver.setMasterSyncAutomatically(false);
		updateResources();
	}


	@Override
	void showSettings() {

	    Intent i = new Intent();
	    i.setAction("android.intent.action.CLOSE_SYSTEM_DIALOGS");
	    getContext().sendBroadcast(i);
	    i.setAction("android.settings.AccountSyncSettingsActivity");
        i.setFlags(i.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(i);
	}





}  // 
