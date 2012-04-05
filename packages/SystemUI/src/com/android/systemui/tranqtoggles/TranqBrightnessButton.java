package com.android.systemui.tranqtoggles;


import com.android.systemui.R;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.location.LocationManager;
import android.os.Handler;
import android.os.IPowerManager;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;




public class TranqBrightnessButton extends TranqToggleButton {

	private View showBrightness;
	private View mIndicator;
	private ImageView mIcon;
	private View mDivider;
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

		showBrightness = (View) getRootView().findViewById(R.id.button_6);
		mIndicator = (View) getRootView().findViewById(R.id.indicator_6);
		mIcon = (ImageView) getRootView().findViewById(R.id.brightness_icon);
		if (TranqToggleButton.mCustomIconColors) mIcon.setColorFilter(TranqToggleButton.mToggleIconOffColor);
		mDivider = (View) getRootView().findViewById(R.id.divider_6);		

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
		
		mIcon.clearColorFilter();

	    boolean autoBrightnessStatus = true;
		try {
			autoBrightnessStatus = (Settings.System.getInt(getContext().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE) == 1);
		} catch (SettingNotFoundException e) {
			e.printStackTrace();
		}

	    if(autoBrightnessStatus){
			mIndicator.setBackgroundColor(TranqToggleViewTop.mToggleIndOnColor);
			mIcon.setImageResource(R.drawable.tranqtoggle_brightness_on);
			if (TranqToggleButton.mCustomIconColors) mIcon.setColorFilter(TranqToggleButton.mToggleIconOnColor);
			setTextColor(TranqToggleViewTop.mToggleTextOnColor);
	      }else{
			mIcon.setImageResource(R.drawable.tranqtoggle_brightness_off);
			if (TranqToggleButton.mCustomIconColors) mIcon.setColorFilter(TranqToggleButton.mToggleIconOffColor);
			mIndicator.setBackgroundColor(TranqToggleViewTop.mToggleIndOffColor);
			setTextColor(TranqToggleViewTop.mToggleTextOffColor);

	      }

		  mDivider.setBackgroundColor(TranqToggleViewTop.mToggleDivColor);
		  
		  if (TranqToggleButton.mShowBrightness) {
			  showBrightness.setVisibility(View.VISIBLE);
		  } else {
			  showBrightness.setVisibility(View.GONE);
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