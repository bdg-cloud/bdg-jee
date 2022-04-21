package fr.legrain.bdg.ws.rest.client.service;

import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.bdg.ws.rest.client.AbstractApiClientService;
import fr.legrain.bdg.ws.rest.client.BdgRestClient;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public class ArticleApiClientService extends AbstractApiClientService {
	
	public TaArticleDTO findArticle(int idArticle) {

		WebTarget target = client.target(BdgRestClient.baseURL).path("articles").path("/"+idArticle);
		
		Response requestResult =
				target.request(MediaType.APPLICATION_JSON_TYPE)
				.get();
		
		if (requestResult.getStatus() == 200) {
			TaArticleDTO dto = requestResult.readEntity(TaArticleDTO.class);
			return dto;
		} else {
			//erreur
		    System.out.println(requestResult.readEntity(String.class));
		    return null;
		}
		
	}
	
	public TaArticleDTO findArticleAncienneMethode(String dossier, String login, String password, int idArticle) {
		BdgRestClient.login = login;
		BdgRestClient.password = password;
		WebTarget target = client.target(BdgRestClient.baseURL).path("articles").path("/"+idArticle);
		
		Response requestResult =
				target.request(MediaType.APPLICATION_JSON_TYPE)
				.get();
		
		if (requestResult.getStatus() == 200) {
			TaArticleDTO dto = requestResult.readEntity(TaArticleDTO.class);
			return dto;
		} else {
			//erreur
		    System.out.println(requestResult.readEntity(String.class));
		    return null;
		}
		
		
		
	}
}
