package embgine.core;

import embgine.graphics.Camera;

public class EntityGroup {
	
	private int[] perLayer;
	private Entity[][] screenPool;
	
	private int layers;
	
	private Spawn behavior;
	
	private int numParams;
	
	//array system stuff
	private int size;
	private Entity[] collection;
	private int first; 
	private int last; 
	private boolean full;
	
	/**
	 * initializes the entity group in the scene
	 * 
	 * @param b - the behavior interface
	 * @param np - the number of render parameters
	 * @param mx - the max number of entities in this group
	 * @param lay - the amount of layers in the scene
	 */
	public EntityGroup(Spawn spawn, int np, int mx, int lay) {
		behavior = spawn;
		
		numParams = np;

		size = mx;
		collection = new Entity[size];
		first = 0;
		last = 0;
		full = false;
		
		layers = lay;
	}
	
	/**
	 * creates an instance of the entity belonging to this group
	 * 
	 * @return the entity created. Or NULL if the entity could not be created
	 */
	public Entity createInstance() {
		Entity create = new Entity(this, behavior, numParams);
		if( add(create) ) {
			behavior.spawn();
			return create;
		}else {
			return null;
		}
	}
	
	/**
	 * destroys one of the entity instances in this group
	 * 
	 * @param i - the index of the entity to delete
	 */
	public void destroyInstance(int i) {
		remove(i);
	}
	
	/**
	 * updates the list of entities on screen
	 * 
	 * @param camera - the game camera, to be used for screen checking
	 */
	public void onScreenUpdate(Camera camera) {
		perLayer = new int[layers];
		Entity[][] screenPool = new Entity[layers][size];
		
		for(int i = 0; i < last; ++i) {
			Entity e = collection[i];
			if(e != null && e.getOnScreen(camera)) {
				int layer = e.getLayer();
				screenPool[layer][perLayer[layer]] = e;
				++perLayer[layer];
			}
		}
		
	}
	
	public void update(int layer) {
		for(int i = 0; i < last; ++i) {
			Entity e = collection[i];
			if(e != null && e.getLayer() == layer) {
				e.update();
			}
		}
	}
	
	public void render(int layer) {
		Entity[] list = screenPool[layer];
		int len = perLayer[layer];
		for(int i = 0; i < len; ++i) {
			Entity e = list[i];
			if(e.getLayer() == layer) {
				e.render();
			}
		}
	}
	
	public void clear() {
		for(int i = 0; i < last; ++i) {
			collection[i] = null;
		}
		
		first = 0;
		last = 0;
		
		full = false;
	}
	
	/*
	 * private methods from here on, used as part of the array system
	 */

	private boolean add(Entity o) {
		if(!full) {
			
			o.setIndex(first);
			
			collection[first] = o;
			
			if(last < first + 1) {
				last = first + 1;
			}
			
			forwardFirst(first + 1);
			
			return true;
		}else {
			return false;
		}
	}
	
	private void remove(int ind) {
		collection[ind] = null;
		
		forwardFirst(0);
		
		if(ind == last) {
			last = 0;
			for(int i = last - 1; i > -1; --i) {
				if(collection[i] != null) {
					last = i;
					break;
				}
			}
		}
		
		full = false;
	}
	
	private void forwardFirst(int start) {
		over: {
			for(int i = start; i < size; ++i) {
				if(collection[i] == null) {
					first = i;
					break over;
				}
			}
			full = true;
		}
	}
		
}