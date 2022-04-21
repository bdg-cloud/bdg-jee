package fr.legrain.generation.service;




import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import fr.legrain.article.model.TaArticle;
import fr.legrain.bdg.documents.service.remote.ITaFactureServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaReglementServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocLigneEcheanceAbonnementVersfactureServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.tiers.service.remote.ITaTPaiementServiceRemote;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.IInfosDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.model.SWTDocument;
import fr.legrain.document.model.TaAbonnement;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaInfosFacture;
import fr.legrain.document.model.TaLAbonnement;
import fr.legrain.document.model.TaLEcheance;
import fr.legrain.document.model.TaLFacture;
import fr.legrain.document.model.TaLPanier;
import fr.legrain.document.model.TaLigneALigneEcheance;
import fr.legrain.document.model.TaPanier;
import fr.legrain.document.model.TaRReglement;
import fr.legrain.document.model.TaReglement;
import fr.legrain.document.model.TaTLigne;
import fr.legrain.document.model.TaTPaiement;
import fr.legrain.document.model.TypeDoc;
import fr.legrain.dossier.model.TaPreferences;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.lib.data.IHMEtat;
import fr.legrain.lib.data.LibChaine;
import fr.legrain.lib.data.LibDate;
import fr.legrain.servicewebexterne.model.TaServiceWebExterne;
import fr.legrain.tiers.model.TaTiers;

@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public  class GenereDocLigneEcheanceAbonnementVersFacture extends AbstractGenereDocVersFactureLEcheance implements IGenereDocLigneEcheanceAbonnementVersfactureServiceRemote{
	private @EJB ITaFactureServiceRemote taFactureService;
	private @EJB ITaReglementServiceRemote taReglementService;
	private @EJB ITaTPaiementServiceRemote taTPaiementService;
	
	
	protected TaFacture castDocumentDest() {
		return (TaFacture)dd;
	}
	
	@Override
	public IDocumentTiers copieDocumentSpecifique(List<TaLEcheance> ds, IDocumentTiers dd
			,Boolean ligneSeparatrice,String libelleSeparateur) {
		try {

			TaEtat etatLigne=taEtatService.documentEncours(null);
			codeDejaGenere=false;
			
			//rajout yann
			TaAbonnement abo = ds.get(0).getTaLAbonnement().getTaDocument();
			TaTiers tiers = abo.getTaTiers();
			TaTPaiement taTPaiement = abo.getTaTPaiement();
			
			Map<String, String> paramsCode = new LinkedHashMap<String, String>();
			paramsCode.put(Const.C_NOMFOURNISSEUR, codeTiers);
			paramsCode.put(Const.C_DATEDOCUMENT, LibDate.dateToString(((TaFacture)dd).getDateDocument()));
			((TaFacture)dd).setTaTiers(tiers);
			((TaFacture)dd).setCodeDocument(taFactureService.genereCode(paramsCode));
			
			//libelleDoc
//			((TaFacture)dd).setTtc(1);
//			if(!LibChaine.empty(nouveauLibelle)) {
//				((TaFacture)dd).setLibelleDocument(nouveauLibelle);
//			} else {
//				((TaFacture)dd).setLibelleDocument(libelleDocument+" "+dd.getCodeDocument());
//			}
//			//((TaFacture)dd).setLibelleDocument("Facture ShippingBo N°: "+dd.getCodeDocument());
			
			for (TaLEcheance document : ds) {
				
				TaLAbonnement ligneAbo = taLEcheanceService.fetchLigneAbo(document.getIdLEcheance());
				ILigneDocumentTiers lf = new TaLFacture();
				((TaLFacture) lf).initialiseLigne(false);
				TaArticle art = taArticleService.findById(document.getTaArticle().getIdArticle());		
				
				
				((TaLFacture) lf).setTaDocument((TaFacture) dd);
				((TaLFacture) lf).setTaTLigne(taTLigneService.findByCode("H"));
				((TaLFacture) lf).setTaArticle(art);
				((TaLFacture) lf).setLibLDocument(ligneAbo.getLibLDocument());
				
				
				if(document.getCoefMultiplicateur() !=null) {
					((TaLFacture) lf).setQteLDocument(document.getQteLDocument().multiply(document.getCoefMultiplicateur()));
				}else {
					((TaLFacture) lf).setQteLDocument(document.getQteLDocument());
				}
				
				
				
				((TaLFacture) lf).setCodeTvaLDocument(art.getTaTva().getCodeTva());
				((TaLFacture) lf).setCodeTTvaLDocument("F");
				
				if(art.getTaUnite1() != null) {
					((TaLFacture) lf).setU1LDocument(art.getTaUnite1().getCodeUnite());
				}
				
				((TaLFacture) lf).setCompteLDocument(art.getNumcptArticle());
				
				
				((TaLFacture) lf).setMtTtcLDocument(document.getMtTtcLDocument());
				BigDecimal montantHt = lf.getMtTtcLDocument().divide(BigDecimal.valueOf(1).add(
						 (art.getTaTva().getTauxTva().divide(new BigDecimal(100),MathContext.DECIMAL128).setScale(3,BigDecimal.ROUND_HALF_UP)
					)),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP);
				((TaLFacture) lf).setMtHtLDocument(montantHt);
				BigDecimal prixULDocument = montantHt.divide(((TaLFacture) lf).getQteLDocument(), 2,BigDecimal.ROUND_HALF_UP);
				
				

				
				
				
				((TaLFacture) lf).setPrixULDocument(prixULDocument);
				((TaLFacture) lf).setTauxTvaLDocument(art.getTaTva().getTauxTva());
				
			
				((TaFacture)dd).addLigne((TaLFacture)lf);
				
				TaLigneALigneEcheance lal = new TaLigneALigneEcheance();
				lal.setTaLEcheance(document);
				lal.setTaLFacture((TaLFacture) lf);
				lf.setAbonnement(true);
//				lalEcheance.add(lal);
				((TaLFacture) lf).getTaLigneALignesEcheance().add(lal);

				
				//ligne de commentaire sur la période
				ILigneDocumentTiers lfcom = new TaLFacture();
				((TaLFacture) lfcom).initialiseLigne(true);
				TaTLigne taTLigne = taTLigneService.findByCode(SWTDocument.C_CODE_T_LIGNE_H);
				((TaLFacture) lfcom).setTaTLigne(taTLigne);
				//l.setTaLEcheance(ech);
				((TaLFacture) lfcom).setLibLDocument("Période du "+LibDate.dateToString(document.getDebutPeriode())+" au "+LibDate.dateToString(document.getFinPeriode()));
				((TaFacture)dd).addLigne((TaLFacture)lfcom);
				((TaLFacture) lfcom).setTaDocument(((TaFacture)dd));
				
				//ligne commentaire pour les compléments 
				if(ligneAbo!=null) {
					if(ligneAbo.getComplement1()!=null && !ligneAbo.getComplement1().isEmpty()) {
						lf = new TaLFacture();
						lf.setTaTLigne(taTLigne);
						//lf.setTaLEcheance(document);
						lf.setLibLDocument(ligneAbo.getComplement1());
						((TaFacture) dd).addLigne((TaLFacture)lf);
						((TaLFacture)lf).setTaDocument((TaFacture)dd);
					}
					if(ligneAbo.getComplement2()!=null && !ligneAbo.getComplement2().isEmpty()) {
						lf = new TaLFacture();
						lf.setTaTLigne(taTLigne);
						//lf.setTaLEcheance(document);
						lf.setLibLDocument(ligneAbo.getComplement2());
						((TaFacture) dd).addLigne((TaLFacture)lf);
						((TaLFacture)lf).setTaDocument((TaFacture)dd);
					}
					if(ligneAbo.getComplement3()!=null && !ligneAbo.getComplement3().isEmpty()) {
						lf = new TaLFacture();
						lf.setTaTLigne(taTLigne);
						//lf.setTaLEcheance(document);
						lf.setLibLDocument(ligneAbo.getComplement3());
						((TaFacture) dd).addLigne((TaLFacture)lf);
						((TaLFacture)lf).setTaDocument((TaFacture)dd);
					}

				}
				
			

			}
			
		taLEcheanceService.regleEcheances(ds);	

			TaInfosFacture infos = new TaInfosFacture();
			infos.setTaDocument(((TaFacture)dd));
			infos=(TaInfosFacture)copieInfosDocument(infos);
			((TaFacture)dd).setTaInfosDocument(infos);

			//rajout yann condition regleDoc le 11/05/21 qui était deja la avant modif isa (bouffage de code ?)
			if(regleDoc) {
//				if(selectedType.equals(TypeDoc.TYPE_FACTURE)) {
					 Date now = new Date();
					 TaRReglement taRReglement = new TaRReglement();
					 TaReglement reglement = new TaReglement();
					
					 taRReglement.setAffectation(dd.getNetTtcCalc());
					 taRReglement.setEtat(IHMEtat.integre);
					 TaPreferences prefTypePaiement = null;
					 //CB par défaut
					 if(taTPaiement != null) {
						 reglement.setTaTPaiement(taTPaiement);
					 }else {
						 String codeTypePaiement = "CB";
						 if(codeServiceWeb != null && !codeServiceWeb.isEmpty()) {
							 switch (codeServiceWeb) {
								case TaServiceWebExterne.CODE_SERVICE_SHIPPINGBO:
									prefTypePaiement =  taPreferencesService.findByGoupeAndCle(ITaPreferencesServiceRemote.IMPORTATION, ITaPreferencesServiceRemote.TYPE_PAIEMENT_SHIPPINGBO);
									break;
								case TaServiceWebExterne.CODE_SERVICE_MENSURA:
									prefTypePaiement = taPreferencesService.findByGoupeAndCle(ITaPreferencesServiceRemote.IMPORTATION, ITaPreferencesServiceRemote.TYPE_PAIEMENT_MENSURA);
									break;
								case TaServiceWebExterne.CODE_SERVICE_WOOCOMMERCE:
									prefTypePaiement = taPreferencesService.findByGoupeAndCle(ITaPreferencesServiceRemote.IMPORTATION, ITaPreferencesServiceRemote.TYPE_PAIEMENT_WOOCOMMERCE);
									break;
								case TaServiceWebExterne.CODE_SERVICE_PRESTASHOP:
									prefTypePaiement = taPreferencesService.findByGoupeAndCle(ITaPreferencesServiceRemote.IMPORTATION, ITaPreferencesServiceRemote.TYPE_PAIEMENT_PRESTASHOP);
									break;

								default:
									break;
								}
							 
							 if(prefTypePaiement != null && prefTypePaiement.getValeur() != null) {
								  codeTypePaiement = prefTypePaiement.getValeur();
							 }
						 }
						 
						 reglement.setTaTPaiement(taTPaiementService.findByCode(codeTypePaiement));
					 }
					 
					 reglement.setTaTiers(dd.getTaTiers());
					 if(dd.getLibelleDocument() != null) {
						 reglement.setLibelleDocument(dd.getLibelleDocument());
					 }else {
						 reglement.setLibelleDocument("Règlement échéance");
					 }
					 
					 reglement.getTaFactures().add((TaFacture) dd);
					 reglement.setDateDocument(now);
					 reglement.setDateLivDocument(now);
					 reglement.setCodeDocument(taReglementService.genereCode(null));
					 reglement.setNetTtcCalc(dd.getNetTtcCalc());
					 
					 ((TaFacture)dd).setRegleDocument(dd.getNetTtcCalc());
						((TaFacture)dd).setRegleCompletDocument(dd.getNetTtcCalc());
						((TaFacture)dd).setNetAPayer(BigDecimal.valueOf(0));
						
					 taRReglement.setTaReglement(reglement);
					 taRReglement.setTaFacture((TaFacture)dd);
					 ((TaFacture)dd).addRReglement(taRReglement);
						
					reglement=taReglementService.merge(reglement);
					taReglementService.annuleCode(reglement.getCodeDocument());
					
					 taRReglement.setTaReglement(reglement);
					 taRReglement.setTaFacture((TaFacture)dd);
					 ((TaFacture)dd).addRReglement(taRReglement);
//				 }
			}
			



			


			return dd;
		} catch (Exception e) {
			logger.error("",e);
		}
		return dd;
	}

	@Override
	public IInfosDocumentTiers copieInfosDocumentSpecifique(IInfosDocumentTiers is, IInfosDocumentTiers id) {
		return id;
	}

	@Override
	public ILigneDocumentTiers copieLigneDocumentSpecifique(ILigneDocumentTiers ls, ILigneDocumentTiers ld) {
		return ld;
	}

	@Override
	public IDocumentTiers genereDocumentSpecifique(IDocumentTiers ds, IDocumentTiers dd,String codeTiers, boolean generationModele,boolean genereCode) {
		return null;
	}

	public String creationRequeteDejaGenere(List<TaLEcheance> ds) {
//		int idDoc = ds.getIdDocument();
//		String jpql = "select x from TaRDocument x where x.taFacture.idDocument="+idDoc+" and x.taFacture is not null";
//		return jpql;
		return null;
	}
	@Override
	public void setRecupLibelleSeparateurDoc(boolean b) {
		// TODO Auto-generated method stub
		super.setRecupLibelleSeparateurDoc(b);
	}

	@Override
	public void setLigneSeparatrice(Boolean ligneSeparatrice) {
		// TODO Auto-generated method stub
		super.setLigneSeparatrice(ligneSeparatrice);
	}

	@Override
	public IDocumentTiers genereDocument(List<TaLEcheance> ds, IDocumentTiers dd, String codeTiers , boolean enregistre , boolean generationModele,boolean genereCode,boolean multiple) {
		// TODO Auto-generated method stub
		return super.genereDocument(ds, dd, codeTiers, enregistre, generationModele, genereCode, multiple);
	}

	@Override
	public void setRelationDocument(boolean relationDocument) {
		super.setRelationDocument(relationDocument);
		
	}

	@Override
	public IDocumentTiers genereDocument(IDocumentTiers document, IDocumentTiers dd, String codeTiers, boolean b,
			boolean generationModele, boolean genereCode, boolean multiple) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ILigneDocumentTiers copieLigneDocumentSpecifique(TaLEcheance ls, ILigneDocumentTiers ld) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IInfosDocumentTiers copieInfosDocumentSpecifique(IInfosDocumentTiers id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDocumentTiers genereDocumentSpecifique(List<TaLEcheance> ds, IDocumentTiers dd, String codeTiers,
			boolean generationModele, boolean genereCode) {
		try {			

			if(genereCode && !codeDejaGenere )((TaFacture)dd).setCodeDocument(taFactureService.genereCode(null));
			((TaFacture)dd).setCommentaire(commentaire);
			taFactureService.calculDateEcheanceAbstract(((TaFacture)dd),taCPaiement.getReportCPaiement(),taCPaiement.getFinMoisCPaiement());

			((TaFacture)dd).calculeTvaEtTotaux();	

		return dd;
	} catch (Exception e) {
		logger.error("",e);
		return null;
	}
}
	@Override
	public IDocumentTiers copieDocumentSpecifique(IDocumentTiers ds, IDocumentTiers dd, Boolean ligneSeparatrice,
			String libelleSeparateur) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String creationRequeteDejaGenere(IDocumentTiers ds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDocumentTiers genereDocument(IDocumentTiers dd, String codeTiers, boolean b,
			boolean generationModele, boolean genereCode, boolean multiple) {
		// TODO Auto-generated method stub
		return null;
	}







}
