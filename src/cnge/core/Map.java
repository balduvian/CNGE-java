package cnge.core;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;

import cnge.graphics.Camera;
import cnge.graphics.FBO;
import cnge.graphics.Transform;
import cnge.graphics.texture.Texture;

abstract public class Map<B extends Block> extends Entity {
	
	public static final int NO_BLOCK = -1;
	
	protected BlockSet<B> blockSet;
	private int[][] tiles;
	private int width;
	private int height;
	
	protected int up;
	protected int left;
	protected int down;
	protected int right;
	
	protected int acr;
	protected int dow;
	
	private Access access;
	
	protected int scale;
	
	private FBO mapBuffer;
	
	public Map(Access a, int s) {
		access = a;
		scale = s;
		mapBuffer = new FBO();
		getOnScreenDims();
	}
	
	public void mapSetup(float x, float y, BlockSet<B> bs, int[][] t) {
		setup(x, y);
		tiles = t;
		width = t.length;
		height = t[0].length;
		blockSet = bs;
		transform.setSize(width * scale, height * scale);
	}
	
	/**
	 * called once per block
	 */
	abstract public void blockRender(int l, int x, int y, float left, float right, float up, float down);
	
	/**
	 * renders the map texture to the main buffer
	 * 
	 * @param t - the texture from the map buffer
	 */
	abstract public void mapRender(Transform t, Texture tx);
	
	public interface Access {
		Object access(Object[][] a, int x, int y) throws AccessException;
	}
	
	public Object access(Object[][] a, int x, int y) throws AccessException {
		return access.access(a, x, y);
	}
	
	public BlockSet<B> getBlockSet(){
		return blockSet;
	}
	
	public void getOnScreenDims() {
		Transform t = camera.getTransform();
		acr = (int)Math.ceil(t.getWidth() / scale) + 1;
		dow = (int)Math.ceil(t.getHeight() / scale) + 1;
		mapBuffer.replaceTexture(new Texture(acr * scale, dow * scale));
	}
	
	/**
	 * converts map coordinates to world coordinates
	 * @param x - x in map coordinates
	 * @return x in world coordinates (LEFT SIDE)
	 */
	public float getX(int x) {
		return x * scale * transform.wScale + transform.x;
	}
	
	/**
	 * converts map coordinates to world coordinates
	 * @param y - y in map coordinates
	 * @return y in world coordinates (TOP SIDE)
	 */
	public float getY(int y) {
		return y * scale * transform.hScale + transform.y;
	}
	
	public float getScale() {
		return scale;
	}
	
	public int getUp() {
		return up;
	}
	
	public int getLeft() {
		return left;
	}
	
	public int getDown() {
		return down;
	}
	
	public int getRight() {
		return right;
	}
	
	public int[][] getTies() {
		return tiles;
	}
	
	public int get(int x, int y) {
		return tiles[x][y];
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public boolean onScreenUpdate() {
		Transform ct = camera.getTransform();
		
		if(!(onScreen = alwaysOn)) {
			float ex = transform.x;
			float ey = transform.y;
			float ew = transform. getWidth();
			float eh = transform.getHeight();
			
			float cx = ct.x;
			float cy = ct.y;
			float cw = ct. getWidth();
			float ch = ct.getHeight();
			
			onScreen = (ex + ew > cx) && (ex < cx + cw) && (ey + eh > cy) && (ey < cy + ch);
		}
		
		left  = (int)Math.floor( ( ct.x - transform.x) / (scale * transform.wScale) );
		up    = (int)Math.floor( ( ct.y - transform.y) / (scale * transform.wScale) );
		   
		right = left + acr;
		down  = up + dow;
		
		return onScreen;
	}
	
	/**
	 * gets the map coordinate correspoding to a world coordinate
	 * 
	 * @param x - x pposition in the world
	 * 
	 * @return the integer x position in map coordinates 
	 */
	public int atX(float x) {
		return (int)((x-transform.x) * width / transform.width);
	}
	
	/**
	 * gets the map coordinate correspoding to a world coordinate
	 * 
	 * @param y - y pposition in the world
	 * 
	 * @return the integer y position in map coordinates 
	 */
	public int atY(float y) {
		return (int)((y-transform.y) * height / transform.height);
	}
	
	public FBO getMapBuffer() {
		return mapBuffer;
	}
	
	/**
	 * accesses the map only within its bounds
	 * 
	 * @param x - map coordinate
	 * @param y - map coordinate
	 * 
	 * @return the block there
	 * @throws AccessException
	 * 
	 * @see atX()
	 * @see atY()
	 */
	public int boundedAccess(int[][] a, int x, int y) throws AccessException {
		try {
			return a[x][y];
		} catch(ArrayIndexOutOfBoundsException ex) {
			throw new AccessException();
		}
	}
	
	/**
	 * accesses the map but each edge repeats
	 * 
	 * @param x - map coordinate
	 * @param y - map coordinate
	 * 
	 * @return the block there
	 * 
	 * @see atX()
	 * @see atY()
	 */
	public int edgeAccess(int[][] a, int x, int y) {
		try {
			return a[x][y];
		} catch(ArrayIndexOutOfBoundsException ex) {
			if(x < 0) {
				x = 0;
			} else if(x >= width) {
				x = width - 1;
			}
			if(y < 0) {
				y = 0;
			} else if(y >= height) {
				y = height - 1;
			}
			return a[x][y];
		}
	}
	
	/**
	 * accesses the map but it extends horizontally in both directions
	 * 
	 * @param x - map coordinate
	 * @param y - map coordinate
	 * 
	 * @return the block there
	 * @throws AccessException
	 * 
	 * @see atX()
	 * @see atY()
	 */
	public int horzEdgeAccess(int[][] a, int x, int y) throws AccessException {
		try {
			return a[x][y];
		} catch(ArrayIndexOutOfBoundsException ex) {
			if(x < 0) {
				x = 0;
			} else if(x >= width) {
				x = width - 1;
			}
			try {
				return a[x][y];
			} catch(ArrayIndexOutOfBoundsException e) {
				throw new AccessException();
			}
		}
	}
	
	/**
	 * accesses the map but it extends vertically in both directions
	 * 
	 * @param x - map coordinate
	 * @param y - map coordinate
	 * 
	 * @return the block there
	 * @throws AccessException
	 * 
	 * @see atX()
	 * @see atY()
	 */
	public int vertEdgeAccess(int x, int y) throws AccessException {
		try {
			return tiles[x][y];
		} catch(ArrayIndexOutOfBoundsException ex) {
			if(y < 0) {
				y = 0;
			} else if(y >= height) {
				y = height - 1;
			}
			try {
				return tiles[x][y];
			} catch(ArrayIndexOutOfBoundsException e) {
				throw new AccessException();
			}
		}
	}
	
	/**
	 * accesses the map but it's cloned horizontally
	 * 
	 * @param x - map coordinate
	 * @param y - map coordinate
	 * 
	 * @return the block there
	 * @throws AccessException
	 * 
	 * @see atX()
	 * @see atY()
	 */
	public int repeatHorzAccess(int x, int y) throws AccessException {
		try {
			return tiles[x][y];
		} catch(ArrayIndexOutOfBoundsException ex) {
			x %= width;
			try {
				return tiles[x][y];
			} catch(ArrayIndexOutOfBoundsException e) {
				throw new AccessException();
			}
		}
	}
	
	/**
	 * accesses the map but it's cloned everywhere
	 * 
	 * @param x - map coordinate
	 * @param y - map coordinate
	 * 
	 * @return the block there
	 * 
	 * @see atX()
	 * @see atY()
	 */
	public int repeatAllAccess(int x, int y) {
		try {
			return tiles[x][y];
		} catch(ArrayIndexOutOfBoundsException ex) {
			x %= width;
			y %= height;
			return tiles[x][y];
		}
	}
	
	/**
	 * will be thrown when you try and access a map and it's outside the map bounds
	 * 
	 * @author Emmet
	 */
	public static class AccessException extends Exception {
		private static final long serialVersionUID = 9197260479519042104L;
	}
	
	public void render(int layer) {
		
		mapBuffer.enable();
		camera.setDims(acr * scale, dow * scale);
		
		//clear the buffer
		glClearColor(0, 0, 0, 0);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		for(int x = 0; x < acr; ++x) {
			for(int y = 0; y < dow; ++y) {
				blockRender(layer, x + left, y + up, x * scale, (x + 1) * scale, y * scale, (y + 1) * scale);
			}
		}
		
		Base.screenBuffer.enable();
		camera.defaultDims();
        
		mapRender(new Transform(left * scale, up * scale, acr * scale, dow * scale), mapBuffer.getTexture());
	}
	
	/**
	 * the default inherited render method from entity is not used
	 * 
	 * @deprecated FUCK FUCK FUCK FUCK DONT USE THIS METHOD
	 * 
	 * THIS WILL CLOSE DOWN YOUR PROGRAM
	 */
	public void render() {
		System.err.println("I TOLD YOU NOT TO USE THIS YOU SKEKER");
		System.exit(-3);
	}
	
}