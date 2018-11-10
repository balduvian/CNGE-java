package game.scenes.game.groups;

import cnge.core.Behavior;
import cnge.core.Entity;
import cnge.core.animation.Anim1D;
import cnge.core.group.EntityGroup;
import cnge.graphics.Camera;
import cnge.graphics.Shader;
import cnge.graphics.Transform;
import cnge.graphics.texture.Texture;

import static game.scenes.game.GameGraphics.*;

public class SparkBackground extends EntityGroup<SparkBackground.B>{

	public static final float parallax = 0.1f;
	
	public static class B extends Entity{
		
		public Anim1D skyAnim;
		
		public B() {
			super();
			transform.setSize(256, 256);
			setAlwaysOn(true);
			skyAnim = new Anim1D(
				new int[] {
					0,
					1,
					2,
					3,
				},
				new double[] {
					0.8,
					0.8,
					0.8,
					0.8,
				}
			);
		}
		
	}
	
	public SparkBackground() {
		super(
			B.class,
			1,
			new Behavior<B>() {
				public Entity create(Object... p) {
					return new B();
				}

				public void update(B e) {
					e.skyAnim.update();
				}

				public void render(B e, Camera c) {
					skyTex.bind();
					
					tileShader.enable();
					
					Transform t = e.getTransform();
					Transform ct = c.getTransform();
					
					tileShader.setUniforms(0.25f * (ct.width / t.width), (ct.height / t.height), (float)(ct.abcissa * parallax - t.abcissa) / ct.getWidth() + (e.skyAnim.getX() * 0.25f), (float)(ct.ordinate * parallax - t.ordinate) / ct.getHeight(), 1, 1, 1, 1);
					
					tileShader.setMvp(camera.getModelViewProjectionMatrix(camera.getModelMatrix(camera.getTransform())));
					
					rect.render();
					
					Shader.disable();
					
					Texture.unbind();
				}
				
			}
		);
	}

}