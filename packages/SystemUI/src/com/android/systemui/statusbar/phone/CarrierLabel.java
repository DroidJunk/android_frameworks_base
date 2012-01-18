/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.systemui.statusbar.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.provider.Telephony;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Slog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;



/**
 * This widget display an analogic clock with two hands for hours and
 * minutes.
 */
public class CarrierLabel extends TextView {
	
	private final String Tranq_Settings = "TRANQ_SETTINGS";
    private boolean mAttached;
	private SharedPreferences mPrefs;
    private boolean mShowCarrier = true;
    private int mCarrierColor = 0xff33b5e5;
    private boolean mCustomCarrier = true;
    private String mCustomCarrierText = "Tranquil Ice";
    private int mCarrierSize = 15;
    private String mDefaultCarrierText = "";
    

    

    public CarrierLabel(Context context) {
        this(context, null);
        
        setClickable(true);
        
		setOnClickListener(new OnClickListener() { 
			public void onClick (View v){
	        	Intent i = new Intent();
	        	i.setAction(Tranq_Settings );
	       	   	i.putExtra("TogglesOn", true);
	       	   	getContext().sendBroadcast(i);
	       	   	i = null;
	       	   	
	        	i = new Intent();
	            i.setAction(Tranq_Settings );
	            i.putExtra("UpdateToggles", true);
	            getContext().sendBroadcast(i);
	            i = null;
			}
		}
		);
    }

    public CarrierLabel(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CarrierLabel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        updateNetworkName(false, null, false, null);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (!mAttached) {
            mAttached = true;
            
      		Context settingsContext = getContext();
			try {
				settingsContext = getContext().createPackageContext("com.android.settings",0);
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
     		
			mPrefs = settingsContext.getSharedPreferences("Tranquility_Settings", Context.MODE_PRIVATE);
     		
     		mShowCarrier = mPrefs.getBoolean("show_carrier", true);
    		mCarrierColor = mPrefs.getInt("carrier_color", 0xff33b5e5);
    		mCustomCarrier = mPrefs.getBoolean("carrier_custom", true);
    		mCustomCarrierText = mPrefs.getString("carrier_custom_text", "Tranquil Ice");
    		mCarrierSize = mPrefs.getInt("carrier_size", 15);
    		
            updateCarrierLabel();
    		
            IntentFilter filter = new IntentFilter();
            filter.addAction(Telephony.Intents.SPN_STRINGS_UPDATED_ACTION);
            filter.addAction(Tranq_Settings);
            getContext().registerReceiver(mIntentReceiver, filter, null, getHandler());
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAttached) {
            getContext().unregisterReceiver(mIntentReceiver);
            mAttached = false;
        }
    }

    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Telephony.Intents.SPN_STRINGS_UPDATED_ACTION.equals(action)) {
                updateNetworkName(intent.getBooleanExtra(Telephony.Intents.EXTRA_SHOW_SPN, false),
                        intent.getStringExtra(Telephony.Intents.EXTRA_SPN),
                        intent.getBooleanExtra(Telephony.Intents.EXTRA_SHOW_PLMN, false),
                        intent.getStringExtra(Telephony.Intents.EXTRA_PLMN));
            }
            
            if (action.equals("TRANQ_SETTINGS")) {
             	mShowCarrier = intent.getBooleanExtra("ShowCarrier", mShowCarrier);
            	mCustomCarrier = intent.getBooleanExtra("CustomCarrier", mCustomCarrier);
            	mCarrierColor = intent.getIntExtra("CarrierColor", mCarrierColor);	
            	if (intent.getStringExtra("CustomCarrierText") != null) mCustomCarrierText	= intent.getStringExtra("CustomCarrierText");
            	mCarrierSize = intent.getIntExtra("CarrierSize", mCarrierSize);
            	updateCarrierLabel();
            }
             
        }
    };

    void updateNetworkName(boolean showSpn, String spn, boolean showPlmn, String plmn) {
        Slog.d("CarrierLabel", "updateNetworkName showSpn=" + showSpn + " spn=" + spn
		        + " showPlmn=" + showPlmn + " plmn=" + plmn);
        StringBuilder str = new StringBuilder();
        boolean something = false;
        if (showPlmn && plmn != null) {
            str.append(plmn);
            something = true;
        }
        if (showSpn && spn != null) {
            if (something) {
                str.append('\n');
            }
            str.append(spn);
            something = true;
        }
        if (something) {
            setText(str.toString());
        } else {
        	setText(com.android.internal.R.string.lockscreen_carrier_default);
        }
        mDefaultCarrierText = (String) getText();
        updateCarrierLabel();
    }

    
    
    void updateCarrierLabel(){
    	
        if (mShowCarrier) {
        	setVisibility(View.VISIBLE);
              
        } else {
        	setVisibility(View.GONE);
        }
        
        setTextColor(mCarrierColor);
        
        if (mCustomCarrier) {
        	setText(mCustomCarrierText);
        } else {
        	setText(mDefaultCarrierText);
        }
        
        setTextSize(mCarrierSize);
     	
    }
    
    
}


