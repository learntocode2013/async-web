package org.dibakar.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.time.LocalDateTime;

@Path("secured/date")
public class DateResource {

    @GET
    @Path("{dateString}")
    @Produces(MediaType.APPLICATION_JSON)
    public String displayDate(@PathParam("dateString") MyDate myDate) {
        if( null == myDate ) return "" ;
        return "Got " + myDate.toString();
    }

    @GET
    @Produces(value = {MediaType.TEXT_PLAIN,"text/datetime"})
    public LocalDateTime getCurrentDate() {
        return LocalDateTime.now();
    }
}
