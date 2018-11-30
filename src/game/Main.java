package game;

import java.io.IOException;

import cnge.core.Base;
import cnge.core.BasePreset;
import cnge.core.Font;
import cnge.core.LoadScreen;
import cnge.graphics.Window;
import cnge.graphics.texture.TexturePreset;
import game.scenes.game.GameScene;
import game.scenes.noise.NoiseScene;

public class Main {
	
	public static final float GRAVITY = 800;
	
	public static void main(String[] args) {
		
		/*
		 * mac startup sequence
		 * 
		 * have to add the command beforehand then re run jar
		 */
		String os = System.getProperty("os.name");
		
		if (os.indexOf("mac") >= 0){
			if (args.length == 0) {
				try {
	                Runtime.getRuntime().exec(new String[]{"java", "-XstartOnFirstThread", "-jar", "Spark Runner 2.jar", "noReRun"});
	                System.exit(-1);
	            } catch (IOException ex) { ex.printStackTrace(); }
			}else {
				new Main();
			}
		}else {
			new Main();
		}
		
	}
	
	private Main() {
		Window window = new Window(false, "Spark Runner 2", true);
		
		window.setIcon("res/textures/icon.png");
		
		TexturePreset.setDefaults(false, false, true);
		
		SparkBase base = new SparkBase(window, new BasePreset(Base.ASPECT_FRAMER, BasePreset.UNIT_SCREEN_PIXELS, 512, 288, 10));
		
		base.start(new GameScene());
	}
	
}