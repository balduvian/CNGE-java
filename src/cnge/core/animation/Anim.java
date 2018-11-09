package cnge.core.animation;

import cnge.core.Base;

abstract public class Anim {
	
	protected double timer;
	protected double[] frameTimes;
	protected int frame;
	protected int numFrames;
	
	public Anim(double[] f) {
		frameTimes = f;
		numFrames = f.length;
		timer = 0;
		frame = 0;
	}
	
	abstract public int getX();
	
	public void update() {
		timer += Base.time;
		if(timer >= frameTimes[frame]) {
			++frame;
			frame %= numFrames;
			timer = 0;
		}
	}
	
	public void reset() {
		frame = 0;
		timer = 0;
	}
}