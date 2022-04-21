package fr.legrain.generation.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import fr.legrain.article.model.TaLot;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaLotServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLPanierServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLigneALigneServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaPanierServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocPanierVersPanierServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.tiers.service.remote.ITaEtatServiceRemote;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.IInfosDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.dto.TaLigneALigneSupplementDTO;
import fr.legrain.document.model.TaDevis;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaInfosPanier;
import fr.legrain.document.model.TaLPanier;
import fr.legrain.document.model.TaLProforma;
import fr.legrain.document.model.TaLigneALigne;
import fr.legrain.document.model.TaPanier;
import fr.legrain.document.model.TaProforma;
import fr.legrain.document.model.TaRDocument;
import fr.legrain.dossier.model.TaPreferences;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.LibDate;



@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public  class GenereDocPanierVersPanier extends AbstractGenereDocVersPanier implements IGenereDocPanierVersPanierServiceRemote{
	private @EJB ITaPanierServiceRemote taPanierService;
	private @EJB ITaLPanierServiceRemote taLPanierService;
	protected @EJB ITaPreferencesServiceRemote taPreferencesService;
	@EJB private ITaLotServiceRemote taLotService ;
	@EJB private ITaArticleServiceRemote taArticleService ;
	@EJB private ITaEtatServiceRemote taEtatService ;
	@EJB private ITaLigneALigneServiceRemote taLigneALigneService ;

	
	protected TaPanier castDocumentSource() {
		return (TaPanier)ds;
	}
	
	protected TaPanier castDocumentDest() {
		return (TaPanier)dd;
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
			((TaPanier)dd).setTaTPaiement(((TaPanier)ds).getTaTPaiement());
			//			((TaPanier)dd).setDateEchDocument(((TaPanier)ds).getDateEchDocument());
			//			((TaPanier)dd).setRegleDocument(BigDecimal.valueOf(0));
			((TaPanier)dd).setRemHtDocument(((TaPanier)ds).getRemHtDocument());
			if(multiple)((TaPanier)dd).setTxRemHtDocument(BigDecimal.valueOf(0));
			else((TaPanier)dd).setTxRemHtDocument(((TaPanier)ds).getTxRemHtDocument());
			((TaPanier)dd).setRemTtcDocument(((TaPanier)ds).getRemTtcDocument());
			((TaPanier)dd).setTxRemTtcDocument(((TaPanier)ds).getTxRemTtcDocument());
			((TaPanier)dd).setNbEDocument(((TaPanier)ds).getNbEDocument());
			((TaPanier)dd).setTtc(((TaPanier)ds).getTtc());
			//			((TaPanier)dd).setExport(((TaPanier)ds).getExport());
			((TaPanier)dd).setCommentaire(((TaPanier)ds).getCommentaire());
			
			TaEtat etatLigne=taEtatService.documentEncours(null);
			codeDejaGenere=false;
			

			Map<String, String> paramsCode = new LinkedHashMap<String, String>();
			if((codeTiers==null || codeTiers.isEmpty()) 
					&& ((TaPanier)dd).getTaTiers()!=null)
				codeTiers=((TaPanier)dd).getTaTiers().getCodeTiers();
			paramsCode.put(Const.C_NOMFOURNISSEUR, codeTiers);

			paramsCode.put(Const.C_DATEDOCUMENT, LibDate.dateToString(((TaPanier)dd).getDateDocument()));				
			if(isGenereCode()) {
				((TaPanier)dd).setCodeDocument(taPanierService.genereCode(paramsCode));
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
						TaLPanier temp = (TaLPanier)copieLigneDocument(ligne, new TaLPanier());
						BigDecimal ratio=recupRatioQte(temp.getQteLDocument(),lSupp.getQteRecue());
						if(lSupp.getQteRecue()!=null)temp.setQteLDocument(lSupp.getQteRecue());
						if(ligne.getUniteGenere()!=null)temp.setU1LDocument(ligne.getUniteGenere());
						//calculer la proportion de la qté 2 en fonction de la Qté1 générée
						if(temp.getQte2LDocument()!=null && temp.getQte2LDocument().compareTo(BigDecimal.ZERO)!=0) {
							temp.setQte2LDocument(temp.getQte2LDocument().multiply(ratio));
						}
						if(lSupp.getNumLot()==null ||lSupp.getNumLot().isEmpty()) {
							Map<String, String> params = new LinkedHashMap<String, String>();
							if(((TaPanier)dd)!=null && ((TaPanier)dd).getTaTiers()!=null)params.put(Const.C_NOMFOURNISSEUR, ((TaPanier)dd).getTaTiers().getNomTiers());
							if(((TaPanier)dd)!=null && ((TaPanier)dd).getTaTiers()!=null)params.put(Const.C_CODEFOURNISSEUR, ((TaPanier)dd).getTaTiers().getCodeTiers());

							params.put(Const.C_CODEDOCUMENT, ((TaPanier)dd).getCodeDocument());
							params.put(Const.C_DATEDOCUMENT, LibDate.dateToString(((TaPanier)dd).getDateDocument()));
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
						temp.setTaDocument(((TaPanier)dd));
						TaLigneALigne lal = new TaLigneALigne();
						lal.setNumLot(lSupp.getNumLot());
						lal.setPrixU(ligne.getPrixULDocument());
						lal.setQte(ligne.getQteLDocument());
						lal.setQteRecue(lSupp.getQteRecue());
						lal.setUnite(ligne.getU1LDocument());
						lal.setUniteRecue(ligne.getUniteGenere());
						lal.setTaLPanier((TaLPanier) ligne);
						lal.setTaLPanier(temp);
						lal.setIdLigneSrc(ligne.getIdLDocument());
						temp.getTaLigneALignes().add(lal);
						((TaLPanier) ligne).getTaLigneALignes().add(lal);
						temp.addREtatLigneDoc(etatLigne);

						if(temp.getTaArticle()!=null)temp.setTaArticle(taArticleService.findByIdAvecLazy(temp.getTaArticle().getIdArticle(), false));
						((TaPanier)dd).addLigne(temp);
					}

					//génération de la ligne existante si qté # de 0
					if(ligne.getQteGenere()!=null && ligne.getQteGenere().compareTo(BigDecimal.ZERO)!=0) {
						BigDecimal qteTotale=ligne.getQteGenere();
						TaLPanier temp = (TaLPanier)copieLigneDocument(ligne, new TaLPanier());
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
						temp.setTaDocument(((TaPanier)dd));
						TaLigneALigne lal = new TaLigneALigne();
						if(ligne.getTaLot()!=null)lal.setNumLot(ligne.getTaLot().getNumLot());
						lal.setPrixU(ligne.getPrixULDocument());
						lal.setQte(ligne.getQteLDocument());
						lal.setQteRecue(ligne.getQteGenere());
						lal.setUnite(ligne.getU1LDocument());
						lal.setUniteRecue(ligne.getUniteGenere());
						lal.setTaLPanier((TaLPanier) ligne);
						lal.setTaLPanier(temp);
						lal.setIdLigneSrc(ligne.getIdLDocument());
						temp.getTaLigneALignes().add(lal);
						((TaLPanier) ligne).getTaLigneALignes().add(lal);
						temp.addREtatLigneDoc(etatLigne);
//						TaREtatLigneDocument rEtat = new TaREtatLigneDocument();
//						rEtat.setTaEtat(etatLigne);
//						rEtat.setTaLPanier(temp);
//						temp.setTaREtatLigneDocument(rEtat);
//						temp.getTaREtatLigneDocuments().add(rEtat);
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
							lal2.setTaLPanier(temp);
							lal2.setIdLigneSrc(ligneReel.getIdLDocument());
							temp.getTaLigneALignes().add(lal2);
							((TaLPanier) ligne).getTaLigneALignes().add(lal2);
							temp.addREtatLigneDoc(etatLigne);
						}
						if(temp.getTaArticle()!=null)temp.setTaArticle(taArticleService.findByIdAvecLazy(temp.getTaArticle().getIdArticle(), false));
						((TaPanier)dd).addLigne(temp);
					}
				}
//				// traiter l'état de la ligne
//				BigDecimal totalTransformee=BigDecimal.ZERO;
//				TaEtat etatLigneOrg = null;
////				List<TaLigneALigne> ll =taLigneALigneService.selectAll(ligne, null, null);
//				for (TaLigneALigne l : ((TaLPanier)ligne).getTaLigneALignes()) {
//					totalTransformee=totalTransformee.add(l.getQteRecue());
//				}
//				if(totalTransformee.compareTo(ligne.getQteLDocument())>=0)
//					etatLigneOrg=taEtatService.documentTermine(null);
//				else if(totalTransformee.signum()!=0) etatLigneOrg=taEtatService.documentPartiellementTransforme(null);
//				ligne.setTaEtat(etatLigneOrg);
				}
				}
			}else {
				for (ILigneDocumentTiers ligne : ((TaPanier)ds).getLignes()) {
					TaLPanier temp = (TaLPanier)copieLigneDocument(ligne, new TaLPanier()); 
					temp.setTaDocument(((TaPanier)dd));
					((TaPanier)dd).addLigne(temp);
				}
			}
			TaInfosPanier infos = (TaInfosPanier)copieInfosDocument(((TaPanier)ds).getTaInfosDocument(),new TaInfosPanier());
			infos.setTaDocument(((TaPanier)dd));
			((TaPanier)dd).setTaInfosDocument(infos);
			

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
		TaPanier documentInit = ((TaPanier)ds);
		try {
			
			if(documentInit!=null){
				documentInit.setLegrain(true);
//				dd=copieDocument(ds,dd);


				
				((TaPanier)dd).setCommentaire(commentaire);
				taPanierService.calculDateEcheanceAbstract(((TaPanier)dd),taCPaiement.getReportCPaiement(),taCPaiement.getFinMoisCPaiement());
				if(ds.getRelationDocument()){
				TaRDocument taRDocument = new TaRDocument();
				taRDocument.setTaPanier(((TaPanier)dd));
				taRDocument.setTaPanier(documentInit);
				taRDocument.setIdSrc(documentInit.getIdDocument());
				((TaPanier)dd).getTaRDocuments().add(taRDocument);
				}
				((TaPanier)dd).calculeTvaEtTotaux();	
			}
			return dd;
		} catch (Exception e) {
			logger.error("",e);
			return null;
		}
	}
	
	public String creationRequeteDejaGenere(IDocumentTiers ds) {
		int idDoc = ds.getIdDocument();
		String jpql = "";
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
