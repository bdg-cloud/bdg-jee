package fr.legrain.bdg.ws.rest.client.service;



import com.fasterxml.jackson.databind.ObjectMapper;

import fr.legrain.bdg.rest.model.ParamEmailConfirmationCommandeBoutique;
import fr.legrain.bdg.ws.rest.client.AbstractApiClientService;
import fr.legrain.bdg.ws.rest.client.BdgRestClient;
import fr.legrain.document.dto.TaLEcheanceDTO;
import fr.legrain.document.dto.TaPanierDTO;
import fr.legrain.tiers.dto.TaEspaceClientDTO;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public class PanierApiClientService extends AbstractApiClientService {
	
	public PanierApiClientService() {
		bdg = BdgRestClient.findInstance();
		client = bdg.getClient();
	}
	
	public TaPanierDTO getPanierActif(String codeTiers) throws Exception {
		
		WebTarget target = client.target(BdgRestClient.baseURL).path("paniers").path("actif");
		target = target.queryParam("codeTiers", codeTiers);

		
		Response requestResult =
				target.request(/*MediaType.APPLICATION_JSON_TYPE*/)
				.get();
		
		if (requestResult.getStatus() == 200) {
			TaPanierDTO panier = requestResult.readEntity(new GenericType<TaPanierDTO>(){});
			return panier;
		}else {
			String errorMessage = requestResult.readEntity(String.class);
			System.out.println( errorMessage );
			throw new Exception(errorMessage);

		}
		
		
	}
	
	
	public TaPanierDTO ajoutLigneEcheance(Integer idPanier, TaLEcheanceDTO ldto) throws Exception {
		WebTarget target = client.target(BdgRestClient.baseURL).path("paniers").path(String.valueOf(idPanier)).path("lignes-echeances");
		
		ObjectMapper mapper = new ObjectMapper();
		String dtoStr;
		dtoStr = mapper.writeValueAsString(ldto);
		
		Response requestResult =
				target.request(/*MediaType.APPLICATION_JSON_TYPE*/)
				.post(Entity.json(dtoStr));
		if (requestResult.getStatus() == 200) {
			TaPanierDTO panier = requestResult.readEntity(new GenericType<TaPanierDTO>(){});
			return panier;
		}else {
			String errorMessage = requestResult.readEntity(String.class);
			System.out.println( errorMessage );
			throw new Exception(errorMessage);

		}
	}
	
	public TaPanierDTO supprimeLigne(Integer idPanier, Integer numLigne) throws Exception {
		WebTarget target = client.target(BdgRestClient.baseURL).path("paniers").path(String.valueOf(idPanier)).path("lignes").path(String.valueOf(numLigne));
		
		Response requestResult =
				target.request(/*MediaType.APPLICATION_JSON_TYPE*/)
				.delete();
		if (requestResult.getStatus() == 200) {
			TaPanierDTO panier = requestResult.readEntity(new GenericType<TaPanierDTO>(){});
			return panier;
		}else {
			String errorMessage = requestResult.readEntity(String.class);
			System.out.println( errorMessage );
			throw new Exception(errorMessage);

		}
	}
	
	public TaPanierDTO emailConfirmationCommandeBoutique(Integer id, 
			ParamEmailConfirmationCommandeBoutique paramEmailConfirmationCommandeBoutique) throws Exception {
		
		WebTarget target = client.target(BdgRestClient.baseURL).path("paniers").path(String.valueOf(id)).path("email-confirmation-commande");
		target = target.queryParam("paramEmailConfirmationCommandeBoutique", paramEmailConfirmationCommandeBoutique);
		
		ObjectMapper mapper = new ObjectMapper();
		String dtoStr;
		dtoStr = mapper.writeValueAsString(paramEmailConfirmationCommandeBoutique);
		
		Response requestResult =
				target.request(/*MediaType.APPLICATION_JSON_TYPE*/)
				.post(Entity.json(dtoStr));
		
		if (requestResult.getStatus() == 200) {
			TaPanierDTO panier = requestResult.readEntity(new GenericType<TaPanierDTO>(){});
			return panier;
		}else {
			String errorMessage = requestResult.readEntity(String.class);
			System.out.println( errorMessage );
			throw new Exception(errorMessage);

		}
		
		
	}
	
	

}