package org.dibakar.rest;

import org.glassfish.jersey.server.ServerProperties;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@ApplicationPath("webapi")
public class JAXRSConfiguration extends Application {
    private final Set<Class<?>> resources           = new HashSet<>();
    private final Set<Object> singletons          = new HashSet<>();
    private final Map<String,Object> appProperties  = new HashMap<>();

    @Override
    public Set<Class<?>> getClasses() {
        resources.add(MyResource.class);
        resources.add(DateResource.class);
        resources.add(MyDateConvertorProvider.class);
        resources.add(LocalDateMessageBodyWriter.class);
        resources.add(DevelopersResource.class);
        resources.add(PoweredByResponseFilter.class);
        resources.add(LoggingFilter.class);
        resources.add(Configurator.class);
        resources.add(AsyncDevelopersResource.class);
        return resources;
    }

    @Override
    public Set<Object> getSingletons() {
        singletons.add(AsyncDevelopersResource.class);
        return singletons;
    }

    @Override
    public Map<String, Object> getProperties() {
        appProperties.putIfAbsent(ServerProperties.BV_SEND_ERROR_IN_RESPONSE,true);
        return appProperties;
    }
}
