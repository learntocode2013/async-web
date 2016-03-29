package org.dibakar.rest;

import org.glassfish.jersey.internal.util.Base64;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;


public class SecurityFilter implements ContainerRequestFilter {
    private final String AUTHORIZATION_HEADER = "Authorization";
    private final String AUTHORIZATION_VALUE_PREFIX = "Basic";
    private final String SECURE_PATH_INDICATION = "secured";

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        if (requestContext.getUriInfo().getPath().contains(SECURE_PATH_INDICATION)) {
            final List<String> authHeaders = requestContext.getHeaders().get(AUTHORIZATION_HEADER);
            if ( null != authHeaders && ! authHeaders.isEmpty() ) {
                String authToken = authHeaders.get(0);
                authToken = authToken.replaceFirst(AUTHORIZATION_VALUE_PREFIX, "").trim();
                final String plainAuthValue = Base64.decodeAsString(authToken.getBytes());
                final String[] credentials = plainAuthValue.split(":");

                System.out.println("Decoded auth string: " + plainAuthValue);
                System.out.println("Credentials : " + credentials[0] + " | " + credentials[1]);

                final String username = credentials[0];
                final String password = credentials[1];

                if ("user".equals(username) && "password".equals(password)) {
                    return;
                }
            }

            Response unathorizedResponse = Response
                    .status(Response.Status.UNAUTHORIZED)
                    .build();
            requestContext.abortWith(unathorizedResponse);
        }

    }
}
