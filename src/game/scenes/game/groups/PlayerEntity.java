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

public class PlayerEntity extends EntityGroup<PlayerEntity._PlayerEntity> {

	public class _PlayerEntity extends Entity{

		int frame;
		double time;
		
		public _PlayerEntity(PlayerEntity g, float x, float y, int l) {
			super(g, x, y, l);
		}

	}
	
	public PlayerEntity() {
		super(
			_PlayerEntity.class,
			0, 
			new Behavior<_PlayerEntity>() {
				public void spawn(_PlayerEntity e) {
					e.frame = 0;
					e.time = 0;
					
					e.getTransform().setSize(256, 256);
				}
				public void update(_PlayerEntity e) {
					e.time += Base.time;
					if(e.time >= 0.666666666) {
						e.time = 0;
						++e.frame;
						e.frame %= 14;
					}
					
				}
				public void render(_PlayerEntity e, Camera c) {
					Vector4f frame = coinTex.getFrame(e.frame);
					
					coinTex.bind();
					
					tileShader.enable();
					
					tileShader.setUniforms(frame.x, frame.y, frame.z, frame.w, 1, 1, 1, 1);
					tileShader.setMvp(c.getModelViewProjectionMatrix(c.getModelMatrix(e.getTransform())));
					
					rect.render();
					
					Shader.disable();
					
					Texture.unbind();
				}
			}
		);
	}

}