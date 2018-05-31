package View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.JComponent;

import Model.LoadScreenModel;

public class LoadScreenView extends JComponent {

    private static final long serialVersionUID = 1L;

    private LoadScreenModel mModel;

    private int offset = 0;

    public LoadScreenView(LoadScreenModel model) {
	mModel = model;
    }

    @Override
    public void paint(Graphics gx) {
	super.paint(gx);

	gx.setColor(Color.white);
	gx.fillRect(0, 0, getWidth(), getHeight());
	gx.setColor(Color.black);
	gx.drawRect(0, 0, getWidth()-1, getHeight()-1);

	int centerY = getHeight() / 4;
	int centerX = getWidth() / 2;

	int dist = 20;
	for (int i = 0; i < 360; i += 20) {
	    float angle = (float) Math.toRadians(i + offset);
	    int x = (int) (centerX + (dist * Math.cos(angle)));
	    int y = (int) (centerY + (dist * Math.sin(angle)));
	    gx.drawOval(x, y, 5, 5);
	}
	offset++;
	if (offset > 360) {
	    offset = 0;
	}
	centerY *= 2;
	FontMetrics metrics = gx.getFontMetrics();
	int hgt = metrics.getHeight();

	for (String s : mModel.getStrings()) {
	    int adv = metrics.stringWidth(s) / 2;
	    gx.drawString(s, centerX - adv, centerY += hgt);
	}
    }

    public Dimension getPreferredSize() {
	return new Dimension(100, 100);
    }

    public Dimension getMinimumSize() {
	return getPreferredSize();
    }
}
