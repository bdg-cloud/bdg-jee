package fr.legrain.autorisations.ws.client;

import java.io.Serializable;
import java.rmi.RemoteException;

import fr.legrain.autorisations.ws.ListeModules;
import fr.legrain.autorisations.ws.Module;



public class Test implements Serializable{

	public static void main(String[] args) {
		AutorisationWebServiceClientCXF test = new AutorisationWebServiceClientCXF();
		String codeTiers = "demo";
		int idProduit = 4;
		
		try {
			ListeModules lm = test.listeModulesAutorises(codeTiers, idProduit);
			if(lm!=null) {
				for (Module m : lm.getModules().getModule()) {
					System.out.println(m.getId());
				}
			}
			
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
