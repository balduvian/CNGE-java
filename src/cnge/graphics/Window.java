package cnge.graphics;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_DST_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_LEQUAL;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glViewport;

import static org.lwjgl.opengl.GL11.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import cnge.core.Resizer;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import org.lwjgl.openal.*;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.stb.STBVorbis.*;
import static org.lwjgl.system.MemoryStack.stackMallocInt;
import static org.lwjgl.system.MemoryStack.stackPop;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.libc.LibCStdlib.free;

public class Window {
	
	private long window;
	
	private int width;
	private int height;

	private long cursorID;
	
	private GLFWVidMode vidMode;
	
	private int fixedFrameRate;
	
	private Resizer resizer;
	
	public Window(boolean fu, String t, boolean r) {

		glfwInit();
		
		//glfwSetErrorCallback(GLFWErrorCallback.createPrint(System.err));
		//if(!glfwInit()) {
		//	System.err.println("GLFW Failed to Initialize");
		//	System.exit(-1);
		//}
		
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_RESIZABLE, r ? GLFW_TRUE: GLFW_FALSE);
		//glfwWindowHint(GLFW_DECORATED, GLFW_FALSE);
		
		vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());	

		fixedFrameRate = vidMode.refreshRate();
		
		if(fu) {
			width = vidMode.width();
			height = vidMode.height();
			window = glfwCreateWindow(width, height, t, glfwGetPrimaryMonitor(), 0);
		}else {
			width = vidMode.width()/2;
			height = vidMode.height()/2;
			window = glfwCreateWindow(width, height, t, 0, 0);
		}
		glfwMakeContextCurrent(window);
		createCapabilities();
		
		glfwSwapInterval(1);
		
		glfwSetWindowSizeCallback(window, 
			(long window, int ww, int hh) -> {
				width = ww;
				height = hh;
				glViewport(0, 0, width, height);
				resizer.resize(width, height);
			} 	
		);

		glEnable(GL30.GL_CLIP_DISTANCE0);
		glEnable(GL_TEXTURE_2D);
		
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}
	
	public void setResize(Resizer r) {
		resizer = r;
	}
	
	public void resetCursor() {
		glfwSetCursorPos(window, vidMode.width(), vidMode.height());
	}
	
	public void fullscreen(boolean full) {
		if(full) {
			if(fixedFrameRate == -1) {
				glfwSetWindowMonitor(window, glfwGetPrimaryMonitor(), 0, 0, vidMode.width(), vidMode.height(), vidMode.refreshRate());
			}else {
				glfwSetWindowMonitor(window, glfwGetPrimaryMonitor(), 0, 0, vidMode.width(), vidMode.height(), fixedFrameRate);
			}
		}else {
			if(fixedFrameRate == -1) {
				glfwSetWindowMonitor(window, 0, vidMode.width()/4, vidMode.height()/4, vidMode.width()/2, vidMode.height()/2, vidMode.refreshRate());
			}else {
				glfwSetWindowMonitor(window, 0, vidMode.width()/4, vidMode.height()/4, vidMode.width()/2, vidMode.height()/2, fixedFrameRate);
			}
		}
	}
	
	public int getRefreshRate() {
		return vidMode.refreshRate();
	}
	
	public void showCursor() {
		glfwSetCursor(window, cursorID);
		glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
	}
	
	public void hideCursor() {
		glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
	}
	
	public void setIcon(String imagePath) {
		
		GLFWImage image = Window.makeGLFWImage(imagePath);   
		GLFWImage.Buffer buffer = GLFWImage.malloc(1);
		buffer.put(0, image);
		
		glfwSetWindowIcon(window, buffer);
	}
	
	public static GLFWImage makeGLFWImage(String imagePath) {
		BufferedImage b = null;
		try {
			b = ImageIO.read(new File(imagePath));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		int bwi = b.getWidth();
		int bhi = b.getHeight();
		int len = bwi * bhi;
		
		int[] rgbArray = new int[len];
		
		System.out.println();
		
		b.getRGB(0, 0, bwi, bhi, rgbArray, 0, bwi);
		
		ByteBuffer buffer = BufferUtils.createByteBuffer(len * 4);
		
		for(int i = 0; i < len; ++i) {
			int rgb = rgbArray[i];
			buffer.put((byte)(rgb >> 16 & 0xff));
			buffer.put((byte)(rgb >>  8 & 0xff));
			buffer.put((byte)(rgb       & 0xff));
			buffer.put((byte)(rgb >> 24 & 0xff));
		}
		
		buffer.flip();
			
	    // create a GLFWImage
	    GLFWImage img= GLFWImage.create();
	    img.width(bwi);     // setup the images' width
	    img.height(bhi);   // setup the images' height
	    img.pixels(buffer);   // pass image data
	    
	    return img;
	}
	
	private void setCursor(String cursorPath) {
		GLFWImage cursorImg = makeGLFWImage(cursorPath);

	    // create custom cursor and store its ID
	    int hotspotX = 0;
	    int hotspotY = 0;
	    cursorID = GLFW.glfwCreateCursor(cursorImg, hotspotX , hotspotY);

	    // set current cursor
	    glfwSetCursor(window, cursorID);
	}
	
	public Vector3f getMouseCoords(Camera c) {
		double[] x = new double[1];
		double[] y = new double[1];
		glfwGetCursorPos(window, x, y);
		Vector3f ret = new Vector3f((float)x[0], (float)y[0], 0);
		Transform t = c.getTransform();
		ret.mul(t.getWidth()/width, t.getHeight()/height, 1);
		ret.sub(t.getWidth()/2, t.getHeight()/2, 0);
		return ret;
	}
	
	public boolean mousePressed(int button) {
		return glfwGetMouseButton(window, button) == GLFW_PRESS;
	}
	
	public boolean keyPressed(int keyCode) {
		return glfwGetKey(window, keyCode) == GLFW_PRESS;
	}
	
	public void update() {
		glfwPollEvents();
	}
	
	public static void clear() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}
	
	public void swap() {
		glfwSwapBuffers(window);
	}
	
	public void close() {
		glfwSetWindowShouldClose(window, true);
		//glfwTerminate();
	}
	
	public boolean shouldClose() {
		return glfwWindowShouldClose(window);
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setSize(int w, int h) {
		glfwSetWindowSize(window, w, h);
		glViewport(0,0,w,h);
		width = w;
		height = h;
	}
	
	public long get() {
		return window;
	}
}
