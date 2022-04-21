package fr.legrain.moncompte.ws.client;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;



public class Test implements Serializable{

	public static void main(String[] args) {
		try {
			
			SetupWebServiceClientCXF test = new SetupWebServiceClientCXF();
			String codeTiers = "demo";
			//int idProduit = 4;
			String codeProduit = "bdg";
			String versionClient = "rxxxx";
			
			String lm = test.chargeDernierSetup(codeTiers, codeProduit, versionClient);
//			if(lm!=null) {
//				for (Module m : lm.getModules().getModule()) {
//					System.out.println(m.getNomModule());
//				}
//			}
			
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
