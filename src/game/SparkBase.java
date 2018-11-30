//gatcuddy <emmettglaser@gmail.com>

package game;

import cnge.core.Base;
import cnge.core.BasePreset;

import cnge.graphics.Window;

public class SparkBase extends Base {
	
	public SparkLoadScreen loadScreen;
	
	public SparkBase(Window win, BasePreset set) {
		super(win, set);
		loadScreen = new SparkLoadScreen();
		loadLoadScreen(loadScreen);
	}
	
}