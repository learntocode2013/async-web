package org.dibakar.rest;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Provider
@Produces(value = {MediaType.TEXT_PLAIN,"text/datetime"})
public class LocalDateMessageBodyWriter implements MessageBodyWriter{
    @Override
    public long getSize(Object o, Class aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    @Override
    public boolean isWriteable(Class aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return LocalDateTime.class.isAssignableFrom(aClass) ;
    }

    @Override
    public void writeTo(Object o, Class aClass, Type type, Annotation[] annotations, MediaType mediaType,
                        MultivaluedMap multivaluedMap, OutputStream outputStream) throws IOException,
            WebApplicationException {
        LocalDateTime dateTime = (LocalDateTime)o;
        outputStream.write(dateTime.toString().getBytes());
    }
}
