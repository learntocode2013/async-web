package org.dibakar.servlet;

import javax.servlet.AsyncContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.IOException;
import java.util.Queue;

public class WriteListenerImpl implements WriteListener {
	private final ServletOutputStream outStream;
	private final Queue<String> queue;
	private final AsyncContext context;

	public WriteListenerImpl(AsyncContext _context, ServletOutputStream _outStream, Queue<String> _queue) {
		this.context = _context;
		this.outStream = _outStream;
		this.queue = _queue;
	}

	@Override
	public void onWritePossible() throws IOException {
		while( null != queue.peek() && outStream.isReady() ) {
			String data = queue.remove();
			outStream.write(data.getBytes());
		}
		// No more data to write.
		if( null == queue.peek() ) {
			context.complete();
			System.out.println("No more data to write | WriteListener exiting...");
		}
	}

	@Override
	public void onError(Throwable throwable) {
		context.complete();
		throwable.printStackTrace();
	}
}
