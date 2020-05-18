package com.android.brightfuture.timer1;

import java.util.TimerTask;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

public class BFTimer extends TimerTask
{ 
    private Context iContext = null; 
    private Handler iHandler = new Handler(); 
 
    
     // Write Custom Constructor to pass Context 
    public BFTimer(Context aContext) 
    { 
        this.iContext = aContext; 
    } 
 
    @Override 
    public void run()
    { 
        new Thread(new Runnable() 
        { 
            @Override 
            public void run()
            { 
                iHandler.post(new Runnable()
                { 
                    @Override 
                    public void run()
                    { 
                    	// this could be combined into one statement
                    	    
                    	//tick the quadrant over - using public static accessors
                    	int quadrant = Timer1Activity.getStartQuadrant();
                    	Timer1Activity.setQuadrantColours();
                    	
                    	quadrant++;
                    	quadrant %=4;
                    	
                    	Timer1Activity.setStartQuadrant(quadrant);
                    	
                    	
                    	if (Constants.KAllowDebug)
                    	{
                    		Toast t = Toast.makeText (iContext, "Tick " + quadrant, Toast.LENGTH_SHORT);
                    		t.show ();
                    	}
                    }	 
                }); 
            } 
        }).start(); 
     } 
} 

// end of file
