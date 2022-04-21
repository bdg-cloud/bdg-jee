package fr.legrain.bdg.webapp.documents;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;

import fr.legrain.droits.service.SessionInfo;
import fr.legrain.droits.service.TenantInfo;

@WebServlet("/imprimer-liste/*")
public class ImprimerListeServlet extends HttpServlet {

	private static final long serialVersionUID = 3885870053375006222L;
	
	private @Inject TenantInfo tenantInfo;
	private @Inject SessionInfo sessionInfo;
//	private @Resource UserTransaction tx;

	private @Inject DocFusionAImprimer docFusionAImprimer;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletOutputStream out = response.getOutputStream();
		final int ARBITARY_SIZE = 1048;
		if(docFusionAImprimer!=null && docFusionAImprimer.getFichierAImprimer()!=null) {
			
//			ByteArrayOutputStream baos = new ByteArrayOutputStream();
//			byte[] boas = Files.readAllBytes(f);
			
//			File f = new File(docFusionAImprimer.getFichierAImprimer());
//		     byte[] baos = new byte[(int) f.length()];
//		     baos = FileUtils.readFileToByteArray(f);
		     
		     FileInputStream fis = new FileInputStream(docFusionAImprimer.getFichierAImprimer());
			
			response.setContentType("application/pdf");

//			response.setContentLength(fos.size());
			
			try(InputStream in = fis) {
			 
			            byte[] buffer = new byte[ARBITARY_SIZE];
			         
			            int numBytesRead;
			            while ((numBytesRead = in.read(buffer)) > 0) {
			                out.write(buffer, 0, numBytesRead);
			            }
			        }
			
		}
	}

	

}