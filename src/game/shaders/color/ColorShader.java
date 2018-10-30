package game.shaders.color;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform4f;

import embgine.graphics.Shader;

/**
 * draws a simple single color into a shape
 */
public class ColorShader extends Shader {

	private int colorLoc;
	
	public ColorShader() {
		super("bezier/shaders/color/col2d.vs", "bezier/shaders/color/col2d.fs");
		
		colorLoc = glGetUniformLocation(program, "color");
	}
	
	/**
	 * sends uniforms to the color shader after being enabled
	 * 
	 * @param r - red value
	 * @param g - green value
	 * @param b - blue value
	 * @param a - alpha value
	 */
	public void sendUniforms(float r, float g, float b, float a) {
		glUniform4f(colorLoc, r, g, b, a);
	}

}
