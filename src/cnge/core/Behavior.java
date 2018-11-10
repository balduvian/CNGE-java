package cnge.core;

import cnge.graphics.Camera;

/**
 * defines a custom set of behaviors for entities,
 * shared by all entities in an entity group
 * 
 * @author Emmett
 */
public interface Behavior<E extends Entity> {
	
	/*
	 * if you would look here at these methods, you will see that each one takes in Object[] p and Transform t
	 * the entity group will automatically pass in the correct values for these for each entity
	 * these are needed to personalize the actions for each entity instance
	 */
	
	/**
	 * this has to defined for this entity group to produce instances of entities
	 * 
	 * @param x - x position to create entity at
	 * @param y - and the y
	 * @param l - the layer to create entity at
	 * @param p - additional spawn parameters
	 * 
	 * @return - a new instance of the specific entity for this group
	 */
	public Entity create(Object... p);
	
	public void update(E e);
	
	public void render(E e, Camera c);
}