package game.scenes.game.groups;

import static game.scenes.game.GameBlocks.PLAIN_BLOCK;
import static game.scenes.game.GameGraphics.rect;
import static game.scenes.game.GameGraphics.tileShader;
import static game.scenes.game.GameGraphics.textureShader;

import cnge.core.Map;
import cnge.graphics.Shader;
import cnge.graphics.Transform;
import cnge.graphics.texture.Texture;
import cnge.graphics.texture.TileTexture;
import game.TexBlock;
import game.scenes.game.GameGraphics;
import game.shaders.TextureShader;

public class SparkMap extends Map {
	
	public SparkMap() {
		super(mAccess, 32);
	}

	public static Access mAccess = new Access() {
		public int access(Map m, int x, int y) throws MapAccessException {
			return m.edgeAccess(x, y);
		}
	};
	
	public void blockRender(int b, int x, int y, float l, float r, float u, float d) {
		TexBlock tb = (TexBlock) blockSet[b];
		TileTexture tex = tb.texture;
		
		tex.bind();
		
		tileShader.enable();
		
		tileShader.setMvp(camera.getModelViewProjectionMatrix(camera.getModelMatrix(l, r, u, d)));
		
		try {
			if(tb.id == PLAIN_BLOCK && SparkLevel.values[x][y] == SparkLevel.VALUE_TOP) {
				tileShader.setUniforms(tex.getX(), tex.getY(), tex.getZ(0), tex.getW(1), 1, 1, 1, 1);
			}else {
				tileShader.setUniforms(tex.getX(), tex.getY(), tex.getZ(tb.texX), tex.getW(tb.texY), 1, 1, 1, 1);
			}
		} catch(Exception ex) {
			tileShader.setUniforms(tex.getX(), tex.getY(), tex.getZ(tb.texX), tex.getW(tb.texY), 1, 1, 1, 1);
		}
		
		rect.render();
		
		Shader.disable();
		
		Texture.unbind();
	}
	
	public void mapRender(Transform t, Texture tx) {
		tx.bind();
		
		tileShader.enable();
		
		tileShader.setMvp(camera.getModelViewProjectionMatrix(camera.getModelMatrix(t)));
		tileShader.setUniforms(1, -1, 0, 0, 1, 1, 1, 1);
		
		rect.render();
		
		Shader.disable();
		
		Texture.unbind();
	}
	
	public void update() {
		
	}

}