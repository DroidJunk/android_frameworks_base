package com.android.systemui.quicksettings;

import com.android.systemui.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.preference.CheckBoxPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public abstract class QuickButton extends TextView {

	private final String Tranq_Settings = "TRANQ_SETTINGS";
	private SharedPreferences mPrefs;
	public static boolean mShowClockButton = true;
	public static boolean mShowLockscreenButton = true;
	public static boolean mShowMeterButton = true;
	public static boolean mShowNavButton = true;
	public static boolean mShowPulldownButton = true;
	public static boolean mShowToggleButton = true;
	public static boolean mShowIconButton = true;
	public static boolean mShowLedButton = true;
	public static boolean mShowQuietButton = true;
	public static boolean mCustomIconColors = false;
	public static int mToggleIconOnColor = 0xff33b5e5;



	public QuickButton(Context context, AttributeSet attrs) {
		super(context, attrs);

        IntentFilter filter = new IntentFilter();
        filter.addAction(Tranq_Settings);
        getContext().registerReceiver(mIntentReceiver, filter, null, getHandler());
       
  		Context settingsContext = getContext();
		try {
			settingsContext = getContext().createPackageContext("com.android.settings",0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     		
		mPrefs = settingsContext.getSharedPreferences("Tranquility_Settings", Context.MODE_PRIVATE);
		mShowClockButton = mPrefs.getBoolean("quick_setting_clock", true);
		mShowMeterButton = mPrefs.getBoolean("quick_setting_meter", true);
		mShowNavButton = mPrefs.getBoolean("quick_setting_nav", true);
		mShowLockscreenButton = mPrefs.getBoolean("quick_setting_lockscreen", true);
		mShowPulldownButton = mPrefs.getBoolean("quick_setting_pulldown", true);
		mShowToggleButton = mPrefs.getBoolean("quick_setting_toggle", true);
		mShowIconButton = mPrefs.getBoolean("quick_setting_iconcolor", true);
		mShowLedButton = mPrefs.getBoolean("quick_setting_led", true);
		mShowQuietButton = mPrefs.getBoolean("quick_setting_quiet", true);
		mCustomIconColors = mPrefs.getBoolean("toggles_custom_icon_colors", false);
   		mToggleIconOnColor = mPrefs.getInt("toggles_icon_on_color", 0xff33b5e5);

		setOnClickListener(new OnClickListener() { 
			public void onClick (View v){

				if (getStatusOn()) {
					toggleOff();
				} else {
					toggleOn();
				}
			}
		}
	);
}

    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            int oldColor = 0;
            
            if (action.equals("TRANQ_SETTINGS")) {
            	

            	if (intent.getBooleanExtra("ToggleCustomIconColors", mCustomIconColors) == false) {
            		mCustomIconColors = false;
            		updateResources();
            	} else {
            		mCustomIconColors = true;
            		updateResources();
            	}
            	oldColor = mToggleIconOnColor;
                mToggleIconOnColor = intent.getIntExtra("ToggleIconOnColor", mToggleIconOnColor);
            	if (oldColor != mToggleIconOnColor) updateResources();
   	
            	if (intent.getBooleanExtra("UpdateToggles", false) == true) {
            		updateResources();
            	}

            	if (intent.getBooleanExtra("UpdateSettings", false) == true) {
            		updateResources();
            	}

            	if (intent.getBooleanExtra("ShowClockButton", mShowClockButton) == false) {
            		mShowClockButton = false;
            		updateResources();
            	} else {
            		mShowClockButton = true;
            		updateResources();
            	}
            	if (intent.getBooleanExtra("ShowLockscreenButton", mShowLockscreenButton) == false) {
            		mShowLockscreenButton = false;
            		updateResources();
            	} else {
            		mShowLockscreenButton = true;
            		updateResources();
            	}
            	if (intent.getBooleanExtra("ShowMeterButton", mShowMeterButton) == false) {
            		mShowMeterButton = false;
            		updateResources();
            	} else {
            		mShowMeterButton = true;
            		updateResources();
            	}
            	if (intent.getBooleanExtra("ShowNavButton", mShowNavButton) == false) {
            		mShowNavButton = false;
            		updateResources();
            	} else {
            		mShowNavButton = true;
            		updateResources();
            	}
            	if (intent.getBooleanExtra("ShowPulldownButton", mShowPulldownButton) == false) {
            		mShowPulldownButton = false;
            		updateResources();
            	} else {
            		mShowPulldownButton = true;
            		updateResources();
            	}
            	if (intent.getBooleanExtra("ShowToggleButton", mShowToggleButton) == false) {
            		mShowToggleButton = false;
            		updateResources();
            	} else {
            		mShowToggleButton = true;
            		updateResources();
            	}
            	  	if (intent.getBooleanExtra("ShowQuietButton", mShowQuietButton) == false) {
            		mShowQuietButton = false;
            		updateResources();
            	} else {
            		mShowQuietButton = true;
            		updateResources();
            	}
            	if (intent.getBooleanExtra("ShowIconButton", mShowIconButton) == false) {
            		mShowIconButton = false;
            		updateResources();
            	} else {
            		mShowIconButton = true;
            		updateResources();
            	}
            	if (intent.getBooleanExtra("ShowLedButton", mShowLedButton) == false) {
            		mShowLedButton = false;
            		updateResources();
            	} else {
            		mShowLedButton = true;
            		updateResources();
            	}
            }
        }
    };    

    
	abstract void updateResources();
	abstract void toggleOn();
	abstract void toggleOff();
	abstract boolean getStatusOn();
	abstract void showSettings();    
    

} //



