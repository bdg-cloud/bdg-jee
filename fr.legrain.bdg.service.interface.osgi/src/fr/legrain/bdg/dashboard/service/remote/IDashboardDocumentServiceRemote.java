package fr.legrain.bdg.dashboard.service.remote;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import fr.legrain.document.dashboard.dto.TaArticlesParTiersDTO;
import fr.legrain.document.dto.DocumentChiffreAffaireDTO;
import fr.legrain.document.dto.DocumentDTO;

@Remote
public interface IDashboardDocumentServiceRemote {

	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalDTO(Date dateDebut,Date dateFin,String codeTiers);
	public DocumentChiffreAffaireDTO chiffreAffaireTotalDTO(Date dateDebut, Date dateFin,String codeTiers);
	
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTransformeTotalDTO(Date dateDebut, Date dateFin,String codeTiers);
	public DocumentChiffreAffaireDTO chiffreAffaireTransformeTotalDTO(Date dateDebut, Date dateFin,String codeTiers);

	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireNonTransformeTotalDTO(Date dateDebut, Date dateFin,String codeTiers);
	public DocumentChiffreAffaireDTO chiffreAffaireNonTransformeTotalDTO(Date dateDebut, Date dateFin,String codeTiers);
	
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireNonTransformeARelancerTotalDTO(Date dateDebut, Date dateFin, int deltaNbJours,String codeTiers);
	public DocumentChiffreAffaireDTO chiffreAffaireNonTransformeARelancerTotalDTO(Date dateDebut, Date dateFin, int deltaNbJours,String codeTiers);

	public long countDocument(Date dateDebut, Date dateFin,String codeTiers);
//	public long countDocument(Date debut, Date fin, String codeTiers , String codeArticle);
	public long countDocumentTransforme(Date dateDebut, Date dateFin,String codeTiers);
	public long countDocumentNonTransforme(Date dateDebut, Date dateFin,String codeTiers);
	public long countDocumentNonTransformeARelancer(Date dateDebut, Date dateFin, int deltaNbJours,String codeTiers);
	
//	/*** avec Etat ***/////
//	public List<DocumentChiffreAffaireDTO> countDocumentAvecEtat(Date debut, Date fin, String codeTiers , String codeArticle, String etat);
//	public List<DocumentDTO> findAllDTOPeriodeAvecEtat(Date dateDebut, Date dateFin,String codeTiers, String etat);
//	public List<DocumentChiffreAffaireDTO> chiffreAffaireTotalAvecEtatDTO(Date dateDebut, Date dateFin,String codeTiers, String etat);
//	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalAvecEtatDTO(Date dateDebut, Date dateFin,String codeTiers, String etat);
//	
//	public DocumentChiffreAffaireDTO chiffreAffaireTotalSurEtatDTO(Date dateDebut, Date dateFin,String codeTiers, String etat);
//	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalSurEtatDTO(Date dateDebut, Date dateFin,String codeTiers, String etat);
	

	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalJmaDTO(Date dateDebut, Date dateFin, int precision,String codeTiers);
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireNonTransformeJmaDTO(Date dateDebut, Date dateFin, int precision,String codeTiers);
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTransformeJmaDTO(Date dateDebut, Date dateFin, int precision,String codeTiers);
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireNonTransformeARelancerJmaDTO(Date dateDebut, Date dateFin, int precision, int deltaNbJours,String codeTiers);

	public List<DocumentDTO> findAllDTOPeriode(Date dateDebut, Date dateFin,String codeTiers);

	public List<DocumentDTO> findAllDTOPeriodeSimple(Date dateDebut, Date dateFin,String codeTiers);
	public List<DocumentDTO> findAllDTOIntervalle(String codeDebut, String codeFin,String codeTiers);
	public List<DocumentDTO> findDocumentNonTransfosDTO(Date dateDebut, Date dateFin,String codeTiers);
	public List<DocumentDTO> findDocumentNonTransfosARelancerDTO(Date dateDebut, Date dateFin, int deltaNbJours,String codeTiers);
	public List<DocumentDTO> findDocumentTransfosDTO(Date dateDebut, Date dateFin,String codeTiers);
	

	
	public List<TaArticlesParTiersDTO> findArticlesParTiersParMois(Date debut, Date fin,String codeTiers);
	public List<TaArticlesParTiersDTO> findArticlesParTiersTransforme(Date debut, Date fin,String codeTiers);
	public List<TaArticlesParTiersDTO> findArticlesParTiersNonTransforme(Date debut, Date fin,String codeTiers);
	public List<TaArticlesParTiersDTO> findArticlesParTiersNonTransformeARelancer(Date debut, Date fin, int deltaNbJours,String codeTiers);
	public List<TaArticlesParTiersDTO> findArticlesParTiersParMoisParTypeRegroupement(Date dateDebut, Date dateFin,String codeTiers,String typeRegroupement,Object valeurRegroupement, boolean regroupee) ;

	public List<TaArticlesParTiersDTO> findArticlesParTiersParMois(Date debut, Date fin);
	public List<TaArticlesParTiersDTO> findArticlesParTiersTransforme(Date debut, Date fin);
	public List<TaArticlesParTiersDTO> findArticlesParTiersNonTransforme(Date debut, Date fin);
	public List<TaArticlesParTiersDTO> findArticlesParTiersNonTransformeARelancer(Date debut, Date fin, int deltaNbJours);
	public List<TaArticlesParTiersDTO> findArticlesParTiersParMoisParTypeRegroupement(Date dateDebut, Date dateFin,String typeRegroupement,Object valeurRegroupement);

	
	// regroupement de type exemple : famille article, famille tiers, type paiement, taux tva ....
	public DocumentChiffreAffaireDTO chiffreAffaireTotalDTOParTypeRegroupement(Date dateDebut, Date dateFin,String codeTiers,String typeRegroupement,Object valeurRegroupement,boolean regrouper);
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalDTOParTypeRegroupement(Date dateDebut, Date dateFin,String codeTiers,String typeRegroupement,Object valeurRegroupement,boolean regrouper);
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalJmaDTOParRegroupement(Date dateDebut, Date dateFin,int precision,String codeTiers,String typeRegroupement,Object valeurRegroupement);
	public List<DocumentChiffreAffaireDTO> chiffreAffaireParEtat(Date debut, Date fin,String codeTiers,Object codeEtat,String typeRegroupement,Object valeurRegroupement);
	
	/**RAJOUT YANN**/
	public List<DocumentChiffreAffaireDTO> listeSumChiffreAffaireTotalDTOArticle(Date dateDebut, Date dateFin, String codeArticle, String codeTiers);
	public List<DocumentChiffreAffaireDTO> listeSumChiffreAffaireTotalDTOArticleMois(Date dateDebut, Date dateFin, String codeArticle, String codeTiers);
	public List<DocumentChiffreAffaireDTO> countChiffreAffaireTotalDTOArticle(Date dateDebut, Date dateFin, String codeArticle, String codeTiers);
	public List<DocumentChiffreAffaireDTO> sumChiffreAffaireTotalDTOArticle(Date dateDebut, Date dateFin);
	public List<DocumentChiffreAffaireDTO> sumChiffreAffaireTotalDTOArticle(Date dateDebut, Date dateFin, String codeArticle );
	public List<DocumentChiffreAffaireDTO> sumChiffreAffaireTotalDTOArticleMoinsAvoir(Date dateDebut, Date dateFin, String codeArticle);
	
	//liés aux états
	public List<DocumentChiffreAffaireDTO> countDocumentAndCodeEtatParTypeRegroupement(Date debut, Date fin,String codeTiers,Object codeEtat,String typeRegroupement,Object valeurRegroupement);
	public List<DocumentDTO> findAllDTOPeriodeAndCodeEtatParTypeRegroupement(Date debut, Date fin,String codeTiers,Object codeEtat,String typeRegroupement,Object valeurRegroupement);
	public List<TaArticlesParTiersDTO> findArticlesParTiersMoisAndCodeEtatParTypeRegroupement(Date debut, Date fin,Object codeEtat,String typeRegroupement,Object valeurRegroupement) ;
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalDTOAndCodeEtatParTypeRegroupement(Date dateDebut, Date dateFin,String codeTiers,Object codeEtat,String typeRegroupement,Object valeurRegroupement,boolean regrouper);
	
	
	public List<DocumentDTO> findAllDTOPeriodeAvecEtat(Date dateDebut, Date dateFin, String codeTiers, String etat);
	public List<DocumentChiffreAffaireDTO> chiffreAffaireTotalAvecEtatDTO(Date dateDebut, Date dateFin, String codeTiers,	String etat);
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalAvecEtatDTO(Date dateDebut, Date dateFin, String codeTiers,String etat);
	public DocumentChiffreAffaireDTO chiffreAffaireTotalSurEtatDTO(Date dateDebut, Date dateFin, String codeTiers,	String etat);
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalSurEtatDTO(Date dateDebut, Date dateFin, String codeTiers,String etat);
	public List<DocumentChiffreAffaireDTO> countDocumentAvecEtat(Date debut, Date fin, String codeTiers, String codeArticle,String etat);
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalJmaAvecEtatDTO(Date dateDebut, Date dateFin, int precision,	String codeTiers, String etat);
	public List<DocumentChiffreAffaireDTO> listLigneArticleDTOTiersAvecEtat(Date dateDebut, Date dateFin, String codeArticle,String codeTiers, String etat, int deltaNbJours);
	public List<DocumentChiffreAffaireDTO> listLigneArticleDTOTiersAvecEtat(Date dateDebut, Date dateFin, String codeArticle,String codeTiers, String etat, String orderBy, int deltaNbJours);
	public List<TaArticlesParTiersDTO> findArticlesParTiersParMoisAvecEtat(Date debut, Date fin, String codeTiers, String etat,int deltaNbJours);
	
	

}
