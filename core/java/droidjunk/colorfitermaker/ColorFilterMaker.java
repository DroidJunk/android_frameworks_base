package droidjunk.colorfitermaker;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;

    public class ColorFilterMaker
    {
        /**
     * Makes a ColorFilter
     * 
     * @param newColor new color of image when filter is applied.
     * @return
     */    	
    public static ColorFilter adjustHue(int newColor )
    {
        ColorMatrix cm = new ColorMatrix();

        adjustHue(cm, newColor);

        return new ColorMatrixColorFilter(cm);
    }
  

    private static void adjustHue(ColorMatrix cm, int newColor) {
        
    	float A = (float)Color.alpha(newColor);
        float R = (float)Color.red(newColor);
        float G = (float)Color.green(newColor);
        float B =  (float)Color.blue(newColor);
        
        float[] matrix = new float[]
            {       
            R/255f		,0		,0		,0		,0    // Red
            ,0			,G/255f	,0      ,0		,0    // Green
            ,0   		,0		,B/255f	,0		,0    // Blue
            ,0    		,0      ,0      ,A/255f 	,0f   // Alpha
            };
        
        
        cm.postConcat(new ColorMatrix(matrix));
    }


    
    }  