package Model.Force;

import java.awt.Point;
import java.awt.geom.Point2D;

import Controller.CoordinateMathCalculator;
import Controller.ForceVectorCalculator;
import Controller.UserController.KneelingCalc;
import Model.Graph;
import Model.IScaler;

public abstract class BaseForceModelOld implements IForceModel {

    private ForceVectorCalculator mVectorCalc;
    private CoordinateMathCalculator mCoordCalc;

    private IScaler mRateOfSimulation;

    public BaseForceModelOld(ForceVectorCalculator vectorCalc,
	    CoordinateMathCalculator coordCalc, KneelingCalc kc) {
	mVectorCalc = vectorCalc;
	mCoordCalc = coordCalc;
    }

    @Override
    public void manipulate(Graph graph, IScaler rate) {
	// nodes with relationship
	for (ForceRelationship r : graph.getRelationships()) {
	    nodesWithRelationship(r.getRelationship().get(0),r.getRelationship().get(1));
	}

	// nodes without relationship
	for (ForceNode fn1 : graph.Nodes()) {
	    for (ForceNode fn2 : graph.Nodes()) {
		if (fn1 == fn2)
		    continue;
		nodesWithoutRelationship(fn1, fn2);
	    }
	}

	// simplify forces of each forceNode and move
	Point2D.Float p;
	ForceVector fVec;

	for (ForceNode fNode : graph.Nodes()) {
	    fVec = fNode.getFinalVector(mVectorCalc);

	    Point pos = fNode.getPoint();
	    // find cords
	    p = mCoordCalc.findCoordinates(fVec, pos.x, pos.y,
		    fNode.Mass(), rate.getValue());
	    fNode.setX(p.x);
	    fNode.setY(p.y);
	}
    }

    protected abstract void nodesWithRelationship(ForceNode n1, ForceNode n2);

    protected abstract void nodesWithoutRelationship(ForceNode n1, ForceNode n2);
}
