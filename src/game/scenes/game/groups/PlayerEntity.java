package game.scenes.game.groups;

import static game.scenes.game.GameGraphics.coinTex;
import static game.scenes.game.GameGraphics.rect;
import static game.scenes.game.GameGraphics.tileShader;

import org.joml.Vector4f;

import cnge.core.Base;
import cnge.core.Behavior;
import cnge.core.Block;
import cnge.core.Entity;
import cnge.core.Map;
import cnge.core.Map.MapAccessException;
import cnge.core.animation.Anim2D;
import cnge.core.group.EntityGroup;
import cnge.graphics.Camera;
import cnge.graphics.Shader;
import cnge.graphics.Texture;
import cnge.graphics.Transform;
import game.Main;
import game.SparkBlock;
import game.scenes.game.GameScene;

public class PlayerEntity extends EntityGroup<PlayerEntity.E> {

	public static final float maxX = 128;
	public static final float maxY = 512;
	public static final float walkA = 256;
	public static final float stopA = 256;
	
	public static class E extends Entity {
		
		public Anim2D run;
		public int frame;
		
		public float velocityX;
		public float velocityY;
		
		public float dx;
		public float dy;
		
		public float accelerationX;
		public float accelerationY;
		
		public boolean air;
		
		public E() {
			super();
			frame = 0;
			velocityX = 0;
			velocityY = 0;
			air = false;
			accelerationY = 32;
			accelerationX = 0;
			run = new Anim2D(
				new int[][] {
					{1, 0},
					{2, 0},
					{1, 0},
					{3, 0},
					{0, 1},
					{3, 0}
				},
				new double[] {
					0.1,
					0.1,
					0.1,
					0.1,
					0.1,
					0.1
				}
			);
		}
		
	}
	
	private static Behavior<E> eBehavior;
	
	public PlayerEntity() {
		super(
			E.class,
			4, 
			eBehavior
		);
		
		eBehavior = new Behavior<E>() {
			public void spawn(E e) {
				
				e.getTransform().setSize(256, 256);
			}
			public void update(E e) {
				GameScene gs = ((GameScene)scene);
				Map map = gs.currentMap;
				Transform t = e.getTransform();
				
				e.accelerationY = Main.GRAVITY;
				
				float tempA = 0;
				boolean walking = false;
				if(gs.pressLeft) {
					walking = true;
					tempA -= walkA;
				}
				if(gs.pressRight) {
					walking = true;
					tempA += walkA;
				}
				if(!walking) {
					if(e.velocityX > 0) {
						tempA -= stopA;
					}else if(e.velocityX < 0) {
						tempA += stopA;
					}else {
						tempA = 0;
					}
				}
				e.accelerationX = tempA;
				
				e.velocityX += (float)(e.accelerationX * Base.time);
				if(walking) {
					if(e.velocityX > maxX) {
						e.velocityX = maxX;
					}else if(e.velocityX < -maxX){
						e.velocityX = -maxX;
					}
				}
				
				e.dx = (float)(e.velocityX * Base.time);
				e.dy = (float)(e.velocityY * Base.time);
				
				float left = t.abcissa;
				float right = t.abcissa + t.width;
				float up = t.ordinate;
				float down = t.ordinate + t.height;
				
				int l = (int)left;
				int r = (int)right;
				int u = (int)up;
				int d = (int)down;
				
				for(int i = l; i <= r; ++i) {
					for(int j = u; j <= d; ++j) {
						try {
							SparkBlock b = (SparkBlock)map.block(map.access(i, j));
							if(b.solid) {
								
							}
						} catch (MapAccessException ex) {
							ex.printStackTrace();
						}
						
					}
				}
				//map.access(x, y);
			}
			public void render(E e, Camera c) {
				Vector4f frame = coinTex.getFrame(e.frame);
				
				coinTex.bind();
				
				tileShader.enable();
				
				tileShader.setUniforms(frame.x, frame.y, frame.z, frame.w, 1, 1, 1, 1);
				tileShader.setMvp(c.getModelViewProjectionMatrix(c.getModelMatrix(e.getTransform())));
				
				rect.render();
				
				Shader.disable();
				
				Texture.unbind();
			}
			@Override
			public Entity create(float x, float y, int l, Object... p) {
				return new E();
			}
		};
	}

}