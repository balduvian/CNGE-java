package embgine.graphics.shapes;

import org.lwjgl.opengl.GL11;

public class CurvedTriangle extends BezierShape{

	public CurvedTriangle() {
		super(
			new float[] {
				0, 0, 0,
				1, 1, 0,
				0, 1, 0
			}, 
			new int[] {
				0,
				1,
				2,
				0
			},
			new float[] {
				1,
				0,
				0,
			},
			new float[] {
				1, 0, /**/ 1, 0,
				
				0, 0, /**/ 0, 0,
				
				0, 0, /**/ 0, 0,
			}, 
			GL11.GL_TRIANGLES
		);
	}

}
