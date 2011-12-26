package com.android.systemui.tranqtoggles;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public abstract class TranqToggleButton extends TextView {


	public TranqToggleButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		
		
		setOnClickListener(new OnClickListener() { 
			public void onClick (View v){
				
				if (getStatusOn()) {
					toggleOff();
				} else {
					toggleOn();
				}
			}
		}
		);
		
		setOnLongClickListener(new OnLongClickListener() { 
			public boolean onLongClick (View v){
				showSettings();
				return true;
			}
		}
		);
		
	}
	
	
	abstract void updateResources();
	abstract void toggleOn();
	abstract void toggleOff();
	abstract boolean getStatusOn();
	abstract void showSettings();

} //


