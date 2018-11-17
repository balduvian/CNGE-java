package cnge.core;

import cnge.graphics.Camera;
import cnge.graphics.FBO;
import cnge.graphics.Transform;
import cnge.graphics.texture.Texture;

abstract public class Map<B extends Block> extends Entity {
	
	public static final int NO_BLOCK = -1;
	
	protected B[] blockSet;
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
	
	public void mapSetup(float x, float y, B[] bs, int[][] t) {
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
		int access(Map<?> m, int x, int y) throws MapAccessException;
	}
	
	public int access(int x, int y) throws MapAccessException {
		return access.access(this, x, y);
	}
	
	public B block(int b) throws NullBlockException {
		if(b == -1) {
			throw new NullBlockException();
		}else {
			return blockSet[b];
		}
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
		return x * scale * transform.wScale + transform.abcissa;
	}
	
	/**
	 * converts map coordinates to world coordinates
	 * @param y - y in map coordinates
	 * @return y in world coordinates (TOP SIDE)
	 */
	public float getY(int y) {
		return y * scale * transform.hScale + transform.ordinate;
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
	
	public Block[] getSet() {
		return blockSet;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public boolean onScreenUpdate(Camera c) {
		if(!(onScreen = alwaysOn)) {
			float ex = transform.    abcissa;
			float ey = transform.   ordinate;
			float ew = transform. getWidth();
			float eh = transform.getHeight();
			
			Transform cTransform = c.getTransform();
			
			float cx = cTransform.    abcissa;
			float cy = cTransform.   ordinate;
			float cw = cTransform. getWidth();
			float ch = cTransform.getHeight();
			
			onScreen = (ex + ew > cx) && (ex < cx + cw) && (ey + eh > cy) && (ey < cy + ch);
		}
		
		Transform ct = camera.getTransform();
		
		 left  = (int)Math.floor( ( ct.abcissa - transform.abcissa) / (scale * transform.wScale) );
		 up    = (int)Math.floor( ( ct.ordinate - transform.ordinate) / (scale * transform.wScale) );
		   
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
		return (int)((x-transform.abcissa) * width / transform.width);
	}
	
	/**
	 * gets the map coordinate correspoding to a world coordinate
	 * 
	 * @param y - y pposition in the world
	 * 
	 * @return the integer y position in map coordinates 
	 */
	public int atY(float y) {
		return (int)((y-transform.ordinate) * height / transform.height);
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
	 * @throws MapAccessException
	 * 
	 * @see atX()
	 * @see atY()
	 */
	public int boundedAccess(int x, int y) throws MapAccessException {
		try {
			return tiles[x][y];
		} catch(ArrayIndexOutOfBoundsException ex) {
			throw new MapAccessException();
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
	public int edgeAccess(int x, int y) {
		try {
			return tiles[x][y];
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
			return tiles[x][y];
		}
	}
	
	/**
	 * accesses the map but it extends horizontally in both directions
	 * 
	 * @param x - map coordinate
	 * @param y - map coordinate
	 * 
	 * @return the block there
	 * @throws MapAccessException
	 * 
	 * @see atX()
	 * @see atY()
	 */
	public int horzEdgeAccess(int x, int y) throws MapAccessException {
		try {
			return tiles[x][y];
		} catch(ArrayIndexOutOfBoundsException ex) {
			if(x < 0) {
				x = 0;
			} else if(x >= width) {
				x = width - 1;
			}
			try {
				return tiles[x][y];
			} catch(ArrayIndexOutOfBoundsException e) {
				throw new MapAccessException();
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
	 * @throws MapAccessException
	 * 
	 * @see atX()
	 * @see atY()
	 */
	public int vertEdgeAccess(int x, int y) throws MapAccessException {
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
				throw new MapAccessException();
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
	 * @throws MapAccessException
	 * 
	 * @see atX()
	 * @see atY()
	 */
	public int repeatHorzAccess(int x, int y) throws MapAccessException {
		try {
			return tiles[x][y];
		} catch(ArrayIndexOutOfBoundsException ex) {
			x %= width;
			try {
				return tiles[x][y];
			} catch(ArrayIndexOutOfBoundsException e) {
				throw new MapAccessException();
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
	public static class MapAccessException extends Exception {
		private static final long serialVersionUID = 9197260479519042104L;
	}
	
	/**
	 * will be thrown when you try to access a block and there's nothing there
	 * 
	 * @author Emmet
	 */
	public static class NullBlockException extends Exception {
		private static final long serialVersionUID = 9197260478519042104L;
	}
	
	public void render() {
		//haha you get nothing
		//but seriously, this is not meant to be used by maps
		//use the level's mapRender instead
	}
	
}