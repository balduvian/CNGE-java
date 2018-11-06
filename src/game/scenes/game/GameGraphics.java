package game.scenes.game;

import embgine.core.Scenery;
import embgine.graphics.Shape;
import embgine.graphics.TexShape;
import embgine.graphics.Texture;
import game.shaders.BorderShader;
import game.shaders.ColorShader;
import game.shaders.TileShader;

public class GameGraphics implements Scenery{
	
	public static TexShape rect;
	
	public static TileShader tileShader;
	public static ColorShader colShader;
	public static BorderShader borShader;
	
	public static Texture playerTex;
	public static Texture coinTex;
	public static Texture tileSheet;
	
	public Scenery init() {
		rect = Shape.RECT;
		
		tileShader = new TileShader();
		colShader = new ColorShader();
		borShader = new BorderShader();
		
		playerTex = new Texture("res/textures/icon.png");
		coinTex = new Texture("res/textures/coin.png", 14);
		tileSheet = new Texture("res/textures/blocks.png", 3, 3);
		
		return this;
	}

}