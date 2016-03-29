package org.dibakar.servlet;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet(name = "ProgressAsyncServlet", asyncSupported = true, urlPatterns = {"/progressAsyncServlet"})
public class ProgressAsyncServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		final String containerThread =  Thread.currentThread().getName() ;
		System.out.println("ProgressAsyncServlet.doGet being serviced by -> " + containerThread);
		resp.setContentType("text/html");

		//-------- Setup the AsyncContext
		AsyncContext asyncContext = req.startAsync();
		asyncContext.addListener(new ProgressAsyncListener());
		//asyncContext.setTimeout(TimeUnit.SECONDS.toMillis(12));

		//-------- Register a write listener for container callbacks
		final ServletOutputStream outStream = resp.getOutputStream();

		req.getAsyncContext().start(() -> {
			System.out.println("Container thread allocated for async: "
					+ Thread.currentThread().getName());
			outStream.setWriteListener(new ProgressWriteListener(asyncContext,outStream));
		});

		//------- Track that the request processing thread is released
		System.out.println(containerThread + " being released @ " + LocalDateTime.now());
	}
}
