package game.scenes.game;

import static game.scenes.game.scenery.GameBlocks.*;
import static game.scenes.game.scenery.GameGraphics.rect;
import static game.scenes.game.scenery.GameGraphics.tileShader;

import java.util.ArrayList;

import cnge.core.Block;
import cnge.core.BlockSet;
import cnge.core.Level;
import cnge.graphics.Shader;
import cnge.graphics.texture.Texture;
import cnge.graphics.texture.TileTexture;
import game.SparkBlock;
import game.TexBlock;

abstract public class SparkLevel extends Level<SparkMap, TexBlock> {
	
	public static final int VALUE_TOP = 1;
	
	/*
	 * contains wall data
	 */
	public byte[][] values;
			
	public int[][] batteryPlacements;
	
	public int startX;
	public int startY;
	
	public int finishX;
	public int finishY;
	
	public SparkLevel(String ip, BlockSet<TexBlock> bs) {
		super(ip, bs);
	}
	
	public byte makeValueUp(byte value) {
		return value |= 128;
	}
	
	public byte makeValueRight(byte value) {
		return value |= 64;
	}
	
	public byte makeValueDown(byte value) {
		return value |= 32;
	}
	
	public byte makeValueLeft(byte value) {
		return value |= 16;
	}
	
	public static boolean isUpWall(byte wallValue) {
		return (wallValue & 128) == 128;
	}
	
	public static boolean isRightWall(byte wallValue) {
		return (wallValue & 64) == 64;
	}
	
	public static boolean isDownWall(byte wallValue) {
		return (wallValue & 32) == 32;
	}
	
	public static boolean isLeftWall(byte wallValue) {
		return (wallValue & 16) == 16;
	}
	
	public void onLevelLoad() {
		int w = tiles[0].length;
		int h = tiles[0][0].length;
		
		values = new byte[w][h];
		
		ArrayList<int[]> batteries = new ArrayList<int[]>();
		
		for(int i = 0; i < w; ++i) {
			for(int j = 1; j < h; ++j) {
				int block = tiles[0][i][j];
				SparkBlock bl = (blockSet.get(tiles[0][i][j]));
				if(bl.solid) {
					byte wallValue = 0;
					if(j != 0 && !(blockSet.get(tiles[0][i][j - 1])).solid) {
						wallValue = makeValueUp(wallValue);
					}
					if(i != w - 1 && !(blockSet.get(tiles[0][i + 1][j])).solid) {
						wallValue = makeValueRight(wallValue);
					}
					if(j != h - 1 && !(blockSet.get(tiles[0][i][j + 1])).solid) {
						wallValue = makeValueDown(wallValue);
					}
					if(i != 0 && !(blockSet.get(tiles[0][i - 1][j])).solid) {
						wallValue = makeValueLeft(wallValue);
					}
					values[i][j] = wallValue;
				}
				if(block == START_BLOCK) {
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
	
	public SparkMap mapCreate(int i, Object... params) {
		return new SparkMap(values);
	}

}