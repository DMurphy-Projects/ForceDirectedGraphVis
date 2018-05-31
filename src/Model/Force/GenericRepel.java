package Model.Force;

import java.awt.Point;

public class GenericRepel extends BaseForce {

    private int mForce = 8;

    private int mRadius;

    public GenericRepel(int force) {
	mRadius = -1;
	mForce = force;
    }

    public GenericRepel(int force, int rad) {
	mRadius = (int) Math.pow(rad, 2);
	mForce = force;
    }

    @Override
    public ForceVector getForceOf(Point n1, Point n2) {
	double distanceBetween = Math.pow(n1.x - n2.x, 2)
		+ Math.pow(n1.y - n2.y, 2);

	if (distanceBetween > mRadius && mRadius != -1)
	    return new ForceVector(0, 0);

	float angle = getDirection(n1, n2, distanceBetween);

	return new ForceVector(angle, mForce);
	// n2.addForceVector(new ForceVector(angle - Math.PI, force));
    }

}
