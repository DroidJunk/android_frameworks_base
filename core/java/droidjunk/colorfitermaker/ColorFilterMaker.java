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
    public static ColorFilter changeColor(int newColor )
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

    private static void changeExistingColor(ColorMatrix cm, int currentColor, int newColor) {
        
    	float A = (float)Color.alpha(newColor) / 255;
        float R = (float)Color.red(newColor) / 255;
        float G = (float)Color.green(newColor) / 255;
        float B =  (float)Color.blue(newColor) / 255;
        
    	float cA = (float)Color.alpha(currentColor) / 255;
        float cR = 0.212671f * (float)Color.red(currentColor) / 255;
        float cG = 0.715160f * (float)Color.green(currentColor) / 255;
        float cB = 0.072169f * (float)Color.blue(currentColor) / 255;
        
        
        float[] matrix = new float[]
/*            {       
            R/255f		,cG/255f	,cB/255f	,0			,0    // Red
            ,cR/255f	,G/255f		,cB/255f	,0			,0    // Green
            ,cR/255f	,cG/255f	,B/255f		,0			,0    // Blue
            ,0    		,0     		,0      	,A/255f 	,0f   // Alpha
            };*/
 
        {       
         R			,cG			,cB			,0			,0    // Red
        ,cR			,G			,cB			,0			,0    // Green
        ,cR			,cG			,B			,0			,0    // Blue
        ,0    		,0     		,0      	,A			,0f   // Alpha
        };        
        
        cm.postConcat(new ColorMatrix(matrix));
    }
    
    
    
    public static void changeHue(ColorMatrix cm, float newColor, int currentColor)
    {
 
        float cosVal = (float) Math.cos(newColor);
        float sinVal = (float) Math.sin(newColor);
        float lumR = 0.212671f;
        float lumG = 0.715160f;
        float lumB = 0.072169f;
        
        lumR = lumR * (float)Color.alpha(currentColor)/255;
        lumG = lumG * (float)Color.alpha(currentColor)/255;
        lumB = lumB * (float)Color.alpha(currentColor)/255;
        
/*    	float lumA = (float)Color.alpha(currentColor)/255;
        float lumR = (float)Color.red(currentColor)/255;
        float lumG = (float)Color.green(currentColor)/255;
        float lumB =  (float)Color.blue(currentColor)/255;*/
        
        
        
        
        float[] mat = new float[]
        { 
                lumR + cosVal * (1 - lumR) + sinVal * (-lumR), lumG + cosVal * (-lumG) + sinVal * (-lumG), lumB + cosVal * (-lumB) + sinVal * (1 - lumB), 0, 0, 
                lumR + cosVal * (-lumR) + sinVal * (0.143f), lumG + cosVal * (1 - lumG) + sinVal * (0.140f), lumB + cosVal * (-lumB) + sinVal * (-0.283f), 0, 0,
                lumR + cosVal * (-lumR) + sinVal * (-(1 - lumR)), lumG + cosVal * (-lumG) + sinVal * (lumG), lumB + cosVal * (1 - lumB) + sinVal * (lumB), 0, 0, 
                0f, 0f, 0f, 1f, 0f, 
                0f, 0f, 0f, 0f, 1f };
        cm.postConcat(new ColorMatrix(mat));
    }

    
    
    
    
    }  