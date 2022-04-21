package fr.legrain.bdg.welcome.webapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.legrain.bdg.lib.server.osgi.BdgProperties;

/*
 * http://stackoverflow.com/questions/4543936/load-the-image-from-outside-of-webcontext-in-jsf
 */

/*
User-agent: *
Disallow: /
 */
@WebServlet("/robots.txt")
public class RobotsTxtServlet extends HttpServlet {
	
	private static String chemin = null;
	private static Properties props = null; 
	
	private void initChemin() throws FileNotFoundException, IOException {
		 String propertiesFileName = "bdg.properties";  
		 
		 String path = null;
			if(path==null) {
				if(System.getProperty(BdgProperties.KEY_MODE_DOMAINE_WILDFLY)!=null &&
						System.getProperty(BdgProperties.KEY_MODE_DOMAINE_WILDFLY).equals("true"))
					path = System.getProperty(BdgProperties.KEY_JBOSS_DOMAIN_CONFIG_DIR)+"/"+propertiesFileName;
				else 
					path = System.getProperty(BdgProperties.KEY_JBOSS_SERVER_CONFIG_DIR)+"/"+propertiesFileName;
			}
	      
//		    String path = System.getProperty("jboss.server.config.dir")+"/"+propertiesFileName;  
		      
		    if(new File(path).exists()) { 
		    	File f = new File(path);
		    	props = new Properties(); 
		        props.load(new FileInputStream(f));  
		        chemin = props.getProperty(BdgProperties.KEY_BDG_FILESYSTEM_PATH);
		        if(chemin!=null) {
		        	chemin+="/bdg/bin/clients";
		        }
		       
		    } else {  
		    	chemin = "/var/lgr/bdg/bin/clients";
		    }  


	}
	
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(props==null) {
        	initChemin();
        }
//        File file = new File(chemin, filename);
//        //response.setHeader("Content-Type", getServletContext().getMimetype(filename));
//        response.setHeader("Content-Length", String.valueOf(file.length()));
//        response.setHeader("Content-Disposition", "inline; filename=\"" + filename + "\"");
    	 //Files.copy(file.toPath(), response.getOutputStream());
    	
    	response.setHeader("Content-Type", "text/plain");
        PrintWriter writer = response.getWriter();
         
    	if(props.getProperty(BdgProperties.KEY_NOM_DOMAINE).equals("bdg.cloud")) {
    		writer.write("User-agent: *\n");
    		writer.write("Allow: /\n");
    	} else {
    		//devbdg.work ou pprodbdg.work
    		writer.write("User-agent: *\n");
    		writer.write("Disallow: /\n");
    	}
       
       
        writer.close();
    }

}
