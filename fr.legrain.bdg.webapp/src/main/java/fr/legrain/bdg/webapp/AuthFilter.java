package fr.legrain.bdg.webapp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

import fr.legrain.bdg.lib.server.osgi.BdgProperties;
import fr.legrain.droits.model.TaAuthURL;
import fr.legrain.droits.model.TaUtilisateur;

@WebFilter(filterName = "AuthFilter", urlPatterns = {/*"*.xhtml"*/ "/*"})
public class AuthFilter implements Filter {
	
	private BdgProperties bdgProps = new BdgProperties();

	public AuthFilter() {
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		try {
			
			//boolean maintenance = ConstWeb.maintenance;

			// check whether session variable is set
			HttpServletRequest req = (HttpServletRequest) request;
			HttpServletResponse res = (HttpServletResponse) response;
			HttpSession ses = req.getSession(false);
			
	        TaUtilisateur user = null;
	        if(req.getSession().getAttribute("userSession")!=null) {
	        	user = (TaUtilisateur) req.getSession().getAttribute("userSession");
	        }
	        
	        Auth auth = null;
	        if(req.getSession().getAttribute("auth")!=null) {
	        	auth = (Auth) req.getSession().getAttribute("auth");//session scoped managed bean
	        	//auth.getSessionInfo().setSessionID(req.getSession(false).getId());
	        	//auth.getSessionInfo().setSessionID(((HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false)).getId());
	        }
	        
	        boolean racineDuDomaine = false;
	        String domaine = request.getServerName().substring(0,request.getServerName().lastIndexOf("."));//supprime le tld (dev.demo.promethee.biz => dev.demo.promethee)
	        if(domaine.startsWith(bdgProps.getProperty(BdgProperties.KEY_PREFIXE_SOUS_DOMAINE))) {
	        	domaine = domaine.replace(bdgProps.getProperty(BdgProperties.KEY_PREFIXE_SOUS_DOMAINE), "");
	        }
			if(domaine.indexOf(".")<0) {
				//il n'y a pas de point dans le domaine, donc il n'y a pas de sous domaine
				racineDuDomaine = true;
			}

			if(domaine.startsWith("oauth.")) {
				chain.doFilter(request, response);
			    return;
			}
			
			//  allow user to proccede if url is login.xhtml or user logged in or user is accessing any page in //public folder
			String reqURI = req.getRequestURI();
			String loginMaint = request.getParameter("loginMaint");
			
			if (req.getRequestURI().startsWith(req.getContextPath() + ResourceHandler.RESOURCE_IDENTIFIER)) {
			    chain.doFilter(request, response);
			    return;
			}
			if (reqURI.indexOf("primepush")>=0) {
				//pour atmosphere
			    chain.doFilter(request, response);
			    return;
			}
			if (reqURI.indexOf("openapi")>=0) {
				//pour atmosphere
			    chain.doFilter(request, response);
			    return;
			}
			if (reqURI.indexOf("developer/login.xhtml")>=0) {
				//pour atmosphere
			    chain.doFilter(request, response);
			    return;
			} else if(reqURI.indexOf("developer") >= 0 ) {
				DocApi docApi = (DocApi) req.getSession().getAttribute("docApi");
				if(docApi!=null && docApi.isConnecte()) {
//					res.sendRedirect(req.getContextPath() + "/developer/login.xhtml");
//				} else {
					chain.doFilter(request, response);
					 return;
				}
				
			}
			if (reqURI.indexOf("ArquillianServletRunner")>=0) {
				//pour les test Arquillian
			    chain.doFilter(request, response);
			    return;
			}
			if (reqURI.indexOf("nouveau_dossier")>=0) {
				//pour créer un nouveau dossier
			    chain.doFilter(request, response);
			    return;
			}
//			if (reqURI.indexOf("output?__report=/reports/test")>=0) {
			if (reqURI.indexOf("reports")>=0) {
				//pour lancer une édition Birt à partir de l'extérieur (compte client)
			    chain.doFilter(request, response);
			    return;
			}
//			if (reqURI.indexOf("rest/")>=0) {
			if (reqURI.indexOf("v1/")>=0) {
				//pour lancer une édition Birt à partir de l'extérieur (compte client)
			    chain.doFilter(request, response);
			    return;
			}
			if (reqURI.indexOf("swagger")>=0
					|| reqURI.indexOf("open-api")>=0) {
				//pour lancer une édition Birt à partir de l'extérieur (compte client)
			    chain.doFilter(request, response);
			    return;
			}
			if(racineDuDomaine) {		
//				res.sendRedirect("http://welcome."+request.getServerName());
				res.sendRedirect("https://www."+request.getServerName());
				//chain.doFilter(request, response);
			}
//			if (reqURI.indexOf("/m")>=0) {
//			    if(auth!=null)auth.setTheme("volt");
//			    if(user!=null){
//			    chain.doFilter(request, response);
//			    return;
//			    }
//			}else{
				if(auth!=null)auth.setTheme("icarus-green");
//			}
			
			if(
				(reqURI.indexOf("gensvg") >= 0 ) //pour la serlvet d'édition de code barre
					//	|| (reqURI.indexOf("monitoring") >= 0) 
					){
					chain.doFilter(request, response); //pour dev
					return;
			}
			
			if(ConstWeb.maintenance) {
				if(reqURI.indexOf("maintenance.xhtml") >= 0 
						|| (loginMaint!=null && loginMaint.equals("1") 
						|| (auth!=null && auth.getLoginMaint()!=null && auth.getLoginMaint().equals("1")))
						) {
					chain.doFilter(request, response);
				} else {
					res.sendRedirect(req.getContextPath() + "/maintenance.xhtml");
				}
			} else {

				if(reqURI.indexOf("developer") >= 0 ) {
					DocApi docApi = (DocApi) req.getSession().getAttribute("docApi");
					if(docApi==null || !docApi.isConnecte()) {
						res.sendRedirect(req.getContextPath() + "/developer/login.xhtml");
					} else {
						chain.doFilter(request, response);
					}
				} else 
				 if (user==null) {

//					if (auth==null) {
						if(reqURI.indexOf("login") >= 0 )
							chain.doFilter(request, response);
						else {
//							if (reqURI.indexOf("/m")>=0) {
//								res.sendRedirect(req.getContextPath() + "/m/login/login.xhtml");
//							}else	{
								res.sendRedirect(req.getContextPath() + "/login/login.xhtml");
//							}
						}
					} else if(reqURI.indexOf("login") >= 0 ) {
						chain.doFilter(request, response);
					} else {
						//chain.doFilter(request, response);
						List<TaAuthURL> restrictedURL = new ArrayList<>();
						if(auth!=null) {
							restrictedURL = auth.restrictedURL();
						}
						Iterator<TaAuthURL> ite = restrictedURL.iterator();
						boolean trouve = false;
						if(!restrictedURL.isEmpty()) {
							do {
								TaAuthURL url = ite.next();
								if ( reqURI.indexOf(url.getUrl()) >= 0 ) {
									res.sendRedirect(req.getContextPath() + "/erreur/403.xhtml");
									trouve = true;
								}
							} while (ite.hasNext() && !trouve);
						}
						if(!trouve) {
							chain.doFilter(request, response);
						}
//					}
				}
			}
			
		}
		catch(Throwable t) {
			System.out.println( t.getMessage());
		}
	} //doFilter

	@Override
	public void destroy() {

	}
}