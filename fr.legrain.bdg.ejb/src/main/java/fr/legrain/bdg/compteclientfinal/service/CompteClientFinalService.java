package fr.legrain.bdg.compteclientfinal.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.enterprise.inject.Any;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.TransactionSynchronizationRegistry;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import fr.legrain.bdg.compteclientfinal.remote.ICompteClientFinalServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaDevisServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaFactureServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.document.model.TaDevis;
import fr.legrain.document.model.TaFacture;
import fr.legrain.dossier.model.TaInfoEntreprise;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.moncompte.ws.TaDossier;
import fr.legrain.moncompte.ws.client.MonCompteWebServiceClientCXF;
import fr.legrain.paiement.model.PaiementCarteBancaire;
import fr.legrain.paiement.model.RetourPaiementCarteBancaire;
import fr.legrain.tiers.model.IConstWS;
import fr.legrain.tiers.model.TaTiers;
import fr.legrain.tiers.model.TiersDossier;

@Stateless
@WebService(targetNamespace= IConstWS.NAMESPACE_BDG_DEFAULT)
//@WebService
public class CompteClientFinalService implements ICompteClientFinalServiceRemote {
	
	@EJB private ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;
	@EJB private ITaTiersServiceRemote taTiersService;
	@EJB private ITaFactureServiceRemote taFactureService;
	@EJB private ITaDevisServiceRemote taDevisService;
	@EJB private ICompteClientSoapMultitenantProxy multitenantProxy;
	
	private MonCompteWebServiceClientCXF wsMonCompte;
	private TransactionSynchronizationRegistry reg;
	
	@PostConstruct
	public void init() {
		try {
			wsMonCompte = new MonCompteWebServiceClientCXF();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void initTenant(String tenant) {
//		try {
//			wsMonCompte = new MonCompteWebServiceClientCXF();
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		reg.putResource(ServerTenantInterceptor.TENANT_TOKEN_KEY,tenant);
	}
	
	private void initTenantRegistry() {
		try {
			reg = (TransactionSynchronizationRegistry) new InitialContext().lookup("java:comp/TransactionSynchronizationRegistry");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public RetourPaiementCarteBancaire payerFactureCB(String codeDossierFournisseur, String codeClientChezCeFournisseur, PaiementCarteBancaire cb, TaFacture facture) throws EJBException{
		initTenantRegistry();
		initTenant(codeDossierFournisseur);
		try {
			return multitenantProxy.payerFactureCB(codeDossierFournisseur, codeClientChezCeFournisseur, cb, facture);
		} catch(EJBException e) {
			throw new EJBException(e);
		}
	}
	
	public boolean fournisseurPermetPaiementParCB(String codeDossierFournisseur, String codeClientChezCeFournisseur) {
		initTenantRegistry();
		initTenant(codeDossierFournisseur);
		try {
			return multitenantProxy.fournisseurPermetPaiementParCB(codeDossierFournisseur, codeClientChezCeFournisseur);
		} catch(EJBException e) {
			throw new EJBException(e);
		}
	}

	@Override
	//public Map<String,TaTiers> listeFournisseur() {
	public List<TiersDossier> listeFournisseur() {
		
		String idProduitCompteClient = "fr.legrain.compteclient";
		List<TiersDossier> m = new ArrayList<>();
		
		try {
//			MonCompteWebServiceClientCXF wsMonCompte = new MonCompteWebServiceClientCXF();
			List<TaDossier> ld = wsMonCompte.findListeDossierProduit(idProduitCompteClient);
//			List<TaDossier> ld = wsMonCompte.findListeDossierClient(37);
			initTenantRegistry();
			for (TaDossier taDossier : ld) {
				initTenantRegistry();
				initTenant(taDossier.getCode());
				System.out.println("dossier : "+taDossier.getCode());
//				TaInfoEntreprise infosEts = taInfoEntrepriseService.findInstance();
				TaInfoEntreprise infosEts = multitenantProxy.infosMultiTenant(taDossier.getCode());
//				m.put(taDossier.getCode(), infosEts.getTaTiers());
				m.add(new TiersDossier(taDossier.getCode(), infosEts.getTaTiers()));
			}
		
			return m;
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		
		return null;
	}

	@Override
	public boolean clientExisteChezFournisseur(String codeDossierFournisseur, String codeClientChezCeFournisseur, String cleLiaisonCompteClient) {
		initTenantRegistry();
		initTenant(codeDossierFournisseur);
		return multitenantProxy.clientExisteChezFournisseur(codeDossierFournisseur, codeClientChezCeFournisseur, cleLiaisonCompteClient);
	}

	@Override
	public TaTiers infosClientChezFournisseur(String codeDossierFournisseur, String codeClientChezCeFournisseur) {
		initTenantRegistry();
		initTenant(codeDossierFournisseur);
		return multitenantProxy.infosClientChezFournisseur(codeDossierFournisseur, codeClientChezCeFournisseur);
	}
	
	@Override
	public byte[] pdfClient(String codeDossierFournisseur, String codeClientChezCeFournisseur) {
		initTenantRegistry();
		initTenant(codeDossierFournisseur);
		return multitenantProxy.pdfClient(codeDossierFournisseur, codeClientChezCeFournisseur);
	}
	
	@Override
	public byte[] pdfFacture(String codeDossierFournisseur, String codeFactureChezCeFournisseur) {
		multitenantProxy.setTenant(codeDossierFournisseur);
		return multitenantProxy.pdfFacture(codeDossierFournisseur, codeFactureChezCeFournisseur);
	}

	@Override
	public List<TaFacture> facturesClientChezFournisseur(String codeDossierFournisseur, String codeClientChezCeFournisseur) {
		initTenantRegistry();
		initTenant(codeDossierFournisseur);
		
		//List<TaFacture> listeDoc = taFactureService.findByCodeTiersAndDate(codeClientChezCeFournisseur, debut, fin);
		
		//return listeDoc;
		return null;
	}

	@Override
	@WebMethod(operationName = "facturesClientChezFournisseurDate")
	public List<TaFacture> facturesClientChezFournisseur(String codeDossierFournisseur, String codeClientChezCeForunisseur, Date debut, Date fin) {
		initTenantRegistry();
		initTenant(codeDossierFournisseur);
		return multitenantProxy.facturesClientChezFournisseur(codeDossierFournisseur, codeClientChezCeForunisseur, debut, fin);
	}

	@Override
	public List<TaDevis> devisClientChezFournisseur(String codeDossierFournisseur, String codeClientChezCeForunisseur) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "devisClientChezFournisseurDate")
	public List<TaDevis> devisClientChezFournisseur(String codeDossierFournisseur, String codeClientChezCeForunisseur,
			Date debut, Date fin) {
		// TODO Auto-generated method stub
		return null;
	}

}
