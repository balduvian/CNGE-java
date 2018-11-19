package game.scenes.game.scenery;

import static game.scenes.game.scenery.GameGraphics.tileSheet;

import cnge.core.Scenery;
import game.TexBlock;

public class GameBlocks implements Scenery{

	public static final int LAYER_BACK = 0;
	public static final int LAYER_MID = 1;
	public static final int LAYER_FRONT = 2;
	
	public static final int PLAIN_BLOCK = 0;
	public static final int SIGN_RIGHT = 1;
	public static final int SIGN_LEFT = 2;
	public static final int START_BLOCK = 3;
	public static final int FINISH_BLOCK = 4;
	public static final int BATTERY_SPAWN = 5;
	
	public static TexBlock[] world1Blocks;
	
	public void init() {
		world1Blocks = new TexBlock[] {
			new TexBlock(0xff000000, LAYER_MID,  PLAIN_BLOCK,  true, tileSheet, 0, 2),
			new TexBlock(0xff00c8c8, LAYER_MID,  SIGN_RIGHT, false, tileSheet, 0, 0),
			new TexBlock(0xffffc8c8, LAYER_MID,  SIGN_LEFT, false, tileSheet, 1, 0),
			new TexBlock(0xffff0000, LAYER_MID,  START_BLOCK, true, tileSheet, 3, 1),
			new TexBlock(0xff00ff00, LAYER_MID,  FINISH_BLOCK, true, tileSheet, 3, 2),
			new TexBlock(0xff00ffff, -1,  BATTERY_SPAWN, false, null, 0, 0),
		};
	}

}