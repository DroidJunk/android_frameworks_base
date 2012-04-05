package com.android.systemui.quicksettings;


import com.android.systemui.R;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.os.Handler;
import android.provider.Settings;




public class ToggleButton extends QuickButton {

	private View showToggleButton;
	private ImageView mIcon;
	private View mDivider;




	public ToggleButton(Context context, AttributeSet attrs) {
		super(context, attrs);

	}




	protected void onAttachedToWindow(){
		super.onAttachedToWindow();
		showToggleButton = (View) getRootView().findViewById(R.id.button_toggles);
		mIcon = (ImageView) getRootView().findViewById(R.id.stat_toggles);
		mDivider = (View) getRootView().findViewById(R.id.sdivider_4);
		if (QuickButton.mCustomIconColors) mIcon.setColorFilter(QuickButton.mToggleIconOnColor);

	}


	protected void onDetachedFromWindow(){


	}


	@Override
	protected boolean getStatusOn(){
		return false;


	}

	@Override
	void updateResources() {

		mIcon.clearColorFilter();
		if (QuickButton.mCustomIconColors) mIcon.setColorFilter(QuickButton.mToggleIconOnColor);
		if (QuickButton.mShowToggleButton) {
			showToggleButton.setVisibility(View.VISIBLE);
		} else {
			showToggleButton.setVisibility(View.GONE);
		}
		mDivider.setBackgroundColor(QuickButtonsBottomView.mToggleDivColor);

	}

	@Override
	void toggleOn() {

	    Intent i = new Intent();
	    i.setAction("android.intent.action.CLOSE_SYSTEM_DIALOGS");
	    getContext().sendBroadcast(i);
	    i.setAction("android.settings.TOGGLES");
        i.setFlags(i.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(i);

	}


	@Override
	void toggleOff() {
	}

	@Override
	void showSettings() {
	}


}  //
