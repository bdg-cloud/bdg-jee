package fr.legrain.bdg.rest.proxy.multitenant;

import fr.legrain.bdg.rest.model.ParamEmailConfirmationCommandeBoutique;
import fr.legrain.document.model.TaPanier;

public interface IPanierRestMultitenantProxy {
	
	public TaPanier merge(String codeDossierFournisseur, TaPanier detachedInstance);
	
//	public void emailConfirmationCommandeBoutique(String codeDossierFournisseur,int id, ParamEmailConfirmationCommandeBoutique paramEmailConfirmationCommandeBoutique);

}