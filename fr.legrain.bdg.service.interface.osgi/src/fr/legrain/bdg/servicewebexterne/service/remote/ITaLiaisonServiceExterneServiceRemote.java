package fr.legrain.bdg.servicewebexterne.service.remote;

import java.util.List;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.servicewebexterne.dto.TaLiaisonServiceExterneDTO;
import fr.legrain.servicewebexterne.model.TaLiaisonServiceExterne;

@Remote
public interface ITaLiaisonServiceExterneServiceRemote extends IGenericCRUDServiceRemote<TaLiaisonServiceExterne,TaLiaisonServiceExterneDTO>,
IAbstractLgrDAOServer<TaLiaisonServiceExterne>,IAbstractLgrDAOServerDTO<TaLiaisonServiceExterneDTO> {
	
	public static final String validationContext = "TA_LIAISON_SERVICE_EXTERNE_SERVICE";
	public TaLiaisonServiceExterne findByIdEntityByTypeEntiteByIdServiceWebExterne(Integer idEntite, String typeEntite, Integer idServiceWebExterne);
	public List<TaLiaisonServiceExterne> findAllByIdTiers(Integer idEntite);
	public List<TaLiaisonServiceExterne> findAllByIdArticle(Integer idEntite);
	public TaLiaisonServiceExterne findByIdTiersAndByIdServiceWebExterne(Integer idEntite, Integer idServiceWebExterne);
	public TaLiaisonServiceExterne findByIdArticleAndByIdServiceWebExterne(Integer idEntite, Integer idServiceWebExterne);
	public TaLiaisonServiceExterne findByIdTaTPaiementAndByIdServiceWebExterne(Integer idEntite, Integer idServiceWebExterne);
	public TaLiaisonServiceExterne findByRefArticleAndByIdServiceWebExterne(String refEntite, Integer idServiceWebExterne);
	public TaLiaisonServiceExterne findByRefTiersAndByIdServiceWebExterne(String refEntite, Integer idServiceWebExterne); 
	public TaLiaisonServiceExterne findByRefTaTPaiementAndByIdServiceWebExterne(String refEntite, Integer idServiceWebExterne);
	public List<TaLiaisonServiceExterne> findAllByTypeEntiteAndByIdServiceWebExterne(String typeEntite, Integer idServiceWebExterne);
	public List<TaLiaisonServiceExterneDTO> findAllByTypeEntiteAndByIdServiceWebExterneDTO(String typeEntite, Integer idServiceWebExterne);

}
