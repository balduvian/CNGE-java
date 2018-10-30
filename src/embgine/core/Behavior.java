package embgine.core;

import embgine.graphics.Camera;

public interface Behavior {
	public void spawn();
	
	public boolean isOnScreen(Camera c);
	
	public void update();
	
	public void render();
}
