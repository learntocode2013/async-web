package org.dibakar.servlet;

import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import java.io.IOException;

public class ProgressAsyncListener implements AsyncListener {
	@Override
	public void onComplete(AsyncEvent event) throws IOException {
		System.out.println("ProgressAsyncListener.onComplete > async request completed");
	}

	@Override
	public void onTimeout(AsyncEvent event) throws IOException {
		System.out.println("ProgressAsyncListener.onTimeout >");
		event.getAsyncContext().getResponse().getWriter().write("request has timed-out");
	}

	@Override
	public void onError(AsyncEvent event) throws IOException {
		System.out.println("ProgressAsyncListener.onError >");
	}

	@Override
	public void onStartAsync(AsyncEvent event) throws IOException {
		System.out.println("ProgressAsyncListener.onStartAsync >");
	}
}
