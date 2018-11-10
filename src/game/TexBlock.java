package game;

import cnge.graphics.texture.Texture;

public class TexBlock extends SparkBlock{

	public int texX;
	public int texY;
	public Texture texture;
	
	public TexBlock(int cc, int l, int i, boolean s, Texture t, int x, int y) {
		super(cc, l, i, s);
		texture = t;
		texX = x;
		texY = y;
	}

}