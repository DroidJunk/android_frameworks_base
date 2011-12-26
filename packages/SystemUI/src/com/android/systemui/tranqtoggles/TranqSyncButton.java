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
	private BroadcastReceiver mBroadcastReciver;
	


	public TranqSyncButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		


		
	}

	
	
	
	protected void onAttachedToWindow(){
		super.onAttachedToWindow();
		
		mIndicator = (View) getRootView().findViewById(R.id.indicator_8);
		mIcon = (View) getRootView().findViewById(R.id.sync_icon);	
		
		
		
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
			mIndicator.setBackgroundColor(0xffc0ff00);
			mIcon.setBackgroundResource(R.drawable.tranqtoggle_sync_on);
			setTextColor(0xffc0ff00);
			
		} else {
			mIcon.setBackgroundResource(R.drawable.tranqtoggle_sync_off);
			mIndicator.setBackgroundColor(0xff959595);
			setTextColor(0xffffffff);
		}
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
	    i.setAction("android.settings.SYNC_SETTINGS");
        i.setFlags(i.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(i);
	}





}  // 
