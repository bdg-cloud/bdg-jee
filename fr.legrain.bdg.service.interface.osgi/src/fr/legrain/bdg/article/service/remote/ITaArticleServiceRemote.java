package fr.legrain.bdg.article.service.remote;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.FinderException;
import javax.ejb.Remote;

import fr.legrain.article.dto.TaArticleComposeDTO;
import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.article.dto.TaPrixDTO;
import fr.legrain.article.model.TaArticle;
import fr.legrain.article.model.TaArticleCompose;
import fr.legrain.article.model.TaCategorieArticle;
import fr.legrain.article.model.TaRapportUnite;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.document.dto.DocumentChiffreAffaireDTO;
import fr.legrain.tiers.dto.TaTiersDTO;

@Remote
public interface ITaArticleServiceRemote extends IGenericCRUDServiceRemote<TaArticle,TaArticleDTO>,
														IAbstractLgrDAOServer<TaArticle>,IAbstractLgrDAOServerDTO<TaArticleDTO>{
	public static final String validationContext = "ARTICLE";
	
	public List<TaArticle>  findByCodeTva(String codeTva);
	
	public List<Object[]> findByActifLight(boolean actif);
	public List<Object[]> findByEcranLight();
	public List<TaArticleDTO> findAllLight();
	public List<TaArticleDTO> findAllLight2();
	public List<TaArticleDTO> findAllNonCompose();
	public List<TaArticleDTO> findArticleCaisseLight(String codeFamille);
	public List<TaArticleDTO> findByCodeLight(String code);
	public String genereCode( Map<String, String> params) ;
	public void annuleCode(String code);
	public void verrouilleCode(String code);
	public TaRapportUnite getRapport( String CodeArticle );
	public TaArticle initPrixArticle(TaArticle taArticle,TaArticleDTO dto); 
	public boolean initEtatRapportUnite(boolean enModif, TaArticle taArticle, TaArticleDTO dto);
	public boolean rapportUniteEstRempli(TaArticleDTO dto);
	public boolean crdEstRempli(TaArticleDTO dto);
	public TaArticle initTaRTaTitreTransport(TaArticle taArticle);
	public List<TaArticleDTO>  findByMPremierePFiniDTO(Boolean mPremiere, Boolean pFini,Boolean lesDeux,Boolean unOuAutre);
	public List<TaArticle> findByMPremierePFini(Boolean mPremiere, Boolean pFini,Boolean lesDeux,Boolean unOuAutre);
	public List<TaArticle>  findByMPremiere(Boolean mPremiere);
	public List<TaArticle>  findByPFini(Boolean pFini);
	public List<TaArticleDTO> findByCodeLightMP(String code);
	public List<TaArticleDTO> findByCodeLightPF(String code);
	public List<TaArticleDTO>  selectAllInReception();
	public List<TaArticleDTO> selectAllMPInFabrication() ;
	public List<TaArticleDTO> selectAllPFInFabrication() ;
//	public TaArticle findById(int id) throws FinderException;
	public List<TaTiersDTO>  findFournisseurProduit(String produit);
	public List<TaArticleDTO>  findProduitsFournisseur(String fournisseur);
	public File exportToCSV(List<TaArticleDTO> l);
	
	public boolean articleEstUtilise(TaArticle taArticle);
	
	public int countAllArticleActif();
	public List<DocumentChiffreAffaireDTO> findChiffreAffaireTotalParCodeArticleDTO(Date dateDebut, Date dateFin, String codeArticle);

	public List<TaArticleDTO>  remonteGrilleReference(String codeArticle, String codeFamille) ;
	public List<TaArticleDTO>  remonteGrilleReference(String codeArticle, String codeFamille,boolean prixObligatoire);
	
	public List<TaArticleDTO>  remonteGrilleTarifaire(String codeArticle, String codeFamille, String codeTTarif);
	public List<TaArticleDTO>  remonteGrilleTarifaireTiers(String codeArticle, String codeFamille, String codeTTarif,String codeTiers);
	public List<TaArticleDTO> findLightSurCodeTTarif(String codeTTarif) ;
	public List<TaArticleDTO> findLightSurCodeTTarifEtCodeTiers(String codeTTarif,String codeTiers);
	public List<TaArticleDTO>  remonteGrilleTarifaireComplete(String codeArticle, String codeFamille, String codeTTarif,String codeTiers);
	public List<TaArticleDTO> selectAllDTOAvecPlanByIdFrequence(Integer idFrequence);
	public List<TaArticleDTO> selectAllDTOAvecPlan();
	
	public void modificationUniteDeReference(int idArticle, int idNouvelleUniteRef);
	public TaArticle findByCodeBarre(String codeBarre);
	public TaArticle findByIdAvecLazy(int id,boolean titreTransport)  ;
	public TaArticle findByCodeAvecLazy(String code,boolean titreTransport) ;
	public List<TaArticleDTO> findByCodeAndLibelleLight(String code);
	
	public TaArticle findByCodeAvecConformite(String code);
	public TaArticle findByIdAvecConformite(int id);
	
	public List<TaArticleComposeDTO> findNomenclatureDTOByIdArticle(int idArticle);
	public List<TaArticleCompose> findNomenclatureByIdArticle(int idArticle);
	public TaArticle findArticleEnfantByIdArticleParent(int idArticle);
	public TaArticle setNomenclature(TaArticle article);
	
	public TaArticle affecteCategorie(TaArticle art, TaCategorieArticle cat);
	public void supprimeCategorie(TaArticle art, TaCategorieArticle cat);
	public TaArticle supprimeTouteCategorie(TaArticle art);
}
