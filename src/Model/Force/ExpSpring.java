package Model.Force;

import java.awt.Point;

public class ExpSpring extends Spring {

    public ExpSpring(int len) {
	super(len);
    }

    @Override
    public ForceVector getForceOf(Point n1, Point n2) {
	double distanceBetween = Math.pow(n1.x - n2.x, 2)
		+ Math.pow(n1.y - n2.y, 2);

	float angle = getDirection(n1, n2, distanceBetween);

	double differenceFactor = getDifferenceFactor(distanceBetween, mSpringLength);
	// System.out.println(differenceFactor);
	double force = mForce * (differenceFactor * differenceFactor);
	// System.out.println(force);

	// System.out.println(differenceFactor);
	return getForceVector(distanceBetween, mSpringLength, angle, force);
    }

}
