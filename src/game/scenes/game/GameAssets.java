package game.scenes.game;

import static game.scenes.game.GameAssets.currentLevel;
import static game.scenes.game.GameAssets.player;

import cnge.core.AssetBundle;
import cnge.core.BlockSet;
import cnge.core.LoadScreen;
import cnge.core.Scene;
import cnge.core.Timer;
import cnge.core.AssetBundle.LoadAction;
import cnge.core.AssetBundle.SceneLoadAction;
import cnge.graphics.Shape;
import cnge.graphics.TexShape;
import cnge.graphics.texture.TexturePreset;
import cnge.graphics.texture.TileTexture;
import game.SparkBase;
import game.SparkLoadScreen;
import game.TexBlock;
import game.scenes.game.entities.Battery;
import game.scenes.game.entities.Blackening;
import game.scenes.game.entities.Countdown;
import game.scenes.game.entities.Player;
import game.scenes.game.entities.Sky;
import game.scenes.game.levels.Level1;
import game.shaders.ColorShader;
import game.shaders.TextShader;
import game.shaders.TextureShader;
import game.shaders.TileShader;

public class GameAssets extends AssetBundle<GameScene> {
	
	public static final int LAYER_BACK = 0;
	public static final int LAYER_MID = 1;
	public static final int LAYER_FRONT = 2;
	
	public static final int PLAIN_BLOCK = 0;
	public static final int SIGN_RIGHT = 1;
	public static final int SIGN_LEFT = 2;
	public static final int START_BLOCK = 3;
	public static final int FINISH_BLOCK = 4;
	public static final int BATTERY_SPAWN = 5;
	
	/*
	 * block sets
	 */
	public static BlockSet<TexBlock> world1Blocks;
	
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
	
	/*
	 * graphics
	 */
	public static TexShape rect;
	
	public static TileShader tileShader;
	public static ColorShader colShader;
	public static TextureShader textureShader;
	public static TextShader textShader;
	
	public static TileTexture tileSheet;
	public static TileTexture playerSheet; 
	public static TileTexture skyTex;
	public static TileTexture batteryTex;
	public static TileTexture countdownTex;
	
	/*
	 * timers
	 */
	public static Timer startTimer;
	public static Timer deathTimer;
	
	@SafeVarargs
	public GameAssets(SceneLoadAction<GameScene>... c) {
		super(
			((SparkBase)base).loadScreen,
			new LoadAction[] {
					() -> {
						rect = Shape.RECT;		
					},
					() -> {
						tileShader = new TileShader();
						colShader = new ColorShader();
						textureShader = new TextureShader();
						textShader = new TextShader();
					},
					() -> {
						tileSheet = new TileTexture("res/textures/blocks.png", 4, 4, new TexturePreset().clampHorz(true).clampVert(true));
					},
					() -> {
						playerSheet = new TileTexture("res/textures/sparky.png", 4, 2);
					},
					() -> {
						skyTex = new TileTexture("res/textures/sky.png", 4, new TexturePreset().clampHorz(false).clampVert(false));
					},
					() -> {
						batteryTex = new TileTexture("res/textures/battery.png", 7, 2);
					},
					() -> {
						countdownTex = new TileTexture("res/textures/countdown.png", 2, 2);
					},
					() -> {
						world1Blocks = new BlockSet<TexBlock>(
							//default
							new TexBlock(-1, -1, false, null, -1, -1),	
							//actual
							new TexBlock(0xff000000, LAYER_MID, true, tileSheet, 0, 2),
							new TexBlock(0xff00c8c8, LAYER_MID, false, tileSheet, 0, 0),
							new TexBlock(0xffffc8c8, LAYER_MID, false, tileSheet, 1, 0),
							new TexBlock(0xffff0000, LAYER_MID, true, tileSheet, 3, 1),
							new TexBlock(0xff00ff00, LAYER_MID, true, tileSheet, 3, 2),
							new TexBlock(0xff00ffff, -1, false, null, 0, 0)	
						);
					},
					() -> {
						level1 = new Level1();
						
						sparkFont = new SparkFont();
					}
				},
			c
		);
	}
	
}