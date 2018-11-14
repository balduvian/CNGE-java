package game;

import cnge.graphics.texture.TileTexture;

public class TexBlock extends SparkBlock{

	public int texX;
	public int texY;
	public TileTexture texture;
	
	public TexBlock(int cc, int l, int i, boolean s, TileTexture t, int x, int y) {
		super(cc, l, i, s);
		texture = t;
		texX = x;
		texY = y;
	}

}