package fr.legrain.bdg.ws.rest.client;

import java.io.IOException;

import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;
import jakarta.ws.rs.ext.Provider;

@Provider
public class RequestClientFilter implements ClientRequestFilter {

	public static final String AccessControlAllowOrigin = "Access-Control-Allow-Origin";
	public static final String XDossier = "X-Dossier";
	public static final String XLgr = "X-Lgr";
	public static final String XBdglogin = "X-Bdg-login";
	public static final String XBdgpassword = "X-Bdg-password";
	public static final String Authorization = "Authorization";

	@Override
	public void filter(ClientRequestContext requestContext) throws IOException {
		//requestContext.setProperty("test", "test client request filter");
		if(!requestContext.getHeaders().containsKey(AccessControlAllowOrigin))
			requestContext.getHeaders().add(AccessControlAllowOrigin, "*");

		if(!requestContext.getHeaders().containsKey(XDossier) && BdgRestClient.dossier!=null)
			requestContext.getHeaders().add(XDossier, BdgRestClient.dossier);

		if(!requestContext.getHeaders().containsKey(XLgr))
			requestContext.getHeaders().add(XLgr, "aa");

		if(!requestContext.getHeaders().containsKey(XBdglogin) && BdgRestClient.login!=null)
			requestContext.getHeaders().add(XBdglogin, BdgRestClient.login);

		if(!requestContext.getHeaders().containsKey(XBdgpassword) && BdgRestClient.password!=null)
			requestContext.getHeaders().add(XBdgpassword, BdgRestClient.password);

		if(!requestContext.getHeaders().containsKey(Authorization) && BdgRestClient.accessToken!=null)
			requestContext.getHeaders().add(Authorization, "Bearer "+BdgRestClient.accessToken);

	}
}