package fr.legrain.document.model;

import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;

public class SWTDocumentListeEager {

	
	public IDocumentTiers recupSetREtat(IDocumentTiers doc) {
		if(doc!=null) {
			if(doc.getTaREtat()!=null)doc.getTaREtat().getIdREtat();
			if(doc.getTaREtats()!=null) {
				for (TaREtat o : doc.getTaREtats()) {
					o.getIdREtat();
				}
			}
		}
		return doc;
	}
	
	public IDocumentTiers recupTiers(IDocumentTiers obj) {
		if(obj!=null) obj.getTaTiers().getIdTiers();
		return obj;
		
	}
	
	public ILigneDocumentTiers recupSetArticle(ILigneDocumentTiers obj) {
//		if(obj!=null) {	
//		for (ILigneDocumentTiers o : obj.getLignesGeneral()) {
			if(obj.getTaArticle()!=null)obj.getTaArticle().getIdArticle();
//		}
//		}
		return obj;
		
	}
	
	public IDocumentTiers recupSetHistREtat(IDocumentTiers doc) {
		if(doc!=null) {
		if(doc.getTaHistREtats()!=null) {
			for (TaHistREtat o : doc.getTaHistREtats()) {
				o.getIdHistREtat();
			}
		}
		}
		return doc;
	}
	
//	public TaLFlash recupSetLigneALigne(TaLFlash ldoc) {
//			for (TaLigneALigne ll : ldoc.getTaLigneALignes()) {
//				ll.getId();
//			}		
//		return ldoc;
//	}	
	
	protected ILigneDocumentTiers recupSetLigneALigne(ILigneDocumentTiers ldoc) {
//		if(doc!=null) {
//		for (ILigneDocumentTiers l : doc.getLignesGeneral()) {
			if(ldoc.getTaLigneALignes()!=null) {
				for (TaLigneALigne ll : ldoc.getTaLigneALignes()) {
					ll.getId();
					if(ll.getTaLAcompte()!=null)ll.getTaLAcompte().getTaDocument().getIdDocument();
					if(ll.getTaLApporteur()!=null)ll.getTaLApporteur().getTaDocument().getIdDocument();
					if(ll.getTaLAvisEcheance()!=null)ll.getTaLAvisEcheance().getTaDocument().getIdDocument();
					if(ll.getTaLAvoir()!=null)ll.getTaLAvoir().getTaDocument().getIdDocument();
					if(ll.getTaLBoncde()!=null)ll.getTaLBoncde().getTaDocument().getIdDocument();
					if(ll.getTaLBoncdeAchat()!=null)ll.getTaLBoncdeAchat().getTaDocument().getIdDocument();
					if(ll.getTaLBonliv()!=null)ll.getTaLBonliv().getTaDocument().getIdDocument();
					if(ll.getTaLBonReception()!=null)ll.getTaLBonReception().getTaDocument().getIdDocument();
					if(ll.getTaLDevis()!=null)ll.getTaLDevis().getTaDocument().getIdDocument();
					if(ll.getTaLFacture()!=null)ll.getTaLFacture().getTaDocument().getIdDocument();
					if(ll.getTaLFlash()!=null)ll.getTaLFlash().getTaFlash().getIdFlash();
					if(ll.getTaLPrelevement()!=null)ll.getTaLPrelevement().getTaDocument().getIdDocument();
					if(ll.getTaLPreparation()!=null)ll.getTaLPreparation().getTaDocument().getIdDocument();
					if(ll.getTaLProforma()!=null)ll.getTaLProforma().getTaDocument().getIdDocument();
					if(ll.getTaLPanier()!=null)ll.getTaLPanier().getTaDocument().getIdDocument();
					if(ll.getTaLTicketDeCaisse()!=null)ll.getTaLTicketDeCaisse().getTaDocument().getIdDocument();

					
				}
			}
//		}
		
		return ldoc;
	}
	
	protected ILigneDocumentTiers recupSetREtatLigneDocuments(ILigneDocumentTiers ldoc) {
//		if(doc!=null) {
//		for (ILigneDocumentTiers l : doc.getLignesGeneral()) {
			ldoc.getTaDocument().getIdDocument();
			if(ldoc.getTaREtatLigneDocument()!=null)ldoc.getTaREtatLigneDocument().getIdREtatLigneDoc();
			if(ldoc.getTaREtatLigneDocuments()!=null) {
				for (TaREtatLigneDocument ll : ldoc.getTaREtatLigneDocuments()) {
					ll.getIdREtatLigneDoc();
				}
//			}
		}
//		}
		return ldoc;
	}
	
	
//	protected TaLFlash recupSetREtatLigneDocuments(TaLFlash ldoc) {
////		if(doc!=null) {
////		for (TaLFlash l : doc.getLignes()) {
//			if(ldoc.getTaREtatLigneDocument()!=null)ldoc.getTaREtatLigneDocument().getIdREtatLigneDoc();
//			for (TaREtatLigneDocument ll : ldoc.getTaREtatLigneDocuments()) {
//				ll.getIdREtatLigneDoc();
//			}
////		}
////		}
//		return ldoc;
//	}
	
	
	protected ILigneDocumentTiers recupSetHistREtatLigneDocuments(ILigneDocumentTiers ldoc) {
//		if(doc!=null) {
//		for (ILigneDocumentTiers l : doc.getLignesGeneral()) {
			if(ldoc.getTaHistREtatLigneDocuments()!=null) {
				for (TaHistREtatLigneDocument ll : ldoc.getTaHistREtatLigneDocuments()) {
					ll.getIdHistREtatLigneDoc();
				}
			}
//		}
//		}
		return ldoc;
	}
	
	
//	protected TaLFlash recupSetHistREtatLigneDocuments(TaLFlash ldoc) {
////		if(doc!=null) {
////		for (TaLFlash l : doc.getLignes()) {
//			for (TaHistREtatLigneDocument ll : ldoc.getTaHistREtatLigneDocuments()) {
//				ll.getIdHistREtatLigneDoc();
//			}
////		}
////		}
//		return ldoc;
//	}
	
	protected IDocumentTiers recupSetRDocument(IDocumentTiers doc) {
		if(doc!=null) {
		if(doc.getTaRDocuments()!=null) {
			for (TaRDocument l : doc.getTaRDocuments()) {
				l.getIdRDocument();
			}
		}
		}
		return doc;
	}

}
