package Model.Force;

import java.awt.Point;

//provides definition of a force to be used as part of a IForceModel
public interface IForce {
	//all forces can be described as a relationship between two nodes
	//should return force vector of n1, n2 force vector can be derived
	public ForceVector getForceOf(Point n1, Point n2);
}
