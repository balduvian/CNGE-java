package game.scenes.game;

import static game.scenes.game.GameGraphics.tileSheet;

import cnge.core.Scenery;
import game.TexBlock;

public class GameBlocks implements Scenery{

	public static final int LAYER_BACK = 0;
	public static final int LAYER_MID = 1;
	public static final int LAYER_FRONT = 2;
	
	public static final int PLAIN_BLOCK = 0;
	public static final int PANEL_BLOCK = 1;
	public static final int SIGN_BLOCK = 2;
	
	public static TexBlock[] world1Blocks;
	
	public void init() {
		world1Blocks = new TexBlock[] {
			new TexBlock(0xff000000, LAYER_MID, PLAIN_BLOCK,  true, tileSheet, 0, 2),
			new TexBlock(0xff646464, LAYER_MID, PANEL_BLOCK,  true, tileSheet, 2, 1),
			new TexBlock(0xffc8c8c8, LAYER_BACK,  SIGN_BLOCK, false, tileSheet, 0, 0),
		};
	}

}