package game.scenes.game.groups;

import static game.scenes.game.GameGraphics.coinTex;
import static game.scenes.game.GameGraphics.rect;
import static game.scenes.game.GameGraphics.tileShader;
import static game.scenes.game.GameGraphics.*;

import org.joml.Vector4f;

import cnge.core.Base;
import cnge.core.Behavior;
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
import game.scenes.game.GameGraphics;
import game.scenes.game.GameScene;

public class PlayerEntity extends EntityGroup<PlayerEntity.E> {

	public static final float maxX = 192;
	public static final float maxY = 512;
	public static final float walkA = 1024;
	public static final float stopA = 1024;
	public static final float airA = 512;
	public static final float stopAirA = 32;
	public static final float jumpV = 320;
	
	public static class E extends Entity {
		
		public Anim2D run;
		public int frameX;
		public int frameY;
		
		public float velocityX;
		public float velocityY;
		
		public float dx;
		public float dy;
		
		public float accelerationX;
		public float accelerationY;
		
		public boolean air;
		
		public E() {
			super();
			frameX = 0;
			frameY = 0;
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
	
	public PlayerEntity() {
		super(
			E.class,
			4, 
			new Behavior<E>() {
				public void spawn(E e) {
					
					e.getTransform().setSize(32, 32);
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
						if(e.air) {
							tempA -= airA;
						}else {
							tempA -= walkA;
						}
					}
					if(gs.pressRight) {
						walking = true;
						if(e.air) {
							tempA += airA;
						}else {
							tempA += walkA;
						}
					}
					if(gs.pressJump && !e.air) {
						e.velocityY = -jumpV;
					}
					if(!walking) {
						if(e.velocityX > 0) {
							if(e.air) {
								tempA -= stopAirA;
							}else {
								tempA -= stopA;
							}
							e.velocityX += (float)(tempA * Base.time);
							if(e.velocityX < 0) {
								e.velocityX = 0;
							}
						}else if(e.velocityX < 0) {
							if(e.air) {
								tempA += stopAirA;
							}else {
								tempA += stopA;
							}
							e.velocityX += (float)(tempA * Base.time);
							if(e.velocityX > 0) {
								e.velocityX = 0;
							}
						}else {
							tempA = 0;
						}
					}else {
						e.velocityX += (float)(tempA * Base.time);
					}
					
					e.velocityY += e.accelerationY * Base.time;
					
					if(walking) {
						if(e.velocityX > maxX) {
							e.velocityX = maxX;
						}else if(e.velocityX < -maxX){
							e.velocityX = -maxX;
						}
					}
					
					e.dx = (float)(e.velocityX * Base.time);
					e.dy = (float)(e.velocityY * Base.time);
					
					float baseLeft = t.abcissa;
					float baseRight = t.abcissa + t.width;
					float baseUp = t.ordinate;
					float baseDown = t.ordinate + t.height;
					
					float left = baseLeft + e.dx;
					float right = baseRight + e.dx;
					float up = baseUp + e.dy;
					float down = baseDown + e.dy;
					
					int l = (int)(Math.min(baseLeft, left) / map.getScale() );
					int r = (int)(Math.max(baseRight, right + e.dx) / map.getScale() );
					int u = (int)(Math.min(baseUp, up) / map.getScale() );
					int d = (int)(Math.max(baseDown, down) / map.getScale() );
					
					//System.out.println(l + " " + r + " " + u + " " + d);
					
					boolean hasHitGround = false;
					
					for(int i = l; i <= r; ++i) {
						for(int j = u; j <= d; ++j) {
							try {
								SparkBlock b = (SparkBlock)map.block(map.access(i, j));
								if(b != null && b.solid) {
									do {
										float upSide = map.getY(j);
										float leftSide = map.getX(i);
										float downSide = map.getY(j + 1);
										float rightSide = map.getX(i + 1);
										SparkBlock bc = (SparkBlock)map.block(map.access(i, j - 1));
										if(e.dy > 0 && (baseLeft < rightSide && baseRight > leftSide) && (down > upSide && down < downSide) && (bc == null || !bc.solid)) {
											e.velocityY = 0;
											e.dy = upSide - baseDown;
											hasHitGround = true;
											break;
										}
										
										bc = (SparkBlock)map.block(map.access(i - 1, j));
										if(e.dx > 0 && (baseUp < downSide && baseDown > upSide) && (right > leftSide && right < rightSide) && (bc == null || !bc.solid)) {
											e.velocityX = 0;
											e.dx = leftSide - baseRight;
											break;
										}
										
										bc = (SparkBlock)map.block(map.access(i, j + 1));
										if(e.dy < 0 && (baseLeft < rightSide && baseRight > leftSide) && (up < downSide && up > upSide) && (bc == null || !bc.solid)) {
											e.velocityY = 0;
											e.dy = downSide - baseUp;
											break;
										}
										
										bc = (SparkBlock)map.block(map.access(i + 1, j));
										if(e.dx < 0 && (baseUp < downSide && baseDown > upSide) && (left < rightSide && left > leftSide) && (bc == null || !bc.solid)) {
											e.velocityX = 0;
											e.dx = rightSide - baseLeft;
											break;
										}
										
									} while(false);
								}
							} catch (MapAccessException ex) {
								ex.printStackTrace();
							}
							
						}
					}
					e.air = !hasHitGround;
					
					t.move(e.dx, e.dy);
					
					scene.setCameraCenter(t.abcissa + t.getWidth() / 2, t.ordinate + t.getHeight() / 2);
				}
				public void render(E e, Camera c) {
					Vector4f frame = playerSheet.getFrame(e.frameX, e.frameY);
					
					playerSheet.bind();
					
					colShader.enable();
					
					colShader.sendUniforms(1, 1, 0, 1);
					//tileShader.setUniforms(frame.x, frame.y, frame.z, frame.w, 1, 1, 1, 1);
					colShader.setMvp(c.getModelViewProjectionMatrix(c.getModelMatrix(e.getTransform())));
					
					rect.render();
					
					Shader.disable();
					
					Texture.unbind();
				}
				@Override
				public Entity create(float x, float y, int l, Object... p) {
					return new E();
				}
			}
		);
	}

}