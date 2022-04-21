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
import fr.legrain.bdg.documents.service.remote.ITaRDocumentServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaTLigneServiceRemote;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.tiers.service.remote.ITaTAdrServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.IInfosDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.model.TaAcompte;
import fr.legrain.document.model.TaApporteur;
import fr.legrain.document.model.TaAvoir;
import fr.legrain.document.model.TaBoncde;
import fr.legrain.document.model.TaBonliv;
import fr.legrain.document.model.TaDevis;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaFlash;
import fr.legrain.document.model.TaInfosAbonnement;
import fr.legrain.document.model.TaLAcompte;
import fr.legrain.document.model.TaLApporteur;
import fr.legrain.document.model.TaLAvoir;
import fr.legrain.document.model.TaLBoncde;
import fr.legrain.document.model.TaLBonliv;
import fr.legrain.document.model.TaLDevis;
import fr.legrain.document.model.TaLFacture;
import fr.legrain.document.model.TaLFlash;
import fr.legrain.document.model.TaLPrelevement;
import fr.legrain.document.model.TaLProforma;
import fr.legrain.document.model.TaPrelevement;
import fr.legrain.document.model.TaProforma;
import fr.legrain.document.model.TaRDocument;
import fr.legrain.document.model.TaTLigne;
import fr.legrain.document.model.TaTicketDeCaisse;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.tiers.dto.AdresseInfosFacturationDTO;
import fr.legrain.tiers.dto.AdresseInfosLivraisonDTO;
import fr.legrain.tiers.dto.IdentiteTiersDTO;
import fr.legrain.tiers.dto.InfosCPaiementDTO;
import fr.legrain.tiers.model.TaAdresse;
import fr.legrain.tiers.model.TaCPaiement;
import fr.legrain.tiers.model.TaTTvaDoc;
import fr.legrain.tiers.model.TaTiers;


@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public abstract class AbstractGenereDocFlash extends AbstractGenereDoc {
	
	static Logger logger = Logger.getLogger(AbstractGenereDocFlash.class.getName());
	
	private  @EJB ITaTAdrServiceRemote taTAdrService;
	private  @EJB ITaTiersServiceRemote taTiersService;
	protected  @EJB ITaTLigneServiceRemote taTLigneService;
	private  @EJB ITaRDocumentServiceRemote taRDocumentService;
	protected @EJB  ITaTypeMouvementServiceRemote taTypeMouvementService;
	private @EJB ITaLotServiceRemote taLotService;
	private @EJB ITaArticleServiceRemote taArticleService;
	protected @EJB ITaMouvementStockServiceRemote taMouvementStockService;
	protected @EJB ITaEntrepotServiceRemote taEntrepotService;
	
	protected String libelleDoc="";
	protected Date dateDoc;
	protected String commentaire="";
	protected String code="";
	protected String codeTiers="";
	protected boolean multiple;
	protected boolean generationModele;
	protected boolean forceMouvement;
	private boolean generationLigne=false;
	protected boolean genereCode=true;
	protected boolean documentGereLesLots;
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
	protected boolean documentGereLesCrd;


	protected LgrDozerMapper<TaAdresse,AdresseInfosFacturationDTO> mapperModelToUIAdresseInfosDocument = new LgrDozerMapper<TaAdresse,AdresseInfosFacturationDTO>();
	protected LgrDozerMapper<TaAdresse,AdresseInfosLivraisonDTO> mapperModelToUIAdresseLivInfosDocument = new LgrDozerMapper<TaAdresse,AdresseInfosLivraisonDTO>();
	protected LgrDozerMapper<TaCPaiement,InfosCPaiementDTO> mapperModelToUICPaiementInfosDocument = new LgrDozerMapper<TaCPaiement,InfosCPaiementDTO>();
	protected LgrDozerMapper<TaTiers,IdentiteTiersDTO> mapperModelToUIIHMIdentiteTiersInfosDocument = new LgrDozerMapper<TaTiers,IdentiteTiersDTO>();

	protected TaFlash ds;
	protected IDocumentTiers dd;

//	private static AbstractGenereDoc getGenereDoc(TaFlash ds,Class c) {
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
	public IDocumentTiers copieDocument(TaFlash ds, IDocumentTiers dd){
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
	
	public abstract IDocumentTiers copieDocumentSpecifique(TaFlash ds, IDocumentTiers dd,Boolean ligneSeparatrice,String libelleSeparateur);
	
	public IDocumentTiers copieDocumentGeneral(TaFlash ds, IDocumentTiers dd){
		try {
			dd.setTaTiers(ds.getTaTiers());
//			dd.setCodeDocument("");
			dd.setDateDocument(ds.getDateFlash());
			dd.setDateLivDocument(ds.getDateFlash());
			dd.setLibelleDocument(ds.getLibelleFlash());
			
			int i=0;
			TaTLigne tl=taTLigneService.findByCode("C");
//			ds.calculeTvaEtTotaux();
			for (TaLFlash ligne : ds.getLignes()) {
				if(ligne.isGenereLigne()) {
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
	
	
	public ILigneDocumentTiers copieLigneDocument(TaLFlash ls, ILigneDocumentTiers ld) {
		copieLigneDocumentGeneral(ls,ld);
		copieLigneDocumentSpecifique(ls,ld);
		return ld;
	}
	
	public abstract ILigneDocumentTiers copieLigneDocumentSpecifique(TaLFlash ls, ILigneDocumentTiers ld);
	
	public ILigneDocumentTiers copieLigneDocumentGeneral(TaLFlash ls, ILigneDocumentTiers ld) {
		try {
			List<ArticleLotEntrepotDTO> listArticleLotEntrepot;
			BigDecimal valeur=null;
			BigDecimal htOrigine=null;
			ld.setTaTLigne(taTLigneService.findByCode("H"));
			ld.setTaArticle(taArticleService.findByCode(ls.getCodeArticle()));
			ld.setTaLot(taLotService.findByCode(ls.getNumLot()));
			if(!generationModele) {
				ld.setTaLot(ls.getTaLot());
				ld.setTaEntrepot(ls.getTaEntrepot());
				ld.setEmplacementLDocument(ls.getEmplacementLFlash());
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
//			ld.setNumLigneLDocument(ls.getNumLot());
			ld.setLibLDocument(ls.getLibLFlash());
			ld.setQteLDocument(ls.getQteLFlash());
			ld.setQte2LDocument(ls.getQte2LFlash());
			ld.setU1LDocument(ls.getU1LFlash());
			ld.setU2LDocument(ls.getU2LFlash());
//			ld.setPrixULDocument(ls.getPrixULDocument());
//			ld.setTauxTvaLDocument(ls.getTauxTvaLDocument());
//			ld.setCompteLDocument(ls.getCompteLDocument());
//			ld.setCodeTvaLDocument(ls.getCodeTvaLDocument());
//			ld.setCodeTTvaLDocument(ls.getCodeTTvaLDocument());
//			if(ls.getPrixULDocument()!=null && ls.getQteLDocument()!=null)
//				htOrigine=ls.getPrixULDocument().multiply(ls.getQteLDocument()).setScale(2,BigDecimal.ROUND_HALF_UP);
			//modif yann
			if(!generationModele && multiple) {
				//OLD
//				ld.setMtHtLDocument(ls.getMtHtLApresRemiseGlobaleDocument());
//				ld.setMtTtcLDocument(ls.getMtTtcLApresRemiseGlobaleDocument());
			}else {
			//NEW 
//				ld.setMtHtLDocument(ls.getMtHtLDocument());
//				ld.setMtTtcLDocument(ls.getMtTtcLDocument());
			}
			
			
			
			//ld.setMtHtLApresRemiseGlobaleDocument(ls.getMtHtLApresRemiseGlobaleDocument());
			//ld.setMtTtcLApresRemiseGlobaleDocument(ls.getMtTtcLApresRemiseGlobaleDocument());
//			valeur=BigDecimal.ZERO;
//			if(ls.getTaFlash().getTtc()==1){
//				if(htOrigine!=null)
//					//modif yann
////					if(!generationModele && multiple) {
////						//OLD
////						ld.setRemHtLDocument(ls.getMtTtcLApresRemiseGlobaleDocument().subtract(htOrigine).abs());	
////					}else {
////						//NEW
////						ld.setRemHtLDocument(htOrigine.subtract(ls.getMtTtcLDocument()).abs());	
////					}
//					
//				if(htOrigine!=null && htOrigine.compareTo(BigDecimal.valueOf(0))!=0)
//					valeur=ld.getRemHtLDocument().multiply(BigDecimal.valueOf(100)).divide(
//						htOrigine,MathContext.DECIMAL128).setScale(3,BigDecimal.ROUND_HALF_UP);
//			}else{
//				if(htOrigine!=null)
//					//modif yann
////					if(!generationModele && multiple) {
////					  //OLD
////					  ld.setRemHtLDocument(ls.getMtHtLApresRemiseGlobaleDocument().subtract(htOrigine).abs());	
////					}else {
////					  //NEW
////					  ld.setRemHtLDocument(htOrigine.subtract(ls.getMtHtLDocument()).abs());
////					}	
//						
//				if(htOrigine!=null && htOrigine.compareTo(BigDecimal.ZERO)!=0)
//					valeur=ld.getRemHtLDocument().multiply(BigDecimal.valueOf(100)).divide(
//						htOrigine,MathContext.DECIMAL128).setScale(3,BigDecimal.ROUND_HALF_UP);
//			}
//			
//			ld.setRemTxLDocument(valeur);
		} catch(Exception cnse) {
			logger.error("",cnse);
		}
		return ld;
	}
	
	public IInfosDocumentTiers copieInfosDocument( IInfosDocumentTiers id) {
		copieInfosDocumentGeneral(id);
		copieInfosDocumentSpecifique(id);
		return id;
	}
	
	public abstract IInfosDocumentTiers copieInfosDocumentSpecifique( IInfosDocumentTiers id);
	
	public IInfosDocumentTiers copieInfosDocumentGeneral( IInfosDocumentTiers id) {
		try {		
				if(id.getTaDocument()!=null && id.getTaDocument().getTaTiers()!=null) {
					if(id.getTaDocument().getTaTiers().aDesAdressesDuType(typeAdresseFacturation)) {
						//ajout des adresse de facturation au modele
						for (TaAdresse taAdresse : id.getTaDocument().getTaTiers().getTaAdresses()) {
							if(taAdresse.getTaTAdr()!=null && taAdresse.getTaTAdr().getCodeTAdr().equals(typeAdresseFacturation)){
								id.setAdresse1(taAdresse.getAdresse1Adresse());
								id.setAdresse2(taAdresse.getAdresse2Adresse());
								id.setAdresse3(taAdresse.getAdresse3Adresse());
								id.setCodepostal(taAdresse.getCodepostalAdresse());
								id.setVille(taAdresse.getVilleAdresse());
								id.setPays(taAdresse.getPaysAdresse());
							}
						}
					}else {
						if(id.getTaDocument().getTaTiers().getTaAdresse()!=null){
							id.setAdresse1(id.getTaDocument().getTaTiers().getTaAdresse().getAdresse1Adresse());
							id.setAdresse2(id.getTaDocument().getTaTiers().getTaAdresse().getAdresse2Adresse());
							id.setAdresse3(id.getTaDocument().getTaTiers().getTaAdresse().getAdresse3Adresse());
							id.setCodepostal(id.getTaDocument().getTaTiers().getTaAdresse().getCodepostalAdresse());
							id.setVille(id.getTaDocument().getTaTiers().getTaAdresse().getVilleAdresse());
							id.setPays(id.getTaDocument().getTaTiers().getTaAdresse().getPaysAdresse());
						}
					}
					if(id.getTaDocument().getTaTiers().aDesAdressesDuType(typeAdresseLivraison)) {
						//ajout des adresse de livraison au modele
						for (TaAdresse taAdresse : id.getTaDocument().getTaTiers().getTaAdresses()) {
							if(taAdresse.getTaTAdr()!=null && taAdresse.getTaTAdr().getCodeTAdr().equals(typeAdresseLivraison)){
								id.setAdresse1Liv(taAdresse.getAdresse1Adresse());
								id.setAdresse2Liv(taAdresse.getAdresse2Adresse());
								id.setAdresse3Liv(taAdresse.getAdresse3Adresse());
								id.setCodepostalLiv(taAdresse.getCodepostalAdresse());
								id.setVilleLiv(taAdresse.getVilleAdresse());
								id.setPaysLiv(taAdresse.getPaysAdresse());
							}
						}
					}else {
						if(id.getTaDocument().getTaTiers().getTaAdresse()!=null){
							id.setAdresse1Liv(id.getTaDocument().getTaTiers().getTaAdresse().getAdresse1Adresse());
							id.setAdresse2Liv(id.getTaDocument().getTaTiers().getTaAdresse().getAdresse2Adresse());
							id.setAdresse3Liv(id.getTaDocument().getTaTiers().getTaAdresse().getAdresse3Adresse());
							id.setCodepostalLiv(id.getTaDocument().getTaTiers().getTaAdresse().getCodepostalAdresse());
							id.setVilleLiv(id.getTaDocument().getTaTiers().getTaAdresse().getVilleAdresse());
							id.setPaysLiv(id.getTaDocument().getTaTiers().getTaAdresse().getPaysAdresse());
						}
					}
					id.setCodeCompta(id.getTaDocument().getTaTiers().getCodeCompta());
					id.setCompte(id.getTaDocument().getTaTiers().getCompte());
					id.setNomTiers(id.getTaDocument().getTaTiers().getNomTiers());
					id.setPrenomTiers(id.getTaDocument().getTaTiers().getPrenomTiers());
					id.setSurnomTiers(id.getTaDocument().getTaTiers().getSurnomTiers());
					if(id.getTaDocument().getTaTiers().getTaTCivilite()!=null)
						id.setCodeTCivilite(id.getTaDocument().getTaTiers().getTaTCivilite().getCodeTCivilite());
					if(id.getTaDocument().getTaTiers().getTaTEntite()!=null)
						id.setCodeTEntite(id.getTaDocument().getTaTiers().getTaTEntite().getCodeTEntite());
					if(id.getTaDocument().getTaTiers().getTaCompl()!=null)
						id.setTvaIComCompl(id.getTaDocument().getTaTiers().getTaCompl().getTvaIComCompl());
//					id.setCodeCPaiement("888");
					if(id.getTaDocument().getTaTiers().getTaCPaiement()!=null) {
						id.setCodeCPaiement(id.getTaDocument().getTaTiers().getTaCPaiement().getCodeCPaiement());
						id.setLibCPaiement(id.getTaDocument().getTaTiers().getTaCPaiement().getLibCPaiement());
						id.setReportCPaiement(id.getTaDocument().getTaTiers().getTaCPaiement().getReportCPaiement());
						id.setFinMoisCPaiement(id.getTaDocument().getTaTiers().getTaCPaiement().getFinMoisCPaiement());
					}
					if(id.getTaDocument().getTaTiers().getTaEntreprise()!=null)
					id.setNomEntreprise(id.getTaDocument().getTaTiers().getTaEntreprise().getNomEntreprise());
					if(id.getTaDocument().getTaTiers().getTaTTvaDoc()!=null)
						id.setCodeTTvaDoc(id.getTaDocument().getTaTiers().getTaTTvaDoc().getCodeTTvaDoc());
				}
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
	public IDocumentTiers genereDocument(TaFlash ds, IDocumentTiers dd, String codeTiers,boolean genereCode,boolean multiple) {
		return genereDocument(ds,dd,codeTiers,true,false,genereCode,multiple);
	}
	
	public IDocumentTiers enregistreDocument(IDocumentTiers dd){
		return enregistreNouveauDocument(dd);
	}
	/**
	 * Génère un document à partir d'un autre
	 * @param ds
	 * @param dd
	 * @return
	 */
	public IDocumentTiers genereDocument(TaFlash ds, IDocumentTiers dd, String codeTiers , Boolean enregistre , boolean generationModele,boolean genereCode,boolean multiple) {
		boolean dejaMouvement=false;
		this.genereCode=genereCode;
		this.ds = ds;
		this.dd = dd;
		this.generationModele=generationModele;
		this.multiple=multiple;
		dejaMouvement=false;

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
	
	public abstract IDocumentTiers genereDocumentSpecifique(TaFlash ds, IDocumentTiers dd, String codeTiers, boolean generationModele,boolean genereCode);
	
	public IDocumentTiers genereDocumentGeneral(TaFlash ds, IDocumentTiers dd, String codeTiers) {
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
	
	public boolean dejaGenere(TaFlash ds) {
		boolean dejaGenere = false;
		String jpql = creationRequeteDejaGenere(ds);
		List<TaRDocument> l = taRDocumentService.dejaGenere(jpql);
		System.err.println(l.size());
		if(l.size()>0) {
			dejaGenere = true;
		}
		return dejaGenere;
	}
	
	public IDocumentTiers dejaGenereDocument(TaFlash ds) {
		IDocumentTiers dejaGenere = null;
		String jpql = creationRequeteDejaGenere(ds);
		List<TaRDocument> l = taRDocumentService.dejaGenere(jpql);
		System.err.println(l.size());
		if(l.size()>0) {
			dejaGenere = (IDocumentTiers) l.get(0);
		}
		return dejaGenere;
	}
	
	public abstract String creationRequeteDejaGenere(TaFlash ds);
	
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
		if(dd.getTypeDocument().equals(TaBoncde.TYPE_DOC)){
			lf=new TaLBoncde();
			((TaLBoncde)lf).initialiseLigne(true);
			((TaLBoncde)lf).setLibLDocument(libelle);
			((TaLBoncde)lf).setTaDocument(((TaBoncde)dd));
			((TaLBoncde)lf).setTaTLigne(tl);
			((TaBoncde)dd).addLigne((TaLBoncde)lf);
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



	public boolean isDocumentGereLesLots() {
		return documentGereLesLots;
	}

	public void setDocumentGereLesLots(boolean documentGereLesLots) {
		this.documentGereLesLots = documentGereLesLots;
	}

	public boolean isDocumentGereLesCrd() {
		return documentGereLesCrd;
	}

	public void setDocumentGereLesCrd(boolean documentGereLesCrd) {
		this.documentGereLesCrd = documentGereLesCrd;
	}


}
