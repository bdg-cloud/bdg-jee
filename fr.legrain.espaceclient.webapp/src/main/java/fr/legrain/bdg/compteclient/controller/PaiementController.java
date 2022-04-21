package fr.legrain.bdg.compteclient.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.xml.ws.soap.SOAPFaultException;

import fr.legrain.bdg.compteclient.model.TaMesFournisseurs;
import fr.legrain.bdg.compteclient.service.interfaces.remote.general.ThrowableExceptionLgr;
import fr.legrain.bdg.compteclient.ws.PaiementCarteBancaire;
import fr.legrain.bdg.compteclient.ws.RetourPaiementCarteBancaire;
import fr.legrain.bdg.compteclient.ws.TaFacture;
import fr.legrain.bdg.ws.client.CompteClientServiceClientCXF;

@ManagedBean 
@ViewScoped
public class PaiementController implements Serializable {

	private static final long serialVersionUID = -6935358924065762869L;
	
	private CompteClientServiceClientCXF wsCompteClient;
	
	private int[] listeMoisCB = new int[]{1,2,3,4,5,6,7,8,9,10,11,12};
	private int[] listeAnneeCB = new int[]{2019,2020,2021,2022,2023,2024,2025,2026,2027,2028,2029};
	
	//@ManagedProperty(value = "#{param.mesfrs}")
	private TaMesFournisseurs taMesFournisseurs;
	
	//@ManagedProperty(value = "#{param.fact}")
	private TaFacture taFacture;
	
	private String numCarte;
	private int moisCarte;
	private int anneeCarte;
	private String cryptoCarte;
	private String nomCarte;
	
	private RetourPaiementCarteBancaire rcb = null;
	
//	private BigDecimal montantPaiement;
//	private String descriptionPaiement;
	
	@PostConstruct
	public void init() {
		
		try {
			wsCompteClient = new CompteClientServiceClientCXF();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		taMesFournisseurs = (TaMesFournisseurs) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("mesfrs");
		taFacture = (TaFacture) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fact");
		

	}
	
	public void actPayer(ActionEvent e) {
		System.out.println("PaiementController.actPayer()");
		
		try {
			PaiementCarteBancaire pcb = new PaiementCarteBancaire();
			pcb.setNumeroCarte(numCarte);
			pcb.setNomPorteurCarte(nomCarte);
			pcb.setMoisCarte(moisCarte);
			pcb.setAnneeCarte(anneeCarte);
			pcb.setCryptogrammeCarte(cryptoCarte);
			pcb.setMontantPaiement(taFacture.getResteAReglerComplet());
			pcb.setOriginePaiement("BDG Espace client");
			pcb.setCompteClient(true);
			
			rcb = wsCompteClient.payerFactureCB(taMesFournisseurs.getTaFournisseur().getCodeDossier(), taMesFournisseurs.getCodeClient(), pcb, taFacture);
			
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("mesfrs");
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("fact");
			
			if(rcb!=null) {
				System.out.println("PaiementController.actPayer() ref : "+rcb.getRefPaiement());
			}
//		} catch(SOAPFaultException ex) {
//			FacesContext.getCurrentInstance().addMessage(
//					null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur",
//							ex.getMessage())
//					);
//			ex.printStackTrace();
		} catch(Exception ex) {
//			ThrowableExceptionLgr.renvoieCauseRuntimeException(ex)
			FacesContext.getCurrentInstance().addMessage(
					null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur",
							ThrowableExceptionLgr.renvoieCauseRuntimeException(ex).getMessage())
					);
			ex.printStackTrace();
		}
	}
	
	public void actAnnuler(ActionEvent e) {
		System.out.println("PaiementController.actAnnuler()");
	}
	
	public TaMesFournisseurs getTaMesFournisseurs() {
		return taMesFournisseurs;
	}
	public void setTaMesFournisseurs(TaMesFournisseurs taFournisseur) {
		this.taMesFournisseurs = taFournisseur;
	}
	public TaFacture getTaFacture() {
		return taFacture;
	}
	public void setTaFacture(TaFacture taFacture) {
		this.taFacture = taFacture;
	}
	public String getNumCarte() {
		return numCarte;
	}
	public void setNumCarte(String numCarte) {
		this.numCarte = numCarte;
	}
	public int getMoisCarte() {
		return moisCarte;
	}
	public void setMoisCarte(int moisCarte) {
		this.moisCarte = moisCarte;
	}
	public int getAnneeCarte() {
		return anneeCarte;
	}
	public void setAnneeCarte(int anneeCarte) {
		this.anneeCarte = anneeCarte;
	}
	public String getCryptoCarte() {
		return cryptoCarte;
	}
	public void setCryptoCarte(String cryptoCarte) {
		this.cryptoCarte = cryptoCarte;
	}
	public String getNomCarte() {
		return nomCarte;
	}
	public void setNomCarte(String nomCarte) {
		this.nomCarte = nomCarte;
	}
	public int[] getListeMoisCB() {
		return listeMoisCB;
	}
	public int[] getListeAnneeCB() {
		return listeAnneeCB;
	}

	public RetourPaiementCarteBancaire getRcb() {
		return rcb;
	}

	public void setRcb(RetourPaiementCarteBancaire rcb) {
		this.rcb = rcb;
	}

}
