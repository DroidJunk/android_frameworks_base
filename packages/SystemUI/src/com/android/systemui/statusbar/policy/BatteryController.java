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
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.os.BatteryManager;
import android.os.Handler;
import android.provider.Settings;
import android.util.Slog;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.systemui.R;

public class BatteryController extends BroadcastReceiver {
    private static final String TAG = "StatusBar.BatteryController";

    private Context mContext;
    private ArrayList<ImageView> mIconViews = new ArrayList<ImageView>();
    private ArrayList<TextView> mLabelViews = new ArrayList<TextView>();
    private int mBattIcon;
    private int mChargeIcon;

    private boolean mHide;
    private boolean mStock;
    private boolean mIcs;
    private boolean mBall;
    private boolean mVertical;
    private boolean mAnimate;
    private Handler mHandler;

    public BatteryController(Context context) {
        mContext = context;

        mStock = (Settings.System.getInt(mContext.getContentResolver(), Settings.System.BATTERY_ICON, 1) == 1);
        mIcs = (Settings.System.getInt(mContext.getContentResolver(), Settings.System.BATTERY_ICON, 1) == 2);
        mVertical = (Settings.System.getInt(mContext.getContentResolver(), Settings.System.BATTERY_ICON, 1) == 3);
        mBall = (Settings.System.getInt(mContext.getContentResolver(), Settings.System.BATTERY_ICON, 1) == 4);
        mAnimate = (Settings.System.getInt(mContext.getContentResolver(), Settings.System.BATTERY_ICON, 1) == 5);
        mHide = (Settings.System.getInt(mContext.getContentResolver(), Settings.System.BATTERY_ICON, 1) == 6);

        mHandler = new Handler();
        SettingsObserver settingsObserver = new SettingsObserver(mHandler);
        settingsObserver.observe();

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        context.registerReceiver(this, filter);
    }

    class SettingsObserver extends ContentObserver {
        SettingsObserver(Handler handler) {
            super(handler);
        }
        void observe() {
            ContentResolver resolver = mContext.getContentResolver();
            resolver.registerContentObserver(Settings.System.getUriFor(Settings.System.BATTERY_ICON), false, this);
        }

        @Override
        public void onChange(boolean selfChange) {
            updateSettings();
        }
    }

    public void addIconView(ImageView v) {
        mIconViews.add(v);
    }

    public void addLabelView(TextView v) {
        mLabelViews.add(v);
    }

    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if (action.equals(Intent.ACTION_BATTERY_CHANGED)) {
            final int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            final boolean plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0) != 0;
            if (mStock) {
                mBattIcon = R.drawable.stat_sys_battery;
                mChargeIcon = R.drawable.stat_sys_battery_charge;
            } else if (mIcs) {
                mBattIcon = R.drawable.stock_sys_battery;
                mChargeIcon = R.drawable.stock_sys_battery_charge;
            } else if (mVertical) {
                mBattIcon = R.drawable.stat_vertical_battery;
                mChargeIcon = R.drawable.stat_vertical_battery_charge;
            } else if (mBall) {
                mBattIcon = R.drawable.stat_ball_battery;
                mChargeIcon = R.drawable.stat_ball_battery_charge;
            } else if (mAnimate) {
                mBattIcon = R.drawable.stat_animate_battery;
                mChargeIcon = R.drawable.stat_animate_battery_charge;
            }
            final int icon = plugged ? mChargeIcon 
                                     : mBattIcon;
            int N = mIconViews.size();
            for (int i=0; i<N; i++) {
                ImageView v = mIconViews.get(i);
                v.setImageResource(icon);
                v.setImageLevel(level);
                v.setContentDescription(mContext.getString(R.string.accessibility_battery_level,
                        level));
                if (mHide)
                    v.setVisibility(View.GONE);
                else
                    v.setVisibility(View.VISIBLE);
            }
            N = mLabelViews.size();
            for (int i=0; i<N; i++) {
                TextView v = mLabelViews.get(i);
                v.setText(mContext.getString(R.string.status_bar_settings_battery_meter_format,
                        level));
            }
        }
    }

    private void updateSettings() {
        ContentResolver resolver = mContext.getContentResolver();
        mStock = (Settings.System.getInt(resolver, Settings.System.BATTERY_ICON, 1) == 1);
        mIcs = (Settings.System.getInt(resolver, Settings.System.BATTERY_ICON, 1) == 2);
        mVertical = (Settings.System.getInt(resolver, Settings.System.BATTERY_ICON, 1) == 3);
        mBall = (Settings.System.getInt(resolver, Settings.System.BATTERY_ICON, 1) == 4);
        mAnimate = (Settings.System.getInt(resolver, Settings.System.BATTERY_ICON, 1) == 5);
        mHide = (Settings.System.getInt(resolver, Settings.System.BATTERY_ICON, 1) == 6);
    }
}
