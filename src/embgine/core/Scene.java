package embgine.core;

import embgine.graphics.Camera;

public abstract class Scene {

	protected static Camera camera;
	
	protected static int sceneWidth;
	protected static int sceneHeight;
	
	abstract public void resizeUpdate();
	
	abstract public void update();
	
	abstract public void render();
	
	public void giveCamera(Camera c) {
		camera = c;
	}
	
	public void giveDims(int w, int h) {
		sceneWidth = w;
		sceneHeight = h;
	}
	
}
