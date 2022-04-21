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
import fr.legrain.bdg.documents.service.remote.ITaLPreparationServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLigneALigneServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaPreparationServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocBonPreparationVersBonLivraisonServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.tiers.service.remote.ITaEtatServiceRemote;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.IInfosDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.dto.TaLigneALigneSupplementDTO;
import fr.legrain.document.model.TaBonliv;
import fr.legrain.document.model.TaDevis;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaInfosBonliv;
import fr.legrain.document.model.TaLBonliv;
import fr.legrain.document.model.TaLPreparation;
import fr.legrain.document.model.TaLigneALigne;
import fr.legrain.document.model.TaPreparation;
import fr.legrain.document.model.TaRDocument;
import fr.legrain.dossier.model.TaPreferences;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.LibDate;



@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public  class GenereDocBonPreparationVersBonLivraison extends AbstractGenereDocVersBonLivraison implements IGenereDocBonPreparationVersBonLivraisonServiceRemote{
	private @EJB ITaPreparationServiceRemote taPreparationService;
	private @EJB ITaLPreparationServiceRemote taLPreparationService;
	protected @EJB ITaPreferencesServiceRemote taPreferencesService;
	@EJB private ITaLotServiceRemote taLotService ;
	@EJB private ITaArticleServiceRemote taArticleService ;
	@EJB private ITaEtatServiceRemote taEtatService ;
	@EJB private ITaLigneALigneServiceRemote taLigneALigneService ;

	
	protected TaPreparation castDocumentSource() {
		return (TaPreparation)ds;
	}
	
	protected TaBonliv castDocumentDest() {
		return (TaBonliv)dd;
	}
	
	

	
	@Override
	public IDocumentTiers copieDocumentSpecifique(IDocumentTiers ds, IDocumentTiers dd
			,Boolean ligneSeparatrice,String libelleSeparateur) {
		try {
			((TaBonliv)dd).setTaTPaiement(((TaPreparation)ds).getTaTPaiement());
			//			((TaBonliv)dd).setDateEchDocument(((TaPreparation)ds).getDateEchDocument());
			//			((TaBonliv)dd).setRegleDocument(BigDecimal.valueOf(0));
			((TaBonliv)dd).setRemHtDocument(((TaPreparation)ds).getRemHtDocument());
			if(multiple)((TaBonliv)dd).setTxRemHtDocument(BigDecimal.valueOf(0));
			else((TaBonliv)dd).setTxRemHtDocument(((TaPreparation)ds).getTxRemHtDocument());
			((TaBonliv)dd).setRemTtcDocument(((TaPreparation)ds).getRemTtcDocument());
			((TaBonliv)dd).setTxRemTtcDocument(((TaPreparation)ds).getTxRemTtcDocument());
			((TaBonliv)dd).setNbEDocument(((TaPreparation)ds).getNbEDocument());
			((TaBonliv)dd).setTtc(((TaPreparation)ds).getTtc());
			//			((TaBonliv)dd).setExport(((TaPreparation)ds).getExport());
			((TaBonliv)dd).setCommentaire(((TaPreparation)ds).getCommentaire());

			TaEtat etatLigne=taEtatService.documentEncours(null);
			codeDejaGenere=false;
			

				Map<String, String> paramsCode = new LinkedHashMap<String, String>();
				if((codeTiers==null || codeTiers.isEmpty()) 
						&& ((TaBonliv)dd).getTaTiers()!=null)
					codeTiers=((TaBonliv)dd).getTaTiers().getCodeTiers();
				paramsCode.put(Const.C_NOMFOURNISSEUR, codeTiers);

				paramsCode.put(Const.C_DATEDOCUMENT, LibDate.dateToString(((TaBonliv)dd).getDateDocument()));				
				if(isGenereCode()) {
					((TaBonliv)dd).setCodeDocument(taBonlivService.genereCode(paramsCode));
				codeDejaGenere=true;
				}

				if(!generationModele) {	
				TaPreferences p = taPreferencesService.findByGoupeAndCle(ITaPreferencesServiceRemote.GROUPE_PREFERENCES_LOTS, ITaPreferencesServiceRemote.LOTS_GESTION_DES_LOTS);
				if(p!=null && LibConversion.StringToBoolean(p.getValeur()) != null) {
					dd.setGestionLot(LibConversion.StringToBoolean(p.getValeur()));
				}

				for (ILigneDocumentTiers ligne : ((TaPreparation)ds).getLignes()) {
					if(ligne.getTaArticle()==null) {
						TaLBonliv temp = (TaLBonliv)copieLigneDocument(ligne, new TaLBonliv()); 
						temp.setTaDocument(((TaBonliv)dd));
						((TaBonliv)dd).addLigne(temp);
					}
					else {
					if(ligne.getGenereLigne()) {
						//récupérer la liste lignesALignes
						ligne.setTaLigneALignes(new HashSet<TaLigneALigne>(taLigneALigneService.findByLDocument(ligne)));

						if(ligne.getQteGenere()==null)ligne.setQteGenere(ligne.getQteLDocument());
						for (TaLigneALigneSupplementDTO lSupp : ligne.getListeSupplement()) {
							TaLBonliv temp = (TaLBonliv)copieLigneDocument(ligne, new TaLBonliv());
							BigDecimal ratio=recupRatioQte(temp.getQteLDocument(),lSupp.getQteRecue());
							if(lSupp.getQteRecue()!=null)temp.setQteLDocument(lSupp.getQteRecue());
							if(ligne.getUniteGenere()!=null)temp.setU1LDocument(ligne.getUniteGenere());
							//calculer la proportion de la qté 2 en fonction de la Qté1 générée
							if(temp.getQte2LDocument()!=null && temp.getQte2LDocument().compareTo(BigDecimal.ZERO)!=0) {
								temp.setQte2LDocument(temp.getQte2LDocument().multiply(ratio));
							}
							if(lSupp.getNumLot()==null ||lSupp.getNumLot().isEmpty()) {
								Map<String, String> params = new LinkedHashMap<String, String>();
								if(((TaBonliv)dd)!=null && ((TaBonliv)dd).getTaTiers()!=null)params.put(Const.C_NOMFOURNISSEUR, ((TaBonliv)dd).getTaTiers().getNomTiers());
								if(((TaBonliv)dd)!=null && ((TaBonliv)dd).getTaTiers()!=null)params.put(Const.C_CODEFOURNISSEUR, ((TaBonliv)dd).getTaTiers().getCodeTiers());

								params.put(Const.C_CODEDOCUMENT, ((TaBonliv)dd).getCodeDocument());
								params.put(Const.C_DATEDOCUMENT, LibDate.dateToString(((TaBonliv)dd).getDateDocument()));
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
							temp.setTaDocument(((TaBonliv)dd));
							TaLigneALigne lal = new TaLigneALigne();
							lal.setNumLot(lSupp.getNumLot());
							lal.setPrixU(ligne.getPrixULDocument());
							lal.setQte(ligne.getQteLDocument());
							lal.setQteRecue(lSupp.getQteRecue());
							lal.setUnite(ligne.getU1LDocument());
							lal.setUniteRecue(ligne.getUniteGenere());
							lal.setTaLPreparation((TaLPreparation) ligne);
							lal.setTaLBonliv(temp);
							lal.setIdLigneSrc(ligne.getIdLDocument());
							temp.getTaLigneALignes().add(lal);
							((TaLPreparation) ligne).getTaLigneALignes().add(lal);
							temp.addREtatLigneDoc(etatLigne);
							if(temp.getTaArticle()!=null)temp.setTaArticle(taArticleService.findByIdAvecLazy(temp.getTaArticle().getIdArticle(), false));
							((TaBonliv)dd).addLigne(temp);
						}

						//génération de la ligne existante si qté # de 0
						if(ligne.getQteGenere()!=null && ligne.getQteGenere().compareTo(BigDecimal.ZERO)!=0) {
							BigDecimal qteTotale=ligne.getQteGenere();
							TaLBonliv temp = (TaLBonliv)copieLigneDocument(ligne, new TaLBonliv());
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


							temp.setTaDocument(((TaBonliv)dd));
							TaLigneALigne lal = new TaLigneALigne();
							if(ligne.getTaLot()!=null)lal.setNumLot(ligne.getTaLot().getNumLot());
							lal.setPrixU(ligne.getPrixULDocument());
							lal.setQte(ligne.getQteLDocument());
							lal.setQteRecue(ligne.getQteGenere());
							lal.setUnite(ligne.getU1LDocument());
							lal.setUniteRecue(ligne.getUniteGenere());
							lal.setTaLPreparation((TaLPreparation) ligne);
							lal.setTaLBonliv(temp);
							lal.setIdLigneSrc(ligne.getIdLDocument());
							temp.getTaLigneALignes().add(lal);
							((TaLPreparation) ligne).getTaLigneALignes().add(lal);
							temp.addREtatLigneDoc(etatLigne);

							for (TaLigneALigneSupplementDTO o : ligne.getListeLigneAIntegrer()) {
								//enregistrer le lien entre les lignes en recherchant la vraie ligne d'origine
								TaLigneALigne lal2 = new TaLigneALigne();

								TaLPreparation ligneReel=(TaLPreparation) o.getLigne();
								lal2.setNumLot(ligne.getNumlotGenere());
								lal2.setPrixU(ligneReel.getPrixULDocument());
								lal2.setQte(ligneReel.getQteLDocument());
								lal2.setQteRecue(o.getQteRecue());
								lal2.setUnite(ligneReel.getU1LDocument());
								lal2.setUniteRecue(ligne.getUniteGenere());
								lal2.setTaLPreparation((TaLPreparation) ligneReel);
								lal2.setTaLBonliv(temp);
								lal2.setIdLigneSrc(ligneReel.getIdLDocument());
								temp.getTaLigneALignes().add(lal2);
								((TaLPreparation) ligne).getTaLigneALignes().add(lal2);
								temp.addREtatLigneDoc(etatLigne);
							}
							if(temp.getTaArticle()!=null)temp.setTaArticle(taArticleService.findByIdAvecLazy(temp.getTaArticle().getIdArticle(), false));
							((TaBonliv)dd).addLigne(temp);
						}
					}
					}
				}
			}
			else {
				for (ILigneDocumentTiers ligne : ((TaDevis)ds).getLignes()) {
					TaLBonliv temp = (TaLBonliv)copieLigneDocument(ligne, new TaLBonliv()); 
					temp.setTaDocument(((TaBonliv)dd));
					((TaBonliv)dd).addLigne(temp);
				}
			}
			TaInfosBonliv infos = (TaInfosBonliv)copieInfosDocument(((TaPreparation)ds).getTaInfosDocument(),new TaInfosBonliv());
			infos.setTaDocument(((TaBonliv)dd));
			((TaBonliv)dd).setTaInfosDocument(infos);

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
		if(ld.getTaArticle()!=null && ((TaLPreparation)ls).getCodeTitreTransport()!=null ) {
			((TaLBonliv)ld).setCodeTitreTransport(((TaLPreparation)ls).getCodeTitreTransport());
			((TaLBonliv)ld).setQteTitreTransport(((TaLPreparation)ls).getQteTitreTransport());			
		}
		else
			//sinon, si l'article attend de la crd, on la met dans doc dest
			if(ld.getTaArticle()!=null && ld.getTaArticle().getTaRTaTitreTransport()!=null) {
				((TaLBonliv)ld).setCodeTitreTransport(ls.getTaArticle().getTaRTaTitreTransport().getTaTitreTransport().getCodeTitreTransport());
				if(((TaLBonliv)ld).getTaArticle().getTaRTaTitreTransport().getQteTitreTransport()!=null) {
					((TaLBonliv)ld).setQteTitreTransport(((TaLBonliv)ld).getTaArticle().getTaRTaTitreTransport().getQteTitreTransport().multiply(((TaLBonliv)ld).getQteLDocument()));
				}
			}		
	return ld;
}


	@Override
	public IDocumentTiers genereDocumentSpecifique(IDocumentTiers ds, IDocumentTiers dd,String codeTiers, boolean generationModele,boolean genereCode) {
		TaPreparation documentInit = ((TaPreparation)ds);
		try {
			
			if(documentInit!=null){
				documentInit.setLegrain(true);
//				dd=copieDocument(ds,dd);


				
				((TaBonliv)dd).setCommentaire(commentaire);
				taBonlivService.calculDateEcheanceAbstract(((TaBonliv)dd),taCPaiement.getReportCPaiement(),taCPaiement.getFinMoisCPaiement());
				if(ds.getRelationDocument()){
				TaRDocument taRDocument = new TaRDocument();
				taRDocument.setTaBonliv(((TaBonliv)dd));
				taRDocument.setTaPreparation(documentInit);
				taRDocument.setIdSrc(documentInit.getIdDocument());
				((TaBonliv)dd).getTaRDocuments().add(taRDocument);
				}
				((TaBonliv)dd).calculeTvaEtTotaux();
				
//				if(!generationModele){
//				if(ds.getTaRReglementLiaisons()!=null && ds.getTaRReglementLiaisons().size()>0) {
//					for (TaRReglementLiaison rr : ds.getTaRReglementLiaisons()) {
//						TaRReglementLiaison taRReglement = new TaRReglementLiaison();  
//						
//						taRReglement.setTaReglement(rr.getTaReglement());
//						taRReglement.setAffectation(rr.getAffectation());
//						taRReglement.setTaBonliv((TaBonliv)dd);
//						taRReglement.setEtat(IHMEtat.integre);
//						((TaBonliv)dd).addRReglementLiaison(taRReglement);
//					}
//				}
//			}
//			((TaBonliv)dd).calculRegleDocument();
//			((TaBonliv)dd).calculRegleDocumentComplet();
//			((TaBonliv)dd).setNetAPayer(((TaBonliv)dd).calculResteAReglerComplet());
			
			}
			return dd;
		} catch (Exception e) {
			logger.error("",e);
			return null;
		}
	}
	
	public String creationRequeteDejaGenere(IDocumentTiers ds) {
		int idDoc = ds.getIdDocument();
		String jpql = "select x from TaRDocument x where x.taPreparation.idDocument="+idDoc+" and x.taBonliv is not null";
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
