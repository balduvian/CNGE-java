package game.scenes.game;

import cnge.core.Scenery;
import game.scenes.game.groups.Blackening;
import game.scenes.game.groups.Countdown;
import game.scenes.game.groups.Level1Map;
import game.scenes.game.groups.Player;
import game.scenes.game.groups.Sky;
import game.scenes.game.groups.SparkLevel;
import game.scenes.game.groups.SparkMap;

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
	
	/*
	 * map groups
	 */
	public static Level1Map level1;
	
	public void init() {
		level1 = new Level1Map();
	}

}