package embgine.core;

import embgine.graphics.Camera;

public abstract class Scene {

	private static Base base;
	
	protected static Camera camera;
	protected static int screenWidth;
	protected static int screenHeight;
	
	protected int layers;
	
	protected EntityGroup[] groups;
	protected int numGroups;
	
	public Scene(int l, EntityGroup[] g) {
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
	
	public static void giveStuff(Camera c, Base b) {
		camera = c;
		base = b;
	}
	
	public static void giveDims(int w, int h) {
		screenWidth = w;
		screenHeight = h;
	}
	
	public static void changeScene(Scene s) {
		base.setScene(s);
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