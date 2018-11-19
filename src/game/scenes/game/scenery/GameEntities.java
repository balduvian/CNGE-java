package game.scenes.game.scenery;

import cnge.core.Scenery;
import game.scenes.game.SparkFont;
import game.scenes.game.SparkLevel;
import game.scenes.game.SparkMap;
import game.scenes.game.entities.Battery;
import game.scenes.game.entities.Blackening;
import game.scenes.game.entities.Countdown;
import game.scenes.game.entities.Player;
import game.scenes.game.entities.Sky;
import game.scenes.game.levels.Level1;

public class GameEntities implements Scenery {
	
	/*
	 * stuff in the scene
	 */
	public static SparkLevel currentLevel;
	public static SparkMap currentMap;
	public static Player player;
	public static Sky background;
	public static Countdown countdown;
	public static Blackening blackening;
	
	public static Battery[] batteries;
	public static int numBatteries;
	
	/*
	 * map groups
	 */
	public static Level1 level1;
	
	/*
	 * fonts
	 */
	public static SparkFont sparkFont;
	
	public void init() {
		level1 = new Level1();
		
		sparkFont = new SparkFont();
	}

}