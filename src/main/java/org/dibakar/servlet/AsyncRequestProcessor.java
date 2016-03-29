package org.dibakar.servlet;

import javax.servlet.AsyncContext;
import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

public class AsyncRequestProcessor implements Runnable {
	private final AsyncContext context ;
	public AsyncRequestProcessor(AsyncContext i_asyncContext) {
		context = i_asyncContext;
	}

	@Override
	public void run() {
		ServletOutputStream out = null ;
		try {
			out = context.getResponse().getOutputStream();
			out.println("Started working on async request on : " + LocalDateTime.now());
			System.out.println("DEBUG: Started working on async request on : "+ LocalDateTime.now());
			// long running operation
			Thread.sleep(TimeUnit.SECONDS.toMillis(20));
			System.out.println("DEBUG: Finished working on async request on : "+ LocalDateTime.now());
			if( null != out ) {
				out.println("Async request was completed on : "
						+ LocalDateTime.now().toString()
				+ " by thread: " + Thread.currentThread().getName()
				);
			}
		} catch (IOException cause) {
			cause.printStackTrace(System.err);
		} catch (InterruptedException e) {
			e.printStackTrace(System.err);
		}
		context.complete();
	}
}
