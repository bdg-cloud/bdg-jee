package fr.legrain.bdg.documents.service.remote;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import fr.legrain.bdg.dashboard.service.remote.IDashboardDocumentServiceRemote;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceDocumentRemote;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.document.dto.DocumentChiffreAffaireDTO;
import fr.legrain.document.dto.TaPrelevementDTO;
import fr.legrain.document.model.TaPrelevement;
import fr.legrain.edition.model.TaEdition;
import fr.legrain.document.model.TaPreparation;

@Remote
public interface ITaPrelevementServiceRemote extends IGenericCRUDServiceDocumentRemote<TaPrelevement,TaPrelevementDTO>,
IDocumentLigneALigneService<TaPrelevement>,														
IAbstractLgrDAOServer<TaPrelevement>,IAbstractLgrDAOServerDTO<TaPrelevementDTO>,
														IDocumentService<TaPrelevement>, IDocumentTiersStatistiquesService<TaPrelevement>,IDashboardDocumentServiceRemote{
public static final String validationContext = "PRELEVEMENT";
	
	public String genereCode( Map<String, String> params);
	public void annuleCode(String code);
	public void verrouilleCode(String code);
	
	public TaPrelevement mergeAndFindById(TaPrelevement detachedInstance, String validationContext);
	
	public void calculeTvaEtTotaux(TaPrelevement doc);
	
	/**
	 * Calcul des totaux de la facture
	 */
	public void calculTotaux(TaPrelevement doc);

	/**
	 * Reparti le total chaque code TVA sur l'ensemble des lignes concernées par ce code. 
	 */
	public void dispatcherTva(TaPrelevement doc);
	
	/**
	 * Lance la fonction de calcul du montant sur chacunes des lignes du document.
	 */
	public void calculMontantLigneDocument(TaPrelevement doc);
	
	/**
	 * Calcul de la grille de TVA en fonction de lignes du document et du taux de remise HT global.
	 * Mise à jour de la propriété <code>lignesTVA</code>
	 * et mise à jour du montant de la TVA dans les lignes du document
	 */
	public void calculTvaTotal(TaPrelevement doc);
	
	/**
	 * Calcul de la grille de TVA en fonction de lignes du document et du taux de remise HT global.
	 * Mise à jour de la propriété <code>lignesTVA</code>
	 */
	public void calculLignesTva(TaPrelevement doc);
	
	public Date calculDateEcheanceAbstract(TaPrelevement doc, Integer report, Integer finDeMois);
	
	public Date calculDateEcheance(TaPrelevement doc, Integer report, Integer finDeMois);
	
	public List<TaPrelevementDTO> findAllLight();
	
	public String writingFileEdition(TaEdition taEdition);
	
//	public long countPrelevement(Date debut, Date fin);	
//	public long countPrelevementNonTransforme(Date debut, Date fin);
//	public long countPrelevementNonTransformeARelancer(Date debut, Date fin, int deltaNbJours);
//	public long countPrelevementTransfos(Date debut, Date fin);
//	public List<DocumentChiffreAffaireDTO> caPrelevementNonTransfosARelancerDTO(Date dateDebut, Date dateFin, int deltaNbJours);
////	public List<DocumentChiffreAffaireDTO> findChiffreAffaireTotal(Date debut, Date fin, int precision);
//	public List<DocumentChiffreAffaireDTO> findChiffreAffaireTotalDTO(Date debut, Date fin);
//	public List<DocumentChiffreAffaireDTO> findChiffreAffaireJmaDTO(Date debut, Date fin, int precision);
//	public List<DocumentChiffreAffaireDTO> findChiffreAffaireTransformeTotalDTO(Date debut, Date fin);
//	public List<DocumentChiffreAffaireDTO> findChiffreAffaireTransformeJmaDTO(Date debut, Date fin, int precision);
//	public List<DocumentChiffreAffaireDTO> findChiffreAffaireNonTransformeTotalDTO(Date debut, Date fin);
//	public List<DocumentChiffreAffaireDTO> findChiffreAffaireNonTransformeJmaDTO(Date debut, Date fin, int precision);
//	public List<DocumentChiffreAffaireDTO> findChiffreAffaireTransformeARelancerJmaDTO(Date debut, Date fin,int precision);
		

}

