package Controller.UserController;

import Model.GraphPackage;
import Model.IScaler;

public class KneelingCalc implements IUserController {

    private float mFactor = 0;

    long startTime = 0;

    Float[] mValues = new Float[] { 0f, 10f, 30f, 60f, 75f, 90f, 150f };
    int place = 1;
    float tol = 0.75f;
    float tol2 = 0.2f;

    private Boolean mOn = false;

    public void setKneel(float dist) {
	// System.out.println("Dist " + dist);
	mFactor = ((mFactor * 0.98f) + (dist * 0.02f));
	mFactor = (int) mFactor;
    }

    public float getKneelValue() {
	if (place > 0)
	    if (mFactor < (mValues[place] - (tol2 * mValues[place - 1]))) {
		place--;
		// System.out.println("Value : " + mFactor + " Speed : " +
		// mValues[place]);
		return mValues[place];
	    }
	if (place < mValues.length - 1)
	    if (mFactor > (mValues[place] + (tol * mValues[place + 1]))) {
		place++;
		// System.out.println("Value : " + mFactor + " Speed : " +
		// mValues[place]);
		return mValues[place];
	    }
	// System.out.println("Value : " + mFactor + " Speed : " +
	// mValues[place]);
	return mValues[place];
    }

    public float getKneelValue(int i) {

	if (place > 0)
	    if (mFactor < (mValues[place] - (tol2 * mValues[place - 1]))) {
		place--;
		System.out.println("Value : " + mFactor + " Speed : "
			+ mValues[place]);
		return mValues[place];
	    }
	if (place < mValues.length - 1)
	    if (mFactor > (mValues[place] + (tol * mValues[place + 1]))) {
		place++;
		System.out.println("Value : " + mFactor + " Speed : "
			+ mValues[place]);
		return mValues[place];
	    }
	System.out.println("Value : " + mFactor + " Speed : " + mValues[place]);
	// return mValues[place];
	if (place > 0)
	    if (mFactor < (mValues[place] - (tol * mValues[place - 1]))) {
		place--;
		System.out.println("Value : " + mFactor + " Speed : "
			+ mValues[place]);
		return mValues[place];
	    }
	if (place < mValues.length - 1)
	    if (mFactor > (mValues[place] + (tol * mValues[place + 1]))) {
		place++;
		System.out.println("Value : " + mFactor + " Speed : "
			+ mValues[place]);
		return mValues[place];
	    }
	System.out.println("Value : " + mFactor + " Speed : " + mValues[place]);
	// return mValues[place];

	if (mFactor < (150 * (1f - tol))) {
	    if (place > 0) {
		place--;
	    }
	} else if (mFactor > (150 * (1f + tol))) {
	    if (place < mValues.length - 1) {
		place++;
	    }
	}
	return mValues[place];
    }

    @Override
    public void update(GraphPackage gPack) {
	if (mOn) {
	    IScaler scale = gPack.getScaler();
	    scale.setValue(getKneelValue() / 1000);
	}
    }

    public void set(Boolean on) {
	mOn = on;
    }
}
