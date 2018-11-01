package game;

import static org.lwjgl.opengl.GL11.*;

import embgine.core.Entity;
import embgine.core.EntityGroup;
import embgine.core.Scene;
import embgine.core.Behavior;
import embgine.graphics.Shape;
import embgine.graphics.TexShape;
import embgine.graphics.Texture;
import embgine.graphics.Transform;
import embgine.graphics.shapes.RectShape;
import game.shaders.BorderShader;
import game.shaders.ColorShader;
import game.shaders.TileShader;

public class GameScene extends Scene{
	
	public static int GAMESCENE_LAYERS = 5;
	
	private TexShape rect;
	
	private TileShader tileShader;
	private ColorShader colShader;
	private BorderShader borShader;
	
	private Texture playerTex;
	
	private enum group {
		player,
		dab,
		hard
	}
	
	public GameScene() {
		super(
			new EntityGroup[] {
				new EntityGroup(
					"player",
					new Behavior() {
						public void spawn() {
							// TODO Auto-generated method stub
							
						}
						public void update() {
							// TODO Auto-generated method stub
							
						}
						public void render() {
							// TODO Auto-generated method stub
							
						}
					},
					0,
					4,
					0
				)
			},
			GAMESCENE_LAYERS
		);
		
		rect = Shape.RECT;
		
		tileShader = new TileShader();
		colShader = new ColorShader();
		borShader = new BorderShader();
		
		playerTex = new Texture("res/textuers/icon.png");
	}
	
	@Override
	public void start() {
		
	}

	@Override
	public void preUpdate() {
		
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