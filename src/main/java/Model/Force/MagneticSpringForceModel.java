package Model.Force;

import java.awt.Point;

import Controller.CoordinateMathCalculator;
import Controller.ForceVectorCalculator;
import Controller.UserController.KneelingCalc;


public class MagneticSpringForceModel extends BaseEdgeEdgeForceModel {

    private IForce mRepelForce;
    private IForce mSpringForce;

    public MagneticSpringForceModel(ForceVectorCalculator vectorCalc,
	    CoordinateMathCalculator coordCalc, KneelingCalc kc, Spring spring,
	    GenericRepel repel) {
	super(vectorCalc, coordCalc, kc);
	mSpringForce = spring;
	mRepelForce = repel;
    }

    @Override
    protected void edgeEdgeRelationship(ForceNode n1, ForceNode n2,
	    ForceNode n3, ForceNode n4) {
	Point node1 = n1.getPoint();
	Point node2 = n2.getPoint();
	Point node3 = n3.getPoint();
	Point node4 = n4.getPoint();

	ForceVector v1 = mRepelForce.getForceOf(new Point(
		(node1.x + node2.x) / 2, (node1.y + node2.y) / 2), new Point(
		(node3.x + node4.x) / 2, (node3.y + node4.y) / 2));

	ForceVector v2 = new ForceVector(v1.getDirection() - Math.PI,
		v1.getForceValue());

	n1.addForceVector(v1);
	n2.addForceVector(v1);

	n3.addForceVector(v2);
	n4.addForceVector(v2);
    }

    @Override
    protected void nodesWithRelationship(ForceNode n1, ForceNode n2) {

	ForceVector v = mSpringForce.getForceOf(n1.getPoint(), n2.getPoint());
	n1.addForceVector(v);
	n2.addForceVector(
		new ForceVector(v.getDirection() - Math.PI, v.getForceValue()));
    }

    @Override
    protected void nodesWithoutRelationship(ForceNode n1, ForceNode n2) {
	/*
	 * ForceVector v = mRepelForce.getForceOf(n1.getPosition(),
	 * n2.getPosition()); n1.addForceVector(v);
	 */

    }
}
