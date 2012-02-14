package com.android.systemui.tranqtoggles;

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


public abstract class TranqToggleButton extends TextView {

	private final String Tranq_Settings = "TRANQ_SETTINGS";
	private SharedPreferences mPrefs;
	public static boolean mShowFourg = true;
	public static boolean mShowWifi = true;
	public static boolean mShowGps = true;
	public static boolean mShowBluetooth = true;
	public static boolean mShowSound = true;
	public static boolean mShowAirplane = true;
	public static boolean mShowBrightness = true;
	public static boolean mShowRotate = true;
	public static boolean mShowSync = true;
	public static boolean mShowData = true;
	public static int mToggleIconOnColor = 0xff33b5e5;
	public static int mToggleIconInterColor = 0xffff0000;
	public static int mToggleIconOffColor = 0xff5d5d5d;

	


	public TranqToggleButton(Context context, AttributeSet attrs) {
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
		mShowFourg = mPrefs.getBoolean("toggles_show_fourg", true);
		mShowWifi = mPrefs.getBoolean("toggles_show_wifi", true);
		mShowGps = mPrefs.getBoolean("toggles_show_gps", true);
		mShowBluetooth = mPrefs.getBoolean("toggles_show_bluetooth", true);
		mShowSound = mPrefs.getBoolean("toggles_show_sound", true);
		mShowAirplane = mPrefs.getBoolean("toggles_show_airplane", true);
		mShowBrightness = mPrefs.getBoolean("toggles_show_brightness", true);
		mShowRotate = mPrefs.getBoolean("toggles_show_rotate", true);
		mShowSync = mPrefs.getBoolean("toggles_show_sync", true);
		mShowData = mPrefs.getBoolean("toggles_show_data", true);
   		mToggleIconOnColor = mPrefs.getInt("toggles_icon_on_color", 0xff33b5e5);
   		mToggleIconInterColor = mPrefs.getInt("toggles_icon_inter_color", 0xffff0000);
   		mToggleIconOffColor = mPrefs.getInt("toggles_icon_off_color", 0xff5d5d5d);

        
		

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


		setOnLongClickListener(new OnLongClickListener() { 
			public boolean onLongClick (View v){
				showSettings();
				return true;
			}
		}
		);

	}


    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("TRANQ_SETTINGS")) {
            	
            	if (intent.getIntExtra("ToggleIconOnColor", mToggleIconOnColor) != mToggleIconOnColor) {
            		updateResources();
            	}
            	if (intent.getIntExtra("ToggleIconInterColor", mToggleIconInterColor) != mToggleIconInterColor) {
            		updateResources();
            	}
            	if (intent.getIntExtra("ToggleIconOffColor", mToggleIconOffColor) != mToggleIconOffColor) {
            		updateResources();
            	}

            	if (intent.getBooleanExtra("UpdateToggles", false) == true) {
            		updateResources();
            	}
            	
            	if (intent.getBooleanExtra("ShowFourg", mShowFourg) == false) {
            		mShowFourg = false;
            		updateResources();
            	} else {
            		mShowFourg = true;
            		updateResources();
            	}
            	if (intent.getBooleanExtra("ShowWifi", mShowWifi) == false) {
            		mShowWifi = false;
            		updateResources();
            	} else {
            		mShowWifi = true;
            		updateResources();
            	}            	
            	if (intent.getBooleanExtra("ShowGps", mShowGps) == false) {
            		mShowGps = false;
            		updateResources();
            	} else {
            		mShowGps = true;
            		updateResources();
            	}            	
            	if (intent.getBooleanExtra("ShowBluetooth", mShowBluetooth) == false) {
            		mShowBluetooth = false;
            		updateResources();
            	} else {
            		mShowBluetooth = true;
            		updateResources();
            	}            	
            	if (intent.getBooleanExtra("ShowSound", mShowSound) == false) {
            		mShowSound = false;
            		updateResources();
            	} else {
            		mShowSound = true;
            		updateResources();
            	}
            	if (intent.getBooleanExtra("ShowAirplane", mShowAirplane) == false) {
            		mShowAirplane = false;
            		updateResources();
            	} else {
            		mShowAirplane = true;
            		updateResources();
            	}
            	if (intent.getBooleanExtra("ShowBrightness", mShowBrightness) == false) {
            		mShowBrightness = false;
            		updateResources();
            	} else {
            		mShowBrightness = true;
            		updateResources();
            	}            	
            	if (intent.getBooleanExtra("ShowRotate", mShowRotate) == false) {
            		mShowRotate = false;
            		updateResources();
            	} else {
            		mShowRotate = true;
            		updateResources();
            	}            	
            	if (intent.getBooleanExtra("ShowSync", mShowSync) == false) {
            		mShowSync = false;
            		updateResources();
            	} else {
            		mShowSync = true;
            		updateResources();
            	}            	
            	if (intent.getBooleanExtra("ShowData", mShowData) == false) {
            		mShowData = false;
            		updateResources();
            	} else {
            		mShowData = true;
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



