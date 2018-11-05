package game;

public class SnakeBlock extends SparkBlock{

	float thick;
	float red, green, blue, alpha;
	
	public SnakeBlock(int cc, int l, boolean s, float t, float r, float g, float b, float a) {
		super(cc, l, s);
		thick = t;
		red = r;
		green = g;
		blue = b;
		alpha = a;
	}

}