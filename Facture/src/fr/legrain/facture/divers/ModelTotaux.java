package fr.legrain.facture.divers;

import java.util.LinkedList;

import fr.legrain.document.dto.TotauxDocumentDTO;

public class ModelTotaux {

	LinkedList<TotauxDocumentDTO> listeObjet = new LinkedList<TotauxDocumentDTO>();

//	public LinkedList remplirListe(SWTFacture swtFacture) {
//		IHMTotauxFacture ihmTotauxFacture = null;
//		listeObjet.clear();
//		if(swtFacture != null) {
//			ihmTotauxFacture = new IHMTotauxFacture();
//			ihmTotauxFacture.setIHMTotauxFacture(((SWTEnteteFacture)swtFacture.getEntete()));
//			listeObjet.add(ihmTotauxFacture);	
//		}
//		return listeObjet;
//	}
	
	public LinkedList<TotauxDocumentDTO> getListeObjet() {
		return listeObjet;
	}

}
