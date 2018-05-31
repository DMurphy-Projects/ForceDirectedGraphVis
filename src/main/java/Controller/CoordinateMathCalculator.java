package Controller;

import java.awt.geom.Point2D;

import Model.Force.ForceVector;

public class CoordinateMathCalculator {

    public Point2D.Float findCoordinates(ForceVector aV, float x, float y,
	    int mass, float time) {
	// Velocity(m/s) = initialVelocity(m/s) + acceleration(m/s^2)*time(s)
	// Acceleration(m/s^2) = force(n) / mass(kg)
	// Distance(m) = Velocity(m/s) * time(s)
	// initial velocity not needed as previous resultant force vector will
	// be carried on to the next iteration, meaning that continual force in
	// same direction will result in more force being applied each time thus
	// moving
	// farther than the last

	float force = aV.getForceValue();

	//System.out.println(force + " : " + time);
	
	float acceleration = force / mass;
	float velocity = acceleration * time;
	float distance = velocity * time;

	// System.out.println("Distance to move " + distance);
	float xD = (float) (distance * Math.cos(aV.getDirection()));
	float yD = (float) (distance * Math.sin(aV.getDirection()));
	// 0.02 * time
	// System.out.println((xxD + yyD)<(0.02*time));
	x += xD;
	y += yD;

	// x = (float) (Math.ceil(x * 1000) / 1000);
	// y = (float) (Math.ceil(y * 1000) / 1000);

	/*String debug = "";
	System.out.println("X : " + x + " Y : " + y);
	debug += "Force(" + aV.getForceValue() + "), Mass(" + mass + ") = Acc("
		+ acceleration + ")";
	debug += "Time(" + time + ") = Vel(" + velocity + ")";
	debug += "Distance(" + distance + ")";
	debug += "UnRounded(X:" + x + ", Y:" + y + ")";
	debug += "Rounded(X:" + x + ", Y:" + y + ")";
	System.out.println(debug);
	*/
	return new Point2D.Float(x, y);
    }
}
