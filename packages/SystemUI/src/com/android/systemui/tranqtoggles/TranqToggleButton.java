package com.android.systemui.tranqtoggles;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public abstract class TranqToggleButton extends TextView {

	private final String Tranq_Settings = "TRANQ_SETTINGS";
	private SharedPreferences mPrefs;
	
	
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
            	if (intent.getBooleanExtra("UpdateToggles", false) == true) updateResources();
            }
        }
    };    

    
	abstract void updateResources();
	abstract void toggleOn();
	abstract void toggleOff();
	abstract boolean getStatusOn();
	abstract void showSettings();    
    

} //


