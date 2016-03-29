package org.dibakar.rest.client;


import org.dibakar.rest.Engineer;
import org.dibakar.rest.LocalDateMessageBodyReader;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import java.util.List;

public class GenericDemo {
    public static void main(String[] args) {
        GenericDemo client = new GenericDemo();
        Invocation invocation = client.prepareGetRequest();
        List<Engineer> engineers = invocation.invoke().readEntity(new GenericType<List<Engineer>>(){});
        System.out.println("-- List of engineers --");
        //engineers.stream().forEach( engineer -> System.out.println(engineer.getName()));
    }

    public Invocation prepareGetRequest() {
        Client client = ClientBuilder.newBuilder().register(LocalDateMessageBodyReader.class).build();
        WebTarget baseTarget = client.target("http://localhost:8080/advanced-jaxrs/webapi");
        WebTarget devTarget = baseTarget.path("developers");

        return devTarget
                .request()
                .buildGet();
    }
}
