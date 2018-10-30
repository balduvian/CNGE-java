package embgine.core;

import embgine.graphics.Camera;

public class EntityGroup {
	
	private EntityPool pool;
	
	private Behavior renderer;
	private int numParams;
	
	public EntityGroup(Behavior r, int np, int mx) {
		renderer = r;
		numParams = np;
		pool = new EntityPool(mx);
	}
	
	/**
	 * creates an instance of the entity belonging to this group
	 * 
	 * @return the entity created. Or NULL if the entity could not be created
	 */
	public Entity createInstance() {
		Entity create = new Entity(renderer, numParams);
		if( pool.add(create) ) {
			renderer.spawn();
			return create;
		}else {
			return null;
		}
	}
	
	/**
	 * destroys one of the entity instances in this group
	 * 
	 * @return uwu
	 */
	public void destroyInstance(Entity e) {
		
	}
	
	/**
	 * gets you a list of all the entities in this group that are on the screen
	 * @param camera - the game camera, to be used for screen checking
	 * @return an array containing entities that are on screen
	 */
	public Entity[] getOnScreen(Camera camera) {
		int length = 0;
		Entity[] temp = new Entity[size];
		for(int i = 0; i < last; ++i) {
			Entity e = collection[i];
			if(e != null && e.getOnScreen(camera)) {
				temp[length] = e;
				++length;
			}
		}
		Entity[] ret = new Entity[length];
		for(int i = 0; i < length; ++i) {
			ret[i] = temp[i];
		}
		return ret;
	}
	
	public void update(int layer) {
		Entity[] collection = pool.get();
		for(int i = 0; i < last; ++i) {
			Entity e = collection[i];
			if(e != null && e.getLayer() == layer) {
				e.update();
			}
		}
	}
	
	public void render(int layer) {
		for(int i = 0; i < last; ++i) {
			Entity e = collection[i];
			if(e != null && e.getLayer() == layer) {
				e.render();
			}
		}
	}
	
	public class EntityPool {

		private int size;
		
		private Entity[] collection;
		
		private int first; //the first position an object can go in
		
		private int last; //the last position an object exists in
		
		private boolean full;

		public EntityPool(int s) {
			size = s;
			
			collection = new Entity[size];
			
			first = 0;
			last = 0;
			
			full = false;
		}
		
		public Entity[] get() {
			return collection;
		}
		
		public int getLast() {
			return last;
		}
		
		public boolean add(Entity o) {
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
		
		protected void remove(int ind) {
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
		
		protected void clear() {
			for(int i = 0; i < last; ++i) {
				collection[i] = null;
			}
			
			first = 0;
			last = 0;
			
			full = false;
		}
		
	}
	
}