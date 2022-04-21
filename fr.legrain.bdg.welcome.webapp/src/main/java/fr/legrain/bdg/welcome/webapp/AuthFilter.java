package fr.legrain.bdg.welcome.webapp;

import java.io.IOException;

import javax.faces.application.ResourceHandler;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter(filterName = "AuthFilter", urlPatterns = {/*"*.xhtml"*/ "/*"})
public class AuthFilter implements Filter {
	
	public AuthFilter() {
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		try {
			
			// check whether session variable is set
			HttpServletRequest req = (HttpServletRequest) request;
			HttpServletResponse res = (HttpServletResponse) response;
			HttpSession ses = req.getSession(false);
			
//	        Auth auth = null;
//	        if(req.getSession().getAttribute("auth")!=null) {
//	        	auth = (Auth) req.getSession().getAttribute("auth");//session scoped managed bean
//	        	//auth.getSessionInfo().setSessionID(req.getSession(false).getId());
//	        	//auth.getSessionInfo().setSessionID(((HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false)).getId());
//	        }
//	        
//	        boolean racineDuDomaine = false;
//	        String domaine = request.getServerName().substring(0,request.getServerName().lastIndexOf("."));//supprime le tld (dev.demo.promethee.biz => dev.demo.promethee)
//	        if(domaine.startsWith(bdgProps.getProperty(BdgProperties.KEY_PREFIXE_SOUS_DOMAINE))) {
//	        	domaine = domaine.replace(bdgProps.getProperty(BdgProperties.KEY_PREFIXE_SOUS_DOMAINE), "");
//	        }
//			if(domaine.indexOf(".")<0) {
//				//il n'y a pas de point dans le domaine, donc il n'y a pas de sous domaine
//				racineDuDomaine = true;
//			}
			
			String reqURI = req.getRequestURI();
			String nouveauSite = "https://www.bdg.cloud";
			
			if (req.getRequestURI().startsWith(req.getContextPath() + ResourceHandler.RESOURCE_IDENTIFIER)) {
			    chain.doFilter(request, response);
			    return;
			}

			if (reqURI.indexOf("nouveau_dossier")>=0) {
				//pour créer un nouveau dossier
			    chain.doFilter(request, response);
			    return;
			} else {		
//				res.sendRedirect("http://welcome."+request.getServerName());
				//302
				//res.sendRedirect("https://www."+request.getServerName());
				
				//301
				res.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
				res.setHeader("Location", nouveauSite);
				//chain.doFilter(request, response);
			}
//			if(auth!=null)auth.setTheme("icarus-green");
			
			
		}
		catch(Throwable t) {
			System.out.println( t.getMessage());
		}
	} 

	@Override
	public void destroy() {

	}
}