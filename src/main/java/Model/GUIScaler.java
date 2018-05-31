package Model;


public class GUIScaler implements IScaler {

    private float mValue;
    
    public GUIScaler(float value) {
	mValue = value;
    }
    @Override
    public void setValue(float value) {
	mValue = value;
    }

    @Override
    public float getValue() {
	return mValue;
    }    
   
}
