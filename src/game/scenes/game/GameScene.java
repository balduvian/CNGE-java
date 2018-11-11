package game.scenes.game;

import cnge.core.Scene;
import cnge.core.Scenery;
import cnge.core.group.EntityGroup;
import cnge.core.group.MapGroup;
import cnge.core.Base;
import cnge.core.Entity;
import cnge.core.Map;
import cnge.graphics.Transform;
import game.scenes.game.groups.Blackening;
import game.scenes.game.groups.Level1Map;
import game.scenes.game.groups.PlayerEntity;
import game.scenes.game.groups.SparkBackground;

import static org.lwjgl.glfw.GLFW.*;

public class GameScene extends Scene {
	
	public static int LAYERS = 5;
	
	public static int LAYER_BACKGROUND = 0;
	public static int LAYER_PROP = 1;
	public static int LAYER_MAP = 2;
	public static int LAYER_ACTION = 3;
	public static int LAYER_GUI = 4;
	
	public static final double START_TIME = 3;
	
	public Map currentMap;
	public PlayerEntity.E player;
	public Blackening._Blackening blackening;
	
	public boolean pressJump;
	public boolean pressLeft;
	public boolean pressRight;
	
	public double startTimer;
	
	public static PlayerEntity ENTITY_PLAYER;
	public static Level1Map MAP_LEVEL1;
	public static SparkBackground BACKGROUND_SKY;
	public static Blackening ENTITY_BLACKENING;
	
	public GameScene() {
		super(
			LAYERS,
			new Scenery[] {
				new GameGraphics().init(),
				new GameBlocks().init()
			},
			new EntityGroup[] {
				ENTITY_PLAYER = new PlayerEntity(),
				MAP_LEVEL1 = new Level1Map(),
				BACKGROUND_SKY = new SparkBackground(),
				ENTITY_BLACKENING = new Blackening()
			}
		);
	}
	
	@Override
	public void start() {
		startMap(MAP_LEVEL1);
	}
	
	//routine when we start a 
	public void startMap(MapGroup<?> m){
		for(int i = 0; i < numGroups; ++i) {
			groups[i].clear();
		}
		m.load();
		currentMap = m.createMap(0, 0, 0);
		BACKGROUND_SKY.createInstance(0, 0, LAYER_BACKGROUND);
		blackening = ENTITY_BLACKENING.createInstance(0, 0, LAYER_GUI);
		startTimer = START_TIME;
	}
	
	@Override
	public void preUpdate() {
		pressJump = window.keyPressed(GLFW_KEY_W);
		pressRight = window.keyPressed(GLFW_KEY_D);
		pressLeft = window.keyPressed(GLFW_KEY_A);
		
		if(startTimer > 0) {
			startTimer -= Base.time;
			blackening.alpha = (float)(startTimer / START_TIME);		
		} else {
			player.controllable = true;
		}
	}

	@Override
	public void postUpdate() {
		
	}

	@Override
	public void postRender() {
		
	}
	
	@Override
	public void resizeUpdate() {
		
	}
	
}