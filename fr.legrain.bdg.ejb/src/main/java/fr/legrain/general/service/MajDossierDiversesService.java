package fr.legrain.general.service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.TransactionTimeout;

import fr.legrain.bdg.documents.service.remote.ITaFactureServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLigneALigneServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaRDocumentServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.bdg.importation.service.remote.ITransactionnalMergeEtatDocumentService;
import fr.legrain.bdg.tiers.service.remote.ITaEtatServiceRemote;
import fr.legrain.document.dto.IDocumentDTO;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.dto.TaFactureDTO;
import fr.legrain.document.model.RetourMajDossier;
import fr.legrain.document.model.TaAcompte;
import fr.legrain.document.model.TaApporteur;
import fr.legrain.document.model.TaAvisEcheance;
import fr.legrain.document.model.TaAvoir;
import fr.legrain.document.model.TaBoncde;
import fr.legrain.document.model.TaBoncdeAchat;
import fr.legrain.document.model.TaBonliv;
import fr.legrain.document.model.TaDevis;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaFlash;
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
import fr.legrain.document.model.TaREtat;
import fr.legrain.document.model.TaREtatLigneDocument;
import fr.legrain.document.model.TaTicketDeCaisse;
import fr.legrain.documents.dao.IEtatDAO;
import fr.legrain.documents.dao.IFactureDAO;
import fr.legrain.documents.dao.IREtatDAO;
import fr.legrain.documents.dao.IREtatLigneDAO;
import fr.legrain.droits.service.SessionInfo;
import fr.legrain.general.service.remote.IMajDossierDiversesServiceRemote;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;

/**
 * Session Bean implementation class TaFactureBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class MajDossierDiversesService implements IMajDossierDiversesServiceRemote {

	static Logger logger = Logger.getLogger(MajDossierDiversesService.class);
	

	@EJB private ITaRDocumentServiceRemote taRDocumentService;
	@EJB private ITaLigneALigneServiceRemote taLigneALigneService;
	@EJB private ITaFactureServiceRemote taFactureService;
	
	
	

	@EJB	private ITaEtatServiceRemote taEtatService;
	
	@Inject private IREtatDAO daoREtat;
	@Inject private IREtatLigneDAO daoREtatLigne;
	@Inject private IEtatDAO daoEtat;
	@Inject private IFactureDAO daoFacture;
	
	@EJB private ITransactionnalMergeEtatDocumentService transactionnalMergeEtatDocumentService;
	@EJB private ITaPreferencesServiceRemote PreferencesService;
	@Inject private	SessionInfo sessionInfo;
	
	List<IDocumentTiers> listeDocumentSrc=new LinkedList<>();
			
	/**
	 * Default constructor. 
	 */
	public MajDossierDiversesService() {

	}
	
	

	

	@TransactionTimeout(value=600,unit=TimeUnit.MINUTES)
	public RetourMajDossier testDivers()  {
		RetourMajDossier retour=new RetourMajDossier(true);
		TaFlash o = new TaFlash();
		try {
	
			
//		TaEtat etat = taFlashService.etatLigneInsertion(o);
//			
//			
//		o.setCodeFlash(taFlashService.genereCode(null));
//		o.setLibelleFlash("ddd");
//		o.setDateFlash(new Date());
//		o.setDateTransfert(new Date());
//		o.setNumeroCommandeFournisseur("numcommandeF");
//		o.setIdUtilisateur(2);
//		o.setCodeTDoc(TaPreparation.TYPE_DOC);
////		o.setTaUtilisateur(taUtilisateurService.findById(2));
////		o.setTaTDoc(taTDocService.findByCode(TaBoncde.TYPE_DOC));
//		
//		TaLFlash lf=new TaLFlash();
//		lf.setLibLFlash("Aloyau Entier Brut de Boeuf frais (réceptionné)");
//		lf.setCodeBarreLu("codeBarre");
//		lf.setCodeBarre(lf.getCodeBarreLu());
//		lf.setEmplacementLFlash("emplacement");
//		lf.setQteLFlash(BigDecimal.valueOf(10));
//
//		lf.setU1LFlash(TaUniteService.findByCode("Kilo").getCodeUnite());
//		lf.setCodeArticle("BOE-ALOYBR");
////		lf.setTaArticle(taArticleService.findByCode("BDG_ASSO_LGR300"));
////		lf.setCodeEntrepot("LOCAL1");
////		lf.setTaEntrepot(taEntrepotService.findByCode("LOCAL1"));
//		lf.setNumLot("BR20/02/17-01-CBON-01");
////		lf.setTaLot(taLotService.findOrCreateLotVirtuelArticle(lf.getTaArticle().getIdArticle()));
//		lf.setTaFlash(o);
//		lf.addREtatLigneDoc(etat);
//		
//		o.getLignes().add(lf);
//		
//		lf=new TaLFlash();
//		
//		lf.addREtatLigneDoc(etat);
//		
//		lf.setLibLFlash("Aloyau de Boeuf frais net (sans bavette et filet)");
//		lf.setCodeBarreLu("codeBarre");
//		lf.setCodeBarre(lf.getCodeBarreLu());
//		lf.setEmplacementLFlash("emplacement");
//		lf.setQteLFlash(BigDecimal.valueOf(1));
//
//			lf.setU1LFlash(TaUniteService.findByCode("Kilo").getCodeUnite());
//
//			lf.setCodeArticle("BOE-ALOYNE");
////			lf.setTaArticle(taArticleService.findByCode("BDG_ASSO_LGR300"));
////			lf.setCodeEntrepot("LOCAL1");
////			lf.setTaEntrepot(taEntrepotService.findByCode("LOCAL1"));
//			lf.setNumLot("BR20/02/17-01-CBON-02");
//			
//		lf.setTaFlash(o);
//
//		
//		o.getLignes().add(lf);
//		
//		taFlashService.merge(o);
//		
////		taFlashService.remove(taFlashService.findById(9));

		
		} catch (Exception e) {
			System.out.println("erreur : " + e);
		}
		return retour;
	}
	
	public ILigneDocumentTiers rechercheLigneCorrespondanteComplete(ILigneDocumentTiers ligneSrc,IDocumentTiers dest,List<ILigneDocumentTiers> listeTraite) {
		for (ILigneDocumentTiers l :dest.getLignesGeneral() ) {
			if(l.getTaArticle()!=null && !listeTraite.contains(l)) {
				boolean trouveReduit=l.getTaArticle().equalsCodeArticleAndUnite1(ligneSrc.getTaArticle());
				if(trouveReduit) {
					if (l.getQteLDocument() == null ) {
						if(ligneSrc.getQteLDocument()==null)return l; 
					} else if(ligneSrc.getQteLDocument()!=null && l.getQteLDocument().compareTo(ligneSrc.getQteLDocument())==0)
						return l;
				}			
			}			
		}
		return rechercheLigneCorrespondante(ligneSrc,dest, listeTraite);
	}
	
	public ILigneDocumentTiers rechercheLigneCorrespondante(ILigneDocumentTiers ligneSrc,IDocumentTiers dest,List<ILigneDocumentTiers> listeTraite) {
		for (ILigneDocumentTiers l :dest.getLignesGeneral() ) {
			if(l.getTaArticle()!=null && !listeTraite.contains(l)) {
				if(l.getTaArticle().equalsCodeArticleAndUnite1(ligneSrc.getTaArticle()))
					return l;				
			}
		}
		return null;
	}
	
	
	public List<ILigneDocumentTiers> genereLigneALigne(ILigneDocumentTiers ligneSrc,IDocumentTiers dest,List<ILigneDocumentTiers> listeTraite) {
//		List<ILigneDocumentTiers> listeTraite = new LinkedList<>();
				ILigneDocumentTiers l=rechercheLigneCorrespondanteComplete(ligneSrc,dest, listeTraite);
				if(l!=null && !listeTraite.contains(l)){
					TaLigneALigne ll = new TaLigneALigne();
					ll.setIdLigneSrc(ligneSrc.getIdLDocument());
					if(ligneSrc.getTaLot()!=null)ll.setNumLot(ligneSrc.getTaLot().getNumLot());
					ll.setQte(ligneSrc.getQteLDocument());
					ll.setQteRecue(l.getQteLDocument());
					ll.setPrixU(ligneSrc.getPrixULDocument());
					ll.setUnite(ligneSrc.getU1LDocument());
					ll.setUniteRecue(ligneSrc.getU1LDocument());
					if(ligneSrc.getTaDocument() instanceof TaBonliv)ll.setTaLBonliv((TaLBonliv) ligneSrc);
					if(ligneSrc.getTaDocument() instanceof TaAcompte)ll.setTaLAcompte((TaLAcompte) ligneSrc);
					if(ligneSrc.getTaDocument() instanceof TaAvoir)ll.setTaLAvoir((TaLAvoir) ligneSrc);
					if(ligneSrc.getTaDocument() instanceof TaApporteur)ll.setTaLApporteur((TaLApporteur) ligneSrc);
					if(ligneSrc.getTaDocument() instanceof TaAvisEcheance)ll.setTaLAvisEcheance((TaLAvisEcheance) ligneSrc);
					if(ligneSrc.getTaDocument() instanceof TaBoncde)ll.setTaLBoncde((TaLBoncde) ligneSrc);
					if(ligneSrc.getTaDocument() instanceof TaBoncdeAchat)ll.setTaLBoncdeAchat((TaLBoncdeAchat) ligneSrc);
					if(ligneSrc.getTaDocument() instanceof TaDevis)ll.setTaLDevis((TaLDevis) ligneSrc);
					if(ligneSrc.getTaDocument() instanceof TaFacture)ll.setTaLFacture((TaLFacture) ligneSrc);
					if(ligneSrc.getTaDocument() instanceof TaPrelevement)ll.setTaLPrelevement((TaLPrelevement) ligneSrc);
					if(ligneSrc.getTaDocument() instanceof TaProforma)ll.setTaLProforma((TaLProforma) ligneSrc);
					if(ligneSrc.getTaDocument() instanceof TaTicketDeCaisse)ll.setTaLTicketDeCaisse((TaLTicketDeCaisse) ligneSrc);
					if(ligneSrc.getTaDocument() instanceof TaPreparation)ll.setTaLPreparation((TaLPreparation) ligneSrc);
					
					if(l.getTaDocument() instanceof TaBonliv)ll.setTaLBonliv((TaLBonliv) l);
					if(l.getTaDocument() instanceof TaAcompte)ll.setTaLAcompte((TaLAcompte) l);
					if(l.getTaDocument() instanceof TaAvoir)ll.setTaLAvoir((TaLAvoir) l);
					if(l.getTaDocument() instanceof TaApporteur)ll.setTaLApporteur((TaLApporteur) l);
					if(l.getTaDocument() instanceof TaAvisEcheance)ll.setTaLAvisEcheance((TaLAvisEcheance) l);
					if(l.getTaDocument() instanceof TaBoncde)ll.setTaLBoncde((TaLBoncde) l);
					if(l.getTaDocument() instanceof TaBoncdeAchat)ll.setTaLBoncdeAchat((TaLBoncdeAchat) l);
					if(l.getTaDocument() instanceof TaDevis)ll.setTaLDevis((TaLDevis) l);
					if(l.getTaDocument() instanceof TaFacture)ll.setTaLFacture((TaLFacture) l);
					if(l.getTaDocument() instanceof TaPrelevement)ll.setTaLPrelevement((TaLPrelevement) l);
					if(l.getTaDocument() instanceof TaProforma)ll.setTaLProforma((TaLProforma) l);
					if(l.getTaDocument() instanceof TaTicketDeCaisse)ll.setTaLTicketDeCaisse((TaLTicketDeCaisse) l);
					if(l.getTaDocument() instanceof TaPreparation)ll.setTaLPreparation((TaLPreparation) l);
					ll=taLigneALigneService.merge(ll);
					ligneSrc.getTaLigneALignes().add(ll);
					l.getTaLigneALignes().add(ll);
					listeTraite.add(l);
					return listeTraite;
				}
		
		return listeTraite;
	};
	
	@TransactionTimeout(value=600,unit=TimeUnit.MINUTES)
	public RetourMajDossier majLigneALigne()  {
		RetourMajDossier retour=new RetourMajDossier(true);
		TaLigneALigne ll = new TaLigneALigne();
		IDocumentTiers src = null;
		IDocumentTiers dest = null;
		int rang=0;
		try {
			System.out.println("Debut : ");
			List<TaRDocument> l= taRDocumentService.selectAll();
			retour.setNbDepart(l.size());
			for (TaRDocument o : l) {
				dest=null;
				src=null;
				if(o.getTaPreparation()==null && o.getTaBoncdeAchat()==null && o.getTaPrelevement()==null && o.getIdSrc()!=null) {
					if(o.getTaBonliv()!=null)						
						if(o.getTaBonliv().getIdDocument()==o.getIdSrc())src=o.getTaBonliv();
						else dest=o.getTaBonliv();
					if(o.getTaAcompte()!=null)
						if(o.getTaAcompte().getIdDocument()==o.getIdSrc())src=o.getTaAcompte();
						else dest=o.getTaAcompte();
					if(o.getTaAvoir()!=null)
						if(o.getTaAvoir().getIdDocument()==o.getIdSrc())src=o.getTaAvoir();
						else dest=o.getTaAvoir();
					if(o.getTaApporteur()!=null)
						if(o.getTaApporteur().getIdDocument()==o.getIdSrc())src=o.getTaApporteur();
						else dest=o.getTaApporteur();
					if(o.getTaAvisEcheance()!=null)
						if(o.getTaAvisEcheance().getIdDocument()==o.getIdSrc())src=o.getTaAvisEcheance();
						else dest=o.getTaAvisEcheance();
					if(o.getTaBoncde()!=null)
						if(o.getTaBoncde().getIdDocument()==o.getIdSrc())src=o.getTaBoncde();
						else dest=o.getTaBoncde();
					if(o.getTaDevis()!=null)
						if(o.getTaDevis().getIdDocument()==o.getIdSrc())src=o.getTaDevis();
						else dest=o.getTaDevis();
					if(o.getTaFacture()!=null)
						if(o.getTaFacture().getIdDocument()==o.getIdSrc())src=o.getTaFacture();
						else dest=o.getTaFacture();
					if(o.getTaPrelevement()!=null)
						if(o.getTaPrelevement().getIdDocument()==o.getIdSrc())src=o.getTaPrelevement();
						else dest=o.getTaPrelevement();
					if(o.getTaProforma()!=null)
						if(o.getTaProforma().getIdDocument()==o.getIdSrc())src=o.getTaProforma();
						else dest=o.getTaProforma();
					if(o.getTaTicketDeCaisse()!=null)
						if(o.getTaTicketDeCaisse().getIdDocument()==o.getIdSrc())src=o.getTaTicketDeCaisse();
						else dest=o.getTaTicketDeCaisse();
				
				listeDocumentSrc.add(src);
				//quand on a le source et le destinataire on peut essayer de créer les liaisons ligneALigne
				//pour chaque ligne src je regarde si article et unité correspondent dans dest
				List<ILigneDocumentTiers> lDestTraite=new LinkedList<>();
				if(src!=null && dest !=null) {
					for (ILigneDocumentTiers ligne : src.getLignesGeneral()) {
						lDestTraite=genereLigneALigne(ligne,dest,lDestTraite);
					} 
				}

//				if(src instanceof TaBonliv)taBonlivService.mergeEtat((TaBonliv) src);
//				if(src instanceof TaAcompte)taAcompteService.mergeEtat((TaAcompte) src);
//				if(src instanceof TaAvoir)taAvoirService.mergeEtat((TaAvoir) src);
//				if(src instanceof TaApporteur)taApporteurService.mergeEtat((TaApporteur) src);
//				if(src instanceof TaAvisEcheance)taAvisEcheanceService.mergeEtat((TaAvisEcheance) src);
//				if(src instanceof TaBoncde)taBoncdeService.mergeEtat((TaBoncde) src);
//				if(src instanceof TaBoncdeAchat)taBoncdeAchatService.mergeEtat((TaBoncdeAchat) src);
//				if(src instanceof TaDevis)taDevisService.mergeEtat((TaDevis) src);
//				if(src instanceof TaFacture)taFactureService.mergeEtat((TaFacture) src);
//				if(src instanceof TaPrelevement)taPrelevementService.mergeEtat((TaPrelevement) src);
//				if(src instanceof TaProforma)taProformaService.mergeEtat((TaProforma) src);
//				if(src instanceof TaTicketDeCaisse)taTicketDeCaisseService.mergeEtat((TaTicketDeCaisse) src);
//				if(src instanceof TaPreparation)taPreparationService.mergeEtat((TaPreparation) src);
//
//
//				LinkedList<IDocumentTiers> lien=new LinkedList<>();
//				lien.add(src);
//				if(dest instanceof TaBonliv)taBonlivService.mergeEtat((TaBonliv) dest);
//				if(dest instanceof TaAcompte)taAcompteService.mergeEtat((TaAcompte) dest);
//				if(dest instanceof TaAvoir)taAvoirService.mergeEtat((TaAvoir) dest);
//				if(dest instanceof TaApporteur)taApporteurService.mergeEtat((TaApporteur) dest);
//				if(dest instanceof TaAvisEcheance)taAvisEcheanceService.mergeEtat((TaAvisEcheance) dest);
//				if(dest instanceof TaBoncde)taBoncdeService.mergeEtat((TaBoncde) dest);
//				if(dest instanceof TaBoncdeAchat)taBoncdeAchatService.mergeEtat((TaBoncdeAchat) dest);
//				if(dest instanceof TaDevis)taDevisService.mergeEtat((TaDevis) dest);
//				if(dest instanceof TaFacture)taFactureService.mergeEtat((TaFacture) dest);
//				if(dest instanceof TaPrelevement)taPrelevementService.mergeEtat((TaPrelevement) dest);
//				if(dest instanceof TaProforma)taProformaService.mergeEtat((TaProforma) dest);
//				if(dest instanceof TaTicketDeCaisse)taTicketDeCaisseService.mergeEtat((TaTicketDeCaisse) dest);
//				if(dest instanceof TaPreparation)taPreparationService.mergeEtat((TaPreparation) dest);
				rang++;
				System.out.println("tour : " + rang);
				}
			}
			retour.setNbRetour(rang);
		} catch (Exception e) {
			System.out.println("erreur : " + e);
			retour.setMessage(e.getMessage());
			retour.setRetour(false);
		}
		retour.setMessage(retour.getMessage()+"----"+retour.getNbRetour()+" maj de documents sur "+retour.getNbDepart());
		return retour;
	}
	

	@TransactionTimeout(value=600,unit=TimeUnit.MINUTES)
	public RetourMajDossier majEtatFactureReglee()  {
		RetourMajDossier retour=new RetourMajDossier(true);
		
		try {
			System.out.println("Debut majEtatDocument: ");

			int rang=1;			

			List<TaFactureDTO> listeFac = taFactureService.rechercheDocumentPartiellementOrTotalementRegle();
			retour.setNbDepart(listeFac.size());
			for (TaFactureDTO d : listeFac) {
				transactionnalMergeEtatDocumentService.mergeEtatDocument(d);
				System.out.println("fac : " + rang);
				rang++;
			}
			
			
			retour.setNbRetour(rang);
			
		} catch (Exception e) {
			System.out.println("erreur : " + e);
			retour.setMessage(e.getMessage());
			retour.setRetour(false);
		}
		retour.setMessage(retour.getMessage()+"----"+retour.getNbRetour()+" maj de documents sur "+retour.getNbDepart());
		return retour;
	}
	
	private boolean rechercheDejaFait(IDocumentDTO dto,List<IDocumentDTO>l) {
		for (IDocumentDTO o : l) {
			if(o.getId()==dto.getId())return true;
		}
		return false;
	}

	@TransactionTimeout(value=600,unit=TimeUnit.MINUTES)
	public RetourMajDossier majEtatDocument()  {
		RetourMajDossier retour=new RetourMajDossier(true);
//		retour.setNbDepart(listeDocumentSrc.size());
		try {
			System.out.println("Debut majEtatDocument: ");
			List<IDocumentDTO> lDejaMaj=new LinkedList<>();
			List<IDocumentDTO>ll= transactionnalMergeEtatDocumentService.recupListeLigneALigne();
			retour.setNbDepart(ll.size());

			int rang=1;
			for (IDocumentDTO d : ll) {
				if(!rechercheDejaFait(d,lDejaMaj)) {
					transactionnalMergeEtatDocumentService.mergeEtatDocument(d);
				}else retour.setNbDepart(retour.getNbDepart()-1);
				lDejaMaj.add(d);
				System.out.println("d : " + rang);
				rang++;
			}

			
//			List<TaBonlivDTO> listeBl = taBonlivService.findAllLight();
//			rang=1;
//			for (TaBonlivDTO d : listeBl) {
//				transactionnalMergeEtatDocumentService.mergeEtatDocument(d);
//				System.out.println("bl : " + rang);
//				rang++;
//			}
//			
//			List<TaAcompteDTO> listeAc = taAcompteService.findAllLight();
//			rang=1;
//			for (TaAcompteDTO d : listeAc) {
//				transactionnalMergeEtatDocumentService.mergeEtatDocument(d);
//				System.out.println("ac : " + rang);
//				rang++;
//			}
//			
//			List<TaAvoirDTO> listeAv = taAvoirService.findAllLight();
//			rang=1;
//			for (TaAvoirDTO d : listeAv) {
//				transactionnalMergeEtatDocumentService.mergeEtatDocument(d);
//				System.out.println("av : " + rang);
//				rang++;
//			}
//			
//			List<TaApporteurDTO> listeAp = taApporteurService.findAllLight();
//			rang=1;
//			for (TaApporteurDTO d : listeAp) {
//				transactionnalMergeEtatDocumentService.mergeEtatDocument(d);
//				System.out.println("ap : " + rang);
//				rang++;
//			}
//			
//			List<TaAvisEcheanceDTO> listeAvis = taAvisEcheanceService.findAllLight();
//			rang=1;
//			for(TaAvisEcheanceDTO d : listeAvis) {
//				transactionnalMergeEtatDocumentService.mergeEtatDocument(d);
//				System.out.println("avis : " + rang);
//				rang++;
//			}
//			
//			List<TaBoncdeDTO> listeBc = taBoncdeService.findAllLight();
//			rang=1;
//			for (TaBoncdeDTO d : listeBc) {
//				transactionnalMergeEtatDocumentService.mergeEtatDocument(d);
//				System.out.println("bc : " + rang);
//				rang++;
//			}
//			
//			List<TaBoncdeAchatDTO> listeBcA = taBoncdeAchatService.findAllLight();
//			rang=1;
//			for (TaBoncdeAchatDTO d : listeBcA) {
//				transactionnalMergeEtatDocumentService.mergeEtatDocument(d);
//				System.out.println("bca : " + rang);
//				rang++;
//			}
//			
//			List<TaDevisDTO> listeDe = taDevisService.findAllLight();
//			rang=1;
//			for (TaDevisDTO d : listeDe) {
//				transactionnalMergeEtatDocumentService.mergeEtatDocument(d);
//				System.out.println("de : " + rang);
//				rang++;
//			}
//			
//			rang=1;
//			List<TaFactureDTO> listeFac = taFactureService.findAllLight();
//			for (TaFactureDTO d : listeFac) {
//				transactionnalMergeEtatDocumentService.mergeEtatDocument(d);
//				System.out.println("fac : " + rang);
//				rang++;
//			}
//			
//			List<TaPrelevementDTO> listePrel = taPrelevementService.findAllLight();
//			rang=1;
//			for (TaPrelevementDTO d : listePrel) {
//				transactionnalMergeEtatDocumentService.mergeEtatDocument(d);
//				System.out.println("prel : " + rang);
//				rang++;
//			}
//			
//			
//			List<TaProformaDTO> listePro = taProformaService.findAllLight();
//			rang=1;
//			for (TaProformaDTO d : listePro) {
//				transactionnalMergeEtatDocumentService.mergeEtatDocument(d);
//				System.out.println("pro : " + rang);
//				rang++;
//			}
//			
//			
//			List<TaTicketDeCaisseDTO> listeTicket = taTicketDeCaisseService.findAllLight();
//			rang=1;
//			for (TaTicketDeCaisseDTO d : listeTicket) {
//				transactionnalMergeEtatDocumentService.mergeEtatDocument(d);
//				System.out.println("ticket : " + rang);
//				rang++;
//			}
//			
//			
//			List<TaPreparationDTO> listePrepa = taPreparationService.findAllLight();
//			rang=1;
//			for (TaPreparationDTO d : listePrepa) {
//				transactionnalMergeEtatDocumentService.mergeEtatDocument(d);
//				System.out.println("prepa : " + rang);
//				rang++;
//			}
			
			
			retour.setNbRetour(rang);
		} catch (Exception e) {
			System.out.println("erreur : " + e);
			retour.setMessage(e.getMessage());
			retour.setRetour(false);
		}
		retour.setMessage(retour.getMessage()+"----"+retour.getNbRetour()+" maj de documents sur "+retour.getNbDepart());
		return retour;
	}

	
	public RetourMajDossier updateEtatEncoursTousDocs() {
		RetourMajDossier retour=new RetourMajDossier(true);
		try {
			System.out.println("Debut majEtatDocument: ");
			retour= daoREtat.updateEtatEncoursTousDocs();		


		} catch (Exception e) {
			System.out.println("erreur : " + e);
			retour.setMessage(e.getMessage());
			retour.setRetour(false);
		}
		if(retour.isRetour())retour.setMessage("MAJ Terminée avec succès");
		return retour;
	}
	
	public RetourMajDossier majLigneALigneProcedureStockee() {
		RetourMajDossier retour=new RetourMajDossier(true);
		try {
			System.out.println("Debut majLigneALigneProcedureStockee: ");
			retour= daoREtat.majLigneALigneProcedureStockee();		


		} catch (Exception e) {
			System.out.println("erreur : " + e);
			retour.setMessage(e.getMessage());
			retour.setRetour(false);
		}
		if(retour.isRetour())retour.setMessage("MAJ Terminée avec succès");
		return retour;
	}
	
	public RetourMajDossier updateEtatTousDocs() {
		RetourMajDossier retour=new RetourMajDossier(true);
		try {
			System.out.println("Debut majEtatDocument: ");
			retour= daoREtat.updateEtatTousDocs();		


		} catch (Exception e) {
			System.out.println("erreur : " + e);
			retour.setMessage(e.getMessage());
			retour.setRetour(false);
		}
		if(retour.isRetour())retour.setMessage("MAJ Terminée avec succès");
		return retour;
	}
	
//	public RetourMajDossier updateEtatTousDocs() {
//
//		RetourMajDossier retour=new RetourMajDossier(true);
//		int rang=0;
//		try {
//			System.out.println("Debut : ");
//			TaEtat etat=taEtatService.documentEncours(null);
//			TaREtat etatDoc;
//			TaREtatLigneDocument etatLigne;
//
//			List<TaFactureDTO> l = taFactureService.findAllLight();
//			retour.setNbDepart(l.size());
//			for (IDocumentDTO d : l) {
//				IDocumentTiers doc=taFactureService.getReference(d.getId());
//				etatDoc=new TaREtat();
//				etatDoc.setDateEtat(new Date());
//				etatDoc.setCommentaire("Initialisation par défaut par Legrain");
//				etatDoc.setTaFacture((TaFacture) doc);
//				etatDoc.setTaEtat(etat);
//
//				etatDoc=daoREtat.merge(etatDoc);
//
//				doc.setTaREtat(etatDoc);
//				for (ILigneDocumentTiers ld : doc.getLignesGeneral()) {
//					etatLigne=new TaREtatLigneDocument();
//					etatLigne.setDateEtat(new Date());
//					etatLigne.setCommentaire("Initialisation par défaut par Legrain");
//					etatLigne.setTaLFacture((TaLFacture) ld);
//					etatLigne.setTaEtat(etat);
//					etatLigne=daoREtatLigne.merge(etatLigne);
//					ld.setTaREtatLigneDocument(etatLigne);
//				}
//				//			doc.getTaREtats().add(etatDoc);
//				daoFacture.merge((TaFacture) doc);
//				rang++;
//				System.out.println("bl : " + rang);
//			}
//			retour.setNbRetour(rang);
//		} catch (Exception e) {
//			System.out.println("erreur : " + e);
//			retour.setMessage(e.getMessage());
//			retour.setRetour(false);
//		}
//		return retour;
//	}
	
	
}
