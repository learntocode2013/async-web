package org.dibakar.rest;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.container.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Path("async/developers")
@Singleton
public class AsyncDevelopersResource {
    private static final BlockingQueue<AsyncResponse> suspended =
            new ArrayBlockingQueue<AsyncResponse>(5);
    private static final AtomicInteger failCount = new AtomicInteger(0);

    @GET
    @Produces(MediaType.APPLICATION_XML)
    public void getDeveloperOnCreate(@Suspended AsyncResponse asyncResponse) throws InterruptedException
    {
        asyncResponse.setTimeout(20, TimeUnit.SECONDS);
        asyncResponse.setTimeoutHandler(getTimeOutHandler());
        asyncResponse.register(getCompletionCallbackHandler(),getConnectionCallbackHandler());
        suspended.put(asyncResponse);
    }

    private TimeoutHandler getTimeOutHandler() {
        final TimeoutHandler timeoutHandler = asyncResp -> {
            asyncResp.resume(Response.status(Response.Status
                    .SERVICE_UNAVAILABLE).entity("TIMED OUT !").build());
        } ;
        return timeoutHandler;
    }

    private CompletionCallback getCompletionCallbackHandler() {
        final CompletionCallback completionCallback = throwable -> {
            if( null == throwable ) {
                System.out.println("Async response was successfully processed...");
            } else {
                System.err.println("Count of async request failures: " + failCount.incrementAndGet());
            }
        };
        return completionCallback;
    }

    private ConnectionCallback getConnectionCallbackHandler() {
        final ConnectionCallback connectionCallback = asyncResp -> {
            // no need to process disconnected client requests
            if( ! asyncResp.isDone() ) {
                asyncResp.cancel();
            }
        } ;
        return connectionCallback ;
    }

    @POST
    @Consumes(MediaType.APPLICATION_XML)
    public Response create(Engineer newEng) throws InterruptedException
    {
        // a new engineer was created.
        // broadcast this new domain object to all listeners
        broadcastToListeners(newEng);
        return Response.ok().status(Response.Status.ACCEPTED).build();
    }

    private void broadcastToListeners(Engineer newEng) throws InterruptedException
    {
        final Iterator<AsyncResponse> asyncResponseIterator = suspended.iterator();
        while ( asyncResponseIterator.hasNext() )
        {
            AsyncResponse response = suspended.poll(1,TimeUnit.SECONDS);
            if( null != response) response.resume(newEng);
            Thread.sleep(TimeUnit.SECONDS.toMillis(1));
        }
    }
}
