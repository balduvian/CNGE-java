package embgine.core;

import embgine.core.group.EntityGroup;
import embgine.graphics.Camera;
import embgine.graphics.Transform;

public class Entity {
	
	private boolean onScreen;
	
	private int index;
	private int layer;
	
	private EntityGroup group;
	protected Transform transform;
	private Object[] params;
	
	public Entity(EntityGroup g, int np, float x, float y, int l) {
		group = g;
		transform = new Transform();
		transform.setTranslation(x, y);
		params = new Object[np];
		layer = l;
	}
	
	public Object[] getParams() {
		return params;
	}
	
	public Transform getTransform() {
		return transform;
	}
	
	public void destroy() {
		group.destroyInstance(index);
	}
	
	public void setParams(Object... values) {
		params = values;
	}
	
	public void setParam(int i, Object value) {
		params[i] = value;
	}
	
	public void setIndex(int i) {
		index = i;
	}
	
	public int getIndex() {
		return index;
	}
	
	public void setLayer(int l) {
		layer = l;
	}
	
	public int getLayer() {
		return layer;
	}
	
	public boolean onScreenUpdate(Camera c) {
		float ex = transform.    abcissa;
		float ey = transform.   ordinate;
		float ew = transform. getWidth();
		float eh = transform.getHeight();
		
		Transform cTransform = c.getTransform();
		
		float cx = cTransform.    abcissa;
		float cy = cTransform.   ordinate;
		float cw = cTransform. getWidth();
		float ch = cTransform.getHeight();
		
		onScreen = (ex + ew > cx) && (ex < cx + cw) && (ey + eh > cy) && (ey < cy + ch);
		return onScreen;
	}
	
	public void setOnScreen(boolean o) {
		onScreen = o;
	}
	
	public boolean getOnScreen() {
		return onScreen;
	}
	
}