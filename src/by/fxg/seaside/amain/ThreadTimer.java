package by.fxg.seaside.amain;

public class ThreadTimer extends Thread {
	public static ThreadTimer instance;
	public int delayer = 0;
	public int rotation360deg = 0;
	
	public ThreadTimer() {
		instance = this;
	}
	
	public void run() {
		while(true) {
			if (this.rotation360deg < 360) this.rotation360deg += 1; 
			else this.rotation360deg = 0;
			
			try {
				this.sleep(20);
			} catch (InterruptedException e) {
			}
		}
	}
}
