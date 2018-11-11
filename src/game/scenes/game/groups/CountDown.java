package game.scenes.game.groups;

import static game.scenes.game.GameGraphics.*;

import cnge.core.Behavior;
import cnge.core.Entity;
import cnge.core.animation.Anim2D;
import cnge.core.group.EntityGroup;
import cnge.graphics.Camera;
import cnge.graphics.Shader;
import cnge.graphics.Transform;

public class CountDown extends EntityGroup<CountDown.C> {

	public CountDown() {
		super(
			C.class, 
			1, 
			new Behavior<C>() {
				public C create(Object... p) {
					return new C();
				}
				public void update(C e) {
					
				}
				public void render(C e, Camera c) {
					colShader.enable();
					colShader.sendUniforms(0, 0, 0, e.alpha);
					colShader.setMvp(c.getModelProjectionMatrix(c.getModelMatrix(new Transform(c.getTransform().width, c.getTransform().height))));
					
					rect.render();
					
					Shader.disable();
				}
			}
		);
	}

	public static class C extends Entity {
		
		public float alpha;
		public Anim2D anim;
		
		public C() {
			super();
			alpha = 1;
			anim = new Anim2D(
				new int[][] {
					
				},
				new double[] {
						
				}
			);
			setAlwaysOn(true);
		}
		
	}
	
}