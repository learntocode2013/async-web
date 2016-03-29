package org.dibakar.servlet;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "UploadServlet", urlPatterns = {"/UploadServlet"}, asyncSupported = true)
public class UploadServlet extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		final AsyncContext asyncContext = req.startAsync();
		registerAsyncListener(asyncContext);
		ServletInputStream inputStream = req.getInputStream();
		ReadListener readListener = new ReadListenerImpl(inputStream,resp,asyncContext);
		inputStream.setReadListener(readListener);
	}

	private void registerAsyncListener(AsyncContext asyncContext) {
		asyncContext.addListener(new AsyncListener() {
			@Override
			public void onComplete(AsyncEvent event) throws IOException {
				event.getSuppliedResponse().getOutputStream().print("File upload was successfully completed");
			}

			@Override
			public void onTimeout(AsyncEvent event) throws IOException {
				System.out.println("my asyncListener on timeout");
			}

			@Override
			public void onError(AsyncEvent event) throws IOException {
				event.getThrowable().printStackTrace(System.out);
			}

			@Override
			public void onStartAsync(AsyncEvent event) throws IOException {

			}
		});
	}
}
