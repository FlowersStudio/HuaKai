package com.piscen.huakai.http;

import java.util.ArrayList;

/**
 *	HTTP线程池
 */
public class HttpConnection {
	public static final int MAX_CONNECTIONS = 10;
	public static final int CONN_TIMEOUT = 6000; // 6S
	public static final int SO_TIMEOUT = 10000; // 10S
	
	private ArrayList<Runnable> active = new ArrayList<Runnable>();
	private ArrayList<Runnable> queue = new ArrayList<Runnable>();

	private static HttpConnection instance;

	public static HttpConnection getInstance() {
		if (instance == null) {
			instance = new HttpConnection();
		}
		return instance;
	}

	public void push(Runnable runnable) {
		queue.add(runnable);
		if (active.size() < MAX_CONNECTIONS) {
			startNext();
		}
	}

	private void startNext() {
		if (!queue.isEmpty()) {
			Runnable next = queue.get(0);
			queue.remove(0);
			active.add(next);
			new Thread(next).start();
		}
	}

	public void finish(Runnable runnable) {
		active.remove(runnable);
		startNext();
	}

}