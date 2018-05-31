package Controller;

import java.awt.Container;
import java.awt.Point;

import javax.swing.JFrame;

import View.LoadScreenView;

public class LoadScreenController {

    private JFrame mWindow;

    private LoadScreenView mView;

    private Thread mTicker;

    public LoadScreenController(LoadScreenView view, Point pos) {
	mView = view;
	mWindow = new JFrame("Loading");
	mWindow.setUndecorated(true);
	Container content = mWindow.getContentPane();
	content.add(mView);
	mWindow.pack();

	pos = new Point(pos.x - (mWindow.getWidth() / 2), pos.y
		- (mWindow.getHeight() / 2));
	mWindow.setLocation(pos);

	mWindow.setVisible(true);
	mTicker = new ControllerTick();
	mTicker.start();
    }

    public void hide() {
	try {
	    Thread.sleep(500);
	} catch (InterruptedException e) {
	}
	mTicker.interrupt();
	mWindow.setVisible(false);
    }

    class ControllerTick extends Thread {
	public void run() {
	    try {
		while (true) {
		    mView.repaint();
		    Thread.sleep(10);
		}
	    } catch (InterruptedException e) {
	    }
	}
    }
}
