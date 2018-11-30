package game.scenes.game;

import static game.scenes.game.GameAssets.*;

import cnge.core.Font;
import cnge.graphics.Shader;
import cnge.graphics.texture.Texture;

public class SparkFont extends Font{

	public SparkFont() {
		super("res/font/default.bmp", "res/font/default.csv");
	}

	protected void charRender(int cx, int cy, float left, float right, float up, float down) {
		texture.bind();
		
		textShader.enable();
		textShader.setMvp(camera.getModelProjectionMatrix(camera.getModelMatrix(left, right, up, down)));		
		textShader.setUniforms(texture.getX(), texture.getY(), texture.getZ(cx), texture.getW(cy), 1, 1, 1, 1);
		
		rect.render();
		
		Shader.disable();
		
		Texture.unbind();
	}

}
