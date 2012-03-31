package com.android.systemui.quicksettings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;


public class QuickButtonsView extends LinearLayout {

	private final String Tranq_Settings = "TRANQ_SETTINGS";
	private SharedPreferences mPrefs;
	public static boolean mSettingsOn = true;
	public static boolean mSettingsBottom = true;
	public static int mToggleColor = 0xff3b3b3b;
	public static boolean mShowToggleDiv = true;
	public static int mToggleDivColor = 0xff535252;
 
	public QuickButtonsView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	protected void onFinishInflate(){
		super.onFinishInflate();
	}

	protected void onAttachedToWindow(){
		super.onAttachedToWindow();

  		Context settingsContext = getContext();
		try {
			settingsContext = getContext().createPackageContext("com.android.settings",0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     		
		mPrefs = settingsContext.getSharedPreferences("Tranquility_Settings", Context.MODE_PRIVATE);

		mSettingsOn = mPrefs.getBoolean("toggles_show_quicksettings", true);
		mSettingsBottom = mPrefs.getBoolean("quicksettings_bottom", true);
   		mToggleColor = mPrefs.getInt("toggle_color", 0xff3b3b3b);
  		mShowToggleDiv = mPrefs.getBoolean("toggle_show_divider", true);
   		mToggleDivColor = mPrefs.getInt("toggle_divider_color", 0xff535252);
   		
   		if (!mShowToggleDiv) {
   			mToggleDivColor = 0;
   		}
   		
   		setBackgroundColor(mToggleColor);
   		
   		updateView();
   		
        IntentFilter filter = new IntentFilter();
        filter.addAction(Tranq_Settings);
        getContext().registerReceiver(mIntentReceiver, filter, null, getHandler());
	}	
 
    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("TRANQ_SETTINGS")) {
             	
            	mSettingsOn = intent.getBooleanExtra("SettingsOn", mSettingsOn);	
            	mSettingsBottom = intent.getBooleanExtra("SettingsBottom", mSettingsBottom);
            	mToggleColor = intent.getIntExtra("ToggleColor", mToggleColor);	
            	mShowToggleDiv = intent.getBooleanExtra("ToggleShowDivider", mShowToggleDiv);
            	mToggleDivColor = intent.getIntExtra("ToggleDividerColor", mToggleDivColor);

            	setBackgroundColor(mToggleColor);
            	updateView();

            }
        }    
    };
       
    private void updateView(){

    	if (!mSettingsOn) {
    		setVisibility(View.GONE);
    	} else if (!mSettingsBottom) {
    		setVisibility(View.VISIBLE);
    		} else {
    		setVisibility(View.GONE);
    		}    	
    }
}
