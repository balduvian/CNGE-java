package embgine.core.group;

import java.lang.reflect.Array;

import embgine.core.Behavior;
import embgine.core.Entity;
import embgine.graphics.Camera;

public class EntityGroup<E extends Entity> {
	
	protected static Camera camera;
	
	protected int[] perLayer;
	protected E[][] screenPool;
	
	protected int layers;
	
	protected Behavior<E> behavior;
	
	//array system stuff
	protected int size;
	protected E[] collection;
	private int first; 
	protected int last; 
	private boolean full;

	private Class<E> entityType;
	
	/**
	 * initializes the entity group in the scene
	 * 
	 * @param na - the name of the group
	 * @param np - the number of render parameters
	 * @param mx - the max number of entities in this group
	 * @param bh - the behavior interface
	 */
	@SuppressWarnings("unchecked")
	public EntityGroup(Class<E> et, int mx, Behavior<E> bh) {
		entityType = et;
		
		behavior = bh;
		
		size = mx;
		collection = (E[]) Array.newInstance(entityType, size);
		first = 0;
		last = 0;
		full = false;
	}
	
	public static void giveCamera(Camera c) {
		camera = c;
	}
	
	public void giveLayers(int la) {
		layers = la;
	}
	
	/**
	 * creates an instance of the entity belonging to this group
	 * 
	 * @param x - the x position of the entity
	 * @param y - the y position of the entity
	 * @param l - the layer the entity is on
	 * 
	 * @return the entity created. Or NULL if the entity could not be created
	 */
	@SuppressWarnings("unchecked")
	public E createInstance(float x, float y, int l) {
		E create = null;
		try {
			create = (E)entityType.getConstructors()[0].newInstance(this, x, y, l);
		} catch (Exception ex) {
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
	@SuppressWarnings("unchecked")
	public void onScreenUpdate(Camera camera) {

		perLayer = new int[layers];
		screenPool = (E[][]) new Object[layers][size];
		
		for(int i = 0; i < last; ++i) {
			screenPool[i] = (E[]) Array.newInstance(entityType, size);
			E e = collection[i];
			if(e != null && e.onScreenUpdate(camera)) {
				int layer = e.getLayer();
				screenPool[layer][perLayer[layer]] = e;
				++perLayer[layer];
			}
		}
		
	}
	
	public void update() {
		for(int i = 0; i < last; ++i) {
			E e = collection[i];
			if(e != null) {
				behavior.update(e);
			}
		}
	}
	
	public void render(int layer) {
		
		int len = perLayer[layer];
		
		for(int i = 0; i < len; ++i) {
			E e = screenPool[layer][i];
			if(e.getLayer() == layer) {
				behavior.render(e, camera);
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

	protected boolean add(E o) {
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