package Model;

public interface IScaler {
    // strategy pattern for scaling values to help with speeding up larger
    // graphs
    public float getValue();
    public void setValue(float value);
}
