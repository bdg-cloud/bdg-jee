package fr.legrain.documents.dao;

import java.util.Date;
import java.util.List;

import fr.legrain.abonnement.stripe.model.TaStripePaymentIntent;
import fr.legrain.data.IGenericDAO;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.TaPanierDTO;
import fr.legrain.document.model.TaBoncde;
import fr.legrain.document.model.TaPanier;
import fr.legrain.document.model.TaReglement;
import fr.legrain.tiers.dao.IDocumentDAO;

//@Remote
public interface IPanierDAO extends IGenericDAO<TaPanier>,IDocumentDAO<TaPanier>,IDashboardDocumentDAO {

	public List<IDocumentTiers> selectDocNonTransformeRequete(IDocumentTiers doc ,String codeTiers,String typeOrigine,String typeDest,Date debut,Date fin);
	
	public List<TaPanierDTO> findAllLight();
	public List<TaPanierDTO> findByCodeLight(String code);
	
	public TaPanier findByActif(String codeTiers);
	public TaPanier findByActifAvecLiaisonReglement(String codeTiers);
	
	public TaBoncde findCommandePanier(String codePanier);
	public TaReglement findReglementPourPanier(String codePanier);
	public TaStripePaymentIntent findReglementStripePanier(String codePanier);
	
//	public List<TaArticlesParTiersDTO> findArticlesParTiersParMois(Date debut, Date fin);
//	public List<TaArticlesParTiersDTO> findArticlesParTiersTransforme(Date debut, Date fin);
//	public List<TaArticlesParTiersDTO> findArticlesParTiersNonTransforme(Date debut, Date fin);
//	public List<TaArticlesParTiersDTO> findArticlesParTiersNonTransformeARelancer(Date debut, Date fin, int deltaNbJours);	
//	public List<TaPanierDTO> findAllDTOPeriode(Date dateDebut, Date dateFin);
//	public List<TaPanierDTO> findPanierNonTransfosDTO(Date dateDebut, Date dateFin);
//	public List<TaPanierDTO> findPanierNonTransfosARelancerDTO(Date dateDebut, Date dateFin, int deltaNbJours);
//	public List<TaPanierDTO> findPanierTransfosDTO(Date dateDebut, Date dateFin);
	
//	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalDTO(Date debut, Date fin);
//	public DocumentChiffreAffaireDTO chiffreAffaireTotalDTO(Date dateDebut, Date dateFin);
//	
//	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireNonTransformeTotalDTO(Date debut, Date fin);
//	public DocumentChiffreAffaireDTO chiffreAffaireNonTransformeTotalDTO(Date dateDebut, Date dateFin);
//
//
//	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTransformeTotalDTO(Date debut, Date fin);
//	public DocumentChiffreAffaireDTO chiffreAffaireTransformeTotalDTO(Date dateDebut, Date dateFin);
//	
//	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireNonTransformeARelancerTotalDTO(Date dateDebut, Date dateFin, int deltaNbJours);
//	public DocumentChiffreAffaireDTO chiffreAffaireNonTransformeARelancerTotalDTO(Date dateDebut, Date dateFin, int deltaNbJours);
//	
//	public long countDocument(Date debut, Date fin);
//	public long countDocumentTransforme(Date debut, Date fin);
//	public long countDocumentNonTransforme(Date debut, Date fin);
//	public long countDocumentNonTransformeARelancer(Date debut, Date fin, int deltaNbJours);
//
//	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalJmaDTO(Date debut, Date fin, int precision);
//	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTransformeJmaDTO(Date debut, Date fin, int precision);
//	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireNonTransformeJmaDTO(Date debut, Date fin, int precision);
//	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireNonTransformeARelancerJmaDTO(Date debut, Date fin, int precision, int deltaNbJours);
//	public long countPanier(Date debut, Date fin);
//	public long countPanierNonTransforme(Date debut, Date fin);
//	public long countPanierNonTransformeARelancer(Date debut, Date fin, int deltaNbJours);
//	public long countPanierTransfos(Date debut, Date fin);
//	public List<DocumentChiffreAffaireDTO> caPanierNonTransfosARelancerDTO(Date dateDebut, Date dateFin, int deltaNbJours);
//	public List<DocumentChiffreAffaireDTO> findChiffreAffaireTotalDTO(Date debut, Date fin);
//	public List<DocumentChiffreAffaireDTO> findChiffreAffaireJmaDTO(Date debut, Date fin, int precision);
//	public List<DocumentChiffreAffaireDTO> findChiffreAffaireTransformeTotalDTO(Date debut, Date fin);
//	public List<DocumentChiffreAffaireDTO> findChiffreAffaireTransformeJmaDTO(Date debut, Date fin, int precision);
//	public List<DocumentChiffreAffaireDTO> findChiffreAffaireNonTransformeJmaDTO(Date debut, Date fin, int precision);
//	public List<DocumentChiffreAffaireDTO> findChiffreAffaireNonTransformeTotalDTO(Date debut, Date fin);
//	public List<DocumentChiffreAffaireDTO> findChiffreAffaireNonTransformeARelancerJmaDTO(Date debut, Date fin, int precision);
}
