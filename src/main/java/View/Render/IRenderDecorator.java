package View.Render;

import java.awt.Graphics;
import Model.Graph;
import View.PanZoomHelper;

public interface IRenderDecorator {
	
	public void render(Graphics gx, Graph g, PanZoomHelper helper);
}
