package com.android.systemui.tranqtoggles;


import com.android.systemui.R;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.location.LocationManager;
import android.os.Handler;
import android.os.IPowerManager;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;




public class TranqRotateButton extends TranqToggleButton {
	
	private View mIndicator;
	private View mIcon;
	private Context mContext; 
	Handler mHandler = new Handler();
	final RotateModeObserver mRotateModeObserver = new RotateModeObserver(mHandler) ;
	

	
	// Brightness settings observer
	class RotateModeObserver extends ContentObserver{
		
		public RotateModeObserver(Handler handler) {
			super(handler);
		}

	    @Override
	    public void onChange(boolean selfChange){

	    	updateResources();
	    }
	}



	public TranqRotateButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		mContext = context;
	}
	
	
	
	protected void onAttachedToWindow(){
		super.onAttachedToWindow();
		
		mIndicator = (View) getRootView().findViewById(R.id.indicator_7);
		mIcon = (View) getRootView().findViewById(R.id.rotate_icon);	
		
        getContext().getContentResolver().registerContentObserver(
                Settings.System.getUriFor(Settings.System.ACCELEROMETER_ROTATION), true,
                mRotateModeObserver);
       	
		updateResources();
	}

	
	protected void onDetachedFromWindow(){
		
        getContext().getContentResolver().unregisterContentObserver(mRotateModeObserver);

	}
	


	@Override
	protected boolean getStatusOn(){

		boolean mTrue = true;
		try {
			mTrue = (Settings.System.getInt(getContext().getContentResolver(), Settings.System.ACCELEROMETER_ROTATION) == 1);
		} catch (SettingNotFoundException e) {
			e.printStackTrace();
		}
		return mTrue;
	}

	
	
	@Override
	void updateResources() {

		
		
		
		
	    boolean rotateStatus = true;
		try {
			rotateStatus = (Settings.System.getInt(getContext().getContentResolver(), Settings.System.ACCELEROMETER_ROTATION) == 1);
		} catch (SettingNotFoundException e) {
			e.printStackTrace();
		}
		
	    if(rotateStatus){
			mIndicator.setBackgroundColor(0xffffbb33);
			mIcon.setBackgroundResource(R.drawable.tranqtoggle_rotate_on);
			setTextColor(0xffffbb33);
	      }else{
			mIcon.setBackgroundResource(R.drawable.tranqtoggle_rotate_off);
			mIndicator.setBackgroundColor(0xff000000);
			setTextColor(0xffffffff);
	      }
	}



	@Override
	void toggleOn() {


		Settings.System.putInt(getContext().getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 1);
		updateResources();
	}


	@Override
	void toggleOff() {
		
		Settings.System.putInt(getContext().getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0);	
		updateResources();
	}


	

	@Override
	void showSettings() {

	    Intent i = new Intent();
	    i.setAction("android.intent.action.CLOSE_SYSTEM_DIALOGS");
	    mContext.sendBroadcast(i);
	    i.setAction("android.settings.DISPLAY_SETTINGS");
        i.setFlags(i.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(i);
	}





}  // 
