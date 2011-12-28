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
import android.database.ContentObserver;
import android.os.Handler;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.provider.Telephony;
import android.util.AttributeSet;
import android.util.Slog;
import android.view.View;
import android.widget.TextView;

import com.android.internal.telephony.Phone;





/**
 * This widget display an analogic clock with two hands for hours and
 * minutes.
 */
public class CarrierLabel extends TextView {
    private boolean mAttached;
    private boolean mShowCarrier = true;
    private int mCarrierColor = 0xff33b5e5;
    private boolean mCustomCarrier = true;
    private String mCustomCarrierText = "Tranquil Ice";
    private String mDefaultCarrierText = "";
    
    
    
    Handler mHandler = new Handler();
    final ShowCarrierObserver mShowCarrierObserver = new ShowCarrierObserver(mHandler);
    final CarrierColorObserver mCarrierColorObserver = new CarrierColorObserver(mHandler);
    final CustomCarrierObserver mCustomCarrierObserver = new CustomCarrierObserver(mHandler) ;
    final CustomCarrierTextObserver mCustomCarrierTextObserver = new CustomCarrierTextObserver(mHandler) ;


    // ShowCarrier settings observer
    class ShowCarrierObserver extends ContentObserver{
    	
    	public ShowCarrierObserver(Handler handler) {
    		super(handler);
    	}

 
        @Override
        public void onChange(boolean selfChange){
        	
            try {
				mShowCarrier = (Boolean)(Settings.System.getInt(getContext().getContentResolver(), Settings.System.STATUSBAR_CARRIER_SHOW) == 1);
			} catch (SettingNotFoundException e) {
				e.printStackTrace();
			}
            
            UpdateCarrierLabel();
        }
    }
    
    
    
    
    // CarrierColor settings observer
    class CarrierColorObserver extends ContentObserver{
    	
    	public CarrierColorObserver(Handler handler) {
    		super(handler);
    	}

        @Override
        public void onChange(boolean selfChange){
        	
            try {
				mCarrierColor = (Integer) Settings.System.getInt(getContext().getContentResolver(), Settings.System.STATUSBAR_CARRIER_COLOR);
			} catch (SettingNotFoundException e) {
				e.printStackTrace();
			}
            
            UpdateCarrierLabel();
        }
    }
    
  
    
    // CarrierCustom settings observer
    class CustomCarrierObserver extends ContentObserver{
    	
    	public CustomCarrierObserver(Handler handler) {
    		super(handler);
    	}

        @Override
        public void onChange(boolean selfChange){
        	
            try {
				mCustomCarrier = (Boolean)(Settings.System.getInt(getContext().getContentResolver(), Settings.System.STATUSBAR_CARRIER_CUSTOM) == 1);
			} catch (SettingNotFoundException e) {
				e.printStackTrace();
			}
            
            UpdateCarrierLabel();
        }
    }    
    
   
    
    
    // CarrierCustomText settings observer
    class CustomCarrierTextObserver extends ContentObserver{
    	
    	public CustomCarrierTextObserver(Handler handler) {
    		super(handler);
    	}

        @Override
        public void onChange(boolean selfChange){
        	
            mCustomCarrierText = (String) Settings.System.getString(getContext().getContentResolver(), Settings.System.STATUSBAR_CARRIER_CUSTOM_TEXT);
            
            UpdateCarrierLabel();
        }
    }    
    
 

    public CarrierLabel(Context context) {
        this(context, null);
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
            
            
            getContext().getContentResolver().registerContentObserver(
                    Settings.System.getUriFor(Settings.System.STATUSBAR_CARRIER_SHOW), true, mShowCarrierObserver);
            
            getContext().getContentResolver().registerContentObserver(
                    Settings.System.getUriFor(Settings.System.STATUSBAR_CARRIER_COLOR), true, mCarrierColorObserver);
            
            getContext().getContentResolver().registerContentObserver(
                    Settings.System.getUriFor(Settings.System.STATUSBAR_CARRIER_CUSTOM), true, mCustomCarrierObserver);
            
            getContext().getContentResolver().registerContentObserver(
                    Settings.System.getUriFor(Settings.System.STATUSBAR_CARRIER_CUSTOM_TEXT), true, mCustomCarrierTextObserver);
            
            
            
            try {
				mShowCarrier = (Boolean)(Settings.System.getInt(getContext().getContentResolver(), Settings.System.STATUSBAR_CARRIER_SHOW) == 1);
			} catch (SettingNotFoundException e) {
				e.printStackTrace();
			}

            try {
				mCarrierColor = (Integer) Settings.System.getInt(getContext().getContentResolver(), Settings.System.STATUSBAR_CARRIER_COLOR);
			} catch (SettingNotFoundException e) {
				e.printStackTrace();
			}
            
            try {
				mCustomCarrier = (Boolean) (Settings.System.getInt(getContext().getContentResolver(), Settings.System.STATUSBAR_CARRIER_CUSTOM) == 1);
			} catch (SettingNotFoundException e) {
				e.printStackTrace();
			}
            
            mCustomCarrierText = (String)Settings.System.getString(getContext().getContentResolver(), Settings.System.STATUSBAR_CARRIER_CUSTOM_TEXT);
            
            UpdateCarrierLabel();
    		
            IntentFilter filter = new IntentFilter();
            filter.addAction(Telephony.Intents.SPN_STRINGS_UPDATED_ACTION);
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
        UpdateCarrierLabel();
    }

    
    
    void UpdateCarrierLabel(){
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
        	
        
    	
    }
    
    
}


