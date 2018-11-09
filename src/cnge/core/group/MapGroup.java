package cnge.core.group;

import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.Array;

import javax.imageio.ImageIO;

import cnge.core.Block;
import cnge.core.Map;
import cnge.core.Map.MapAccessException;
import cnge.core.MapBehavior;
import cnge.graphics.Camera;
import cnge.graphics.Transform;

public class MapGroup<M extends Map> extends EntityGroup<M> {

	private int sections;
	
	private int[][][] tiles;
	private int[] widths;
	private int[] heights;
	
	private String[] mapImages;
	
	private Block[] blockSet;
	
	/**
	 * initializes a new map group
	 * 
	 * @param et - the class of the entity being created
	 * @param mx - the max number of entities in this group
	 * @param bh - the behavior interface
	 * @param ip - paths to the map images
	 * @param ts - the array containing the tile scales for each section of the map
	 * @param bs - the blockset for the map
	 */
	public MapGroup(Class<M> mt, int mx, MapBehavior<M> bh, String[] ip) {
		super(mt, mx, bh);
		sections = ip.length;
		mapImages = ip;
	}
	
	/**
	 * defines a new map type for the scene, BUT it only has one section
	 * 
	 * @param imagePath - link to map image
	 * @param t - transform
	 * @param bs - the blockset for the map
	 * @param ip - path to the map image
	 * @param ts - tile scale for the map
	 * @param bs - the blockset for the map
	 */
	public MapGroup(Class<M> mt, int mx, MapBehavior<M> bh, String ip, Block[] bs) {
		super(mt, mx, bh);
		sections = 1;
		mapImages = new String[] {ip};
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
	 * @return returns the maps, NULL if could not create.
	 * NOTE: all maps must be successfully created to be returned
	 */
	@SuppressWarnings("unchecked")
	public M[] createMaps(float x, float y) {
		M[] creates = (M[]) Array.newInstance(entityType, sections);
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
			M create = null;
			try {
				creates[i] = create;
				create = (M) entityType.getConstructors()[0].newInstance(this, x, y, 0);
			} catch(Exception ex) {
				ex.printStackTrace();
				System.exit(-1);
			}
			if( add(create) ) {
				behavior.spawn(create);
			}else {
				return null;
			}
		}
		return creates;
	}
	
	public Block[] getBlockSet() {
		return blockSet;
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
	@SuppressWarnings("unchecked")
	public Map createMap(float x, float y, int l, Object... p) {
		int w = widths[0];
		int h = heights[0];
		int[][] take = tiles[0];
		int[][] give = new int[w][h];
		for(int j = 0; j < w; ++j) {
			for(int k = 0; k < h; ++k) {
				give[j][k] = take[j][k];
			}
		}
		M create = null;
		try {
			create = (M) behavior.create(x, y, l, p);
			create.mapSetup(x, y, l, this, give);
		} catch(Exception ex) {
			ex.printStackTrace();
			System.exit(-1);
		}
		if( add(create) ) {
			behavior.spawn(create);
			return create;
		}else {
			return null;
		}
	}
	
	/**
	 * updates the list of entities on screen
	 * 
	 * @param camera - the game camera, to be used for screen checking
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void onScreenUpdate(Camera camera) {

		perLayer = new int[layers];
		screenPool = (M[][]) Array.newInstance(entityType, layers, size);
		
		for(int i = 0; i < last; ++i) {
			M m = (M)collection[i];
			//System.out.println(m.getTransform().width + " | " + m.getTransform().height);
			if(m != null && m.onScreenUpdate(camera)) {
				
				//System.out.println("FOUND");
				int layer = m.getLayer();
				screenPool[layer][perLayer[layer]] = m;
				++perLayer[layer];
				
				Transform mt = m.getTransform();
				Transform ct = camera.getTransform();
				
				
				m. setLeft( (int)Math.floor( ( ct.abcissa - mt.abcissa) / (mt.getWidth()) ));
				m.setRight( (int) Math.ceil( ((ct.abcissa +   ct.width) - mt.abcissa) / (mt.getWidth()) ));
				m.   setUp( (int)Math.floor( ( ct.ordinate - mt.ordinate) / (mt.getHeight()) ));
				m. setDown( (int) Math.ceil( ((ct.ordinate +  ct.height) - mt.ordinate) / (mt.getHeight()) ));
				
				System.out.println(m.getRight());
				//System.out.println( ( ct.abcissa - mt.abcissa) + " | " + (tileScale[i] * mt.wScale) );
			}
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
		
		for(int i = 0; i < len; ++i) {
			M m = (M)screenPool[layer][i];
			
			/*
			 * this transform is the size of a block
			 */
			Transform t = m.getTransform();
			float wide = t.getWidth() / m.getWidth();
			float tall = t.getHeight() / m.getHeight();
			
			//System.out.println(wide + " " + tall);
			//System.exit(-1);
			
			Transform blockTransform = new Transform(wide, tall);
			
			if(m.getLayer() == layer) {
				
				int u = m.getUp();
				int r = m.getRight();
				int d = m.getDown();
				int l = m.getLeft();
				
				for(int x = l; x < r; ++x) {
					for(int y = u; y < d; ++y) {
						try {
							int tile = m.access(x, y);
							if(tile != -1) {
								blockTransform.setTranslation(x * wide + t.abcissa, y * tall + t.ordinate);
								((MapBehavior<M>)behavior).mapRender(blockSet[tile], x, y, m, blockTransform);
							}
						} catch (MapAccessException ex) { }
					}
				}
					
			}
			behavior.render(m, camera);
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
		
		((MapBehavior<M>)behavior).mapSpawn(tiles, blockSet);
	}
	
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