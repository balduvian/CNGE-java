package game.scenes.game;

import cnge.core.AssetBundle;
import cnge.core.AssetBundle.SceneLoadAction;
import cnge.core.BlockSet;
import cnge.core.Scene;
import cnge.core.Timer;
import cnge.graphics.Shape;
import cnge.graphics.Transform;
import cnge.graphics.texture.TexturePreset;
import cnge.graphics.texture.TileTexture;
import game.scenes.game.entities.Battery;
import game.scenes.game.entities.Blackening;
import game.scenes.game.entities.Countdown;
import game.scenes.game.entities.Player;
import game.scenes.game.entities.Sky;
import game.scenes.game.levels.Level1;
import game.scenes.noise.NoiseScene;
import game.shaders.ColorShader;
import game.shaders.TextShader;
import game.shaders.TextureShader;
import game.shaders.TileShader;

import static game.scenes.game.GameAssets.*;
import static org.lwjgl.glfw.GLFW.*;

import java.lang.reflect.Array;

import game.SparkBase;

import game.TexBlock;

public class GameScene extends Scene<GameScene> {
	
	public boolean pressJump;
	public boolean pressLeft;
	public boolean pressRight;
	
	public float currentMapHeight;
	public float deathBarrier;
	
	public GameScene() {
		super(
			new GameAssets(
				new SceneLoadAction<GameScene>() {
					public void load(GameScene s) {
						startTimer = new Timer(
							3, //3, 2, 1, GO
							() -> {
								player.controllable = true;
							}
						);
					}
				},
				
				new SceneLoadAction<GameScene>() {
					public void load(GameScene s) {
						deathTimer = new Timer(
							2, //2 seconds after you die to restart
							()-> {
								s.startMap(currentLevel);
							}
						);
					}
				},
				
				new SceneLoadAction<GameScene>() {
					public void load(GameScene s) {
						s.startMap(level1);
					}
				}
				
			)
		);
	}
	
	@Override
	public void start() {
		
	}
	
	//routine when we start a new map in the scene
	public void startMap(SparkLevel m){
		currentLevel = m;
		
		m.load();
		currentMap = m.createMap(0, 0, 0);
		currentMapHeight = currentMap.getHeight();
		deathBarrier = (currentMapHeight + 1) * 32;
		
		createEntity(background = new Sky(), 0, 0);
		createEntity(blackening = new Blackening(), 0, 0);
		createEntity(countdown = new Countdown(), 128, 80);
		
		numBatteries = m.batteryPlacements.length;
		batteries = new Battery[numBatteries];
		for(int i = 0; i < numBatteries; ++i) {
			createEntity(batteries[i] = new Battery(i), m.batteryPlacements[i][0], m.batteryPlacements[i][1]);
		}
		
		createEntity(player = new Player(), currentLevel.startX, currentLevel.startY);
		
		startTimer.start();
	}
	
	@Override
	public void update() {
		pressJump = window.keyPressed(GLFW_KEY_W);
		pressRight = window.keyPressed(GLFW_KEY_D);
		pressLeft = window.keyPressed(GLFW_KEY_A);
		
		startTimer.update();
		deathTimer.update();
		
		eUpdate_S(player);
		
		cameraDownLimit(currentMapHeight * 32);
		cameraLeftLimit(0);
		
		eUpdate(background);
		
		eUpdate(blackening);
		eUpdate(countdown);
		
		for(int i = 0; i < numBatteries; ++i) {
			eUpdate_OS(batteries[i]);
		}
		
		currentMap.onScreenUpdate();
		currentMap.update();
		//eUpdate_S(currentMap);
		
		Transform t = player.getTransform();
		if(t.x > currentLevel.finishX && t.y > currentLevel.finishY) {
			Scene.changeScene(new NoiseScene());
		}
	}
	
	public void render() {
		eRender(background);
		
		currentMap.render(LAYER_MID);
		
		for(int i = 0; i < numBatteries; ++i) {
			eRender_OS(batteries[i]);
		}
		
		eRender_S(player);
		
		eRender_S(blackening);
		eRender_S(countdown);
		
		sparkFont.render(new char[] {'y','o',' ','w','h','a','t',' ','u','p'}, 0, 0, 1, false);
		 
	}
	
	@Override
	public void resizeUpdate() {
		
	}
	
	public void die() {
		deathTimer.start();
		player = null;
		blackening.alphaMorph.setTime(2);
		blackening.alphaMorph.reset(0, 1);
	}
	
}