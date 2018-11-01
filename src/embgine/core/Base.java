//gatcuddy <emmettglaser@gmail.com>

package embgine.core;

import static org.lwjgl.opengl.GL11.glClearColor;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import static org.lwjgl.opengl.GL11.*;

import embgine.graphics.ALManagement;
import embgine.graphics.Camera;
import embgine.graphics.FBO;
import embgine.graphics.Shader;
import embgine.graphics.Texture;
import embgine.graphics.Transform;
import embgine.graphics.Window;
import embgine.graphics.shapes.Shape;

/**
 * @author Emmet
 */
public class Base {
	
	public static int fps;
	public static double time;
	public static long nanos;
	
	protected Window window;
	protected Camera camera;
	protected ALManagement audio;

	private int frameRate;
	
	private Scene scene;
	
	private BaseShader baseShader;
	
	private FBO screenBuffer;
	
	private boolean fullWidth;
	
	private int gameScreenType;
	private boolean gamePixelType;
	
	private int frameWidth;
	private int frameHeight;
	
	private int gameUnit;
	
	private int gameWidth;
	private int gameHeight;
	private int gameLimit;
	
	/**
	 * The base of the entire program. Create one in your own main method
	 * 
	 * @param win - the window
	 * @param sm - screen mode for the window |
	 * 
	 * {@link #SCREEN_PIXEL} |
	 * {@link #ASPECT_STRETCH_HORZ} |
	 * {@link #ASPECT_STRETCH_VERT} |
	 * {@link #ASPECT_FIXED} |
	 * 
	 * @param am - aspect mode modifiers |
	 * 
	 * {@link #UNIT_GAME_PIXELS} |
	 * {@link #UNIT_SCREEN_PIXELS} |
	 * 
	 * @return the base
	 */
	public Base(Window win, BasePreset scrp) {
		window = win;
		
		frameRate = window.getRefreshRate();
		
		camera = new Camera(window.getWidth(), window.getHeight());
		
		screenBuffer = new FBO(new Texture());
		
		baseShader = new BaseShader();
		
		Shape.init(camera);
		
		audio = new ALManagement();
		
		gameScreenType = scrp.screenType;
		gamePixelType = scrp.pixelType;
		gameWidth = scrp.width;
		gameHeight = scrp.height;
		gameUnit = scrp.unit;
		gameLimit = scrp.limit;
		
		window.setResize(
			(w, h) -> {
				camera.setDims(w, h);
				reFrame(w, h);
			}
		);
	}
	
	/**
	 * this is the last thing you call in main to start the game
	 * 
	 * @param s - the scene to start on
	 */
	public void start(Scene s) {
		scene = s;
		
		scene.giveCamera(camera);
		
		reFrame(window.getWidth(), window.getHeight());
		
		gameLoop();
	}
	
	/**
	 * called when the window is resized, sets the renderable frame inside the window based on the screen mode
	 * 
	 * @param w - auto put in, the width of the window
	 * @param h - auto put in the height of the window
	 */
	private void reFrame(int w, int h) {
		//alrighty, now let's set the frame that will be rendered in the window based on the height and width of the window
		switch(gameScreenType) {
			case BasePreset.SCREEN_PIXEL:
				//the frame will be the same size as the window
				frameWidth = w;
				frameHeight = h;
				break;
			case BasePreset.ASPECT_STRETCH_HORZ:
				
				fullWidth = false;
				
				float windowRatio = (float)window.getWidth() / window.getHeight();
				
				frameHeight = h;
				
				gameWidth = (int)Math.round(windowRatio * gameHeight);
				if(gameWidth > gameLimit) {
					gameWidth = gameLimit;
					frameWidth = (int)Math.round(window.getWidth() * ( (float)(gameWidth / gameHeight) / (windowRatio) ));
				}else {
					frameWidth = w;
				}
				
				break;
			case BasePreset.ASPECT_STRETCH_VERT:
				
				fullWidth = true;
				
				windowRatio = (float)window.getHeight() / window.getWidth();
				
				frameWidth = w;
				
				gameHeight = (int)Math.round(windowRatio * gameWidth);
				if(gameHeight > gameLimit) {
					gameHeight = gameLimit;
					frameHeight = (int)Math.round(window.getHeight() * ( (float)(gameHeight / gameWidth) / (windowRatio) ));
				}else {
					frameHeight = h;
				}
				
				break;
			case BasePreset.ASPECT_FIXED:
				windowRatio = (float)window.getWidth() / window.getHeight();
				
				float gameRatio = (float)gameWidth / gameHeight;
				
				if(windowRatio < gameRatio) {
					fullWidth = true;
					frameWidth = window.getWidth();
					frameHeight = (int)(window.getHeight() * (windowRatio/gameRatio));
				}else {
					fullWidth = false;
					frameWidth = (int)(window.getWidth() * (gameRatio/windowRatio));
					frameHeight = window.getHeight();
				}
				
				break;
		}
		
		if(gamePixelType) {
			screenBuffer.replaceTexture(new Texture(gameWidth, gameHeight));
		} else {
			screenBuffer.replaceTexture(new Texture(frameWidth, frameHeight));
		}
		
		scene.giveDims(w, h);
		scene.resizeUpdate();
	}
	
	public void gameLoop() {
		long usingFPS = 1000000000 / frameRate;
		long last = System.nanoTime();
		long lastSec = last;
		int frames = 0;
		while(!window.shouldClose()) {
			long now = System.nanoTime();
			if(now-last > usingFPS) {
				nanos = (now-last);
				time = (nanos)/1000000000d;
				update();
				render();
				window.swap();
				last = now; 
				++frames;
			}
			if(now-lastSec > 1000000000) {
				fps = frames; 
				frames = 0;
				lastSec = now;
				System.out.println(fps);
			}
		}
		audio.destroy();
	}
	
	private void update() {
		window.update();
		
		scene.update();
		
		camera.update();
	}
	
	private void render() {
		
		//start up that mf game screenBuffer
		screenBuffer.enable();
		
		glClearColor(0, 0, 0, 1);
		
		Window.clear();
		
		scene.render();
		
		FBO.disable(window);
		
		glClearColor(0, 0, 0, 1);
		Window.clear();
		
		Shape rect = Shape.RECT;
		
		screenBuffer.getBoundTexture().bind();
		
		baseShader.enable();
		
		baseShader.setMvp(camera.ndcFullMatrix());
		
		if(fullWidth) {
			glViewport(0, window.getHeight()/2 - frameHeight/2, frameWidth, frameHeight);
		} else {
			glViewport(window.getWidth()/2 - frameWidth/2, 0, frameWidth, frameHeight);
		}
		
		rect.render();
		baseShader.disable();
		
		screenBuffer.getBoundTexture().unbind();
		
		////////////
		
		
	}
	
}