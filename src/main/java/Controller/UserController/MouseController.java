package Controller.UserController;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import Controller.DistanceCalculator;
import Model.GraphPackage;
import Model.Force.ForceNode;
import Model.Force.ForceVector;
import View.GraphView;
import View.PanZoomHelper;

public class MouseController implements MouseMotionListener,
	MouseWheelListener, MouseListener, IUserController {

    private DistanceCalculator mDistaceCalc;

    private Point mLastPoint;
    private Point mCurrentPoint;

    private int mSensitivity = 7;
    private float mZoomSensitivity = 0.1f;
    private int mTolerance = 1;

    private ForceNode mDraggedNode;

    private GraphView mView;

    private boolean mOn = true;
    
    public MouseController() {
	mDistaceCalc = new DistanceCalculator();

	mLastPoint = new Point(0, 0);
	mCurrentPoint = new Point(0, 0);
    }

    public void removeView() {
	mView.removeMouseMotionListener(this);
	mView.removeMouseWheelListener(this);
	mView.removeMouseListener(this);
    }

    public void changeView(GraphView view) {
	if (mView != null) {
	    removeView();
	}
	mView = view;
	mView.addMouseMotionListener(this);
	mView.addMouseWheelListener(this);
	mView.addMouseListener(this);
    }

    @Override
    public void mouseDragged(MouseEvent arg0) {
	if (!mOn) {
	    return;
	}
	mLastPoint.x = mCurrentPoint.x;
	mLastPoint.y = mCurrentPoint.y;

	mCurrentPoint = arg0.getPoint();

	if (mDraggedNode == null) {
	    pan();
	}
    }

    public void update() {
	if (mDraggedNode != null) {
	    dragNode(mDraggedNode);
	}
    }

    public void setState(boolean on) {
	mOn = on;
    }

    private void dragNode(ForceNode fNode) {
	if (!mOn) {
	    return;
	}
	PanZoomHelper helper = mView.getHelper();
	Point newPos = helper.reversePoint(mCurrentPoint);
	Point node = fNode.getPoint();
	double dir = Math.atan2(newPos.y - node.y, newPos.x - node.x);
	fNode.addForceVector(new ForceVector(dir, mDistaceCalc.distanceNoRoot(
		fNode.getPoint(), newPos)));
    }

    private void pan() {
	if (!mOn) {
	    return;
	}
	int xDir = mLastPoint.x - mCurrentPoint.x;
	int yDir = mLastPoint.y - mCurrentPoint.y;

	PanZoomHelper helper = mView.getHelper();

	if (xDir > mTolerance) {
	    helper.subHorozontalOffset(mSensitivity);
	} else if (xDir < -mTolerance) {
	    helper.addHorozontalOffset(mSensitivity);
	}

	if (yDir > mTolerance) {
	    helper.subVerticalOffset(mSensitivity);
	} else if (yDir < -mTolerance) {
	    helper.addVerticalOffset(mSensitivity);
	}
    }

    @Override
    public void mouseMoved(MouseEvent arg0) {
	mLastPoint.x = mCurrentPoint.x;
	mLastPoint.y = mCurrentPoint.y;

	mCurrentPoint = arg0.getPoint();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent arg0) {
	if (!mOn) {
	    return;
	}
	int notches = arg0.getWheelRotation();
	PanZoomHelper helper = mView.getHelper();

	// up
	if (notches < 0) {
	    helper.addZoom(mZoomSensitivity);
	} else {
	    helper.subZoom(mZoomSensitivity);
	}
    }

    @Override
    public void mouseClicked(MouseEvent arg0) {

    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
	// TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent arg0) {
	mDraggedNode = null;
    }

    @Override
    public void mousePressed(MouseEvent arg0) {
	if (!mOn) {
	    return;
	}
	PanZoomHelper helper = mView.getHelper();
	mDraggedNode = mView.getGraph().findNode(
		helper.reversePoint(mCurrentPoint), 10);
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
	mDraggedNode = null;
    }

    @Override
    public void update(GraphPackage gPack) {
	GraphView view = gPack.getView();
	if (view != mView)
	{
	    changeView(view);
	}
	update();
    }
}
