package embgine.core.group;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import embgine.core.Behavior;
import embgine.core.Block;
import embgine.core.Entity;
import embgine.core.Map;
import embgine.core.MapBehavior;
import embgine.graphics.Transform;

public class MapGroup extends EntityGroup {

	private int sections;
	
	private int[][][] tiles;
	private int[] widths;
	private int[] heights;
	
	private float[] tileScale;
	
	private String[] mapImages;
	
	private Block[] blockSet;
	
	/**
	 * defines a new map type for the scene
	 * 
	 * @param imagePaths - links to where the images are
	 * @param ts - transforms for the sections [IT BETTER BE THE SAME LENGTH AS IMAGEPATHS]
	 * @param bs - the blockset for the map
	 */
	public MapGroup(String na, int np, int mx, Behavior bh, String[] imagePaths, float[] ts, Block[] bs) {
		super(na, np, mx, bh);
		sections = imagePaths.length;
		mapImages = imagePaths;
		tileScale = ts;
		blockSet = bs;
	}
	
	/**
	 * defines a new map type for the scene, BUT it only has one section
	 * 
	 * @param imagePath - link to map image
	 * @param t - transform
	 * @param bs - the blockset for the map
	 */
	public MapGroup(String na, int np, int mx, Behavior bh, String imagePath, float t, Block[] bs) {
		super(na, np, mx, bh);
		sections = 1;
		mapImages = new String[] {imagePath};
		tileScale = new float[] {t};
		blockSet = bs;
	}
	
	/**
	 * use this instead of the {@link EntityGroup} one
	 * 
	 * creates all the map sections into indiviudal maps
	 * 
	 * @param x - the x position of the map
	 * @param y - the y position of the map
	 * @param l - the layer the map is on
	 * 
	 * @return returns the maps, NULL if could not create
	 */
	public Map[] createMaps(float x, float y) {
		Map[] creates = new Map[sections];
		for(int i = 0; i < sections; ++i) {
			int w = widths[i];
			int h = heights[i];
			int[][] take = tiles[i];
			int[][] give = new int[w][h];
			for(int j = 0; j < w; ++j) {
				for(int k = 0; k < h; ++k) {
					give[j][k] = take[j][k];
				}
			}
			Map create = new Map(this, numParams, x, y, 0, tileScale[i], give, blockSet);
			creates[i] = create;
			if( add(create) ) {
				behavior.spawn(create, create.getParams(), create.getTransform());
			}else {
				return null;
			}
		}
		return creates;
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
	public Map createMap(float x, float y) {
		int w = widths[0];
		int h = heights[0];
		int[][] take = tiles[0];
		int[][] give = new int[w][h];
		for(int j = 0; j < w; ++j) {
			for(int k = 0; k < h; ++k) {
				give[j][k] = take[j][k];
			}
		}
		Map create = new Map(this, numParams, x, y, 0, tileScale[0], give, blockSet);
		if( add(create) ) {
			behavior.spawn(create, create.getParams(), create.getTransform());
			return create;
		}else {
			return null;
		}
	}
	
	/**
	 * overrides render from entity group 
	 * 
	 * @param layer - the current render layer
	 */
	@Override
	public void render(int layer) {
		
		int len = perLayer[layer];

		Entity[] list = screenPool[layer];
		
		for(int i = 0; i < len; ++i) {
			Map e = (Map)list[i];
			
			/*
			 * this transform is the size of a block
			 */
			Transform t = e.getTransform();
			float wide = tileScale[i] * t.wScale;
			float tall = tileScale[i] * t.hScale;
			Transform blockTransform = new Transform(wide, tall);
			
			if(e.getLayer() == layer) {
				int w = e.getWidth();
				int h = e.getHeight();
				for(int x = 0; x < w; ++x) {
					for(int y = 0; y < h; ++y) {
						blockTransform.setTranslation(x * wide + t.abcissa, y * tall + t.ordinate);
						((MapBehavior)behavior).mapRender(blockSet[tiles[0][x][y]], x, y, e, e.getParams(), blockTransform );
					}
				}
					
			}
			behavior.render(e, e.getParams(), e.getTransform());
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
		
		((MapBehavior)behavior).mapSpawn(blockSet, tiles);
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
					if(data[j * height + i] == color) {
						array[i][j] = block;
					}
				}
			}
		}
	}
	
}