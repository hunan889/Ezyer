package com.niuan.common.ezyer.util.thread;

class ThreadSwitcher {
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