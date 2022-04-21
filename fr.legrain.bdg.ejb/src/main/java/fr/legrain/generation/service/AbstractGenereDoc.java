package fr.legrain.generation.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import org.apache.log4j.Logger;

import fr.legrain.article.dto.ArticleLotEntrepotDTO;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaEntrepotServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaLotServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaMouvementStockServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaTypeMouvementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLEcheanceServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLigneALigneEcheanceServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLigneALigneServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaRDocumentServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaRReglementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaTLigneServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.bdg.generation.service.remote.IAbstractGenereDocServiceRemote;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.tiers.service.remote.ITaEtatServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTAdrServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.IInfosDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.dto.TaLigneALigneEcheanceDTO;
import fr.legrain.document.model.TaAcompte;
import fr.legrain.document.model.TaApporteur;
import fr.legrain.document.model.TaAvisEcheance;
import fr.legrain.document.model.TaAvoir;
import fr.legrain.document.model.TaBonReception;
import fr.legrain.document.model.TaBoncde;
import fr.legrain.document.model.TaBoncdeAchat;
import fr.legrain.document.model.TaBonliv;
import fr.legrain.document.model.TaDevis;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaLAcompte;
import fr.legrain.document.model.TaLApporteur;
import fr.legrain.document.model.TaLAvisEcheance;
import fr.legrain.document.model.TaLAvoir;
import fr.legrain.document.model.TaLBonReception;
import fr.legrain.document.model.TaLBoncde;
import fr.legrain.document.model.TaLBoncdeAchat;
import fr.legrain.document.model.TaLBonliv;
import fr.legrain.document.model.TaLDevis;
import fr.legrain.document.model.TaLEcheance;
import fr.legrain.document.model.TaLFacture;
import fr.legrain.document.model.TaLPanier;
import fr.legrain.document.model.TaLPrelevement;
import fr.legrain.document.model.TaLPreparation;
import fr.legrain.document.model.TaLProforma;
import fr.legrain.document.model.TaLTicketDeCaisse;
import fr.legrain.document.model.TaLigneALigneEcheance;
import fr.legrain.document.model.TaPanier;
import fr.legrain.document.model.TaPrelevement;
import fr.legrain.document.model.TaPreparation;
import fr.legrain.document.model.TaProforma;
import fr.legrain.document.model.TaRDocument;
import fr.legrain.document.model.TaTLigne;
import fr.legrain.document.model.TaTicketDeCaisse;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.lib.data.LibCalcul;
import fr.legrain.tiers.dto.AdresseInfosFacturationDTO;
import fr.legrain.tiers.dto.AdresseInfosLivraisonDTO;
import fr.legrain.tiers.dto.IdentiteTiersDTO;
import fr.legrain.tiers.dto.InfosCPaiementDTO;
import fr.legrain.tiers.model.TaAdresse;
import fr.legrain.tiers.model.TaCPaiement;
import fr.legrain.tiers.model.TaTTvaDoc;
import fr.legrain.tiers.model.TaTiers;
import javassist.expr.Instanceof;


@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public abstract class AbstractGenereDoc {
	
	static Logger logger = Logger.getLogger(AbstractGenereDoc.class.getName());
	
	private  @EJB ITaTAdrServiceRemote taTAdrService;
	protected  @EJB ITaTiersServiceRemote taTiersService;
	protected  @EJB ITaTLigneServiceRemote taTLigneService;
	protected  @EJB ITaRDocumentServiceRemote taRDocumentService;
	protected @EJB  ITaTypeMouvementServiceRemote taTypeMouvementService;
	protected @EJB ITaLotServiceRemote taLotService;
	protected @EJB ITaMouvementStockServiceRemote taMouvementStockService;
	
	protected @EJB ITaPreferencesServiceRemote taPreferencesService;
	@EJB protected ITaArticleServiceRemote taArticleService ;
	@EJB protected ITaEtatServiceRemote taEtatService ;
	@EJB protected ITaLigneALigneServiceRemote taLigneALigneService ;
	@EJB protected ITaLigneALigneEcheanceServiceRemote taLigneALigneEcheanceService;
	
	@EJB protected ITaLEcheanceServiceRemote taLEcheanceService;
	
	protected String libelleDoc="";
	protected Date dateDoc;
	protected String commentaire="";
	protected String code="";
	protected String codeTiers="";
	protected boolean multiple;
	protected boolean generationModele;
	protected boolean documentGereLesLots;
	protected boolean documentGereLesCrd;
	protected boolean forceMouvement;
	protected boolean capturerReglement=false;
	private boolean generationLigne=false;
	protected boolean genereCode=true;
	protected boolean codeDejaGenere=false;

	private String typeAdresseFacturation="";
	private String typeAdresseLivraison="";
	
	protected Boolean relationDocument=true;
	protected Boolean ligneSeparatrice=false;
	protected String libelleSeparateurDoc="";
	protected Boolean RecupLibelleSeparateurDoc=false;
//	protected Integer report=0;
//	protected Integer finDeMois=0;
	protected TaCPaiement taCPaiement=new TaCPaiement();
	protected TaTTvaDoc taTTvaDoc=new TaTTvaDoc();



	protected LgrDozerMapper<TaAdresse,AdresseInfosFacturationDTO> mapperModelToUIAdresseInfosDocument = new LgrDozerMapper<TaAdresse,AdresseInfosFacturationDTO>();
	protected LgrDozerMapper<TaAdresse,AdresseInfosLivraisonDTO> mapperModelToUIAdresseLivInfosDocument = new LgrDozerMapper<TaAdresse,AdresseInfosLivraisonDTO>();
	protected LgrDozerMapper<TaCPaiement,InfosCPaiementDTO> mapperModelToUICPaiementInfosDocument = new LgrDozerMapper<TaCPaiement,InfosCPaiementDTO>();
	protected LgrDozerMapper<TaTiers,IdentiteTiersDTO> mapperModelToUIIHMIdentiteTiersInfosDocument = new LgrDozerMapper<TaTiers,IdentiteTiersDTO>();

	protected IDocumentTiers ds;
	protected IDocumentTiers dd;
	protected List<TaLigneALigneEcheance> lalEcheance;
	
	protected @EJB ITaEntrepotServiceRemote taEntrepotService;

//	private static AbstractGenereDoc getGenereDoc(IDocumentTiers ds,Class c) {
//		if(ds instanceof TaFacture && c.)
//		return null;
//		
//	}
	
	/**
	 * Copie un document dans un autre
	 * @param ds - document source
	 * @param dd - document destination
	 * @return - le document destination
	 */
	public IDocumentTiers copieDocument(IDocumentTiers ds, IDocumentTiers dd){
		copieDocumentGeneral(ds,dd);
		copieDocumentSpecifique(ds,dd,getLigneSeparatrice(),getLibelleSeparateurDoc());
		
		if(getLigneSeparatrice()){
			try {
				TaTLigne tl=taTLigneService.findByCode("C");
				creationNewLigne(tl,"");
			} catch (Exception e) {
				logger.error("", e);
			}
		}
		return dd;
	}
	
	public abstract IDocumentTiers copieDocumentSpecifique(IDocumentTiers ds, IDocumentTiers dd,Boolean ligneSeparatrice,String libelleSeparateur);
	
	public IDocumentTiers copieDocumentGeneral(IDocumentTiers ds, IDocumentTiers dd){
		try {
			dd.setTaTiers(ds.getTaTiers());
//			dd.setCodeDocument("");
			dd.setDateDocument(ds.getDateDocument());
			dd.setDateLivDocument(ds.getDateLivDocument());
			dd.setLibelleDocument(ds.getLibelleDocument());
			
			dd.setUtiliseUniteSaisie(ds.getUtiliseUniteSaisie());
			
			int i=0;
			TaTLigne tl=taTLigneService.findByCode("C");
			ds.calculeTvaEtTotaux();
			for (ILigneDocumentTiers ligne : ds.getLignesGeneral()) {
				if(ligne.getGenereLigne()) {
					if(getLibelleSeparateurDoc()!=null && !getLibelleSeparateurDoc().trim().equals("") && i==0){
						ILigneDocumentTiers ligneSup= creationNewLigne(tl,getLibelleSeparateurDoc());
						i++;
					}
				}
			}
			
		} catch(Exception cnse) {
			logger.error("",cnse);
		}

		return dd;
	}
	
	
	public ILigneDocumentTiers copieLigneDocument(ILigneDocumentTiers ls, ILigneDocumentTiers ld) {
		copieLigneDocumentGeneral(ls,ld);
		copieLigneDocumentSpecifique(ls,ld);
		return ld;
	}
	
	public abstract ILigneDocumentTiers copieLigneDocumentSpecifique(ILigneDocumentTiers ls, ILigneDocumentTiers ld);
	
	public ILigneDocumentTiers copieLigneDocumentGeneral(ILigneDocumentTiers ls, ILigneDocumentTiers ld) {
		try {
			List<ArticleLotEntrepotDTO> listArticleLotEntrepot;
			BigDecimal valeur=null;
			BigDecimal htOrigine=null;
			ld.setTaTLigne(ls.getTaTLigne());
			ld.setTaArticle(ls.getTaArticle());
			if(!generationModele) {
				ld.setTaLot(ls.getTaLot());
				ld.setTaEntrepot(ls.getTaEntrepot());
				ld.setEmplacementLDocument(ls.getEmplacementLDocument());
				if((ls.getTaLot()==null && !documentGereLesLots) 
						|| (ls.getTaArticle()!=null && (ls.getTaArticle().getGestionLot()!=null &&!ls.getTaArticle().getGestionLot()))) {
					//utiliser un lot virtuel
					if(ls.getTaLot()==null && ls.getTaArticle()!=null){
						listArticleLotEntrepot = new ArrayList<ArticleLotEntrepotDTO>();
						listArticleLotEntrepot = taMouvementStockService.getEtatStockByArticleVirtuel(ls.getTaArticle().getCodeArticle(),false);
						if(listArticleLotEntrepot!=null && listArticleLotEntrepot.size()>0) {
							ArticleLotEntrepotDTO lot=listArticleLotEntrepot.get(0);
							ld.setTaLot(taLotService.findByCode(lot.getNumLot()));
							if(lot.getIdEntrepot()!=null)ld.setTaEntrepot(taEntrepotService.findById(lot.getIdEntrepot()));
							if(lot.getEmplacement()!=null && !lot.getEmplacement().isEmpty())ld.setEmplacementLDocument(lot.getEmplacement());
						}else {
							ld.setTaLot(taLotService.findOrCreateLotVirtuelArticle(ls.getTaArticle().getIdArticle()));
						}

						//					ld.setTaLot(taLotService.findOrCreateLotVirtuelArticle(ls.getTaArticle().getIdArticle()));
					}
				}
			}
			ld.setNumLigneLDocument(ls.getNumLigneLDocument());
			ld.setLibLDocument(ls.getLibLDocument());
			ld.setQteLDocument(ls.getQteLDocument());
			ld.setQte2LDocument(ls.getQte2LDocument());
			ld.setU1LDocument(ls.getU1LDocument());
			ld.setU2LDocument(ls.getU2LDocument());
			ld.setPrixULDocument(ls.getPrixULDocument());
			ld.setTauxTvaLDocument(ls.getTauxTvaLDocument());
			ld.setCompteLDocument(ls.getCompteLDocument());
			ld.setCodeTvaLDocument(ls.getCodeTvaLDocument());
			ld.setCodeTTvaLDocument(ls.getCodeTTvaLDocument());
			ld.setUSaisieLDocument(ls.getUSaisieLDocument());
			ld.setQteSaisieLDocument(ls.getQteSaisieLDocument());
			if(ls.getPrixULDocument()!=null && ls.getQteLDocument()!=null)
				
				/* j'utilise cette fonction pour utiliser le même système d'arrondi que dans les écrans de document
				 * Quand on aura corriger cette façon d'arrondir, ça fonctionnera aussi ici*/
				ld.setMtHtLDocument(ls.getPrixULDocument().multiply(ls.getQteLDocument()));
				htOrigine=ld.getMtHtLDocument();
//				htOrigine=ls.getPrixULDocument().multiply(ls.getQteLDocument()).setScale(2,BigDecimal.ROUND_HALF_UP);
			//modif yann
			if(!generationModele && multiple) {
				//OLD
				ld.setMtHtLDocument(ls.getMtHtLApresRemiseGlobaleDocument());
				ld.setMtTtcLDocument(ls.getMtTtcLApresRemiseGlobaleDocument());
			}else {
			//NEW 
				ld.setMtHtLDocument(ls.getMtHtLDocument());
				ld.setMtTtcLDocument(ls.getMtTtcLDocument());
			}
			
			
			
			//ld.setMtHtLApresRemiseGlobaleDocument(ls.getMtHtLApresRemiseGlobaleDocument());
			//ld.setMtTtcLApresRemiseGlobaleDocument(ls.getMtTtcLApresRemiseGlobaleDocument());
			valeur=BigDecimal.ZERO;
			if(ls.getTaDocument().getTtc()==1){
				if(htOrigine!=null)
					//modif yann
					if(!generationModele && multiple) {
						//OLD
						ld.setRemHtLDocument(ls.getMtTtcLApresRemiseGlobaleDocument().subtract(htOrigine).abs());	
					}else {
						//NEW
						ld.setRemHtLDocument(htOrigine.subtract(ls.getMtTtcLDocument()).abs());	
					}
					
				if(htOrigine!=null && htOrigine.compareTo(BigDecimal.valueOf(0))!=0)
					valeur=ld.getRemHtLDocument().multiply(BigDecimal.valueOf(100)).divide(
						htOrigine,MathContext.DECIMAL128).setScale(3,BigDecimal.ROUND_HALF_UP);
			}else{
				if(htOrigine!=null)
					//modif yann
					if(!generationModele && multiple) {
					  //OLD
					  ld.setRemHtLDocument(ls.getMtHtLApresRemiseGlobaleDocument().subtract(htOrigine).abs());	
					}else {
					  //NEW
					  ld.setRemHtLDocument(htOrigine.subtract(ls.getMtHtLDocument()).abs());
					}	
						
				if(htOrigine!=null && htOrigine.compareTo(BigDecimal.ZERO)!=0)
					valeur=ld.getRemHtLDocument().multiply(BigDecimal.valueOf(100)).divide(
						htOrigine,MathContext.DECIMAL128).setScale(3,BigDecimal.ROUND_HALF_UP);
			}
			
			ld.setRemTxLDocument(valeur.abs());
			
			if(ls.getAbonnement() && !generationModele) {
				List<TaLEcheance>  liste =taLigneALigneEcheanceService.dejaGenereLEcheanceDocument(ls);
				for (TaLEcheance o : liste) {
					TaLigneALigneEcheance lal = new TaLigneALigneEcheance();
					lal.setTaLEcheance(o);
					if(dd instanceof TaApporteur)lal.setTaLApporteur((TaLApporteur) ld);
					if(dd instanceof TaAcompte)lal.setTaLAcompte((TaLAcompte) ld);
					if(dd instanceof TaAvoir)lal.setTaLAvoir((TaLAvoir) ld);
					if(dd instanceof TaAvisEcheance)lal.setTaLAvisEcheance((TaLAvisEcheance) ld);
					if(dd instanceof TaBonliv)lal.setTaLBonliv((TaLBonliv) ld);
					if(dd instanceof TaBoncde)lal.setTaLBoncde((TaLBoncde) ld);
					if(dd instanceof TaBoncdeAchat)lal.setTaLBoncdeAchat((TaLBoncdeAchat) ld);
					if(dd instanceof TaBonReception)lal.setTaLBonReception((TaLBonReception) ld);
					if(dd instanceof TaDevis)lal.setTaLDevis((TaLDevis) ld);
					if(dd instanceof TaFacture)lal.setTaLFacture((TaLFacture) ld);
					if(dd instanceof TaPanier)lal.setTaLPanier((TaLPanier) ld);
					if(dd instanceof TaPrelevement)lal.setTaLPrelevement((TaLPrelevement) ld);
					if(dd instanceof TaPreparation)lal.setTaLPreparation((TaLPreparation) ld);
					if(dd instanceof TaProforma)lal.setTaLProforma((TaLProforma) ld);
					if(dd instanceof TaTicketDeCaisse)lal.setTaLTicketDeCaisse((TaLTicketDeCaisse) ld);
					ld.setAbonnement(true);
					ld.getTaLigneALignesEcheance().add(lal);
				}
				
				if(liste!=null && liste.size()>0) {
                    if(dd instanceof TaFacture)taLEcheanceService.regleEcheances(liste);
                }
				
			}
		} catch(Exception cnse) {
			logger.error("",cnse);
		}
		return ld;
	}
	
	public IInfosDocumentTiers copieInfosDocument(IInfosDocumentTiers is, IInfosDocumentTiers id) {
		copieInfosDocumentGeneral(is,id);
		copieInfosDocumentSpecifique(is,id);
		return id;
	}
	
	public abstract IInfosDocumentTiers copieInfosDocumentSpecifique(IInfosDocumentTiers is, IInfosDocumentTiers id);
	
	public IInfosDocumentTiers copieInfosDocumentGeneral(IInfosDocumentTiers is, IInfosDocumentTiers id) {
		try {
			id.setAdresse1(is.getAdresse1());
			id.setAdresse2(is.getAdresse2());
			id.setAdresse3(is.getAdresse3());
			id.setCodepostal(is.getCodepostal());
			id.setVille(is.getVille());
			id.setPays(is.getPays());
			id.setAdresse1Liv(is.getAdresse1Liv());
			id.setAdresse2Liv(is.getAdresse2Liv());
			id.setAdresse3Liv(is.getAdresse3Liv());
			id.setCodepostalLiv(is.getCodepostalLiv());
			id.setVilleLiv(is.getVilleLiv());
			id.setPaysLiv(is.getPaysLiv());
			id.setCodeCompta(is.getCodeCompta());
			id.setCompte(is.getCompte());
			id.setNomTiers(is.getNomTiers());
			id.setPrenomTiers(is.getPrenomTiers());
			id.setSurnomTiers(is.getSurnomTiers());
			id.setCodeTCivilite(is.getCodeTCivilite());
			id.setCodeTEntite(is.getCodeTEntite());
			id.setTvaIComCompl(is.getTvaIComCompl());
			id.setCodeCPaiement("888");
			id.setCodeCPaiement(taCPaiement.getCodeCPaiement());
			id.setLibCPaiement(taCPaiement.getLibCPaiement());
			id.setReportCPaiement(taCPaiement.getReportCPaiement());
			id.setFinMoisCPaiement(taCPaiement.getFinMoisCPaiement());
			id.setNomEntreprise(is.getNomEntreprise());
			id.setCodeTTvaDoc(is.getCodeTTvaDoc());
		} catch(Exception cnse) {
			logger.error("",cnse);
		}
		return id;
	}
	
	/**
	 * Génère un document à partir d'un autre
	 * @param ds
	 * @param dd
	 * @return
	 */
	public IDocumentTiers genereDocument(IDocumentTiers ds, IDocumentTiers dd, String codeTiers,boolean genereCode,boolean multiple) {
		return genereDocument(ds,dd,codeTiers,true,false,genereCode,multiple);
	}
	

	public IDocumentTiers enregistreDocument(IDocumentTiers dd){
		return dd=enregistreNouveauDocument(dd);

	}
	/**
	 * Génère un document à partir d'un autre
	 * @param ds
	 * @param dd
	 * @return
	 */
	public IDocumentTiers genereDocument(IDocumentTiers ds, IDocumentTiers dd, String codeTiers , Boolean enregistre , boolean generationModele,boolean genereCode,boolean multiple) {
		boolean dejaMouvement=false;
		this.genereCode=genereCode;
		this.ds = ds;
		this.dd = dd;
		this.generationModele=generationModele;
		this.multiple=multiple;
		if(ds instanceof TaFacture && ((TaFacture)ds).getTaGrMouvStock()!=null)dejaMouvement=true;
		if(ds instanceof TaBonliv && ((TaBonliv)ds).getTaGrMouvStock()!=null)dejaMouvement=true;
		if(ds instanceof TaAvoir && ((TaAvoir)ds).getTaGrMouvStock()!=null)dejaMouvement=true;
		if(ds instanceof TaTicketDeCaisse && ((TaTicketDeCaisse)ds).getTaGrMouvStock()!=null)dejaMouvement=true;

		this.forceMouvement=(ds instanceof TaFacture )&&(dd instanceof TaAvoir ) ||
				(((dd instanceof TaBonliv )||(dd instanceof TaAvoir )||(dd instanceof TaFacture )||(dd instanceof TaTicketDeCaisse))&& (!dejaMouvement));
		dd=copieDocument(ds,dd);
		genereDocumentGeneral(ds,dd,codeTiers);
		genereDocumentSpecifique(ds,dd,codeTiers,generationModele,genereCode);
//		dd.calculDateEcheanceAbstract(taCPaiement.getReportCPaiement(),taCPaiement.getFinMoisCPaiement());
		if(dd.getDateLivDocument()==null) dd.setDateLivDocument(dd.getDateDocument());
		if(codeTiers!=null) {
			if(!this.codeTiers.equals(dd.getTaTiers().getCodeTiers())){
				TaTiers tiers = null;
				try {
					tiers = taTiersService.findByCode(this.codeTiers);
				} catch (FinderException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				dd.setTaTiers(tiers);
				changementDeTiers(dd,true);
			}
			
			
		}
		if (enregistre) enregistreNouveauDocument(dd);
		return dd;
	}
	
	public abstract IDocumentTiers genereDocumentSpecifique(IDocumentTiers ds, IDocumentTiers dd, String codeTiers, boolean generationModele,boolean genereCode);
	
	public IDocumentTiers genereDocumentGeneral(IDocumentTiers ds, IDocumentTiers dd, String codeTiers) {
		if(libelleDoc!=null) {
			dd.setLibelleDocument(libelleDoc);
		}
		if(dateDoc!=null) { //on a une date en parametre
			dd.setDateDocument(dateDoc);
		}
		
		return dd;
	}
	

	public IDocumentTiers enregistreNouveauDocument(IDocumentTiers dd) {
		dd=enregistreNouveauDocumentGeneral(dd);
		return dd=enregistreNouveauDocumentSpecifique(dd);
	}
	

	public abstract IDocumentTiers enregistreNouveauDocumentSpecifique(IDocumentTiers dd);

	

	public IDocumentTiers enregistreNouveauDocumentGeneral(IDocumentTiers dd) {
		return dd;

	}
	
//	public void enregistreRelationDocument(IDocumentTiers dd) {
//		enregistreRelationDocumentGeneral(dd);
//		enregistreRelationDocumentSpecifique(dd);
//	}
//	
//	public abstract void enregistreRelationDocumentSpecifique(IDocumentTiers dd);
//	
//	public void enregistreRelationDocumentGeneral(IDocumentTiers dd) {
//	}
	
	/**
	 * Initialise l'infosdocument du document passé en parametre si l'info document est null
	 * @param dd
	 */
	public abstract void initInfosDocument(IDocumentTiers dd);
	
	/**
	 * Initialisation des propriétés de la facture en fonction du codeTiers
	 */
	public  void changementDeTiers(IDocumentTiers dd,boolean changeTiers) {
		boolean leTiersADesAdresseLiv = false;
		boolean leTiersADesAdresseFact = false;
		
//		typeAdresseFacturation=DocumentPlugin.getDefault().getPreferenceStore().getString(fr.legrain.document.preferences.PreferenceConstants.TYPE_ADRESSE_FACTURATION);
//		typeAdresseLivraison=DocumentPlugin.getDefault().getPreferenceStore().getString(fr.legrain.document.preferences.PreferenceConstants.TYPE_ADRESSE_BONLIV);
		
		typeAdresseFacturation="";
		typeAdresseLivraison="";

		if(dd.getTaTiers()!=null && changeTiers){
			IdentiteTiersDTO ihmidentiteTiers=new IdentiteTiersDTO();
			mapperModelToUIIHMIdentiteTiersInfosDocument.map(dd.getTaTiers(),ihmidentiteTiers);
			mapUIToModelIHMIdentiteTiersVersInfosDoc(ihmidentiteTiers, dd);


			try {
				if(typeAdresseLivraison!=null && taTAdrService.findByCode(typeAdresseLivraison)!=null) { //le type d'adresse existe bien
					leTiersADesAdresseLiv = dd.getTaTiers().aDesAdressesDuType(typeAdresseLivraison); //le codeTiers a des adresse de ce type
				}
				if(typeAdresseFacturation!=null && taTAdrService.findByCode(typeAdresseFacturation)!=null) { //le type d'adresse existe bien
					leTiersADesAdresseFact = dd.getTaTiers().aDesAdressesDuType(typeAdresseFacturation); //le codeTiers a des adresse de ce type
				}					
			} catch (FinderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
			Iterator<TaAdresse> ite = null;
			TaAdresse taAdresseLiv =null;
			AdresseInfosLivraisonDTO ihmAdresseInfosLivraison = null;
			if(dd.getTaTiers().getTaAdresses()!=null)ite =  dd.getTaTiers().getTaAdresses().iterator();
			if(leTiersADesAdresseLiv) { 
				//ajout des adresse de livraison au modele
				while (ihmAdresseInfosLivraison==null && ite.hasNext()){	
					taAdresseLiv =ite.next();
					if(taAdresseLiv.getTaTAdr()!=null && taAdresseLiv.getTaTAdr().getCodeTAdr().equals(typeAdresseLivraison)){
						ihmAdresseInfosLivraison = new AdresseInfosLivraisonDTO();
						mapperModelToUIAdresseLivInfosDocument.map(taAdresseLiv, ihmAdresseInfosLivraison);
						mapUIToModelAdresseLivVersInfosDoc(ihmAdresseInfosLivraison, dd);
					}
				}
			}else if( ite.hasNext()){
				taAdresseLiv =ite.next();
				ihmAdresseInfosLivraison = new AdresseInfosLivraisonDTO();
				mapperModelToUIAdresseLivInfosDocument.map(taAdresseLiv, ihmAdresseInfosLivraison);
				mapUIToModelAdresseLivVersInfosDoc(ihmAdresseInfosLivraison, dd);
				}

			TaAdresse taAdresseFact =null;
			AdresseInfosFacturationDTO ihmAdresseInfosFacturation =null;
			if(dd.getTaTiers().getTaAdresses()!=null)ite =  dd.getTaTiers().getTaAdresses().iterator();
			if(leTiersADesAdresseFact) { 
				while (ihmAdresseInfosFacturation==null && ite.hasNext()){
					taAdresseFact =ite.next();
					if(taAdresseFact.getTaTAdr().getCodeTAdr().equals(typeAdresseFacturation)){
						ihmAdresseInfosFacturation = new AdresseInfosFacturationDTO();
						mapperModelToUIAdresseInfosDocument.map(taAdresseFact, ihmAdresseInfosFacturation);
						mapUIToModelAdresseFactVersInfosDoc(ihmAdresseInfosFacturation, dd);
					}
				}				
			}else if( ite.hasNext()){
				taAdresseFact =ite.next();
				ihmAdresseInfosFacturation = new AdresseInfosFacturationDTO();
				mapperModelToUIAdresseInfosDocument.map(taAdresseFact, ihmAdresseInfosFacturation);
				mapUIToModelAdresseFactVersInfosDoc(ihmAdresseInfosFacturation, dd);
			}			
//			Iterator<TaAdresse> ite = null;
//			if(leTiersADesAdresseLiv) { 
//				//ajout des adresse de livraison au modele
//				TaAdresse taAdresseLiv =null;
//				AdresseInfosLivraisonDTO ihmAdresseInfosLivraison = null;
//				ite =  dd.getTaTiers().getTaAdresses().iterator();
//				while (ihmAdresseInfosLivraison==null && ite.hasNext()){	
//					taAdresseLiv =ite.next();
//					if(taAdresseLiv.getTaTAdr().getCodeTAdr().equals(typeAdresseLivraison)){
//						ihmAdresseInfosLivraison = new AdresseInfosLivraisonDTO();
//						mapperModelToUIAdresseLivInfosDocument.map(taAdresseLiv, ihmAdresseInfosLivraison);
//						mapUIToModelAdresseLivVersInfosDoc(ihmAdresseInfosLivraison, dd);
//					}
//				}
//			}
//
//			if(leTiersADesAdresseFact) { 
//				TaAdresse taAdresseFact =null;
//				AdresseInfosFacturationDTO ihmAdresseInfosFacturation =null;
//				ite =  dd.getTaTiers().getTaAdresses().iterator();
//				while (ihmAdresseInfosFacturation==null && ite.hasNext()){
//					taAdresseFact =ite.next();
//					if(taAdresseFact.getTaTAdr().getCodeTAdr().equals(typeAdresseFacturation)){
//						ihmAdresseInfosFacturation = new AdresseInfosFacturationDTO();
//						mapperModelToUIAdresseInfosDocument.map(taAdresseFact, ihmAdresseInfosFacturation);
//						mapUIToModelAdresseFactVersInfosDoc(ihmAdresseInfosFacturation, dd);
//					}
//				}				
//			}
			
			
			if(dd.getTaTiers().getTaCPaiement()==null)
				mapUIToModelCPaiementVersInfosDoc(new InfosCPaiementDTO(), dd);
			else{
				InfosCPaiementDTO ihmInfosCPaiement =new InfosCPaiementDTO();
				mapperModelToUICPaiementInfosDocument.map(dd.getTaTiers().getTaCPaiement(), ihmInfosCPaiement);
				mapUIToModelCPaiementVersInfosDoc(ihmInfosCPaiement, dd);
			}
			
			mapUIToModelDocumentVersInfosDoc(dd);	
		}
	}
	
	public boolean dejaGenere(IDocumentTiers ds, boolean accepterMultiType) {
		boolean dejaGenere = false;
		String jpql = null;
		if(accepterMultiType)jpql=creationRequeteDejaGenere(ds);//accepte la génération si autre type de document déjà généré
		else jpql=creationRequeteDejaGenereSource(ds);//vérifie que le document source n'a pas déjà servi, peut important le type de document
		List<TaRDocument> l = taRDocumentService.dejaGenere(jpql);
		System.err.println(l.size());
		if(l.size()>0) {
			dejaGenere = true;
		}
		return dejaGenere;
	}
	
	public IDocumentTiers dejaGenereDocument(IDocumentTiers ds, boolean accepterMultiType) {
		IDocumentTiers dejaGenere = null;
		String jpql = null;
		if(accepterMultiType)jpql=creationRequeteDejaGenere(ds);//accepte la génération si autre type de document déjà généré
		else jpql=creationRequeteDejaGenereSource(ds);//vérifie que le document source n'a pas déjà servi, peut important le type de document
		List<TaRDocument> l = taRDocumentService.dejaGenere(jpql);
		System.err.println(l.size());
		if(l.size()>0) {
			dejaGenere = (IDocumentTiers) l.get(0);
		}
		return dejaGenere;
	}
	
	public abstract String creationRequeteDejaGenere(IDocumentTiers ds);
	
	public abstract void mapUIToModelAdresseLivVersInfosDoc(AdresseInfosLivraisonDTO infos, IDocumentTiers dd);
	public abstract void mapUIToModelAdresseFactVersInfosDoc(AdresseInfosFacturationDTO infos, IDocumentTiers dd);
	public abstract void mapUIToModelCPaiementVersInfosDoc(InfosCPaiementDTO infos, IDocumentTiers dd);
	public abstract void mapUIToModelIHMIdentiteTiersVersInfosDoc(IdentiteTiersDTO infos, IDocumentTiers dd);

	public abstract void mapUIToModelDocumentVersInfosDoc(IDocumentTiers dd);

	public String getLibelleDoc() {
		return libelleDoc;
	}

	public void setLibelleDoc(String libelleDoc) {
		this.libelleDoc = libelleDoc;
	}

	public Date getDateDoc() {
		return dateDoc;
	}

	public void setDateDoc(Date dateDoc) {
		this.dateDoc = dateDoc;
	}

	public String getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCodeTiers() {
		return codeTiers;
	}

	public void setCodeTiers(String codeTiers) {
		this.codeTiers = codeTiers;
	}

	public String getTypeAdresseFacturation() {
		return typeAdresseFacturation;
	}

	public void setTypeAdresseFacturation(String typeAdresseFacturation) {
		this.typeAdresseFacturation = typeAdresseFacturation;
	}

	public String getTypeAdresseLivraison() {
		return typeAdresseLivraison;
	}

	public void setTypeAdresseLivraison(String typeAdresseLivraison) {
		this.typeAdresseLivraison = typeAdresseLivraison;
	}
	public Boolean getRelationDocument() {
		return relationDocument;
	}

	public void setRelationDocument(Boolean relationDocument) {
		this.relationDocument = relationDocument;
	}

	public Boolean getLigneSeparatrice() {
		return ligneSeparatrice;
	}

	public void setLigneSeparatrice(Boolean ligneSeparatrice) {
		this.ligneSeparatrice = ligneSeparatrice;
	}

	public String getLibelleSeparateurDoc() {
		return libelleSeparateurDoc;
	}

	public void setLibelleSeparateurDoc(String libelleSeparateurDoc) {
		this.libelleSeparateurDoc = libelleSeparateurDoc;
	}

	public TaTTvaDoc getTaTTvaDoc() {
		return taTTvaDoc;
	}

	public void setTaTTvaDoc(TaTTvaDoc taTTvaDoc) {
		this.taTTvaDoc = taTTvaDoc;
	}
	
//	public Integer getReport() {
//		return report;
//	}
//
//	public void setReport(Integer report) {
//		this.report = report;
//	}
//
//	public Integer getFinDeMois() {
//		return finDeMois;
//	}
//
//	public void setFinDeMois(Integer finDeMois) {
//		this.finDeMois = finDeMois;
//	}

	public TaCPaiement getTaCPaiement() {
		return taCPaiement;
	}

	public void setTaCPaiement(TaCPaiement taCPaiement) {
		this.taCPaiement = taCPaiement;
	}



	public ILigneDocumentTiers creationNewLigne(TaTLigne tl,String libelle) throws Exception{
		ILigneDocumentTiers lf=null;

		if(dd.getTypeDocument().equals(TaPreparation.TYPE_DOC)){
			lf=new fr.legrain.document.model.TaLPreparation();
			((TaLPreparation)lf).initialiseLigne(true);
			((TaLPreparation)lf).setLibLDocument(libelle);
			((TaLPreparation)lf).setTaDocument(((TaPreparation)dd));
			((TaLPreparation)lf).setTaTLigne(tl);
			((TaPreparation)dd).addLigne((TaLPreparation)lf);
		}
		if(dd.getTypeDocument().equals(TaPanier.TYPE_DOC)){
			lf=new fr.legrain.document.model.TaLPanier();
			((TaLPanier)lf).initialiseLigne(true);
			((TaLPanier)lf).setLibLDocument(libelle);
			((TaLPanier)lf).setTaDocument(((TaPanier)dd));
			((TaLPanier)lf).setTaTLigne(tl);
			((TaPanier)dd).addLigne((TaLPanier)lf);
		}
		if(dd.getTypeDocument().equals(TaAvisEcheance.TYPE_DOC)){
			lf=new fr.legrain.document.model.TaLAvisEcheance();
			((TaLAvisEcheance)lf).initialiseLigne(true);
			((TaLAvisEcheance)lf).setLibLDocument(libelle);
			((TaLAvisEcheance)lf).setTaDocument(((TaAvisEcheance)dd));
			((TaLAvisEcheance)lf).setTaTLigne(tl);
			((TaAvisEcheance)dd).addLigne((TaLAvisEcheance)lf);
		}
		
		if(dd.getTypeDocument().equals(TaFacture.TYPE_DOC)){
			lf=new fr.legrain.document.model.TaLFacture();
			((TaLFacture)lf).initialiseLigne(true);
			((TaLFacture)lf).setLibLDocument(libelle);
			((TaLFacture)lf).setTaDocument(((TaFacture)dd));
			((TaLFacture)lf).setTaTLigne(tl);
			((TaFacture)dd).addLigne((TaLFacture)lf);
		}
		if(dd.getTypeDocument().equals(TaAvoir.TYPE_DOC)){
			lf=new TaLAvoir();
			((TaLAvoir)lf).initialiseLigne(true);
			((TaLAvoir)lf).setLibLDocument(libelle);
			((TaLAvoir)lf).setTaDocument(((TaAvoir)dd));
			((TaLAvoir)lf).setTaTLigne(tl);
			((TaAvoir)dd).addLigne((TaLAvoir)lf);
		}
		if(dd.getTypeDocument().equals(TaApporteur.TYPE_DOC)){
			lf=new TaLApporteur();
			((TaLApporteur)lf).initialiseLigne(true);
			((TaLApporteur)lf).setLibLDocument(libelle);
			((TaLApporteur)lf).setTaDocument(((TaApporteur)dd));
			((TaLApporteur)lf).setTaTLigne(tl);
			((TaApporteur)dd).addLigne((TaLApporteur)lf);
		}
		if(dd.getTypeDocument().equals(TaBonReception.TYPE_DOC)){
			lf=new TaLBonReception();
			((TaLBonReception)lf).initialiseLigne(true);
			((TaLBonReception)lf).setLibLDocument(libelle);
			((TaLBonReception)lf).setTaDocument(((TaBonReception)dd));
			((TaLBonReception)lf).setTaTLigne(tl);
			((TaBonReception)dd).addLigne((TaLBonReception)lf);
		}
		if(dd.getTypeDocument().equals(TaBoncde.TYPE_DOC)){
			lf=new TaLBoncde();
			((TaLBoncde)lf).initialiseLigne(true);
			((TaLBoncde)lf).setLibLDocument(libelle);
			((TaLBoncde)lf).setTaDocument(((TaBoncde)dd));
			((TaLBoncde)lf).setTaTLigne(tl);
			((TaBoncde)dd).addLigne((TaLBoncde)lf);
		}
		if(dd.getTypeDocument().equals(TaBoncdeAchat.TYPE_DOC)){
			lf=new TaLBoncdeAchat();
			((TaLBoncdeAchat)lf).initialiseLigne(true);
			((TaLBoncdeAchat)lf).setLibLDocument(libelle);
			((TaLBoncdeAchat)lf).setTaDocument(((TaBoncdeAchat)dd));
			((TaLBoncdeAchat)lf).setTaTLigne(tl);
			((TaBoncdeAchat)dd).addLigne((TaLBoncdeAchat)lf);
		}
		if(dd.getTypeDocument().equals(TaBonliv.TYPE_DOC)){
			lf=new TaLBonliv();
			((TaLBonliv)lf).initialiseLigne(true);
			((TaLBonliv)lf).setLibLDocument(libelle);
			((TaLBonliv)lf).setTaDocument(((TaBonliv)dd));
			((TaLBonliv)lf).setTaTLigne(tl);
			((TaBonliv)dd).addLigne((TaLBonliv)lf);
		}
		if(dd.getTypeDocument().equals(TaDevis.TYPE_DOC)){
			lf=new TaLDevis();
			((TaLDevis)lf).initialiseLigne(true);
			((TaLDevis)lf).setLibLDocument(libelle);
			((TaLDevis)lf).setTaDocument(((TaDevis)dd));
			((TaLDevis)lf).setTaTLigne(tl);
			((TaDevis)dd).addLigne((TaLDevis)lf);
		}
		if(dd.getTypeDocument().equals(TaAcompte.TYPE_DOC)){
			lf=new TaLAcompte();
			((TaLAcompte)lf).initialiseLigne(true);
			((TaLAcompte)lf).setLibLDocument(libelle);
			((TaLAcompte)lf).setTaDocument(((TaAcompte)dd));
			((TaLAcompte)lf).setTaTLigne(tl);
			((TaAcompte)dd).addLigne((TaLAcompte)lf);
		}
		if(dd.getTypeDocument().equals(TaPrelevement.TYPE_DOC)){
			lf=new TaLPrelevement();
			((TaLPrelevement)lf).initialiseLigne(true);
			((TaLPrelevement)lf).setLibLDocument(libelle);
			((TaLPrelevement)lf).setTaDocument(((TaPrelevement)dd));
			((TaLPrelevement)lf).setTaTLigne(tl);
			((TaPrelevement)dd).addLigne((TaLPrelevement)lf);
		}
		if(dd.getTypeDocument().equals(TaProforma.TYPE_DOC)){
			lf=new TaLProforma();
			((TaLProforma)lf).initialiseLigne(true);
			((TaLProforma)lf).setLibLDocument(libelle);
			((TaLProforma)lf).setTaDocument(((TaProforma)dd));
			((TaLProforma)lf).setTaTLigne(tl);
			((TaProforma)dd).addLigne((TaLProforma)lf);
		}
		if(dd.getTypeDocument().equals(TaTicketDeCaisse.TYPE_DOC)){
			lf=new TaLTicketDeCaisse();
			((TaLTicketDeCaisse)lf).initialiseLigne(true);
			((TaLTicketDeCaisse)lf).setLibLDocument(libelle);
			((TaLTicketDeCaisse)lf).setTaDocument(((TaTicketDeCaisse)dd));
			((TaLTicketDeCaisse)lf).setTaTLigne(tl);
			((TaTicketDeCaisse)dd).addLigne((TaLTicketDeCaisse)lf);
		}
		return lf;
	}

	public Boolean getRecupLibelleSeparateurDoc() {
		return RecupLibelleSeparateurDoc;
	}

	public void setRecupLibelleSeparateurDoc(Boolean recupLibelleSeparateurDoc) {
		RecupLibelleSeparateurDoc = recupLibelleSeparateurDoc;
	}

	public ITaTAdrServiceRemote getTaTAdrService() {
		return taTAdrService;
	}

	public void setTaTAdrService(ITaTAdrServiceRemote taTAdrService) {
		this.taTAdrService = taTAdrService;
	}

	public ITaTiersServiceRemote getTaTiersService() {
		return taTiersService;
	}

	public void setTaTiersService(ITaTiersServiceRemote taTiersService) {
		this.taTiersService = taTiersService;
	}

	public ITaTLigneServiceRemote getTaTLigneService() {
		return taTLigneService;
	}

	public void setTaTLigneService(ITaTLigneServiceRemote taTLigneService) {
		this.taTLigneService = taTLigneService;
	}

	public ITaRDocumentServiceRemote getTaRDocumentService() {
		return taRDocumentService;
	}

	public void setTaRDocumentService(ITaRDocumentServiceRemote taRDocumentService) {
		this.taRDocumentService = taRDocumentService;
	}

	public boolean isGenereCode() {
		return genereCode;
	}

	public void setGenereCode(boolean genereCode) {
		this.genereCode = genereCode;
	}



	public BigDecimal recupRatioQte(BigDecimal qteInitial,BigDecimal qteGen) {
		BigDecimal ratio=BigDecimal.ONE;
		if(qteInitial!=null && qteInitial.compareTo(BigDecimal.ZERO)!=0) {
			ratio=qteGen.divide(qteInitial,MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP);
		}
		return ratio;
	}

	public boolean isCodeDejaGenere() {
		return codeDejaGenere;
	}

	public void setCodeDejaGenere(boolean codeDejaGenere) {
		this.codeDejaGenere = codeDejaGenere;
	}
	
	public String creationRequeteDejaGenereSource(IDocumentTiers ds) {
		int idDoc = ds.getIdDocument();
		String jpql = "select x from TaRDocument x where  x.idSrc = "+idDoc;
		return jpql;
	}

	public boolean isDocumentGereLesLots() {
		return documentGereLesLots;
	}

	public void setDocumentGereLesLots(boolean documentGereLesLots) {
		this.documentGereLesLots = documentGereLesLots;
	}

	public boolean isCapturerReglement() {
		return capturerReglement;
	}

	public void setCapturerReglement(boolean capturerReglement) {
		this.capturerReglement = capturerReglement;
	}

	public boolean isDocumentGereLesCrd() {
		return documentGereLesCrd;
	}

	public void setDocumentGereLesCrd(boolean documentGereLesCrd) {
		this.documentGereLesCrd = documentGereLesCrd;
	}

}
