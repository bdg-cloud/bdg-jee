package fr.legrain.bdg.ws.rest.client;

import jakarta.ws.rs.client.Client;

public abstract class AbstractApiClientService {
	
	protected Client client;
	protected BdgRestClient bdg;
	
	public AbstractApiClientService() {
		bdg = BdgRestClient.findInstance();
		client = bdg.getClient();
	}
	
}
