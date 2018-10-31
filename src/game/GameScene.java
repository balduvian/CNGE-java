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
import game.shaders.border.BorderShader;
import game.shaders.color.ColorShader;
import game.shaders.tile.TileShader;

public class GameScene extends Scene{
	
	private Shape shape;
	private Shape rect;
	
	private TileShader tileShader;
	private ColorShader colShader;
	private BorderShader borShader;
	
	private Texture currentCapture;
	
	private EntityGroup player;
	private Entity sidePanel;
	private Entity sideButton;
	
	private boolean webcamReady;
	
	private boolean cameraAttached;
	
	private boolean oddFrame;
	
	public GameScene() {
		shape = new CurvedTriangle();
		rect = Shape.RECT;
		tileShader = new TileShader();
		colShader = new ColorShader();
		borShader = new BorderShader();
		
		oddFrame = true;
		
		currentCapture = new Texture();
		
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
			4, 0
		);
		
		cameraAttached = getCams();
		
	}

	public boolean getCams() {
		
		return ( webcamReady );
	}
	
	public void getResolutions() {
		
	}
	
	public void getCapture() {
		
	}
	
	@Override
	public void resizeUpdate() {
		player.getTransform().set(sceneWidth, 0, -sceneWidth, sceneHeight);
		
		int spw = sceneWidth / 2;
		
		sidePanel.getTransform().set(0, 0, spw, sceneHeight);
		
		sideButton.getTransform().set(100, 100, spw - 200, 100);
	}
	
	@Override
	public void update() {
		
		if(cameraAttached && (oddFrame = !oddFrame) ) {
			getCapture();
		}
		
	}

	@Override
	public void render() {
		player.render();
		
		sidePanel.render();
		
		sideButton.render();
	}
	
}