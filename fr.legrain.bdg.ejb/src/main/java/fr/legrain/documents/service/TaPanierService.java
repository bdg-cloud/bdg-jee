package fr.legrain.documents.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.security.DeclareRoles;
import javax.ejb.CreateException;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.jws.WebMethod;

import org.apache.log4j.Logger;
import org.hibernate.OptimisticLockException;

import fr.legrain.abonnement.stripe.model.TaStripePaymentIntent;
import fr.legrain.article.dao.ITvaDAO;
import fr.legrain.article.model.TaArticle;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.controle.service.remote.ITaGenCodeExServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLPanierServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaPanierServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaTLigneServiceRemote;
import fr.legrain.bdg.edition.osgi.EditionProgrammeDefaut;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.model.mapping.mapper.TaPanierMapper;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaParamEspaceClientServiceRemote;
import fr.legrain.birt.BirtUtil;
import fr.legrain.data.AbstractDocumentService;
import fr.legrain.document.dashboard.dto.TaArticlesParTiersDTO;
import fr.legrain.document.dto.DocumentChiffreAffaireDTO;
import fr.legrain.document.dto.DocumentDTO;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.dto.TaLPanierDTO;
import fr.legrain.document.dto.TaPanierDTO;
import fr.legrain.document.model.LigneTva;
import fr.legrain.document.model.SWTDocument;
import fr.legrain.document.model.TaBoncde;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaInfosPanier;
import fr.legrain.document.model.TaLPanier;
import fr.legrain.document.model.TaMiseADisposition;
import fr.legrain.document.model.TaPanier;
import fr.legrain.document.model.TaRReglementLiaison;
import fr.legrain.document.model.TaReglement;
import fr.legrain.document.model.TaTLigne;
import fr.legrain.document.model.TaTPaiement;
import fr.legrain.documents.dao.IPanierDAO;
import fr.legrain.documents.dao.IReglementDAO;
import fr.legrain.dossier.model.TaPreferences;
import fr.legrain.droits.service.SessionInfo;
import fr.legrain.edition.model.TaActionEdition;
import fr.legrain.hibernate.multitenant.SchemaResolver;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.lib.data.IHMEtat;
import fr.legrain.lib.data.LibCalcul;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.LibDate;
import fr.legrain.tiers.dao.ICPaiementDAO;
import fr.legrain.tiers.dao.ITCPaiementDAO;
import fr.legrain.tiers.model.TaCPaiement;
import fr.legrain.tiers.model.TaParamEspaceClient;
import fr.legrain.tiers.model.TaTCPaiement;
import fr.legrain.tiers.model.TaTiers;

/**
 * Session Bean implementation class TaPanierBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaPanierService extends AbstractDocumentService<TaPanier, TaPanierDTO> implements ITaPanierServiceRemote {

	static Logger logger = Logger.getLogger(TaPanierService.class);

	@Inject private IPanierDAO dao;
	@Inject private ITvaDAO taTvaDAO;
	@Inject private ITCPaiementDAO taTCPaiementDAO;
	@Inject private ICPaiementDAO taCPaiementDAO;
	@Inject private IReglementDAO daoReglement;
	
	@Inject private	SessionInfo sessionInfo;
	
	@EJB private ITaGenCodeExServiceRemote gencode;
	@EJB private ITaParamEspaceClientServiceRemote taParamEspaceClientService;
	@EJB private ITaLPanierServiceRemote taLPanierService;
	@EJB private ITaArticleServiceRemote taArticleService;
	@EJB private ITaTLigneServiceRemote taTLigneService;
	
	@EJB private ITaTiersServiceRemote taTiersService;
	
	private LgrDozerMapper<TaPanierDTO,TaPanier> mapperUIToModel = new LgrDozerMapper<>();
	private LgrDozerMapper<TaPanier,TaPanierDTO> mapperModelToUI = new LgrDozerMapper<>();
	
	/**
	 * Default constructor. 
	 */
	public TaPanierService() {
		super(TaPanier.class,TaPanierDTO.class);
		editionDefaut = EditionProgrammeDefaut.EDITION_DEFAUT_PANIER_FICHIER;
	}
	
	public List<TaPanierDTO> findAllLight() {
		return dao.findAllLight();
	}
	
	public List<TaPanierDTO> findByCodeLight(String code) {
		return dao.findByCodeLight(code);
	}

	public TaPanier findByActif(String codeTiers) {
		return dao.findByActif(codeTiers);
	}
	
	public TaPanier findByActifAvecLiaisonReglement(String codeTiers) {
		return dao.findByActifAvecLiaisonReglement(codeTiers);
	}
	
	public TaBoncde findCommandePanier(String codePanier) {
		return dao.findCommandePanier(codePanier);
	}
	
	public TaReglement findReglementPourPanier(String codePanier) {
		return dao.findReglementPourPanier(codePanier);
	}
	
	public TaStripePaymentIntent findReglementStripePanier(String codePanier) {
		return dao.findReglementStripePanier(codePanier);
	}
	
	public TaPanierDTO recupereDerniereVersionPanierDTO(TaPanier entity) throws FinderException {
		TaPanierDTO dto = findByIdDTO(entity.getIdDocument());
		dto.setInfos(entity.getTaInfosDocument());
//		if(dto.getInfos()!=null) {
//			dto.getInfos().setTaDocument(null);
//		}
		TaLPanierDTO ldto = null;
		dto.setLignesDTO(new ArrayList<TaLPanierDTO>());
		for (TaLPanier l : entity.getLignes()) {
			ldto = taLPanierService.findByIdDTO(l.getIdLDocument());
			dto.getLignesDTO().add(ldto);
		}
		return dto;
	}
	
	public TaPanierDTO supprimerAjustementPrixVariable(int idPanier) {
		TaPanier t = new TaPanier();
		TaPanierDTO dto = new TaPanierDTO();

		try {  		

			TaParamEspaceClient taParamEspaceClient = taParamEspaceClientService.findInstance();
			t = findByIDFetch(idPanier);
			
			TaArticle articleVar = null;
			TaPreferences prefCodeArtVar = taPreferencesService.findByCode("code_article_pour_ajustement_prix_variable");
			if(prefCodeArtVar!=null && prefCodeArtVar.getValeur() != null) {
				articleVar = taArticleService.findByCode(prefCodeArtVar.getValeur());
			}
			
			TaPreferences prefPourcentVar = taPreferencesService.findByCode("pourcentage_prix_variable");
			Integer pourcentage = null;
			if(prefPourcentVar!=null && prefPourcentVar.getValeur() != null) {
				pourcentage = LibConversion.stringToInteger(prefPourcentVar.getValeur());
			}
			
			if(articleVar!=null) {//on a bien un paramétrage de prix variable

				TaLPanier ligneASupprimer = null; 

				//suppression de la ligne de prix variable
				for (TaLPanier ligne : t.getLignes()) {
					if(ligne.getTaArticle()!=null && ligne.getTaArticle().getCodeArticle().equals(articleVar.getCodeArticle())) {
						ligneASupprimer = ligne;
						break;
					}
				}
				if(ligneASupprimer!=null) {
					t.removeLigne(ligneASupprimer);
//					ligneASupprimer.setTaDocument(null);
				} else {
					// pas de ligne a supprimer => erreur
				}
				t.calculeTvaEtTotaux();
				
				//suppression du commentaire sur les prix variables
				for (TaLPanier ligne : t.getLignes()) {
					if(ligne.getTaTLigne()!=null && ligne.getTaTLigne().getCodeTLigne().equals("C")) {
						if(ligne.getLibLDocument().startsWith(TaPanier.debutCommentairePrixVariable)) {
							ligneASupprimer = ligne;
							break;
						}
					}
				}

				if(ligneASupprimer!=null) {
					t.removeLigne(ligneASupprimer);
					//ligneASupprimer.setTaDocument(null);
				} else {
					// pas de ligne a supprimer => erreur
				}
				
				
				t = merge(t);
			}

			dto = recupereDerniereVersionPanierDTO(t);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dto;
	}
	
	public TaPanierDTO supprimerFdpFixe(int idPanier) {
		TaPanier t = new TaPanier();
		TaPanierDTO dto = new TaPanierDTO();

		try {  		

			TaParamEspaceClient taParamEspaceClient = taParamEspaceClientService.findInstance();
			t = findByIDFetch(idPanier);
			if(taParamEspaceClient.getTaArticleFdp()!=null 
					&& taParamEspaceClient.getTaArticleFdp().getCodeArticle()!=null 
					&& !taParamEspaceClient.getTaArticleFdp().getCodeArticle().equals("")) {

				TaLPanier ligneASupprimer = null; 

				for (TaLPanier ligne : t.getLignes()) {
					if(ligne.getTaArticle()!=null && ligne.getTaArticle().getCodeArticle().equals(taParamEspaceClient.getTaArticleFdp().getCodeArticle())) {
						ligneASupprimer = ligne;
						break;
					}
				}
				if(ligneASupprimer!=null) {
					t.removeLigne(ligneASupprimer);
					//ligneASupprimer.setTaDocument(null);
				} else {
					// pas de ligne a supprimer => erreur
				}
				t.calculeTvaEtTotaux();
				t = merge(t);
			}

			dto = recupereDerniereVersionPanierDTO(t);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dto;
	}
	
	public TaPanierDTO supprimerLigneCommentaireRetrait(int idPanier) {
		TaPanier t = new TaPanier();
		TaPanierDTO dto = new TaPanierDTO();

		try {  		

			TaParamEspaceClient taParamEspaceClient = taParamEspaceClientService.findInstance();
			t = findByIDFetch(idPanier);
			if(taParamEspaceClient.getActivePlanningRetrait()!=null && taParamEspaceClient.getActivePlanningRetrait()) {

				TaLPanier ligneASupprimer = null; 
				boolean trouve = false;
				for (TaLPanier ligne : t.getLignes()) {
					if(ligne.getTaTLigne()!=null && ligne.getTaTLigne().getCodeTLigne().equals("C")) {
						if(ligne.getLibLDocument().startsWith(TaPanier.debutCommentaireRetrait)) {
							ligneASupprimer = ligne;
							break;
						}
					}
				}

				if(ligneASupprimer!=null) {
					t.removeLigne(ligneASupprimer);
					ligneASupprimer.setTaDocument(null);
				} else {
					// pas de ligne a supprimer => erreur
				}
				t = merge(t);
			}

			dto = recupereDerniereVersionPanierDTO(t);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dto;
	}
	
	public TaPanierDTO supprimerOptionsPanier(int idPanier) {
		TaPanierDTO dto = null;
		dto = supprimerAjustementPrixVariable(idPanier);
		dto = supprimerFdpFixe(idPanier);
		dto = supprimerLigneCommentaireRetrait(idPanier);
		return dto;
	}
	
	public TaPanierDTO ajouteLignePrixVariable(int idPanier) {
		TaPanier t = new TaPanier();
		TaPanierDTO dto = new TaPanierDTO();

		try {  		
			t = findByIDFetch(idPanier);
			TaArticle articleVar = null;
			TaPreferences prefCodeArtVar = taPreferencesService.findByCode("code_article_pour_ajustement_prix_variable");
			if(prefCodeArtVar!=null && prefCodeArtVar.getValeur() != null) {
				articleVar = taArticleService.findByCode(prefCodeArtVar.getValeur());
			}
			
			TaPreferences prefPourcentVar = taPreferencesService.findByCode("pourcentage_prix_variable");
			Integer pourcentage = null;
			if(prefPourcentVar!=null && prefPourcentVar.getValeur() != null) {
				pourcentage = LibConversion.stringToInteger(prefPourcentVar.getValeur());
			}
			
			if(pourcentage!=null && articleVar!=null) {//on a bien un paramétrage de prix variable
				BigDecimal totalMajorationPrixVariable = BigDecimal.ZERO;
				boolean auMoinsUnArticleAPrixVariableDansDocument = false;
				for (TaLPanier lp : t.getLignes()) {
					if(lp.getTaArticle()!=null && lp.getTaArticle().getPrixVariable()!=null && lp.getTaArticle().getPrixVariable() && lp.getMtHtLDocument()!=null) {
						totalMajorationPrixVariable = totalMajorationPrixVariable.add(lp.getMtHtLDocument());
						auMoinsUnArticleAPrixVariableDansDocument = true;
					}
				}
				totalMajorationPrixVariable = totalMajorationPrixVariable.multiply(new BigDecimal(pourcentage)).divide(new BigDecimal(100));
				
				boolean trouve = false;
				for (TaLPanier ligne : t.getLignes()) {
					if(ligne.getTaArticle()!=null && ligne.getTaArticle().getCodeArticle().equals(articleVar.getCodeArticle())) {
						trouve = true; //il y a deja une ligne pour les prix variable
						break;
					}
				}

				if(!trouve && auMoinsUnArticleAPrixVariableDansDocument) {
					TaTLigne taTLigne = taTLigneService.findByCode(SWTDocument.C_CODE_T_LIGNE_H);
					TaLPanier nouvelleLignePrixVariable = new TaLPanier();

					nouvelleLignePrixVariable.setTaDocument(t);
					nouvelleLignePrixVariable.setTaTLigne(taTLigne);
					nouvelleLignePrixVariable.setTaArticle(articleVar);
					nouvelleLignePrixVariable.setQteLDocument(BigDecimal.ONE);
					nouvelleLignePrixVariable.setLibLDocument(articleVar.getLibellecArticle());
					nouvelleLignePrixVariable.setLibLDocument("Ajustement de prix variable");

					nouvelleLignePrixVariable.setPrixULDocument(totalMajorationPrixVariable);
					

					nouvelleLignePrixVariable.setCodeTvaLDocument(nouvelleLignePrixVariable.getTaArticle().getTaTva().getCodeTva());
					nouvelleLignePrixVariable.setTauxTvaLDocument(nouvelleLignePrixVariable.getTaArticle().getTaTva().getTauxTva());

					nouvelleLignePrixVariable.calculMontant();

					t.addLigne(nouvelleLignePrixVariable);
					
					
					TaTLigne taTLigne2 = taTLigneService.findByCode("C");

					//    		    		TaLPanier ligneCommentaireSeparation = new TaLPanier();
					//    		    		ligneCommentaireSeparation.setTaDocument(t);
					//    		    		ligneCommentaireSeparation.setTaTLigne(taTLigne);
					//    		    		t.addLigne(ligneCommentaireSeparation);

					TaLPanier ligneCommentaireRetrait = new TaLPanier();
					ligneCommentaireRetrait.setTaDocument(t);
					ligneCommentaireRetrait.setTaTLigne(taTLigne2);
		
					ligneCommentaireRetrait.setLibLDocument(TaPanier.debutCommentairePrixVariable+
							" certains articles ont une quantité/poids qui n'est pas fixe, un prix maximum ("+pourcentage+"% de plus) est appliqué sur le montant de ces lignes."
									+ "Le prix réel sera prélevé au moment de la préparation de la commande et sera indiqué sur la facture.");
					
					t.addLigne(ligneCommentaireRetrait);

				}
			} // else pas de ligne a ajouter

			t.calculeTvaEtTotaux();
			t = merge(t);

			dto = recupereDerniereVersionPanierDTO(t);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dto;
	}
	
	public TaPanierDTO supprimeCommentaireDateRetrait(int idPanier) {
		return null;
	}
	
	public TaPanierDTO ajouteFdpFixePanier(int idPanier) {
		TaPanier t = new TaPanier();
		TaPanierDTO dto = new TaPanierDTO();

		try {  		
			t = findByIDFetch(idPanier);
			TaParamEspaceClient taParamEspaceClient = taParamEspaceClientService.findInstance();
			if(taParamEspaceClient.getTaArticleFdp()!=null) {
				boolean trouve = false;
				for (TaLPanier ligne : t.getLignes()) {
					if(ligne.getTaArticle()!=null && ligne.getTaArticle().getCodeArticle().equals(taParamEspaceClient.getTaArticleFdp().getCodeArticle())) {
						trouve = true;
						break;
					}
				}

				if(!trouve) {
					TaTLigne taTLigne = taTLigneService.findByCode(SWTDocument.C_CODE_T_LIGNE_H);
					TaLPanier nouvelleLigneFdp = new TaLPanier();

					nouvelleLigneFdp.setTaDocument(t);
					nouvelleLigneFdp.setTaTLigne(taTLigne);
					nouvelleLigneFdp.setTaArticle(taParamEspaceClient.getTaArticleFdp());
					nouvelleLigneFdp.setQteLDocument(BigDecimal.ONE);
					nouvelleLigneFdp.setLibLDocument(taParamEspaceClient.getTaArticleFdp().getLibellecArticle());
					nouvelleLigneFdp.setLibLDocument("Frais de port");

					nouvelleLigneFdp.setPrixULDocument(taParamEspaceClient.getTaArticleFdp().getTaPrix().getPrixPrix());
					if(taParamEspaceClient.getFraisPortLimiteOffert()!=null) {
						if(t.getNetTtcCalc().compareTo(taParamEspaceClient.getFraisPortLimiteOffert())>=0) {
							//frais de port offert
							nouvelleLigneFdp.setPrixULDocument(BigDecimal.ZERO);
						} 
					}

					nouvelleLigneFdp.setCodeTvaLDocument(nouvelleLigneFdp.getTaArticle().getTaTva().getCodeTva());
					nouvelleLigneFdp.setTauxTvaLDocument(nouvelleLigneFdp.getTaArticle().getTaTva().getTauxTva());

					nouvelleLigneFdp.calculMontant();

					t.addLigne(nouvelleLigneFdp);

				}
			} // else pas de frais de port a ajouter

			t.calculeTvaEtTotaux();
			t = merge(t);

			dto = recupereDerniereVersionPanierDTO(t);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return dto;
	}
	public TaPanierDTO ajouteCommentaireDateRetrait(int idPanier, Date dateTimeMillis) {
		TaPanier t = new TaPanier();
		TaPanierDTO dto = new TaPanierDTO();

		try {  		
			t = findByIDFetch(idPanier);
			TaParamEspaceClient taParamEspaceClient = taParamEspaceClientService.findInstance();
			if(taParamEspaceClient.getActivePlanningRetrait()!=null && taParamEspaceClient.getActivePlanningRetrait()) {
				boolean trouve = false;
				for (TaLPanier ligne : t.getLignes()) {
					if(ligne.getTaTLigne()!=null && ligne.getTaTLigne().getCodeTLigne().equals("C")) {
						if(ligne.getLibLDocument().startsWith(TaPanier.debutCommentaireRetrait)) {
							trouve = true;
							break;
						}
					}
				}

				if(!trouve) {
					TaTLigne taTLigne = taTLigneService.findByCode("C");

					//    		    		TaLPanier ligneCommentaireSeparation = new TaLPanier();
					//    		    		ligneCommentaireSeparation.setTaDocument(t);
					//    		    		ligneCommentaireSeparation.setTaTLigne(taTLigne);
					//    		    		t.addLigne(ligneCommentaireSeparation);

					TaLPanier ligneCommentaireRetrait = new TaLPanier();
					ligneCommentaireRetrait.setTaDocument(t);
					ligneCommentaireRetrait.setTaTLigne(taTLigne);
					if(dateTimeMillis!=null) {
						String dateTimeMillisString = LibDate.dateToStringTimeStampSansSeconde(dateTimeMillis,"/"," ",":");
						ligneCommentaireRetrait.setLibLDocument(TaPanier.debutCommentaireRetrait+dateTimeMillisString);
					}

					t.addLigne(ligneCommentaireRetrait);

				}
			} // else pas de date de retrait a ajouter

			t.calculeTvaEtTotaux();
			t = merge(t);

			dto = recupereDerniereVersionPanierDTO(t);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return dto;
	}
	
	public TaPanierDTO supprimeFdpFixePanier(int idPanier) {
		return null;
	}
	
	public TaPanierDTO resumerPanier(int idPanier, String selectedTypeExpedition, Date dateRetrait) {
		
		TaPanierDTO dto = null;
		dto = ajouteLignePrixVariable(idPanier);
		 if(selectedTypeExpedition.equals("2")) {
//			 supprimeCommentaireDateRetrait(idPanier);
			 dto = supprimerLigneCommentaireRetrait(idPanier);
			 dto = ajouteFdpFixePanier(idPanier);
		 } else {
			 dto = ajouteCommentaireDateRetrait(idPanier, dateRetrait);
//			 supprimeFdpFixePanier(idPanier);
			 dto = supprimerFdpFixe(idPanier);
		 }
		return dto;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public TaPanier getPanierActifByCodeTier(String codeTiers) {
		TaPanier panier = null;
		TaPanierDTO panierDTO = null;
		panier = dao.findByActif(codeTiers);
//		if(panier == null) {
//			
//		
//			//pas de panier actif il faut créer un nouveau panier vide
//			panierDTO = new TaPanierDTO();
//			TaInfosPanier taInfosDocument = null;
//			panier = new TaPanier();
//			TaTiers tiers;
//			try {
//				tiers = taTiersService.findByCode(codeTiers);
//			} catch (FinderException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			if(taInfosDocument==null) {
//				taInfosDocument = new TaInfosPanier();
//			}
//			taInfosDocument.setTaDocumentGeneral(panier);
//			panier.setTaInfosDocument(taInfosDocument);
//
//			panierDTO.setCodeDocument(genereCode(null)); //ejb
//
//			panierDTO.setIdTiers(tiers.getIdTiers());
//			panierDTO.setCodeTiers(tiers.getCodeTiers());
//			panier.setTaTiers(tiers);
//			panierDTO.setCodeCompta(panier.getTaTiers().getCodeCompta());
//			panierDTO.setCompte(panier.getTaTiers().getCompte());
//			panier.getTaInfosDocument().setCodeCompta(panier.getTaTiers().getCodeCompta());
//			panier.getTaInfosDocument().setCompte(panier.getTaTiers().getCompte());
//			
//			panierDTO.setLibelleDocument("Panier boutique");
//			
//			panierDTO.setLignesDTO(new ArrayList<TaLPanierDTO>());
//			
//			mapperUIToModel.map(panierDTO, panier);
//		}
		
		return panier;
		
		
	}
	public String genereCode( Map<String, String> params) {
		//return dao.genereCode();
		try {
			return gencode.genereCodeJPA(TaPanier.class.getSimpleName(),params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "NOUVEAU CODE";
	}
	
	public void annuleCode(String code) {
		try {
			
			gencode.annulerCodeGenere(gencode.findByCode(TaPanier.class.getSimpleName()),code);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void verrouilleCode(String code) {
		try {
			gencode.rentreCodeGenere(gencode.findByCode(TaPanier.class.getSimpleName()),code, sessionInfo.getSessionID());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Date calculDateEcheance(TaPanier doc, Integer report, Integer finDeMois,TaTPaiement taTPaiement) {
		TaTCPaiement typeCP = taTCPaiementDAO.findByCode(TaTCPaiement.C_CODE_TYPE_PANIER);
		TaCPaiement conditionDoc = null;
		TaCPaiement conditionTiers = null;
		TaCPaiement conditionSaisie = null;
		TaCPaiement conditionTPaiement = null;
		
		if(typeCP!=null) conditionDoc = typeCP.getTaCPaiement();
		List<TaCPaiement> liste=taCPaiementDAO.rechercheParType(typeCP.getCodeTCPaiement());
		if(liste!=null && !liste.isEmpty())conditionDoc=liste.get(0);
		if(doc.getTaTiers()!=null) conditionTiers = doc.getTaTiers().getTaCPaiement();
		if(taTPaiement!=null){
			conditionTPaiement=new TaCPaiement();
			conditionTPaiement.setReportCPaiement(taTPaiement.getReportTPaiement());
			conditionTPaiement.setFinMoisCPaiement(taTPaiement.getFinMoisTPaiement());
		}
		if(report!=null || finDeMois!=null) { 
			conditionSaisie = new TaCPaiement();
			if(report!=null)
				conditionSaisie.setReportCPaiement(report);
			if(finDeMois!=null)
				conditionSaisie.setFinMoisCPaiement(finDeMois);
		}
		
		//on applique toute les conditions par ordre décroissant de priorité, la derniere valide est conservée
		Date nouvelleDate = doc.getDateDocument();
		if(conditionDoc!=null) {
			nouvelleDate = conditionDoc.calculeNouvelleDate(doc.getDateDocument());
		}
		if(conditionTPaiement!=null){
			nouvelleDate = conditionTPaiement.calculeNouvelleDate(doc.getDateDocument());
		}
		if(conditionTiers!=null) {
			nouvelleDate = conditionTiers.calculeNouvelleDate(doc.getDateDocument());
		}
		if(conditionSaisie!=null) {
			nouvelleDate = conditionSaisie.calculeNouvelleDate(doc.getDateDocument());
		}
//		doc.setDateEchDocument(nouvelleDate);
		doc.setDateLivDocument(nouvelleDate);
		return nouvelleDate;
	}
	
	public void calculeTvaEtTotaux(TaPanier doc){
		calculTvaTotal(doc);
		calculTotaux(doc);
	}
	
	/**
	 * Calcul des totaux de la facture
	 */
	public void calculTotaux(TaPanier doc) {
		
//			    MT_TVA Numeric(15,2),
			doc.setMtHtCalc(new BigDecimal(0));
			doc.setNetHtCalc(new BigDecimal(0));
			doc.setMtTtcCalc(new BigDecimal(0));
			doc.setMtTtcAvantRemiseGlobaleCalc(new BigDecimal(0));
			for (Object ligne : doc.getLignes()) {
				if(((TaLPanier)ligne).getTaTLigne().getCodeTLigne().equals(SWTDocument.C_CODE_T_LIGNE_H)) {
					if(((TaLPanier)ligne).getMtHtLApresRemiseGlobaleDocument()!=null)
						doc.setNetHtCalc(doc.getNetHtCalc().add(((TaLPanier)ligne).getMtHtLApresRemiseGlobaleDocument()));
					if(((TaLPanier)ligne).getMtTtcLApresRemiseGlobaleDocument()!=null)
						doc.setMtTtcCalc(doc.getMtTtcCalc().add(((TaLPanier)ligne).getMtTtcLApresRemiseGlobaleDocument()));
					if(((TaLPanier)ligne).getMtHtLDocument()!=null)
						doc.setMtHtCalc(doc.getMtHtCalc().add(((TaLPanier)ligne).getMtHtLDocument()));
					if(((TaLPanier)ligne).getMtTtcLDocument()!=null)
						doc.setMtTtcAvantRemiseGlobaleCalc(doc.getMtTtcAvantRemiseGlobaleCalc().add(((TaLPanier)ligne).getMtTtcLDocument()));
				}
				
			}
			
			doc.setNetTvaCalc(doc.getMtTtcCalc().subtract(doc.getNetHtCalc()));
			BigDecimal tva = new BigDecimal(0);
			for (LigneTva ligneTva : doc.getLignesTVA()) {
				tva = tva.add(ligneTva.getMtTva());
			}
			if(tva.compareTo(doc.getNetTvaCalc())!=0) {
				logger.error("Montant de la TVA incorrect : "+doc.getNetTvaCalc()+" ** "+tva);
			}
			
			//setNetTtcCalc(getMtTtcCalc().subtract(getMtTtcCalc().multiply(getRemTtcFacture().divide(new BigDecimal(100)))));
			//setNetTtcCalc(getMtTtcCalc().subtract(getMtTtcCalc().multiply(getTxRemTtcDocument().divide(new BigDecimal(100)))));
			doc.setNetTtcCalc(doc.getMtTtcCalc().subtract(doc.getMtTtcCalc().multiply(doc.getTxRemTtcDocument().divide(new BigDecimal(100))).setScale(2,BigDecimal.ROUND_HALF_UP)));
			
			/*
			 * remise HT déjà calculée dans dispatcherTva()
			 */
			//setRemTtcDocument(getMtTtcCalc().subtract(getNetTtcCalc()));
			doc.setRemTtcDocument(doc.getMtTtcCalc().subtract(doc.getNetTtcCalc()).setScale(2,BigDecimal.ROUND_HALF_UP));
			
			doc.setNetAPayer(doc.getNetTtcCalc());
			
			//TODO A Finir ou a supprimer
//			select sum(f.mt_tva_recup) from calcul_tva_direct(:module,:id_document,:taux_r_ht,:ttc) f into :MTNETTVA;
//			tva=:mtnettva;
//			mt_ttc=:totalttc;
//			mt_tva=:mt_ttc-:mt_ht;
//			if (ttc=1) {
//			       txremiseht = taux_r_ht;
//			       mtnetttc=:mt_ttc - (:mt_ttc*(:txremiseht/100));
//			       MTNETHT=:mtnetttc - :MTNETTVA;
//			       remise_ht =  :totalttc - :mtnetttc ;
//			} else {
//			      txremiseht = taux_r_ht;
//			      MTNETHT=:mt_ht-(:mt_ht*(:txremiseht/100));
//			      mtnetttc=:MTNETHT + :MTNETTVA;
//			      remise_ht = mt_ht - mtnetht;
//			}
//			  txremisettc = taux_r_ttc;
//			  remise_ttc = (:mtnetttc * (:txremisettc/100));
//			  mtnetttc = :mtnetttc -:remise_ttc;
//			  netapayer = :mtnetttc - :regle;
	}

	/**
	 * Reparti le total chaque code TVA sur l'ensemble des lignes concernées par ce code. 
	 */
	public void dispatcherTva(TaPanier doc) {

		BigDecimal tvaLigne = new BigDecimal(0);
		BigDecimal totalTemp = new BigDecimal(0);

		for (Object ligne : doc.getLignes()) {
			if(((TaLPanier)ligne).getMtHtLDocument()!=null)
				totalTemp = totalTemp.add(((TaLPanier)ligne).getMtHtLDocument());
		}
		if(totalTemp!=null && doc.getTxRemHtDocument()!=null)
			doc.setRemHtDocument(totalTemp.multiply(doc.getTxRemHtDocument().divide(new BigDecimal(100))).setScale(2,BigDecimal.ROUND_HALF_UP));	
		
		for (TaLPanier ligne : doc.getLignes()) {
			if(doc.getTxRemHtDocument()!=null && doc.getTxRemHtDocument().signum()>0 && ligne.getMtHtLDocument()!=null  && ligne.getMtTtcLDocument()!=null) {
				if(doc.getTtc()==1){
					((TaLPanier)ligne).setMtTtcLApresRemiseGlobaleDocument(((TaLPanier)ligne).getMtTtcLDocument().subtract(((TaLPanier)ligne).getMtTtcLDocument()
							.multiply(doc.getTxRemHtDocument()).divide(new BigDecimal(100),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP)));
					((TaLPanier)ligne).setMtHtLApresRemiseGlobaleDocument(((TaLPanier)ligne).getMtTtcLApresRemiseGlobaleDocument());
					
				}else{
					((TaLPanier)ligne).setMtHtLApresRemiseGlobaleDocument(((TaLPanier)ligne).getMtHtLDocument().subtract(((TaLPanier)ligne).getMtHtLDocument()
							.multiply(doc.getTxRemHtDocument()).divide(new BigDecimal(100),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP)));
					((TaLPanier)ligne).setMtTtcLApresRemiseGlobaleDocument(((TaLPanier)ligne).getMtHtLApresRemiseGlobaleDocument());	
				}
			}
		}
				
		for (LigneTva ligneTva : doc.getLignesTVA()) {

			if (ligneTva.getMtTva()!=null) {
				int lignepasse=1;
				BigDecimal tvaTmp = ligneTva.getMtTva();
				BigDecimal ttcTmp = LibCalcul.arrondi(ligneTva.getMontantTotalTtcAvecRemise());
				BigDecimal htTmp = LibCalcul.arrondi(ligneTva.getMontantTotalHtAvecRemise());

				for (Object ligne :doc.getLignes()) {
					if(((TaLPanier)ligne).getTaTLigne().getCodeTLigne().equals(SWTDocument.C_CODE_T_LIGNE_H)) {
						if(((TaLPanier)ligne).getCodeTvaLDocument()!=null&&
								((TaLPanier)ligne).getCodeTvaLDocument().equals(ligneTva.getCodeTva())){
							if (ligneTva.getMontantTotalHt().signum()==0) 
								tvaLigne = ((TaLPanier)ligne).getMtHtLDocument().multiply(ligneTva.getTauxTva()).divide(new BigDecimal(100));
							else {
								if  (lignepasse>= ligneTva.getNbLigneDocument()) 
									tvaLigne = tvaTmp;
								else {
									if(doc.getTtc()==1){
										if(LibCalcul.arrondi(ligneTva.getMontantTotalTtcAvecRemise()).signum()<=0)
											tvaLigne=BigDecimal.valueOf(0);
										else
											tvaLigne = (ligneTva.getMtTva().multiply(((TaLPanier)ligne).getMtTtcLDocument())).divide(LibCalcul.arrondi(ligneTva.getMontantTotalTtcAvecRemise()),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP);
									}
									else{
										if(LibCalcul.arrondi(ligneTva.getMontantTotalHtAvecRemise()).signum()<=0)
											tvaLigne =BigDecimal.valueOf(0);
										else
											tvaLigne = (ligneTva.getMtTva().multiply(((TaLPanier)ligne).getMtHtLDocument())).divide(LibCalcul.arrondi(ligneTva.getMontantTotalHtAvecRemise()),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP);
									}
								}
							}
							tvaTmp =  tvaTmp.subtract(tvaLigne);
							totalTemp = totalTemp.add(((TaLPanier)ligne).getMtHtLDocument());

							if(doc.getTxRemHtDocument()!=null && doc.getTxRemHtDocument().signum()>0) {
								if  (lignepasse>= ligneTva.getNbLigneDocument()) {
									((TaLPanier)ligne).setMtHtLApresRemiseGlobaleDocument(htTmp);
									((TaLPanier)ligne).setMtTtcLApresRemiseGlobaleDocument(ttcTmp);
								} else {
									((TaLPanier)ligne).setMtHtLApresRemiseGlobaleDocument(((TaLPanier)ligne).getMtHtLDocument().subtract(((TaLPanier)ligne).getMtHtLDocument().multiply(doc.getTxRemHtDocument()).divide(new BigDecimal(100),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP)));
									((TaLPanier)ligne).setMtTtcLApresRemiseGlobaleDocument(((TaLPanier)ligne).getMtTtcLDocument().subtract(((TaLPanier)ligne).getMtTtcLDocument().multiply(doc.getTxRemHtDocument()).divide(new BigDecimal(100),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP)));
								}
//								ttcTmp =  ttcTmp.subtract(((TaLPanier)ligne).getMtTtcLFacture());
//								htTmp =  htTmp.subtract(((TaLPanier)ligne).getMtHtLFacture());
							} else {
								if(doc.getTtc()==1)
									if  (lignepasse>= ligneTva.getNbLigneDocument()) {
										((TaLPanier)ligne).setMtHtLApresRemiseGlobaleDocument(htTmp);
									}else{
										((TaLPanier)ligne).setMtHtLApresRemiseGlobaleDocument(((TaLPanier)ligne).getMtTtcLDocument().subtract(tvaLigne));
									}
								else
									if  (lignepasse>= ligneTva.getNbLigneDocument()) {
										((TaLPanier)ligne).setMtTtcLApresRemiseGlobaleDocument(ttcTmp);
									}else {
										((TaLPanier)ligne).setMtTtcLApresRemiseGlobaleDocument(((TaLPanier)ligne).getMtHtLDocument().add(tvaLigne));
									}

							}
							ttcTmp =  ttcTmp.subtract(((TaLPanier)ligne).getMtTtcLApresRemiseGlobaleDocument());
							htTmp =  htTmp.subtract(((TaLPanier)ligne).getMtHtLApresRemiseGlobaleDocument());

							lignepasse++;
						}
					}
					doc.setRemHtDocument(totalTemp.multiply(doc.getTxRemHtDocument().divide(new BigDecimal(100))));						

//					setRemHtDocument(getRemHtDocument().add(totalTemp.multiply(txRemHtDocument.divide(new BigDecimal(100)))));						

				}
			}
		}
//		}

	}
	
	/**
	 * Lance la fonction de calcul du montant sur chacunes des lignes du document.
	 */
	public void calculMontantLigneDocument(TaPanier doc) {
		for (Object ligne : doc.getLignes()) {
			((TaLPanier)ligne).calculMontant();
		}
	}
	
	/**
	 * Calcul de la grille de TVA en fonction de lignes du document et du taux de remise HT global.
	 * Mise à jour de la propriété <code>lignesTVA</code>
	 * et mise à jour du montant de la TVA dans les lignes du document
	 */
	public void calculTvaTotal(TaPanier doc) {
		calculMontantLigneDocument(doc);
		calculLignesTva(doc);
		dispatcherTva(doc);
	}
	
	/**
	 * Calcul de la grille de TVA en fonction de lignes du document et du taux de remise HT global.
	 * Mise à jour de la propriété <code>lignesTVA</code>
	 */
	public void calculLignesTva(TaPanier doc) {

		Map<String,BigDecimal> montantTotalHt = new HashMap<String,BigDecimal>();
		Map<String,BigDecimal> montantTotalTtc = new HashMap<String,BigDecimal>();
		Map<String,BigDecimal> montantTotalHtAvecRemise = new HashMap<String,BigDecimal>();
		Map<String,BigDecimal> montantTotalTtcAvecRemise = new HashMap<String,BigDecimal>();
		Map<String,BigDecimal> mtTVA = new HashMap<String,BigDecimal>();
		Map<String,BigDecimal> tauxTVA = new HashMap<String,BigDecimal>();
		Map<String,Integer> nbLigne = new HashMap<String,Integer>();
		String codeTVA = null;
		
		/*
		 * calcul de la TVA different en fonction de la propriete TTC
		 */
		BigDecimal ttcLigne = null;
		BigDecimal htLigne = null;
		for (Object ligne : doc.getLignes()) {
			//en commentaire pour ne pas refaire les calculs pendants les editions, 
			//((TaLPanier)ligne).calculMontant();
			codeTVA = ((TaLPanier)ligne).getCodeTvaLDocument();
			if(codeTVA!=null && !codeTVA.equals("")) {
				ttcLigne = ((TaLPanier)ligne).getMtTtcLDocument();
				htLigne = ((TaLPanier)ligne).getMtHtLDocument();
				if(montantTotalHt.containsKey(codeTVA)) {
					montantTotalTtc.put(codeTVA,montantTotalTtc.get(codeTVA).add(ttcLigne));
					montantTotalHt.put(codeTVA,montantTotalHt.get(codeTVA).add(htLigne));
					montantTotalTtcAvecRemise.put(codeTVA,montantTotalTtcAvecRemise.get(codeTVA).add(ttcLigne));
					montantTotalHtAvecRemise.put(codeTVA,montantTotalHtAvecRemise.get(codeTVA).add(htLigne));
					nbLigne.put(codeTVA,nbLigne.get(codeTVA)+1);
				} else {
					montantTotalTtc.put(codeTVA,ttcLigne);
					montantTotalHt.put(codeTVA,htLigne);
					montantTotalTtcAvecRemise.put(codeTVA,ttcLigne);
					montantTotalHtAvecRemise.put(codeTVA,htLigne);
					tauxTVA.put(codeTVA,((TaLPanier)ligne).getTauxTvaLDocument());
					nbLigne.put(codeTVA,1);
				}
			}
		}

		for (String codeTva : montantTotalTtc.keySet()) {
			//les 2 maps ont les meme cles
			BigDecimal mtTtcTotal = montantTotalTtc.get(codeTva);
			BigDecimal mtHtTotal = montantTotalHt.get(codeTva);
			BigDecimal tva =null;
			if(doc.getTxRemHtDocument()!=null && doc.getTxRemHtDocument().signum()>0) {
//				mtTtcTotal =LibCalcul.arrondi(mtTtcTotal.subtract(     mtTtcTotal.multiply(   txRemHtDocument.divide(new BigDecimal(100))  )       ));
//				mtHtTotal = LibCalcul.arrondi(mtHtTotal.subtract(    mtHtTotal.multiply( (txRemHtDocument.divide(new BigDecimal(100))))     ) ) ;
				BigDecimal valeurInterTTC=mtTtcTotal.multiply(   doc.getTxRemHtDocument().divide(new BigDecimal(100)),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP);
				mtTtcTotal =LibCalcul.arrondi(mtTtcTotal.subtract(valeurInterTTC )) ;
				BigDecimal valeurInterHT=mtHtTotal.multiply( doc.getTxRemHtDocument().divide(new BigDecimal(100)),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP);
				mtHtTotal = LibCalcul.arrondi(mtHtTotal.subtract( valeurInterHT )) ;
				montantTotalTtcAvecRemise.put(codeTva, mtTtcTotal);
				montantTotalHtAvecRemise.put(codeTva, mtHtTotal);
			} 

			if (doc.getTtc()==1) {
				tva=mtTtcTotal.subtract((mtTtcTotal.multiply(BigDecimal.valueOf(100))) .divide((BigDecimal.valueOf(100).add(tauxTVA.get(codeTva))) ,MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP)     ) ;
				mtTVA.put(codeTva, tva);
				montantTotalHtAvecRemise.put(codeTva, mtTtcTotal.subtract(tva));
			} else {
				tva=mtHtTotal.multiply(   (tauxTVA.get(codeTva).divide(new BigDecimal(100)))) ;
				mtTVA.put(codeTva, tva );
				montantTotalTtcAvecRemise.put(codeTva, mtHtTotal.add(tva));
			}
		}

		doc.getLignesTVA().clear();
		for (String codeTva : mtTVA.keySet()) {
			LigneTva ligneTva = new LigneTva();
			ligneTva.setCodeTva(codeTva);
			ligneTva.setTauxTva(tauxTVA.get(codeTva));
			ligneTva.setMtTva(mtTVA.get(codeTva));
			ligneTva.setMontantTotalHt(montantTotalHt.get(codeTva));
			ligneTva.setMontantTotalTtc(montantTotalTtc.get(codeTva));
			ligneTva.setMontantTotalHtAvecRemise(montantTotalHtAvecRemise.get(codeTva));
			ligneTva.setMontantTotalTtcAvecRemise(montantTotalTtcAvecRemise.get(codeTva));
			ligneTva.setLibelle(taTvaDAO.findByCode(codeTva).getLibelleTva());
			ligneTva.setNbLigneDocument(nbLigne.get(codeTva));
			doc.getLignesTVA().add(ligneTva);
		}
		
		//dispatcherTva();
	}
	

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaPanier transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaPanier transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}


	
	public TaPanier merge(TaPanier detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaPanier merge(TaPanier detachedInstance, String validationContext) {
		TaPanier taPanierInitial = detachedInstance;
		validateEntity(detachedInstance, validationContext);
		List<ILigneDocumentTiers> listeLien =null;
		TaPanier objInitial = detachedInstance;
		try {
			if(detachedInstance.getIdDocument()!=0)
				objInitial=findByIDFetch(detachedInstance.getIdDocument());
		} catch (FinderException e) {
			// TODO Auto-generated catch block
		}
		modifEtatLigne(detachedInstance);		
		TaEtat etat=changeEtatDocument(detachedInstance);
		if(etat!=null)detachedInstance.addREtat(etat);

		//récupération des liens avec le document d'origine en cours si ces liens sont les sources du document d'origine
		listeLien = recupListeLienLigneALigne(objInitial);
		//récupération des liens avec le document modifié en cours si ces liens sont les sources du document modifié 
		listeLien =recupListeLienLigneALigne(detachedInstance,listeLien);
		
		TaMiseADisposition miseADispo = detachedInstance.getTaMiseADisposition();
		
		//je détache les réglements avant d'enregistrer le panier
		List<TaRReglementLiaison> listeReglementTmp=new LinkedList<>();
		for (TaRReglementLiaison ligne : detachedInstance.getTaRReglementLiaisons()) {
			listeReglementTmp.add(ligne);
		}
		detachedInstance.getTaRReglementLiaisons().clear();		
		for (TaRReglementLiaison taRReglement : listeReglementTmp) {
			taRReglement.setTaPanier(null);
		}
		//j'enregistre
		detachedInstance=dao.merge(detachedInstance);
		
		//je rattache les réglements à la facture
		for (TaRReglementLiaison taRReglement : listeReglementTmp) {
			taRReglement.setTaPanier(detachedInstance);
			detachedInstance.addRReglementLiaison(taRReglement);
		}
		//j'enregistre les réglements
		TaReglement reglement=null;
		for (TaRReglementLiaison ligne : detachedInstance.getTaRReglementLiaisons()) {
			reglement=ligne.getTaReglement();
			if(reglement!=null && reglement.getIdDocument()==0){
				reglement.getTaRReglementLiaisons().remove(ligne);
				reglement=daoReglement.merge(ligne.getTaReglement());
//				reglement.addRReglementLiaison(ligne);
				ligne.setTaReglement(reglement);
			}
		}
		for (TaRReglementLiaison ligne : detachedInstance.getTaRReglementLiaisons()) {
			if(ligne.getTaReglement()!=null) {
				if(ligne.getEtat()==IHMEtat.integre) {
					ligne.setTaMiseADisposition(miseADispo);
					ligne.getTaReglement().setTaMiseADisposition(miseADispo);
				}
				if((ligne.getEtatDeSuppression()==IHMEtat.suppression)){
					ligne.getTaReglement().removeReglementLiaison(ligne);
				}
				
				reglement=daoReglement.merge(ligne.getTaReglement());
				ligne.setTaPanier(detachedInstance);
				ligne.setTaReglement(reglement);
			}
		}

		
		
		detachedInstance=dao.merge(detachedInstance);
		
		//merger les documents source lié au document en cours d'enregistrement
		mergeEntityLieParLigneALigne(listeLien);
		
		annuleCode(detachedInstance.getCodeDocument());
		return detachedInstance;
	}
	
	public TaPanier mergeAndFindById(TaPanier detachedInstance, String validationContext) {
		TaPanier br = merge(detachedInstance,validationContext);
		try {
			br = findById(br.getIdDocument()); //pour etre sur que les valeur initialisé par les triggers "qui_cree,..." soit bien affecté avant une éventuelle nouvelle modification du document
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return br;
	}

	public TaPanier findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaPanier findByCode(String code) throws FinderException {
		return dao.findByCode(code);

	}

//	@RolesAllowed("admin")
	public List<TaPanier> selectAll() {
		return dao.selectAll();

	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaPanierDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaPanierDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaPanier> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaPanierDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaPanierDTO entityToDTO(TaPanier entity) {
//		TaPanierDTO dto = new TaPanierDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaPanierMapper mapper = new TaPanierMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaPanierDTO> listEntityToListDTO(List<TaPanier> entity) {
		List<TaPanierDTO> l = new ArrayList<TaPanierDTO>();

		for (TaPanier taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaPanierDTO> selectAllDTO() {
		System.out.println("List of TaPanierDTO EJB :");
		ArrayList<TaPanierDTO> liste = new ArrayList<TaPanierDTO>();

		List<TaPanier> projects = selectAll();
		for(TaPanier project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaPanierDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaPanierDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaPanierDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaPanierDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaPanierDTO dto, String validationContext) throws EJBException {
		try {
			TaPanierMapper mapper = new TaPanierMapper();
			TaPanier entity = null;
			if(dto.getId()!=null) {
				entity = dao.findById(dto.getId());
				if(dto.getVersionObj()!=entity.getVersionObj()) {
					throw new OptimisticLockException(entity,
							"L'objet à été modifié depuis le dernier accés. Client ID : "+dto.getId()+" - Client Version objet : "+dto.getVersionObj()+" -Serveur Version Objet : "+entity.getVersionObj());
				} else {
					 entity = mapper.mapDtoToEntity(dto,entity);
				}
			}
			
			//dao.merge(entity);
			dao.detach(entity); //pour passer les controles
			enregistrerMerge(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			//throw new CreateException(e.getMessage());
			throw new EJBException(e.getMessage());
		}
	}
	
	public void persistDTO(TaPanierDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaPanierDTO dto, String validationContext) throws CreateException {
		try {
			TaPanierMapper mapper = new TaPanierMapper();
			TaPanier entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaPanierDTO dto) throws RemoveException {
		try {
			TaPanierMapper mapper = new TaPanierMapper();
			TaPanier entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaPanier refresh(TaPanier persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaPanier value, String validationContext) /*throws ExceptLgr*/ {
		try {
			if(validationContext==null) validationContext="";
			validateAll(value,validationContext,false); //ancienne validation, extraction de l'annotation et appel
			//dao.validate(value); //validation automatique via la JSR bean validation
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateEntityPropertyValidationContext")
	public void validateEntityProperty(TaPanier value, String propertyName, String validationContext) {
		try {
			if(validationContext==null) validationContext="";
			validate(value, propertyName, validationContext);
			//dao.validateField(value,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOValidationContext")
	public void validateDTO(TaPanierDTO dto, String validationContext) {
		try {
			TaPanierMapper mapper = new TaPanierMapper();
			TaPanier entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaPanierDTO> validator = new BeanValidator<TaPanierDTO>(TaPanierDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaPanierDTO dto, String propertyName, String validationContext) {
		try {
			TaPanierMapper mapper = new TaPanierMapper();
			TaPanier entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaPanierDTO> validator = new BeanValidator<TaPanierDTO>(TaPanierDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaPanierDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaPanierDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaPanier value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaPanier value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}
	
	@Override
	public List<TaPanier> rechercheDocument(Date dateDeb, Date dateFin) {
		return dao.rechercheDocument(dateDeb, dateFin);
	}

	@Override
	public List<Object[]> rechercheDocument(Date dateDeb, Date dateFin,
			Boolean light) {
		return dao.rechercheDocument(dateDeb, dateFin, light);
	}

	@Override
	public List<TaPanier> rechercheDocument(String codeDeb, String codeFin) {
		return dao.rechercheDocument(codeDeb, codeFin);
	}

	@Override
	public List<TaPanier> rechercheDocument(Date dateDeb, Date dateFin,
			String codeTiers) {
		return dao.rechercheDocument(dateDeb, dateFin, codeTiers);
	}

	@Override
	public List<Object[]> rechercheDocumentLight(Date dateDeb, Date dateFin,
			String codeTiers) {
		return dao.rechercheDocumentLight(dateDeb, dateFin, codeTiers);
	}

	@Override
	public List<Object[]> rechercheDocumentLight(String codeDoc,
			String codeTiers) {
		return dao.rechercheDocumentLight(codeDoc, codeTiers);
	}

	@Override
	public List<TaPanier> rechercheDocument(String codeDeb, String codeFin,
			String codeTiers) {
		return dao.rechercheDocument(codeDeb, codeFin, codeTiers);
	}

	@Override
	public List<TaPanier> rechercheDocument(Date dateDeb, Date dateFin,
			String codeTiers, Boolean export) {
		// TODO Auto-generated method stub
		return dao.rechercheDocument(dateDeb, dateFin, codeTiers, export);
	}

	@Override
	public List<TaPanier> rechercheDocument(String codeDeb, String codeFin,
			String codeTiers, Boolean export) {
		// TODO Auto-generated method stub
		return dao.rechercheDocument(codeDeb, codeFin, codeTiers, export);
	}

	@Override
	public List<TaPanier> findByCodeTiersAndDate(String codeTiers, Date debut,
			Date fin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Object> findChiffreAffaireByCodeTiers(String codeTiers,
			Date debut, Date fin, int precision) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaTiers> findTiersByCodeArticleAndDate(String codeArticle,
			Date debut, Date fin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Object> findChiffreAffaireByCodeArticle(String codeArticle,
			Date debut, Date fin, int precision) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IDocumentTiers> selectDocNonTransformeRequete(
			IDocumentTiers doc, String codeTiers, String typeOrigine,
			String typeDest, Date debut, Date fin) {
		// TODO Auto-generated method stub
		return dao.selectDocNonTransformeRequete(doc, codeTiers, typeOrigine, typeDest, debut, fin);
	}

	@Override
	public List<TaPanier> selectAll(IDocumentTiers taDocument, Date dateDeb,
			Date dateFin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaPanier findByIdDocument(int id) throws FinderException {
		return findById(id);
	}

	@Override
	public Date calculDateEcheanceAbstract(TaPanier doc, Integer report, Integer finDeMois) {
		// TODO Auto-generated method stub
		return calculDateEcheance(doc,report,finDeMois);
	}

	@Override
	public Date calculDateEcheance(TaPanier doc, Integer report, Integer finDeMois) {
		// TODO Auto-generated method stub
		return null;
	}
// ----------------------------------------------------

	@Override
	public List<DocumentDTO> findAllDTOPeriode(Date dateDebut, Date dateFin,String codeTiers) {
		// TODO Auto-generated method stub
		return dao.findAllDTOPeriode(dateDebut, dateFin, codeTiers);
	}

	@Override
	public List<DocumentDTO> findDocumentNonTransfosDTO(Date dateDebut, Date dateFin,String codeTiers) {
		// TODO Auto-generated method stub
		return dao.findDocumentNonTransfosDTO(dateDebut, dateFin,codeTiers);
	}
	
	@Override
	public List<DocumentDTO> findDocumentTransfosDTO(Date dateDebut, Date dateFin,String codeTiers) {
		// TODO Auto-generated method stub
		return dao.findDocumentTransfosDTO(dateDebut, dateFin,codeTiers);
	}
	
	@Override
	public List<DocumentDTO> findDocumentNonTransfosARelancerDTO(Date dateDebut, Date dateFin, int deltaNbJours,String codeTiers) {
		// TODO Auto-generated method stub
		return dao.findDocumentNonTransfosARelancerDTO(dateDebut,dateFin,deltaNbJours,codeTiers);
	}

	@Override
	public long countDocument(Date debut, Date fin,String codeTiers) {
		// TODO Auto-generated method stub
		return dao.countDocument(debut, fin, codeTiers);
	}

	@Override
	public long countDocumentNonTransforme(Date debut, Date fin,String codeTiers) {
		// TODO Auto-generated method stub
		return dao.countDocumentNonTransforme(debut, fin, codeTiers);
	}

	@Override
	public long countDocumentNonTransformeARelancer(Date debut, Date fin, int deltaNbJours,String codeTiers) {
		// TODO Auto-generated method stub
		return dao.countDocumentNonTransformeARelancer(debut, fin, deltaNbJours, codeTiers);
	}

	@Override
	public long countDocumentTransforme(Date debut, Date fin,String codeTiers){
		return dao.countDocumentTransforme(debut, fin, codeTiers);
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalDTO(Date dateDebut, Date dateFin,String codeTiers) {
		// TODO Auto-generated method stub
		return dao.listeChiffreAffaireTotalDTO(dateDebut, dateFin, codeTiers);
	}

	@Override
	public DocumentChiffreAffaireDTO chiffreAffaireTotalDTO(Date dateDebut, Date dateFin,String codeTiers) {
		// TODO Auto-generated method stub
		return dao.chiffreAffaireTotalDTO(dateDebut, dateFin, codeTiers);
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTransformeTotalDTO(Date dateDebut, Date dateFin,String codeTiers) {
		// TODO Auto-generated method stub
		return dao.listeChiffreAffaireTransformeTotalDTO(dateDebut, dateFin, codeTiers);
	}

	@Override
	public DocumentChiffreAffaireDTO chiffreAffaireTransformeTotalDTO(Date dateDebut, Date dateFin,String codeTiers) {
		// TODO Auto-generated method stub
		return dao.chiffreAffaireTransformeTotalDTO(dateDebut, dateFin, codeTiers);
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireNonTransformeTotalDTO(Date dateDebut, Date dateFin,String codeTiers) {
		// TODO Auto-generated method stub
		return dao.listeChiffreAffaireNonTransformeTotalDTO(dateDebut, dateFin, codeTiers);
	}

	@Override
	public DocumentChiffreAffaireDTO chiffreAffaireNonTransformeTotalDTO(Date dateDebut, Date dateFin,String codeTiers) {
		// TODO Auto-generated method stub
		return dao.chiffreAffaireNonTransformeTotalDTO(dateDebut, dateFin, codeTiers);
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireNonTransformeARelancerTotalDTO(Date dateDebut,
			Date dateFin, int deltaNbJours,String codeTiers) {
		// TODO Auto-generated method stub
		return dao.listeChiffreAffaireNonTransformeARelancerTotalDTO(dateDebut, dateFin, deltaNbJours, codeTiers);
	}

	@Override
	public DocumentChiffreAffaireDTO chiffreAffaireNonTransformeARelancerTotalDTO(Date dateDebut, Date dateFin,
			int deltaNbJours,String codeTiers) {
		// TODO Auto-generated method stub
		return dao.chiffreAffaireNonTransformeARelancerTotalDTO(dateDebut, dateFin, deltaNbJours, codeTiers);
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalJmaDTO(Date dateDebut, Date dateFin, int precision,String codeTiers) {
		// TODO Auto-generated method stub
		return dao.listeChiffreAffaireTotalJmaDTO(dateDebut, dateFin, precision, codeTiers);
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireNonTransformeJmaDTO(Date dateDebut, Date dateFin,
			int precision,String codeTiers) {
		// TODO Auto-generated method stub
		return dao.listeChiffreAffaireNonTransformeJmaDTO(dateDebut, dateFin, precision, codeTiers);
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTransformeJmaDTO(Date dateDebut, Date dateFin,
			int precision,String codeTiers) {
		// TODO Auto-generated method stub
		return dao.listeChiffreAffaireTransformeJmaDTO(dateDebut, dateFin, precision, codeTiers);
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireNonTransformeARelancerJmaDTO(Date dateDebut, Date dateFin,
			int precision, int deltaNbJours,String codeTiers) {
		// TODO Auto-generated method stub
		return dao.listeChiffreAffaireNonTransformeARelancerJmaDTO(dateDebut, dateFin, precision, deltaNbJours, codeTiers);
	}

	@Override
	public String generePDF(int idDoc, Map<String,Object> mapParametreEdition, List<String> listResourcesPath, String theme) {
		return generePDF(idDoc,editionDefaut,mapParametreEdition, null, null);
	}
	
	public String generePDF(int idDoc, String modeleEdition, Map<String,Object> mapParametreEdition, List<String> listResourcesPath, String theme) {
		try {
			TaPanier doc = findById(idDoc);
			doc.calculeTvaEtTotaux();
			
			SchemaResolver sr = new SchemaResolver();
			String localPath = bdgProperties.osTempDirDossier(sr.resolveCurrentTenantIdentifier())+"/"+bdgProperties.tmpFileName(doc.getCodeDocument(), doc.getTaTiers().getCodeTiers() ,"bonLiv.pdf");
			System.out.println(localPath);
			System.out.println(bdgProperties.urlDemoHttps()+modeleEdition);
			
			Map<String,Object> mapEdition = new HashMap<String,Object>();

//			TaPanier doc = findById(idDoc);
//			doc.calculeTvaEtTotaux();

			mapEdition.put("doc",doc );

			mapEdition.put("taInfoEntreprise", entrepriseService.findInstance());

			mapEdition.put("lignes", doc.getLignes());

			
			if(mapParametreEdition == null) {
				mapParametreEdition = new HashMap<String,Object>();
			}
			List<TaPreferences> liste= taPreferencesService.findByGroupe("panier");
			if(!mapParametreEdition.containsKey("preferences")) mapParametreEdition.put("preferences", liste);
			if(!mapParametreEdition.containsKey("gestionLot")) mapParametreEdition.put("gestionLot", false);
			if(!mapParametreEdition.containsKey("mode")) mapParametreEdition.put("mode", "");
			if(!mapParametreEdition.containsKey("Theme")) mapParametreEdition.put("Theme", "defaultTheme");
			if(!mapParametreEdition.containsKey("Librairie")) mapParametreEdition.put("Librairie", "bdgFactTheme1");
			mapEdition.put("param", mapParametreEdition);

			//sessionMap.put("edition", mapEdition);
			
			////////////////////////////////////////////////////////////////////////////////////////
			//Test génération de fichier PDF
			
			HashMap<String,Object> hm = new HashMap<>();

			hm.put("edition", mapEdition);
			
			BirtUtil.setAppContextEdition(hm);
			BirtUtil.startReportEngine();
			
			BirtUtil.renderReportToFile(
					//bdgProperties.urlDemoHttps()+modeleEdition, //"https://dev.demo.promethee.biz:8443/reports/documents/facture/FicheFacture.rptdesign",
					modeleEdition,
					localPath, 
					new HashMap<>(), 
					BirtUtil.PDF_FORMAT,
					listResourcesPath,
					theme);
			 
			 return localPath;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<TaArticlesParTiersDTO> findArticlesParTiersParMois(Date debut, Date fin, String codeTiers) {
		// TODO Auto-generated method stub
		return dao.findArticlesParTiersParMois(debut, fin,codeTiers);
	}

	@Override
	public List<TaArticlesParTiersDTO> findArticlesParTiersTransforme(Date debut, Date fin, String codeTiers) {
		// TODO Auto-generated method stub
		return dao.findArticlesParTiersTransforme(debut, fin,codeTiers);
	}

	@Override
	public List<TaArticlesParTiersDTO> findArticlesParTiersNonTransforme(Date debut, Date fin, String codeTiers) {
		// TODO Auto-generated method stub
		return dao.findArticlesParTiersNonTransforme(debut, fin,codeTiers);
	}

	@Override
	public List<TaArticlesParTiersDTO> findArticlesParTiersNonTransformeARelancer(Date debut, Date fin, int deltaNbJours, String codeTiers) {
		// TODO Auto-generated method stub
		return dao.findArticlesParTiersNonTransformeARelancer(debut, fin, deltaNbJours,codeTiers);
	}	
	@Override
	public List<TaPanier> rechercheDocumentVerrouille(Date dateDeb, Date dateFin, String codeTiers,
			Boolean verrouille) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaPanier> rechercheDocumentVerrouille(String codeDeb, String codeFin, String codeTiers,
			Boolean verrouille) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaPanier> rechercheDocument(String codeTiers, Date dateExport, Date dateDeb, Date dateFin) {
		// TODO Auto-generated method stub
		return null;
	}


	
	
/////// regroupement
	
	@Override
	public DocumentChiffreAffaireDTO chiffreAffaireTotalDTOParTypeRegroupement(Date dateDebut, Date dateFin,
			String codeTiers, String typeRegroupement, Object valeurRegroupement, boolean regrouper) {
		// TODO Auto-generated method stub
		return dao.chiffreAffaireTotalDTOParTypeRegroupement(dateDebut, dateFin, codeTiers, typeRegroupement, valeurRegroupement, regrouper);
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalDTOParTypeRegroupement(Date dateDebut, Date dateFin,
			String codeTiers, String typeRegroupement, Object valeurRegroupement, boolean regrouper) {
		// TODO Auto-generated method stub
		return dao.listeChiffreAffaireTotalDTOParTypeRegroupement(dateDebut, dateFin, codeTiers, typeRegroupement, valeurRegroupement, regrouper);
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalJmaDTOParRegroupement(Date dateDebut, Date dateFin,
			int precision, String codeTiers, String typeRegroupement, Object valeurRegroupement) {
		// TODO Auto-generated method stub
		return dao.listeChiffreAffaireTotalJmaDTOParRegroupement(dateDebut, dateFin, precision, codeTiers, typeRegroupement, valeurRegroupement);
	}

	@Override
	public List<DocumentChiffreAffaireDTO> chiffreAffaireParEtat(Date debut, Date fin, String codeTiers,
			Object codeEtat, String typeRegroupement, Object valeurRegroupement) {
		// TODO Auto-generated method stub
		return dao.chiffreAffaireParEtat(debut, fin, codeTiers, codeEtat, typeRegroupement, valeurRegroupement);
	}

	@Override
	public List<DocumentChiffreAffaireDTO> countDocumentAndCodeEtatParTypeRegroupement(Date debut, Date fin,
			String codeTiers, Object codeEtat, String typeRegroupement, Object valeurRegroupement) {
		// TODO Auto-generated method stub
		return dao.countDocumentAndCodeEtatParTypeRegroupement(debut, fin, codeTiers, codeEtat, typeRegroupement, valeurRegroupement);
	}

	@Override
	public List<DocumentDTO> findAllDTOPeriodeAndCodeEtatParTypeRegroupement(Date debut, Date fin, String codeTiers,
			Object codeEtat, String typeRegroupement, Object valeurRegroupement) {
		// TODO Auto-generated method stub
		return dao.findAllDTOPeriodeAndCodeEtatParTypeRegroupement(debut, fin, codeTiers, codeEtat, typeRegroupement, valeurRegroupement);
	}

	@Override
	public List<TaArticlesParTiersDTO> findArticlesParTiersMoisAndCodeEtatParTypeRegroupement(Date debut, Date fin,
			Object codeEtat, String typeRegroupement, Object valeurRegroupement) {
		// TODO Auto-generated method stub
		return dao.findArticlesParTiersMoisAndCodeEtatParTypeRegroupement(debut, fin, codeEtat, typeRegroupement, valeurRegroupement);
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalDTOAndCodeEtatParTypeRegroupement(Date dateDebut,
			Date dateFin, String codeTiers, Object codeEtat, String typeRegroupement, Object valeurRegroupement,
			boolean regrouper) {
		// TODO Auto-generated method stub
		return dao.listeChiffreAffaireTotalDTOAndCodeEtatParTypeRegroupement(dateDebut, dateFin, codeTiers, codeEtat, typeRegroupement, valeurRegroupement, regrouper);
	}

	@Override
	public List<TaArticlesParTiersDTO> findArticlesParTiersParMoisParTypeRegroupement(Date dateDebut, Date dateFin,
			String codeTiers, String typeRegroupement, Object valeurRegroupement, boolean regroupee) {
		// TODO Auto-generated method stub
		return dao.findArticlesParTiersParMoisParTypeRegroupement(dateDebut, dateFin, codeTiers, typeRegroupement, valeurRegroupement, regroupee);
	}
	

	@Override
	public List<TaArticlesParTiersDTO> findArticlesParTiersParMois(Date debut, Date fin) {
		// TODO Auto-generated method stub
		return dao.findArticlesParTiersParMois(debut, fin);
	}

	@Override
	public List<TaArticlesParTiersDTO> findArticlesParTiersTransforme(Date debut, Date fin) {
		// TODO Auto-generated method stub
		return dao.findArticlesParTiersTransforme(debut, fin);
	}

	@Override
	public List<TaArticlesParTiersDTO> findArticlesParTiersNonTransforme(Date debut, Date fin) {
		// TODO Auto-generated method stub
		return dao.findArticlesParTiersNonTransforme(debut, fin);
	}

	@Override
	public List<TaArticlesParTiersDTO> findArticlesParTiersNonTransformeARelancer(Date debut, Date fin,
			int deltaNbJours) {
		// TODO Auto-generated method stub
		return dao.findArticlesParTiersNonTransformeARelancer(debut, fin, deltaNbJours);
	}

	@Override
	public List<TaArticlesParTiersDTO> findArticlesParTiersParMoisParTypeRegroupement(Date dateDebut, Date dateFin,
			String typeRegroupement, Object valeurRegroupement) {
		// TODO Auto-generated method stub
		return dao.findArticlesParTiersParMoisParTypeRegroupement(dateDebut, dateFin, typeRegroupement, valeurRegroupement);
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listeSumChiffreAffaireTotalDTOArticle(Date dateDebut, Date dateFin,
			String codeArticle, String codeTiers) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listeSumChiffreAffaireTotalDTOArticleMois(Date dateDebut, Date dateFin,
			String codeArticle, String codeTiers) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DocumentChiffreAffaireDTO> countChiffreAffaireTotalDTOArticle(Date dateDebut, Date dateFin,
			String codeArticle, String codeTiers) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DocumentChiffreAffaireDTO> sumChiffreAffaireTotalDTOArticle(Date dateDebut, Date dateFin) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<DocumentChiffreAffaireDTO> sumChiffreAffaireTotalDTOArticle(Date dateDebut, Date dateFin,
			String codeArticle) {
		return dao.sumChiffreAffaireTotalDTOArticle(dateDebut,  dateFin, codeArticle);
	}
	@Override
	public List<DocumentChiffreAffaireDTO> sumChiffreAffaireTotalDTOArticleMoinsAvoir(Date dateDebut, Date dateFin, String codeArticle){
		return dao.sumChiffreAffaireTotalDTOArticleMoinsAvoir(dateDebut, dateFin,  codeArticle);
	}

	@Override
	public String generePDF(int idDoc, Map<String, Object> mapParametreEdition, List<String> listResourcesPath,
			String theme, TaActionEdition action) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public TaEtat rechercheEtatInitialDocument() {
		try {
			return taEtatService.documentEncours(null);
		} catch (Exception e) {
			return null;
		}

	}
	
	@Override
	public List<DocumentDTO> findAllDTOPeriodeSimple(Date dateDebut, Date dateFin,String codeTiers) {
		// TODO Auto-generated method stub
		return dao.findAllDTOPeriodeSimple(dateDebut, dateFin, codeTiers);
	}
	
	@Override
	public List<DocumentDTO> findAllDTOIntervalle(String codeDebut, String codeFin,String codeTiers) {
		// TODO Auto-generated method stub
		return dao.findAllDTOIntervalle(codeDebut, codeFin, codeTiers);
	}

	
	@Override
	public TaEtat etatLigneInsertion(TaPanier masterEntity) {
		// TODO Auto-generated method stub
		return super.etatLigneInsertion(masterEntity);
	}

	@Override
	public TaPanier findDocByLDoc(ILigneDocumentTiers lDoc) {
		TaPanier o= (TaPanier) dao.findDocByLDoc(lDoc);
		 recupSetREtat(o);
		 recupSetHistREtat(o);
		 recupSetLigneALigne(o);
		 recupSetRDocument(o);
		 recupSetREtatLigneDocuments(o);
		 recupSetHistREtatLigneDocuments(o);
		recupSetRReglementLiaison(o);
		return o;
	}
	

	@Override
	public TaPanier mergeEtat(IDocumentTiers detachedInstance) {
		// TODO Auto-generated method stub
		modifEtatLigne(detachedInstance);
		TaEtat etat=changeEtatDocument(detachedInstance);
		((TaPanier) detachedInstance).addREtat(etat);
		
		detachedInstance=dao.merge((TaPanier) detachedInstance);	
		

		return (TaPanier) detachedInstance;
	}

	
	
	
	@Override
	public TaPanier findByCodeFetch(String code) throws FinderException {
		// TODO Auto-generated method stub
		TaPanier o = (TaPanier) dao.findByCodeFetch(code);
		if(o!=null) {
		recupSetREtat(o);
		recupSetHistREtat(o);
		recupSetLigneALigne(o);
		recupSetRDocument(o);
		recupSetREtatLigneDocuments(o);
		recupSetHistREtatLigneDocuments(o);
		recupSetRReglementLiaison(o);
		}
		return o;
	}

	@Override
	public TaPanier findByIDFetch(int id) throws FinderException {
		TaPanier o = (TaPanier) dao.findByIdFetch(id);
		if(o!=null) {
			recupSetREtat(o);
			recupSetHistREtat(o);
			recupSetLigneALigne(o);
			recupSetRDocument(o);
			recupSetREtatLigneDocuments(o);
			recupSetHistREtatLigneDocuments(o);
			recupSetRReglementLiaison(o);
		}
		return o;
	}

	@Override
	public void remove(TaPanier persistentInstance, boolean recharger) throws RemoveException {
		// TODO Auto-generated method stub
		try {
			if(recharger)persistentInstance=findByIDFetch(persistentInstance.getIdDocument());
			List<ILigneDocumentTiers> listeLien = recupListeLienLigneALigne(persistentInstance);
			dao.remove(persistentInstance);
			mergeEntityLieParLigneALigne(listeLien);
			
		} catch (Exception e) {
			logger.error("", e);
		}
	}
	
	public void remove(TaPanier persistentInstance) throws RemoveException {
		remove(persistentInstance, true);
	}

	@Override
	public int findDocByLDocDTO(ILigneDocumentTiers lDoc) {
		return (int) dao.findDocByLDocDTO(lDoc);
	}


	

	@Override
	public List<DocumentDTO> findAllDTOPeriodeAvecEtat(Date dateDebut, Date dateFin, String codeTiers, String etat) {
		// TODO Auto-generated method stub
		return dao.findAllDTOPeriodeAvecEtat(dateDebut, dateFin, codeTiers, etat);
	}

	@Override
	public List<DocumentChiffreAffaireDTO> chiffreAffaireTotalAvecEtatDTO(Date dateDebut, Date dateFin, String codeTiers,
			String etat) {
		// TODO Auto-generated method stub
		return dao.chiffreAffaireTotalAvecEtatDTO(dateDebut, dateFin, codeTiers, etat);
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalAvecEtatDTO(Date dateDebut, Date dateFin,
			String codeTiers, String etat) {
		// TODO Auto-generated method stub
		return dao.listeChiffreAffaireTotalAvecEtatDTO(dateDebut, dateFin, codeTiers, etat);
	}

	@Override
	public DocumentChiffreAffaireDTO chiffreAffaireTotalSurEtatDTO(Date dateDebut, Date dateFin, String codeTiers,
			String etat) {
		// TODO Auto-generated method stub
		return dao.chiffreAffaireTotalSurEtatDTO(dateDebut, dateFin, codeTiers, etat);
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalSurEtatDTO(Date dateDebut, Date dateFin,
			String codeTiers, String etat) {
		// TODO Auto-generated method stub
		return dao.listeChiffreAffaireTotalSurEtatDTO(dateDebut, dateFin, codeTiers, etat);
	}


	@Override
	public List<DocumentChiffreAffaireDTO> countDocumentAvecEtat(Date debut, Date fin, String codeTiers,
			String codeArticle, String etat) {
		// TODO Auto-generated method stub
		return dao.countDocumentAvecEtat(debut, fin, codeTiers, codeArticle, etat);
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalJmaAvecEtatDTO(Date dateDebut, Date dateFin,
			int precision, String codeTiers, String etat) {
		// TODO Auto-generated method stub
		return dao.listeChiffreAffaireTotalJmaAvecEtatDTO(dateDebut, dateFin, precision, codeTiers, etat);
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listLigneArticleDTOTiersAvecEtat(Date dateDebut, Date dateFin,
			String codeArticle, String codeTiers, String etat, int deltaNbJours) {
		// TODO Auto-generated method stub
		return dao.listLigneArticleDTOTiersAvecEtat(dateDebut, dateFin, codeArticle, codeTiers, etat, deltaNbJours);
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listLigneArticleDTOTiersAvecEtat(Date dateDebut, Date dateFin,
			String codeArticle, String codeTiers, String etat, String orderBy, int deltaNbJours) {
		// TODO Auto-generated method stub
		return dao.listLigneArticleDTOTiersAvecEtat(dateDebut, dateFin, codeArticle, codeTiers, etat, orderBy, deltaNbJours);
	}

	@Override
	public List<TaArticlesParTiersDTO> findArticlesParTiersParMoisAvecEtat(Date debut, Date fin, String codeTiers,
			String etat, int deltaNbJours) {
		// TODO Auto-generated method stub
		return dao.findArticlesParTiersParMoisAvecEtat(debut, fin, codeTiers, etat, deltaNbJours);
	}
}

