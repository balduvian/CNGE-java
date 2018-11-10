package game;

import java.io.IOException;

import cnge.core.Base;
import cnge.core.BasePreset;
import cnge.graphics.Window;
import game.scenes.game.GameScene;

public class Main {
	
	public static final float GRAVITY = 800;
	
	public static void main(String[] args) {
		
		String os = System.getProperty("os.name");
		
		if (os.indexOf("mac") >= 0){
			if (args.length == 0) {
				try {
	                Runtime.getRuntime().exec(new String[]{"java", "-XstartOnFirstThread", "-jar", "Spark Runner 2.jar"});
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
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
		
		Base base = new Base(window, new BasePreset(BasePreset.ASPECT_FIXED, BasePreset.UNIT_SCREEN_PIXELS, 512, 288));
		
		base.start(new GameScene());
	}
	
}