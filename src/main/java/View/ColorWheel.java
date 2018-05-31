package View;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RadialGradientPaint;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JComponent;

public class ColorWheel extends JComponent {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private BufferedImage mColorWheel;

    private int mCursorX = 0;
    private int mCursorY = 0;

    private Color mSelected = Color.white;

    public void setSelected(Color c)
    {
	mSelected = c;
	this.repaint();
    }
    
    public Color onClick(int x, int y) {
	try {
	    int clr = mColorWheel.getRGB(x, y);
	    int red = (clr & 0x00ff0000) >> 16;
	    int green = (clr & 0x0000ff00) >> 8;
	    int blue = clr & 0x000000ff;
	    // System.out.println(red + " "+green+" "+blue);
	    mCursorX = x;
	    mCursorY = y;
	    // System.out.println(x+" "+y);
	    this.repaint();
	    mSelected = new Color(red, green, blue);
	    return mSelected;
	} catch (ArrayIndexOutOfBoundsException e) {
	    return null;
	}
    }

    public void createColorWheel() {
	mColorWheel = new BufferedImage(getWidth(), getHeight(),
		BufferedImage.TYPE_INT_RGB);
	ArrayList<Polygon> segments = new ArrayList<Polygon>();

	Graphics2D gx = mColorWheel.createGraphics();

	int distance = (getWidth()+getHeight())/4;
	int startX = getWidth()/2;
	int startY = getHeight()/2;

	float counter = 0;
	int numberOfSegments = 360;
	int overlap = 0;

	Polygon segment = new Polygon();
	double angle;
	for (int i = 0; i <= 360; i+=1) {
	    if (counter >= ((360f / numberOfSegments) + overlap)) {
		counter = 0;
		angle = Math.toRadians(i + 1);
		segment.addPoint((int) (startX + Math.cos(angle) * distance),
			(int) (startY + Math.sin(angle) * distance));
		segment.addPoint(startX, startY);
		segments.add(segment);
		segment = new Polygon();
		i -= overlap;
	    }

	    angle = Math.toRadians(i);
	    double x = startX + Math.cos(angle) * distance;
	    double y = startY + Math.sin(angle) * distance;
	    segment.addPoint((int) x, (int) y);
	    counter+=1;
	}
	if (segments.size() < numberOfSegments) {
	    segment.addPoint(startX, startY);
	    segments.add(segment);
	}
	gx.setColor(Color.black);
	gx.fillRect(0, 0, getWidth(), getHeight());

	RadialGradientPaint paint;

	Color curColor;
	Color transparent;
	float color = 0;
	double hPosX;
	double hPosY;
	float size = (360f / numberOfSegments);
	// System.out.println(size);
	angle = Math.toRadians(size / 2);

	// System.out.println(segments.size());
	for (Polygon p : segments) {

	    hPosX = startX + (Math.cos(angle) * distance);
	    hPosY = startY + (Math.sin(angle) * distance);
	    angle += Math.toRadians(size);

	    // gx.setColor(Color.black);
	    // gx.drawOval((int)hPosX - 10, (int)hPosY - 10, 20, 20);

	    curColor = Color.getHSBColor(color, 1f, 1f);
	    transparent = Color.getHSBColor(color, 0f, 1f);
	    paint = new RadialGradientPaint(new Point2D.Double(hPosX, hPosY),
		    distance, new float[] { 0f, 1f }, new Color[] { curColor,
			    transparent });

	    gx.setPaint(paint);
	    // gx.setColor(curColor);
	    gx.fillPolygon(p);
	    color += 1f / numberOfSegments;
	}
    }

    @Override
    protected void paintComponent(Graphics gx) {
	super.paintComponent(gx);

	if (mColorWheel == null) {
	    createColorWheel();
	}

	gx.drawImage(mColorWheel, 0, 0, mColorWheel.getWidth(),
		mColorWheel.getHeight(), 0, 0, mColorWheel.getWidth(),
		mColorWheel.getWidth(), null);

	gx.setColor(Color.black);
	gx.drawOval(mCursorX-5, mCursorY-5, 10, 10);
	gx.setColor(Color.white);
	gx.drawRect(0, 0, 11, 11);
	gx.setColor(mSelected);
	gx.fillRect(1, 1, 10, 10);

    }

    public void jik(Graphics gx) {
	gx.setColor(Color.white);
	gx.fillRect(0, 0, getWidth(), getHeight());

	Graphics2D gx2d = (Graphics2D) gx;

	Color clear = new Color(255, 0, 0, 0);

	AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER);
	gx2d.setComposite(ac);
	int gradRad = 200;

	int xS = 50;
	int yS = 50;

	int x;
	int y;
	int cAlpha = 255;
	float from = 0.5f;
	float to = 1f;
	int length = 200;
	RadialGradientPaint p;
	float angle;

	// primary
	x = (gradRad / 2) + xS;
	y = (gradRad / 2) + yS;
	angle = (float) Math.toRadians(360 / 3);
	x += Math.cos(angle) * length;
	y += Math.sin(angle) * length;
	Color red = new Color(255, 0, 0, cAlpha);

	p = new RadialGradientPaint(new Point2D.Double(x, y), gradRad,
		new float[] { from, to }, new Color[] { red, clear });
	gx2d.setPaint(p);
	gx2d.fillOval(xS, yS, gradRad, gradRad);

	x = (gradRad / 2) + xS;
	y = (gradRad / 2) + yS;
	angle = (float) Math.toRadians(360 / 3 * 2);
	x += Math.cos(angle) * length;
	y += Math.sin(angle) * length;
	Color blue = new Color(0, 0, 255, cAlpha);
	clear = new Color(0, 0, 255, 0);
	p = new RadialGradientPaint(new Point2D.Double(x, y), gradRad,
		new float[] { from, to }, new Color[] { blue, clear });
	gx2d.setPaint(p);
	gx2d.fillOval(xS, yS, gradRad, gradRad);

	x = (gradRad / 2) + xS;
	y = (gradRad / 2) + yS;
	angle = 0;
	x += Math.cos(angle) * length;
	y += Math.sin(angle) * length;
	Color green = new Color(0, 255, 0, cAlpha);
	clear = new Color(0, 255, 0, 0);
	p = new RadialGradientPaint(new Point2D.Double(x, y), gradRad,
		new float[] { from, to }, new Color[] { green, clear });
	gx2d.setPaint(p);
	gx2d.fillOval(xS, yS, gradRad, gradRad);
	// secondary

	x = (gradRad / 2) + xS;
	y = (gradRad / 2) + yS;
	angle = (float) Math.toRadians(360 / 6);
	x += Math.cos(angle) * length;
	y += Math.sin(angle) * length;
	Color yellow = new Color(255, 255, 0, cAlpha);
	clear = new Color(255, 255, 0, 0);
	p = new RadialGradientPaint(new Point2D.Double(x, y), gradRad,
		new float[] { from, to }, new Color[] { yellow, clear });
	gx2d.setPaint(p);
	gx2d.fillOval(xS, yS, gradRad, gradRad);

	x = (gradRad / 2) + xS;
	y = (gradRad / 2) + yS;
	angle = (float) Math.toRadians(360 / 6 * 3);

	x += Math.cos(angle) * length;
	y += Math.sin(angle) * length;

	Color purple = new Color(255, 0, 255, cAlpha);
	clear = new Color(255, 0, 255, 0);
	p = new RadialGradientPaint(new Point2D.Double(x, y), gradRad,
		new float[] { from, to }, new Color[] { purple, clear });
	gx2d.setPaint(p);
	gx2d.fillOval(xS, yS, gradRad, gradRad);

	x = (gradRad / 2) + xS;
	y = (gradRad / 2) + yS;
	angle = (float) Math.toRadians(360 / 6 * 5);

	x += Math.cos(angle) * length;
	y += Math.sin(angle) * length;

	Color cyan = new Color(0, 255, 255, cAlpha);
	clear = new Color(0, 255, 255, 0);
	p = new RadialGradientPaint(new Point2D.Double(x, y), gradRad,
		new float[] { from, to }, new Color[] { cyan, clear });
	gx2d.setPaint(p);
	gx2d.fillOval(xS, yS, gradRad, gradRad);
    }

    public Dimension getPreferredSize() {
	return new Dimension(200, 200);
    }

    public Dimension getMinimumSize() {
	return getPreferredSize();
    }
}
