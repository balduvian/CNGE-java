package game.scenes.game.entities;

import static game.scenes.game.scenery.GameGraphics.*;
import static game.scenes.game.scenery.GameEntities.*;

import cnge.core.Entity;
import cnge.core.Hitbox;
import cnge.core.animation.Anim2D;
import cnge.core.morph.Morph;
import cnge.graphics.Shader;
import cnge.graphics.Transform;
import cnge.graphics.texture.Texture;

public class Battery extends Entity {

	public Anim2D anim;
	public Hitbox box;
	public int batteryIndex;
	
	public Battery(int i) {
		super();
		anim = new Anim2D(
			new int[][] {
				{0, 0},
				{1, 0},
				{2, 0},
				{3, 0},
				{4, 0},
				{5, 0},
				{6, 0},//blank
				{0, 1},
				{1, 1},
				{2, 1},
				{3, 1},
				{4, 1}
			},
			new double[] {
				0.0833333333,
				0.0833333333,
				0.0833333333,
				0.0833333333,
				0.0833333333,
				0.0833333333,
				0.0833333333,
				0.0833333333,
				0.0833333333,
				0.0833333333,
				0.0833333333,
				0.0833333333
			}
		);
		transform.setSize(32, 32);
		box = new Hitbox(8, 2, 16, 28);
		batteryIndex = i;
	}
	
	public void update() {
		anim.update();
		if(player != null) {
			if(box.intersects(player.getTransform().x - transform.x, player.getTransform().y - transform.y, player.collectBox)) {
				batteries[batteryIndex] = null;
			}
		}
	}
	
	public void render() {
		batteryTex.bind();
		
		tileShader.enable();
		tileShader.setUniforms(batteryTex.getX(), batteryTex.getY(), batteryTex.getZ(anim.getX()), batteryTex.getW(anim.getY()), 1, 1, 1, 1);
		tileShader.setMvp(camera.getModelViewProjectionMatrix(camera.getModelMatrix(transform)));
		
		rect.render();
		
		Shader.disable();
		
		Texture.unbind();
	}
	
}