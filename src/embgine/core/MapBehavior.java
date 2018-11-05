package embgine.core;

import embgine.graphics.Transform;

/**
 * adds mapRender method that gets called once per block.
 * 
 * also add mapSpawn method that gets called when the map is loaded.
 * 
 * mapgroups will have this
 * 
 * @author Emmett
 */
public interface MapBehavior extends Behavior{
	
	/**
	 * gets called once per block, render as you will
	 * 
	 * @param mp- the map parameters
	 * @param b - the block
	 * @param x - x in map coordinates
	 * @param y - y in map coordinates
	 * @param m - the map itself
	 * @param p - map entity render parameters
	 * @param t - the transform of the block auto genned, just for you!
	 */
	public void mapRender(Object[] mp, Block b, int x, int y, Map m, Object[] p, Transform t);
	
	/**
	 * gets called when the map is loaded (NO INSTANCES AT THIS POINT)
	 * 
	 * @param p - the map parameters
	 * @param b - the block set
	 * @param m - the map's reference tiles
	 */
	public void mapSpawn(Object[] p, Block[] b, int[][][] m);
}