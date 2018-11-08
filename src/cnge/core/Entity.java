package cnge.core;

import cnge.core.group.EntityGroup;
import cnge.graphics.Camera;
import cnge.graphics.Transform;

public class Entity {
	
	private boolean onScreen;
	
	private int index;
	private int layer;
	
	private EntityGroup<?> group;
	protected Transform transform;
	
	/**
	 * constructor for entities
	 * 
	 * @param params - optional paramters on creation
	 */
	public Entity(Object[] params) {
		System.out.println("yer");
		System.out.println("mum");
		System.out.println("gai");
	}
	
	/**
	 * this is called immeadiately after the entity is constructed.
	 * this is so that you don't have to put these params in the custom entity constructors
	 *
	 * @param g - the entity group this entity belongs to
	 */
	public void setup(float x, float y, int l, EntityGroup<?> g) {
		transform = new Transform();
		transform.setTranslation(x, y);
		layer = l;
		group = g;
	}
	
	public Transform getTransform() {
		return transform;
	}
	
	public void destroy() {
		group.destroyInstance(index);
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