//package fr.legrain.bdg.article.service.remote;
//
//import java.util.List;
//
//import javax.ejb.Remote;
//
//import fr.legrain.article.dto.TaMatierePremiereDTO;
//import fr.legrain.article.model.TaMatierePremiere;
//import fr.legrain.bdg.tiers.service.remote.IAbstractLgrDAOServer;
//import fr.legrain.bdg.tiers.service.remote.IAbstractLgrDAOServerDTO;
//import fr.legrain.bdg.tiers.service.remote.IGenericCRUDServiceRemote;
//
//@Remote
//public interface ITaMatierePremiereServiceRemote extends IGenericCRUDServiceRemote<TaMatierePremiere,TaMatierePremiereDTO>,
//														IAbstractLgrDAOServer<TaMatierePremiere>,IAbstractLgrDAOServerDTO<TaMatierePremiereDTO>{
//	public static final String validationContext = "MATIEREPREMIERE";
//
//	public List<TaMatierePremiere> findByIdFab(Integer id);
//
//	public List<TaMatierePremiereDTO> findDTOByIdFab(Integer id);
//
//	
//}
//
