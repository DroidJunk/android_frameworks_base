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




public class TranqBrightnessButton extends TranqToggleButton {
	
	private View mIndicator;
	private View mIcon;
	private Context mContext; 
	Handler mHandler = new Handler();
	final BrightnessModeObserver mBrightnessModeObserver = new BrightnessModeObserver(mHandler) ;
	

	
	// Brightness settings observer
	class BrightnessModeObserver extends ContentObserver{
		
		public BrightnessModeObserver(Handler handler) {
			super(handler);
		}

	    @Override
	    public void onChange(boolean selfChange){

	    	updateResources();
	    }
	}



	public TranqBrightnessButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		mContext = context;
	}
	
	
	
	protected void onAttachedToWindow(){
		super.onAttachedToWindow();
		
		mIndicator = (View) getRootView().findViewById(R.id.indicator_6);
		mIcon = (View) getRootView().findViewById(R.id.brightness_icon);	
		
        getContext().getContentResolver().registerContentObserver(
                Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS_MODE), true,
                mBrightnessModeObserver);
       	
		updateResources();
	}

	
	protected void onDetachedFromWindow(){
		
        getContext().getContentResolver().unregisterContentObserver(mBrightnessModeObserver);

	}
	


	@Override
	protected boolean getStatusOn(){

		boolean mTrue = true;
		try {
			mTrue = (Settings.System.getInt(getContext().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE) == 1);
		} catch (SettingNotFoundException e) {
			e.printStackTrace();
		}
		return mTrue;
	}

	
	
	@Override
	void updateResources() {

	    boolean autoBrightnessStatus = true;
		try {
			autoBrightnessStatus = (Settings.System.getInt(getContext().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE) == 1);
		} catch (SettingNotFoundException e) {
			e.printStackTrace();
		}
		
	    if(autoBrightnessStatus){
			mIndicator.setBackgroundColor(0xffffbb33);
			mIcon.setBackgroundResource(R.drawable.tranqtoggle_brightness_on);
			setTextColor(0xffffbb33);
	      }else{
			mIcon.setBackgroundResource(R.drawable.tranqtoggle_brightness_off);
			mIndicator.setBackgroundColor(0xff000000);
			setTextColor(0xffffffff);
	      }
	}



	@Override
	void toggleOn() {


		Settings.System.putInt(getContext().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, 1);
		updateResources();
	}


	@Override
	void toggleOff() {
		
		Settings.System.putInt(getContext().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, 0);	
		updateResources();
	}


	
	/*	
    private void setBrightness(int brightness) {
        try {
            IPowerManager power = IPowerManager.Stub.asInterface(
                    ServiceManager.getService("power"));
            if (power != null) {
                power.setBacklightBrightness(brightness);
            }
        } catch (RemoteException doe) {
            
        }
    }	
	
    private int getBrightness(int defaultValue) {
        int brightness = defaultValue;
        try {
            brightness = Settings.System.getInt(getContext().getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS);
        } catch (SettingNotFoundException snfe) {
        }
        return brightness;
    }	*/

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
