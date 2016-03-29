package org.dibakar.servlet;

import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import java.io.IOException;
import java.time.LocalDateTime;

public class CustomAsyncListener implements AsyncListener {
	public CustomAsyncListener() {
		System.out.println("CustomAsyncListener was initialized @: "
				+ LocalDateTime.now() + " by thread: "
				+ Thread.currentThread().getName());
	}

	@Override
	public void onComplete(AsyncEvent event) throws IOException {
		//- notify clients of how much time it took for async processing.
		final LocalDateTime received_at = (LocalDateTime) event
				.getSuppliedRequest()
				.getAttribute("received_at");
		final LocalDateTime completed_at = LocalDateTime.now();
		final int hour_window = completed_at.getHour() - received_at.getHour();
		final int min_window = completed_at.getMinute() - received_at.getMinute();
		final int sec_window = completed_at.getSecond() - received_at.getSecond();
		StringBuilder msg = new StringBuilder(1024);
		msg.append("Server received client request @: ")
				.append(received_at.toString())
				.append(System.lineSeparator())
				.append("Request processing time = ")
				.append(hour_window).append(" hours : ")
				.append(min_window).append(" mins : ")
				.append(sec_window).append(" secs")
				.append(System.lineSeparator())
				.append("Printing thread: ")
				.append(Thread.currentThread().getName())
				.append(System.lineSeparator());

		event.getSuppliedResponse().getOutputStream().println(msg.toString());
	}

	@Override
	public void onTimeout(AsyncEvent event) throws IOException {
		String err_msg = "Async operation timed out after " +
				event.getAsyncContext().getTimeout() + " seconds";
		if( null != event.getSuppliedResponse().getOutputStream() ) {
			event.getSuppliedResponse().getOutputStream().println(err_msg);
		}
		System.err.println(err_msg);
		event.getAsyncContext().complete();
	}

	@Override
	public void onError(AsyncEvent event) throws IOException {
		String err_msg = "Async operation was aborted due to >> "
				+ event.getThrowable().getMessage();
		System.err.println(err_msg);
		event.getAsyncContext().complete();
	}

	@Override
	public void onStartAsync(AsyncEvent event) throws IOException {
		System.out.println("Async operation initiated...");
		if( null != event.getSuppliedResponse().getOutputStream() ) {
			event.getSuppliedResponse().getOutputStream().println("Working on your request...");
		}
	}
}
