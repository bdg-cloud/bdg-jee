package fr.legrain.bdg.ws.rest.client;

import java.io.IOException;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.ext.WriterInterceptor;
import jakarta.ws.rs.ext.WriterInterceptorContext;

@Provider
public class RequestClientWriterInterceptor implements WriterInterceptor {

    @Override
    public void aroundWriteTo(WriterInterceptorContext context) 
      throws IOException, WebApplicationException {
//        context.getOutputStream()
//          .write(("Message added in the writer interceptor in the client side").getBytes());

        context.proceed();
    }
}