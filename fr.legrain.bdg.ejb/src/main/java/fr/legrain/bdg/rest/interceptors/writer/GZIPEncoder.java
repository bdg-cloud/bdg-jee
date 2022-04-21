package fr.legrain.bdg.rest.interceptors.writer;

import java.io.IOException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;

@Provider
//https://dennis-xlc.gitbooks.io/restful-java-with-jax-rs-2-0-en/cn/part1/chapter12/reader_and_writer_interceptors.html
/*
 * Actions sur le BODY 
 */
public class GZIPEncoder implements WriterInterceptor {

   public void aroundWriteTo(WriterInterceptorContext ctx)  throws IOException, WebApplicationException {
//      GZIPOutputStream os = new GZIPOutputStream(ctx.getOutputStream());
//      ctx.getHeaders().putSingle("Content-Encoding", "gzip");
//      ctx.setOutputStream(os);
//      ctx.proceed();
      return;
   }
}
