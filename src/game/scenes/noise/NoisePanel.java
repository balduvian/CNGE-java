package game.scenes.noise;

import cnge.core.Base;
import cnge.core.Entity;
import cnge.graphics.Shader;

import static game.scenes.noise.NoiseScene.*;

import org.joml.Vector2f;

public class NoisePanel extends Entity {

	private float x, y;
	private float w, h, cw, ch;
	private float r, g, b, a;
	
	public NoisePanel(float sx, float sy, float width, float height, float cellW, float cellH, float red, float green, float blue, float alpha) {
		super();
		x = sx;
		y = sy;
		w = width;
		h = height;
		cw = cellW;
		ch = cellH;
		r = red;
		g = green;
		b = blue;
		a = alpha;
	}
	
	public void update() {
		x += 10 * Base.time;
		y += 10 * Base.time;
	}

	public void render() {
		stenShader.enable();
		stenShader.setUniforms(x, y, w, h, cw, ch, r, g, b, a);
		stenShader.setMvp(camera.getModelProjectionMatrix(camera.getModelMatrix(camera.getTransform())));
		
		rect.render();
		
		Shader.disable();
	}

}