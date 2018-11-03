package embgine.core;

import embgine.graphics.Texture;

public class BlockSet {
	
	public int[] colorCode;
	public Texture[] texture;
	public int[] solid;
			
	public BlockSet(int[] c, Texture[] t, int[] s) {
		colorCode = c;
		texture = t;
		solid = s;
	}
	
}