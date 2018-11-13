package game.scenes.game.groups;

import cnge.core.Entity;
import cnge.core.animation.Anim1D;
import cnge.graphics.Shader;
import cnge.graphics.Transform;
import cnge.graphics.texture.Texture;

import static game.scenes.game.GameGraphics.*;

public class Sky extends Entity {

	public static final float parallax = 0.1f;
	
	public Anim1D skyAnim;
		
	public Sky() {
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

	public void update() {
		skyAnim.update();
	}
	
	public void render() {
		skyTex.bind();
		
		tileShader.enable();
		
		Transform ct = camera.getTransform();
		
		tileShader.setUniforms(0.25f * (ct.width / transform.width), (ct.height / transform.height), (float)(ct.abcissa * parallax - transform.abcissa) / ct.getWidth() + (skyAnim.getX() * 0.25f), (float)(ct.ordinate * parallax - transform.ordinate) / ct.getHeight(), 1, 1, 1, 1);
		
		tileShader.setMvp(camera.getModelViewProjectionMatrix(camera.getModelMatrix(camera.getTransform())));
		
		rect.render();
		
		Shader.disable();
		
		Texture.unbind();
	}
}