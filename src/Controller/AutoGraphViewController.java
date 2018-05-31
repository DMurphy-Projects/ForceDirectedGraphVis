package Controller;

import java.awt.Point;

import Controller.UserController.IUserController;
import Model.GraphPackage;
import View.GraphView;
import View.PanZoomHelper;

public class AutoGraphViewController implements IUserController{

    private float mFactor = 1;

    private boolean mOn = false;

    public void update(GraphView view) {
	if (mOn) {
	    manipulateView(view);
	}
    }

    private void manipulateView(GraphView view) {
	PanZoomHelper helper = view.getHelper();

	Point mCenter = view.getGraph().findCenter();
	// System.out.println(mCenter);
	helper.setHorozontal(-mCenter.x);
	helper.setVertical(-mCenter.y);

	float width = view.getGraph().findWidth();
	float height = view.getGraph().findHeight();

	if (width == 0 || height == 0)
	    return;

	float widthFactor = view.getWidth() / width;
	float heightFactor = view.getHeight() / height;

	mFactor = widthFactor;
	if (heightFactor < widthFactor) {
	    mFactor = heightFactor;
	}
	mFactor *= 0.5f;

	helper.setZoom(mFactor);
    }

    public void forceCenter(GraphView view) {
	manipulateView(view);
    }

    public void setState(boolean on) {
	mOn = on;
    }

    @Override
    public void update(GraphPackage gPack) {
	update(gPack.getView());	
    }
}
