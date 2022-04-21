package fr.legrain.bdg.compteclient.service.interfaces.remote;

import java.util.List;

import javax.ejb.Remote;

import fr.legrain.bdg.compteclient.dto.TaMesFournisseursDTO;
import fr.legrain.bdg.compteclient.model.TaFournisseur;
import fr.legrain.bdg.compteclient.model.TaMesFournisseurs;
import fr.legrain.bdg.compteclient.service.interfaces.remote.general.IAbstractLgrDAOServer;
import fr.legrain.bdg.compteclient.service.interfaces.remote.general.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.compteclient.service.interfaces.remote.general.IGenericCRUDServiceRemote;

@Remote
public interface ITaMesFournisseursServiceRemote extends IGenericCRUDServiceRemote<TaMesFournisseurs,TaMesFournisseursDTO>,
IAbstractLgrDAOServer<TaMesFournisseurs>,IAbstractLgrDAOServerDTO<TaMesFournisseursDTO>{
public static final String validationContext = "FOURNISSEUR";
public TaMesFournisseurs findInstance();
public List<TaMesFournisseurs> rechercheMesFournisseurs(int idUtilisateur);
public boolean verifieSiLiaisonClientFournisseurExisteDeja(int idUtilisateur, String codeClient);
}
