package game.scenes.game.groups;

import static game.scenes.game.GameGraphics.coinTex;
import static game.scenes.game.GameGraphics.rect;
import static game.scenes.game.GameGraphics.tileShader;

import org.joml.Vector4f;

import cnge.core.Base;
import cnge.core.Behavior;
import cnge.core.Entity;
import cnge.core.group.EntityGroup;
import cnge.graphics.Camera;
import cnge.graphics.Shader;
import cnge.graphics.Texture;

public class PlayerEntity extends EntityGroup<PlayerEntity.E, PlayerEntity> {

	public class E extends Entity{

		int frame;
		double time;
		
		public E() {
			super();
			frame = 0;
			time = 0;
		}

	}
	
	public PlayerEntity() {
		super(
			E.class,
			4, 
			new Behavior<E, PlayerEntity>() {
				public void spawn(E e) {
					e.frame = 0;
					e.time = 0;
					
					e.getTransform().setSize(256, 256);
				}
				public void update(E e) {
					e.time += Base.time;
					if(e.time >= 0.1666666666) {
						e.time = 0;
						++e.frame;
						e.frame %= 14;
					}
					
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
				public Entity create(PlayerEntity g, float x, float y, int l, Object... p) {
					return g.new E();
				}
			}
		);
	}

}