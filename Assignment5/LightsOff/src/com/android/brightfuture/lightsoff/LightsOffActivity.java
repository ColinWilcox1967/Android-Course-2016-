package com.android.brightfuture.lightsoff;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;

public class LightsOffActivity extends Activity implements OnTouchListener
{
	private static final int LOW_DPI_STATUS_BAR_HEIGHT = 19; 
	private static final int MEDIUM_DPI_STATUS_BAR_HEIGHT = 25; 
	private static final int HIGH_DPI_STATUS_BAR_HEIGHT = 38; 
	
	private static final int DIFFICULTY_EASY = 0;
	private static final int DIFFICULTY_MEDIUM = 1;
	private static final int DIFFICULTY_HARD = 2;
	
	private int iGameDifficulty = DIFFICULTY_EASY;
	
	private boolean [][] iGrid = null;
	private boolean [][] iBackupGrid = null;
	private int iScreenWidth = 0;
	private int iScreenHeight = 0;
	private int iImageWidth = 0;
	private int iImageHeight = 0;
	private Bitmap iBulbOnBitmap = null;
	private Bitmap iBulbOffBitmap = null;
	private GridImageView iCanvasView = null;
	private List<GameMove> iMoveList = null;
	
	// settings for later
	private int iGridWidth = 4;
	private int iGridHeight = 4;
	private int iNumberOfBulbs = 5;
	
	private int iBulbCount = iNumberOfBulbs;
	
	private class GridImageView extends ImageView
	{
		public GridImageView (Context aContext)
		{
			super (aContext);
		}
		
		@Override
		 protected void onDraw (Canvas aCanvas)
		{
		   
		 	for (int y = 0; y < iGridHeight; y++)
		   	{
		   		for (int x = 0; x < iGridWidth; x++)
		   		{
		   			if (iGrid[x][y])
		   			{
		   				aCanvas.drawBitmap (iBulbOnBitmap, x*iImageWidth, y*iImageHeight, null);
		   			}
		   			else
		   			{
		   				aCanvas.drawBitmap(iBulbOffBitmap, x*iImageWidth, y*iImageHeight, null);
		   			}
		   		}
		    }
		 }
	}
	
    @Override
    public void onCreate(Bundle aSavedInstanceState)
    {
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    	
    	
    	// check notification bar settings 
    	//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    	
        super.onCreate(aSavedInstanceState);
        iCanvasView = new GridImageView (this);
        iCanvasView.setOnTouchListener(this);
        
        setContentView(iCanvasView);
              
        iGrid = new boolean [iGridWidth][iGridHeight];
        iBackupGrid = new boolean [iGridWidth][iGridHeight];
               
          
        Display display = getWindowManager().getDefaultDisplay();
        iScreenWidth = display.getWidth();
        iScreenHeight = display.getHeight();
        
        
        iScreenHeight -= getStatusBarHeight ();
      
        
              
        iImageWidth = iScreenWidth/iGridWidth;
        iImageHeight = iScreenHeight/iGridHeight;
        
        
        iBulbOnBitmap = Bitmap.createBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),
        												       R.drawable.bulbon),
        													   iImageWidth,
        													   iImageHeight,
        													   false
        													  ));
        iBulbOffBitmap = Bitmap.createBitmap(Bitmap.createScaledBitmap (BitmapFactory.decodeResource(getResources(),
        														R.drawable.bulboff),
        														iImageWidth,
        														iImageHeight,
        														false
        													));
        		
        
        buildLevel ();
    }
    
    private void buildLevel ()
    {
    	clearGrid ();
    	setLitBulbs (iNumberOfBulbs);
    	iCanvasView.invalidate();
    	
    }
 
    @Override
    public boolean onCreateOptionsMenu(Menu aMenu)
    {
        getMenuInflater().inflate(R.menu.activity_lights_off, aMenu);
        return true;
    }
    
    private void clearGrid ()
    {
    	for (int x = 0; x < iGridWidth; x++)
    	{
    		for (int y = 0; y < iGridHeight; y++)
    		{
    			iGrid[x][y] = false;
    			iBackupGrid[x][y] = false;
    		}
    	}
    	
    	iMoveList = new ArrayList<GameMove>();
    }
    
    private void setLitBulbs (final int aNumberOfBulbs)
    {
    	Random numberGenerator = new Random ();
    	
    	for (int bulb = 0; bulb < aNumberOfBulbs; bulb++)
    	{
    		boolean setBulb = false;
    		while (!setBulb)
    		{
    			
    			int x = numberGenerator.nextInt(iGridWidth);
    			int y = numberGenerator.nextInt(iGridHeight);
    			if (!iGrid[x][y])
    			{
    				setBulb = true;
    				iGrid[x][y] = true;
    				iBackupGrid[x][y] = true;
    			}
    		}
    	}
    }
    
    private void resetGrid ()
    {
    	for (int x = 0; x < iGridWidth; x++)
    	{
    		for (int y = 0; y < iGridHeight; y++)
    		{
    			iGrid[x][y] = iBackupGrid[x][y];
    		}
    	}
    	iCanvasView.invalidate ();
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem aItem)
	{
		int id = aItem.getItemId();
		
		switch (id)
		{
			case R.id.menu_difficulty:
				showDifficultyDialog ();
				return true;
			case R.id.menu_reset:
				resetGrid ();
				return true;
			case R.id.menu_new_grid:
				buildLevel();
				return true;
			case R.id.menu_undo:
				if (!iMoveList.isEmpty())
				{
					// get last move made.
					int count = iMoveList.size();
					
					int x = iMoveList.get(count-1).getX();
					int y = iMoveList.get(count-1).getY();
				
					//undo grid change as result of move
					flipLights(x,y);
					
					// .. and redraw
					iCanvasView.invalidate();
					
					// take it off the list
					iMoveList.remove(count-1);
				}
				return true;
		
		default:
				return false;
			
		}
		
	}
	
	private void showDifficultyDialog ()
	{
		 Dialog dlg = new AlertDialog.Builder(LightsOffActivity.this)
	         .setTitle(R.string.dialog_difficulty_title)
	         .setSingleChoiceItems(R.array.gameDifficultyArray, iGameDifficulty, new DialogInterface.OnClickListener()
	         {
	        	
	        	 
	             public void onClick(DialogInterface dialog, int whichButton)
	             {
	            	// defined to implement interface die DialogInterface only 
	               
	             }
	         })
	         .setPositiveButton(R.string.text_ok, new DialogInterface.OnClickListener()
	         {
	             public void onClick(DialogInterface dialog, int whichButton)
	             {
	            	 // get selected item from list view
	            	 ListView lv = ((AlertDialog)dialog).getListView(); 
	            	 if (lv != null)
	            	 {
	            		 iGameDifficulty = lv.getCheckedItemPosition(); 
	            	 }
	             }
	         })
	         .setNegativeButton(R.string.text_cancel, new DialogInterface.OnClickListener()
	         {
	             public void onClick(DialogInterface dialog, int whichButton) 
	             {
	
	                 /* User clicked No so do some stuff */
	             }
	         })
	        .create();
		
		 dlg.show();
		 
	}
	
	private void flipLights (final int aX, final int aY)
	{
		if (validGridCell (aX - 1, aY))
		{
			flipBulb (aX - 1, aY);
		}
		
		if (validGridCell (aX + 1, aY))
		{
			flipBulb (aX + 1, aY);
		}
		
		if (validGridCell (aX, aY - 1))
		{
			flipBulb (aX, aY - 1);
		}
		
		if (validGridCell (aX, aY + 1))
		{
			flipBulb (aX, aY + 1);
		}
		
		flipBulb (aX, aY);
	}
	
	private void flipBulb (final int aX, final int aY)
	{
		
		if (iGrid[aX][aY])
		{
			iGrid[aX][aY] = false;
			iBulbCount--;
		}
		else
		{
			iGrid[aX][aY] = true;
			iBulbCount++;
		}
	}
	
	private boolean validGridCell (final int aX, final int aY)
	{
		return ((aX >= 0) && (aX < iGridWidth) && (aY >= 0) && (aY < iGridHeight));
	}

	@Override
	public boolean onTouch(View aView, MotionEvent aEvent)
	{
		float x = aEvent.getX();
		float y = aEvent.getY();
		
		int column = (int)x/iImageWidth;
		int row = (int)y/iImageHeight;
		
		iMoveList.add(new GameMove (column, row));
		
		flipLights(column, row);
		
		iCanvasView.invalidate();
		
		return false;
	}
	
	private int getStatusBarHeight ()
	{
	
		DisplayMetrics displayMetrics = new DisplayMetrics(); 
		WindowManager wmgr = ((WindowManager) getSystemService(Context.WINDOW_SERVICE));
		wmgr.getDefaultDisplay().getMetrics(displayMetrics); 
		 
		int statusBarHeight; 
		 
		switch (displayMetrics.densityDpi)
		{ 
		    case DisplayMetrics.DENSITY_HIGH: 
		        return(HIGH_DPI_STATUS_BAR_HEIGHT); 
		       
		    case DisplayMetrics.DENSITY_MEDIUM: 
		        return(MEDIUM_DPI_STATUS_BAR_HEIGHT); 
		      
		    case DisplayMetrics.DENSITY_LOW: 
		        return(LOW_DPI_STATUS_BAR_HEIGHT); 
		      
		    default: 
		        return(MEDIUM_DPI_STATUS_BAR_HEIGHT); 
		} 
	}
	
	
}

// end of file

