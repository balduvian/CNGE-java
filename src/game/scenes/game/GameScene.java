package game.scenes.game;

import cnge.core.Scene;
import cnge.core.Scenery;
import cnge.core.Base;
import cnge.core.Entity;
import cnge.core.Map;
import cnge.core.MapGroup;
import cnge.graphics.Transform;
import game.scenes.game.groups.Blackening;
import game.scenes.game.groups.Countdown;
import game.scenes.game.groups.Level1Map;
import game.scenes.game.groups.Sky;

import static org.lwjgl.glfw.GLFW.*;

import static game.scenes.game.GameEntities.*;

public class GameScene extends Scene {
	
	public static int LAYERS = 5;
	
	public static int LAYER_BACKGROUND = 0;
	public static int LAYER_PROP = 1;
	public static int LAYER_MAP = 2;
	public static int LAYER_ACTION = 3;
	public static int LAYER_GUI = 4;
	
	public static final double START_TIME = 3;
	
	public boolean pressJump;
	public boolean pressLeft;
	public boolean pressRight;
	
	public double startTimer;
	
	public GameScene() {
		super(
			LAYERS
		);
		new GameGraphics().init();
		new GameBlocks().init();
		new GameEntities().init();
	}
	
	@Override
	public void start() {
		startMap(level1);
	}
	
	//routine when we start a new map in the scene
	public void startMap(MapGroup m){
		//TODO some clearing stuff rh
		m.load();
		currentMap = m.createMap(0, 0, 0);
		createEntity(background = new Sky(), 0, 0, 0);
		createEntity(blackening = new Blackening(), 0, 0, LAYER_GUI);
		createEntity(countdown = new Countdown(), 128, 80, LAYER_GUI);
		startTimer = START_TIME;
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
	}
	
	public void render() {
		
	}
	
	@Override
	public void resizeUpdate() {
		
	}
	
}