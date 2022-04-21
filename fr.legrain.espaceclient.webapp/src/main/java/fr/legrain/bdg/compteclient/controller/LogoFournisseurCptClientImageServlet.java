package fr.legrain.bdg.compteclient.controller;

import java.io.IOException;

import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.legrain.bdg.compteclient.model.TaFournisseur;
import fr.legrain.bdg.compteclient.service.interfaces.remote.ITaFournisseurServiceRemote;

@WebServlet("/image_log_frs_cpt_client/*")
public class LogoFournisseurCptClientImageServlet extends HttpServlet {

	private static final long serialVersionUID = 6784744349391471937L;
	
	@EJB private ITaFournisseurServiceRemote service;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	Integer id = Integer.valueOf(request.getPathInfo().substring(1));
        TaFournisseur l;
		try {
			l = service.findById(id);
			
			if(l.getBlobLogo()!=null) {
		        response.setContentType(getServletContext().getMimeType(l.getCodeDossier()));
		        response.setContentLength(l.getBlobLogo().length);
		        response.getOutputStream().write(l.getBlobLogo	());
			}
        
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

}