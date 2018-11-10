package cnge.core;

import cnge.core.group.EntityGroup;
import cnge.graphics.Camera;
import cnge.graphics.Transform;
import cnge.graphics.Window;

public abstract class Scene {

	private static Base base;
	
	protected static Window window;
	protected static Camera camera;
	protected static int screenWidth;
	protected static int screenHeight;
	
	protected int layers;
	
	protected static EntityGroup<?>[] groups;
	protected int numGroups;
	
	public Scene(int l, Scenery[] s, EntityGroup<?>[] g) {
		layers = l;
		
		groups = g;
		numGroups = g.length;
		
		for(int i = 0; i < numGroups; ++i) {
			groups[i].giveLayers(layers);
		}
	}
	
	/*
	 * static stuff that the base gives to the scene
	 */

	public Camera getCamera() {
		return camera;
	}
	
	public EntityGroup<?> getGroup(int i) {
		return groups[i];
	}
	
	public static void giveStuff(Camera c, Base b, Window w) {
		camera = c;
		base = b;
		window = w;
	}
	
	public static void giveDims(int w, int h) {
		screenWidth = w;
		screenHeight = h;
	}
	
	public static void changeScene(Scene s) {
		base.setScene(s);
	}
	
	public void setCameraCenter(float x, float y) {
		Transform ct = camera.getTransform();
		ct.setTranslation(x - ct.getWidth() / 2, y - ct.getHeight() / 2);
	}
	
	/*
	 * override this stuff to do stuff with the scene
	 */
	
	public void overRender() {
		for(int i = 0; i < layers; ++i) {
			for(int j = 0; j < numGroups; ++j) {
				groups[j].render(i);
			}
		}
		postRender();
	}
	
	public void overUpdate() {
		preUpdate();
		for(int j = 0; j < numGroups; ++j) {
			groups[j].onScreenUpdate(camera);
			groups[j].update();
		}
		postUpdate();
	}
	
	abstract public void start();
	
	abstract public void preUpdate();
	
	abstract public void postUpdate();
	
	abstract public void postRender();
	
	abstract public void resizeUpdate();
	
}