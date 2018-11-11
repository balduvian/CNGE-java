package game.scenes.game.groups;

import static game.scenes.game.GameGraphics.*;

import cnge.core.Behavior;
import cnge.core.Entity;
import cnge.core.animation.Anim2D;
import cnge.core.group.EntityGroup;
import cnge.core.morph.Morph;
import cnge.graphics.Camera;
import cnge.graphics.Shader;
import cnge.graphics.Transform;
import cnge.graphics.texture.Texture;

public class Countdown extends EntityGroup<Countdown.C> {

	public Countdown() {
		super(
			C.class, 
			1, 
			new Behavior<C>() {
				public C create(Object... p) {
					return new C();
				}
				public void update(C e) {
					e.anim.update();
					e.current.update();
				}
				public void render(C e, Camera c) {
					countdownTex.bind();
					
					tileShader.enable();
					tileShader.setUniforms(countdownTex.getX(), countdownTex.getX(), countdownTex.getZ(e.anim.getX()), countdownTex.getW(e.anim.getY()), 1, 1, 1, 1);
					tileShader.setMvp(c.getModelProjectionMatrix(c.getModelMatrix(e.getTransform())));
					
					rect.render();
					
					Shader.disable();
					
					Texture.unbind();
				}
			}
		);
	}

	public static class C extends Entity {
		
		public Anim2D anim;
		public Morph grow;
		public Morph shrink;
		
		public Morph current;
		
		public C() {
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
					1,
					0.5
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
				grow.reset();
			});
			anim.addEvent(4, () -> {
				shrink.reset();
				current = shrink;
			});
			anim.addEvent(0, () -> {
				group.destroyInstance(index);
			});
			transform.setSize(256, 128);
			transform.setScale(0, 0);
			grow = new Morph(transform, Morph.OVERCIRCLE, 1).addScaleW(1).addScaleH(1);
			shrink = new Morph(transform, Morph.UNDERCIRCLE, 0.5).addScaleW(0).addScaleH(0);
			setAlwaysOn(true);
			current = grow;
		}
		
	}
	
}