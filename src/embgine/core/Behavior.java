package embgine.core;

import embgine.graphics.Transform;

/**
 * defines a custom set of behaviors for entities,
 * shared by all entities in an entity group
 * 
 * @author Emmett
 */
public interface Behavior {
	
	/*
	 * if you would look here at these methods, you will see that each one takes in Object[] p and Transform t
	 * the entity group will automatically pass in the correct values for these for each entity
	 * these are needed to personalize the actions for each entity instance
	 */
	
	public void  spawn(Entity e, Object[] p, Transform t);
	
	public void update(Entity e, Object[] p, Transform t);
	
	public void render(Entity e, Object[] p, Transform t);
}