package cnge.core;

import cnge.core.group.MapGroup;
import cnge.graphics.Transform;

/**
 * adds mapRender method that gets called once per block.
 * 
 * also add mapSpawn method that gets called when the map is loaded.
 * 
 * mapgroups will have this
 * 
 * @author Emmett
 */
public interface MapBehavior<G extends MapGroup<M>, M extends Map> extends Behavior<M>{
	
	/**
	 * gets called once per block, render as you will
	 * 
	 * @param g - the map group
	 * @param b - the block
	 * @param x - x in map coordinates
	 * @param y - y in map coordinates
	 * @param m - the map itself
	 * @param t - the transform of the block auto genned, just for you!
	 */
	public void mapRender(G g, Block b, int x, int y, M m, Transform t);
	
	/**
	 * gets called when the map is loaded (NO INSTANCES AT THIS POINT)
	 * 
	 * @param p - the map parameters
	 * @param b - the block set
	 * @param m - the map's reference tiles
	 */
	public void mapSpawn(G g, Block[] b);
}