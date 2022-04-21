package fr.legrain.document.model;

import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

public class SwtDocumentListener extends SWTDocumentListeEager {

	@PrePersist
	public void methodInvokedBeforePersist(SWTDocument doc) {
//		System.out.println("persisting document "+doc.getTypeDocument()+" with code = " + doc.getCodeDocument());
	}

	@PostPersist
	public void methodInvokedAfterPersist(SWTDocument doc) {
//		System.out.println("persisting document "+doc.getTypeDocument()+" with code = " + doc.getCodeDocument());
	}

	@PreUpdate
	public void methodInvokedBeforeUpdate(SWTDocument doc) {
//		System.out.println("persisting document "+doc.getTypeDocument()+" with code = " + doc.getCodeDocument());
	}

	@PostUpdate
	public void methodInvokedAfterUpdate(SWTDocument doc) {
//		System.out.println("persisting document "+doc.getTypeDocument()+" with code = " + doc.getCodeDocument());
	}

	@PreRemove
	private void methodInvokedBeforeRemove(SWTDocument doc) {
		System.out.println("methodInvokedBeforeRemove document "+doc.getTypeDocument()+" with code = " + doc.getCodeDocument());
		removeAllRDocuments(doc);
		removeAllRRegmentLiaisons(doc);
	}

	@PostRemove
	public void methodInvokedAfterRemove(SWTDocument doc) {
//		System.out.println("persisting document "+doc.getTypeDocument()+" with code = " + doc.getCodeDocument());
	}
	
	
	private void removeAllRRegmentLiaisons(SWTDocument doc) {
		for (TaRReglementLiaison ligne : doc.getTaRReglementLiaisons()) {			
			if((doc instanceof TaBonliv)&& ligne.getTaBonliv()!=null)ligne.getTaBonliv().getTaRReglementLiaisons().remove(ligne);
			if((doc instanceof TaAcompte)&& ligne.getTaAcompte()!=null)ligne.getTaAcompte().getTaRReglementLiaisons().remove(ligne);
			if((doc instanceof TaAvoir)&& ligne.getTaAvoir()!=null)ligne.getTaAvoir().getTaRReglementLiaisons().remove(ligne);
			if((doc instanceof TaApporteur)&& ligne.getTaApporteur()!=null)ligne.getTaApporteur().getTaRReglementLiaisons().remove(ligne);
			if((doc instanceof TaAvisEcheance)&& ligne.getTaAvisEcheance()!=null)ligne.getTaAvisEcheance().getTaRReglementLiaisons().remove(ligne);
			if((doc instanceof TaAvoir)&& ligne.getTaAvoir()!=null)ligne.getTaAvoir().getTaRReglementLiaisons().remove(ligne);
			if((doc instanceof TaBoncde)&& ligne.getTaBoncde()!=null)ligne.getTaBoncde().getTaRReglementLiaisons().remove(ligne);
			if((doc instanceof TaBoncdeAchat)&& ligne.getTaBoncdeAchat()!=null)ligne.getTaBoncdeAchat().getTaRReglementLiaisons().remove(ligne);
			if((doc instanceof TaDevis)&& ligne.getTaDevis()!=null)ligne.getTaDevis().getTaRReglementLiaisons().remove(ligne);
			if((doc instanceof TaFacture)&& ligne.getTaFacture()!=null)ligne.getTaFacture().getTaRReglementLiaisons().remove(ligne);
			if((doc instanceof TaPrelevement)&& ligne.getTaPrelevement()!=null)ligne.getTaPrelevement().getTaRReglementLiaisons().remove(ligne);
			if((doc instanceof TaPanier)&& ligne.getTaPanier()!=null)ligne.getTaPanier().getTaRReglementLiaisons().remove(ligne);
			if((doc instanceof TaProforma)&& ligne.getTaProforma()!=null)ligne.getTaProforma().getTaRReglementLiaisons().remove(ligne);
			if((doc instanceof TaTicketDeCaisse)&& ligne.getTaTicketDeCaisse()!=null)ligne.getTaTicketDeCaisse().getTaRReglementLiaisons().remove(ligne);	
			if((doc instanceof TaPreparation)&& ligne.getTaPreparation()!=null)ligne.getTaPreparation().getTaRReglementLiaisons().remove(ligne);
			if(ligne.getTaReglement()!=null)ligne.getTaReglement().getTaRReglementLiaisons().remove(ligne);
//			if(ligne.getTaFlash()!=null)ligne.getTaFlash().getTaRReglementLiaisons().remove(ligne);	
		}
		doc.getTaRReglementLiaisons().clear();
	}
	
	private void removeAllRDocuments(SWTDocument doc) {
		for (TaRDocument ligne : doc.getTaRDocuments()) {			
			if(!(doc instanceof TaBonliv)&& ligne.getTaBonliv()!=null)ligne.getTaBonliv().getTaRDocuments().remove(ligne);
			if(!(doc instanceof TaAcompte)&& ligne.getTaAcompte()!=null)ligne.getTaAcompte().getTaRDocuments().remove(ligne);
			if(!(doc instanceof TaAvoir)&& ligne.getTaAvoir()!=null)ligne.getTaAvoir().getTaRDocuments().remove(ligne);
			if(!(doc instanceof TaApporteur)&& ligne.getTaApporteur()!=null)ligne.getTaApporteur().getTaRDocuments().remove(ligne);
			if(!(doc instanceof TaAvisEcheance)&& ligne.getTaAvisEcheance()!=null)ligne.getTaAvisEcheance().getTaRDocuments().remove(ligne);
			if((doc instanceof TaAvoir)&& ligne.getTaAvoir()!=null)ligne.getTaAvoir().getTaRDocuments().remove(ligne);
			if(!(doc instanceof TaBoncde)&& ligne.getTaBoncde()!=null)ligne.getTaBoncde().getTaRDocuments().remove(ligne);
			if(!(doc instanceof TaBoncdeAchat)&& ligne.getTaBoncdeAchat()!=null)ligne.getTaBoncdeAchat().getTaRDocuments().remove(ligne);
			if(!(doc instanceof TaDevis)&& ligne.getTaDevis()!=null)ligne.getTaDevis().getTaRDocuments().remove(ligne);
			if(!(doc instanceof TaFacture)&& ligne.getTaFacture()!=null)ligne.getTaFacture().getTaRDocuments().remove(ligne);
			if(!(doc instanceof TaPrelevement)&& ligne.getTaPrelevement()!=null)ligne.getTaPrelevement().getTaRDocuments().remove(ligne);
			if(!(doc instanceof TaPanier)&& ligne.getTaPanier()!=null)ligne.getTaPanier().getTaRDocuments().remove(ligne);
			if(!(doc instanceof TaProforma)&& ligne.getTaProforma()!=null)ligne.getTaProforma().getTaRDocuments().remove(ligne);
			if(!(doc instanceof TaTicketDeCaisse)&& ligne.getTaTicketDeCaisse()!=null)ligne.getTaTicketDeCaisse().getTaRDocuments().remove(ligne);	
			if(!(doc instanceof TaPreparation)&& ligne.getTaPreparation()!=null)ligne.getTaPreparation().getTaRDocuments().remove(ligne);
			if(ligne.getTaFlash()!=null)ligne.getTaFlash().getTaRDocuments().remove(ligne);	
		}
		doc.getTaRDocuments().clear();
	}
	
	@PostLoad
	public void methodInvokedAfterLoaded(SWTDocument doc) {
//		recupSetRDocument(doc);
//		recupSetHistREtat(doc);
//		recupSetREtat(doc);
	}
	
}
