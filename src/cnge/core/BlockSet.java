package cnge.core;

/**
 * this class contains all the blocks a map or level will use
 *
 * @author Plane Emmett
 * 
 * @param <B> - the type of block in this set
 */
public class BlockSet<B extends Block>{
	
	/**
	 * when there is no block in a space in the map, a -1, this block is put there
	 */
	private B defaultBlock;
	/**
	 * the list of blocks, each with a position in the list
	 */
	private B[] blocks;
	//how many blocks (exclusing default)
	private int numBlocks;
	
	/**
	 * constructs a blockset, auto generates ids for blocks
	 * 
	 * @param bDefault - the default case block
	 * @param bs - the rest of the blocks
	 */
	@SafeVarargs
	public BlockSet(B bDefault, B... bs) {
		defaultBlock = bDefault;
		blocks = bs;
		numBlocks = blocks.length;
		for(int i = 0; i < numBlocks; ++i) {
			blocks[i].setID(i);
		}
	}
	
	/**
	 * gets a block based off its id in this blockset
	 * 
	 * @param id - the block id
	 * @return the block of the ID, the defaultblock if -1
	 */
	public B get(int id) {
		if(id == -1) {
			return defaultBlock;
		}else {
			return blocks[id];
		}
	}
	
	/**
	 * gets the number of blocks in this blockSet
	 * 
	 * @return numBlocks
	 */
	public int getLength() {
		return numBlocks;
	}
	
}