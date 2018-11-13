package game.scenes.game.groups;

import game.scenes.game.GameBlocks;

public class Level1Map extends SparkLevel {
	
	public Level1Map() {
		super(
			"res/levels/level1.png",
			GameBlocks.world1Blocks
		);
	}
	
	public SparkMap mapCreate(int i, Object... params) {
		return new SparkMap();
	}
	
}