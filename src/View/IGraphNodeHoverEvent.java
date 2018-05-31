package View;

public interface IGraphNodeHoverEvent {

    public void addNodeHoverListener(IGraphNodeHoverListener l);

    public void removeNodeHoverListener(IGraphNodeHoverListener l);

    public void notifyNodeHover();
}
