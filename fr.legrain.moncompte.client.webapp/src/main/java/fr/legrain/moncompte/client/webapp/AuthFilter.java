package fr.legrain.moncompte.client.webapp;

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

import fr.legrain.moncompte.model.TaUtilisateur;

//import fr.legrain.droits.model.TaAuthURL;

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
			if (reqURI.indexOf("renouvellement")>=0) {
				//pour la page de renouvellemnt
			    chain.doFilter(request, response);
			    return;
			}
			if (reqURI.indexOf("rest")>=0) {
				//pour api REST
			    chain.doFilter(request, response);
			    return;
			}
			
//			if(
//				(reqURI.indexOf("dev_careco_template.xhtml") >= 0 )
//					//	|| (reqURI.indexOf("monitoring") >= 0) 
//					){
//					chain.doFilter(request, response); //pour dev
//					return;
//			}
			
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

				if (user==null) {

//					if (auth==null) {
						if(reqURI.indexOf("login") >= 0 )
							chain.doFilter(request, response);
						else
							res.sendRedirect(req.getContextPath() + "/login/login.xhtml");
					} else if(reqURI.indexOf("login") >= 0 ) {
						chain.doFilter(request, response);
					} else {
//						//chain.doFilter(request, response);
//						List<TaAuthURL> restrictedURL =  auth.restrictedURL();
//						Iterator<TaAuthURL> ite = restrictedURL.iterator();
//						boolean trouve = false;
//						if(!restrictedURL.isEmpty()) {
//							do {
//								TaAuthURL url = ite.next();
//								if ( reqURI.indexOf(url.getUrl()) >= 0 ) {
//									res.sendRedirect(req.getContextPath() + "/erreur/403.xhtml");
//									trouve = true;
//								}
//							} while (ite.hasNext() && !trouve);
//						}
//						if(!trouve) {
							chain.doFilter(request, response);
//						}
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