package org.dibakar.servlet;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class ReadListenerImpl implements ReadListener {
	private final ServletInputStream input ;
	private final HttpServletResponse response ;
	private final AsyncContext context ;
	private final Queue<String> queue = new LinkedBlockingQueue<>();

	public ReadListenerImpl(ServletInputStream input, HttpServletResponse response, AsyncContext context) {
		this.input = input;
		this.response = response;
		this.context = context;
	}

	@Override
	public void onDataAvailable() throws IOException {
		StringBuilder sb = new StringBuilder();
		byte[] bytes = new byte[1024];
		int len = -1 ;

		// read a chunk of data and add to queue.
		while( input.isReady() && -1 != (len=input.read(bytes))) {
			String data = new String(bytes,0,len);
			sb.append(data);
		}

		queue.add(sb.toString());
	}

	@Override
	public void onAllDataRead() throws IOException {
		System.out.println("All incoming data has been read...");
		ServletOutputStream outputStream = response.getOutputStream();
		WriteListener writeListener = new WriteListenerImpl(context,outputStream,queue);
		outputStream.setWriteListener(writeListener);
	}

	@Override
	public void onError(Throwable t) {
		context.complete();
		t.printStackTrace();
	}
}
