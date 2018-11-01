package embgine.core;

import embgine.graphics.Camera;
import embgine.graphics.Transform;

public class Entity {
	
	private boolean onScreen;
	
	private int index;
	private int layer;
	
	private EntityGroup group;
	private Transform transform;
	private Behavior behavior;
	protected Object[] params;
	
	public Entity(EntityGroup g, Behavior r, int np) {
		group = g;
		transform = new Transform();
		behavior = r;
		params = new Object[np];
	}
	
	public void update() {
		behavior.update();
	}
	
	public void render() {
		behavior.render();
	}
	
	public void destroy() {
		group.destroyInstance(index);
	}
	
	public Transform getTransform() {
		return transform;
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
		onScreen = true;
		//TODO FIX THIS SHIT
		return onScreen;
	}
	
	public void setOnScreen(boolean o) {
		onScreen = o;
	}
	
	public boolean getOnScreen(Camera c) {
		return onScreen;
	}
	
}