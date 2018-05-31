package Model;

public class ValueTimeScaler implements IScaler {

    private int mTime;
    private int mTimeStep = 100;

    private float mStartValue;
    private float mEndValue;
    private float mCurrentValue;

    public ValueTimeScaler(float start, float end, int time) {
	mStartValue = start;
	mEndValue = end;
	mCurrentValue = start;
	mTime = time;

	Ticker t = new Ticker();
	t.start();
    }

    @Override
    public void setValue(float value) {
	mCurrentValue = value;
    }
    
    @Override
    public float getValue() {
	// System.out.println(mCurrentValue);
	return mCurrentValue;
    }

    class Ticker extends Thread {
	@Override
	public void run() {
	    for (int i = 0; i < mTime; i += mTimeStep) {
		int manySteps = mTime / mTimeStep;
		float valueRange = mEndValue - mStartValue;
		float vStep = valueRange / manySteps;
		mCurrentValue += vStep;
		try {
		    Thread.sleep(mTimeStep);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
	    }
	    mCurrentValue = mEndValue;
	}
    }
    
}
