package com.niuan.common.ezyer.ui.util.dl.thread;

public class ThreadSwitcher {
	public void pause() {
		synchronized (this) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void resume() {
		synchronized (this) {
			notify();
		}
	}
}