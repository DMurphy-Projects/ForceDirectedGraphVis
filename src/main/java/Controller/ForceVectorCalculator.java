package Controller;

import Model.Force.ForceVector;

public class ForceVectorCalculator {

    public ForceVector getResultantVector(ForceVector[] forces) {
	float sumOfXComponent = 0;
	float sumOfYComponent = 0;

	for (ForceVector fV : forces) {
	    sumOfXComponent += fV.getForceValue() * fV.getXComp();
	    sumOfYComponent += fV.getForceValue() * fV.getYComp();
	}

	float resultantDirection = (float) FastMath.atan2(sumOfYComponent,
		sumOfXComponent);

	float resultantForce;
	// reuse of variables - this point on sumX/sumY == sumX^2/sumY^2
	sumOfXComponent = sumOfXComponent * sumOfXComponent;
	sumOfYComponent = sumOfYComponent * sumOfYComponent;

	// resultant force == ((sumX^2)+(sumY^2))^1/2(sqrt)

	resultantForce = sumOfXComponent + sumOfYComponent;
	// System.out.println("N: "+resultantForce);
	// resultantForce = (float) Math.sqrt(resultantForce);
	resultantForce = FastMath.sqrt(resultantForce);
	// System.out.println("IR: "+resultantForce);

	// rounding to remove E-x - make readable
	//resultantForce = Math.round(resultantForce);
	
	//System.out.println("Result Force " + resultantForce);
	// System.out.println("Result Direction " +
	// Math.round(Math.toDegrees(resultantDirection)));
	return new ForceVector(resultantDirection, resultantForce);
    }
}
