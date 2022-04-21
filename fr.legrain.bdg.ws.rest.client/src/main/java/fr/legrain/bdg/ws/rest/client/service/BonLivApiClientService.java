package fr.legrain.bdg.ws.rest.client.service;

import java.util.List;

import fr.legrain.bdg.ws.rest.client.AbstractApiClientService;
import fr.legrain.bdg.ws.rest.client.BdgRestClient;
import fr.legrain.document.dto.TaBonlivDTO;
import fr.legrain.document.dto.TaFactureDTO;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public class BonLivApiClientService extends AbstractApiClientService {
	
	public BonLivApiClientService() {
		bdg = BdgRestClient.findInstance();
		client = bdg.getClient();
	}
	
	public List<TaBonlivDTO> listeBonLiv(String codeTiers, String dateDebut, String dateFin) {
		
		WebTarget target = client.target(BdgRestClient.baseURL).path("bonliv").path("/");
		target = target.queryParam("codeClientChezCeFournisseur", codeTiers);
		target = target.queryParam("debut", dateDebut);
		target = target.queryParam("fin", dateFin);
		
		Response requestResult =
				target.request(/*MediaType.APPLICATION_JSON_TYPE*/)
				.get();
		
		List<TaBonlivDTO> listeObjet = requestResult.readEntity(new GenericType<List<TaBonlivDTO>>(){});
		return listeObjet;
	}
	
	public String listeBonlivJson(String codeTiers, String dateDebut, String dateFin) {
		WebTarget target = client.target(BdgRestClient.baseURL).path("bonliv").path("/");
		target = target.queryParam("codeClientChezCeFournisseur", codeTiers);
		target = target.queryParam("debut", dateDebut);
		target = target.queryParam("fin", dateFin);
		
		Response requestResult =
				target.request(/*MediaType.APPLICATION_JSON_TYPE*/)
				.get();
		
		String json = requestResult.readEntity(String.class);
		return json;
	}
	
	public TaBonlivDTO createBonliv(TaBonlivDTO dto) {
		
		WebTarget target = client.target(BdgRestClient.baseURL).path("bonliv").path("/");
//		target = target.queryParam("codeClientChezCeFournisseur", codeTiers);
//		target = target.queryParam("debut", dateDebut);
//		target = target.queryParam("fin", dateFin);
		
		Response requestResult =
				target.request(MediaType.APPLICATION_JSON_TYPE)
				.post(Entity.entity(dto, MediaType.APPLICATION_JSON_TYPE));
		
		dto = requestResult.readEntity(new GenericType<TaBonlivDTO>(){});
		return dto;
	}
}
