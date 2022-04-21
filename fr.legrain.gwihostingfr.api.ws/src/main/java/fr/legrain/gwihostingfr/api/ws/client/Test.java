package fr.legrain.gwihostingfr.api.ws.client;


import java.rmi.RemoteException;


public class Test {

	public static void main(String[] args) {
		GwiWebServiceClientCXF test = new GwiWebServiceClientCXF();
//		String domaine = "promethee.biz";
		String domaine = "legrain.work";
		String sousDomaine = "demo6";
		String ip = "1.2.3.4";
		
		try {
//			Boolean r = test.creerSousDomaine(domaine, sousDomaine, ip);
			
			Boolean r = test.supprimerSousDomaine(domaine, sousDomaine);
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
