/*
 * Copyright (C) 2011 The Android Open Source Project
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

package com.android.systemui.statusbar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Slog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.android.systemui.statusbar.policy.NetworkController;

import com.android.systemui.R;



// Intimately tied to the design of res/layout/signal_cluster_view.xml
public class SignalClusterView
        extends LinearLayout 
        implements NetworkController.SignalCluster {

    static final boolean DEBUG = false;
    static final String TAG = "SignalClusterView";
    
    private final String Tranq_Settings = "TRANQ_SETTINGS";
    private SharedPreferences mPrefs;
    private int mIconColorHue = 0;
    private int mHueIntens = 0;
    private int mIconColorShade = 0xff33b5e5;
    private int mShadeIntens = 0;
    
    
    NetworkController mNC;

    private boolean mWifiVisible = false;
    private int mWifiStrengthId = 0, mWifiActivityId = 0;
    private boolean mMobileVisible = false;
    private int mMobileStrengthId = 0, mMobileActivityId = 0, mMobileTypeId = 0;
    private boolean mIsAirplaneMode = false;
    private String mWifiDescription, mMobileDescription, mMobileTypeDescription;

    ViewGroup mWifiGroup, mMobileGroup;
    ImageView mWifi, mMobile, mWifiActivity, mMobileActivity, mMobileType;
    View mSpacer;

    public SignalClusterView(Context context) {
        this(context, null);
    }

    public SignalClusterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SignalClusterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setNetworkController(NetworkController nc) {
        if (DEBUG) Slog.d(TAG, "NetworkController=" + nc);
        mNC = nc;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        mWifiGroup      = (ViewGroup) findViewById(R.id.wifi_combo);
        mWifi           = (ImageView) findViewById(R.id.wifi_signal);
        mWifiActivity   = (ImageView) findViewById(R.id.wifi_inout);
        mMobileGroup    = (ViewGroup) findViewById(R.id.mobile_combo);
        mMobile         = (ImageView) findViewById(R.id.mobile_signal);
        mMobileActivity = (ImageView) findViewById(R.id.mobile_inout);
        mMobileType     = (ImageView) findViewById(R.id.mobile_type);
        mSpacer         =             findViewById(R.id.spacer);

        
  		Context settingsContext = getContext();
		try {
			settingsContext = getContext().createPackageContext("com.android.settings",0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     		
		mPrefs = settingsContext.getSharedPreferences("Tranquility_Settings", Context.MODE_PRIVATE);
   		mIconColorShade = mPrefs.getInt("icon_color", 0xff33b5e5);
        
        
        IntentFilter filter = new IntentFilter();
        filter.addAction(Tranq_Settings);
        getContext().registerReceiver(mIntentReceiver, filter, null, getHandler());
        
        
        
        
        apply();
    }

    @Override
    protected void onDetachedFromWindow() {
        mWifiGroup      = null;
        mWifi           = null;
        mWifiActivity   = null;
        mMobileGroup    = null;
        mMobile         = null;
        mMobileActivity = null;
        mMobileType     = null;

        super.onDetachedFromWindow();
    }
    
    
    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            
            if (action.equals("TRANQ_SETTINGS")) {
            	mIconColorHue = intent.getIntExtra("IconColorHue", mIconColorHue);
            	mHueIntens = intent.getIntExtra("IconColorHueIntens", mHueIntens);
            	mIconColorShade= intent.getIntExtra("IconColorShade", mIconColorShade);
            	mShadeIntens = intent.getIntExtra("IconColorShadeIntens", mShadeIntens);
            	Log.e("***************************************************  ", String.valueOf(mShadeIntens));
            	apply();
            }
        }
    };     
    
    

    public void setWifiIndicators(boolean visible, int strengthIcon, int activityIcon,
            String contentDescription) {
        mWifiVisible = visible;
        mWifiStrengthId = strengthIcon;
        mWifiActivityId = activityIcon;
        mWifiDescription = contentDescription;

        apply();
    }

    public void setMobileDataIndicators(boolean visible, int strengthIcon, int activityIcon,
            int typeIcon, String contentDescription, String typeContentDescription) {
        mMobileVisible = visible;
        mMobileStrengthId = strengthIcon;
        mMobileActivityId = activityIcon;
        mMobileTypeId = typeIcon;
        mMobileDescription = contentDescription;
        mMobileTypeDescription = typeContentDescription;

        apply();
    }

    public void setIsAirplaneMode(boolean is) {
        mIsAirplaneMode = is;
    }

    // Run after each indicator change.
    private void apply() {
        if (mWifiGroup == null) return;

        if (mWifiVisible) {
            mWifiGroup.setVisibility(View.VISIBLE);
            mWifi.setImageResource(mWifiStrengthId);
            mWifiActivity.setImageResource(mWifiActivityId);
            mWifiGroup.setContentDescription(mWifiDescription);
            mWifi.setColorFilter(ColorFilterMaker.changeHue(mIconColorHue, mHueIntens));
        } else {
            mWifiGroup.setVisibility(View.GONE);
        }

        if (DEBUG) Slog.d(TAG,
                String.format("wifi: %s sig=%d act=%d",
                    (mWifiVisible ? "VISIBLE" : "GONE"),
                    mWifiStrengthId, mWifiActivityId));

        if (mMobileVisible) {
            mMobileGroup.setVisibility(View.VISIBLE);
            mMobile.setImageResource(mMobileStrengthId);
            mMobileActivity.setImageResource(mMobileActivityId);
            mMobileType.setImageResource(mMobileTypeId);
            mMobileGroup.setContentDescription(mMobileTypeDescription + " " + mMobileDescription);
            mMobile.setColorFilter(ColorFilterMaker.shadeColor(mIconColorShade, mShadeIntens));
        } else {
            mMobileGroup.setVisibility(View.GONE);
        }

        if (mMobileVisible && mWifiVisible && mIsAirplaneMode) {
            mSpacer.setVisibility(View.INVISIBLE);
        } else {
            mSpacer.setVisibility(View.GONE);
        }

        if (DEBUG) Slog.d(TAG,
                String.format("mobile: %s sig=%d act=%d typ=%d",
                    (mMobileVisible ? "VISIBLE" : "GONE"),
                    mMobileStrengthId, mMobileActivityId, mMobileTypeId));

        mMobileType.setVisibility(
                !mWifiVisible ? View.VISIBLE : View.GONE);
    }
    
    
    public static class ColorFilterMaker
    {
        /**
     * Makes a ColorFilter
     * 
     * @param newColor new color of image when filter is applied.
     * @return
     */    	
    public ColorFilter changeColor(int newColor )
    {
        ColorMatrix cm = new ColorMatrix();

        changeColor(cm, newColor);

        return new ColorMatrixColorFilter(cm);
    }
  

    public static ColorFilter shadeColor(int newColor, int intens )
    {
        ColorMatrix cm = new ColorMatrix();

        changeExistingColor(cm, newColor, intens);

        return new ColorMatrixColorFilter(cm);
    }    

    public static ColorFilter changeHue(int newHue, int intens )
    {
        ColorMatrix cm = new ColorMatrix();

        changeHue(cm, newHue, intens);

        return new ColorMatrixColorFilter(cm);
    }        
    
    
    // This works for a b/w or grayscale image
    private static void changeColor(ColorMatrix cm, int newColor) {
        
    	float A = (float)Color.alpha(newColor);
        float R = (float)Color.red(newColor);
        float G = (float)Color.green(newColor);
        float B =  (float)Color.blue(newColor);
        
        float[] matrix = new float[]
            {       
            R/255f		,0			,0			,0			,0    // Red
            ,0			,G/255f		,0    		,0			,0    // Green
            ,0   		,0			,B/255f		,0			,0    // Blue
            ,0    		,0     		,0      	,A/255f 	,0f   // Alpha
            };
        
        
        cm.postConcat(new ColorMatrix(matrix));
    }

    private static void changeExistingColor(ColorMatrix cm, int newColor, int itens) {
        
    	float A = (float)Color.alpha(newColor) / 255;
        float R = (float)Color.red(newColor) / 255;
        float G = (float)Color.green(newColor) / 255;
        float B =  (float)Color.blue(newColor) / 255;
        
    	
        
        float[] mat = new float[]
                {       
                R			,0			,0			,0		,itens  // Red
                ,0			,G			,0    		,0		,itens  // Green
                ,0   		,0			,B			,0		,itens	// Blue
                ,0    		,0     		,0      	,1		,0  	// Alpha
                };      
        
        float[] matrix = new float[]
                {       
        		.5f		,.5f	,.5f	,0			,0    // Red
               ,.5f		,.5f	,.5f	,0			,0    // Green
               ,.5f		,.5f	,.5f	,0			,0    // Blue
               ,0   	,0 		,0 		,1			,0    // Alpha
               };    
        
        cm.setConcat(new ColorMatrix(mat), new ColorMatrix(matrix));
        
    
    }
    
    
    
    public static void changeHue(ColorMatrix cm, int newHue, int intens)
    {
    	
     
           
        float cosVal = (float) Math.cos(newHue);
        float sinVal = (float) Math.sin(newHue);
        float lumR = 0.212671f;
        float lumG = 0.715160f;
        float lumB = 0.072169f;  
        newHue = newHue / 3;
        
        if (newHue > 180) newHue = 180 - newHue;
        
        Log.e("***********************************************    ", String.valueOf(newHue));
       
        
        float[] mat = new float[]
        { 
                lumR + cosVal * (1 - lumR) + sinVal * (-lumR)		, lumG + cosVal * (-lumG) + sinVal * (-lumG)		, lumB + cosVal * (-lumB) + sinVal * (1 - lumB)		, 0		, intens, 
                lumR + cosVal * (-lumR) + sinVal * (0.143f)			, lumG + cosVal * (1 - lumG) + sinVal * (0.140f)	, lumB + cosVal * (-lumB) + sinVal * (-0.283f)		, 0		, intens,
                lumR + cosVal * (-lumR) + sinVal * (-(1 - lumR))	, lumG + cosVal * (-lumG) + sinVal * (lumG)			, lumB + cosVal * (1 - lumB) + sinVal * (lumB)		, 0		, intens, 
                0f, 0f, 0f, 1f, 0f, 
                0f, 0f, 0f, 0f, 1f };
              
        
        cm.postConcat(new ColorMatrix(mat));
        //cm.setConcat(new ColorMatrix(mat), new ColorMatrix(matrix));
    }

    
    
    
    
    }  
    
}

