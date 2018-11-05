package game;

import embgine.graphics.Texture;

public class TexBlock extends SparkBlock{

	public int texX;
	public int texY;
	public Texture texture;
	
	public TexBlock(int cc, int l, boolean s, Texture t, int x, int y) {
		super(cc, l, s);
		texX = x;
		texY = y;
	}

}