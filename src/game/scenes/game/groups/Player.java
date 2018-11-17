package game.scenes.game.groups;

import static game.scenes.game.GameGraphics.*;

import cnge.core.Base;
import cnge.core.Entity;
import cnge.core.Hitbox;
import cnge.core.Map;
import cnge.core.Map.MapAccessException;
import cnge.core.Map.NullBlockException;
import cnge.core.animation.Anim2D;
import cnge.graphics.Shader;
import cnge.graphics.Transform;
import cnge.graphics.texture.Texture;
import game.Main;
import game.SparkBlock;
import game.scenes.game.GameEntities;
import game.scenes.game.GameScene;

public class Player extends Entity {

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
	
	public boolean controllable;
	
	public Player() {
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
		controllable = false;
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
	
	public void update() {
		GameScene gs = ((GameScene)scene);
		Map map = GameEntities.currentMap;
		Transform t = getTransform();
		Hitbox b = box;
		
		accelerationY = Main.GRAVITY;
		
		float tempA = 0;
		boolean walking = false;
		
		if(controllable) {
			if(wallJumpTimer > 0) {
				wallJumpTimer -= Base.time;
				if(wallJumpTimer < 0 || !gs.pressJump) {
					wallJumpTimer = 0;
				}
			}else {
				if(gs.pressLeft) {
					facing = LEFT;
					walking = true;
					if(air) {
						tempA -= airA;
					}else {
						tempA -= walkA;
					}
				}
				if(gs.pressRight) {
					facing = RIGHT;
					walking = true;
					if(air) {
						tempA += airA;
					}else {
						tempA += walkA;
					}
				}
				if(jumpTimer > 0) {
					jumpTimer -= Base.time;
					velocityY = -jumpV;
					if(jumpTimer < 0 || !gs.pressJump) {
						jumpTimer = 0;
					}
				}else {
					if(jumpLock) {
						jumpLock = gs.pressJump;
					}else {
						if(gs.pressJump) {
							if(air) {
								if(wallLeft) {
									velocityX = maxX * 2.25f;
									velocityY = -jumpV * ( 1 + (float)jumpTime);
									wallJumpTimer = wallJumpTime;
									facing = RIGHT;
									jumpLock = true;
								}else if (wallRight){
									velocityX = -maxX * 2.25f;
									velocityY = -jumpV * ( 1 + (float)jumpTime);
									wallJumpTimer = wallJumpTime;
									facing = LEFT;
									jumpLock = true;
								}
							}else {
								velocityY = -jumpV;
								jumpTimer = jumpTime;
								jumpLock = true;
							}
						}
					}
				}
			}
		}
		if(!walking) {
			if(velocityX > 0) {
				if(air) {
					tempA -= stopAirA;
				}else {
					tempA -= stopA;
				}
				velocityX += (float)(tempA * Base.time);
				if(velocityX < 0) {
					velocityX = 0;
				}
			}else if(velocityX < 0) {
				if(air) {
					tempA += stopAirA;
				}else {
					tempA += stopA;
				}
				velocityX += (float)(tempA * Base.time);
				if(velocityX > 0) {
					velocityX = 0;
				}
			}else {
				tempA = 0;
			}
		}else {
			velocityX += (float)(tempA * Base.time);
		}
		
		if((wallRight || wallLeft)) {
			if(velocityY < 0) {
				velocityY += (wallA + Main.GRAVITY) * Base.time;
			}else {
				velocityY += wallA * Base.time;
				if(velocityY > wallSpeed) {
					velocityY = wallSpeed;
				}
			}
		}else {
			velocityY += accelerationY * Base.time;
		}
		
		//slowdown part for overspeeding
		if(walking) {
			if(velocityX > maxX) {
				if(air) {
					velocityX -= stopAirA;
				}else {
					velocityX -= stopA;
				}
				if(velocityX < maxX) {
					velocityX = maxX;
				}
			}else if(velocityX < -maxX){
				if(air) {
					velocityX += stopAirA;
				}else {
					velocityX += stopA;
				}
				if(velocityX > -maxX) {
					velocityX = -maxX;
				}
			}
		}
		
		dx = (float)(velocityX * Base.time);
		dy = (float)(velocityY * Base.time);
		
		float baseLeft = t.abcissa + b.x;
		float baseRight = t.abcissa + b.x + b.width;
		float baseUp = t.ordinate + b.y;
		float baseDown = t.ordinate + b.y + b.height;
		
		float left = baseLeft + dx;
		float right = baseRight + dx;
		float up = baseUp + dy;
		float down = baseDown + dy;
		
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
		
		wallRight = false;
		wallLeft = false;
		
		for(int i = l; i <= r; ++i) {
			for(int j = u; j <= d; ++j) {
				try {
					try {
						SparkBlock sb = (SparkBlock)map.block(map.access(i, j));
						if(sb.solid) {
							do {
								float upSide = map.getY(j);
								float leftSide = map.getX(i);
								float downSide = map.getY(j + 1);
								float rightSide = map.getX(i + 1);
								
								SparkBlock adjacentBlock;
								
								/**
								 * kinda a misnomer, it means that there is no solid block adjacent to it
								 */
								boolean adjacent;
								
								try {
									adjacentBlock = (SparkBlock)map.block(map.access(i, j - 1));
									adjacent = !adjacentBlock.solid;
								} catch(NullBlockException ex) {
									adjacent = true;
								}
								if(adjacent) {
									if((baseLeft < rightSide && baseRight > leftSide)) {
										float tempDown = upSide - baseDown;
										if(tempDown < downDist && !(tempDown < 0)) {
											downDist = tempDown;
										}
									}
									if(dy > 0 && (down > upSide && down < downSide)) {
										velocityY = 0;
										dy = upSide - baseDown;
										hasHitGround = true;
										break;
									}
								}
								
								try {
									adjacentBlock = (SparkBlock)map.block(map.access(i - 1, j));
									adjacent = !adjacentBlock.solid;
								} catch(NullBlockException ex) {
									adjacent = true;
								}
								if(adjacent && dx > 0 && (baseUp < downSide && baseDown > upSide) && (right > leftSide && right < rightSide)) {
									velocityX = 0;
									dx = leftSide - baseRight;
									wallRight = true;
									break;
								}
								
								
								try {
									adjacentBlock = (SparkBlock)map.block(map.access(i, j + 1));
									adjacent = !adjacentBlock.solid;
								} catch(NullBlockException ex) {
									adjacent = true;
								}
								if(adjacent && dy < 0 && (baseLeft < rightSide && baseRight > leftSide) && (up < downSide && up > upSide)) {
									velocityY = 0;
									dy = downSide - baseUp;
									break;
								}
								
								
								try {
									adjacentBlock = (SparkBlock)map.block(map.access(i + 1, j));
									adjacent = !adjacentBlock.solid;
								} catch(NullBlockException ex) {
									adjacent = true;
								}
								if(adjacent && dx < 0 && (baseUp < downSide && baseDown > upSide) && (left < rightSide && left > leftSide)) {
									velocityX = 0;
									dx = rightSide - baseLeft;
									wallLeft = true;
									break;
								}
								
							} while(false);
						}
					} catch (NullBlockException ex) {
					}
				} catch (MapAccessException ex) { }
			}
		}
		air = !hasHitGround;
		
		if(wallRight) {
			wallRight = downDist > 32;
		}
		if(wallLeft) {
			wallLeft = downDist > 32;
		}
		
		if(air) {
			if(wallLeft || wallRight){
				frameX = 2;
				frameY = 1;
			}else {
				frameX = 1;
				frameY = 1;
			}
		}else {
			if(walking) {
				run.update();
				frameX = run.getX();
				frameY = run.getY();
			}else {
				frameX = 0;
				frameY = 0;
			}
		}
		
		t.move(dx, dy);
		
		scene.setCameraCenter(t.abcissa + t.getWidth() / 2, t.ordinate + t.getHeight() / 2);
	}
	
	public void render() {
		playerSheet.bind();
		
		tileShader.enable();
		
		tileShader.setUniforms(playerSheet.getX(), playerSheet.getY(), playerSheet.getZ(frameX), playerSheet.getW(frameY), 1, 1, 1, 1);
		
		Transform renderT = new Transform(transform);
		if(!facing) {
			renderT.width = -renderT.width;
			renderT.abcissa -= renderT.width;
		}
		tileShader.setMvp(camera.getModelViewProjectionMatrix(camera.getModelMatrix(renderT)));
		
		rect.render();
		
		Shader.disable();
		
		Texture.unbind();
	}

}