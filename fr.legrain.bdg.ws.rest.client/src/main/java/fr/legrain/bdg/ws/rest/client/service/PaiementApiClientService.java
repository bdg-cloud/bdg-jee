package fr.legrain.bdg.ws.rest.client.service;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.bdg.rest.model.ParamPaiement;
import fr.legrain.bdg.ws.rest.client.AbstractApiClientService;
import fr.legrain.bdg.ws.rest.client.BdgRestClient;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public class PaiementApiClientService extends AbstractApiClientService {
	
	public Integer etatPaiementCourant(String idExternePaymentIntent) {

		WebTarget target = client.target(BdgRestClient.baseURL).path("paiement").path("etat-paiement-courant");
		target = target.queryParam("idPaymentIntent", idExternePaymentIntent);
		
		Response requestResult =
				target.request(MediaType.APPLICATION_JSON_TYPE)
				.get();
			
		if (requestResult.getStatus() == 200) {
			String r = requestResult.readEntity(String.class);
			Gson gson = new Gson();
			JsonElement json = gson.fromJson(r, JsonElement.class);
			Integer result = null;
			if(!json.getAsJsonObject().get("b").isJsonNull())
				result = json.getAsJsonObject().get("b").getAsInt();
			return result;
		} else {
			//erreur
		    System.out.println(requestResult.readEntity(String.class));
		    return null;
		}
		
	}
	
	public Boolean paimentCbPossible() {

		WebTarget target = client.target(BdgRestClient.baseURL).path("paiement").path("paiment-cb-possible");
		
		Response requestResult =
				target.request(MediaType.APPLICATION_JSON_TYPE)
				.get();
		
		if (requestResult.getStatus() == 200) {
			String r = requestResult.readEntity(String.class);
			Gson gson = new Gson();
			JsonElement json = gson.fromJson(r, JsonElement.class);
			Boolean result = null;
			if(!json.getAsJsonObject().get("b").isJsonNull())
				result = json.getAsJsonObject().get("b").getAsBoolean();
			return result;
		} else {
			//erreur
		    System.out.println(requestResult.readEntity(String.class));
		    return null;
		}
		
	}
	
	public String clePubliqueStripe() {

		WebTarget target = client.target(BdgRestClient.baseURL).path("paiement").path("cle-publique-stripe");
		
		Response requestResult =
				target.request(MediaType.APPLICATION_JSON_TYPE)
				.get();
		
		if (requestResult.getStatus() == 200) {
			String r = requestResult.readEntity(String.class);
			Gson gson = new Gson();
			JsonElement json = gson.fromJson(r, JsonElement.class);
			String result = null;
			if(!json.getAsJsonObject().get("b").isJsonNull()) {
				result = json.getAsJsonObject().get("b").getAsString();
				if(result!=null && result.trim().toLowerCase().equals("null")) {
					result = null;
				}
			}
			return result;
		} else {
			//erreur
		    System.out.println(requestResult.readEntity(String.class));
		    return null;
		}
		
	}
	
	public String cleConnectStripe() {

		WebTarget target = client.target(BdgRestClient.baseURL).path("paiement").path("cle-connect-stripe");
		
		Response requestResult =
				target.request(MediaType.APPLICATION_JSON_TYPE)
				.get();
		
		if (requestResult.getStatus() == 200) {
			String r = requestResult.readEntity(String.class);
			Gson gson = new Gson();
			JsonElement json = gson.fromJson(r, JsonElement.class);
			String result = null;
			if(!json.getAsJsonObject().get("b").isJsonNull()) {
				result = json.getAsJsonObject().get("b").getAsString();
				if(result!=null && result.trim().toLowerCase().equals("null")) {
					result = null;
				}
			}
			return result;
		} else {
			//erreur
		    System.out.println(requestResult.readEntity(String.class));
		    return null;
		}
		
	}
	
	public String paiementDocumentCb(ParamPaiement p) {

		WebTarget target = client.target(BdgRestClient.baseURL).path("paiement").path("paiement-document-cb");
		
		Response requestResult =
				target.request(MediaType.APPLICATION_JSON_TYPE)
				.post(Entity.entity(p, MediaType.APPLICATION_JSON_TYPE));
		
		//.entity("{\"paymentSecret\":\""+paymentSecret+"\"}")
		
		if (requestResult.getStatus() == 200) {
			String r = requestResult.readEntity(String.class);
			Gson gson = new Gson();
			JsonElement json = gson.fromJson(r, JsonElement.class);
			String result = null;
			if(!json.getAsJsonObject().get("paymentSecret").isJsonNull())
				result = json.getAsJsonObject().get("paymentSecret").getAsString();
			return result;
		} else {
			//erreur
		    System.out.println(requestResult.readEntity(String.class));
		    return null;
		}
		
	}

}
