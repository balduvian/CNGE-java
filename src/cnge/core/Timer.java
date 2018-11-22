package cnge.core;

/**
 * YOOOOOOOOOOOOOOOOOOOOOOOOOO
 * @author Emmett
 *
 */
public class Timer {
	
	private double time;
	private double timer;
	
	private boolean going;
	
	private TimerEvent event;
	
	public Timer(double t, TimerEvent e) {
		time = t;
		event = e;
	}
	
	public interface TimerEvent {
		public void event();
	}
	
	public void update() {
		if(going) {
			timer -= Base.time;
			if(timer <= 0) {
				timer = 0;
				going = false;
				event.event();
			}
		}
	}
	
	public void forceEnd() {
		going = false;
		timer = 0;
		event.event();
	}
	
	public void start() {
		timer = time;
		going = true;
	}
	
	public void pause() {
		going = false;
	}
	
	public void resume() {
		going = true;
	}
	
}