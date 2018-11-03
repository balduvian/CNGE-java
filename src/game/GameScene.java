package game;

import static org.lwjgl.opengl.GL11.*;

import org.joml.Vector4f;

import embgine.core.Entity;
import embgine.core.EntityGroup;
import embgine.core.Scene;
import embgine.core.Base;
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
	
	public static int LAYERS = 5;
	
	private static TexShape rect;
	
	private static TileShader tileShader;
	private static ColorShader colShader;
	private static BorderShader borShader;
	
	private static Texture playerTex;
	private static Texture coinTex;
	
	private enum group {
		player,
		dab,
		hard
	}
	
	public GameScene() {
		super(
			LAYERS,
			new EntityGroup[] {
				new EntityGroup(
					"player",
					new Behavior() {
						public void spawn(Object[] p, Transform t) {
							
							System.out.println("I'M HERE BITCHES");
							
							p[0] = (int)0;
							p[1] = (double)0;
							
							t.setSize(1, 1);
						}
						public void update(Object[] p, Transform t) {
							p[1] = (double)p[1] + Base.time;
							if((double)p[1] >= 0.1666) {
								p[1] = (double)0;
								p[0] = (int)p[0] + 1;
								p[0] = (int)p[0] % 14;
							}
						}
						public void render(Object[] p, Transform t) {
							coinTex.bind();
							
							Vector4f frame = coinTex.getFrame((int)p[0]);
							
							tileShader.enable();
							tileShader.setUniforms(frame.x, frame.y, frame.z, frame.w, 1, 1, 1, 1);
							tileShader.setMvp(camera.getModelProjectionMatrix(camera.getModelMatrix(t)));
							
							rect.render();
							
							tileShader.disable();
							
							coinTex.unbind();
					
						}
					},
					2,
					4
				)
			}
		);
		
		rect = Shape.RECT;
		
		tileShader = new TileShader();
		colShader = new ColorShader();
		borShader = new BorderShader();
		
		playerTex = new Texture("res/textures/icon.png");
		coinTex = new Texture("res/textures/coin.png", 14);
	}
	
	@Override
	public void start() {
		groups[0].createInstance(0, 0, 0);
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