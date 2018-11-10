package game.scenes.game;

import cnge.core.Scene;
import cnge.core.Scenery;
import cnge.core.group.EntityGroup;
import cnge.core.group.MapGroup;
import cnge.core.Base;
import cnge.core.Entity;
import cnge.core.Map;
import cnge.graphics.Transform;
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
	
	public static final int ENTITY_PLAYER = 0;
	public static final int MAP_LEVEL1 = 1;
	public static final int BACKGROUND = 2;
	
	public Map currentMap;
	public Entity player;
	
	public boolean pressJump;
	public boolean pressLeft;
	public boolean pressRight;
	
	public GameScene() {
		super(
			LAYERS,
			new Scenery[] {
				new GameGraphics().init(),
				new GameBlocks().init()
			},
			new EntityGroup[] {
				new PlayerEntity(),
				new Level1Map(),
				new SparkBackground()
			}
		);
	}
	
	@Override
	public void start() {
		startMap(MAP_LEVEL1);
	}
	
	public void startMap(int m){
		((MapGroup<?>)groups[m]).load();
		currentMap = ((MapGroup<?>)groups[m]).createMap(0, 0, 0);
		groups[BACKGROUND].createInstance(0, 0, LAYER_BACKGROUND);
	}
	
	@Override
	public void preUpdate() {
		pressJump = window.keyPressed(GLFW_KEY_W);
		pressRight = window.keyPressed(GLFW_KEY_D);
		pressLeft = window.keyPressed(GLFW_KEY_A);
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