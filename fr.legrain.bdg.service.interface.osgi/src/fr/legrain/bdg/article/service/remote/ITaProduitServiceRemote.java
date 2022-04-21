//package fr.legrain.bdg.article.service.remote;
//
//import java.util.List;
//
//import javax.ejb.Remote;
//
//import fr.legrain.article.dto.TaProduitDTO;
//import fr.legrain.article.model.TaProduit;
//import fr.legrain.bdg.tiers.service.remote.IAbstractLgrDAOServer;
//import fr.legrain.bdg.tiers.service.remote.IAbstractLgrDAOServerDTO;
//import fr.legrain.bdg.tiers.service.remote.IGenericCRUDServiceRemote;
//
//@Remote
//public interface ITaProduitServiceRemote extends IGenericCRUDServiceRemote<TaProduit,TaProduitDTO>,
//														IAbstractLgrDAOServer<TaProduit>,IAbstractLgrDAOServerDTO<TaProduitDTO>{
//	public static final String validationContext = "PRODUIT";
//
//	public List<TaProduit> findByIdFab(Integer id);
//
//	public List<TaProduitDTO> findDTOByIdFab(Integer id);
//	
//}
