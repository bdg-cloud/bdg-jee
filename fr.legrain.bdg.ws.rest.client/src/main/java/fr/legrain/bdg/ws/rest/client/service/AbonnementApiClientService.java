package fr.legrain.bdg.ws.rest.client.service;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.legrain.bdg.ws.rest.client.AbstractApiClientService;
import fr.legrain.bdg.ws.rest.client.BdgRestClient;
import fr.legrain.document.dto.TaLEcheanceDTO;
import fr.legrain.document.dto.TaPanierDTO;
import fr.legrain.document.dto.TaAbonnementDTO;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;

public class AbonnementApiClientService extends AbstractApiClientService {
	
	public AbonnementApiClientService() {
		bdg = BdgRestClient.findInstance();
		client = bdg.getClient();
	}
	
	public TaAbonnementDTO creerAbonnement( TaAbonnementDTO dto) throws Exception {
		
		WebTarget target = client.target(BdgRestClient.baseURL).path("abonnements");
		target = target.queryParam("dto", dto);
		
		ObjectMapper mapper = new ObjectMapper();
		String dtoStr;
		dtoStr = mapper.writeValueAsString(dto);
		
		Response requestResult =
				target.request(/*MediaType.APPLICATION_JSON_TYPE*/)
				.post(Entity.json(dtoStr));
		
		if (requestResult.getStatus() == 200) {
			TaAbonnementDTO abo = requestResult.readEntity(new GenericType<TaAbonnementDTO>(){});
			return abo;
		}else {
			String errorMessage = requestResult.readEntity(String.class);
			System.out.println( errorMessage );
			throw new Exception("testest");

		}
		
		
	}
	
	public List<TaLEcheanceDTO> listeEcheance(String codeTiers, List<String> listeCodeEtat, Boolean codeModuleBDG) throws Exception {
		
		WebTarget target = client.target(BdgRestClient.baseURL).path("abonnements").path("echeances");
		target = target.queryParam("codeTiers", codeTiers);
		target = target.queryParam("listeCodeEtat", listeCodeEtat);
		target = target.queryParam("codeModuleBDG", codeModuleBDG);

		
		Response requestResult =
				target.request(/*MediaType.APPLICATION_JSON_TYPE*/)
				.get();
		
		if (requestResult.getStatus() == 200) {
			List<TaLEcheanceDTO> listeObjet = requestResult.readEntity(new GenericType<List<TaLEcheanceDTO>>(){});
			return listeObjet;
		}else {
			String errorMessage = requestResult.readEntity(String.class);
			System.out.println( errorMessage );
			throw new Exception(errorMessage);
		}
		
		
		
	}
	
	public String listeFactureJson(String codeTiers, String dateDebut, String dateFin) {
		WebTarget target = client.target(BdgRestClient.baseURL).path("abonnements").path("echeances");
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
