package fr.legrain.bdg.rest.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.ejb.EJBException;

import fr.legrain.abonnement.dto.TaAbonnementFullDTO;
import fr.legrain.bdg.rest.model.ParamCompteEspaceClient;
import fr.legrain.document.dto.TaAvisEcheanceDTO;
import fr.legrain.document.dto.TaBoncdeDTO;
import fr.legrain.document.dto.TaBonlivDTO;
import fr.legrain.document.dto.TaDevisDTO;
import fr.legrain.document.dto.TaFactureDTO;
import fr.legrain.document.model.TaFacture;
import fr.legrain.dossier.model.TaInfoEntreprise;
import fr.legrain.paiement.model.PaiementCarteBancaire;
import fr.legrain.paiement.model.RetourPaiementCarteBancaire;
import fr.legrain.tiers.dto.TaEspaceClientDTO;
import fr.legrain.tiers.dto.TaParamEspaceClientDTO;
import fr.legrain.tiers.dto.TaTiersDTO;
import fr.legrain.tiers.model.TaEspaceClient;
import fr.legrain.tiers.model.TaTiers;

public interface IConstRestModel {
	
	public String separateurChaineCryptee = "~";

	void init();

	TaInfoEntreprise infosMultiTenant(String tenant);

	TaTiersDTO infosTiersMultiTenantDTO(String tenant);

	//@Transactional(value=TxType.REQUIRES_NEW)
//	String creerPaymentIntent(String codeDossierFournisseur, String codeClientChezCeFournisseur, BigDecimal montant,
//			String codeDocument, String typeDocument) throws EJBException;

	RetourPaiementCarteBancaire payerFactureCB(String codeDossierFournisseur, String codeClientChezCeFournisseur,
			PaiementCarteBancaire cb, TaFacture facture) throws EJBException;

//	boolean fournisseurPermetPaiementParCB(String codeDossierFournisseur, String codeClientChezCeFournisseur);
	
//	int etatPaiementCourant(String codeDossierFournisseur, String idStripePaymentIntent) throws EJBException;

	boolean clientExisteChezFournisseur(String codeDossierFournisseur, String codeClientChezCeFournisseur,
			String cleLiaisonCompteClient);

	TaTiersDTO infosClientChezFournisseurDTO(String codeDossierFournisseur, String codeClientChezCeFournisseur);
	TaTiers infosClientChezFournisseur(String codeDossierFournisseur, String codeClientChezCeFournisseur);
	
//	TaEspaceClientDTO creerCompteEspaceClient(ParamCompteEspaceClient p) throws Exception;
	
//	TaEspaceClientDTO modifierCompteEspaceClient(String codeDossierFournisseur,ParamCompteEspaceClient p) throws Exception;
//	TaEspaceClientDTO liaisonNouveauCompteEspaceClient(String codeDossierFournisseur,ParamCompteEspaceClient p) throws Exception;
	
//	public String clePubliqueStripe(String dossier);
//	public String cleConnectStripe(String dossier);
	
	public TaEspaceClient rechercheEspaceClientMultiTiersSecondaire(String codeTiers);
	
//	public TaParamEspaceClientDTO parametreEspaceClient(String dossier);
//	public byte[] logo(String dossier);
		
//	public TaEspaceClientDTO demandeMotDePasse(ParamCompteEspaceClient p);
//	public TaEspaceClientDTO validationMotDePasse(ParamCompteEspaceClient p) throws Exception;
	
	TaEspaceClientDTO login(ParamCompteEspaceClient p);

	byte[] pdfClient(String codeDossierFournisseur, String codeClientChezCeFournisseur);

//	byte[] pdfFacture(String codeDossierFournisseur, String codeFactureChezCeFournisseur);
//
//	String pdfFactureFile(String codeDossierFournisseur, String codeFactureChezCeFournisseur);
//	String pdfDevisFile(String codeDossierFournisseur, String codeFactureChezCeFournisseur);
//	public String pdfCommandeFile(String codeDossierFournisseur, String codeFactureChezCeFournisseur);
//	String pdfAvisEcheanceFile(String codeDossierFournisseur, String codeFactureChezCeFournisseur);
//
//	List<TaFacture> facturesClientChezFournisseur(String codeDossierFournisseur, String codeClientChezCeFournisseur);
//
//	List<TaFacture> facturesClientChezFournisseur(String codeDossierFournisseur, String codeClientChezCeForunisseur,
//			Date debut, Date fin);
//
//	List<TaFactureDTO> facturesClientChezFournisseurDTO(String codeDossierFournisseur,
//			String codeClientChezCeForunisseur, Date debut, Date fin);
//	
//	TaFactureDTO getFactureById(String codeDossierFournisseur, String codeClientChezCeFournisseur, Integer idFacture);
//	
//	TaAvisEcheanceDTO getAvisEcheanceById(String codeDossierFournisseur, String codeClientChezCeFournisseur, Integer idAvisEcheance);
//
//	List<TaDevisDTO> devisClientChezFournisseurDTO(String codeDossierFournisseur, String codeClientChezCeForunisseur,
//			Date debut, Date fin);
//	
//	List<TaBonlivDTO> livraisonsClientChezFournisseurDTO(String codeDossierFournisseur, String codeClientChezCeForunisseur,
//			Date debut, Date fin);
//	
//	List<TaBoncdeDTO> commandesClientChezFournisseurDTO(String codeDossierFournisseur, String codeClientChezCeForunisseur,
//			Date debut, Date fin);
//	
//	List<TaAvisEcheanceDTO> avisEcheanceClientChezFournisseurDTO(String codeDossierFournisseur, String codeClientChezCeForunisseur,
//			Date debut, Date fin);
//	
//	public List<TaAbonnementFullDTO> abonnementClientChezFournisseurDTO(String codeDossierFournisseur, String codeClientChezCeForunisseur,
//			Date debut, Date fin);
//	
//	public TaFactureDTO facturePourAvisEcheance(String codeDossierFournisseur, String codeAvisEcheance);

	String getTenant();

	void setTenant(String tenant);
	
//	String replaceTenantAlias(String tenantAlias);
//
//	boolean authentification(String dossier, String login, String password);

}