package game.scenes.game.groups;

import static game.scenes.game.GameGraphics.*;

import cnge.core.Behavior;
import cnge.core.Entity;
import cnge.core.group.EntityGroup;
import cnge.graphics.Camera;
import cnge.graphics.Shader;
import cnge.graphics.Transform;

public class Blackening extends EntityGroup<Blackening._Blackening> {

	public Blackening() {
		super(
			_Blackening.class, 
			1, 
			new Behavior<_Blackening>() {
				public _Blackening create(Object... p) {
					return new _Blackening();
				}
				public void update(_Blackening e) {
					
				}
				public void render(_Blackening e, Camera c) {
					colShader.enable();
					colShader.sendUniforms(0, 0, 0, e.alpha);
					colShader.setMvp(c.getModelProjectionMatrix(c.getModelMatrix(new Transform(c.getTransform().width, c.getTransform().height))));
					
					rect.render();
					
					Shader.disable();
				}
			}
		);
	}

	public static class _Blackening extends Entity {
		
		public float alpha;
		
		public _Blackening() {
			super();
			alpha = 1;
			setAlwaysOn(true);
		}
		
	}
	
}