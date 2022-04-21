package fr.legrain.bdg.webapp.articles;

import java.io.IOException;

import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.legrain.article.model.TaLabelArticle;
import fr.legrain.bdg.article.service.remote.ITaLabelArticleServiceRemote;

@WebServlet("/image_label_article/*")
public class LabelArticleImageServlet extends HttpServlet {

	private static final long serialVersionUID = -9075718678533434258L;
	
	@EJB private ITaLabelArticleServiceRemote service;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	Integer id = Integer.valueOf(request.getPathInfo().substring(1));
        TaLabelArticle l;
		try {
			l = service.findById(id);
		
	        response.setContentType(getServletContext().getMimeType(l.getCodeLabelArticle()));
	        response.setContentLength(l.getBlobLogo().length);
	        response.getOutputStream().write(l.getBlobLogo());
        
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

}