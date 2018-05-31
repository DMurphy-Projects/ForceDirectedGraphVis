package Component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import Model.ColorScheme;
import Model.ReferenceColor;
import View.ColorWheel;

@SuppressWarnings("serial")
public class ColorComponent extends Panel implements MouseMotionListener,
	MouseListener, ActionListener {

    private ColorWheel mWheel;

    private Color mCurrentColor;

    private ColorScheme mColors;

    JComboBox<String> mOptions;

    public ColorComponent()
    {
	createGui();
    }
    
    public void setColorScheme(ColorScheme c) {
	mColors = c;
	mOptions.removeAllItems();
	for (String s : mColors.getColorNames()) {
	    mOptions.addItem(s);
	}
    }

    private void createGui() {
	mCurrentColor = Color.white;
	
	JPanel tab = new JPanel(new BorderLayout());
	JPanel frame = new JPanel();
	frame.setLayout(new BoxLayout(frame, BoxLayout.Y_AXIS));

	mOptions = new JComboBox<String>();
	mOptions.addActionListener(this);
	frame.add(mOptions);

	mWheel = new ColorWheel();
	frame.add(mWheel);
	mWheel.addMouseMotionListener(this);
	mWheel.addMouseListener(this);
	tab.add(frame);

	this.add(tab);
    }

    private void updateColor(MouseEvent arg0) {
	Color c = mWheel.onClick(arg0.getX(), arg0.getY());
	if (c != null) {
	    mCurrentColor = c;
	}
	if (mColors != null) {
	    ReferenceColor rc = mColors.getColor((String) mOptions
		    .getSelectedItem());
	    if (rc != null) {
		rc.setColor(mCurrentColor);
	    }
	}
    }

    @Override
    public void mouseDragged(MouseEvent arg0) {
	updateColor(arg0);
    }

    @Override
    public void mouseMoved(MouseEvent arg0) {
	// TODO Auto-generated method stub

    }

    @Override
    public void mouseClicked(MouseEvent arg0) {
	// TODO Auto-generated method stub

    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
	// TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent arg0) {
	// TODO Auto-generated method stub

    }

    @Override
    public void mousePressed(MouseEvent arg0) {
	updateColor(arg0);
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
	// TODO Auto-generated method stub

    }

    public Color getColor() {
	return mCurrentColor;
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
	String action = arg0.getActionCommand();
	switch (action) {
	case "comboBoxChanged":
	    ReferenceColor rc = mColors.getColor((String) mOptions
		    .getSelectedItem());
	    if (rc != null) {
		mWheel.setSelected(rc.getColor());
	    }
	    break;
	}
    }

}
