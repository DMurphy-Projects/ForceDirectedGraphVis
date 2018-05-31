package View;

import java.awt.Point;

public class PanZoomHelper {

	private float mZoom = 1f;

	private int mXOffset = 0;

	private int mYOffset = 0;
	
	private int mWindowOffsetX = 0;
	
	private int mWindowOffsetY = 0;
	
	private int mWindowWidth = 0;

	private int mWindowHeight = 0;
	
	public float Zoom()
	{
		return mZoom;
	}
	public int WindowWidth()
	{
		return mWindowWidth;
	}
	public int WindowHeight()
	{
		return mWindowHeight;
	}
	public int XOffset()
	{
		return mXOffset;
	}
	public int YOffset()
	{
		return mYOffset;
	}
	
	private int lowerLimit(int value) {
		if (value < 1) {
			value = 1;
		}
		return value;
	}
	
	public void addHorozontalOffset(int i)
	{
		mXOffset += lowerLimit((int) (i/mZoom));
	}
	public void subHorozontalOffset(int i)
	{
		mXOffset -= lowerLimit((int) (i/mZoom));
	}
	public void horozontalPanNoZoom(int i)
	{
		mXOffset += i;
	}
	public void setHorozontal(int i)
	{
		mXOffset = i;
	}
	
	public void addVerticalOffset(int i)
	{
		mYOffset += lowerLimit((int) (i/mZoom));
	}
	public void subVerticalOffset(int i)
	{
		mYOffset -= lowerLimit((int) (i/mZoom));
	}
	public void vericalPanNoZoom(int i)
	{
		mYOffset += i;
	}
	public void setVertical(int i)
	{
		mYOffset = i;
	}
	
	public void addZoom(float perc)
	{
		mZoom += (mZoom * perc);
	}
	public void subZoom(float perc)
	{
		mZoom -= (mZoom * perc);
	}
	public void multiplyZoom(float factor)
	{
		mZoom *= factor;
	}
	public void setZoom(float zoom)
	{
		mZoom = zoom;
	}
	
	public void setWindowSize(int width, int height)
	{
		mWindowOffsetX = width/2;
		mWindowOffsetY = height/2;
		mWindowWidth = width;
		mWindowHeight = height;
	}
	
	public int adjustX(float point) {
		point += mXOffset;
		point *= mZoom;
		point += mWindowOffsetX;
		return (int) point;
	}
	public int adjustY(float point) {
		point += mYOffset;
		point *= mZoom;
		point += mWindowOffsetY;
		return (int) point;
	}
	public int reverseAdjustX(float point)
	{
		point -= mWindowOffsetX;
		point /= mZoom;
		point -= mXOffset;
		return (int) point;
	}
	public int reverseAdjustY(float point)
	{
		point -= mWindowOffsetY;
		point /= mZoom;
		point -= mYOffset;
		return (int) point;
	}
	public Point adjustPoint(Point p)
	{
		return new Point(adjustX(p.x), adjustY(p.y));
	}
	public Point reversePoint(Point p)
	{
		return new Point(reverseAdjustX(p.x), reverseAdjustY(p.y));
	}
}
