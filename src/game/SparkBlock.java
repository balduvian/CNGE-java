package game;

import embgine.core.Block;

public class SparkBlock extends Block{

	public boolean solid; 
	
	public SparkBlock(int cc, int l, boolean s) {
		super(cc, l);
		solid = s;
	}

}