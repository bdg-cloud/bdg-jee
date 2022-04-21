package fr.legrain.bdg.webapp.app;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.legrain.moncompte.ws.TaCgv;

//http://stackoverflow.com/questions/14484555/unable-to-show-pdf-in-pmedia-generated-from-streamed-content-in-primefaces
@WebServlet("/cgv.pdf")
public class CgvServlet extends HttpServlet {

	private static final long serialVersionUID = 6123824500571416106L;

	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        byte[] content = ((TaCgv) request.getSession().getAttribute("cgv")).getBlobFichier();
        response.setContentType("application/pdf");
        response.setContentLength(content.length);
        response.getOutputStream().write(content);
    }

}