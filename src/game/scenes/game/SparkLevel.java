package game.scenes.game;

import static game.scenes.game.scenery.GameBlocks.*;
import static game.scenes.game.scenery.GameGraphics.rect;
import static game.scenes.game.scenery.GameGraphics.tileShader;

import java.util.ArrayList;

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

	public int[][] batteryPlacements;
	
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
		
		ArrayList<int[]> batteries = new ArrayList<int[]>();
		
		for(int i = 0; i < w; ++i) {
			for(int j = 1; j < h; ++j) {
				int block = tiles[0][i][j];
				//test if non solid above	
				if(block == PLAIN_BLOCK) {
					try {
						SparkBlock bl = ((SparkBlock)blockSet[tiles[0][i][j - 1]]);
						if(!bl.solid) {
							values[i][j] = VALUE_TOP;
						}
					} catch(Exception ex) {
						values[i][j] = VALUE_TOP;
					}
				} else if(block == START_BLOCK) {
					startX = i * 32;
					startY = (j - 1) * 32; 
				} else if(block == FINISH_BLOCK) {
					finishX = i * 32;
					finishY = (j - 1) * 32;
				} else if(block == BATTERY_SPAWN) {
					batteries.add(new int[] {i * 32, j * 32});
				}
			}
		}
		
		batteryPlacements = new int[batteries.size()][2];
		batteryPlacements = batteries.toArray(batteryPlacements);
	}

}