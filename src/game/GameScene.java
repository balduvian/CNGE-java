package game;

import static org.lwjgl.opengl.GL11.*;

import embgine.core.Entity;
import embgine.core.EntityGroup;
import embgine.core.Scene;
import embgine.core.Spawn;
import embgine.graphics.Texture;
import embgine.graphics.Transform;
import embgine.graphics.shapes.CurvedTriangle;
import embgine.graphics.shapes.Shape;
import game.shaders.BorderShader;
import game.shaders.ColorShader;
import game.shaders.TileShader;

public class GameScene extends Scene{
	
	private Shape shape;
	private Shape rect;
	
	private TileShader tileShader;
	private ColorShader colShader;
	private BorderShader borShader;
	
	private EntityGroup player;
	
	public GameScene() {
		shape = new CurvedTriangle();
		rect = Shape.RECT;
		tileShader = new TileShader();
		colShader = new ColorShader();
		borShader = new BorderShader();
		
		player = new EntityGroup(
			new Spawn() {
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
		);
		
	}
	
	@Override
	public void resizeUpdate() {
		
	}
	
	@Override
	public void update() {
		
	}

	@Override
	public void render() {
		
	}
	
}