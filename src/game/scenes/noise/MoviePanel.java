package game.scenes.noise;

import cnge.core.Base;
import cnge.core.Entity;
import cnge.graphics.Shader;

import static game.scenes.noise.NoiseAssets.*;

public class MoviePanel extends Entity {

	private float seed;
	
	public MoviePanel(float ss) {
		super();
		seed = ss;
	}
	
	public void update() {
		seed += Base.time;
	}

	public void render() {
		movieShader.enable();
		movieShader.setUniforms(seed);
		movieShader.setMvp(camera.getModelProjectionMatrix(camera.getModelMatrix(camera.getTransform())));
		
		rect.render();
		
		Shader.disable();
	}

}