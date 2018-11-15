package cnge.core;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import cnge.core.Map.MapAccessException;
import cnge.graphics.Camera;
import cnge.graphics.FBO;
import cnge.graphics.Transform;
import cnge.graphics.texture.Texture;
import cnge.graphics.texture.TexturePreset;

public abstract class MapGroup<M extends Map> {

	private int sections;
	
	protected int[][][] tiles;
	protected int[] widths;
	protected int[] heights;
	
	private String[] mapImages;
	
	protected Block[] blockSet;
	
	private FBO mapBuffer;
	
	/**
	 * constructs a new mapgroup for the scene
	 * 
	 * @param ip - the paths to the map loader images
	 * @param bs - the blockset for the maps
	 */
	public MapGroup(String[] ip, Block[] bs) {
		sections = ip.length;
		mapImages = ip;
		blockSet = bs;
		mapBuffer = new FBO();
	}
	
	/**
	 * constructs a new mapgroup for the scene with only 1 section
	 * 
	 * @param ip - the paths to the map loader images
	 * @param bs - the blockset for the maps
	 */
	public MapGroup(String ip, Block[] bs) {
		sections = 1;
		mapImages = new String[] {ip};
		blockSet = bs;
		mapBuffer = new FBO();
	}

	/**
	 * BOIIIIIIIIIII TODO
	 */
	public M createMaps(int i, float x, float y, Object... params) {
		int w = widths[i];
		int h = heights[i];
		int[][] take = tiles[i];
		int[][] give = new int[w][h];
		/*
		 * we make a clone array that we give to the map instance
		 */
		for(int j = 0; j < w; ++j) {
			for(int k = 0; k < h; ++k) {
				give[j][k] = take[j][k];
			}
		}
		M create = mapCreate(i, params);
		create.mapSetup(x, y, blockSet, give);
		return create;
	}
	
	/**
	 * use this instead of the {@link EntityGroup} one
	 * 
	 * creates an instance of the single map section belonging to this group
	 * 
	 * @param x - the x position of the map
	 * @param y - the y position of the map
	 * @param l - the layer the map is on
	 * 
	 * @return returns the map, NULL if could not create
	 */
	public M createMap(float x, float y, Object... params) {
		int w = widths[0];
		int h = heights[0];
		int[][] take = tiles[0];
		int[][] give = new int[w][h];
		/*
		 * we make a clone array that we give to the map instance
		 */
		for(int j = 0; j < w; ++j) {
			for(int k = 0; k < h; ++k) {
				give[j][k] = take[j][k];
			}
		}
		M create = mapCreate(0, params);
		create.mapSetup(x, y, blockSet, give);
		return create;
	}
	
	public Block[] getBlockSet() {
		return blockSet;
	}
	
	abstract public M mapCreate(int i, Object... params);
	
	/**
	 * overrides render from entity group 
	 * 
	 * @param m - the map to render
	 * @param layer - the current render layer
	 */
	public void render(Map m, int layer) {
		
		int u = m.getUp();
		int r = m.getRight();
		int d = m.getDown();
		int l = m.getLeft();
		
		//System.out.println(l);
		
		int acr = (r - l);
		int dow = (d - u);
		
		//System.out.println(acr + " " + dow);
		
		int size = (int)(m.getScale());
		mapBuffer.replaceTexture(new Texture(acr * size, dow * size));
		mapBuffer.enable();
		
		//System.out.println(m.getScale() + " " + size + " | " + l + " " + r + " " + d + " " + u);
		//System.out.println("width" + mapBuffer.getBoundTexture().getWidth());
		//int times = 0;
		for(int x = 0; x < acr; ++x) {
			//++times;
			for(int y = 0; y < dow; ++y) {
				try {
					int tile = m.access(x, y);
					if(tile != -1) {
						if(blockSet[tile].layer == layer) {
							m.blockRender(tile, x - l, y - u, x * size, (x + 1) * size, y * size, (y + 1) * size);
						}
						//System.out.println("left: " + (x)*size + " right: " + (x+1)*size);
					}
				} catch (MapAccessException ex) { }
			}
			//System.exit(-1);
		}
		
		//System.out.println(times);
		//System.exit(-1);
		
		Base.screenBuffer.enable();
		
		System.out.println(acr*size + " | " + dow * size);
		m.mapRender(new Transform(l * size, u * size, acr * size, dow * size), mapBuffer.getBoundTexture());
	}

	/**
	 * when the map needs to actually be used, call this one. Loads in the map,
	 * 
	 * how much time will it take? Who knows
	 */
	public void load() {
		tiles = new int[sections][][];
		widths = new int[sections];
		heights = new int[sections];
		
		Thread[] tList = new Thread[sections];
		for(int i = 0; i < sections; ++i) {
			(tList[i] = new Thread(new LoaderThread(i))).run();
		}
		
		for(int i = 0; i < sections; ++i) {
			try {
				tList[i].join();
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
		
		mapSpawn();
	}
	
	abstract public void mapSpawn();
	
	private class LoaderThread implements Runnable {
		int number;
		
		public LoaderThread(int n) {
			number = n;
		}
		
		public void run() {
			
			BufferedImage b = null;
			try {
				b = ImageIO.read(new File(mapImages[number]));
			}catch(Exception ex) {
				ex.printStackTrace();
				System.exit(-1);
			}
			
			int width = b.getWidth();
			int height = b.getHeight(); 
			
			int[] data = new int[width * height];
			b.getRGB(0, 0, width, height, data, 0, width);
			
			tiles[number] = new int[width][height];

			//first fill out with -1 (Spooky)
			for(int j = 0; j < width; ++j) {
				for(int k = 0; k < height; ++k) {
					tiles[number][j][k] = -1;
				}
			}
			
			int bs = blockSet.length;
			Thread[] tList = new Thread[bs];
			for(int i = 0; i < bs; ++i) {
				(tList[i] = new Thread(new PlacerThread(i, width, height, data, tiles[number]))).run();
			}
			
			for(int i = 0; i < bs; ++i) {
				try {
					tList[i].join();
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}
			
			widths[number] = width;
			heights[number] = height;
		}
	}
	
	private class PlacerThread implements Runnable {
		int block;
		
		int width;
		int height;
		int[] data;
		int[][] array;
		
		public PlacerThread(int b, int w, int h, int[] d, int[][] a) {
			block = b;
			
			width = w;
			height = h;
			data = d;
			array = a;
		}
		
		public void run() {
			int color = blockSet[block].colorCode;
			for(int j = 0; j < height; ++j) {
				for(int i = 0; i < width; ++i) {
					if(data[j * width + i] == color) {
						array[i][j] = block;
					}
				}
			}
		}
	}
	
}