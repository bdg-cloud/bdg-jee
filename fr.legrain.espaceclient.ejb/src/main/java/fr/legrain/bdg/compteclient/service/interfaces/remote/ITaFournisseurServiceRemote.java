package fr.legrain.bdg.compteclient.service.interfaces.remote;


import javax.ejb.Remote;

import fr.legrain.bdg.compteclient.dto.TaFournisseurDTO;
import fr.legrain.bdg.compteclient.model.TaFournisseur;
import fr.legrain.bdg.compteclient.service.interfaces.remote.general.IAbstractLgrDAOServer;
import fr.legrain.bdg.compteclient.service.interfaces.remote.general.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.compteclient.service.interfaces.remote.general.IGenericCRUDServiceRemote;
// Interface de service étendu à sa classe DTO(digital transfert objet)
@Remote	// Permet un accès à l'EJB depuis un client hors de la JVM
public interface ITaFournisseurServiceRemote extends IGenericCRUDServiceRemote<TaFournisseur,TaFournisseurDTO>,
														IAbstractLgrDAOServer<TaFournisseur>,IAbstractLgrDAOServerDTO<TaFournisseurDTO>{
// 
	public static final String validationContext = "FOURNISSEUR";
	public TaFournisseur findInstance();
	public TaFournisseur findByCodeDossier(String codeDossier);
}


