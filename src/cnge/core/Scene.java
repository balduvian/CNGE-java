package cnge.core;

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
	
	protected int numGroups;
	
	public Scene(int l) {
		layers = l;
	}
	
	/*
	 * static stuff that the base gives to the scene
	 */

	public Camera getCamera() {
		return camera;
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
	
	public void createEntity(Entity e, float x, float y, int l) {
		e.setup(x, y, l);
	}
	
	public void destroyEntity(Entity e) {
		e = null;
	}
	
	/*
	 * override this stuff to do stuff with the scene
	 */
	
	abstract public void render();
	
	abstract public void update();
	
	abstract public void start();
	
	abstract public void resizeUpdate();
	
}