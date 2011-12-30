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



public class TranqSettingsButton extends TranqToggleButton {
	

	private View mIcon;
	

		

	public TranqSettingsButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}

	
	
	
	protected void onAttachedToWindow(){
		super.onAttachedToWindow();
		
	}

	
	protected void onDetachedFromWindow(){
		
		
	}


	@Override
	protected boolean getStatusOn(){
		return false;
		
		
	}

	@Override
	void updateResources() {

		
	}


	@Override
	void toggleOn() {
	
	    Intent i = new Intent();
	    i.setAction("android.intent.action.CLOSE_SYSTEM_DIALOGS");
	    getContext().sendBroadcast(i);
	    i.setAction("android.settings.SETTINGS");
        i.setFlags(i.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(i);
		
	}


	@Override
	void toggleOff() {
		

	}


	@Override
	void showSettings() {

	    Intent i = new Intent();
	    i.setAction("android.intent.action.CLOSE_SYSTEM_DIALOGS");
	    getContext().sendBroadcast(i);
	    i.setAction("android.settings.CUSTOM_SETTINGS");
        i.setFlags(i.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(i);
	}





}  // 
