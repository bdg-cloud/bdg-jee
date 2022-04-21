package fr.legrain.bdg.webapp.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
 * http://stackoverflow.com/questions/4543936/load-the-image-from-outside-of-webcontext-in-jsf
 */

@WebServlet("/client_rcp/*")
public class ClientRCPServlet extends HttpServlet {
	
	private static String chemin = null;
	
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
        String filename = request.getPathInfo().substring(1);
        if(chemin==null) {
        	initChemin();
        }
        File file = new File(chemin, filename);
        //response.setHeader("Content-Type", getServletContext().getMimetype(filename));
        response.setHeader("Content-Length", String.valueOf(file.length()));
        response.setHeader("Content-Disposition", "inline; filename=\"" + filename + "\"");
        Files.copy(file.toPath(), response.getOutputStream());
    }

}
