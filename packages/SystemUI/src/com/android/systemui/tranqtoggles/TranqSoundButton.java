package com.android.systemui.tranqtoggles;


import com.android.systemui.R;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.util.AttributeSet;
import android.view.View;




public class TranqSoundButton extends TranqToggleButton {
	
	private View mIndicator;
	private View mIcon;
	private BroadcastReceiver mBroadcastReciver;
	private AudioManager mAudioManager;	


	public TranqSoundButton(Context context, AttributeSet attrs) {
		super(context, attrs);

		mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		
	}

	
	
	
	protected void onAttachedToWindow(){
		super.onAttachedToWindow();
		
		mIndicator = (View) getRootView().findViewById(R.id.indicator_4);
		mIcon = (View) getRootView().findViewById(R.id.sound_icon);	
		
	    final IntentFilter mFilter = new IntentFilter();
	    mFilter.addAction("android.media.RINGER_MODE_CHANGED");
	    mBroadcastReciver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
            	updateResources();
            }
        };

        getContext().registerReceiver(mBroadcastReciver, mFilter);
		updateResources();
	}

	
	protected void onDetachedFromWindow(){
		getContext().unregisterReceiver(mBroadcastReciver);
	}


	@Override
	protected boolean getStatusOn(){
		return (mAudioManager.getRingerMode() == mAudioManager.RINGER_MODE_NORMAL);
	}

	@Override
	void updateResources() {

		if (mAudioManager.getRingerMode() == mAudioManager.RINGER_MODE_NORMAL) {
			mIndicator.setBackgroundColor(0xff000000);
			mIcon.setBackgroundResource(R.drawable.tranqtoggle_sound_on);
			setTextColor(0xff00ddff);
			} else 
				if (mAudioManager.getRingerMode() == mAudioManager.RINGER_MODE_VIBRATE) {
				mIndicator.setBackgroundColor(0xff000000);
				mIcon.setBackgroundResource(R.drawable.tranqtoggle_sound_off_vibrate);
				setTextColor(0xffffbb33);
				
			} else {
				mIcon.setBackgroundResource(R.drawable.tranqtoggle_sound_off);
				mIndicator.setBackgroundColor(0xff000000);
				setTextColor(0xffffffff);
		}
	}



	@Override
	void toggleOn() {
		if (mAudioManager.getRingerMode() == mAudioManager.RINGER_MODE_VIBRATE) {
			mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
		} else {		
		mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
		}
		updateResources();
	}


	@Override
	void toggleOff() {
		
		if (mAudioManager.getRingerMode() == mAudioManager.RINGER_MODE_NORMAL) {
			mAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
		} else {
		mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
		}
		updateResources();
	}


	@Override
	void showSettings() {

	    Intent i = new Intent();
	    i.setAction("android.intent.action.CLOSE_SYSTEM_DIALOGS");
	    getContext().sendBroadcast(i);
	    i.setAction("android.settings.SOUND_SETTINGS");
        i.setFlags(i.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(i);
	}





}  // 
