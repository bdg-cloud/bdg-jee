package fr.legrain.bdg.rest.filters;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Date;
import java.util.Iterator;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.core.Context;

import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.legrain.bdg.general.service.remote.ITaLogDossierServiceRemote;
import fr.legrain.general.model.TaLogDossier;

public class AbstractLogFilter {

	public static final String BDG_UUID_REQUEST = "BDG_UUID_REQUEST";

	private @EJB ITaLogDossierServiceRemote taLogDossierService;

	@Context
	private HttpServletRequest servletRequest;
	private ObjectMapper om = new ObjectMapper();

	private TaLogDossier initLogApi(TaLogDossier log, ContainerRequestContext requestContext,String dossier,String utilisateur, String message) {
		try {
			log.setUuid((String) requestContext.getProperty(BDG_UUID_REQUEST));
			log.setQuand(new Date());
			log.setUrlApi(requestContext.getUriInfo().getAbsolutePath().toString());
			log.setAppelDistant(true);
			log.setDossier(dossier);
			log.setMessage(message);
			log.setMethodeHttpApi(requestContext.getMethod());
			log.setUtilisateur(utilisateur);
			log.setParametreEnteteApi(om.writeValueAsString(requestContext.getHeaders()));
			log.setParametreRequeteApi(om.writeValueAsString(requestContext.getUriInfo().getQueryParameters()));
//			log.setCorpsRequeteApi(om.writeValueAsString(requestContext.getEntityStream()));
			
//			ByteArrayOutputStream baos = new ByteArrayOutputStream();
//			byte[] byteArray = IOUtils.toByteArray(requestContext.getEntityStream());     
//		    InputStream input1 = new ByteArrayInputStream(byteArray);

			ByteArrayOutputStream result = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			for (int length; (length = requestContext.getEntityStream().read(buffer)) != -1; ) {
				result.write(buffer, 0, length);
			}
//			log.setCorpsRequeteApi(om.writeValueAsString(result.toString("UTF-8")));
			log.setCorpsRequeteApi(result.toString("UTF-8"));
			

			log.setIpDistante(servletRequest.getRemoteAddr());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return log;
	}


	public void logGeneralApi(ContainerRequestContext requestContext,String dossier,String utilisateur, String message) {
		TaLogDossier log = new TaLogDossier();
		log = initLogApi(log,requestContext,dossier,utilisateur,message);

		taLogDossierService.merge(log);
	}

	public void logGeneralApi(String requestLogIdentifier, ContainerRequestContext req, ContainerResponseContext res, String dossier,String utilisateur, String message) {
		TaLogDossier log = null;
		try {
			if(requestLogIdentifier!=null) { //mise a jour d'un log existant
				log = taLogDossierService.findByUUID(requestLogIdentifier);
			} else {
				//requete
				log = new TaLogDossier();
				log = initLogApi(log,req,dossier,utilisateur,message);
			}
			if(log!=null) {
				//réponse
				log.setEtat(res.getStatus()+" - "+ res.getStatusInfo().toString());
				
//				ByteArrayOutputStream result = new ByteArrayOutputStream();
//				byte[] buffer = new byte[1024];
//				for (int length; (length = req.getEntityStream().read(buffer)) != -1; ) {
//					result.write(buffer, 0, length);
//				}
////				log.setCorpsRequeteApi(om.writeValueAsString(result.toString("UTF-8")));
//				log.setCorpsRequeteApi(result.toString("UTF-8"));
				
				log.setCorpsReponseApi(om.writeValueAsString(res.getEntity()));
				taLogDossierService.merge(log);
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void logGeneralApi(ContainerRequestContext req, ContainerResponseContext res, String dossier,String utilisateur, String message) {
		logGeneralApi(null,req,res,dossier,utilisateur,message);
	}
}
