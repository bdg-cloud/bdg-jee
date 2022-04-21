package fr.legrain.bdg.rest.filters.response;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

import fr.legrain.bdg.rest.AbstractRestService;
import fr.legrain.bdg.rest.filters.AbstractLogFilter;
import fr.legrain.general.service.ThreadLocalContextHolder;

@Provider
/*
 * Actions sur le HEADER
 */
//https://dennis-xlc.gitbooks.io/restful-java-with-jax-rs-2-0-en/cn/part1/chapter12/server_side_filters.html
public class LogFilter extends AbstractLogFilter implements ContainerResponseFilter {

	@Context
	private HttpServletRequest servletRequest;

	public void filter(ContainerRequestContext req, ContainerResponseContext res) throws IOException {


		String XDossier = req.getHeaderString(AbstractRestService.X_HEADER_DOSSIER);

		if(XDossier!=null && req.getProperty(AbstractLogFilter.BDG_UUID_REQUEST)!=null) {
			String uuid = null;
//			if(req.getProperty(AbstractLogFilter.BDG_UUID_REQUEST)!=null) {
				uuid =  (String) req.getProperty(AbstractLogFilter.BDG_UUID_REQUEST);
//			}
			logGeneralApi(uuid,req, res, XDossier, null/*claims.getBody().getSubject()*/,null);
		}
		
		ThreadLocalContextHolder.cleanupThread();

	}




}