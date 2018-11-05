package game.scenes.game;

import org.joml.Vector4f;

import embgine.core.Entity;
import embgine.core.Map;
import embgine.core.MapBehavior;
import embgine.core.Scene;
import embgine.core.Scenery;
import embgine.core.group.EntityGroup;
import embgine.core.group.MapGroup;
import embgine.core.Base;
import embgine.core.Behavior;
import embgine.core.Block;
import embgine.graphics.Shader;
import embgine.graphics.Texture;
import embgine.graphics.Transform;
import game.TexBlock;

import static game.scenes.game.GameBlocks.*;
import static game.scenes.game.GameGraphics.*;

public class GameScene extends Scene {
	
	public static int LAYERS = 5;
	
	public static int LAYER_BACKGROUND;
	public static int LAYER_PROP;
	public static int LAYER_MAP;
	public static int LAYER_ACTION;
	public static int LAYER_GUI;
	
	public static final int ENTITY_PLAYER = 0;
	public static final int MAP_LEVEL1 = 1;
	
	public GameScene() {
		super(
			LAYERS,
			new Scenery[] {
				new GameGraphics(),
				new GameBlocks()
			},
			new EntityGroup[] {
				new EntityGroup(
					"player",
					2,
					4,
					new Behavior() {
						public void spawn(Entity e, Object[] p, Transform t) {
							
							p[0] = (int)0;
							p[1] = (double)0;
							
							t.setSize(16, 16);
						}
						public void update(Entity e, Object[] p, Transform t) {
							p[1] = (double)p[1] + Base.time;
							if((double)p[1] >= 0.066666666) {
								p[1] = (double)0;
								p[0] = (int)p[0] + 1;
								p[0] = (int)p[0] % 14;
							}
						}
						public void render(Entity e, Object[] p, Transform t) {
							
							Vector4f frame = coinTex.getFrame((int)p[0]);
							
							coinTex.bind();
							
							tileShader.enable();
							
							tileShader.setUniforms(frame.x, frame.y, frame.z, frame.w, 1, 1, 1, 1);
							tileShader.setMvp(camera.getModelProjectionMatrix(camera.getModelMatrix(t)));
							
							rect.render();
							
							tileShader.disable();
							
							Texture.unbind();
							
						}
					}
				),
				
				new MapGroup(
					"level1",
					0,
					1,
					new MapBehavior() {
						public void spawn(Entity e, Object[] p, Transform t) {
							
						}
						public void update(Entity e, Object[] p, Transform t) {
							
						}
						public void render(Entity e, Object[] p, Transform t) {
							
						}
						public void mapRender(Block b, int x, int y, Map e, Object[] p, Transform t) {
							TexBlock tb = (TexBlock)b;
							
							tb.texture.bind();
							
							tileShader.enable();
							
							tileShader.setMvp(camera.getModelViewProjectionMatrix(camera.getModelMatrix(t)));
							
							Vector4f frame = tb.texture.getFrame(tb.texX, tb.texY);
							tileShader.setUniforms(frame.x, frame.y, frame.z, frame.w, 1, 1, 1, 1);
							
							rect.render();
							
							Shader.disable();
							
							Texture.unbind();
						}
						public void mapSpawn(Block[] b, int[][][] m) {
							
						}
					},
					"res/maps/level1.png",
					2,
					world1Blocks
				)
			}
		);
	}
	
	public void mapRender(Map m) {
		
	}
	
	@Override
	public void start() {
		groups[ENTITY_PLAYER].createInstance(8, 8, 0);
		((MapGroup)groups[MAP_LEVEL1]).createMap(0, 0);
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