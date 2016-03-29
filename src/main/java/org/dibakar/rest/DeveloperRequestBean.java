package org.dibakar.rest;


import javax.ws.rs.QueryParam;

public class DeveloperRequestBean {
    @QueryParam("name")
    public String name ;

    @QueryParam("country")
    public String country ;

    @QueryParam("exp")
    public int exp ;
}
