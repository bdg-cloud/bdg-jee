package fr.legrain.devis.divers;

import java.util.LinkedList;

import fr.legrain.document.model.LigneTva;
import fr.legrain.document.model.TaDevis;


public class ModelLignesTVADevis {

	LinkedList<LignesTVADevisDTO> listeObjet = new LinkedList<LignesTVADevisDTO>();

	public LinkedList remplirListe(TaDevis taDevis) {
		LignesTVADevisDTO ihmLignesTVA = null;
		listeObjet.clear();
		if(taDevis != null) {
			for(LigneTva ligneTVA : taDevis.getLignesTVA()){
				ihmLignesTVA = new LignesTVADevisDTO();
				ihmLignesTVA.setIHMDevisLignesTVA(ligneTVA);
				listeObjet.add(ihmLignesTVA);
			}
		}
		return listeObjet;
	}
	
	public LinkedList<LignesTVADevisDTO> getListeObjet() {
		return listeObjet;
	}

}
