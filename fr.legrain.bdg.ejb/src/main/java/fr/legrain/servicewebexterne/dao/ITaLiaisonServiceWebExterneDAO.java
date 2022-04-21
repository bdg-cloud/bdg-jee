package fr.legrain.servicewebexterne.dao;

import java.util.List;

import fr.legrain.data.IGenericDAO;
import fr.legrain.servicewebexterne.dto.TaLiaisonServiceExterneDTO;
import fr.legrain.servicewebexterne.model.TaLiaisonServiceExterne;

public interface ITaLiaisonServiceWebExterneDAO extends IGenericDAO<TaLiaisonServiceExterne> {
	public TaLiaisonServiceExterne findByIdEntityByTypeEntiteByIdServiceWebExterne(Integer idEntite, String typeEntite, Integer idServiceWebExterne);
	public List<TaLiaisonServiceExterne> findAllByIdTiers(Integer idEntite);
	public List<TaLiaisonServiceExterne> findAllByIdArticle(Integer idEntite);
	public TaLiaisonServiceExterne findByIdTiersAndByIdServiceWebExterne(Integer idEntite, Integer idServiceWebExterne);
	public TaLiaisonServiceExterne findByIdArticleAndByIdServiceWebExterne(Integer idEntite, Integer idServiceWebExterne);
	public TaLiaisonServiceExterne findByIdTaTPaiementAndByIdServiceWebExterne(Integer idEntite, Integer idServiceWebExterne);
	public TaLiaisonServiceExterne findByRefArticleAndByIdServiceWebExterne(String refEntite, Integer idServiceWebExterne);
	public TaLiaisonServiceExterne findByRefTaTPaiementAndByIdServiceWebExterne(String refEntite, Integer idServiceWebExterne);
	public TaLiaisonServiceExterne findByRefTiersAndByIdServiceWebExterne(String refEntite, Integer idServiceWebExterne); 
	public List<TaLiaisonServiceExterne> findAllByTypeEntiteAndByIdServiceWebExterne(String typeEntite, Integer idServiceWebExterne);
	public List<TaLiaisonServiceExterneDTO> findAllByTypeEntiteAndByIdServiceWebExterneDTO(String typeEntite, Integer idServiceWebExterne);

}
