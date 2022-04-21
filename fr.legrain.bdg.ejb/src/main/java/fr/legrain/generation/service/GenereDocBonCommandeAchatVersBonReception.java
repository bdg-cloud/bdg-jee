package fr.legrain.generation.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import fr.legrain.article.model.TaLot;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaLotServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaBoncdeAchatServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLBoncdeAchatServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLigneALigneServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocBonCommandeAchatVersBonReceptionServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.tiers.service.remote.ITaEtatServiceRemote;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.IInfosDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.dto.TaLigneALigneSupplementDTO;
import fr.legrain.document.model.TaApporteur;
import fr.legrain.document.model.TaBonReception;
import fr.legrain.document.model.TaBoncde;
import fr.legrain.document.model.TaBoncdeAchat;
import fr.legrain.document.model.TaBonliv;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaInfosBonReception;
import fr.legrain.document.model.TaLApporteur;
import fr.legrain.document.model.TaLBonReception;
import fr.legrain.document.model.TaLBoncdeAchat;
import fr.legrain.document.model.TaLigneALigne;
import fr.legrain.document.model.TaRDocument;
import fr.legrain.document.model.TaREtat;
import fr.legrain.document.model.TaREtatLigneDocument;
import fr.legrain.dossier.model.TaPreferences;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.LibDate;



@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public  class GenereDocBonCommandeAchatVersBonReception extends AbstractGenereDocVersBonReception implements IGenereDocBonCommandeAchatVersBonReceptionServiceRemote{
	private @EJB ITaBoncdeAchatServiceRemote taBoncdeAchatService;
	private @EJB ITaLBoncdeAchatServiceRemote taLBoncdeAchatService;
	protected @EJB ITaPreferencesServiceRemote taPreferencesService;
	@EJB private ITaLotServiceRemote taLotService ;
	@EJB private ITaArticleServiceRemote taArticleService ;
	@EJB private ITaEtatServiceRemote taEtatService ;
	@EJB private ITaLigneALigneServiceRemote taLigneALigneService ;

	
	protected TaBoncdeAchat castDocumentSource() {
		return (TaBoncdeAchat)ds;
	}
	
	protected TaBonReception castDocumentDest() {
		return (TaBonReception)dd;
	}
	
//	
//	public BigDecimal recupRatioQte(BigDecimal qteInitial,BigDecimal qteGen) {
//		BigDecimal ratio=BigDecimal.ONE;
//		if(qteInitial!=null && qteInitial.compareTo(BigDecimal.ZERO)!=0) {
//			ratio=qteGen.divide(qteInitial,MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP);
//		}
//		return ratio;
//	}
	
	@Override
	public IDocumentTiers copieDocumentSpecifique(IDocumentTiers ds, IDocumentTiers dd
			,Boolean ligneSeparatrice,String libelleSeparateur) {
		try {
			((TaBonReception)dd).setTaTPaiement(((TaBoncdeAchat)ds).getTaTPaiement());
			//			((TaBonReception)dd).setDateEchDocument(((TaBoncdeAchat)ds).getDateEchDocument());
			//			((TaBonReception)dd).setRegleDocument(BigDecimal.valueOf(0));
			((TaBonReception)dd).setRemHtDocument(((TaBoncdeAchat)ds).getRemHtDocument());
			if(multiple)((TaBonReception)dd).setTxRemHtDocument(BigDecimal.valueOf(0));
			else((TaBonReception)dd).setTxRemHtDocument(((TaBoncdeAchat)ds).getTxRemHtDocument());
			((TaBonReception)dd).setRemTtcDocument(((TaBoncdeAchat)ds).getRemTtcDocument());
			((TaBonReception)dd).setTxRemTtcDocument(((TaBoncdeAchat)ds).getTxRemTtcDocument());
			((TaBonReception)dd).setNbEDocument(((TaBoncdeAchat)ds).getNbEDocument());
			((TaBonReception)dd).setTtc(((TaBoncdeAchat)ds).getTtc());
			//			((TaBonReception)dd).setExport(((TaBoncdeAchat)ds).getExport());
			((TaBonReception)dd).setCommentaire(((TaBoncdeAchat)ds).getCommentaire());

			TaEtat etatLigne=taEtatService.documentEncours(null);
			codeDejaGenere=false;
			

			Map<String, String> paramsCode = new LinkedHashMap<String, String>();
			if((codeTiers==null || codeTiers.isEmpty()) 
					&& ((TaBonReception)dd).getTaTiers()!=null)
				codeTiers=((TaBonReception)dd).getTaTiers().getCodeTiers();
			paramsCode.put(Const.C_NOMFOURNISSEUR, codeTiers);
			if(((TaBonReception)dd).getTaTReception()!=null)
				paramsCode.put(Const.C_CODETYPE, ((TaBonReception)dd).getTaTReception().getCodeTReception());
			paramsCode.put(Const.C_DATEDOCUMENT, LibDate.dateToString(((TaBonReception)dd).getDateDocument()));				
			if(isGenereCode()) {
				((TaBonReception)dd).setCodeDocument(taBonReceptionService.genereCode(paramsCode));
				codeDejaGenere=true;
			}

			if(!generationModele) {
			TaPreferences p = taPreferencesService.findByGoupeAndCle(ITaPreferencesServiceRemote.GROUPE_PREFERENCES_LOTS, ITaPreferencesServiceRemote.LOTS_GESTION_DES_LOTS);
			if(p!=null && LibConversion.StringToBoolean(p.getValeur()) != null) {
				dd.setGestionLot(LibConversion.StringToBoolean(p.getValeur()));
			}

			for (ILigneDocumentTiers ligne : ((TaBoncdeAchat)ds).getLignes()) {
				if(ligne.getTaArticle()==null) {
					TaLBonReception temp = (TaLBonReception)copieLigneDocument(ligne, new TaLBonReception()); 
					temp.setTaDocument(((TaBonReception)dd));
					((TaBonReception)dd).addLigne(temp);
				}
				else {
				if(ligne.getGenereLigne()) {
					//récupérer la liste lignesALignes
					ligne.setTaLigneALignes(new HashSet<TaLigneALigne>(taLigneALigneService.findByLDocument(ligne)));
					
					if(ligne.getQteGenere()==null)ligne.setQteGenere(ligne.getQteLDocument());
					
					for (TaLigneALigneSupplementDTO lSupp : ligne.getListeSupplement()) {
						TaLBonReception temp = (TaLBonReception)copieLigneDocument(ligne, new TaLBonReception());
						BigDecimal ratio=recupRatioQte(temp.getQteLDocument(),lSupp.getQteRecue());
						if(lSupp.getQteRecue()!=null)temp.setQteLDocument(lSupp.getQteRecue());
						if(ligne.getUniteGenere()!=null)temp.setU1LDocument(ligne.getUniteGenere());
						//calculer la proportion de la qté 2 en fonction de la Qté1 générée
						if(temp.getQte2LDocument()!=null && temp.getQte2LDocument().compareTo(BigDecimal.ZERO)!=0) {
							temp.setQte2LDocument(temp.getQte2LDocument().multiply(ratio));
						}
						if(lSupp.getNumLot()==null ||lSupp.getNumLot().isEmpty()) {
							Map<String, String> params = new LinkedHashMap<String, String>();
							if(((TaBonReception)dd)!=null && ((TaBonReception)dd).getTaTiers()!=null)params.put(Const.C_NOMFOURNISSEUR, ((TaBonReception)dd).getTaTiers().getNomTiers());
							if(((TaBonReception)dd)!=null && ((TaBonReception)dd).getTaTiers()!=null)params.put(Const.C_CODEFOURNISSEUR, ((TaBonReception)dd).getTaTiers().getCodeTiers());

							params.put(Const.C_CODEDOCUMENT, ((TaBonReception)dd).getCodeDocument());
							params.put(Const.C_DATEDOCUMENT, LibDate.dateToString(((TaBonReception)dd).getDateDocument()));
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
						temp.setTaDocument(((TaBonReception)dd));
						TaLigneALigne lal = new TaLigneALigne();
						lal.setNumLot(lSupp.getNumLot());
						lal.setPrixU(ligne.getPrixULDocument());
						lal.setQte(ligne.getQteLDocument());
						lal.setQteRecue(lSupp.getQteRecue());
						lal.setUnite(ligne.getU1LDocument());
						lal.setUniteRecue(ligne.getUniteGenere());
						lal.setTaLBoncdeAchat((TaLBoncdeAchat) ligne);
						lal.setTaLBonReception(temp);
						lal.setIdLigneSrc(ligne.getIdLDocument());
						temp.getTaLigneALignes().add(lal);
						((TaLBoncdeAchat) ligne).getTaLigneALignes().add(lal);
						temp.addREtatLigneDoc(etatLigne);
						if(temp.getTaArticle()!=null)temp.setTaArticle(taArticleService.findByIdAvecLazy(temp.getTaArticle().getIdArticle(), false));
						((TaBonReception)dd).addLigne(temp);
					}

					//génération de la ligne existante si qté # de 0
					if(ligne.getQteGenere()!=null && ligne.getQteGenere().compareTo(BigDecimal.ZERO)!=0) {
						BigDecimal qteTotale=ligne.getQteGenere();
						TaLBonReception temp = (TaLBonReception)copieLigneDocument(ligne, new TaLBonReception());
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
						String numLot;
						if(ligne.getNumlotGenere()==null ||ligne.getNumlotGenere().isEmpty()) {
							Map<String, String> params = new LinkedHashMap<String, String>();
							if(((TaBonReception)dd)!=null && ((TaBonReception)dd).getTaTiers()!=null)params.put(Const.C_NOMFOURNISSEUR, ((TaBonReception)dd).getTaTiers().getNomTiers());
							if(((TaBonReception)dd)!=null && ((TaBonReception)dd).getTaTiers()!=null)params.put(Const.C_CODEFOURNISSEUR, ((TaBonReception)dd).getTaTiers().getCodeTiers());

							params.put(Const.C_CODEDOCUMENT, ((TaBonReception)dd).getCodeDocument());
							params.put(Const.C_DATEDOCUMENT, LibDate.dateToString(((TaBonReception)dd).getDateDocument()));
							if(ligne.getTaArticle()!=null && !ligne.getTaArticle().getGestionLot()){
								params.put(Const.C_VIRTUEL, "true");
								params.put(Const.C_IDARTICLEVIRTUEL, LibConversion.integerToString(ligne.getTaArticle().getIdArticle()));
							}
							String numLot1=taLotService.genereCode(params);
							ligne.setNumlotGenere(numLot1);
						}
						if(ligne.getNumlotGenere()!=null) {
							TaLot lot = new TaLot();
							lot.setNumLot(ligne.getNumlotGenere());
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
						temp.setTaDocument(((TaBonReception)dd));
						TaLigneALigne lal = new TaLigneALigne();
						lal.setNumLot(ligne.getNumlotGenere());
						lal.setPrixU(ligne.getPrixULDocument());
						lal.setQte(ligne.getQteLDocument());
						lal.setQteRecue(ligne.getQteGenere());
						lal.setUnite(ligne.getU1LDocument());
						lal.setUniteRecue(ligne.getUniteGenere());
						lal.setTaLBoncdeAchat((TaLBoncdeAchat) ligne);
						lal.setTaLBonReception(temp);
						lal.setIdLigneSrc(ligne.getIdLDocument());
						temp.getTaLigneALignes().add(lal);
						((TaLBoncdeAchat) ligne).getTaLigneALignes().add(lal);
						temp.addREtatLigneDoc(etatLigne);
						for (TaLigneALigneSupplementDTO o : ligne.getListeLigneAIntegrer()) {
							//enregistrer le lien entre les lignes en recherchant la vraie ligne d'origine
							TaLigneALigne lal2 = new TaLigneALigne();
							
							TaLBoncdeAchat ligneReel=(TaLBoncdeAchat) o.getLigne();
							lal2.setNumLot(ligne.getNumlotGenere());
							lal2.setPrixU(ligneReel.getPrixULDocument());
							lal2.setQte(ligneReel.getQteLDocument());
							lal2.setQteRecue(o.getQteRecue());
							lal2.setUnite(ligneReel.getU1LDocument());
							lal2.setUniteRecue(ligne.getUniteGenere());
							lal2.setTaLBoncdeAchat((TaLBoncdeAchat) ligneReel);
							lal2.setTaLBonReception(temp);
							lal2.setIdLigneSrc(ligneReel.getIdLDocument());
							temp.getTaLigneALignes().add(lal2);
							((TaLBoncdeAchat) ligne).getTaLigneALignes().add(lal2);
							temp.addREtatLigneDoc(etatLigne);
						}
						if(temp.getTaArticle()!=null)temp.setTaArticle(taArticleService.findByIdAvecLazy(temp.getTaArticle().getIdArticle(), false));
						((TaBonReception)dd).addLigne(temp);
					}
				}
				}
			}
			}else {
				for (ILigneDocumentTiers ligne : ((TaBoncde)ds).getLignes()) {
					TaLBonReception temp = (TaLBonReception)copieLigneDocument(ligne, new TaLBonReception()); 
					temp.setTaDocument(((TaBonReception)dd));
					((TaBonReception)dd).addLigne(temp);
				}
			}

			TaInfosBonReception infos = (TaInfosBonReception)copieInfosDocument(((TaBoncdeAchat)ds).getTaInfosDocument(),new TaInfosBonReception());
			infos.setTaDocument(((TaBonReception)dd));
			((TaBonReception)dd).setTaInfosDocument(infos);
			
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
		TaBoncdeAchat documentInit = ((TaBoncdeAchat)ds);
		try {
			
			if(documentInit!=null){
				documentInit.setLegrain(true);
//				dd=copieDocument(ds,dd);


				
				((TaBonReception)dd).setCommentaire(commentaire);
				taBonReceptionService.calculDateEcheanceAbstract(((TaBonReception)dd),taCPaiement.getReportCPaiement(),taCPaiement.getFinMoisCPaiement());
				if(ds.getRelationDocument()){
				TaRDocument taRDocument = new TaRDocument();
				taRDocument.setTaBonReception(((TaBonReception)dd));
				taRDocument.setTaBoncdeAchat(documentInit);
				taRDocument.setIdSrc(documentInit.getIdDocument());
				((TaBonReception)dd).getTaRDocuments().add(taRDocument);
				}
				((TaBonReception)dd).calculeTvaEtTotaux();	
			}
			return dd;
		} catch (Exception e) {
			logger.error("",e);
			return null;
		}
	}
	
	public String creationRequeteDejaGenere(IDocumentTiers ds) {
		int idDoc = ds.getIdDocument();
		String jpql = "select x from TaRDocument x where x.taBoncdeAchat.idDocument="+idDoc+" and x.taBonReception is not null";
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
