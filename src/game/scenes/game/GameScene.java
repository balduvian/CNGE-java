package game.scenes.game;

import cnge.core.Scene;
import game.scenes.game.entities.Battery;
import game.scenes.game.entities.Blackening;
import game.scenes.game.entities.Countdown;
import game.scenes.game.entities.Player;
import game.scenes.game.entities.Sky;
import game.scenes.game.scenery.GameBlocks;
import game.scenes.game.scenery.GameEntities;
import game.scenes.game.scenery.GameGraphics;
import cnge.core.Base;
import cnge.core.Level;

import static game.scenes.game.scenery.GameEntities.*;
import static org.lwjgl.glfw.GLFW.*;

public class GameScene extends Scene {
	
	public static final double START_TIME = 3;
	
	public boolean pressJump;
	public boolean pressLeft;
	public boolean pressRight;
	
	public double startTimer;
	
	public GameScene() {
		new GameGraphics().init();
		new GameBlocks().init();
		new GameEntities().init();
	}
	
	@Override
	public void start() {
		startMap(level1);
	}
	
	//routine when we start a new map in the scene
	public void startMap(SparkLevel m){
		currentLevel = m;
		
		m.load();
		currentMap = m.createMap(0, 0, 0);
		createEntity(background = new Sky(), 0, 0);
		createEntity(blackening = new Blackening(), 0, 0);
		createEntity(countdown = new Countdown(), 128, 80);
		startTimer = START_TIME;
		
		numBatteries = m.batteryPlacements.length;
		batteries = new Battery[numBatteries];
		for(int i = 0; i < numBatteries; ++i) {
			createEntity(batteries[i] = new Battery(i), m.batteryPlacements[i][0], m.batteryPlacements[i][1]);
		}
		
		createEntity(player = new Player(), currentLevel.startX, currentLevel.startY);
	}
	
	@Override
	public void update() {
		pressJump = window.keyPressed(GLFW_KEY_W);
		pressRight = window.keyPressed(GLFW_KEY_D);
		pressLeft = window.keyPressed(GLFW_KEY_A);
		
		if(startTimer > 0) {
			startTimer -= Base.time;	
		} else {
			player.controllable = true;
		}
		
		eUpdate(background);
		
		eUpdate(blackening);
		eUpdate(countdown);
		
		eUpdate(player);
		
		for(int i = 0; i < numBatteries; ++i) {
			eUpdate_OS(batteries[i]);
		}
		
		currentMap.onScreenUpdate();
		currentMap.update();
		//eUpdate_S(currentMap);
	}
	
	public void render() {
		eRender(background);
		
		currentMap.render(GameBlocks.LAYER_MID);
		
		for(int i = 0; i < numBatteries; ++i) {
			eRender_OS(batteries[i]);
		}
		
		eRender(player);
		
		eRender_S(blackening);
		eRender_S(countdown);
		
		sparkFont.render(new char[] {'y','o',' ','w','h','a','t',' ','u','p'}, 0, 0, 1, false);
		//sparkFont.render(new char[] {'c','e','n','t','e','r',' ','t','h','i','s',' ','m','f',' ','t','e','x','t'}, 256, 124, 2, true);
	}
	
	@Override
	public void resizeUpdate() {
		
	}
	
}