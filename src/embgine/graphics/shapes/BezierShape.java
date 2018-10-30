package embgine.graphics.shapes;

/**
 * a shape that supports bezier curves between vertexes
 */
abstract public class BezierShape extends Shape{

	/**
	 * makes the texture shape
	 * 
	 * @param vertices - list of vertices
	 * @param indices - the order in which the vertices are connected
	 * @param enables - whether there's a curve on this vertex or not
	 * @param handles - the x,y for two handles for the bezier curve on this vertex
	 * @param drawMode - points, lines, or triangles
	 */
	public BezierShape(float[] vertices, int[] indices, float[] enables, float[] handles, int drawMode) {
		super(3, vertices, indices, drawMode);
		vbo.addAttrib(enables, 1);
		vbo.addAttrib(handles, 4);
	}

}