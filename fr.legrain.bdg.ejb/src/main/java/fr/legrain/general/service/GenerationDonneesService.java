package fr.legrain.general.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

import fr.legrain.article.model.TaArticle;
import fr.legrain.article.model.TaEntrepot;
import fr.legrain.article.model.TaFamille;
import fr.legrain.article.model.TaPrix;
import fr.legrain.article.model.TaRapportUnite;
import fr.legrain.article.model.TaTFabrication;
import fr.legrain.article.model.TaTReception;
import fr.legrain.article.model.TaUnite;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaEntrepotServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaFamilleServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaRTitreTransportServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaTFabricationServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaTReceptionServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaTTvaServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaTvaServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaUniteServiceRemote;
import fr.legrain.bdg.article.service.remote.ITypeCodeBarreServiceRemote;
import fr.legrain.bdg.article.titretransport.service.remote.ITaTitreTransportServiceRemote;
import fr.legrain.bdg.conformite.service.remote.ITaGroupeServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaBonReceptionServiceRemote;
import fr.legrain.bdg.lib.server.osgi.BdgProperties;
import fr.legrain.bdg.tiers.service.remote.ITaAdresseServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaCPaiementServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaComplServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaEmailServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaFamilleTiersServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTAdrServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTCiviliteServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTEmailServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTEntiteServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTPaiementServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTTarifServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTTelServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTTiersServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTTvaDocServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTWebServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTelephoneServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaWebServiceRemote;
import fr.legrain.conformite.model.TaGroupe;
import fr.legrain.document.model.TaBonReception;
import fr.legrain.general.service.remote.IGenerationDonneesServiceRemote;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.tiers.model.TaAdresse;
import fr.legrain.tiers.model.TaCommentaire;
import fr.legrain.tiers.model.TaEmail;
import fr.legrain.tiers.model.TaTEntite;
import fr.legrain.tiers.model.TaTTel;
import fr.legrain.tiers.model.TaTTiers;
import fr.legrain.tiers.model.TaTTvaDoc;
import fr.legrain.tiers.model.TaTelephone;
import fr.legrain.tiers.model.TaTiers;
import fr.legrain.tiers.model.TaWeb;

@Stateless
@Interceptors(ServerTenantInterceptor.class)
public class GenerationDonneesService implements IGenerationDonneesServiceRemote {

	static Logger logger = Logger.getLogger(GenerationDonneesService.class);

	@Resource 
	private SessionContext context;

	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private BdgProperties p = new BdgProperties();


	@EJB ITaTiersServiceRemote taTiersService;
	@EJB ITaArticleServiceRemote taArticleService;
	@EJB ITaBonReceptionServiceRemote taBonReceptionService;

	private @EJB ITaTTiersServiceRemote taTTiersService;

	private @EJB ITaTCiviliteServiceRemote taTCivlicteService;
	private @EJB ITaTTvaDocServiceRemote taTTvaDocService;
	private @EJB ITaComplServiceRemote taTComplService;
	private @EJB ITaTEntiteServiceRemote taTEntiteService;
	private @EJB ITaCPaiementServiceRemote taCPaiementService;
	private @EJB ITaTPaiementServiceRemote taTPaiementService;	
	private @EJB ITaTTarifServiceRemote taTTarifService;
	private @EJB ITaTelephoneServiceRemote taTelephoneService;	
	private @EJB ITaEmailServiceRemote taEmailService;
	private @EJB ITaWebServiceRemote taWebService;
	private @EJB ITaAdresseServiceRemote taAdresseService;
	private @EJB ITaTAdrServiceRemote taTAdresseService;
	private @EJB ITaTEmailServiceRemote taTEmailService;
	private @EJB ITaTWebServiceRemote taTWebService;
	private @EJB ITaTTelServiceRemote taTTelService;

	private @EJB ITaFamilleServiceRemote taFamilleService;	
	private @EJB ITaTvaServiceRemote taTvaService;
	private @EJB ITaTTvaServiceRemote taTTvaService;
	private @EJB ITaUniteServiceRemote taUniteService;
	private @EJB ITaRTitreTransportServiceRemote taRTitreTransportService;
	private @EJB ITaTitreTransportServiceRemote taTitreTransportService;
	private @EJB ITaGroupeServiceRemote taGroupeService;
	private @EJB ITaFamilleTiersServiceRemote taFamilleTiersService;	
	
	private @EJB ITaEntrepotServiceRemote taEntrepotService;
	private @EJB ITaTFabricationServiceRemote taTFabricationService;
	private @EJB ITaTReceptionServiceRemote taTReceptionService;
	private @EJB ITypeCodeBarreServiceRemote taTypeCodeBarreService;
	
	private Integer nbTotal = 1;
	private Integer nbFait = 0;
	private Integer progress = 0;
	
	private Integer nbArticle = 5;
	private Integer nbTiers = 5;
	private Integer nbBR = 0;
	private Integer nbFAB = 0;
	private Integer nbControleConf = 0;

	public GenerationDonneesService() {}

	@Override
	public void genTousLesParametres() {
		genTousLesParametresArticles();
		genTousLesParametresTiers();
		genTousLesParametresSolstyce();
		articleReelle();
		tiersReel();
//		bonDeReception();
	}
	
	@Override
	public void genTousLesParametresArticles() {
		familleArticle();
		unites();
	}
	
	@Override
	public void genTousLesParametresTiers() {

		typeAdresse();
		typeCivilite();
		typeEntite();
		typeTelephone();
		typeTiers();
		typeWeb();
		typeEmail();
		familleTiers();
	}
	
	@Override
	public void genTousLesParametresSolstyce() {
		entrepot();
		typeFabrication();
		typeReception();
		typeCodeBarre();
		groupeControles();
	}
	
	@Override
	public void genFamilleArticle() {
		familleArticle();
	}
	
	@Override
	public void genUnites() {
		unites(); 
	}
	
	@Override
	public void genTypeAdresse() {
		typeAdresse();
	}
	
	@Override
	public void genTypeCivilite() {
		typeCivilite();
	}
	
	@Override
	public void genTypeEntite() {
		typeEntite();
	}
	
	@Override
	public void genTypeTelephone() {
		typeTelephone();
	}
	
	@Override
	public void genTypeTiers() {
		typeTiers();
	}
	
	@Override
	public void genTypeWeb() {
		typeWeb(); 
	}
	
	@Override
	public void genTypeEmail() {
		typeEmail();
	}
	
	@Override
	public void genFamilleTiers() {
		familleTiers();
	}
	
	@Override
	public void genEntrepot() {
		entrepot();
	}
	
	@Override
	public void genTypeFabrication() {
		typeFabrication();
	}
	
	@Override
	public void genTypeReception() {
		typeReception();
	}
	
	@Override
	public void genTypeCodeBarre() {
		typeCodeBarre(); 
	}
	
	@Override
	public void genGroupeControles() {
		groupeControles();
	}
	
	@Override
	public void genData() {
		nbTotal = nbArticle+nbTiers+nbBR+nbFAB+nbControleConf;
		article();
		tiers();
		bonDeReception();
	}

	@Override
	public void familleArticle() {
		try {
//			for (int i = 0; i < nbArticle; i++) {
				TaFamille obj = new TaFamille();
				obj.setCodeFamille("ABATS");
				obj.setLibcFamille("Boyaux");
				obj.setLiblFamille("");
				obj = taFamilleService.merge(obj,ITaArticleServiceRemote.validationContext);
				
				obj = new TaFamille();
				obj.setCodeFamille("CONDITIONNEMENT FER");
				obj.setLibcFamille("BOITE CONSERVE FER BLANC-BARQUETTES ALU 300G/400G");
				obj.setLiblFamille("");
				obj = taFamilleService.merge(obj,ITaArticleServiceRemote.validationContext);
				
				obj = new TaFamille();
				obj.setCodeFamille("CONFITS");
				obj.setLibcFamille("CC/CO/CC PS/CP/GESIERS/GRAISSE/AILERONS CONFITS");
				obj.setLiblFamille("");
				obj = taFamilleService.merge(obj,ITaArticleServiceRemote.validationContext);
				
				obj = new TaFamille();
				obj.setCodeFamille("EPICERIE");
				obj.setLibcFamille("CONCENTRE TOMATES/HUILE/SEL&POIVRE/CONDIMENTS");
				obj.setLiblFamille("");
				obj = taFamilleService.merge(obj,ITaArticleServiceRemote.validationContext);
				
				obj = new TaFamille();
				obj.setCodeFamille("FOIS GRAS");
				obj.setLibcFamille("FG C / FG O / FG MC");
				obj.setLiblFamille("");
				obj = taFamilleService.merge(obj,ITaArticleServiceRemote.validationContext);
				
				obj = new TaFamille();
				obj.setCodeFamille("VIANDE DE CANARD");
				obj.setLibcFamille("CUISSE / MAGRET / CARCASSE...");
				obj.setLiblFamille("");
				obj = taFamilleService.merge(obj,ITaArticleServiceRemote.validationContext);
//			}
		} catch(Exception e) {
			e.printStackTrace();
		}

	}
	@Override
	public void unites() {
		try {
//			for (int i = 0; i < nbArticle; i++) {
				TaUnite obj = new TaUnite();
				obj.setCodeUnite("U");
				obj.setHauteur(null);
				obj.setLargeur(null);
				obj.setLiblUnite("Unité");
				obj.setLongueur(null);
				obj.setNbUnite(null);
				obj.setPoids(null);		
				
				obj = taUniteService.merge(obj,ITaArticleServiceRemote.validationContext);
				
				obj = new TaUnite();
				obj.setCodeUnite("G");
				obj.setHauteur(null);
				obj.setLargeur(null);
				obj.setLiblUnite("Gramme");
				obj.setLongueur(null);
				obj.setNbUnite(null);
				obj.setPoids(null);		
				
				obj = taUniteService.merge(obj,ITaArticleServiceRemote.validationContext);
//			}
		} catch(Exception e) {
			e.printStackTrace();
		}

	}
	
	@Override
	public void typeAdresse() {
//		try {
////			for (int i = 0; i < nbArticle; i++) {
//				TaTAdr obj = new TaTAdr();
//
//				obj.setCodeTAdr("");
//				obj.setLiblTAdr("");
//
//				obj = taTAdresseService.merge(obj,ITaArticleServiceRemote.validationContext);
////			}
//		} catch(Exception e) {
//			e.printStackTrace();
//		}

	}
	@Override
	public void typeCivilite() {
//		try {
////			for (int i = 0; i < nbArticle; i++) {
//				TaTCivilite obj = new TaTCivilite();
//
//				obj.setCodeTCivilite("");
//
//				obj = taTCivlicteService.merge(obj,ITaArticleServiceRemote.validationContext);
////			}
//		} catch(Exception e) {
//			e.printStackTrace();
//		}

	}
	@Override
	public void typeEntite() {
		try {
//			for (int i = 0; i < nbArticle; i++) {
				TaTEntite obj = new TaTEntite();
				obj.setCodeTEntite("SA");
				obj.setLiblTEntite("SA");
				obj = taTEntiteService.merge(obj,ITaArticleServiceRemote.validationContext);
				
				obj = new TaTEntite();
				obj.setCodeTEntite("SAS");
				obj.setLiblTEntite("SAS");
				obj = taTEntiteService.merge(obj,ITaArticleServiceRemote.validationContext);
				
//			}
		} catch(Exception e) {
			e.printStackTrace();
		}

	}
	@Override
	public void typeTelephone() {
		try {
//			for (int i = 0; i < nbArticle; i++) {
				TaTTel obj = new TaTTel();
				obj.setCodeTTel("S");
				obj.setLiblTTel("Siège");
				obj = taTTelService.merge(obj,ITaArticleServiceRemote.validationContext);
				
				
//			}
		} catch(Exception e) {
			e.printStackTrace();
		}

	}
	@Override
	public void typeTiers() {
//		try {
////			for (int i = 0; i < nbArticle; i++) {
//				TaTTiers obj = new TaTTiers();
//
//				obj.setCodeTTiers("");
//				obj.setCompteTTiers(compteTTiers);
//				obj.setLibelleTTiers(libelleTTiers);
//
//				obj = taTTiersService.merge(obj,ITaArticleServiceRemote.validationContext);
////			}
//		} catch(Exception e) {
//			e.printStackTrace();
//		}

	}
	@Override
	public void typeWeb() {
//		try {
////			for (int i = 0; i < nbArticle; i++) {
//				TaTWeb obj = new TaTWeb();
//
//				obj.setCodeTWeb("");
//				obj.setLiblTWeb(liblTWeb);
//
//				obj = taTWebService.merge(obj,ITaArticleServiceRemote.validationContext);
////			}
//		} catch(Exception e) {
//			e.printStackTrace();
//		}

	}
	@Override
	public void typeEmail() {
//		try {
////			for (int i = 0; i < nbArticle; i++) {
//				TaTEmail obj = new TaTEmail();
//
//				obj.setCodeTEmail("");
//				obj.setLiblTEmail(liblTEmail);
//
//				obj = taTEmailService.merge(obj,ITaArticleServiceRemote.validationContext);
////			}
//		} catch(Exception e) {
//			e.printStackTrace();
//		}

	}
	@Override
	public void familleTiers() {
//		try {
////			for (int i = 0; i < nbArticle; i++) {
//				TaFamilleTiers obj = new TaFamilleTiers();
//
//				obj.setCodeFamille("");
//				obj.setLibcFamille(libcFamille);
//				obj.setLiblFamille(liblFamille);
//
//				obj = taFamilleTiersService.merge(obj,ITaArticleServiceRemote.validationContext);
////			}
//		} catch(Exception e) {
//			e.printStackTrace();
//		}

	}
	@Override
	public void entrepot() {
		try {
//			for (int i = 0; i < nbArticle; i++) {
				TaEntrepot obj = new TaEntrepot();
				obj.setCodeEntrepot("ENT_1");
				obj.setLibelle("Entrepot 1");
				obj = taEntrepotService.merge(obj,ITaArticleServiceRemote.validationContext);
				
				obj = new TaEntrepot();
				obj.setCodeEntrepot("ENT_2");
				obj.setLibelle("Entrepot 2");
				obj = taEntrepotService.merge(obj,ITaArticleServiceRemote.validationContext);
//			}
		} catch(Exception e) {
			e.printStackTrace();
		}

	}
	@Override
	public void typeFabrication() {
		try {
//			for (int i = 0; i < nbArticle; i++) {
				TaTFabrication obj = new TaTFabrication();
				obj.setCodeTFabrication("ABA");
				obj.setLiblTFabrication("Abattage");
				obj = taTFabricationService.merge(obj,ITaArticleServiceRemote.validationContext);
				
				obj = new TaTFabrication();
				obj.setCodeTFabrication("DEC");
				obj.setLiblTFabrication("Découpage");
				obj = taTFabricationService.merge(obj,ITaArticleServiceRemote.validationContext);				
//			}
		} catch(Exception e) {
			e.printStackTrace();
		}

	}
	@Override
	public void typeReception() {
		try {
//			for (int i = 0; i < nbArticle; i++) {
				TaTReception obj = new TaTReception();
				obj.setCodeTReception("GAV");
				obj.setLiblTReception("Gavage");
				obj = taTReceptionService.merge(obj,ITaArticleServiceRemote.validationContext);
//			}
		} catch(Exception e) {
			e.printStackTrace();
		}

	}
	@Override
	public void typeCodeBarre() {
//		try {
////			for (int i = 0; i < nbArticle; i++) {
//				TaTypeCodeBarre obj = new TaTypeCodeBarre();
//
//				obj.setCodeTypeCodeBarre("");
//				obj.setLibelle(codebarre);
//
//				obj = taTypeCodeBarreService.merge(obj,ITaArticleServiceRemote.validationContext);
////			}
//		} catch(Exception e) {
//			e.printStackTrace();
//		}

	}
	@Override
	public void groupeControles() {
		try {
//			for (int i = 0; i < nbArticle; i++) {
				TaGroupe obj = new TaGroupe();
				obj.setCodeGroupe("GRP1");
				obj.setLibelle("groupe 1");
				obj = taGroupeService.merge(obj,ITaArticleServiceRemote.validationContext);
				
				obj = new TaGroupe();
				obj.setCodeGroupe("GRP2");
				obj.setLibelle("groupe 2");
				obj = taGroupeService.merge(obj,ITaArticleServiceRemote.validationContext);
				
				obj = new TaGroupe();
				obj.setCodeGroupe("GRP3");
				obj.setLibelle("groupe 3");
				obj = taGroupeService.merge(obj,ITaArticleServiceRemote.validationContext);
//			}
		} catch(Exception e) {
			e.printStackTrace();
		}

	}
	
	@Override
	public void articleReelle() {
		try {
//			for (int i = 0; i < nbArticle; i++) {
				TaArticle article = new TaArticle();

				article.setCodeArticle("CUISSE CG");
				article.setLibellecArticle("CUISSE DE CANARD GRAS");
				article.setLibellelArticle(article.getLibellecArticle());
				article.setActif(1);
				article.setMatierePremiere(true);
				article.setProduitFini(false);
				article.setParamDluo("30");
				article.setCodeBarre("123456789123");

				//U1 et Prix defaut
				TaPrix prix = new TaPrix();
				prix.setPrixPrix(new BigDecimal(0));
				prix.setPrixttcPrix(new BigDecimal(0));
				TaUnite u1 = taUniteService.findByCode("KG");
				article.setTaUnite1(u1);
				prix.setTaArticle(article);
				article.setTaPrix(prix);

				//Famille
				TaFamille f = taFamilleService.findByCode("VIANDE DE CANARD");
				article.setTaFamille(f);

				//Rapport unité
				TaRapportUnite rapport = new TaRapportUnite();
				rapport.setTaArticle(article);
				rapport.setTaUnite1(u1);
				TaUnite u2 = taUniteService.findByCode("KG");
				rapport.setTaUnite2(u2);
				rapport.setRapport(new BigDecimal(1));
				rapport.setNbDecimal(2);
				rapport.setSens(0);
				article.setTaRapportUnite(rapport);

				article.setStockMinArticle(new BigDecimal(0));

				article = taArticleService.merge(article,ITaArticleServiceRemote.validationContext);
				
				/*******************/
				article = new TaArticle();

				article.setCodeArticle("BOITE600G-99");
				article.setLibellecArticle("BOITE FER 600G DIAMETRE 99");
				article.setLibellelArticle(article.getLibellecArticle());
				article.setActif(1);
				article.setMatierePremiere(true);
				article.setProduitFini(false);
				article.setParamDluo("30");
				article.setCodeBarre("123456789123");

				//U1 et Prix defaut
				prix = new TaPrix();
				prix.setPrixPrix(new BigDecimal(0));
				prix.setPrixttcPrix(new BigDecimal(0));
				u1 = taUniteService.findByCode("U");
				article.setTaUnite1(u1);
				prix.setTaArticle(article);
				article.setTaPrix(prix);

				//Famille
				f = taFamilleService.findByCode("CONDITIONNEMENT FER");
				article.setTaFamille(f);

				//Rapport unité
				rapport = new TaRapportUnite();
				rapport.setTaArticle(article);
				rapport.setTaUnite1(u1);
				u2 = taUniteService.findByCode("U");
				rapport.setTaUnite2(u2);
				rapport.setRapport(new BigDecimal(1));
				rapport.setNbDecimal(2);
				rapport.setSens(0);
				article.setTaRapportUnite(rapport);

				article.setStockMinArticle(new BigDecimal(0));

				article = taArticleService.merge(article,ITaArticleServiceRemote.validationContext);
				
				/*******************/
				article = new TaArticle();

				article.setCodeArticle("BOITE1/2-52.6");
				article.setLibellecArticle("BOITE FER 1/2 65G DIAMETRE 52.6");
				article.setLibellelArticle(article.getLibellecArticle());
				article.setActif(1);
				article.setMatierePremiere(true);
				article.setProduitFini(false);
				article.setParamDluo("30");
				article.setCodeBarre("123456789123");

				//U1 et Prix defaut
				prix = new TaPrix();
				prix.setPrixPrix(new BigDecimal(0));
				prix.setPrixttcPrix(new BigDecimal(0));
				u1 = taUniteService.findByCode("U");
				article.setTaUnite1(u1);
				prix.setTaArticle(article);
				article.setTaPrix(prix);

				//Famille
				f = taFamilleService.findByCode("CONDITIONNEMENT FER");
				article.setTaFamille(f);

				//Rapport unité
				rapport = new TaRapportUnite();
				rapport.setTaArticle(article);
				rapport.setTaUnite1(u1);
				u2 = taUniteService.findByCode("U");
				rapport.setTaUnite2(u2);
				rapport.setRapport(new BigDecimal(1));
				rapport.setNbDecimal(2);
				rapport.setSens(0);
				article.setTaRapportUnite(rapport);

				article.setStockMinArticle(new BigDecimal(0));

				article = taArticleService.merge(article,ITaArticleServiceRemote.validationContext);
				
				
				/*******************/
				article = new TaArticle();

				article.setCodeArticle("FGO");
				article.setLibellecArticle("FOIE GRAS D'OIE");
				article.setLibellelArticle(article.getLibellecArticle());
				article.setActif(1);
				article.setMatierePremiere(true);
				article.setProduitFini(false);
				article.setParamDluo("30");
				article.setCodeBarre("123456789123");

				//U1 et Prix defaut
				prix = new TaPrix();
				prix.setPrixPrix(new BigDecimal(0));
				prix.setPrixttcPrix(new BigDecimal(0));
				u1 = taUniteService.findByCode("KG");
				article.setTaUnite1(u1);
				prix.setTaArticle(article);
				article.setTaPrix(prix);

				//Famille
				f = taFamilleService.findByCode("ABATS");
				article.setTaFamille(f);

				//Rapport unité
				rapport = new TaRapportUnite();
				rapport.setTaArticle(article);
				rapport.setTaUnite1(u1);
				u2 = taUniteService.findByCode("KG");
				rapport.setTaUnite2(u2);
				rapport.setRapport(new BigDecimal(1));
				rapport.setNbDecimal(2);
				rapport.setSens(0);
				article.setTaRapportUnite(rapport);

				article.setStockMinArticle(new BigDecimal(0));

				article = taArticleService.merge(article,ITaArticleServiceRemote.validationContext);
				

				/*******************/
				article = new TaArticle();

				article.setCodeArticle("SEL");
				article.setLibellecArticle("XEL");
				article.setLibellelArticle(article.getLibellecArticle());
				article.setActif(1);
				article.setMatierePremiere(true);
				article.setProduitFini(false);
				article.setParamDluo("30");
				article.setCodeBarre("123456789123");

				//U1 et Prix defaut
				prix = new TaPrix();
				prix.setPrixPrix(new BigDecimal(0));
				prix.setPrixttcPrix(new BigDecimal(0));
				u1 = taUniteService.findByCode("KG");
				article.setTaUnite1(u1);
				prix.setTaArticle(article);
				article.setTaPrix(prix);

				//Famille
				f = taFamilleService.findByCode("EPICERIE");
				article.setTaFamille(f);

				//Rapport unité
				rapport = new TaRapportUnite();
				rapport.setTaArticle(article);
				rapport.setTaUnite1(u1);
				u2 = taUniteService.findByCode("G");
				rapport.setTaUnite2(u2);
				rapport.setRapport(new BigDecimal(1));
				rapport.setNbDecimal(2);
				rapport.setSens(0);
				article.setTaRapportUnite(rapport);

				article.setStockMinArticle(new BigDecimal(0));

				article = taArticleService.merge(article,ITaArticleServiceRemote.validationContext);
				
				
				/*******************/
				article = new TaArticle();

				article.setCodeArticle("POIVRE");
				article.setLibellecArticle("POIVRE");
				article.setLibellelArticle(article.getLibellecArticle());
				article.setActif(1);
				article.setMatierePremiere(true);
				article.setProduitFini(false);
				article.setParamDluo("30");
				article.setCodeBarre("123456789123");

				//U1 et Prix defaut
				prix = new TaPrix();
				prix.setPrixPrix(new BigDecimal(0));
				prix.setPrixttcPrix(new BigDecimal(0));
				u1 = taUniteService.findByCode("KG");
				article.setTaUnite1(u1);
				prix.setTaArticle(article);
				article.setTaPrix(prix);

				//Famille
				f = taFamilleService.findByCode("EPICERIE");
				article.setTaFamille(f);

				//Rapport unité
				rapport = new TaRapportUnite();
				rapport.setTaArticle(article);
				rapport.setTaUnite1(u1);
				u2 = taUniteService.findByCode("G");
				rapport.setTaUnite2(u2);
				rapport.setRapport(new BigDecimal(1));
				rapport.setNbDecimal(2);
				rapport.setSens(0);
				article.setTaRapportUnite(rapport);

				article.setStockMinArticle(new BigDecimal(0));

				article = taArticleService.merge(article,ITaArticleServiceRemote.validationContext);
				

				/*******************/
				article = new TaArticle();

				article.setCodeArticle("GRAISSE");
				article.setLibellecArticle("GRAISSE ISSUE FABRICATION");
				article.setLibellelArticle(article.getLibellecArticle());
				article.setActif(1);
				article.setMatierePremiere(true);
				article.setProduitFini(false);
				article.setParamDluo("30");
				article.setCodeBarre("123456789123");

				//U1 et Prix defaut
				prix = new TaPrix();
				prix.setPrixPrix(new BigDecimal(0));
				prix.setPrixttcPrix(new BigDecimal(0));
				u1 = taUniteService.findByCode("KG");
				article.setTaUnite1(u1);
				prix.setTaArticle(article);
				article.setTaPrix(prix);

				//Famille
				f = taFamilleService.findByCode("VIANDE DE CANARD");
				article.setTaFamille(f);

				//Rapport unité
				rapport = new TaRapportUnite();
				rapport.setTaArticle(article);
				rapport.setTaUnite1(u1);
				u2 = taUniteService.findByCode("KG");
				rapport.setTaUnite2(u2);
				rapport.setRapport(new BigDecimal(1));
				rapport.setNbDecimal(2);
				rapport.setSens(0);
				article.setTaRapportUnite(rapport);

				article.setStockMinArticle(new BigDecimal(0));

				article = taArticleService.merge(article,ITaArticleServiceRemote.validationContext);
				
				/*********************************/
				
				/*********Produits finis**********/
				article = new TaArticle();

				article.setCodeArticle("SECC560G");
				article.setLibellecArticle("CONFIT CANARD 560G SE");
				article.setLibellelArticle(article.getLibellecArticle());
				article.setActif(1);
				article.setMatierePremiere(false);
				article.setProduitFini(true);
				article.setParamDluo("30");
				article.setCodeBarre("123456789123");

				//U1 et Prix defaut
				prix = new TaPrix();
				prix.setPrixPrix(new BigDecimal(0));
				prix.setPrixttcPrix(new BigDecimal(0));
				u1 = taUniteService.findByCode("U");
				article.setTaUnite1(u1);
				prix.setTaArticle(article);
				article.setTaPrix(prix);

				//Famille
				f = taFamilleService.findByCode("CONFITS");
				article.setTaFamille(f);

				//Rapport unité
				rapport = new TaRapportUnite();
				rapport.setTaArticle(article);
				rapport.setTaUnite1(u1);
				u2 = taUniteService.findByCode("KG");
				rapport.setTaUnite2(u2);
				rapport.setRapport(new BigDecimal(1));
				rapport.setNbDecimal(2);
				rapport.setSens(0);
				article.setTaRapportUnite(rapport);

				article.setStockMinArticle(new BigDecimal(0));

				article = taArticleService.merge(article,ITaArticleServiceRemote.validationContext);
				
				
				/*********Produits finis**********/
				article = new TaArticle();

				article.setCodeArticle("SEFO1/12");
				article.setLibellecArticle("FOIE GRAS D'OIE 1/12 SE");
				article.setLibellelArticle(article.getLibellecArticle());
				article.setActif(1);
				article.setMatierePremiere(false);
				article.setProduitFini(true);
				article.setParamDluo("30");
				article.setCodeBarre("123456789123");

				//U1 et Prix defaut
				prix = new TaPrix();
				prix.setPrixPrix(new BigDecimal(0));
				prix.setPrixttcPrix(new BigDecimal(0));
				u1 = taUniteService.findByCode("U");
				article.setTaUnite1(u1);
				prix.setTaArticle(article);
				article.setTaPrix(prix);

				//Famille
				f = taFamilleService.findByCode("FOIES GRAS");
				article.setTaFamille(f);

				//Rapport unité
				rapport = new TaRapportUnite();
				rapport.setTaArticle(article);
				rapport.setTaUnite1(u1);
				u2 = taUniteService.findByCode("KG");
				rapport.setTaUnite2(u2);
				rapport.setRapport(new BigDecimal(1));
				rapport.setNbDecimal(2);
				rapport.setSens(0);
				article.setTaRapportUnite(rapport);

				article.setStockMinArticle(new BigDecimal(0));

				article = taArticleService.merge(article,ITaArticleServiceRemote.validationContext);
				
				
				
//				nbFait++;
//			}
		} catch(Exception e) {
			e.printStackTrace();
		}

	}

	
	@Override
	public void tiersReel() {
		try {
//			for (int i = 0; i < nbTiers; i++) {
			List<TaTiers> listeTiers=taTiersService.selectAll();
			for (TaTiers taTiers : listeTiers) {
				taTiersService.remove(taTiers);
			}
				TaTiers tiers = new TaTiers();

				tiers.setCodeTiers(taTiersService.genereCode(null)); 

				tiers.setCodeCompta(tiers.getCodeTiers());
				tiers.setCompte("401"); //ejb

				TaTTiers taTTiers = taTTiersService.findByCode("C");
				tiers.setCompte(taTTiers.getCompteTTiers());
				tiers.setTaTTiers(taTTiers);
				
				tiers.setTtcTiers(0);
				
				tiers.setActifTiers(1);
				
				TaCommentaire commentaire=new TaCommentaire();
				commentaire.setCommentaire("Fournisseur : Epicerie diverse Barquette, film...");
				tiers.setTaCommentaire(commentaire);

				TaTTvaDoc taTTvaDoc = taTTvaDocService.findByCode("F");
				tiers.setTaTTvaDoc(taTTvaDoc);
				
				TaTEntite taTEntite = taTEntiteService.findByCode("SA");
				tiers.setTaTEntite(taTEntite);
				
//				TaTCivilite taTCivilite = taTCivlicteService.findByCode("F");
//				tiers.setTaTCivilite(taTCivilite);
				
				tiers.setNomTiers("LA BOVIDA");
				tiers.setPrenomTiers("");
				
				TaAdresse adresse = new TaAdresse();
				adresse.setAdresse1Adresse("Le césar");
				adresse.setAdresse2Adresse("Rue des bois des Chagnières");
				adresse.setAdresse3Adresse("");
				adresse.setCodepostalAdresse("18570");
				adresse.setVilleAdresse("LE SUBDRAY");
				adresse.setPaysAdresse("France");
				adresse.setCommAdministratifAdresse(0);
				adresse.setCommCommercialAdresse(0);
				adresse.setTaTiers(tiers);
				tiers.addAdresse(adresse);
				
				
				TaTelephone telephone = new TaTelephone();
				telephone.setNumeroTelephone("0248667300");				
				telephone.setTaTTel(taTTelService.findByCode("S"));
				tiers.addTelephone(telephone);
				telephone.setTaTiers(tiers);
				
//				telephone = new TaTelephone();
//				telephone.setNumeroTelephone("0248667306");				
//				telephone.setTaTTel(taTTelService.findByCode("FAX"));
//				tiers.addTelephone(telephone);
//				telephone.setTaTiers(tiers);
				
				TaEmail email = new TaEmail();
				email.setAdresseEmail("");
				tiers.addEmail(email);
				email.setTaTiers(tiers);
				
				TaWeb web = new TaWeb();
				web.setAdresseWeb("www.labovida.com");
				tiers.addWeb(web);
				web.setTaTiers(tiers);
				

				tiers = taTiersService.merge(tiers,ITaTiersServiceRemote.validationContext);
				
				
				/******************/
				TaTiers tiers2 = new TaTiers();

				tiers2.setCodeTiers(taTiersService.genereCode(null)); 

				tiers2.setCodeCompta(tiers2.getCodeTiers());
				tiers2.setCompte("401"); //ejb

				taTTiers = taTTiersService.findByCode("C");
				tiers2.setCompte(taTTiers.getCompteTTiers());
				tiers2.setTaTTiers(taTTiers);
				
				tiers2.setTtcTiers(0);
				
				tiers2.setActifTiers(1);
				
				TaCommentaire commentaire2=new TaCommentaire();
				commentaire2.setCommentaire("Société de Commercialisation : Boites métalliques / Emballages cartons / Plateaux fruits et légumes");
				tiers2.setTaCommentaire(commentaire2);

				taTTvaDoc = taTTvaDocService.findByCode("F");
				tiers2.setTaTTvaDoc(taTTvaDoc);
				
				taTEntite = taTEntiteService.findByCode("SARL");
				tiers2.setTaTEntite(taTEntite);
				
//				TaTCivilite taTCivilite = taTCivlicteService.findByCode("F");
//				tiers.setTaTCivilite(taTCivilite);
				
				tiers2.setNomTiers("COPROMETAL");
				tiers2.setPrenomTiers("");
				
				TaAdresse adresse2 = new TaAdresse();
				adresse2.setAdresse1Adresse("Mau");
				adresse2.setAdresse2Adresse("");
				adresse2.setAdresse3Adresse("");
				adresse2.setCodepostalAdresse("47300");
				adresse2.setVilleAdresse("LE LEDAT");
				adresse2.setPaysAdresse("France");
				adresse2.setCommAdministratifAdresse(0);
				adresse2.setCommCommercialAdresse(0);
				tiers2.addAdresse(adresse2);
				adresse2.setTaTiers(tiers2);
				
				
				TaTelephone telephone2 = new TaTelephone();
				telephone2.setNumeroTelephone("0553707070");				
				telephone2.setTaTTel(taTTelService.findByCode("S"));
				tiers2.addTelephone(telephone2);
				telephone2.setTaTiers(tiers2);
				
//				TaTelephone telephone3 = new TaTelephone();
//				telephone3.setNumeroTelephone("0553402321");				
//				telephone3.setTaTTel(taTTelService.findByCode("FAX"));
//				tiers2.addTelephone(telephone3);
//				telephone3.setTaTiers(tiers2);
				
				TaEmail email2 = new TaEmail();
				email2.setAdresseEmail("coprometal@libertysurf.fr");
				tiers2.addEmail(email2);
				email2.setTaTiers(tiers2);
				
				TaWeb web2 = new TaWeb();
				web2.setAdresseWeb("");
				tiers2.addWeb(web2);
				web2.setTaTiers(tiers2);

				tiers2 = taTiersService.merge(tiers2,ITaTiersServiceRemote.validationContext);
				
				/******************/
				tiers = new TaTiers();

				tiers.setCodeTiers(taTiersService.genereCode(null)); 

				tiers.setCodeCompta(tiers.getCodeTiers());
				tiers.setCompte("401"); //ejb

				taTTiers = taTTiersService.findByCode("C");
				tiers.setCompte(taTTiers.getCompteTTiers());
				tiers.setTaTTiers(taTTiers);
				
				tiers.setTtcTiers(0);
				
				tiers.setActifTiers(1);
				
				commentaire=new TaCommentaire();
				commentaire.setCommentaire("");
				tiers.setTaCommentaire(commentaire);

				taTTvaDoc = taTTvaDocService.findByCode("F");
				tiers.setTaTTvaDoc(taTTvaDoc);
				
				taTEntite = taTEntiteService.findByCode("SA");
				tiers.setTaTEntite(taTEntite);
				
//				TaTCivilite taTCivilite = taTCivlicteService.findByCode("F");
//				tiers.setTaTCivilite(taTCivilite);
				
				tiers.setNomTiers("SARLAT PERIGORD FOIE GRAS");
				tiers.setPrenomTiers("");
				
				adresse = new TaAdresse();
				adresse.setAdresse1Adresse("rue pierre Brossolette");
				adresse.setAdresse2Adresse("");
				adresse.setAdresse3Adresse("");
				adresse.setCodepostalAdresse("24200");
				adresse.setVilleAdresse("SARLAT");
				adresse.setPaysAdresse("France");
				adresse.setCommAdministratifAdresse(0);
				adresse.setCommCommercialAdresse(0);
				tiers.addAdresse(adresse);
				adresse.setTaTiers(tiers);
				
				telephone = new TaTelephone();
				telephone.setNumeroTelephone("0553302879");				
				telephone.setTaTTel(taTTelService.findByCode("S"));
				tiers.addTelephone(telephone);
				telephone.setTaTiers(tiers);
				
//				telephone = new TaTelephone();
//				telephone.setNumeroTelephone("0553302558");				
//				telephone.setTaTTel(taTTelService.findByCode("FAX"));
//				tiers.addTelephone(telephone);
//				telephone.setTaTiers(tiers);
				
				email = new TaEmail();
				email.setAdresseEmail("");
				tiers.addEmail(email);
				email.setTaTiers(tiers);
				
				web = new TaWeb();
				web.setAdresseWeb("");
				tiers.addWeb(web);
				web.setTaTiers(tiers);

				tiers = taTiersService.merge(tiers,ITaTiersServiceRemote.validationContext);
				
				/******************/
				tiers = new TaTiers();

				tiers.setCodeTiers(taTiersService.genereCode(null)); 

				tiers.setCodeCompta(tiers.getCodeTiers());
				tiers.setCompte("401"); //ejb

				taTTiers = taTTiersService.findByCode("C");
				tiers.setCompte(taTTiers.getCompteTTiers());
				tiers.setTaTTiers(taTTiers);
				
				tiers.setTtcTiers(0);
				
				tiers.setActifTiers(1);
				
				commentaire=new TaCommentaire();
				commentaire.setCommentaire("Logo vétérinaire (fr 24.439.004.ce)");
				tiers.setTaCommentaire(commentaire);

				taTTvaDoc = taTTvaDocService.findByCode("F");
				tiers.setTaTTvaDoc(taTTvaDoc);
				
				taTEntite = taTEntiteService.findByCode("SA");
				tiers.setTaTEntite(taTEntite);
				
//				TaTCivilite taTCivilite = taTCivlicteService.findByCode("F");
//				tiers.setTaTCivilite(taTCivilite);
				
				tiers.setNomTiers("DELMOND FOIE GRAS");
				tiers.setPrenomTiers("");
				
				adresse = new TaAdresse();
				adresse.setAdresse1Adresse("");
				adresse.setAdresse2Adresse("");
				adresse.setAdresse3Adresse("");
				adresse.setCodepostalAdresse("24200");
				adresse.setVilleAdresse("VEZAC");
				adresse.setPaysAdresse("France");
				adresse.setCommAdministratifAdresse(0);
				adresse.setCommCommercialAdresse(0);
				tiers.addAdresse(adresse);
				adresse.setTaTiers(tiers);
				
				telephone = new TaTelephone();
				telephone.setNumeroTelephone("0553025432");				
				telephone.setTaTTel(taTTelService.findByCode("S"));
				tiers.addTelephone(telephone);
				telephone.setTaTiers(tiers);

				
				email = new TaEmail();
				email.setAdresseEmail("");
				tiers.addEmail(email);
				email.setTaTiers(tiers);
				
				web = new TaWeb();
				web.setAdresseWeb("");
				tiers.addWeb(web);
				web.setTaTiers(tiers);

				tiers = taTiersService.merge(tiers,ITaTiersServiceRemote.validationContext);
				
				
				/******************/
				tiers = new TaTiers();

				tiers.setCodeTiers(taTiersService.genereCode(null)); 

				tiers.setCodeCompta(tiers.getCodeTiers());
				tiers.setCompte("401"); //ejb

				taTTiers = taTTiersService.findByCode("C");
				tiers.setCompte(taTTiers.getCompteTTiers());
				tiers.setTaTTiers(taTTiers);
				
				tiers.setTtcTiers(0);
				
				tiers.setActifTiers(1);
				
				commentaire=new TaCommentaire();
				commentaire.setCommentaire("Fournisseur : sel / amidon");
				tiers.setTaCommentaire(commentaire);

				taTTvaDoc = taTTvaDocService.findByCode("F");
				tiers.setTaTTvaDoc(taTTvaDoc);
				
				taTEntite = taTEntiteService.findByCode("SAS");
				tiers.setTaTEntite(taTEntite);
				
//				TaTCivilite taTCivilite = taTCivlicteService.findByCode("F");
//				tiers.setTaTCivilite(taTCivilite);
				
				tiers.setNomTiers("GACHES CHIMIE");
				tiers.setPrenomTiers("");
				
				adresse = new TaAdresse();
				adresse.setAdresse1Adresse("Avenue de la Gare");
				adresse.setAdresse2Adresse("");
				adresse.setAdresse3Adresse("");
				adresse.setCodepostalAdresse("31750");
				adresse.setVilleAdresse("ESCALQUENS");
				adresse.setPaysAdresse("France");
				adresse.setCommAdministratifAdresse(0);
				adresse.setCommCommercialAdresse(0);
				tiers.addAdresse(adresse);
				adresse.setTaTiers(tiers);
				
				telephone = new TaTelephone();
				telephone.setNumeroTelephone("0562719595");				
				telephone.setTaTTel(taTTelService.findByCode("S"));
				tiers.addTelephone(telephone);
				telephone.setTaTiers(tiers);
				
				
//				telephone = new TaTelephone();
//				telephone.setNumeroTelephone("0561814372");				
//				telephone.setTaTTel(taTTelService.findByCode("FAX"));
//				tiers.addTelephone(telephone);
//				telephone.setTaTiers(tiers);
				
				
				email = new TaEmail();
				email.setAdresseEmail("");
				tiers.addEmail(email);
				email.setTaTiers(tiers);
				
				web = new TaWeb();
				web.setAdresseWeb("");
				tiers.addWeb(web);
				web.setTaTiers(tiers);

				tiers = taTiersService.merge(tiers,ITaTiersServiceRemote.validationContext);
				
				

//			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void article() {
		try {
			for (int i = 0; i < nbArticle; i++) {
				TaArticle article = new TaArticle();

				article.setCodeArticle(taArticleService.genereCode(null));
				article.setLibellecArticle("Article "+article.getCodeArticle());
				article.setLibellelArticle("Libellé article "+article.getCodeArticle());
				article.setActif(1);
				article.setMatierePremiere(true);
				article.setProduitFini(true);
				article.setParamDluo("30");
				article.setCodeBarre("123456789123");

				//U1 et Prix defaut
				TaPrix prix = new TaPrix();
				prix.setPrixPrix(new BigDecimal(0));
				prix.setPrixttcPrix(new BigDecimal(0));
				TaUnite u1 = taUniteService.findByCode("L");
				article.setTaUnite1(u1);
				article.setTaPrix(prix);

				//Famille
				TaFamille f = taFamilleService.findByCode("F1");
				article.setTaFamille(f);

				//Rapport unité
				TaRapportUnite rapport = new TaRapportUnite();
				rapport.setTaArticle(article);
				rapport.setTaUnite1(u1);
				TaUnite u2 = taUniteService.findByCode("Kg");
				rapport.setTaUnite2(u2);
				rapport.setRapport(new BigDecimal(2));
				rapport.setNbDecimal(2);
				rapport.setSens(0);
				article.setTaRapportUnite(rapport);

				article.setStockMinArticle(new BigDecimal(-1));

				article = taArticleService.merge(article,ITaArticleServiceRemote.validationContext);
				nbFait++;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void tiers() {
		try {
			for (int i = 0; i < nbTiers; i++) {
				TaTiers tiers = new TaTiers();

				tiers.setCodeTiers(taTiersService.genereCode(null)); 

				tiers.setCodeCompta(tiers.getCodeTiers());
				tiers.setCompte("111"); //ejb

				TaTTiers taTTiers = taTTiersService.findByCode("C");
				tiers.setCompte(taTTiers.getCompteTTiers());
				tiers.setTaTTiers(taTTiers);
				
				tiers.setTtcTiers(0);
				
				tiers.setActifTiers(1);

				TaTTvaDoc taTTvaDoc = taTTvaDocService.findByCode("F");
				tiers.setTaTTvaDoc(taTTvaDoc);
				
//				TaTEntite taTEntite = taTEntiteService.findByCode("F");
//				tiers.setTaTEntite(taTEntite);
				
//				TaTCivilite taTCivilite = taTCivlicteService.findByCode("F");
//				tiers.setTaTCivilite(taTCivilite);
				
				tiers.setNomTiers("Tiers "+tiers.getCodeTiers());
				tiers.setPrenomTiers("prénom tiers "+i);
				
				TaAdresse adresse = new TaAdresse();
				adresse.setAdresse1Adresse("adr "+(1+i));
				adresse.setAdresse2Adresse("adr "+(2+i));
				adresse.setAdresse3Adresse("adr "+(3+i));
				adresse.setCodepostalAdresse("82000");
				adresse.setVilleAdresse("Ville "+i);
				adresse.setPaysAdresse("France");
				adresse.setCommAdministratifAdresse(0);
				adresse.setCommCommercialAdresse(0);
				tiers.addAdresse(adresse);
				
				
				TaTelephone telephone = new TaTelephone();
				telephone.setNumeroTelephone("0000000000");
				tiers.addTelephone(telephone);
				
				TaEmail email = new TaEmail();
				email.setAdresseEmail("adresse@site-"+i+".com");
				tiers.addEmail(email);
				
				TaWeb web = new TaWeb();
				web.setAdresseWeb("http://site-"+i+".com");
				tiers.addWeb(web);
				

				tiers = taTiersService.merge(tiers,ITaTiersServiceRemote.validationContext);
				nbFait++;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void bonDeReceptionReel() {
		try {
			TaBonReception taBonReception = new TaBonReception();
			taBonReception.setCodeDocument(taBonReceptionService.genereCode(null));
			taBonReception.setDateDocument(new Date());
			taBonReception.setLibelleDocument("Bon de réception n°: "+taBonReception.getCodeDocument());
			taBonReception.setDateLivDocument(taBonReception.getDateDocument());
			taBonReception.setCommentaire("67392 boite 600g + fonds");
			taBonReception.setTaTiers(taTiersService.findByCode("COPROMETAL"));
			taBonReception.setTaTReception(null);
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void bonDeReception() {
		try {
			for (int i = 0; i < nbBR; i++) {
				
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void fabrication() {
		try {
			for (int i = 0; i < nbFAB; i++) {
				
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void controleConf() {
		try {
			for (int i = 0; i < nbControleConf; i++) {
				
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
