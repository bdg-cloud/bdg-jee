package fr.legrain.importation.service;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.transaction.UserTransaction;

import fr.legrain.bdg.documents.service.remote.ITaAcompteServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaApporteurServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaAvisEcheanceServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaAvoirServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaBoncdeAchatServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaBoncdeServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaBonlivServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaDevisServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaFactureServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLigneALigneServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaPrelevementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaPreparationServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaProformaServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaTicketDeCaisseServiceRemote;
import fr.legrain.bdg.importation.service.remote.ITransactionnalMergeEtatDocumentService;
import fr.legrain.document.dto.IDocumentDTO;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.dto.TaAcompteDTO;
import fr.legrain.document.dto.TaApporteurDTO;
import fr.legrain.document.dto.TaAvisEcheanceDTO;
import fr.legrain.document.dto.TaAvoirDTO;
import fr.legrain.document.dto.TaBoncdeAchatDTO;
import fr.legrain.document.dto.TaBoncdeDTO;
import fr.legrain.document.dto.TaBonlivDTO;
import fr.legrain.document.dto.TaDevisDTO;
import fr.legrain.document.dto.TaFactureDTO;
import fr.legrain.document.dto.TaPrelevementDTO;
import fr.legrain.document.dto.TaPreparationDTO;
import fr.legrain.document.dto.TaProformaDTO;
import fr.legrain.document.dto.TaTicketDeCaisseDTO;
import fr.legrain.document.model.TaAcompte;
import fr.legrain.document.model.TaApporteur;
import fr.legrain.document.model.TaAvisEcheance;
import fr.legrain.document.model.TaAvoir;
import fr.legrain.document.model.TaBoncde;
import fr.legrain.document.model.TaBoncdeAchat;
import fr.legrain.document.model.TaBonliv;
import fr.legrain.document.model.TaDevis;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaLAcompte;
import fr.legrain.document.model.TaLApporteur;
import fr.legrain.document.model.TaLAvisEcheance;
import fr.legrain.document.model.TaLAvoir;
import fr.legrain.document.model.TaLBoncde;
import fr.legrain.document.model.TaLBoncdeAchat;
import fr.legrain.document.model.TaLBonliv;
import fr.legrain.document.model.TaLDevis;
import fr.legrain.document.model.TaLFacture;
import fr.legrain.document.model.TaLPrelevement;
import fr.legrain.document.model.TaLPreparation;
import fr.legrain.document.model.TaLProforma;
import fr.legrain.document.model.TaLTicketDeCaisse;
import fr.legrain.document.model.TaLigneALigne;
import fr.legrain.document.model.TaPrelevement;
import fr.legrain.document.model.TaPreparation;
import fr.legrain.document.model.TaProforma;
import fr.legrain.document.model.TaRDocument;
import fr.legrain.document.model.TaTicketDeCaisse;
import fr.legrain.documents.dao.IEtatDAO;
import fr.legrain.documents.dao.IFactureDAO;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
@TransactionManagement(TransactionManagementType.BEAN)
public class TransactionnalMergeEtatDocumentService implements ITransactionnalMergeEtatDocumentService{
	
	@EJB private ITaBonlivServiceRemote taBonlivService;
	@EJB private ITaBoncdeAchatServiceRemote taBoncdeAchatService;
	@EJB private ITaBoncdeServiceRemote taBoncdeService;
	@EJB private ITaDevisServiceRemote taDevisService;
	@EJB private ITaFactureServiceRemote taFactureService;
	@EJB private ITaApporteurServiceRemote taApporteurService;
	@EJB private ITaAvoirServiceRemote taAvoirService;
	@EJB private ITaAvisEcheanceServiceRemote taAvisEcheanceService;
	@EJB private ITaProformaServiceRemote taProformaService;
	@EJB private ITaPrelevementServiceRemote taPrelevementService;
	@EJB private ITaTicketDeCaisseServiceRemote taTicketDeCaisseService;
	@EJB private ITaAcompteServiceRemote taAcompteService;
	@EJB private ITaPreparationServiceRemote taPreparationService;
	
	@EJB private ITaLigneALigneServiceRemote taLigneALigneService;
	@Inject private IEtatDAO daoEtat;
	
	@Resource private UserTransaction tx; 

	


	
	@PostConstruct
	public void postConstruct(){

	}
	
	public void mergeEtatDocument(IDocumentTiers doc) throws Exception{
		 tx.begin();

   	 
			if(doc instanceof TaBonliv) {
				TaBonliv d=taBonlivService.findByIDFetch(doc.getIdDocument());
				d = taBonlivService.mergeEtat(d);
			}
			if(doc instanceof TaAcompte) {
		 		TaAcompte d=taAcompteService.findByIDFetch(doc.getIdDocument());
		    d = taAcompteService.mergeEtat(d);      
			}
			if(doc instanceof TaAvoir) { 
		 		TaAvoir d=taAvoirService.findByIDFetch(doc.getIdDocument());
		    d = taAvoirService.mergeEtat(d);
			}
			if(doc instanceof TaApporteur) {                  
		 		TaApporteur d=taApporteurService.findByIDFetch(doc.getIdDocument());
		    d = taApporteurService.mergeEtat(d);
			}
			if(doc instanceof TaAvisEcheance) {               
		 		TaAvisEcheance d=taAvisEcheanceService.findByIDFetch(doc.getIdDocument());
		    d = taAvisEcheanceService.mergeEtat(d);
			}
			if(doc instanceof TaBoncde) {                     
		 		TaBoncde d=taBoncdeService.findByIDFetch(doc.getIdDocument());
		    d = taBoncdeService.mergeEtat(d);
			}
			if(doc instanceof TaBoncdeAchat) {                
		 		TaBoncdeAchat d=taBoncdeAchatService.findByIDFetch(doc.getIdDocument());
		    d = taBoncdeAchatService.mergeEtat(d);
			}
			if(doc instanceof TaDevis) {                      
		 		TaDevis d=taDevisService.findByIDFetch(doc.getIdDocument());
		    d = taDevisService.mergeEtat(d);
			}
			if(doc instanceof TaFacture) {                    
		 		TaFacture d=taFactureService.findByIDFetch(doc.getIdDocument());
		    d = taFactureService.mergeEtat(d);
			}
			if(doc instanceof TaPrelevement) {                
		 		TaPrelevement d=taPrelevementService.findByIDFetch(doc.getIdDocument());
		    d = taPrelevementService.mergeEtat(d);
			}
			if(doc instanceof TaProforma) {                   
		 		TaProforma d=taProformaService.findByIDFetch(doc.getIdDocument());
		    d = taProformaService.mergeEtat(d);
			}
			if(doc instanceof TaTicketDeCaisse) {             
		 		TaTicketDeCaisse d=taTicketDeCaisseService.findByIDFetch(doc.getIdDocument());
		    d = taTicketDeCaisseService.mergeEtat(d);
			}
			if(doc instanceof TaPreparation) {                
		 		TaPreparation d=taPreparationService.findByIDFetch(doc.getIdDocument());
		    d = taPreparationService.mergeEtat(d);
			}
			
   	 tx.commit();   	 
	}
	
	public List<IDocumentDTO> recupListeLigneALigne()throws Exception{
		List<IDocumentDTO> ll;
		tx.begin();
		ll=  taLigneALigneService.recupIdDocumentSrc();
		tx.commit();
		return ll;
	}
	
	public void mergeEtatDocument(IDocumentDTO doc) throws Exception{
		 tx.begin();

    	 
			if(doc instanceof TaBonlivDTO) {
				TaBonliv d=taBonlivService.findByIDFetch(doc.getId());
				d = taBonlivService.mergeEtat(d);
			}
			if(doc instanceof TaAcompteDTO) {
		 		TaAcompte d=taAcompteService.findByIDFetch(doc.getId());
		    d = taAcompteService.mergeEtat(d);      
			}
			if(doc instanceof TaAvoirDTO) { 
		 		TaAvoir d=taAvoirService.findByIDFetch(doc.getId());
		    d = taAvoirService.mergeEtat(d);
			}
			if(doc instanceof TaApporteurDTO) {                  
		 		TaApporteur d=taApporteurService.findByIDFetch(doc.getId());
		    d = taApporteurService.mergeEtat(d);
			}
			if(doc instanceof TaAvisEcheanceDTO) {               
		 		TaAvisEcheance d=taAvisEcheanceService.findByIDFetch(doc.getId());
		    d = taAvisEcheanceService.mergeEtat(d);
			}
			if(doc instanceof TaBoncdeDTO) {                     
		 		TaBoncde d=taBoncdeService.findByIDFetch(doc.getId());
		    d = taBoncdeService.mergeEtat(d);
			}
			if(doc instanceof TaBoncdeAchatDTO) {                
		 		TaBoncdeAchat d=taBoncdeAchatService.findByIDFetch(doc.getId());
		    d = taBoncdeAchatService.mergeEtat(d);
			}
			if(doc instanceof TaDevisDTO) {                      
		 		TaDevis d=taDevisService.findByIDFetch(doc.getId());
		    d = taDevisService.mergeEtat(d);
			}
			if(doc instanceof TaFactureDTO) {                    
		 		TaFacture d=taFactureService.findByIDFetch(doc.getId());
		    d = taFactureService.mergeEtat(d);
			}
			if(doc instanceof TaPrelevementDTO) {                
		 		TaPrelevement d=taPrelevementService.findByIDFetch(doc.getId());
		    d = taPrelevementService.mergeEtat(d);
			}
			if(doc instanceof TaProformaDTO) {                   
		 		TaProforma d=taProformaService.findByIDFetch(doc.getId());
		    d = taProformaService.mergeEtat(d);
			}
			if(doc instanceof TaTicketDeCaisseDTO) {             
		 		TaTicketDeCaisse d=taTicketDeCaisseService.findByIDFetch(doc.getId());
		    d = taTicketDeCaisseService.mergeEtat(d);
			}
			if(doc instanceof TaPreparationDTO) {                
		 		TaPreparation d=taPreparationService.findByIDFetch(doc.getId());
		    d = taPreparationService.mergeEtat(d);
			}
			
    	 tx.commit();   	 
	}
	
	
	public IDocumentTiers mergeEtatDocument(IDocumentTiers doc, boolean avecCommit) throws Exception{
		if(avecCommit) tx.begin();
			if(doc instanceof TaBonliv)doc = taBonlivService.mergeEtat((TaBonliv) doc);
			if(doc instanceof TaAcompte)doc = taAcompteService.mergeEtat((TaAcompte) doc);
			if(doc instanceof TaAvoir)doc = taAvoirService.mergeEtat((TaAvoir) doc);
			if(doc instanceof TaApporteur)doc = taApporteurService.mergeEtat((TaApporteur) doc);
			if(doc instanceof TaAvisEcheance)doc = taAvisEcheanceService.mergeEtat((TaAvisEcheance) doc);
			if(doc instanceof TaBoncde)doc = taBoncdeService.mergeEtat((TaBoncde) doc);
			if(doc instanceof TaBoncdeAchat)doc = taBoncdeAchatService.mergeEtat((TaBoncdeAchat) doc);
			if(doc instanceof TaDevis)doc = taDevisService.mergeEtat((TaDevis) doc);
			if(doc instanceof TaFacture)doc = taFactureService.mergeEtat((TaFacture) doc);
			if(doc instanceof TaPrelevement)doc = taPrelevementService.mergeEtat((TaPrelevement) doc);
			if(doc instanceof TaProforma)doc = taProformaService.mergeEtat((TaProforma) doc);
			if(doc instanceof TaTicketDeCaisse)doc = taTicketDeCaisseService.mergeEtat((TaTicketDeCaisse) doc);
			if(doc instanceof TaPreparation)doc = taPreparationService.mergeEtat((TaPreparation) doc);
			if(avecCommit) tx.commit();   	 
    	 return doc;
	}	
	
	
	
//	public List<ILigneDocumentTiers> genereLigneALigne(ILigneDocumentTiers ligneSrc,IDocumentTiers dest,List<ILigneDocumentTiers> listeTraite) {
////		List<ILigneDocumentTiers> listeTraite = new LinkedList<>();
//				ILigneDocumentTiers l=rechercheLigneCorrespondanteComplete(ligneSrc,dest, listeTraite);
//				if(l!=null && !listeTraite.contains(l)){
//					TaLigneALigne ll = new TaLigneALigne();
//					ll.setIdLigneSrc(ligneSrc.getIdLDocument());
//					if(ligneSrc.getTaLot()!=null)ll.setNumLot(ligneSrc.getTaLot().getNumLot());
//					ll.setQte(ligneSrc.getQteLDocument());
//					ll.setQteRecue(l.getQteLDocument());
//					ll.setPrixU(ligneSrc.getPrixULDocument());
//					ll.setUnite(ligneSrc.getU1LDocument());
//					ll.setUniteRecue(ligneSrc.getU1LDocument());
//					if(ligneSrc.getTaDocument() instanceof TaBonliv)ll.setTaLBonliv((TaLBonliv) ligneSrc);
//					if(ligneSrc.getTaDocument() instanceof TaAcompte)ll.setTaLAcompte((TaLAcompte) ligneSrc);
//					if(ligneSrc.getTaDocument() instanceof TaAvoir)ll.setTaLAvoir((TaLAvoir) ligneSrc);
//					if(ligneSrc.getTaDocument() instanceof TaApporteur)ll.setTaLApporteur((TaLApporteur) ligneSrc);
//					if(ligneSrc.getTaDocument() instanceof TaAvisEcheance)ll.setTaLAvisEcheance((TaLAvisEcheance) ligneSrc);
//					if(ligneSrc.getTaDocument() instanceof TaBoncde)ll.setTaLBoncde((TaLBoncde) ligneSrc);
//					if(ligneSrc.getTaDocument() instanceof TaBoncdeAchat)ll.setTaLBoncdeAchat((TaLBoncdeAchat) ligneSrc);
//					if(ligneSrc.getTaDocument() instanceof TaDevis)ll.setTaLDevis((TaLDevis) ligneSrc);
//					if(ligneSrc.getTaDocument() instanceof TaFacture)ll.setTaLFacture((TaLFacture) ligneSrc);
//					if(ligneSrc.getTaDocument() instanceof TaPrelevement)ll.setTaLPrelevement((TaLPrelevement) ligneSrc);
//					if(ligneSrc.getTaDocument() instanceof TaProforma)ll.setTaLProforma((TaLProforma) ligneSrc);
//					if(ligneSrc.getTaDocument() instanceof TaTicketDeCaisse)ll.setTaLTicketDeCaisse((TaLTicketDeCaisse) ligneSrc);
//					if(ligneSrc.getTaDocument() instanceof TaPreparation)ll.setTaLPreparation((TaLPreparation) ligneSrc);
//					
//					if(l.getTaDocument() instanceof TaBonliv)ll.setTaLBonliv((TaLBonliv) l);
//					if(l.getTaDocument() instanceof TaAcompte)ll.setTaLAcompte((TaLAcompte) l);
//					if(l.getTaDocument() instanceof TaAvoir)ll.setTaLAvoir((TaLAvoir) l);
//					if(l.getTaDocument() instanceof TaApporteur)ll.setTaLApporteur((TaLApporteur) l);
//					if(l.getTaDocument() instanceof TaAvisEcheance)ll.setTaLAvisEcheance((TaLAvisEcheance) l);
//					if(l.getTaDocument() instanceof TaBoncde)ll.setTaLBoncde((TaLBoncde) l);
//					if(l.getTaDocument() instanceof TaBoncdeAchat)ll.setTaLBoncdeAchat((TaLBoncdeAchat) l);
//					if(l.getTaDocument() instanceof TaDevis)ll.setTaLDevis((TaLDevis) l);
//					if(l.getTaDocument() instanceof TaFacture)ll.setTaLFacture((TaLFacture) l);
//					if(l.getTaDocument() instanceof TaPrelevement)ll.setTaLPrelevement((TaLPrelevement) l);
//					if(l.getTaDocument() instanceof TaProforma)ll.setTaLProforma((TaLProforma) l);
//					if(l.getTaDocument() instanceof TaTicketDeCaisse)ll.setTaLTicketDeCaisse((TaLTicketDeCaisse) l);
//					if(l.getTaDocument() instanceof TaPreparation)ll.setTaLPreparation((TaLPreparation) l);
//					ll=taLigneALigneService.merge(ll);
//					ligneSrc.getTaLigneALignes().add(ll);
//					l.getTaLigneALignes().add(ll);
//					listeTraite.add(l);
//					return listeTraite;
//				}
//		
//		return listeTraite;
//	};
//
//	
//	public ILigneDocumentTiers rechercheLigneCorrespondanteComplete(ILigneDocumentTiers ligneSrc,IDocumentTiers dest,List<ILigneDocumentTiers> listeTraite) {
//		for (ILigneDocumentTiers l :dest.getLignesGeneral() ) {
//			if(l.getTaArticle()!=null && !listeTraite.contains(l)) {
//				boolean trouveReduit=l.getTaArticle().equalsCodeArticleAndUnite1(ligneSrc.getTaArticle());
//				if(trouveReduit) {
//					if (l.getQteLDocument() == null ) {
//						if(ligneSrc.getQteLDocument()==null)return l; 
//					} else if(ligneSrc.getQteLDocument()!=null && l.getQteLDocument().compareTo(ligneSrc.getQteLDocument())==0)
//						return l;
//				}			
//			}			
//		}
//		return rechercheLigneCorrespondante(ligneSrc,dest, listeTraite);
//	}
//	
//	public ILigneDocumentTiers rechercheLigneCorrespondante(ILigneDocumentTiers ligneSrc,IDocumentTiers dest,List<ILigneDocumentTiers> listeTraite) {
//		for (ILigneDocumentTiers l :dest.getLignesGeneral() ) {
//			if(l.getTaArticle()!=null && !listeTraite.contains(l)) {
//				if(l.getTaArticle().equalsCodeArticleAndUnite1(ligneSrc.getTaArticle()))
//					return l;				
//			}
//		}
//		return null;
//	}

//	public void updateLigneALigne_etatDocument(TaRDocument o)  throws Exception{
//		TaLigneALigne ll = new TaLigneALigne();
//		IDocumentTiers src = null;
//		IDocumentTiers dest = null;
//		try {
//
//			 tx.begin();
//			if(o.getTaPreparation()==null && o.getTaBoncdeAchat()==null && o.getTaPrelevement()==null && o.getIdSrc()!=null) {
//				if(o.getTaBonliv()!=null)						
//					if(o.getTaBonliv().getIdDocument()==o.getIdSrc())src=taBonlivService.findByIDFetch(o.getTaBonliv().getIdDocument());
//					else dest=taBonlivService.findByIDFetch(o.getTaBonliv().getIdDocument());
//				if(o.getTaAcompte()!=null)
//					if(o.getTaAcompte().getIdDocument()==o.getIdSrc())src=taAcompteService.findByIDFetch(o.getTaBonliv().getIdDocument());
//					else dest=taAcompteService.findByIDFetch(o.getTaBonliv().getIdDocument());
//				if(o.getTaAvoir()!=null)
//					if(o.getTaAvoir().getIdDocument()==o.getIdSrc())src=taAvoirService.findByIDFetch(o.getTaAvoir().getIdDocument());
//					else dest=taAvoirService.findByIDFetch(o.getTaAvoir().getIdDocument());
//				if(o.getTaApporteur()!=null)
//					if(o.getTaApporteur().getIdDocument()==o.getIdSrc())src=taApporteurService.findByIDFetch(o.getTaApporteur().getIdDocument());
//					else dest=taApporteurService.findByIDFetch(o.getTaApporteur().getIdDocument());
//				if(o.getTaAvisEcheance()!=null)
//					if(o.getTaAvisEcheance().getIdDocument()==o.getIdSrc())src=taAvisEcheanceService.findByIDFetch(o.getTaAvisEcheance().getIdDocument());
//					else dest=taAvisEcheanceService.findByIDFetch(o.getTaAvisEcheance().getIdDocument());
//				if(o.getTaBoncde()!=null)
//					if(o.getTaBoncde().getIdDocument()==o.getIdSrc())src=taBoncdeService.findByIDFetch(o.getTaBoncde().getIdDocument());
//					else dest=taBoncdeService.findByIDFetch(o.getTaBoncde().getIdDocument());
//				if(o.getTaDevis()!=null)
//					if(o.getTaDevis().getIdDocument()==o.getIdSrc())src=taDevisService.findByIDFetch(o.getTaDevis().getIdDocument());
//					else dest=taDevisService.findByIDFetch(o.getTaDevis().getIdDocument());
//				if(o.getTaFacture()!=null)
//					if(o.getTaFacture().getIdDocument()==o.getIdSrc())src=taFactureService.findByIDFetch(o.getTaFacture().getIdDocument());
//					else dest=taFactureService.findByIDFetch(o.getTaFacture().getIdDocument());
//				if(o.getTaPrelevement()!=null)
//					if(o.getTaPrelevement().getIdDocument()==o.getIdSrc())src=taPrelevementService.findByIDFetch(o.getTaPrelevement().getIdDocument());
//					else dest=taPrelevementService.findByIDFetch(o.getTaPrelevement().getIdDocument());
//				if(o.getTaProforma()!=null)
//					if(o.getTaProforma().getIdDocument()==o.getIdSrc())src=taProformaService.findByIDFetch(o.getTaProforma().getIdDocument());
//					else dest=taProformaService.findByIDFetch(o.getTaProforma().getIdDocument());
//				if(o.getTaTicketDeCaisse()!=null)
//					if(o.getTaTicketDeCaisse().getIdDocument()==o.getIdSrc())src=taPreparationService.findByIDFetch(o.getTaTicketDeCaisse().getIdDocument());
//					else dest=taPreparationService.findByIDFetch(o.getTaTicketDeCaisse().getIdDocument());
//			}			//quand on a le source et le destinataire on peut essayer de créer les liaisons ligneALigne
//			//pour chaque ligne src je regarde si article et unité correspondent dans dest
//			List<ILigneDocumentTiers> lDestTraite=new LinkedList<>();
//			if(src!=null && dest !=null) {
//				for (ILigneDocumentTiers ligne : src.getLignesGeneral()) {
//					lDestTraite=genereLigneALigne(ligne,dest,lDestTraite);
//				} 
//			}
//
//			src=mergeEtatDocument(src,false);
//
//
//
//			LinkedList<IDocumentTiers> lien=new LinkedList<>();
//			lien.add(src);
//			dest=mergeEtatDocument(dest,false);
//
//			 tx.commit();
//		} catch (Exception e) {
//			System.out.println("erreur : " + e);
//		}
//	}

}
