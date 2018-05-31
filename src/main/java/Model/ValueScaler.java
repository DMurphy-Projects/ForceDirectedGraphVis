package Model;

public class ValueScaler implements IScaler {

    private float mValue;

    private float mStep;

    private float mLowest;

    public ValueScaler(float start, float step, float lowest) {
	mValue = start;
	mStep = step;
	mLowest = lowest;
    }

    public float getValue() {
	mValue -= mStep;
	if (mValue < mLowest) {
	    mValue = mLowest;
	}
	return mValue;
    }
    @Override
    public void setValue(float value) {
	mValue = value;
    }

}
