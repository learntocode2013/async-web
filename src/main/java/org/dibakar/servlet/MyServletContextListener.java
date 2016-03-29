package org.dibakar.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.concurrent.*;

// ----------------------------------------------------------------
// Initialize async thread pool instance for use by async servlets
//------------------------------------------------------------------
@WebListener
public class MyServletContextListener implements ServletContextListener {

	public MyServletContextListener() {
	}

	//-------------------------------------------------------------------------------
	// Create a global thread pool. Could be a container managed thread pool as well
	//-------------------------------------------------------------------------------
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		final int poolSize = Runtime.getRuntime()
				.availableProcessors();
		Executor thread_pool = Executors.newFixedThreadPool(poolSize);
		sce.getServletContext().setAttribute("thread_pool",thread_pool);
		System.out.println("Async thread-pool(with size: " + poolSize
				+ ") was initialized from custom context listener... ");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		System.out.println("Web application shutting down | Closing all thread pools...");
		Object thread_pool = sce.getServletContext().getAttribute("thread_pool");
		thread_pool = null;
	}
}
