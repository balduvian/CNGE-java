package embgine.graphics;

/**
 * the positioning class for many many things in embgine
 * 
 */
public class Transform {
	
	/** the x coordinate */
	public float abcissa;
	/** the y coordinate */
	public float ordinate;
	public float width;
	public float height;
	public float wScale;
	public float hScale;
	public float rotation;
	
	public Transform(float x, float y, float w, float h, float r) {
		abcissa = x;
		ordinate = y;
		width = w;
		height = h;
		wScale = 1;
		hScale = 1;
		rotation = r;
	}
	
	public Transform(float x, float y, float w, float h) {
		abcissa = x;
		ordinate = y;
		width = w;
		height = h;
		wScale = 1;
		hScale = 1;
		rotation = 0;
	}
	
	public Transform(float w, float h) {
		abcissa = 0;
		ordinate = 0;
		width = w;
		height = h;
		wScale = 1;
		hScale = 1;
		rotation = 0;
	}
	
	public Transform() {
		abcissa = 0;
		ordinate = 0;
		width = 1;
		height = 1;
		wScale = 1;
		hScale = 1;
		rotation = 0;
	}
	
	public Transform(Transform t) {
		abcissa = t.abcissa;
		ordinate = t.ordinate;
		width = t.width;
		height = t.height;
		wScale = t.wScale;
		hScale = t.hScale;
		rotation = t.rotation;
	}
	
	public void set(Transform t) {
		abcissa = t.abcissa;
		ordinate = t.ordinate;
		width = t.width;
		height = t.height;
		wScale = t.wScale;
		hScale = t.hScale;
		rotation = t.rotation;
	}
	
	public void set(float x, float y, float w, float h, float r) {
		abcissa = x;
		ordinate = y;
		width = w;
		height = h;
		rotation = r;
	}
	
	public void set(float x, float y, float w, float h) {
		abcissa = x;
		ordinate = y;
		width = w;
		height = h;
	}
	
	public void setTranslation(float x, float y) {
		abcissa = x;
		ordinate = y;
	}
	
	public void setScale(float w, float h) {
		wScale = w;
		hScale = h;
	}
	
	public void setSize(float w, float h) {
		width = w;
		height = h;
	}

	public void move(float x, float y) {
		abcissa += x;
		ordinate += y;
	}
	
	public float getWidth() {
		return width * wScale;
	}
	
	public float getHeight() {
		return height * hScale;
	}
	
}