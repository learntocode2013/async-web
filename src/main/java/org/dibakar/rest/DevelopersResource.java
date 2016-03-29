package org.dibakar.rest;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("secured/developers")
public class DevelopersResource {

    @POST
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public Response create(@Valid Engineer newEngineer)
    {
        return Response.status(Response.Status.CREATED).entity(newEngineer).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Engineer create(@BeanParam DeveloperRequestBean requestBean)
    {
        return new Engineer(requestBean.name,requestBean.country,requestBean.exp);
    }

    @GET
    @Produces(MediaType.APPLICATION_XML)
    @Path("{firstname}-{lastname}")
    public Engineer getEngineerByName(@PathParam("firstname") String firstName, @PathParam("lastname") String lastName)
    {
        return new Engineer(firstName + "_" + lastName,"United States",40);
    }
}
