package com.android.systemui.tranqtoggles;


import com.android.systemui.R;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;




public class TranqSoundButton extends TranqToggleButton {

	private View showSound;
	private View mIndicator;
	private ImageView mIcon;
	private View mDivider;
	private BroadcastReceiver mBroadcastReciver;
	private AudioManager mAudioManager;	


	public TranqSoundButton(Context context, AttributeSet attrs) {
		super(context, attrs);

		mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

	}




	protected void onAttachedToWindow(){
		super.onAttachedToWindow();

		showSound = (View) getRootView().findViewById(R.id.button_4);
		mIndicator = (View) getRootView().findViewById(R.id.indicator_4);
		mIcon = (ImageView) getRootView().findViewById(R.id.sound_icon);	
		mDivider = (View) getRootView().findViewById(R.id.divider_4);

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

			mIndicator.setBackgroundColor(TranqToggleViewTop.mToggleIndOnColor);
			mIcon.setImageResource(R.drawable.tranqtoggle_sound_on);
			mIcon.setColorFilter(TranqToggleViewTop.mToggleIconOnColor);
			setTextColor(TranqToggleViewTop.mToggleTextOnColor);
			} else 
				if (mAudioManager.getRingerMode() == mAudioManager.RINGER_MODE_VIBRATE) {
				mIndicator.setBackgroundColor(TranqToggleViewTop.mToggleIndOffColor);
				mIcon.setImageResource(R.drawable.tranqtoggle_sound_off_vibrate);
				mIcon.setColorFilter(TranqToggleViewTop.mToggleIconInterColor);
				setTextColor(TranqToggleViewTop.mToggleTextOffColor);

			} else {
				mIcon.setImageResource(R.drawable.tranqtoggle_sound_off);
				mIcon.setColorFilter(TranqToggleViewTop.mToggleIconOffColor);
				mIndicator.setBackgroundColor(TranqToggleViewTop.mToggleIndOffColor);
				setTextColor(TranqToggleViewTop.mToggleTextOffColor);
		}

		mDivider.setBackgroundColor(TranqToggleViewTop.mToggleDivColor);
		
		if (TranqToggleButton.mShowSound) {
			showSound.setVisibility(View.VISIBLE);
		} else {
			showSound.setVisibility(View.GONE);
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
