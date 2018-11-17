package game.scenes.game;

import cnge.core.Scenery;
import cnge.graphics.Shape;
import cnge.graphics.TexShape;
import cnge.graphics.texture.TexturePreset;
import cnge.graphics.texture.TileTexture;
import game.shaders.ColorShader;
import game.shaders.TextureShader;
import game.shaders.TileShader;

public class GameGraphics implements Scenery {
	
	public static TexShape rect;
	
	public static TileShader tileShader;
	public static ColorShader colShader;
	public static TextureShader textureShader;
	
	public static TileTexture tileSheet;
	public static TileTexture playerSheet; 
	public static TileTexture skyTex;
	public static TileTexture batteryTex;
	public static TileTexture countdownTex;
	
	public void init() {
		rect = Shape.RECT;
		
		tileShader = new TileShader();
		colShader = new ColorShader();
		textureShader = new TextureShader();
		
		tileSheet = new TileTexture("res/textures/blocks.png", 4, 4, new TexturePreset().clampHorz(true).clampVert(true));
		
		playerSheet = new TileTexture("res/textures/sparky.png", 4, 2);
		skyTex = new TileTexture("res/textures/sky.png", 4, new TexturePreset().clampHorz(false).clampVert(false));
		batteryTex = new TileTexture("res/textures/battery.png", 8, 2);
		countdownTex = new TileTexture("res/textures/countdown.png", 2, 2);
	}

}