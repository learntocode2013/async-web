package org.dibakar.servlet;

import javax.servlet.AsyncContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProgressWriteListener implements WriteListener {
	private final AsyncContext context;
	private final ServletOutputStream outputStream;
	private final int BOUND = 100 ;
	private int progressCount = 0 ;
	private int visitIndex = 0 ;


	private List<String> linesToPrint = new ArrayList<>();

	public ProgressWriteListener(AsyncContext context, ServletOutputStream output) {
		this.context = context;
		outputStream = output;
		//-------------- Add lines to be sent over the wire.
		linesToPrint = addOutputContent(linesToPrint);
	}

	private List<String> addOutputContent(List<String> linesToPrint) {
		linesToPrint.add("<html><head><title>Progress async servlet</title></head><body>");
		linesToPrint.add("entering doGet => thread name: " + Thread.currentThread().getName() + "<br>");
		linesToPrint.add("<progress id='progress' max='100'></progress>");
		linesToPrint.add("<p>async context start being executed by thread: "
				+ Thread.currentThread().getName() + "</p><br>");
		while( progressCount < BOUND ) {
			linesToPrint.add("<script>document.getElementById('progress').value=\""+
						progressCount++	+"\"</script>");
		}
		linesToPrint.add("<script>document.getElementById('progress').style.display='none';</script>");
		linesToPrint.add("<p>exiting doGet => thread name: " + Thread.currentThread().getName() + "</p><br>");
		linesToPrint.add("</body></html>");
		System.out.println("Unsent line count : " + linesToPrint.size());
		return linesToPrint;
	}

	@Override
	public void onWritePossible() throws IOException {
		final String containerThread = Thread.currentThread().getName();
		// -------- attempt to write until we are done
		System.out.println(Thread.currentThread().getName() + " called onWritePossible");
		while(!linesToPrint.isEmpty()) {
			System.out.println(containerThread + " servicing writes at time: " + LocalDateTime.now());
			// -------- as long as I can write
			boolean canWrite = outputStream.isReady();
			if( ! canWrite ) {
				System.out.println(containerThread + " returning since  writes is not possible at time: " + LocalDateTime.now());
				return;
			}
			if(canWrite) {
				final byte[] bytes = getCustomizedLine(linesToPrint.remove(visitIndex)).getBytes();
				outputStream.write(bytes);
				visitIndex++;
				System.out.println(Thread.currentThread().getName() + " sent some data" +
						" out | visitIndex: " + visitIndex + " | Remaining line count: "
						+ linesToPrint.size());
			} else {
				System.out.println("OutStream not ready for write | Thread: " + containerThread);
			}
		}

		// ------ signal that we are done writing
		if( linesToPrint.isEmpty() ) {
			context.complete();
			System.out.println(containerThread + " signalled async operation complete at " + LocalDateTime.now());
		}
	}

	private String getCustomizedLine(String line) {
		return line ;
	}

	@Override
	public void onError(Throwable t) {
		context.complete();
	}
}
