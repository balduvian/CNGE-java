package game.scenes.noise;

import cnge.core.AssetBundle;
import cnge.core.LoadScreen;
import cnge.graphics.Sound;
import cnge.graphics.shapes.RectShape;
import game.shaders.MovieShader;
import game.shaders.StenShader;

public class NoiseAssets extends AssetBundle {

	public static StenShader stenShader;
	public static MovieShader movieShader;
	public static RectShape rect;
	
	public static Sound[] lagSounds;
	
	public NoiseAssets(LoadScreen s) {
		super(
			s,
			new LoadAction[] {
				() -> {
					stenShader = new StenShader();
				},
				() -> {
					movieShader = new MovieShader();
				},
				() -> {
					rect = new RectShape();
				},
				() -> {
					lagSounds = new Sound[100];
					for(int i = 0; i < 50; ++i) {
						lagSounds[i] = new Sound("res/sounds/xp.wav");
					}
				},
				() -> {
					for(int i = 50; i < 100; ++i) {
						lagSounds[i] = new Sound("res/sounds/xp.wav");
					}
				}
			}
		);
	}

}