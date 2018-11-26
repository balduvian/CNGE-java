package game.scenes.noise;


import cnge.core.Entity;
import cnge.core.Scene;
import cnge.graphics.shapes.RectShape;
import game.shaders.StenShader;

public class NoiseScene extends Scene {

	public static StenShader stenShader;
	public static RectShape rect;
	
	public NoisePanel noisePanel;
	
	@Override
	public void start() {
		camera.getTransform().setTranslation(0, 0);
		rect = new RectShape();
		stenShader = new StenShader();
		
		noisePanel = new NoisePanel(0, 0, 160, 90, 10, 10, 1, 1, 1, 1);
	}
	
	@Override
	public void render() {
		noisePanel.render();
	}

	@Override
	public void update() {
		noisePanel.update();
	}

	@Override
	public void resizeUpdate() {
	
	}

}