package embgine.core;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import embgine.graphics.Transform;

public class MapGroup extends EntityGroup {

	private int sections;
	
	private int[][][] tiles;
	private int[] widths;
	private int[] heights;
	
	private Transform[] transforms;
	
	private String[] mapImages;
	
	private BlockSet blockSet;
	
	/**
	 * defines a new map type for the scene
	 * 
	 * @param imagePaths - links to where the images are
	 * @param ts - transforms for the sections [IT BETTER BE THE SAME LENGTH AS IMAGEPATHS]
	 * @param bs - the blockset for the map
	 */
	public MapGroup(String na, int np, int mx, Behavior bh, String[] imagePaths, Transform[] ts, BlockSet bs) {
		super(na, np, mx, bh);
		sections = imagePaths.length;
		mapImages = imagePaths;
		transforms = ts;
		blockSet = bs;
	}
	
	/**
	 * defines a new map type for the scene, BUT it only has one section
	 * 
	 * @param imagePath - link to map image
	 * @param t - transform
	 * @param bs - the blockset for the map
	 */
	public MapGroup(String na, int np, int mx, Behavior bh, String imagePath, Transform t, BlockSet bs) {
		super(na, np, mx, bh);
		sections = 1;
		mapImages = new String[] {imagePath};
		transforms = new Transform[] {t};
		blockSet = bs;
	}
	
	//TODO DO THIS SHITTTT
	/**
	 * 
	 */
	@Override
	public void render(int layer) {
		
		int len = perLayer[layer];

		Entity[] list = screenPool[layer];
		
		for(int i = 0; i < len; ++i) {
			Entity e = screenPool[layer][i];
			if(e.getLayer() == layer) {
				behavior.render(e, e.getParams(), e.getTransform());
			}
		}
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
	}
	
	private class LoaderThread implements Runnable {
		int number;
		
		public LoaderThread(int n) {
			number = n;
		}
		
		public void run() {
			BufferedImage b = null;
			try {
				b = ImageIO.read(getClass().getClassLoader().getResource(mapImages[number]));
			}catch(Exception ex) {
				ex.printStackTrace();
				System.exit(-1);
			}
			
			int width = b.getWidth();
			int height = b.getHeight(); 
			
			int[] data = new int[width * height];
			b.getRGB(0, 0, width, height, data, 0, width);
			
			tiles[number] = new int[width][height];
			
			int bs = blockSet.numBlocks;
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
			int color = blockSet.colorCode[block];
			for(int j = 0; j < height; ++j) {
				for(int i = 0; i < width; ++i) {
					if(data[j * height + i] == color) {
						array[i][j] = block;
					}
				}
			}
		}
	}
	
}