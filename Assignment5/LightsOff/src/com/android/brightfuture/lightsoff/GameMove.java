package com.android.brightfuture.lightsoff;

public class GameMove
{
	private int iX;
	private int iY;
	
	public GameMove (final int aX, final int aY)
	{
		iX = aX;
		iY = aY;
	}
	
	public int getX()
	{
		return (iX);
	}
	
	public int getY()
	{
		return (iY);
	}
	
	public void setX(final int aX)
	{
		iX = aX;
	}
	
	public void setY(final int aY)
	{
		iY = aY;
	}
}