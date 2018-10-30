package game;

import static org.lwjgl.opengl.GL11.*;

import embgine.core.Entity;
import embgine.core.Scene;
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
	
	private EntityGroup imagePanel;
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
		
		imagePanel = new Entity(
			() -> {
				currentCapture.bind();
				
				tileShader.enable();
				tileShader.setUniforms(1, 1, 0, 0, 1, 1, 1, 1);
				tileShader.setMvp(camera.getModelProjectionMatrix(camera.getModelMatrix(imagePanel.getTransform())));
				
				rect.render();
				
				tileShader.disable();
				
				currentCapture.unbind();
			},
			0
		);
		
		sidePanel = new Entity(
			() -> {
				colShader.enable();
				colShader.sendUniforms(1f, 1f, 1f, 0.5f);
				colShader.setMvp(camera.getModelProjectionMatrix(camera.getModelMatrix(sidePanel.getTransform())));
				
				rect.render();
				
				colShader.disable();
			},
			0
		);
		
		sideButton = new Entity(
			() -> {
				Transform t = sideButton.getTransform();
				
				borShader.enable(
					
				);
				
				borShader.sendUniforms(
					0.9f, 0.9f, 0.9f, 1f,
					0.1f, 0.1f, 0.1f, 1f,
					(float)sceneWidth,
					(float)sceneHeight,
					t.abcissa,
					t.ordinate,
					t.width,
					t.height,
					6f,
					0f
				);
				
				float wScale = 6;
				float hScale = 6;
				
				borShader.setMvp(camera.getModelProjectionMatrix(camera.getModelMatrix(sideButton.getTransform())).scale(1.5f, 1.5f, 1f).scale(1.5f, 1.5f, 1f));
				
				rect.render();
				
				borShader.disable();
			},
			1
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
		imagePanel.getTransform().set(sceneWidth, 0, -sceneWidth, sceneHeight);
		
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
		imagePanel.render();
		
		sidePanel.render();
		
		sideButton.render();
	}
	
}