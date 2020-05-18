package com.android.brightfuture.timer1;


import java.util.Timer;
import java.util.TimerTask;

import com.android.brightfuture.timer1.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class Timer1Activity extends Activity implements OnClickListener
{
	private static final String TAG = "ColourApp1AppActivity";
	
	private Button iStartButton = null;
	private Button iStopButton = null;
	
	private static View iQuadrant1 = null;
	private static View iQuadrant2 = null;
	private static View iQuadrant3 = null;
	private static View iQuadrant4 = null;
	
	private Timer iTimer = null;
	private int iTimerInterval = 500; // milliseconds
	
	private static int iStartQuadrant = 0;
	
	
	@Override
    public void onCreate(Bundle aSavedInstanceState)
	{
        super.onCreate(aSavedInstanceState);
        
        Log.i (TAG, "onCreate ()");
        setContentView(R.layout.main);
        
           
        // get references to UI controls
        iStartButton = (Button)findViewById(R.id.ButtonStart);
        iStopButton = (Button)findViewById(R.id.ButtonStop);
        iQuadrant1 = (View)findViewById(R.id.quadrant1);
        iQuadrant2 = (View)findViewById(R.id.quadrant2);
        iQuadrant3 = (View)findViewById(R.id.quadrant3);
        iQuadrant4 = (View)findViewById(R.id.quadrant4);
        
        // hook buttons up to their listeners
        iStartButton.setOnClickListener(this);
        iStopButton.setOnClickListener(this);
     
        resetQuadrants (); // set them all to black!
      
        setButtonStates (true, false);
    }

	// quadrant accessors
	public static void setStartQuadrant (final int aStartQuadrant)
	{
		iStartQuadrant = aStartQuadrant;
	}
	
	public static int getStartQuadrant ()
	{
		return (iStartQuadrant);
	}
	
	// virtual methods from onClickListener
	@Override
	public void onClick(View aView) 
	{
		Log.i (TAG, "onClick () called");
		
		int id = aView.getId(); // determine what has been clicked
		switch (id)
		{
			case R.id.ButtonStart:
				setButtonStates (false, true);
				
				// define the timer
		        iTimer = new Timer(); // create a new one
		        TimerTask tickHandler = new BFTimer(Timer1Activity.this); // create a timer dervied handler
		        iTimer.scheduleAtFixedRate(tickHandler, iTimerInterval, iTimerInterval); // prep it
				break;
			case R.id.ButtonStop:
				setButtonStates (true, false);
			
				// stop the timer and purge any pending events
				iTimer.cancel();
				iTimer.purge();
				
				setStartQuadrant(0);
				resetQuadrants ();
				break;
			default:
				Log.e (TAG, "Unknown object has been clicked - " + id);
		}
		
	}
	
	// private methods
	
	private void resetQuadrants ()
	{
		Log.i (TAG, "resetQuadrants ()");
		setQuadrantColours(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK);
	}
	
	private static void setQuadrantColours (final int aQuadrant1Colour, final int aQuadrant2Colour,
											final int aQuadrant3Colour, final int aQuadrant4Colour
										   )
	{
		Log.i (TAG, "setQuadrantColours () : (Q1,Q2,Q3,Q4)=(" + aQuadrant1Colour+","+aQuadrant2Colour+","+aQuadrant3Colour+","+aQuadrant4Colour+")");
		iQuadrant1.setBackgroundColor(aQuadrant1Colour);
		iQuadrant2.setBackgroundColor(aQuadrant2Colour);
		iQuadrant3.setBackgroundColor(aQuadrant3Colour);
		iQuadrant4.setBackgroundColor(aQuadrant4Colour);
	}
	
	private void setButtonStates (final boolean aStartState, final boolean aStopState)
	{
		Log.i (TAG, "setButton States () : (start,stop) = ("+aStartState+","+aStopState+")" );
		iStartButton.setEnabled(aStartState);
		iStopButton.setEnabled(aStopState);
	}
	
	public static void setQuadrantColours ()
	{
		int iFirstQuadrant = getStartQuadrant ();
		Log.i (TAG, "setQuadrantColours ()" + iFirstQuadrant);
		
		switch (iFirstQuadrant)
		{
			case 0:
				setQuadrantColours(Color.RED, Color.YELLOW, Color.BLUE, Color.GREEN);
				break;
			case 1:
				setQuadrantColours(Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW);
				break;
			case 2:
				setQuadrantColours(Color.GREEN, Color.BLUE, Color.YELLOW, Color.RED);
				break;
			case 3:
				setQuadrantColours(Color.YELLOW, Color.GREEN, Color.RED, Color.BLUE);
				break;
			default:
				Log.i (TAG, "Unknown quadrant - " + iFirstQuadrant);
		}
	}
}

// end of file
