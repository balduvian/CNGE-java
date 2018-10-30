package game;

import embgine.core.Base;
import embgine.core.BasePreset;
import embgine.graphics.Window;

public class Main {
	
	public static void main(String[] args) {
		
		Window window = new Window(false, "Spark Runner 2", true);
		
		window.setIcon("bezier/images/icon.png");
		
		Base base = new Base(window, new BasePreset(BasePreset.ASPECT_STRETCH_HORZ, false, 48, 48, 16, 64));
		
		base.start(new GameScene());
		
	}
	
}