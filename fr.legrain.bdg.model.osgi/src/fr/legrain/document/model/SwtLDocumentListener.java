package fr.legrain.document.model;

import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

public class SwtLDocumentListener extends SWTDocumentListeEager {

	@PrePersist
	public void methodInvokedBeforePersist(SWTLigneDocument ligne) {
	}

	@PostPersist
	public void methodInvokedAfterPersist(SWTLigneDocument ligne) {
	}

	@PreUpdate
	public void methodInvokedBeforeUpdate(SWTLigneDocument ligne) {
	}

	@PostUpdate
	public void methodInvokedAfterUpdate(SWTLigneDocument ligne) {
	}

	@PreRemove
	private void methodInvokedBeforeRemove(SWTLigneDocument ligne) {
		removeAllLigneALigne(ligne);
	}

	@PostRemove
	public void methodInvokedAfterRemove(SWTLigneDocument ligne) {
	}
	

	
	private void removeAllLigneALigne(SWTLigneDocument ligne) {
		for (TaLigneALigne ll : ligne.getTaLigneALignes()) {			
			if(!(ligne instanceof TaLBonliv)&& ll.getTaLBonliv()!=null)ll.getTaLBonliv().getTaLigneALignes().remove(ll);
			if(!(ligne instanceof TaLAvoir)&& ll.getTaLAvoir()!=null)ll.getTaLAvoir().getTaLigneALignes().remove(ll);
			if(!(ligne instanceof TaLBonReception)&& ll.getTaLBonReception()!=null)ll.getTaLBonReception().getTaLigneALignes().remove(ll);
			if(!(ligne instanceof TaLAcompte)&& ll.getTaLAcompte()!=null)ll.getTaLAcompte().getTaLigneALignes().remove(ll);
			if(!(ligne instanceof TaLApporteur)&& ll.getTaLApporteur()!=null)ll.getTaLApporteur().getTaLigneALignes().remove(ll);
			if(!(ligne instanceof TaLAvisEcheance)&& ll.getTaLAvisEcheance()!=null)ll.getTaLAvisEcheance().getTaLigneALignes().remove(ll);
			if(!(ligne instanceof TaLBoncde)&& ll.getTaLBoncde()!=null)ll.getTaLBoncde().getTaLigneALignes().remove(ll);
			if(!(ligne instanceof TaLBoncdeAchat)&& ll.getTaLBoncdeAchat()!=null)ll.getTaLBoncdeAchat().getTaLigneALignes().remove(ll);
			if(!(ligne instanceof TaLDevis)&& ll.getTaLDevis()!=null)ll.getTaLDevis().getTaLigneALignes().remove(ll);
			if(!(ligne instanceof TaLFacture)&& ll.getTaLFacture()!=null)ll.getTaLFacture().getTaLigneALignes().remove(ll);
			if(!(ligne instanceof TaLPrelevement)&& ll.getTaLPrelevement()!=null)ll.getTaLPrelevement().getTaLigneALignes().remove(ll);
			if(!(ligne instanceof TaLProforma)&& ll.getTaLProforma()!=null)ll.getTaLProforma().getTaLigneALignes().remove(ll);
			if(!(ligne instanceof TaLPanier)&& ll.getTaLPanier()!=null)ll.getTaLPanier().getTaLigneALignes().remove(ll);
			if(!(ligne instanceof TaLTicketDeCaisse)&& ll.getTaLTicketDeCaisse()!=null)ll.getTaLTicketDeCaisse().getTaLigneALignes().remove(ll);
			if(!(ligne instanceof TaLPreparation)&&ll.getTaLPreparation()!=null)ll.getTaLPreparation().getTaLigneALignes().remove(ll);
			if(ll.getTaLFlash()!=null)ll.getTaLFlash().getTaLigneALignes().remove(ll);
		}
		ligne.getTaLigneALignes().clear();
	}
	
	
	@PostLoad
	public void methodInvokedAfterLoaded(SWTLigneDocument ldoc) {
//		recupSetLigneALigne(ldoc);
//		recupSetHistREtatLigneDocuments(ldoc);
//		recupSetREtatLigneDocuments(ldoc);
	}
}
