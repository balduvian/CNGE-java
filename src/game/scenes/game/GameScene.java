package game.scenes.game;

import org.joml.Vector4f;

import cnge.core.Entity;
import cnge.core.Map;
import cnge.core.MapBehavior;
import cnge.core.Scene;
import cnge.core.Scenery;
import cnge.core.group.EntityGroup;
import cnge.core.group.MapGroup;
import cnge.core.Base;
import cnge.core.Behavior;
import cnge.core.Block;
import cnge.graphics.Shader;
import cnge.graphics.Texture;
import cnge.graphics.Transform;
import game.SparkBlock;
import game.TexBlock;
import game.scenes.game.groups.Level1Map;
import game.scenes.game.groups.PlayerEntity;

import static game.scenes.game.GameBlocks.*;
import static game.scenes.game.GameGraphics.*;
import static org.lwjgl.glfw.GLFW.*;

public class GameScene extends Scene {
	
	public static int LAYERS = 5;
	
	public static int LAYER_BACKGROUND;
	public static int LAYER_PROP;
	public static int LAYER_MAP;
	public static int LAYER_ACTION;
	public static int LAYER_GUI;
	
	public static final int ENTITY_COIN = 0;
	public static final int MAP_LEVEL1 = 1;
	public static final int ENTITY_PLAYER = 2;
	
	public GameScene() {
		super(
			LAYERS,
			new Scenery[] {
				new GameGraphics().init(),
				new GameBlocks().init()
			},
			new EntityGroup[] {
				new PlayerEntity(),
				new Level1Map()
			}
		);
	}
	
	@Override
	public void start() {
		((MapGroup<?>)groups[MAP_LEVEL1]).load();
		((MapGroup<?>)groups[MAP_LEVEL1]).createMap(0, 0, 0);
	}

	@Override
	public void preUpdate() {
		Transform ct = camera.getTransform();
		if(window.keyPressed(GLFW_KEY_W)) {
			ct.moveY(-(float)(32 * Base.time));
		}
		if(window.keyPressed(GLFW_KEY_A)) {
			ct.moveX(-(float)(32 * Base.time));
		}
		if(window.keyPressed(GLFW_KEY_S)) {
			ct.moveY((float)(32 * Base.time));
		}
		if(window.keyPressed(GLFW_KEY_D)) {
			ct.moveX((float)(32 * Base.time));
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