package fr.legrain.bdg.webapp.servicewebexterne;

import java.io.IOException;

import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.legrain.bdg.servicewebexterne.service.remote.ITaServiceWebExterneServiceRemote;
import fr.legrain.servicewebexterne.model.TaServiceWebExterne;

@WebServlet("/image_service_web_externe/*")
public class ServiceWebExterneImageServlet extends HttpServlet {

	private static final long serialVersionUID = -9075718678533434258L;
	
	@EJB private ITaServiceWebExterneServiceRemote service;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	Integer id = Integer.valueOf(request.getPathInfo().substring(1));
        TaServiceWebExterne l;
		try {
			l = service.findById(id);
		
	        response.setContentType(getServletContext().getMimeType(l.getCodeServiceWebExterne()));
	        response.setContentLength(l.getLogo().length);
	        response.getOutputStream().write(l.getLogo());
        
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

}