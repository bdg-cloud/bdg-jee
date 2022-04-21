package fr.legrain.bdg.webapp.dev;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.legrain.tiers.service.TaTiersService;

/*
 * http://stackoverflow.com/questions/4543936/load-the-image-from-outside-of-webcontext-in-jsf
 */

@WebServlet("/open-api/*")
public class LgrSwaggerOpenApiServlet extends HttpServlet {

	private static String chemin = null;

	TaTiersService s;

	private void initChemin() throws FileNotFoundException, IOException {
		String propertiesFileName = "bdg.properties";  

		Properties props = new Properties();  
		String path = System.getProperty("jboss.server.config.dir")+"/"+propertiesFileName;  

		if(new File(path).exists()) { 
			File f = new File(path);
			props.load(new FileInputStream(f));  
			chemin = props.getProperty("bdg.filesystem.path");
			if(chemin!=null) {
				chemin+="/bdg/bin/clients";
			}

		} else {  
			chemin = "/var/lgr/bdg/bin/clients";
		}  


	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		BufferedInputStream buf = null;
		try {
			//        String filename = request.getPathInfo().substring(1);
			//        if(chemin==null) {
			//        	initChemin();
			//        }
			//        File file = new File(chemin, filename);
			InputStream is = TaTiersService.class.getClassLoader().getResourceAsStream("META-INF/openapi.json");
			//response.setHeader("Content-Type", getServletContext().getMimetype(filename));
			//        response.setHeader("Content-Length", String.valueOf(file.length()));
			response.setHeader("Content-Disposition", "inline; filename=\"" + "openapi.json" + "\"");
			//        Files.copy(s, response.getOutputStream());

			buf = new BufferedInputStream(is);
			int readBytes = 0;
			//read from the file; write to the ServletOutputStream
			while ((readBytes = buf.read()) != -1) {
				response.getOutputStream().write(readBytes);
			}
		} catch (IOException ioe) {
			throw new ServletException(ioe.getMessage());
		} finally {
			if (response.getOutputStream() != null)
				response.getOutputStream().close();
			if (buf != null)
				buf.close();
		}
	}

}
