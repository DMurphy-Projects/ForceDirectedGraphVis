package Model.Force;

import java.awt.Point;
import java.util.Random;

import Controller.FastMath;


public abstract class BaseForce implements IForce {

    private float rAngle = 0;
    private Random r = new Random();

    protected float getDirection(Point n1, Point n2, double dist) {
	double angle;
	if (dist == 0) {
	    // System.out.println(n1 + " " + " " + n1 + " " + dist);
	    angle = Math.toRadians(r.nextInt(360));
	    // angle = (rAngle+=Math.toRadians(72));
	    // if (rAngle > Math.PI*2)rAngle = 0;
	    // System.out.println(Math.toDegrees(angle));
	} else {
	    // n1 towards n2
	    angle = FastMath.atan2(n1.y - n2.y, n1.x - n2.x);
	}
	return (float) angle;
    }

}
