package game.scenes.game;

import static game.scenes.game.scenery.GameBlocks.PLAIN_BLOCK;
import static game.scenes.game.scenery.GameGraphics.rect;
import static game.scenes.game.scenery.GameGraphics.tileShader;

import cnge.core.Block;
import cnge.core.Level;
import cnge.graphics.Shader;
import cnge.graphics.texture.Texture;
import cnge.graphics.texture.TileTexture;
import game.SparkBlock;
import game.TexBlock;

abstract public class SparkLevel extends Level<SparkMap> {
	
	public static final int VALUE_TOP = 1;
	
	public static int[][] values;

	public int startX;
	public int startY;
	
	public int finishX;
	public int finishY;
	
	public SparkLevel(String ip, Block[] bs) {
		super(ip, bs);
	}
	
	public void mapSpawn() {
		int w = tiles[0].length;
		int h = tiles[0][0].length;
		
		values = new int[w][h];
		
		for(int i = 0; i < w; ++i) {
			for(int j = 1; j < h; ++j) {
				//test if non solid above
				if(tiles[0][i][j] == PLAIN_BLOCK) {
					try {
						SparkBlock bl = ((SparkBlock)blockSet[tiles[0][i][j - 1]]);
						if(!bl.solid) {
							values[i][j] = VALUE_TOP;
						}
					}catch(Exception ex) {
						values[i][j] = VALUE_TOP;
					}
				}
			}
		}
		
		startX = 32;
		startY = 128;
	}

}