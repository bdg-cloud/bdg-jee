package fr.legrain.bdg.webapp.edition.birt;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLDecoder;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import fr.legrain.bdg.servicewebexterne.service.remote.ITaServiceWebExterneServiceRemote;

@WebServlet(
        name = "BdgEditionServlet",
        urlPatterns = {"/edition"}
//        asyncSupported = false,
//        initParams = {
//                @WebInitParam(name = "fichier", value = "admin")
//                @WebInitParam(name = "param1", value = "value1"),
//                @WebInitParam(name = "param2", value = "value2")
//        }
)
public class EditionServlet extends HttpServlet {

	private static final long serialVersionUID = -9075718678533434258L;
	
	@EJB private ITaServiceWebExterneServiceRemote service;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//    	Integer id = Integer.valueOf(request.getPathInfo().substring(1));
//        TaServiceWebExterne l;
		try {
//			String f = getServletConfig().getInitParameter("fichier");
			String f = request.getParameter("fichier");
			f = URLDecoder.decode(f, "UTF-8");
			 
//			l = service.findById(id);
			byte[] a = IOUtils.toByteArray(new FileInputStream(f));
		
	       // response.setContentType(getServletContext().getMimeType(l.getCodeServiceWebExterne()));
	        response.setContentLength(a.length);
	        response.getOutputStream().write(a);
        
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

}