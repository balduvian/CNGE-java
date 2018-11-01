package embgine.graphics;

import embgine.graphics.shapes.ArrowShape;
import embgine.graphics.shapes.RectShape;

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