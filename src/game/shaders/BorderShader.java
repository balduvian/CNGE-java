package game.shaders;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform4f;

import embgine.graphics.Shader;

import static org.lwjgl.opengl.GL20.glUniform1f;

public class BorderShader extends Shader{
	
	public static final float OFFSET_CENTERED = 0;
	public static final float OFFSET_INSIDE = 1;
	public static final float OFFSET_OUTSIDE = 2;
		
	private int fillColorLoc;
	private int borderColorLoc;
	private int windowWidthLoc;
	private int windowHeightLoc;
	private int leftLoc;
	private int upLoc;
	private int widthLoc;
	private int heightLoc;
	private int borderWidthLoc;
	private int offsetLoc;
	
	public BorderShader() {
		super("res/shaders/border/bor2d.vs", "res/shaders/border/bor2d.fs");
		
		fillColorLoc = glGetUniformLocation(program, "fillColor");
		borderColorLoc = glGetUniformLocation(program, "borderColor");
		windowWidthLoc = glGetUniformLocation(program, "windowWidth");
		windowHeightLoc = glGetUniformLocation(program, "windowHeight");
		leftLoc = glGetUniformLocation(program, "left");
		upLoc = glGetUniformLocation(program, "up");
		widthLoc = glGetUniformLocation(program, "width");
		heightLoc = glGetUniformLocation(program, "height");
		borderWidthLoc = glGetUniformLocation(program, "borderWidth");
		offsetLoc = glGetUniformLocation(program, "offset");
	}

	/**
	 * passes uniforms into border shader
	 * 
	 * @param fcr - fill color red
	 * @param fcg - fill color green
	 * @param fcb - fill color blue
	 * @param fca - fill color alpha
	 * @param bcr - border color red
	 * @param bcg - border color green
	 * @param bcb - border color blue
	 * @param bca - border color alpha
	 * @param ww - window width
	 * @param wh - window height
	 * @param l - left edge in pixels
	 * @param u - top edge in pixels
	 * @param w - width of box in pixels
	 * @param h - height of box in pixels
	 * @param bw - width of border in pixels
	 * @param o - offset mode for the border | 0: centered | 1: inside | 2: outside
	 */
	public void sendUniforms(float fcr, float fcg, float fcb, float fca, float bcr, float bcg, float bcb, float bca, float ww, float wh, float l, float u, float w, float h, float bw, float o) {
		glUniform4f(fillColorLoc, fcr, fcg, fcb, fca);
		glUniform4f(borderColorLoc, bcr, bcg, bcb, bca);
		glUniform1f(windowWidthLoc, ww);
		glUniform1f(windowHeightLoc, wh);
		glUniform1f(leftLoc, l);
		glUniform1f(upLoc, u);
		glUniform1f(widthLoc, w);
		glUniform1f(heightLoc, h);
		glUniform1f(borderWidthLoc, bw);
		glUniform1f(offsetLoc, o);
	}

}