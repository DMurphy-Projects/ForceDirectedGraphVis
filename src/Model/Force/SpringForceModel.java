package Model.Force;

import Controller.CoordinateMathCalculator;
import Controller.ForceVectorCalculator;
import Controller.UserController.KneelingCalc;


public class SpringForceModel extends BaseForceModel {

    private IForce mSpringForce;
    private IForce mRepelForce;

    public SpringForceModel(ForceVectorCalculator vectorCalc,
	    CoordinateMathCalculator coordCalc, KneelingCalc kc, Spring spring,
	    GenericRepel repel) {
	super(vectorCalc, coordCalc, kc);
	mSpringForce = spring;
	mRepelForce = repel;
    }

    @Override
    protected void nodesWithRelationship(ForceNode n1, ForceNode n2) {
	ForceVector v = mSpringForce.getForceOf(n1.getPoint(), n2.getPoint());
	n1.addForceVector(v);
	n2.addForceVector(new ForceVector(v.getDirection() - Math.PI, v
		.getForceValue()));
    }

    @Override
    protected void nodesWithoutRelationship(ForceNode n1, ForceNode n2) {
	ForceVector v = mRepelForce.getForceOf(n1.getPoint(), n2.getPoint());
	n1.addForceVector(v);
	n2.addForceVector(new ForceVector(v.getDirection() - Math.PI, v
		.getForceValue()));
    }
}
