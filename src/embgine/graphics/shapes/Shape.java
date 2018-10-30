package embgine.graphics.shapes;

import embgine.graphics.Camera;
import embgine.graphics.VBO;

abstract public class Shape {
	
	protected VBO vbo;
	
	public static RectShape RECT;
	public static ArrowShape ARROW;
	
	public static void init(Camera c) {
		RECT = new RectShape();
		ARROW = new ArrowShape();
	}
	
	public Shape(int numAttribs, float vertices[], int[] indices, int drawMode) {
		vbo = new VBO(numAttribs, vertices, indices, drawMode);
	}
	
	public void render() {
		vbo.render();
	}
	
}