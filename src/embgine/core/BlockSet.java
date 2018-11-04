package embgine.core;

import embgine.graphics.Texture;

public class BlockSet {
	
	public int numBlocks;
	
	public int[] colorCode;
	public Texture[] texture;
	public boolean[] solid;
	
	/**
	 * 
	 * @param c - color codes array
	 * @param t - texture array
	 * @param s - solid array
	 */
	public BlockSet(int[] c, Texture[] t, boolean[] s) {
		colorCode = c;
		texture = t;
		solid = s;
		
		numBlocks = colorCode.length;
	}
	
}