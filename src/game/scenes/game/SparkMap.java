package game.scenes.game;

import static game.scenes.game.scenery.GameBlocks.PLAIN_BLOCK;
import static game.scenes.game.scenery.GameGraphics.rect;
import static game.scenes.game.scenery.GameGraphics.textureShader;
import static game.scenes.game.scenery.GameGraphics.tileShader;

import cnge.core.Map;
import cnge.graphics.Shader;
import cnge.graphics.Transform;
import cnge.graphics.texture.Texture;
import cnge.graphics.texture.TileTexture;
import game.SparkBlock;
import game.TexBlock;
import game.shaders.TextureShader;

public class SparkMap extends Map<TexBlock> {
	
	public int[][] values;
	
	public SparkMap(int[][] v) {
		super(mAccess, 32);
		values = v;
	}

	public static Access mAccess = new Access() {
		public int access(int[][] a, int x, int y) {
			return edgeAccess(a, x, y);
		}
	};
	
	public void blockRender(int l, int x, int y, float left, float right, float up, float down) {
		int bId = mapAccess(x, y);
		if(bId != Map.OUTSIDE_MAP) {
			TexBlock tb = blockSet.get(bId);
			if(tb != null && tb.layer == l) {
				TileTexture tex = tb.texture;
				
				tex.bind();
				
				tileShader.enable();
				
				tileShader.setMvp(camera.getModelProjectionMatrix(camera.getModelMatrix(left, right, up, down)));
				
				int value = access(values, x, y);
				if(tb.id == PLAIN_BLOCK && SparkLevel.isUpWall(value)) {
					tileShader.setUniforms(tex.getX(), tex.getY(), tex.getZ(0), tex.getW(1), 1, 1, 1, 1);
				} else {
					tileShader.setUniforms(tex.getX(), tex.getY(), tex.getZ(tb.texX), tex.getW(tb.texY), 1, 1, 1, 1);
				}
				
				rect.render();
				
				Shader.disable();
				
				Texture.unbind();
			}
		}
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