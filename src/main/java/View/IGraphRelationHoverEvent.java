package View;

public interface IGraphRelationHoverEvent {
    public void addRelationHoverListener(IGraphRelationHoverListener l);

    public void removeRelationHoverListener(IGraphRelationHoverListener l);

    public void notifyRelationHover();
}
