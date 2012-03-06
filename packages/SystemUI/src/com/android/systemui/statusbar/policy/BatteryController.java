/*
 * Copyright (C) 2010 The Android Open Source Project
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

package com.android.systemui.statusbar.policy;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.text.style.CharacterStyle;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.os.BatteryManager;
import android.os.Handler;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.systemui.R;
import com.android.systemui.statusbar.SignalClusterView.ColorFilterMaker;

public class BatteryController extends LinearLayout {
    private static final String TAG = "StatusBar.BatteryController";

    private Context mContext;
    private ArrayList<ImageView> mIconViews = new ArrayList<ImageView>();
    private ArrayList<TextView> mLabelViews = new ArrayList<TextView>();

    private ImageView mBatteryIcon;

    private static int mBatteryStyle;

    private final String Tranq_Settings = "TRANQ_SETTINGS";
    private int mIconColorHue = 0;
    private int mHueIntens = 0;
    private int mIconColorShade = 0xff33b5e5;
    private int mShadeIntens = 0;

    private int mLevel = -1;
    private boolean mPlugged = false;

    public static final int STYLE_STOCK = 0;
    public static final int STYLE_ICS = 1;
    public static final int STYLE_VERTICAL = 2;
    public static final int STYLE_BALL = 3;
    public static final int STYLE_CIRCLE = 4;
    public static final int STYLE_ANIMATED = 5;
    public static final int STYLE_HIDE = 6;

    public BatteryController(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        init();
        mBatteryIcon = (ImageView) findViewById(R.id.battery);
        addIconView(mBatteryIcon);

        SettingsObserver settingsObserver = new SettingsObserver(new Handler());
        settingsObserver.observe();
        updateSettings(); // to initialize values

    }

    private void init() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        filter.addAction(Tranq_Settings);
        mContext.registerReceiver(mBatteryBroadcastReceiver, filter);
    }

    public void addIconView(ImageView v) {
        mIconViews.add(v);
    }

    public void addLabelView(TextView v) {
        mLabelViews.add(v);
    }

    private BroadcastReceiver mBatteryBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action.equals(Intent.ACTION_BATTERY_CHANGED)) {
                final int level = intent.getIntExtra(
                        BatteryManager.EXTRA_LEVEL, 0);
                final boolean plugged = intent.getIntExtra(
                        BatteryManager.EXTRA_PLUGGED, 0) != 0;
                setBatteryIcon(level, plugged);
            }
            
            if (action.equals("TRANQ_SETTINGS")) {
            	mIconColorHue = intent.getIntExtra("IconColorHue", mIconColorHue);
            	mHueIntens = intent.getIntExtra("IconColorHueIntens", mHueIntens);
            	mIconColorShade= intent.getIntExtra("IconColorShade", mIconColorShade);
            	mShadeIntens = intent.getIntExtra("IconColorShadeIntens", mShadeIntens);
            	updateSettings();
            }
        }
    };

    
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
  

    public static ColorFilter changeExistingColor(int currentColor, int newColor )
    {
        ColorMatrix cm = new ColorMatrix();

        changeExistingColor(cm, currentColor, newColor);

        return new ColorMatrixColorFilter(cm);
    }    

    public static ColorFilter changeHue(int currentColor, int newColor )
    {
        ColorMatrix cm = new ColorMatrix();

        changeHue(cm, currentColor, newColor);

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

    private static void changeExistingColor(ColorMatrix cm, int c, int newColor) {
        
    	float A = (float)Color.alpha(newColor) / 255;
        float R = (float)Color.red(newColor) / 255;
        float G = (float)Color.green(newColor) / 255;
        float B =  (float)Color.blue(newColor) / 255;
        
    	float cA = (float)Color.alpha(c) / 255;
        float cR = (float)Color.red(c) / 255;
        float cG = (float)Color.green(c) / 255;
        float cB = (float)Color.blue(c) / 255;
        

        
        float[] mat = new float[]
                {       
                R			,0			,0			,0		,c    // Red
                ,0			,G			,0    		,0		,c    // Green
                ,0   		,0			,B			,0		,c    // Blue
                ,0    		,0     		,0      	,1		,0   // Alpha
                //,c    		,c     		,c      	,0	 	,1   // ??
                };      
        
        float[] matrix = new float[]
                {       
        		.5f	,.5f	,.5f	,0			,0    // Red
               ,.5f	,.5f	,.5f	,0			,0    // Green
               ,.5f	,.5f	,.5f	,0			,0    // Blue
               ,0   	,0 		,0 		,1			,0    // Alpha
               //,0		,0			,0			,0			,1
               };    
        

        //cm.postConcat(new ColorMatrix(matrix));
        //cm.set(new ColorMatrix(matrix));
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
    
    private void setBatteryIcon(int level, boolean plugged) {
        mLevel = level;
        mPlugged = plugged;
        ContentResolver cr = mContext.getContentResolver();
        mBatteryStyle = Settings.System.getInt(cr,
                Settings.System.BATTERY_ICON, 0);
        int icon;
        if (mBatteryStyle == STYLE_STOCK) {
            icon = plugged ? R.drawable.stat_sys_battery_charge
                    : R.drawable.stat_sys_battery;
        } else if (mBatteryStyle == STYLE_ICS) {
            icon = plugged ? R.drawable.stock_sys_battery_charge
                    : R.drawable.stock_sys_battery;
        } else if (mBatteryStyle == STYLE_VERTICAL) {
            icon = plugged ? R.drawable.stat_vertical_battery_charge
                    : R.drawable.stat_vertical_battery;
        } else if (mBatteryStyle == STYLE_BALL) {
            icon = plugged ? R.drawable.stat_ball_battery_charge
                    : R.drawable.stat_ball_battery;
        } else if (mBatteryStyle == STYLE_CIRCLE) {
            icon = plugged ? R.drawable.stat_bcircle_battery_charge
                    : R.drawable.stat_bcircle_battery;
        } else if (mBatteryStyle == STYLE_ANIMATED) {
            icon = plugged ? R.drawable.stat_animate_battery_charge
                    : R.drawable.stat_animate_battery;
       } else {
            icon = plugged ? R.drawable.stat_sys_battery_charge
                    : R.drawable.stat_sys_battery;
        }
        int N = mIconViews.size();
        for (int i = 0; i < N; i++) {
            ImageView v = mIconViews.get(i);
            v.setImageResource(icon);
            v.setImageLevel(level);
            v.setContentDescription(mContext.getString(
                    R.string.accessibility_battery_level, level));
            v.setColorFilter(ColorFilterMaker.changeHue(mIconColorHue, mHueIntens));
        }
        N = mLabelViews.size();
        for (int i = 0; i < N; i++) {
            TextView v = mLabelViews.get(i);
            v.setText(mContext.getString(
                    R.string.status_bar_settings_battery_meter_format, level));
        }
    }

    class SettingsObserver extends ContentObserver {
        SettingsObserver(Handler handler) {
            super(handler);
        }

        void observe() {
            ContentResolver resolver = mContext.getContentResolver();
            resolver.registerContentObserver(Settings.System
                    .getUriFor(Settings.System.BATTERY_ICON), false,
                    this);
        }

        @Override
        public void onChange(boolean selfChange) {
            updateSettings();
        }
    }

    private void updateSettings() {
        // Slog.i(TAG, "updated settings values");
        ContentResolver cr = mContext.getContentResolver();
        mBatteryStyle = Settings.System.getInt(cr,
                Settings.System.BATTERY_ICON, 0);

        switch (mBatteryStyle) {
            case STYLE_STOCK:;
                mBatteryIcon.setVisibility(View.VISIBLE);
                setVisibility(View.VISIBLE);
                break;
            case STYLE_ICS:
                mBatteryIcon.setVisibility(View.VISIBLE);
                setVisibility(View.VISIBLE);
                break;
            case STYLE_VERTICAL:
                mBatteryIcon.setVisibility(View.VISIBLE);
                setVisibility(View.VISIBLE);
                break;
            case STYLE_BALL:
                mBatteryIcon.setVisibility(View.VISIBLE);
                setVisibility(View.VISIBLE);
                break;
            case STYLE_CIRCLE:
                mBatteryIcon.setVisibility(View.VISIBLE);
                setVisibility(View.VISIBLE);
                break;
            case STYLE_ANIMATED:
                mBatteryIcon.setVisibility(View.VISIBLE);
                setVisibility(View.VISIBLE);
                break;
            case STYLE_HIDE:
                mBatteryIcon.setVisibility(View.GONE);
                setVisibility(View.GONE);
                break;
            default:
                mBatteryIcon.setVisibility(View.VISIBLE);
                setVisibility(View.VISIBLE);
                break;
        }

        setBatteryIcon(mLevel, mPlugged);

    }
}
