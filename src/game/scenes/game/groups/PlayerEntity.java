package game.scenes.game.groups;

import static game.scenes.game.GameGraphics.coinTex;
import static game.scenes.game.GameGraphics.rect;
import static game.scenes.game.GameGraphics.tileShader;
import static game.scenes.game.GameGraphics.*;

import org.joml.Vector4f;

import cnge.core.Base;
import cnge.core.Behavior;
import cnge.core.Entity;
import cnge.core.Hitbox;
import cnge.core.Map;
import cnge.core.Map.MapAccessException;
import cnge.core.animation.Anim2D;
import cnge.core.group.EntityGroup;
import cnge.graphics.Camera;
import cnge.graphics.Shader;
import cnge.graphics.Transform;
import cnge.graphics.texture.Texture;
import game.Main;
import game.SparkBlock;
import game.scenes.game.GameGraphics;
import game.scenes.game.GameScene;

public class PlayerEntity extends EntityGroup<PlayerEntity.E> {

	public static final float maxX = 192;
	public static final float maxY = 2000;
	public static final float walkA = 1024;
	public static final float stopA = 1024;
	public static final float airA = 512;
	public static final float stopAirA = 128;
	public static final float jumpV = 270;
	public static final float wallA = 128;
	public static final float wallSpeed = 128;
	public static final double wallJumpTime = 0.5;
	public static final double jumpTime = 0.125;
	public static final boolean RIGHT = true;
	public static final boolean LEFT = false;
	
	public static class E extends Entity {
		
		public Hitbox box;
		
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
		
		public boolean facing;
		
		public float width = 32;
		public float height = 48;
		
		public boolean wallRight;
		public boolean wallLeft;
		
		public boolean jumpLock;
		
		public double wallJumpTimer;
		
		public double jumpTimer;
		
		public E() {
			super();
			frameX = 0;
			frameY = 0;
			velocityX = 0;
			velocityY = 0;
			air = false;
			accelerationY = 32;
			accelerationX = 0;
			facing = RIGHT;
			transform.setSize(width, height);
			jumpLock = false;
			box = new Hitbox(11, 16, 10, 32);
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
				public void update(E e) {
					GameScene gs = ((GameScene)scene);
					Map map = gs.currentMap;
					Transform t = e.getTransform();
					Hitbox b = e.box;
					
					e.accelerationY = Main.GRAVITY;
					
					float tempA = 0;
					boolean walking = false;
					
					if(e.wallJumpTimer > 0) {
						e.wallJumpTimer -= Base.time;
						if(e.wallJumpTimer < 0 || !gs.pressJump) {
							e.wallJumpTimer = 0;
						}
					}else {
						if(gs.pressLeft) {
							e.facing = LEFT;
							walking = true;
							if(e.air) {
								tempA -= airA;
							}else {
								tempA -= walkA;
							}
						}
						if(gs.pressRight) {
							e.facing = RIGHT;
							walking = true;
							if(e.air) {
								tempA += airA;
							}else {
								tempA += walkA;
							}
						}
						if(e.jumpTimer > 0) {
							e.jumpTimer -= Base.time;
							e.velocityY = -jumpV;
							if(e.jumpTimer < 0 || !gs.pressJump) {
								e.jumpTimer = 0;
							}
						}else {
							if(e.jumpLock) {
								e.jumpLock = gs.pressJump;
							}else {
								if(gs.pressJump) {
									if(e.wallLeft) {
										e.velocityX = maxX * 2.25f;
										e.velocityY = -jumpV * ( 1 + (float)jumpTime);
										e.wallJumpTimer = wallJumpTime;
										e.facing = RIGHT;
										e.jumpLock = true;
									}else if (e.wallRight){
										e.velocityX = -maxX * 2.25f;
										e.velocityY = -jumpV * ( 1 + (float)jumpTime);
										e.wallJumpTimer = wallJumpTime;
										e.facing = LEFT;
										e.jumpLock = true;
									}else if(!e.air){
										e.velocityY = -jumpV;
										e.jumpTimer = jumpTime;
										e.jumpLock = true;
									}
								}
							}
						}
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
					
					if((e.wallRight || e.wallLeft)) {
						if(e.velocityY < 0) {
							e.velocityY += (wallA + Main.GRAVITY) * Base.time;
						}else {
							e.velocityY += wallA * Base.time;
							if(e.velocityY > wallSpeed) {
								e.velocityY = wallSpeed;
							}
						}
					}else {
						e.velocityY += e.accelerationY * Base.time;
					}
					
					//slowdown part for overspeeding
					if(walking) {
						if(e.velocityX > maxX) {
							if(e.air) {
								e.velocityX -= stopAirA;
							}else {
								e.velocityX -= stopA;
							}
							if(e.velocityX < maxX) {
								e.velocityX = maxX;
							}
						}else if(e.velocityX < -maxX){
							if(e.air) {
								e.velocityX += stopAirA;
							}else {
								e.velocityX += stopA;
							}
							if(e.velocityX > -maxX) {
								e.velocityX = -maxX;
							}
						}
					}
					
					e.dx = (float)(e.velocityX * Base.time);
					e.dy = (float)(e.velocityY * Base.time);
					
					float baseLeft = t.abcissa + b.x;
					float baseRight = t.abcissa + b.x + b.width;
					float baseUp = t.ordinate + b.y;
					float baseDown = t.ordinate + b.y + b.height;
					
					float left = baseLeft + e.dx;
					float right = baseRight + e.dx;
					float up = baseUp + e.dy;
					float down = baseDown + e.dy;
					
					int l = 0;
					int r = 0;
					if(left > 0) {
						l = (int)(Math.min(baseLeft, left) / map.getScale() );
					}else {
						l = (int)(Math.min(baseLeft, left) / map.getScale() - 1);
					}
					if(right > 0) {
						r = (int)(Math.max(baseRight, right) / map.getScale() );
					}else {
						r = (int)(Math.max(baseRight, right) / map.getScale() + 1);
					}
					
					int u = (int)(Math.min(baseUp, up) / map.getScale() );
					int d = (int)(Math.max(baseDown, down) / map.getScale() ) + 1;
					
					boolean hasHitGround = false;
					
					float downDist = 100;
					
					e.wallRight = false;
					e.wallLeft = false;
					
					for(int i = l; i <= r; ++i) {
						for(int j = u; j <= d; ++j) {
							try {
								SparkBlock sb = (SparkBlock)map.block(map.access(i, j));
								if(sb != null && sb.solid) {
									do {
										float upSide = map.getY(j);
										float leftSide = map.getX(i);
										float downSide = map.getY(j + 1);
										float rightSide = map.getX(i + 1);
										SparkBlock bc = (SparkBlock)map.block(map.access(i, j - 1));
										if((baseLeft < rightSide && baseRight > leftSide)) {
											float tempDown = upSide - baseDown;
											if(tempDown < downDist && tempDown > 0) {
												downDist = tempDown;
											}
											if(e.dy > 0 && (down > upSide && down < downSide) && (bc == null || !bc.solid)) {
												e.velocityY = 0;
												e.dy = upSide - baseDown;
												hasHitGround = true;
												break;
											}
										}
										
										bc = (SparkBlock)map.block(map.access(i - 1, j));
										if(e.dx > 0 && (baseUp < downSide && baseDown > upSide) && (right > leftSide && right < rightSide) && (bc == null || !bc.solid)) {
											e.velocityX = 0;
											e.dx = leftSide - baseRight;
											e.wallRight = true;
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
											e.wallLeft = true;
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
					
					if(e.wallRight) {
						e.wallRight = downDist > 32;
					}
					if(e.wallLeft) {
						e.wallLeft = downDist > 32;
					}
					
					if(e.air) {
						if(e.wallLeft || e.wallRight){
							e.frameX = 2;
							e.frameY = 1;
						}else {
							e.frameX = 1;
							e.frameY = 1;
						}
					}else {
						if(walking) {
							e.run.update();
							e.frameX = e.run.getX();
							e.frameY = e.run.getY();
						}else {
							e.frameX = 0;
							e.frameY = 0;
						}
					}
					
					t.move(e.dx, e.dy);
					
					scene.setCameraCenter(t.abcissa + t.getWidth() / 2, t.ordinate + t.getHeight() / 2);
				}
				public void render(E e, Camera c) {
					Vector4f frame = playerSheet.getFrame(e.frameX, e.frameY);
					
					playerSheet.bind();
					
					tileShader.enable();
					
					tileShader.setUniforms(frame.x, frame.y, frame.z, frame.w, 1, 1, 1, 1);
					
					Transform renderT = new Transform(e.getTransform());
					if(!e.facing) {
						renderT.width = -renderT.width;
						renderT.abcissa -= renderT.width;
					}
					tileShader.setMvp(c.getModelViewProjectionMatrix(c.getModelMatrix(renderT)));
					
					rect.render();
					
					Shader.disable();
					
					Texture.unbind();
				}
				@Override
				public Entity create(Object... p) {
					return new E();
				}
			}
		);
	}

}