package game.scenes.game;

import cnge.core.Scene;
import cnge.core.Base;
import cnge.core.Level;
import game.scenes.game.groups.Blackening;
import game.scenes.game.groups.Countdown;
import game.scenes.game.groups.Player;
import game.scenes.game.groups.Sky;
import game.scenes.game.groups.SparkLevel;
import game.scenes.game.groups.SparkMap;

import static org.lwjgl.glfw.GLFW.*;

import static game.scenes.game.GameEntities.*;

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
		
		background.update();
		blackening.update();
		countdown.update();
		player.update();
		
		currentMap.onScreenUpdate(camera);
		currentMap.update();
	}
	
	public void render() {
		background.render();
		
		currentMap.render(GameBlocks.LAYER_MID);
		
		player.render();
		
		if(blackening != null) {
			blackening.render();
		}
		if(countdown != null) {
			countdown.render();
		}
	}
	
	@Override
	public void resizeUpdate() {
		
	}
	
}