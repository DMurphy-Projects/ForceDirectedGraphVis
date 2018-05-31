package Model.Task;

import java.awt.geom.Point2D.Float;

import Controller.CoordinateMathCalculator;
import Controller.ForceVectorCalculator;
import Model.Force.ForceNode;

public class SingleResultantTask extends Thread {
    private ForceNode mFNode;

    private float mRate;

    private CoordinateMathCalculator cCalc;
    private ForceVectorCalculator fCalc;

    public SingleResultantTask(ForceNode fNode, CoordinateMathCalculator c,
	    ForceVectorCalculator fc, float rate) {
	mFNode = fNode;
	mRate = rate;

	cCalc = c;
	fCalc = fc;
    }

    @Override
    public void run() {

	// System.out.println(i);
	// System.out.println(fNode.NodeInfo().getId());

	Float node = mFNode.getPosition();
	// find cords
	Float p = cCalc.findCoordinates(
		mFNode.getFinalVector(fCalc), node.x, node.y,
		mFNode.Mass(), mRate);
	mFNode.setX(p.x);
	mFNode.setY(p.y);

	// System.out.println("Node "+mFNode.NodeInfo().getId());
    }
}
