package fr.legrain.generation.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import fr.legrain.article.model.TaLot;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaLotServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaFactureServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLPanierServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLigneALigneServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaPanierServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaReglementServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocPanierVersFactureServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.tiers.service.remote.ITaCompteBanqueServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaEtatServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTPaiementServiceRemote;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.IInfosDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.dto.TaLigneALigneSupplementDTO;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaInfosFacture;
import fr.legrain.document.model.TaLAcompte;
import fr.legrain.document.model.TaLFacture;
import fr.legrain.document.model.TaLPanier;
import fr.legrain.document.model.TaLigneALigne;
import fr.legrain.document.model.TaPanier;
import fr.legrain.document.model.TaRDocument;
import fr.legrain.document.model.TaRReglement;
import fr.legrain.document.model.TaRReglementLiaison;
import fr.legrain.document.model.TaReglement;
import fr.legrain.document.model.TaTPaiement;
import fr.legrain.dossier.model.TaPreferences;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.lib.data.IHMEtat;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.LibDate;



@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public  class GenereDocPanierVersFacture extends AbstractGenereDocVersFacture implements IGenereDocPanierVersFactureServiceRemote{
	private @EJB ITaPanierServiceRemote taPanierService;
	private @EJB ITaLPanierServiceRemote taLPanierService;
	protected @EJB ITaPreferencesServiceRemote taPreferencesService;
	@EJB private ITaLotServiceRemote taLotService ;
	@EJB private ITaArticleServiceRemote taArticleService ;
	@EJB private ITaEtatServiceRemote taEtatService ;
	@EJB private ITaLigneALigneServiceRemote taLigneALigneService ;
	
	private @EJB ITaReglementServiceRemote taReglementService;
	private @EJB ITaTPaiementServiceRemote taTPaiementService;
	private @EJB ITaCompteBanqueServiceRemote taCompteBanqueService;

	
	protected TaPanier castDocumentSource() {
		return (TaPanier)ds;
	}
	
	protected TaFacture castDocumentDest() {
		return (TaFacture)dd;
	}
	
	
	public BigDecimal recupRatioQte(BigDecimal qteInitial,BigDecimal qteGen) {
		BigDecimal ratio=BigDecimal.ONE;
		if(qteInitial!=null && qteInitial.compareTo(BigDecimal.ZERO)!=0) {
			ratio=qteGen.divide(qteInitial,MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP);
		}
		return ratio;
	}
	
	@Override
	public IDocumentTiers copieDocumentSpecifique(IDocumentTiers ds, IDocumentTiers dd
			,Boolean ligneSeparatrice,String libelleSeparateur) {
		try {
			((TaFacture)dd).setTaTPaiement(((TaPanier)ds).getTaTPaiement());
			//			((TaFacture)dd).setDateEchDocument(((TaPanier)ds).getDateEchDocument());
			//			((TaFacture)dd).setRegleDocument(BigDecimal.valueOf(0));
			((TaFacture)dd).setRemHtDocument(((TaPanier)ds).getRemHtDocument());
			if(multiple)((TaFacture)dd).setTxRemHtDocument(BigDecimal.valueOf(0));
			else((TaFacture)dd).setTxRemHtDocument(((TaPanier)ds).getTxRemHtDocument());
			((TaFacture)dd).setRemTtcDocument(((TaPanier)ds).getRemTtcDocument());
			((TaFacture)dd).setTxRemTtcDocument(((TaPanier)ds).getTxRemTtcDocument());
			((TaFacture)dd).setNbEDocument(((TaPanier)ds).getNbEDocument());
			((TaFacture)dd).setTtc(((TaPanier)ds).getTtc());
			//			((TaFacture)dd).setExport(((TaPanier)ds).getExport());
			((TaFacture)dd).setCommentaire(((TaPanier)ds).getCommentaire());
			
			TaEtat etatLigne=taEtatService.documentEncours(null);
			codeDejaGenere=false;
			

			Map<String, String> paramsCode = new LinkedHashMap<String, String>();
			if((codeTiers==null || codeTiers.isEmpty()) 
					&& ((TaFacture)dd).getTaTiers()!=null)
				codeTiers=((TaFacture)dd).getTaTiers().getCodeTiers();
			paramsCode.put(Const.C_NOMFOURNISSEUR, codeTiers);

			paramsCode.put(Const.C_DATEDOCUMENT, LibDate.dateToString(((TaFacture)dd).getDateDocument()));				
			if(isGenereCode()) {
				((TaFacture)dd).setCodeDocument(taFactureService.genereCode(paramsCode));
				codeDejaGenere=true;
			}
			
			TaPreferences p = taPreferencesService.findByGoupeAndCle(ITaPreferencesServiceRemote.GROUPE_PREFERENCES_LOTS, ITaPreferencesServiceRemote.LOTS_GESTION_DES_LOTS);
			if(p!=null && LibConversion.StringToBoolean(p.getValeur()) != null) {
				dd.setGestionLot(LibConversion.StringToBoolean(p.getValeur()));
			}

			if(!generationModele) {
			for (ILigneDocumentTiers ligne : ((TaPanier)ds).getLignes()) {
				if(ligne.getTaArticle()==null) {
					TaLPanier temp = (TaLPanier)copieLigneDocument(ligne, new TaLPanier()); 
					temp.setTaDocument(((TaPanier)dd));
					((TaPanier)dd).addLigne(temp);

				}
				else {
				if(ligne.getGenereLigne()) {
					//récupérer la liste lignesALignes
					ligne.setTaLigneALignes(new HashSet<TaLigneALigne>(taLigneALigneService.findByLDocument(ligne)));
					
					if(ligne.getQteGenere()==null)ligne.setQteGenere(ligne.getQteLDocument());
					for (TaLigneALigneSupplementDTO lSupp : ligne.getListeSupplement()) {
						TaLFacture temp = (TaLFacture)copieLigneDocument(ligne, new TaLFacture());
						BigDecimal ratio=recupRatioQte(temp.getQteLDocument(),lSupp.getQteRecue());
						if(lSupp.getQteRecue()!=null)temp.setQteLDocument(lSupp.getQteRecue());
						if(ligne.getUniteGenere()!=null)temp.setU1LDocument(ligne.getUniteGenere());
						//calculer la proportion de la qté 2 en fonction de la Qté1 générée
						if(temp.getQte2LDocument()!=null && temp.getQte2LDocument().compareTo(BigDecimal.ZERO)!=0) {
							temp.setQte2LDocument(temp.getQte2LDocument().multiply(ratio));
						}
						if(lSupp.getNumLot()==null ||lSupp.getNumLot().isEmpty()) {
							Map<String, String> params = new LinkedHashMap<String, String>();
							if(((TaFacture)dd)!=null && ((TaFacture)dd).getTaTiers()!=null)params.put(Const.C_NOMFOURNISSEUR, ((TaFacture)dd).getTaTiers().getNomTiers());
							if(((TaFacture)dd)!=null && ((TaFacture)dd).getTaTiers()!=null)params.put(Const.C_CODEFOURNISSEUR, ((TaFacture)dd).getTaTiers().getCodeTiers());

							params.put(Const.C_CODEDOCUMENT, ((TaFacture)dd).getCodeDocument());
							params.put(Const.C_DATEDOCUMENT, LibDate.dateToString(((TaFacture)dd).getDateDocument()));
							if(ligne.getTaArticle()!=null && !ligne.getTaArticle().getGestionLot()){
								params.put(Const.C_VIRTUEL, "true");
								params.put(Const.C_IDARTICLEVIRTUEL, LibConversion.integerToString(ligne.getTaArticle().getIdArticle()));
							}
							String numLot=taLotService.genereCode(params);
							lSupp.setNumLot(numLot);
						}
						if(lSupp.getNumLot()!=null) {
							TaLot lot = new TaLot();
							lot.setNumLot(lSupp.getNumLot());
							if(ligne.getTaArticle()!=null) {
								lot.setTaArticle(ligne.getTaArticle());
								lot.setLibelle(ligne.getTaArticle().getLibellecArticle());							
								lot.setUnite1(ligne.getU1LDocument());
								lot.setUnite2(ligne.getU2LDocument());
								lot.setDateLot(dd.getDateDocument());
								lot.setDluo(LibDate.incrementDate(dd.getDateDocument(), LibConversion.stringToInteger(lot.getTaArticle().getParamDluo(), 0) , 0, 0));
							}
							temp.setTaLot(lot);
						}
						temp.setTaDocument(((TaFacture)dd));
						TaLigneALigne lal = new TaLigneALigne();
						lal.setNumLot(lSupp.getNumLot());
						lal.setPrixU(ligne.getPrixULDocument());
						lal.setQte(ligne.getQteLDocument());
						lal.setQteRecue(lSupp.getQteRecue());
						lal.setUnite(ligne.getU1LDocument());
						lal.setUniteRecue(ligne.getUniteGenere());
						lal.setTaLPanier((TaLPanier) ligne);
						lal.setTaLFacture(temp);
						lal.setIdLigneSrc(ligne.getIdLDocument());
						temp.getTaLigneALignes().add(lal);
						((TaLPanier) ligne).getTaLigneALignes().add(lal);
						temp.addREtatLigneDoc(etatLigne);

						if(temp.getTaArticle()!=null)temp.setTaArticle(taArticleService.findByIdAvecLazy(temp.getTaArticle().getIdArticle(), false));
						((TaFacture)dd).addLigne(temp);
					}

					//génération de la ligne existante si qté # de 0
					if(ligne.getQteGenere()!=null && ligne.getQteGenere().compareTo(BigDecimal.ZERO)!=0) {
						BigDecimal qteTotale=ligne.getQteGenere();
						TaLFacture temp = (TaLFacture)copieLigneDocument(ligne, new TaLFacture());
						for (TaLigneALigneSupplementDTO o : ligne.getListeLigneAIntegrer()) {
							//s'il y a des lignes à regrouper sur cette ligne
							qteTotale=qteTotale.add(o.getQteRecue());
						}

						BigDecimal ratio=recupRatioQte(temp.getQteLDocument(),qteTotale);
						if(qteTotale!=null)temp.setQteLDocument(qteTotale);
						if(ligne.getUniteGenere()!=null)temp.setU1LDocument(ligne.getUniteGenere());
						//calculer la proportion de la qté 2 en fonction de la Qté1 générée
						if(temp.getQte2LDocument()!=null && temp.getQte2LDocument().compareTo(BigDecimal.ZERO)!=0) {
							temp.setQte2LDocument(temp.getQte2LDocument().multiply(ratio));
						}

//						if(ligne.getNumlotGenere()!=null) {
//							TaLot lot = new TaLot();
//							lot.setNumLot(ligne.getNumlotGenere());
//							if(ligne.getTaArticle()!=null) {
//								lot.setTaArticle(ligne.getTaArticle());
//								lot.setLibelle(ligne.getTaArticle().getLibellecArticle());							
//								lot.setUnite1(ligne.getU1LDocument());
//								lot.setUnite2(ligne.getU2LDocument());
//								lot.setDateLot(dd.getDateDocument());
//								lot.setDluo(LibDate.incrementDate(dd.getDateDocument(), LibConversion.stringToInteger(lot.getTaArticle().getParamDluo(), 0) , 0, 0));
//							}
//							temp.setTaLot(lot);
//						}
						temp.setTaDocument(((TaFacture)dd));
						TaLigneALigne lal = new TaLigneALigne();
						if(ligne.getTaLot()!=null)lal.setNumLot(ligne.getTaLot().getNumLot());
						lal.setPrixU(ligne.getPrixULDocument());
						lal.setQte(ligne.getQteLDocument());
						lal.setQteRecue(ligne.getQteGenere());
						lal.setUnite(ligne.getU1LDocument());
						lal.setUniteRecue(ligne.getUniteGenere());
						lal.setTaLPanier((TaLPanier) ligne);
						lal.setTaLFacture(temp);
						lal.setIdLigneSrc(ligne.getIdLDocument());
						temp.getTaLigneALignes().add(lal);
						((TaLPanier) ligne).getTaLigneALignes().add(lal);
						temp.addREtatLigneDoc(etatLigne);

						for (TaLigneALigneSupplementDTO o : ligne.getListeLigneAIntegrer()) {
							//enregistrer le lien entre les lignes en recherchant la vraie ligne d'origine
							TaLigneALigne lal2 = new TaLigneALigne();
							
							TaLPanier ligneReel=(TaLPanier) o.getLigne();
							lal2.setNumLot(ligne.getNumlotGenere());
							lal2.setPrixU(ligneReel.getPrixULDocument());
							lal2.setQte(ligneReel.getQteLDocument());
							lal2.setQteRecue(o.getQteRecue());
							lal2.setUnite(ligneReel.getU1LDocument());
							lal2.setUniteRecue(ligne.getUniteGenere());
							lal2.setTaLPanier((TaLPanier) ligneReel);
							lal2.setTaLFacture(temp);
							lal2.setIdLigneSrc(ligneReel.getIdLDocument());
							temp.getTaLigneALignes().add(lal2);
							((TaLPanier) ligne).getTaLigneALignes().add(lal2);
							temp.addREtatLigneDoc(etatLigne);
						}
						if(temp.getTaArticle()!=null)temp.setTaArticle(taArticleService.findByIdAvecLazy(temp.getTaArticle().getIdArticle(), false));
						((TaFacture)dd).addLigne(temp);
					}
				}
				}
			}
			}else {
				for (ILigneDocumentTiers ligne : ((TaPanier)ds).getLignes()) {
					TaLPanier temp = (TaLPanier)copieLigneDocument(ligne, new TaLPanier()); 
					temp.setTaDocument(((TaPanier)dd));
					((TaPanier)dd).addLigne(temp);
				}
			}
			TaInfosFacture infos = (TaInfosFacture)copieInfosDocument(((TaPanier)ds).getTaInfosDocument(),new TaInfosFacture());
			infos.setTaDocument(((TaFacture)dd));
			((TaFacture)dd).setTaInfosDocument(infos);
			

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
		//si doc source à de la crd on la prend dans doc dest
		if(ld.getTaArticle()!=null && ((TaLPanier)ls).getCodeTitreTransport()!=null ) {
			((TaLFacture)ld).setCodeTitreTransport(((TaLPanier)ls).getCodeTitreTransport());
			((TaLFacture)ld).setQteTitreTransport(((TaLPanier)ls).getQteTitreTransport());			
		}
		else
			//sinon, si l'article attend de la crd, on la met dans doc dest
			if(ld.getTaArticle()!=null && ld.getTaArticle().getTaRTaTitreTransport()!=null) {
				((TaLFacture)ld).setCodeTitreTransport(ls.getTaArticle().getTaRTaTitreTransport().getTaTitreTransport().getCodeTitreTransport());
				if(((TaLFacture)ld).getTaArticle().getTaRTaTitreTransport().getQteTitreTransport()!=null) {
					((TaLFacture)ld).setQteTitreTransport(((TaLFacture)ld).getTaArticle().getTaRTaTitreTransport().getQteTitreTransport().multiply(((TaLFacture)ld).getQteLDocument()));
				}
			}		
		return ld;
	}


	@Override
	public IDocumentTiers genereDocumentSpecifique(IDocumentTiers ds, IDocumentTiers dd,String codeTiers, boolean generationModele,boolean genereCode) {
		TaPanier documentInit = ((TaPanier)ds);
		try {
			
			if(documentInit!=null){
				documentInit.setLegrain(true);
//				dd=copieDocument(ds,dd);


				
				((TaFacture)dd).setCommentaire(commentaire);
				taFactureService.calculDateEcheanceAbstract(((TaFacture)dd),taCPaiement.getReportCPaiement(),taCPaiement.getFinMoisCPaiement());
				if(ds.getRelationDocument()){
				TaRDocument taRDocument = new TaRDocument();
				taRDocument.setTaFacture(((TaFacture)dd));
				taRDocument.setTaPanier(documentInit);
				taRDocument.setIdSrc(documentInit.getIdDocument());
				((TaFacture)dd).getTaRDocuments().add(taRDocument);
				}
				((TaFacture)dd).calculeTvaEtTotaux();
				
				

				if(!generationModele){
					/* Si le ds à un règlement associé, alors on crée l'affectation de ce règlement dans la facture*/
					if(ds.getTaRReglementLiaisons()!=null && ds.getTaRReglementLiaisons().size()>0) {
						for (TaRReglementLiaison rr : ds.getTaRReglementLiaisons()) {
							TaRReglement taRReglement = new TaRReglement();  /* A remettre dès que l'on travaillera sur reglement multiple il faudra passer par une relation TaRReglement*/
							
							taRReglement.setTaReglement(rr.getTaReglement());
							taRReglement.setAffectation(rr.getAffectation());
							taRReglement.setTaFacture((TaFacture)dd);
							taRReglement.setEtat(IHMEtat.integre);
							((TaFacture)dd).addRReglement(taRReglement);
						}
					}
				}
				((TaFacture)dd).calculRegleDocument();
				((TaFacture)dd).calculRegleDocumentComplet();
				((TaFacture)dd).setNetAPayer(((TaFacture)dd).calculResteAReglerComplet());
				
			}
			return dd;
		} catch (Exception e) {
			logger.error("",e);
			return null;
		}
	}
	
	public String creationRequeteDejaGenere(IDocumentTiers ds) {
		int idDoc = ds.getIdDocument();
		String jpql = "select x from TaRDocument x where x.taPanier.idDocument="+idDoc+" and x.taFacture is not null";
		return jpql;
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
	public IDocumentTiers genereDocument(IDocumentTiers document, IDocumentTiers docGenere, String codeTiers, boolean b,
			boolean generationModele,boolean genereCode,boolean multiple) {
		// TODO Auto-generated method stub
		return super.genereDocument(document, docGenere, codeTiers, b, generationModele, genereCode, multiple);
	}

	@Override
	public void setRelationDocument(boolean relationDocument) {
		super.setRelationDocument(relationDocument);
		
	}



}
