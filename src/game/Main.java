package game;

import java.io.IOException;

import embgine.core.Base;
import embgine.core.BasePreset;
import embgine.graphics.Window;

public class Main {
	
	public static void main(String[] args) {
		
		String os = System.getProperty("os.name");
		
		if (os.indexOf("mac") >= 0){
			if (args.length == 0) {
				try {
	                Runtime.getRuntime().exec(new String[]{"java", "-XstartOnFirstThread", "-jar", "TheLastWall.jar", "noReRun"});
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
		
		Base base = new Base(window, new BasePreset(BasePreset.ASPECT_FIXED, BasePreset.UNIT_SCREEN_PIXELS, 48, 48));
		
		base.start(new GameScene());
	}
	
}