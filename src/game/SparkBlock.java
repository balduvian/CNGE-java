package game;

import embgine.core.Block;

public class SparkBlock extends Block{

	public boolean solid; 
	
	public SparkBlock(int cc, int l, int i, boolean s) {
		super(cc, l, i);
		solid = s;
	}

}