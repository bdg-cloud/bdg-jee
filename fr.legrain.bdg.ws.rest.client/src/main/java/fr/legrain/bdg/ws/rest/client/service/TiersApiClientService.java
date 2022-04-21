package fr.legrain.bdg.ws.rest.client.service;

import fr.legrain.bdg.ws.rest.client.AbstractApiClientService;
import fr.legrain.bdg.ws.rest.client.BdgRestClient;
import fr.legrain.tiers.dto.TaTiersDTO;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public class TiersApiClientService extends AbstractApiClientService {
	
	public void findTiers(int idTiers) {

		WebTarget target = client.target(BdgRestClient.baseURL).path("tiers").path("/"+idTiers);
		
		Response requestResult =
				target.request(MediaType.APPLICATION_JSON_TYPE)
				.get();
		
		TaTiersDTO dto = requestResult.readEntity(TaTiersDTO.class);
		System.out.println("Code tiers : "+dto.getCodeTiers());
		
	}
	
	public void findTiersAncienneMethode(String dossier, String login, String password, int idTiers) {
//		initBaseUrl(dossier);
		BdgRestClient.login = login;
		BdgRestClient.password = password;
		WebTarget target = client.target(BdgRestClient.baseURL).path("tiers").path("/"+idTiers);
		
		Response requestResult =
				target.request(MediaType.APPLICATION_JSON_TYPE)
				.get();
		
//		String r = requestResult.readEntity(String.class);
//		System.out.println("BdgRestClient.listeFacture() "+r);
		
//		Gson gson = new Gson();
//		TaArticleDTO dto = gson.fromJson(r, TaArticleDTO.class);
		
//		ObjectMapper objectMapper = new ObjectMapper();
//		
//		TaArticleDTO dto;
//		try {
//			dto = objectMapper.readValue(r, TaArticleDTO.class);
//			System.out.println("BdgRestClient.listeFacture() "+dto.getCodeArticle());
//		} catch (JsonParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (JsonMappingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		TaTiersDTO dto = requestResult.readEntity(TaTiersDTO.class);
		System.out.println("Code tiers : "+dto.getCodeTiers());
		
	}
}
