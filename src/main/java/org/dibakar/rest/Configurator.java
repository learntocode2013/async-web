package org.dibakar.rest;


import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;

@Provider
public class Configurator implements DynamicFeature
{
    @Override
    public void configure(ResourceInfo resourceInfo, FeatureContext context)
    {
        applySecurityForResource(resourceInfo,DevelopersResource.class.getName(),context);
    }

    private void applySecurityForResource(ResourceInfo resourceInfo, String forResourceWithName,FeatureContext context)
    {
        if( resourceInfo.getResourceClass().getName().contains(forResourceWithName))
        {
            context.register(SecurityFilter.class);
        }
    }
}
