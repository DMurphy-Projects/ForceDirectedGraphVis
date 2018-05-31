package Controller;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Controller.UserController.IUserController;
import Model.GraphPackage;
import Model.IScaler;

public class SliderListener implements ChangeListener, IUserController {

    private float mValue = 0;

    private Boolean mOn = false;
    
    @Override
    public void stateChanged(ChangeEvent e) {
	JSlider source = (JSlider) e.getSource();
	if (!source.getValueIsAdjusting()) {
	    mValue = source.getValue();
	}
    }

    public void set(Boolean on) {
	mOn = on;
    }

    public void setValue(float v)
    {
	mValue = v;
    }
    
    @Override
    public void update(GraphPackage gPack) {
	if (mOn) {
	    IScaler scale = gPack.getScaler();
	    scale.setValue(mValue / 1000);
	}
    }
}
