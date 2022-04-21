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
import fr.legrain.document.dto.TaAvoirDTO;
import fr.legrain.document.dto.TaFactureDTO;
import fr.legrain.document.model.TaAvoir;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaPreparation;
import fr.legrain.document.model.TaTicketDeCaisse;
import fr.legrain.edition.model.TaEdition;
import fr.legrain.tiers.model.TaTiers;

@Remote
public interface ITaAvoirServiceRemote extends IGenericCRUDServiceDocumentRemote<TaAvoir,TaAvoirDTO>,
IDocumentLigneALigneService<TaAvoir>,														
IAbstractLgrDAOServer<TaAvoir>,IAbstractLgrDAOServerDTO<TaAvoirDTO>,
														IDocumentService<TaAvoir>, IDocumentTiersStatistiquesService<TaAvoir>,
														IDocumentCodeGenere, IDashboardDocumentServiceRemote{
	public static final String validationContext = "AVOIR";
	public List<TaAvoir> selectAllDisponible(TaFacture taFacture);
	
public TaAvoir mergeAndFindById(TaAvoir detachedInstance, String validationContext);
	
	
public String genereCode( Map<String, String> params);
public void annuleCode(String code);
public void verrouilleCode(String code);

	public void calculeTvaEtTotaux(TaAvoir doc);
	
	/**
	 * Calcul des totaux de la facture
	 */
	public void calculTotaux(TaAvoir doc);

	/**
	 * Reparti le total chaque code TVA sur l'ensemble des lignes concernées par ce code. 
	 */
	public void dispatcherTva(TaAvoir doc);
	
	/**
	 * Lance la fonction de calcul du montant sur chacunes des lignes du document.
	 */
	public void calculMontantLigneDocument(TaAvoir doc);
	
	/**
	 * Calcul de la grille de TVA en fonction de lignes du document et du taux de remise HT global.
	 * Mise à jour de la propriété <code>lignesTVA</code>
	 * et mise à jour du montant de la TVA dans les lignes du document
	 */
	public void calculTvaTotal(TaAvoir doc);
	
	/**
	 * Calcul de la grille de TVA en fonction de lignes du document et du taux de remise HT global.
	 * Mise à jour de la propriété <code>lignesTVA</code>
	 */
	public void calculLignesTva(TaAvoir doc);
	
	public Date calculDateEcheanceAbstract(TaAvoir doc, Integer report, Integer finDeMois) ;
	
	
	public Date calculDateEcheance(TaAvoir doc, Integer report, Integer finDeMois) ;
	

	public List<DocumentChiffreAffaireDTO> listLigneArticleDTOTiers(Date dateDebut, Date dateFin, String codeArticle, String codeTiers, String orderBy);
	public List<DocumentChiffreAffaireDTO> listLigneArticleDTOTiersUnite(Date dateDebut, Date dateFin, String codeArticle, String codeUnite, String codeTiers, String orderBy);
	public List<DocumentChiffreAffaireDTO> listLigneArticleDTOTiersUniteFamille(Date dateDebut, Date dateFin, String codeArticle, String codeUnite,String codeFamille, String codeTiers, String orderBy);
	
	
	public List<TaAvoirDTO> findAllLight();
	
	public List<TaAvoir> rechercheDocumentNonExporte(Date dateDeb, Date dateFin,boolean parDate);
	public List<TaAvoir> rechercheDocumentOrderByDate(Date dateDeb, Date dateFin);
	public List<TaAvoirDTO> rechercheDocumentNonExporteLight(Date dateDeb, Date dateFin,boolean parDate);
	
	
//	public long countAvoir(Date debut, Date fin);
	public List<DocumentChiffreAffaireDTO> findChiffreAffaireTotalDTO(Date debut, Date fin);
	public List<TaAvoir> selectReglementNonLieAuDocument(TaFactureDTO taDocument,Date dateDeb,Date dateFin);
	public List<TaAvoir> selectAllLieAuDocument(TaFactureDTO taDocument,Date dateDeb,Date dateFin);
	
	public int selectCountDisponible(TaTiers taTiers);
	
	public String writingFileEdition(TaEdition taEdition);

	public List<TaAvoirDTO> rechercheDocumentDTO(Date dateExport, String codeTiers, Date dateDeb, Date dateFin);

	public List<TaAvoirDTO> rechercheDocumentDTO(Date dateDeb, Date dateFin, String codeTiers, Boolean export);

	public Date selectMinDateDocumentNonExporte(Date dateDeb, Date dateFin);
}
