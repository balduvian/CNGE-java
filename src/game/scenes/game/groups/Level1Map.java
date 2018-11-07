package game.scenes.game.groups;

import static game.scenes.game.GameBlocks.PLAIN_BLOCK;
import static game.scenes.game.GameGraphics.rect;
import static game.scenes.game.GameGraphics.tileShader;

import org.joml.Vector4f;

import cnge.core.Block;
import cnge.core.Map;
import cnge.core.Map.Access;
import cnge.core.Map.MapAccessException;
import cnge.core.MapBehavior;
import cnge.core.Scene;
import cnge.core.group.MapGroup;
import cnge.graphics.Camera;
import cnge.graphics.Shader;
import cnge.graphics.Texture;
import cnge.graphics.Transform;
import game.SparkBlock;
import game.TexBlock;
import game.scenes.game.GameBlocks;
import game.scenes.game.GameScene;

public class Level1Map extends MapGroup<Level1Map.M>{

	public class M extends Map {

		public M(MapGroup<?> g, float x, float y, int l, Access a, float s, int[][] t) {
			super(g, x, y, l, a, s, t);
		}
		
	}
	
	private static final int VALUE_TOP = 1;
	
	static int[][] values;
	
	public Level1Map() {
		super(
			M.class,
			1,
			new MapBehavior<M>() {
				public void spawn(M e) {
					scene.getGroup(GameScene.ENTITY_COIN).createInstance(96, 96, GameScene.LAYER_ACTION);
				}
				public void update(M e) {
					
				}
				public void render(M e, Camera c) {
					
				}
				public void mapRender(Block b, int x, int y, M m, Transform t) {
					TexBlock tb = (TexBlock)b;
					
					tb.texture.bind();
					
					tileShader.enable();
					
					tileShader.setMvp(camera.getModelViewProjectionMatrix(camera.getModelMatrix(t)));
					
					Vector4f frame = null;
					
					if(tb.id == PLAIN_BLOCK && values[x][y] == VALUE_TOP) {
						frame = tb.texture.getFrame(0, 1);
					}else {
						frame = tb.texture.getFrame(tb.texX, tb.texY);
					}
					
					tileShader.setUniforms(frame.x, frame.y, frame.z, frame.w, 1, 1, 1, 1);
					
					rect.render();
					
					Shader.disable();
					
					Texture.unbind();
				}
				public void mapSpawn(int[][][] tiles, Block[] b) {
					int w = tiles[0].length;
					int h = tiles[0][0].length;
					
					values = new int[w][h];
					
					for(int i = 0; i < w; ++i) {
						for(int j = 1; j < h; ++j) {
							//test if non solid above
							if(tiles[0][i][j] == PLAIN_BLOCK) {
								try {
									SparkBlock bl = ((SparkBlock)b[tiles[0][i][j - 1]]);
									if(!bl.solid) {
										values[i][j] = VALUE_TOP;
									}
								}catch(Exception ex) {
									values[i][j] = VALUE_TOP;
								}
							}
						}
					}
				}
			},
			"res/levels/level1.png",
			new Access() {
				public int access(Map m, int x, int y) throws MapAccessException {
					return m.boundedAccess(x, y);
				}
			},
			32, 
			GameBlocks.world1Blocks
		);
	}
}
