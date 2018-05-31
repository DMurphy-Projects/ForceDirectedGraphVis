package Model.Task;

import java.awt.geom.Point2D.Float;

import Controller.CoordinateMathCalculator;
import Controller.ForceVectorCalculator;
import Model.Force.ForceNode;
import Model.Force.ForceVector;

public class ResultantTask extends Thread {
    private ForceNode[] mFNodes;

    private float mRate;

    private int mStart;
    private int mEnd;

    private CoordinateMathCalculator cCalc;
    private ForceVectorCalculator fCalc;

    public ResultantTask(ForceNode[] fNodes, CoordinateMathCalculator c,
	    ForceVectorCalculator fc, float rate, int start,
	    int end) {
	mFNodes = fNodes;
	mRate = rate;
	mStart = start;
	mEnd = end;

	cCalc = c;
	fCalc = fc;
    }

    @Override
    public void run() {
	for (int i = mStart; i < mEnd; i++) {
	    // System.out.println(i);
	    // System.out.println(fNode.NodeInfo().getId());

	    Float node = mFNodes[i].getPosition();
	    ForceVector vec = mFNodes[i].getFinalVector(fCalc);
	    // find cords
	    Float p = cCalc.findCoordinates(vec,
		    node.x, node.y, mFNodes[i].Mass(), mRate);
	    
	    mFNodes[i].setX(p.x);
	    mFNodes[i].setY(p.y);
	}
    }
}
