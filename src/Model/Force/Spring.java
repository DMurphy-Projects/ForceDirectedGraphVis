package Model.Force;

import java.awt.Point;

public class Spring extends BaseForce {

    protected int mSpringLength;

    protected int mForce = 5;

    // float mCompressionLimit = 2f;
    // private float mExtensionLimit = 1f;

    public Spring(int len) {
	mSpringLength = (int) Math.pow(len, 2);
    }

    protected double getDifferenceFactor(double dist, double basedOn)
    {
	double differenceFactor;
	if (dist < basedOn) {
	    // for shorter
	    differenceFactor = 1 - (dist / basedOn);
	    if (Double.isInfinite(differenceFactor))
		differenceFactor = mForce;
	} else {
	    // for longer
	    differenceFactor = (dist / basedOn);
	}
	return differenceFactor;
    }
    
    protected ForceVector getForceVector(double dist, double basedOn, double angle, double force)
    {
	ForceVector n1Vector = null;
	if (dist >= basedOn)// too far apart
	{
	    n1Vector = new ForceVector(angle - Math.PI, force);
	} else if (dist < basedOn) {
	    n1Vector = new ForceVector(angle, force);
	}
	if (n1Vector != null) {
	    return n1Vector;
	}
	return new ForceVector(0, 0);
    }
    
    @Override
    public ForceVector getForceOf(Point n1, Point n2) {
	// should be sqrt'd but....squared length instead
	double distanceBetween = Math.pow(n1.x - n2.x, 2)
		+ Math.pow(n1.y - n2.y, 2);

	float angle = getDirection(n1, n2, distanceBetween);
	
	// System.out.println(differenceFactor);
	double force = mForce * (getDifferenceFactor(distanceBetween, mSpringLength));
	// System.out.println(force);
	return getForceVector(distanceBetween, mSpringLength, angle, force);
    }

}
