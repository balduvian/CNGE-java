package game.scenes.game.groups;

import static game.scenes.game.GameGraphics.*;

import cnge.core.Entity;
import cnge.core.morph.CustomMorph;
import cnge.core.morph.Morph;
import cnge.graphics.Camera;
import cnge.graphics.Shader;
import cnge.graphics.Transform;

public class Blackening extends Entity {
	
	public float alpha;
	public CustomMorph alphaMorph;
	
	public Blackening() {
		super();
		alpha = 1;
		alphaMorph = new CustomMorph(1, 0, Morph.OVERCIRCLE, 1);
		setAlwaysOn(true);
	}
	
	public void update() {
		alpha = alphaMorph.update();
	}
	
	public void render() {
		colShader.enable();
		colShader.setUniforms(0, 0, 0, alpha);
		colShader.setMvp(camera.getModelProjectionMatrix(camera.getModelMatrix(new Transform(camera.getTransform().width, camera.getTransform().height))));
		
		rect.render();
		
		Shader.disable();
	}
}