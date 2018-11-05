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
import game.SparkBlock;
import game.TexBlock;

import static game.scenes.game.GameBlocks.*;
import static game.scenes.game.GameGraphics.*;
import static org.lwjgl.glfw.GLFW.*;

public class GameScene extends Scene {
	
	public static int LAYERS = 5;
	
	public static int LAYER_BACKGROUND;
	public static int LAYER_PROP;
	public static int LAYER_MAP;
	public static int LAYER_ACTION;
	public static int LAYER_GUI;
	
	public static final int ENTITY_COIN = 0;
	public static final int MAP_LEVEL1 = 1;
	public static final int ENTITY_PLAYER = 2;
	
	public GameScene() {
		super(
			LAYERS,
			new Scenery[] {
				new GameGraphics().init(),
				new GameBlocks().init()
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
							
							t.setSize(256, 256);
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
							tileShader.setMvp(camera.getModelViewProjectionMatrix(camera.getModelMatrix(t)));
							
							rect.render();
							
							Shader.disable();
							
							Texture.unbind();
							
						}
					}
				),
				
				new MapGroup(
					"level1",
					0,
					1,
					1,
					new MapBehavior() {
						public void spawn(Entity e, Object[] p, Transform t) {
							
						}
						public void update(Entity e, Object[] p, Transform t) {
							
						}
						public void render(Entity e, Object[] p, Transform t) {
							
						}
						public void mapRender(Object[] mp, Block b, int x, int y, Map e, Object[] p, Transform t) {
							TexBlock tb = (TexBlock)b;
							
							tb.texture.bind();
							
							tileShader.enable();
							
							tileShader.setMvp(camera.getModelViewProjectionMatrix(camera.getModelMatrix(t)));
							
							Vector4f frame = null;
							
							if(tb.id == PLAIN_BLOCK && ((int[][])mp[0])[x][y] == 1) {
								frame = tb.texture.getFrame(0, 1);
							}else {
								frame = tb.texture.getFrame(tb.texX, tb.texY);
							}
							
							tileShader.setUniforms(frame.x, frame.y, frame.z, frame.w, 1, 1, 1, 1);
							
							rect.render();
							
							Shader.disable();
							
							Texture.unbind();
						}
						public void mapSpawn(Object[] p, Block[] b, int[][][] m) {
							int w = m[0].length;
							int h = m[0][0].length;
							
							p[0] = new int[w][h];
							
							for(int i = 0; i < w; ++i) {
								for(int j = 1; j < h; ++j) {
									//test if non solid above
									if(m[0][i][j] == PLAIN_BLOCK) {
										try {
											SparkBlock bl = ((SparkBlock)b[m[0][i][j - 1]]);
											if(!bl.solid) {
												((int[][])p[0])[i][j] = 1;
											}
										}catch(Exception ex) {
											
											((int[][])p[0])[i][j] = 1;
											
										}
									}
								}
							}
						}
					},
					"res/levels/level1.png",
					32,
					world1Blocks
				)
			}
		);
	}
	
	@Override
	public void start() {
		groups[ENTITY_COIN].createInstance(96, 96, LAYER_ACTION);
		((MapGroup)groups[MAP_LEVEL1]).load();
		((MapGroup)groups[MAP_LEVEL1]).createMap(0, 0);
	}

	@Override
	public void preUpdate() {
		Transform ct = camera.getTransform();
		if(window.keyPressed(GLFW_KEY_W)) {
			ct.moveY(-(float)(32 * Base.time));
		}
		if(window.keyPressed(GLFW_KEY_A)) {
			ct.moveX(-(float)(32 * Base.time));
		}
		if(window.keyPressed(GLFW_KEY_S)) {
			ct.moveY((float)(32 * Base.time));
		}
		if(window.keyPressed(GLFW_KEY_D)) {
			ct.moveX((float)(32 * Base.time));
		}
		
		System.out.println(ct.abcissa + " " + ct.ordinate);
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