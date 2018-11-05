package game.scenes.game;

import static game.scenes.game.GameGraphics.tileSheet;

import embgine.core.Scenery;
import game.TexBlock;

public class GameBlocks implements Scenery{

	public static TexBlock[] world1Blocks;
	
	public Scenery init() {
		world1Blocks = new TexBlock[] {
			new TexBlock(0xff000000, GameScene.LAYER_MAP,  true, tileSheet, 0, 2),
			new TexBlock(0xff646464, GameScene.LAYER_MAP,  true, tileSheet, 2, 1),
			new TexBlock(0xffc8c8c8, GameScene.LAYER_MAP, false, tileSheet, 0, 0),
		};

		return this;
	}

}