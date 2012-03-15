package com.android.systemui.tranqtoggles;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.provider.Telephony;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.systemui.R;



public class TranqToggleViewBottom extends LinearLayout {

	private final String Tranq_Settings = "TRANQ_SETTINGS";
	private SharedPreferences mPrefs;


    


	public TranqToggleViewBottom(Context context, AttributeSet attrs) {
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

   		TranqToggleViewTop.mToggleColor = mPrefs.getInt("toggle_color", 0xff3b3b3b);
   		TranqToggleViewTop.mTogglesOn = mPrefs.getBoolean("toggles_show_toggles", true);
   		TranqToggleViewTop.mTogglesTop = mPrefs.getBoolean("toggles_top", true);
   		TranqToggleViewTop.mToggleIndOnColor = mPrefs.getInt("toggle_ind_on_color", 0xffffbb33);
   		TranqToggleViewTop.mToggleIndOffColor = mPrefs.getInt("toggle_ind_off_color", 0xffba7b00);
   		TranqToggleViewTop.mToggleTextOnColor = mPrefs.getInt("toggle_text_on_color", 0xffffbb33);
   		TranqToggleViewTop.mToggleTextOffColor = mPrefs.getInt("toggle_text_off_color", 0xffba7b00);
   		TranqToggleViewTop.mToggleDivColor = mPrefs.getInt("toggle_divider_color", 0xff535252);
   		
   		setBackgroundColor(TranqToggleViewTop.mToggleColor);
   		
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
             	
            	TranqToggleViewTop.mToggleColor = intent.getIntExtra("ToggleColor", TranqToggleViewTop.mToggleColor);	
            	setBackgroundColor(TranqToggleViewTop.mToggleColor);
            	
            	updateView();

            }
        }    
    };
       

    private void updateView(){
    
    	if (!TranqToggleViewTop.mTogglesOn) {
    		setVisibility(View.GONE);
    	} else if (TranqToggleViewTop.mTogglesTop) {
    		setVisibility(View.GONE);
    		} else {
    			setVisibility(View.VISIBLE);
    		}
    	}

}
