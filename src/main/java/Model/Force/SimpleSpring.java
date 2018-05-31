package Model.Force;

import java.awt.Point;

public class SimpleSpring extends Spring {

    public SimpleSpring(int len) {
	super(len);
    }    

    @Override
    public ForceVector getForceOf(Point n1, Point n2) {
	// should be sqrt'd but....squared length instead
	double distanceBetween = Math.pow(n1.x - n2.x, 2)
		+ Math.pow(n1.y - n2.y, 2);

	float angle = getDirection(n1, n2, distanceBetween);

	ForceVector n1Vector = null;

	if (distanceBetween > mSpringLength)// too far apart
	{
	    n1Vector = new ForceVector(angle - Math.PI, mForce);

	} else if (distanceBetween < mSpringLength) {
	    n1Vector = new ForceVector(angle, mForce);
	}
	if (n1Vector != null) {
	    return n1Vector;
	}
	return new ForceVector(0, 0);
    }

}
