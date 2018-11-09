package cnge.core;

import cnge.core.group.MapGroup;

public class Map extends Entity{
	
	public static final int NO_BLOCK = -1;
	
	private Block[] set;
	private int[][] tiles;
	private int width;
	private int height;
	
	private int up;
	private int left;
	private int down;
	private int right;
	
	private Access access;
	
	private float scale;
	
	public Map(Access a, float s) {
		access = a;
		scale = s;
	}
	
	public void mapSetup(float x, float y, int l, MapGroup<?> g, int[][] t) {
		setup(x, y, l, g);
		tiles = t;
		width = t.length;
		height = t[0].length;
		transform.setSize(width * scale, height * scale);
		set = g.getBlockSet();
	}
	
	public interface Access {
		int access(Map m, int x, int y) throws MapAccessException;
	}
	
	public int access(int x, int y) throws MapAccessException {
		return access.access(this, x, y);
	}
	
	public Block block(int b) {
		return set[b];
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
	public void setUp(int u) {
		up = u;
	}
	public void setLeft(int l) {
		left = l;
	}
	public void setDown(int d) {
		down = d;
	}
	public void setRight(int r) {
		right = r;
	}
	
	public int[][] getTies() {
		return tiles;
	}
	
	public int get(int x, int y) {
		return tiles[x][y];
	}
	
	public Block[] getSet() {
		return set;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
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
	public class MapAccessException extends Exception {
		private static final long serialVersionUID = 9197260479519042104L;
	}
	
}