package fr.legrain.bdg.documents.service.remote;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.Remote;

import fr.legrain.bdg.dashboard.service.remote.IDashboardDocumentPayableServiceRemote;
import fr.legrain.bdg.dashboard.service.remote.IDashboardDocumentServiceRemote;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceDocumentRemote;
import fr.legrain.document.dto.DocumentChiffreAffaireDTO;
import fr.legrain.document.dto.IDocumentPayableCB;
import fr.legrain.document.dto.TaTicketDeCaisseDTO;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaPreparation;
import fr.legrain.document.model.TaRAcompte;
import fr.legrain.document.model.TaRAvoir;
import fr.legrain.document.model.TaRReglement;
import fr.legrain.document.model.TaReglement;
import fr.legrain.document.model.TaTPaiement;
import fr.legrain.document.model.TaTicketDeCaisse;
import fr.legrain.paiement.model.PaiementCarteBancaire;
import fr.legrain.paiement.model.RetourPaiementCarteBancaire;
import fr.legrain.tiers.model.TaTiers;

@Remote
public interface ITaTicketDeCaisseServiceRemote extends IGenericCRUDServiceDocumentRemote<TaTicketDeCaisse,TaTicketDeCaisseDTO>,
IDocumentLigneALigneService<TaTicketDeCaisse>,														
IAbstractLgrDAOServer<TaTicketDeCaisse>,IAbstractLgrDAOServerDTO<TaTicketDeCaisseDTO>,
														IDocumentService<TaTicketDeCaisse>, IDocumentTiersStatistiquesService<TaTicketDeCaisse>,
														IDocumentCodeGenere,IDashboardDocumentPayableServiceRemote {
	public static final String validationContext = "TICKET_CAISSE";
	
	public TaTicketDeCaisse mergeAndFindById(TaTicketDeCaisse detachedInstance, String validationContext);
	
	public List<TaTicketDeCaisse> findByCodeTiersAndDateCompteClient(String codeTiers, Date debut, Date fin);
	
	
	public void calculeTvaEtTotaux(TaTicketDeCaisse doc);
	
	/**
	 * Calcul des totaux de la facture
	 */
	public void calculTotaux(TaTicketDeCaisse doc);

	/**
	 * Reparti le total chaque code TVA sur l'ensemble des lignes concernées par ce code. 
	 */
	public void dispatcherTva(TaTicketDeCaisse doc);
	
	/**
	 * Lance la fonction de calcul du montant sur chacunes des lignes du document.
	 */
	public void calculMontantLigneDocument(TaTicketDeCaisse doc);
	
	/**
	 * Calcul de la grille de TVA en fonction de lignes du document et du taux de remise HT global.
	 * Mise à jour de la propriété <code>lignesTVA</code>
	 * et mise à jour du montant de la TVA dans les lignes du document
	 */
	public void calculTvaTotal(TaTicketDeCaisse doc);
	
	/**
	 * Calcul de la grille de TVA en fonction de lignes du document et du taux de remise HT global.
	 * Mise à jour de la propriété <code>lignesTVA</code>
	 */
	public void calculLignesTva(TaTicketDeCaisse doc);
	
	public void calculDateEcheanceAbstract(TaTicketDeCaisse doc, Integer report, Integer finDeMois) ;
	
	public Date calculDateEcheanceAbstract(TaTicketDeCaisse doc, Integer report, Integer finDeMois,TaTPaiement taTPaiement) ;
	
	public Date calculDateEcheance(TaTicketDeCaisse doc, Integer report, Integer finDeMois,TaTPaiement taTPaiement) ;
	
	public void mettreAJourDateEcheanceReglement(TaTicketDeCaisse doc, TaReglement reglement);
		
	public boolean contientReglementAffectationMultiple(TaTicketDeCaisse doc);
	public void mettreAJourDateEcheanceReglements(TaTicketDeCaisse doc);
	public boolean reglementRempli(TaTicketDeCaisse doc);
	public boolean reglementExiste(TaTicketDeCaisse doc);
	public LinkedList<TaRReglement> rechercheSiDocumentContientTraite(TaTicketDeCaisse doc, String typeTraite);
	public void modifieLibellePaiementMultiple(TaTicketDeCaisse doc);
	public void modifieTypePaiementMultiple(TaTicketDeCaisse doc);
	public TaTicketDeCaisse affecteReglementFacture(TaTicketDeCaisse doc, String typePaiementDefaut) throws Exception;
	public void gestionReglement(TaTicketDeCaisse doc, TaRReglement taReglement);
	public void removeTousRAvoirs(TaTicketDeCaisse doc) throws Exception;
//	public void removeTousRAcomptes(TaTicketDeCaisse doc) throws Exception;
	public TaTicketDeCaisse removeTousRReglements(TaTicketDeCaisse doc) throws Exception;
	public TaTicketDeCaisse removeReglement(TaTicketDeCaisse doc, TaRReglement taRReglement);
	public TaTicketDeCaisse addRReglement(TaTicketDeCaisse doc, TaRReglement taReglement);
//	public void removeRAcompte(TaTicketDeCaisse doc, TaRAcompte taRAcompte);
	public void addRAvoir(TaTicketDeCaisse doc, TaRAvoir taRAvoir);
	public void removeRAvoir(TaTicketDeCaisse doc, TaRAvoir taRAvoir);
//	public void addRAcompte(TaTicketDeCaisse doc, TaRAcompte taRAcompte);
	public TaRReglement creeRReglement(TaTicketDeCaisse doc,TaRReglement taRReglement,Boolean integrer,String typePaiementDefaut) throws Exception;
	public TaRReglement creeRReglement(TaTicketDeCaisse doc,TaRReglement taRReglement,Boolean integrer,String typePaiementDefaut,TaReglement reglement) throws Exception;
	public boolean multiReglement(TaTicketDeCaisse doc);
	public BigDecimal calculResteAReglerComplet(TaTicketDeCaisse doc);
	public TaTicketDeCaisse calculRegleDocument(TaTicketDeCaisse doc);
	public BigDecimal calculRegleDocumentComplet(TaTicketDeCaisse doc);
	public BigDecimal calculSommeReglementsComplet(TaTicketDeCaisse doc,TaRReglement taRReglementEnCours);
	public BigDecimal calculSommeReglementsComplet(TaTicketDeCaisse doc);
	public Boolean aDesAvoirsIndirects(TaTicketDeCaisse doc);
	public Boolean aDesReglementsIndirects(TaTicketDeCaisse doc);
	public BigDecimal calculSommeReglementsIntegres(TaTicketDeCaisse doc);
	public BigDecimal calculSommeReglementsIntegresEcran(TaTicketDeCaisse doc);
//	public void calculSommeAcomptes(TaTicketDeCaisse doc);
	public BigDecimal calculSommeAcomptes(TaTicketDeCaisse doc, TaRAcompte acompteEnCours);
	public BigDecimal calculSommeAvoir(TaTicketDeCaisse doc, TaRAvoir avoirEnCours);
	public BigDecimal calculSommeAvoir(TaTicketDeCaisse doc);
//	public BigDecimal calculSommeAvoirIntegres(TaTicketDeCaisse doc);
//	public void dispatcherTvaAvantRemise(TaTicketDeCaisse doc);
//	public BigDecimal resteTVAAvantRemise(TaTicketDeCaisse doc, LigneTva ligneTva);
//	public BigDecimal prorataMontantTVALigneAvantRemise(TaTicketDeCaisse doc, TaLFacture ligne, LigneTva ligneTva);
//	public BigDecimal prorataMontantTVALigne(TaTicketDeCaisse doc, TaLFacture ligne, LigneTva ligneTva);
	public void removeTousLesAbonnements(TaTicketDeCaisse persistentInstance) throws Exception;
	public void removeTousLesSupportAbons(TaTicketDeCaisse persistentInstance) throws Exception;
	
	public List<TaTicketDeCaisseDTO> findAllLight();
	public List<TaTicketDeCaisseDTO> findAllDTOPeriode(Date dateDebut, Date dateFin);
	public List<TaTicketDeCaisse> rechercheDocumentNonExporte(Date dateDeb, Date dateFin,Boolean parDate);
	public List<TaTicketDeCaisse> rechercheDocumentOrderByDate(Date dateDeb, Date dateFin);
	public List<TaTicketDeCaisseDTO> rechercheDocumentNonExporteLight(Date dateDeb, Date dateFin,boolean parDate) ;
	public List<Object> findChiffreAffaireTotal(Date debut, Date fin, int precision);
//	public List<TaTicketDeCaisseDTO> findAllDTOPeriode(Date dateDebut, Date dateFin);
	
	public List<TaTicketDeCaisse> rechercheDocumentNonTotalementRegle(Date dateDeb, Date dateFin) ;
	public List<TaTicketDeCaisseDTO> rechercheDocumentNonTotalementRegle(Date dateDeb, Date dateFin,String tiers,String document);
	public List<TaTicketDeCaisse> rechercheDocumentNonTotalementRegleAEcheance(Date dateDeb, Date dateFin,String tiers,String document,BigDecimal limite);
	public List<Object[]> rechercheDocumentNonTotalementRegleAEcheance2(Date dateDeb, Date dateFin,String tiers,String document);


	
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalSansAvoirDTO(Date dateDebut, Date dateFin , String codeTiers);

	public String creerPaymentIntent(PaiementCarteBancaire pcb, TaTiers tiersPourReglementLibre, String libelle);
	public String creerPaymentIntent(PaiementCarteBancaire pcb, IDocumentPayableCB documentPayableCB, String libelle);

}
