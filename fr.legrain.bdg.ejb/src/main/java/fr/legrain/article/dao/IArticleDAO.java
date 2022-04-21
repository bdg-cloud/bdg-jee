package fr.legrain.article.dao;

import java.util.List;
import java.util.Map;

import javax.ejb.FinderException;

import fr.legrain.article.dto.TaArticleComposeDTO;
import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.article.dto.TaPrixDTO;
import fr.legrain.article.model.TaArticle;
import fr.legrain.article.model.TaArticleCompose;
import fr.legrain.article.model.TaRapportUnite;
import fr.legrain.data.IGenericDAO;
import fr.legrain.tiers.dto.TaTiersDTO;

//@Remote
public interface IArticleDAO extends IGenericDAO<TaArticle> {
	public List<TaArticle>  findByCodeTva(String codeTva);
	
	public List<Object[]> findByActifLight(boolean actif);
	public List<Object[]> findByEcranLight();
	public List<TaArticleDTO> findByCodeLight(String code);
	public List<TaArticleDTO> findAllLight();
	public List<TaArticleDTO> findAllLight2();
	public List<TaArticleDTO> findAllNonCompose();
	public List<TaArticleDTO> findArticleCaisseLight(String codeFamille);
	public TaArticle remonteArticlePointBonus();
	public Map<Integer, String> findIdEtCode();
	
	public List<TaArticleDTO> findByCodeLightMP(String code);
	public List<TaArticleDTO> findByCodeLightPF(String code);

	public Map<String, String> findCodeEtLibelle();
	public TaRapportUnite getRapport( String CodeArticle ) ;

	public List<TaArticle>  findByMPremierePFini(Boolean mPremiere, Boolean pFini,Boolean lesDeux,Boolean unOuAutre);
	public List<TaArticle>  findByMPremiere(Boolean mPremiere);
	public List<TaArticle>  findByPFini(Boolean pFini);
	
	public List<TaArticleDTO>  selectAllInReception();
	public List<TaArticleDTO> selectAllMPInFabrication() ;
	public List<TaArticleDTO> selectAllPFInFabrication();
	public List<TaTiersDTO>  findFournisseurProduit(String produit);
	public List<TaArticleDTO>  findProduitsFournisseur(String fournisseur);
	public List<TaArticleDTO>  findByMPremierePFiniDTO(Boolean mPremiere,Boolean pFini,Boolean lesDeux, Boolean unOuAutre) ;
	
	public boolean articleEstUtilise(TaArticle taArticle);
	
	public int countAllArticleActif();
	public Boolean exist(String code);
	
	public List<TaArticleDTO>  remonteGrilleReference(String codeArticle, String codeFamille) ;
	public List<TaArticleDTO>  remonteGrilleReference(String codeArticle, String codeFamille,boolean prixObligatoire);
	
	public List<TaArticleDTO>  remonteGrilleTarifaire(String codeArticle, String codeFamille, String codeTTarif);
	
	public List<TaArticleDTO>  remonteGrilleTarifaireTiers(String codeArticle, String codeFamille, String codeTTarif,String codeTiers);
	public List<TaArticleDTO> findLightSurCodeTTarif(String codeTTarif) ;
	public List<TaArticleDTO> findLightSurCodeTTarifEtCodeTiers(String codeTTarif,String codeTiers); 
	public List<TaArticleDTO>  remonteGrilleTarifaireComplete(String codeArticle, String codeFamille, String codeTTarif,String codeTiers);
	
	public void modificationUniteDeReference(int idArticle, int idNouvelleUniteRef);
	public TaArticle findByCodeBarre(String codeBarre);
	
	public TaArticle findByIdAvecLazy(int id,boolean titreTransport) ;
	public TaArticle findByCodeAvecLazy(String code,boolean titreTransport) ;
	public List<TaArticleDTO> findByCodeAndLibelleLight(String code);
	
	public TaArticle findByCodeAvecConformite(String code);
	public TaArticle findByIdAvecConformite(int id);
	
	
	public List<TaArticleComposeDTO> findNomenclatureDTOByIdArticle(int idArticle);

	public List<TaArticleCompose> findNomenclatureByIdArticle(int idArticle);
	public TaArticle findArticleEnfantByIdArticleParent(int idArticle);
	
	public List<TaArticleDTO> selectAllDTOAvecPlanByIdFrequence(Integer idFrequence);
	public List<TaArticleDTO> selectAllDTOAvecPlan();
}
