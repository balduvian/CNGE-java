package embgine.core;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import embgine.graphics.Transform;

public class Map {

	private int sections;
	
	private int[][][] tiles;
	
	private Transform[] transforms;
	
	private BufferedImage[] mapImages;
	
	public Map(String[] imagePaths) {
		sections = imagePaths.length;
		mapImages = new BufferedImage[sections];
		for(int i = 0; i < sections; ++i) {
			try {
				mapImages[i] = ImageIO.read(getClass().getClassLoader().getResource(imagePaths[i]));
			}catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public void load() {
		tiles = new int[sections][][];
		transforms = new Transform[sections];
	}
}