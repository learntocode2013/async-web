package org.dibakar.rest.client;

import org.dibakar.rest.Engineer;
import org.dibakar.rest.LocalDateMessageBodyReader;

import javax.ws.rs.client.*;
import javax.ws.rs.core.Response;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RestApiClient {
    public static void main(String[] args) throws InterruptedException {
        RestApiClient client = new RestApiClient();

        /*
        Invocation invocation = client.prepareRequestForDate(null);
        LocalDateTime dateTime = invocation.invoke().readEntity(LocalDateTime.class);
        System.out.println("RESPONSE: " + dateTime.toString());

        invocation = client.prepareRequestForDate("today");
        System.out.println("Invocation Today: " + invocation.invoke().readEntity(String.class));

        invocation = client.prepareRequestForDate("tomorrow");
        System.out.println("Invocation Tomorrow: " + invocation.invoke().readEntity(String.class));

        invocation = client.prepareRequestForDate("yesterday");
        System.out.println("Invocation Yesterday: " + invocation.invoke().readEntity(String.class));
        */

        // POST requests
        CountDownLatch allDone = new CountDownLatch(200) ;
        ExecutorService tPoolServ = Executors.newFixedThreadPool(500);
        for( int i = 0 ; i < 1000 ; i++ ) {
            tPoolServ.submit(() -> {
                client.pumpDataToTomcat(client);
                allDone.countDown();
            });
        }

        allDone.await();
        tPoolServ.shutdownNow();
    }

    private void pumpDataToTomcat(RestApiClient client)
    {
        for( int i = 0 ; i < 5000 ; i++ ) {
            Engineer martin = new Engineer("Martin Fowler","United States",45);
            Invocation invocation = client.preparePostRequest(martin);

            Response postResponse = invocation.invoke();

            //System.out.println("Response status: " + postResponse.getStatus());

            if( 201 == postResponse.getStatus() || 202 == postResponse.getStatus() ) {
                //System.out.println(postResponse.readEntity(Engineer.class).toString());
            } else {
                System.out.println("Response status: " + postResponse.getStatus());
            }
        }
    }

    public Invocation prepareRequestForDate(String whichDay) {
        Client client = ClientBuilder.newBuilder().register(LocalDateMessageBodyReader.class).build();
        WebTarget baseTarget = client.target("http://localhost:8080/advanced-jaxrs/webapi");
        WebTarget dateTarget = baseTarget.path("date");

        if( null != whichDay && 0 != whichDay.trim().length() ) {
            WebTarget dynamicDateTarget = dateTarget.path("{value}");
            return dynamicDateTarget.resolveTemplate("value",whichDay).request().buildGet();
        }

        return dateTarget.request().buildGet();
    }

    public Invocation preparePostRequest(Engineer newEngineer) {
        Client client = ClientBuilder.newBuilder().register(LocalDateMessageBodyReader.class).build();
        WebTarget baseTarget = client.target("http://localhost:8080/advanced-jaxrs/webapi");
        WebTarget devTarget = baseTarget.path("async/developers");

        return devTarget
                .request()
                .buildPost(Entity.xml(newEngineer)) ;
    }
}
