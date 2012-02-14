package com.android.systemui.tranqtoggles;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;



public class TranqToggleViewTop extends LinearLayout {

	private final String Tranq_Settings = "TRANQ_SETTINGS";
	private SharedPreferences mPrefs;
	public static boolean mTogglesOn = true;
	public static boolean mTogglesTop = true;
	private int mToggleColor = 0xff3b3b3b;
	public static int mToggleIconOnColor = 0xff33b5e5;
	public static int mToggleIconInterColor = 0xffff0000;
	public static int mToggleIconOffColor = 0xff5d5d5d;
    private boolean mShowToggleInd = true;
    public static int mToggleIndOnColor = 0xffffbb33;
    public static int mToggleIndOffColor = 0xffba7b00;
    private int mOrigToggleIndOnColor = 0xffffbb33;
    private int mOrigToggleIndOffColor = 0xffffbb33;
    private boolean mShowToggleText = true;
    public static int mToggleTextOnColor = 0xffffbb33;
    public static int mToggleTextOffColor = 0xffba7b00;   
    private int mOrigToggleTextOnColor = 0xffffbb33;
    private int mOrigToggleTextOffColor = 0xffba7b00;   
    private boolean mShowToggleDiv = true;
    public static int mToggleDivColor = 0xff535252;   
    private int mOrigToggleDivColor = 0xffba7b00;   
    


	public TranqToggleViewTop(Context context, AttributeSet attrs) {
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

		mTogglesOn = mPrefs.getBoolean("toggles_show_toggles", true);
		mTogglesTop = mPrefs.getBoolean("toggles_top", true);
   		mToggleColor = mPrefs.getInt("toggle_color", 0xff3b3b3b);
   		mToggleIconOnColor = mPrefs.getInt("toggle_color", 0xff33b5e5);
   		mToggleIconInterColor = mPrefs.getInt("toggle_color", 0xffff0000);
   		mToggleIconOffColor = mPrefs.getInt("toggle_color", 0xff5d5d5d);
   		mShowToggleInd = mPrefs.getBoolean("toggle_show_indicator", true);
   		mToggleIndOnColor = mPrefs.getInt("toggle_ind_on_color", 0xffffbb33);
   		mToggleIndOffColor = mPrefs.getInt("toggle_ind_off_color", 0xffba7b00);
   		mShowToggleText = mPrefs.getBoolean("toggle_show_text", true);
   		mToggleTextOnColor = mPrefs.getInt("toggle_text_on_color", 0xffffbb33);
   		mToggleTextOffColor = mPrefs.getInt("toggle_text_off_color", 0xffba7b00);
   		mShowToggleDiv = mPrefs.getBoolean("toggle_show_divider", true);
   		mToggleDivColor = mPrefs.getInt("toggle_divider_color", 0xff535252);
   		
   		
   		if (!mShowToggleInd) {
   			mToggleIndOnColor = 0;
   			mToggleIndOffColor = 0;
   		}
   		
   		if (!mShowToggleText) {
   			mToggleTextOnColor = 0;
   			mToggleTextOffColor = 0;
   		}
   		
   		if (!mShowToggleDiv) {
   			mToggleDivColor = 0;
   		}
   		
   		setBackgroundColor(mToggleColor);
   		
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
             	
            	mTogglesOn = intent.getBooleanExtra("TogglesOn", mTogglesOn);	
            	mTogglesTop = intent.getBooleanExtra("TogglesTop", mTogglesTop);
            	mToggleColor = intent.getIntExtra("ToggleColor", mToggleColor);	
            	mToggleIconOnColor = intent.getIntExtra("ToggleIconOnColor", mToggleIconOnColor);	
            	mToggleIconInterColor = intent.getIntExtra("ToggleIconInterColor", mToggleIconInterColor);	
            	mToggleIconOffColor = intent.getIntExtra("ToggleIconOffColor", mToggleIconOffColor);	
				mShowToggleInd = intent.getBooleanExtra("ToggleShowIndicator", mShowToggleInd);
            	mToggleIndOnColor = intent.getIntExtra("ToggleIndOnColor", mToggleIndOnColor);
            	mToggleIndOffColor = intent.getIntExtra("ToggleIndOffColor", mToggleIndOffColor);
				mShowToggleText = intent.getBooleanExtra("ToggleShowText", mShowToggleText);
            	mToggleTextOnColor = intent.getIntExtra("ToggleTextOnColor", mToggleTextOnColor);
            	mToggleTextOffColor = intent.getIntExtra("ToggleTextOffColor", mToggleTextOffColor);
            	mShowToggleDiv = intent.getBooleanExtra("ToggleShowDivider", mShowToggleDiv);
            	mToggleDivColor = intent.getIntExtra("ToggleDividerColor", mToggleDivColor);

            	setBackgroundColor(mToggleColor);
            	updateView();

            }
        }    
    };
       
 
    private void updateView(){

    	if (!mTogglesOn) {
    		setVisibility(View.GONE);
    	} else if (mTogglesTop) {
    		setVisibility(View.VISIBLE);
    		} else {
    			setVisibility(View.GONE);
    		}
    }
}
