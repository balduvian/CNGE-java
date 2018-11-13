package game.scenes.game.groups;

import static game.scenes.game.GameGraphics.*;

import cnge.core.Entity;
import cnge.core.animation.Anim2D;
import cnge.core.morph.Morph;
import cnge.graphics.Shader;
import cnge.graphics.texture.Texture;

public class Countdown extends Entity {

	public Anim2D anim;
	public Morph grow;
	public Morph shrink;
	public Morph current;
	
	public Countdown() {
		super();
		anim = new Anim2D(
			new int[][] {
				{0, 0},//3
				{1, 0},//2
				{0, 1},//1
				{1, 1},//go
				{1, 1}//go fade
			},
			new double[] {
				1,
				1,
				1,
				0.25,
				0.25
			}
		);
		anim.addEvent(1, () -> {
			transform.setScale(0, 0);
			grow.reset();
		});
		anim.addEvent(2, () -> {
			transform.setScale(0, 0);
			grow.reset();
		});
		anim.addEvent(3, () -> {
			transform.setScale(0, 0);
			grow.setTime(0.25);
			grow.reset();
		});
		anim.addEvent(4, () -> {
			shrink.reset();
			current = shrink;
		});
		anim.addEvent(0, () -> {
			
		});
		transform.setSize(256, 128);
		transform.setScale(0, 0);
		grow = new Morph(transform, Morph.OVERCIRCLE, 1).addScaleW(1).addScaleH(1);
		shrink = new Morph(transform, Morph.UNDERCIRCLE, 0.25).addScaleW(0).addScaleH(0);
		setAlwaysOn(true);
		current = grow;
	}
	
	public void update() {
		anim.update();
		current.update();
	}
	
	public void render() {
		countdownTex.bind();
		
		tileShader.enable();
		tileShader.setUniforms(countdownTex.getX(), countdownTex.getX(), countdownTex.getZ(anim.getX()), countdownTex.getW(anim.getY()), 1, 1, 1, 1);
		tileShader.setMvp(camera.getModelProjectionMatrix(camera.getModelMatrix(transform)));
		
		rect.render();
		
		Shader.disable();
		
		Texture.unbind();
	}
	
}