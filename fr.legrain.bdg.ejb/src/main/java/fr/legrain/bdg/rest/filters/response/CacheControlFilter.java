package fr.legrain.bdg.rest.filters.response;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.ext.Provider;

@Provider
/*
 * Actions sur le HEADER
 */
//https://dennis-xlc.gitbooks.io/restful-java-with-jax-rs-2-0-en/cn/part1/chapter12/server_side_filters.html
public class CacheControlFilter implements ContainerResponseFilter {
   
	public void filter(ContainerRequestContext req, ContainerResponseContext res) throws IOException {
//      if (req.getMethod().equals("GET")) {
//         CacheControl cc = new CacheControl();
//         cc.setMaxAge(100);
//         req.getHeaders().add("Cache-Control", cc.toString());
//      }
		
//      CacheControl cc = new CacheControl();
//      cc.setMaxAge(100);
//      req.getHeaders().add("Access-Control-Allow-Origin","*"); 
   
   }
	
}