package Model.Force;

import java.util.ArrayList;

import Controller.CoordinateMathCalculator;
import Controller.ForceVectorCalculator;
import Controller.UserController.KneelingCalc;
import Model.Graph;
import Model.IScaler;

public abstract class BaseEdgeEdgeForceModel extends BaseForceModel {

    public BaseEdgeEdgeForceModel(ForceVectorCalculator vectorCalc,
	    CoordinateMathCalculator coordCalc, KneelingCalc kc) {
	super(vectorCalc, coordCalc, kc);
    }

    @Override
    public void manipulate(final Graph graph, IScaler rate) {
	addTask(new EdgeEdgeRelationTask(graph.getRelationships()));
	super.manipulate(graph, rate);
    }

    private class EdgeEdgeRelationTask extends Thread {
	private ForceRelationship[] mRels;

	public EdgeEdgeRelationTask(ForceRelationship[] rels) {
	    mRels = rels;
	}

	public void run() {
	    int limit = mRels.length;
	    ArrayList<ForceNode> r1;
	    ArrayList<ForceNode> r2;

	    for (int i = 0; i < limit - 1; i++) {
		for (int ii = i + 1; ii < limit; ii++) {
		    r1 = mRels[i].getRelationship();
		    r2 = mRels[ii].getRelationship();

		    edgeEdgeRelationship(r1.get(0), r1.get(1), r2.get(0),
			    r2.get(1));
		}
	    }
	}
    }

    // provides functionality so that all relationships can interact with each
    // other
    protected abstract void edgeEdgeRelationship(ForceNode n1, ForceNode n2,
	    ForceNode n3, ForceNode n4);
}
