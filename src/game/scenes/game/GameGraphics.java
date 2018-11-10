package game.scenes.game;

import cnge.core.Scenery;
import cnge.graphics.Shape;
import cnge.graphics.TexShape;
import cnge.graphics.texture.Texture;
import game.shaders.BorderShader;
import game.shaders.ColorShader;
import game.shaders.TileShader;

public class GameGraphics implements Scenery {
	
	public static TexShape rect;
	
	public static TileShader tileShader;
	public static ColorShader colShader;
	public static BorderShader borShader;
	
	public static Texture playerTex;
	public static Texture coinTex;
	public static Texture tileSheet;
	public static Texture playerSheet; 
	public static Texture skyTex;
	public static Texture batteryTex;
	
	public Scenery init() {
		rect = Shape.RECT;
		
		tileShader = new TileShader();
		colShader = new ColorShader();
		borShader = new BorderShader();
		
		playerTex = new Texture("res/textures/icon.png");
		coinTex = new Texture("res/textures/coin.png", 14);
		tileSheet = new Texture("res/textures/blocks.png", 4, 4);
		playerSheet = new Texture("res/textures/sparky.png", 4, 2, false);
		skyTex = new Texture("res/textures/sky.png", true, false);
		batteryTex = new Texture("res/textures/battery.png", 8, 2);
		
		return this;
	}

}