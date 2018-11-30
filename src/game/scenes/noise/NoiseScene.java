package game.scenes.noise;

import cnge.core.Scene;
import game.SparkBase;

public class NoiseScene extends Scene {

	public NoisePanel noisePanel;
	public MoviePanel moviePanel;
	
	public NoiseScene() {
		super(
			new NoiseAssets(((SparkBase)base).loadScreen)
		);
	}
	
	@Override
	public void start() {
		
		camera.getTransform().setTranslation(0, 0);
		
		noisePanel = new NoisePanel(0, 0, 160, 90, 10, 10, 1, 1, 1, 1);
		moviePanel = new MoviePanel(0);
	}
	
	@Override
	public void update() {
		moviePanel.update();
	}
	
	@Override
	public void render() {
		moviePanel.render();
	}

	@Override
	public void resizeUpdate() {
	
	}

}