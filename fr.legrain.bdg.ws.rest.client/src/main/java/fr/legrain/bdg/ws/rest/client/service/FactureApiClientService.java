package fr.legrain.bdg.ws.rest.client.service;

import java.util.List;

import fr.legrain.bdg.ws.rest.client.AbstractApiClientService;
import fr.legrain.bdg.ws.rest.client.BdgRestClient;
import fr.legrain.document.dto.TaFactureDTO;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;

public class FactureApiClientService extends AbstractApiClientService {
	
	public FactureApiClientService() {
		bdg = BdgRestClient.findInstance();
		client = bdg.getClient();
	}
	
	public List<TaFactureDTO> listeFacture(String codeTiers, String dateDebut, String dateFin) {
		
		WebTarget target = client.target(BdgRestClient.baseURL).path("factures").path("/");
		target = target.queryParam("codeClientChezCeFournisseur", codeTiers);
		target = target.queryParam("debut", dateDebut);
		target = target.queryParam("fin", dateFin);
		
		Response requestResult =
				target.request(/*MediaType.APPLICATION_JSON_TYPE*/)
				.get();
		
		List<TaFactureDTO> listeObjet = requestResult.readEntity(new GenericType<List<TaFactureDTO>>(){});
		return listeObjet;
	}
	
	public String listeFactureJson(String codeTiers, String dateDebut, String dateFin) {
		WebTarget target = client.target(BdgRestClient.baseURL).path("factures").path("/");
		target = target.queryParam("codeClientChezCeFournisseur", codeTiers);
		target = target.queryParam("debut", dateDebut);
		target = target.queryParam("fin", dateFin);
		
		Response requestResult =
				target.request(/*MediaType.APPLICATION_JSON_TYPE*/)
				.get();
		
		String json = requestResult.readEntity(String.class);
		return json;
	}
}
