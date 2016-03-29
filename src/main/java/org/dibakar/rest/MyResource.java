package org.dibakar.rest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("{pathParam}/test")
public class MyResource {
    private int count ;
    @PathParam("pathParam") private String pathParamExample ;
    @QueryParam("query") private String queryParamExample ;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String testMethod() {
        count = count + 1 ;
        return "It works! This method was called " + count + " time(s) with path param " + pathParamExample
                + " and query param " + queryParamExample ;
    }
}
