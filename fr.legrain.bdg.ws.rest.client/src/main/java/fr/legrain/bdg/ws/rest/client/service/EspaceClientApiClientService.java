package fr.legrain.bdg.ws.rest.client.service;

import java.util.List;

import fr.legrain.bdg.ws.rest.client.AbstractApiClientService;
import fr.legrain.bdg.ws.rest.client.BdgRestClient;
import fr.legrain.tiers.dto.TaEspaceClientDTO;
import fr.legrain.tiers.dto.TaTiersDTO;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;

public class EspaceClientApiClientService extends AbstractApiClientService {
	
	public EspaceClientApiClientService() {
		bdg = BdgRestClient.findInstance();
		client = bdg.getClient();
	}
	
	public TaEspaceClientDTO creerCompteEspaceClient(String paramEspaceClient) throws Exception {
		
		WebTarget target = client.target(BdgRestClient.baseURL).path("espace-client").path("creer-compte-espace-client");
		
		Response requestResult;

		requestResult = target.request(/*MediaType.APPLICATION_JSON_TYPE*/)
		.post(Entity.json(paramEspaceClient));
		
		if (requestResult.getStatus() == 200) {
			TaEspaceClientDTO espaceClientDTO = requestResult.readEntity(new GenericType<TaEspaceClientDTO>(){});
			return espaceClientDTO;
		}else {
			String errorMessage = requestResult.readEntity(String.class);
			System.out.println( errorMessage );
			throw new Exception(errorMessage);

		}
		

		
		
	}
	
	public List<TaTiersDTO> listeTiersEspaceClient(Integer idEspaceClient){
		WebTarget target = client.target(BdgRestClient.baseURL).path("espace-client").path("liste-tiers").path(String.valueOf(idEspaceClient));

		Response requestResult =
				target.request(/*MediaType.APPLICATION_JSON_TYPE*/)
				.get();
		
		List<TaTiersDTO> liste = requestResult.readEntity(new GenericType<List<TaTiersDTO>>(){});
		return liste;
	}
	
	public TaEspaceClientDTO liaisonNouveauCompteEspaceClient(String paramEspaceClient) {
		WebTarget target = client.target(BdgRestClient.baseURL).path("espace-client").path("liaison-nouveau-compte-espace-client");
		Response requestResult =
				target.request(/*MediaType.APPLICATION_JSON_TYPE*/)
				.put(Entity.json(paramEspaceClient));
		
		TaEspaceClientDTO espaceClientDTO = requestResult.readEntity(new GenericType<TaEspaceClientDTO>(){});
		return espaceClientDTO;
	}
	
	
}
