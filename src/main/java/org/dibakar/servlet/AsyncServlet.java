package org.dibakar.servlet;

import javax.servlet.AsyncContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

@WebServlet(asyncSupported = true, urlPatterns = {"/AsyncServlet"})
public class AsyncServlet extends HttpServlet {
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
		doGet(req,resp);
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
		System.out.println("AsyncServlet: Processing request with id : " + req.getParameter("id")
		+ ".on thread " + Thread.currentThread().getId() + " : " + Thread.currentThread().getName()
				+ " [" + LocalDate.now() + "] "
		);

		req.setAttribute("received_at", LocalDateTime.now());
		boolean dispatch = req.getParameter("dispatch").toLowerCase().equals("true");
		boolean timeout  = req.getParameter("timeout").toLowerCase().equals("true");

		AsyncContext asyncContext = req.startAsync();
		if(timeout) {
			// TODO: set based on request param...
			asyncContext.setTimeout(TimeUnit.SECONDS.toMillis(2));
		}
		asyncContext.addListener(new CustomAsyncListener());
		Executor thread_pool = (Executor) req.getServletContext().getAttribute("thread_pool");
		thread_pool.execute(new AsyncRequestProcessor(asyncContext));
		pauseRequestProcessingThread();
		System.out.println("Request processing thread : " + Thread.currentThread()
				.getName() + " was released at : " + LocalDateTime.now().toString());
	}

	private void pauseRequestProcessingThread() {
		final String tName = Thread.currentThread().getName();
		System.out.println("Request processing thread: " + tName + " started pause @: "
				+ LocalDateTime.now());
		try {
			Thread.sleep(TimeUnit.SECONDS.toMillis(60));
			System.out.println("Request processing thread "
					+ tName + " resumed after pause @: " + LocalDateTime.now());
		} catch (InterruptedException ignore) {
			// Ignore: do nothing
		}
	}
}
