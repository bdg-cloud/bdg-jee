package fr.legrain.documents.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import fr.legrain.data.IGenericDAO;
import fr.legrain.document.dashboard.dto.TaArticlesParTiersDTO;
import fr.legrain.document.dto.DocumentChiffreAffaireDTO;
import fr.legrain.document.dto.DocumentDTO;
import fr.legrain.document.dto.TaFactureDTO;
import fr.legrain.document.model.TaFacture;
import fr.legrain.tiers.dao.IDocumentDAO;
import fr.legrain.tiers.dao.IDocumentTiersEtatDAO;
import fr.legrain.tiers.dao.IDocumentTiersStatistiquesDAO;

//@Remote
public interface IFactureDAO extends IGenericDAO<TaFacture>,IDocumentDAO<TaFacture>,IDocumentTiersStatistiquesDAO<TaFacture>,IDocumentTiersEtatDAO<TaFacture>,
IDashboardDocumentDAO ,IDashboardDocumentPayableDAO {
	public List<TaFactureDTO> findAllLight();
	
	public TaFacture getReference(int factureId);
	
	public List<TaFacture> rechercheDocumentNonExporte(Date dateDeb, Date dateFin,boolean parDate);
	public List<TaFacture> rechercheDocumentOrderByDate(Date dateDeb, Date dateFin);
	public List<TaFactureDTO> rechercheDocumentNonExporteLight(Date dateDeb, Date dateFin,boolean parDate) ;
	public List<Object> findChiffreAffaireTotal(Date debut, Date fin, int precision);
	
	public List<TaFacture> findByCodeTiersAndDateCompteClient(String codeTiers, Date debut, Date fin);	
//	public List<TaFactureDTO> findAllDTOPeriode(Date dateDebut, Date dateFin);
	public List<TaFacture> rechercheDocumentNonTotalementRegle(Date dateDeb, Date dateFin) ;
	public List<TaFactureDTO> rechercheDocumentNonTotalementRegle(Date dateDeb, Date dateFin,String tiers,String document);
	public List<TaFacture> rechercheDocumentNonTotalementRegleAEcheance(Date dateDeb, Date dateFin,String tiers,String document,BigDecimal limite);
	public List<Object[]> rechercheDocumentNonTotalementRegleAEcheance2(Date dateDeb, Date dateFin,String tiers,String document) ;
	public List<TaFacture> rechercheDocumentNonTotalementRegleReel(Date dateDeb, Date dateFin);
	

	
	//specifique aux factures
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalSansAvoirDTO(Date dateDebut, Date dateFin , String codeTiers);
	
	


	public List<DocumentChiffreAffaireDTO> findChiffreAffaireTotalParCodeTiersDTO(Date dateDebut, Date dateFin, String codeTiers);
	public List<DocumentChiffreAffaireDTO> findChiffreAffaireTotalParCodeArticleDTO(Date dateDebut, Date dateFin, String codeArticle);
	//-----------------------------------------------------//

	public List<TaFactureDTO> findAllDTOPeriode(Date dateDebut, Date dateFin);

	/**RAJOUT YANN**/
	public List<DocumentChiffreAffaireDTO> listLigneArticleDTOTiers(Date dateDebut, Date dateFin, String codeArticle,
			String codeTiers);
	public List<DocumentChiffreAffaireDTO> listLigneArticleDTOTiers(Date dateDebut, Date dateFin, String codeArticle, String codeTiers, String orderBy);
	public List<DocumentChiffreAffaireDTO> listLigneArticleDTOTiersUnite(Date dateDebut, Date dateFin, String codeArticle, String codeUnite, String codeTiers, String orderBy);
	public List<DocumentChiffreAffaireDTO> listLigneArticleDTOTiersUniteFamille(Date dateDebut, Date dateFin, String codeArticle, String codeUnite,String codeFamille, String codeTiers, String orderBy);
	public List<DocumentChiffreAffaireDTO> countFamilleArticleDTOArticle(Date dateDebut, Date dateFin, String codeArticle, String codeTiers);
	
	public List<DocumentChiffreAffaireDTO> listLigneArticlePeriodeParArticleAvoirFactureDTO(Date dateDebut, Date dateFin, String codeArticle);
	public List<DocumentChiffreAffaireDTO> listLigneArticlePeriodeParArticleAvoirFactureDTO(Date dateDebut, Date dateFin, String codeArticle, Boolean synthese);
	public List<DocumentChiffreAffaireDTO> listLigneArticlePeriodeParArticleAvoirFactureDTO(Date dateDebut, Date dateFin, String codeArticle, Boolean synthese, String orderBy);
	public List<DocumentChiffreAffaireDTO> listLigneArticlePeriodeParArticleParUniteAvoirFactureDTO(Date dateDebut, Date dateFin, String codeArticle,String codeUnite, String orderBy);
	public List<DocumentChiffreAffaireDTO> listLigneArticlePeriodeParTiersAvoirFactureDTO(Date dateDebut, Date dateFin, String codeArticle,String codeTiers, Boolean synthese, String orderBy);
	public List<DocumentChiffreAffaireDTO> listLigneArticlePeriodeParTiersParUniteAvoirFactureDTO(Date dateDebut, Date dateFin, String codeArticle,String codeTiers,String codeUnite, String orderBy);
	public List<DocumentChiffreAffaireDTO> listLigneArticlePeriodeParTiersEtArticlesAvoirFactureDTO(Date dateDebut, Date dateFin, String codeArticle,String codeTiers, Boolean synthese, String orderBy);
	
	public List<DocumentChiffreAffaireDTO> listLigneArticlePeriodeAvoirFactureFournisseurDTO(Date dateDebut, Date dateFin, String codeArticle);
	public List<DocumentChiffreAffaireDTO> listLigneArticlePeriodeAvoirFactureFournisseurDTO(Date dateDebut, Date dateFin, String codeArticle, Boolean synthese);
	public List<DocumentChiffreAffaireDTO> listLigneArticlePeriodeAvoirFactureFournisseurDTO(Date dateDebut, Date dateFin, String codeArticle, Boolean synthese, String orderBy);
	
	public List<DocumentChiffreAffaireDTO> listLigneArticlePeriodeParFamilleArticleAvoirFactureDTO(Date dateDebut, Date dateFin, String codeArticle, String orderBy);
	public List<DocumentChiffreAffaireDTO>  detailParFamilleArticleAvoirFactureDTO(Date dateDebut, Date dateFin, String codeFamille, String orderBy);
	public List<DocumentChiffreAffaireDTO> detailParFamilleUniteArticleAvoirFactureDTO(Date dateDebut, Date dateFin, String codeFamille, String codeUnite, String orderBy);
//	public List<DocumentDTO> listLigneArticlePeriodeParArticleAvoirFactureDTO(Date dateDebut, Date dateFin, String codeArticle);
	
	public long countTiersAyantAcheterArticleSurPeriode(Date dateDebut, Date dateFin, String codeArticle);
	public List<DocumentChiffreAffaireDTO> listTiersAyantAcheterArticleDTO(Date dateDebut, Date dateFin, String codeArticle);
	//Pour le graphique dans l'onglet facture et avoirs du tableau de bord par article
	public List<DocumentChiffreAffaireDTO> listSumCAMoisTotalLigneArticlePeriodeParArticleMoinsAvoir(Date dateDebut, Date dateFin, String codeArticle);
	
	
	public List<DocumentChiffreAffaireDTO> listLigneArticleDTOTiersParTypeEtatDoc(Date dateDebut, Date dateFin, String codeArticle, String codeTiers, String typeEtatDoc, int deltaNbJours);

	public List<TaFactureDTO> rechercheDocumentDTO(Date dateExport, String codeTiers, Date dateDeb, Date dateFin);

	public List<TaFactureDTO> rechercheDocumentDTO(Date dateDeb, Date dateFin, String codeTiers, Boolean export);

	public List<TaFactureDTO> rechercheDocumentDTO(String codeDeb, String codeFin);

	public List<TaFactureDTO> rechercheDocumentDTO(Date dateDeb, Date dateFin);

	public void executeUpdate(String requete,List<Integer> ids,Date dateExport);

	public Date selectMinDateDocumentNonExporte(Date dateDeb, Date dateFin);

	public long countDocument(Date debut, Date fin, String codeTiers , String codeArticle);

	public List<TaFactureDTO> rechercheDocumentPartiellementOrTotalementRegle();
	
//	public List<DocumentDTO> findAllDTOPeriodeAvecEtat(Date dateDebut, Date dateFin,String codeTiers, String etat);
//	public List<DocumentChiffreAffaireDTO> chiffreAffaireTotalAvecEtatDTO(Date dateDebut, Date dateFin,String codeTiers, String etat);
//	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalAvecEtatDTO(Date dateDebut, Date dateFin,String codeTiers, String etat);
//	
//	public DocumentChiffreAffaireDTO chiffreAffaireTotalSurEtatDTO(Date dateDebut, Date dateFin,String codeTiers, String etat);
//	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalSurEtatDTO(Date dateDebut, Date dateFin,String codeTiers, String etat);
//	public List<DocumentChiffreAffaireDTO> countDocumentAvecEtat(Date debut, Date fin, String codeTiers , String codeArticle, String etat);
//	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalJmaAvecEtatDTO(Date dateDebut, Date dateFin, int precision,String codeTiers,String etat);
	
//	public List<DocumentChiffreAffaireDTO> chiffreAffaireTotalAvecEtatDTO(Date dateDebut, Date dateFin,String codeTiers, String etat);
//	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalAvecEtatDTO(Date dateDebut, Date dateFin,String codeTiers, String etat) ;
//	public List<DocumentDTO> findAllDTOPeriodeAvecEtat(Date dateDebut, Date dateFin,String codeTiers, String etat) ;
//	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalSurEtatDTO(Date dateDebut, Date dateFin,String codeTiers, String etat) ;
//	public DocumentChiffreAffaireDTO chiffreAffaireTotalSurEtatDTO(Date dateDebut, Date dateFin,String codeTiers, String etat) ;

	
}
