package fr.legrain.bdg.rest.interceptors.reader;

import java.io.IOException;

import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;

@Provider
//https://dennis-xlc.gitbooks.io/restful-java-with-jax-rs-2-0-en/cn/part1/chapter12/reader_and_writer_interceptors.html
/*
 * Actions sur le BODY 
 */
public class GZIPDecoder implements ReaderInterceptor {
   public Object aroundReadFrom(ReaderInterceptorContext ctx) throws IOException {
//      String encoding = ctx.getHeaders().getFirst("Content-Encoding");
//      if (!"gzip".equalsIgnoreCase(encoding)) {
//         return ctx.proceed();
//      }
//      GZipInputStream is = new GZipInputStream(ctx.getInputStream());
//      ctx.setInputStream(is);
//      return ctx.proceed(is);
      
      return null;
   }
}
