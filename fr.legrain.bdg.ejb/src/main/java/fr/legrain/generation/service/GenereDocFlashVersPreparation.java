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
import fr.legrain.bdg.documents.service.remote.ITaLigneALigneServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaPreparationServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocFLashVersPreparationServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.tiers.service.remote.ITaEtatServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.IInfosDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.dto.TaLigneALigneSupplementDTO;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaFlash;
import fr.legrain.document.model.TaInfosPreparation;
import fr.legrain.document.model.TaLFlash;
import fr.legrain.document.model.TaLPreparation;
import fr.legrain.document.model.TaLigneALigne;
import fr.legrain.document.model.TaPreparation;
import fr.legrain.document.model.TaRDocument;
import fr.legrain.dossier.model.TaPreferences;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.LibDate;
import fr.legrain.tiers.dto.AdresseInfosFacturationDTO;
import fr.legrain.tiers.dto.AdresseInfosLivraisonDTO;
import fr.legrain.tiers.dto.IdentiteTiersDTO;
import fr.legrain.tiers.dto.InfosCPaiementDTO;



@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public  class GenereDocFlashVersPreparation extends AbstractGenereDocVersPreparation implements IGenereDocFLashVersPreparationServiceRemote{
	private @EJB ITaPreparationServiceRemote taPreparationService;
	private @EJB ITaTiersServiceRemote taTiersService;
	@EJB private ITaLigneALigneServiceRemote taLigneALigneService ;
	@EJB private ITaEtatServiceRemote taEtatService ;
	protected @EJB ITaPreferencesServiceRemote taPreferencesService;
	@EJB private ITaLotServiceRemote taLotService ;
	@EJB private ITaArticleServiceRemote taArticleService ;
	
	
	protected TaFlash castDocumentSource() {
		return (TaFlash)ds;
	}
	
	protected TaPreparation castDocumentDest() {
		return (TaPreparation)dd;
	}
	
	
	public IDocumentTiers copieDocumentSpecifique2(TaFlash ds, IDocumentTiers dd
			,Boolean ligneSeparatrice,String libelleSeparateur) {
		String codeTiers = null;
		try {
//			((TaPreparation)dd).setTaTPaiement(((TaFlash)ds).getTaTPaiement());
//			((TaPreparation)dd).setDateEchDocument(((TaFlash)ds).getDateEchDocument());
//			((TaPreparation)dd).setRegleDocument(BigDecimal.valueOf(0));
//			((TaPreparation)dd).setRemHtDocument(((TaFlash)ds).getRemHtDocument());
//			if(multiple)((TaPreparation)dd).setTxRemHtDocument(BigDecimal.valueOf(0));else((TaPreparation)dd).setTxRemHtDocument(((TaFlash)ds).getTxRemHtDocument());
//			((TaPreparation)dd).setRemTtcDocument(((TaFlash)ds).getRemTtcDocument());
//			((TaPreparation)dd).setTxRemTtcDocument(((TaFlash)ds).getTxRemTtcDocument());
//			((TaPreparation)dd).setNbEDocument(((TaFlash)ds).getNbEDocument());
//			((TaPreparation)dd).setTtc(((TaFlash)ds).getTtc());
//			((TaPreparation)dd).setExport(((TaFlash)ds).getExport());
			((TaPreparation)dd).setCommentaire(((TaFlash)ds).getCommentaire());
			TaEtat etatLigne=taEtatService.documentEncours(null);
			for (TaLFlash ligne : ((TaFlash)ds).getLignes()) {
				if(ligne.isGenereLigne()) {
				TaLPreparation temp = (TaLPreparation)copieLigneDocument(ligne, new TaLPreparation()); 
				temp.setTaDocument(((TaPreparation)dd));
				((TaPreparation)dd).addLigne(temp);
				codeTiers=ligne.getCodeTiers();
				}
			}
			((TaPreparation)dd).setTaTiers(taTiersService.findByCode(codeTiers));

			TaInfosPreparation infos = new TaInfosPreparation();
			infos.setTaDocument(((TaPreparation)dd));
			infos=(TaInfosPreparation)copieInfosDocument(infos);
			((TaPreparation)dd).setTaInfosDocument(infos);

			return dd;
		} catch (Exception e) {
			logger.error("",e);
		}
		return dd;
	}

	@Override
	public IDocumentTiers copieDocumentSpecifique(TaFlash ds, IDocumentTiers dd
			,Boolean ligneSeparatrice,String libelleSeparateur) {
		try {
//			((TaPreparation)dd).setTaTPaiement(((TaFlash)ds).getTaTPaiement());
			//			((TaPreparation)dd).setDateEchDocument(((TaFlash)ds).getDateEchDocument());
			//			((TaPreparation)dd).setRegleDocument(BigDecimal.valueOf(0));
//			((TaPreparation)dd).setRemHtDocument(((TaFlash)ds).getRemHtDocument());
			if(multiple)((TaPreparation)dd).setTxRemHtDocument(BigDecimal.valueOf(0));
//			else((TaPreparation)dd).setTxRemHtDocument(((TaLFlash)ds).getTxRemHtDocument());
//			((TaPreparation)dd).setRemTtcDocument(((TaFlash)ds).getRemTtcDocument());
//			((TaPreparation)dd).setTxRemTtcDocument(((TaFlash)ds).getTxRemTtcDocument());
//			((TaPreparation)dd).setNbEDocument(((TaFlash)ds).getNbEDocument());
//			((TaPreparation)dd).setTtc(((TaFlash)ds).getTtc());
			//			((TaPreparation)dd).setExport(((TaFlash)ds).getExport());
//			((TaPreparation)dd).setCommentaire(((TaFlash)ds).getCommentaire());

//			Map<String, String> paramsCode = new LinkedHashMap<String, String>();
//			if((codeTiers==null || codeTiers.isEmpty()) 
//					&& ((TaPreparation)dd).getTaTiers()!=null)
//				codeTiers=((TaPreparation)dd).getTaTiers().getCodeTiers();
//			paramsCode.put(Const.C_NOMFOURNISSEUR, codeTiers);
//
//			paramsCode.put(Const.C_DATEDOCUMENT, LibDate.dateToString(((TaPreparation)dd).getDateDocument()));				
//			if(isGenereCode())
//				((TaPreparation)dd).setCodeDocument(taPreparationService.genereCode(paramsCode));

			TaPreferences p = taPreferencesService.findByGoupeAndCle(ITaPreferencesServiceRemote.GROUPE_PREFERENCES_LOTS, ITaPreferencesServiceRemote.LOTS_GESTION_DES_LOTS);
			if(p!=null && LibConversion.StringToBoolean(p.getValeur()) != null) {
				dd.setGestionLot(LibConversion.StringToBoolean(p.getValeur()));
			}

			TaEtat etatLigne=taEtatService.documentEncours(null);
			for (TaLFlash ligne : ((TaFlash)ds).getLignes()) {
				if(ligne.isGenereLigne()) {
					//récupérer la liste lignesALignes
					ligne.setTaLigneALignes(new HashSet<TaLigneALigne>(taLigneALigneService.findByLDocument(ligne)));
					dd.setTaTiers(ligne.getTaTiers());

					for (TaLigneALigneSupplementDTO lSupp : ligne.getListeSupplement()) {
						
						TaLPreparation temp = new TaLPreparation(true);
						temp.setTaDocument(((TaPreparation)dd));
						temp=	(TaLPreparation)copieLigneDocument(ligne,temp );

						BigDecimal ratio=recupRatioQte(temp.getQteLDocument(),lSupp.getQteRecue());
						if(lSupp.getQteRecue()!=null)temp.setQteLDocument(lSupp.getQteRecue());
						if(ligne.getUniteGenere()!=null)temp.setU1LDocument(ligne.getUniteGenere());
						//calculer la proportion de la qté 2 en fonction de la Qté1 générée
						if(temp.getQte2LDocument()!=null && temp.getQte2LDocument().compareTo(BigDecimal.ZERO)!=0) {
							temp.setQte2LDocument(temp.getQte2LDocument().multiply(ratio));
						}

						
						TaLigneALigne lal = new TaLigneALigne();
						lal.setNumLot(lSupp.getNumLot());
						lal.setPrixU(temp.getPrixULDocument());
						lal.setQte(temp.getQteLDocument());
						lal.setQteRecue(lSupp.getQteRecue());
						lal.setUnite(temp.getU1LDocument());
						lal.setUniteRecue(ligne.getUniteGenere());
						lal.setTaLFlash((TaLFlash) ligne);
						lal.setTaLPreparation(temp);
						lal.setIdLigneSrc(ligne.getIdLFlash());
						temp.getTaLigneALignes().add(lal);
						((TaLFlash) ligne).getTaLigneALignes().add(lal);
						temp.addREtatLigneDoc(etatLigne);
						temp.setLegrain(false);
						if(temp.getTaArticle()!=null)temp.setTaArticle(taArticleService.findByIdAvecLazy(temp.getTaArticle().getIdArticle(), false));
						((TaPreparation)dd).addLigne(temp);
						codeTiers=ligne.getCodeTiers();
					}

					//génération de la ligne existante si qté # de 0
					if(ligne.getQteGenere()!=null && ligne.getQteGenere().compareTo(BigDecimal.ZERO)!=0) {
						BigDecimal qteTotale=ligne.getQteGenere();
						
						TaLPreparation temp = new TaLPreparation(true);
						temp.setTaDocument(((TaPreparation)dd));
						temp=	(TaLPreparation)copieLigneDocument(ligne,temp );
						
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

						if(ligne.getNumlotGenere()!=null) {
							TaLot lot = taLotService.findByCode(ligne.getNumlotGenere());
							temp.setTaLot(lot);
						}
						
						TaLigneALigne lal = new TaLigneALigne();
						lal.setNumLot(ligne.getNumlotGenere());
						lal.setPrixU(BigDecimal.ZERO);
						lal.setQte(ligne.getQteLFlash());
						lal.setQteRecue(ligne.getQteGenere());
						lal.setUnite(ligne.getU1LFlash());
						lal.setUniteRecue(ligne.getUniteGenere());
						lal.setTaLFlash((TaLFlash) ligne);
						lal.setTaLPreparation(temp);
						lal.setIdLigneSrc(ligne.getIdLFlash());
						temp.getTaLigneALignes().add(lal);
						((TaLFlash) ligne).getTaLigneALignes().add(lal);
						temp.addREtatLigneDoc(etatLigne);

						for (TaLigneALigneSupplementDTO o : ligne.getListeLigneAIntegrer()) {
							//enregistrer le lien entre les lignes en recherchant la vraie ligne d'origine
							TaLigneALigne lal2 = new TaLigneALigne();
							
							TaLFlash ligneReel=(TaLFlash) o.getLigne();
							lal2.setNumLot(ligne.getNumlotGenere());
							lal2.setPrixU(BigDecimal.ZERO);
							lal2.setQte(ligneReel.getQteLFlash());
							lal2.setQteRecue(o.getQteRecue());
							lal2.setUnite(ligneReel.getU1LFlash());
							lal2.setUniteRecue(ligne.getUniteGenere());
							lal2.setTaLFlash((TaLFlash) ligneReel);
							lal2.setTaLPreparation(temp);
							lal2.setIdLigneSrc(ligneReel.getIdLFlash());
							temp.getTaLigneALignes().add(lal2);
							((TaLFlash) ligne).getTaLigneALignes().add(lal2);
							temp.addREtatLigneDoc(etatLigne);
						}
						temp.setLegrain(false);
						if(temp.getTaArticle()!=null)temp.setTaArticle(taArticleService.findByIdAvecLazy(temp.getTaArticle().getIdArticle(), false));
						((TaPreparation)dd).addLigne(temp);
						codeTiers=ligne.getCodeTiers();
					}
				}

			}

			((TaPreparation)dd).setTaTiers(taTiersService.findByCode(codeTiers));

			TaInfosPreparation infos = new TaInfosPreparation();
			infos.setTaDocument(((TaPreparation)dd));
			infos=(TaInfosPreparation)copieInfosDocument(infos);
			((TaPreparation)dd).setTaInfosDocument(infos);
			

			return dd;
		} catch (Exception e) {
			logger.error("",e);
		}
		return dd;
	}
	@Override
	public IInfosDocumentTiers copieInfosDocumentSpecifique( IInfosDocumentTiers id) {
		return id;
	}

	@Override
	public ILigneDocumentTiers copieLigneDocumentSpecifique(TaLFlash ls, ILigneDocumentTiers ld) {
		if(ls.getCodeBarre()!=null)((TaLPreparation)ld).setCodeBarre(ls.getCodeBarre());
		
		return ld;
	}

	@Override
	public IDocumentTiers genereDocumentSpecifique(TaFlash ds, IDocumentTiers dd,String codeTiers, boolean generationModele,boolean genereCode) {
		TaFlash documentInit = ((TaFlash)ds);
		try {
			
			if(documentInit!=null){
//				documentInit.setLegrain(true);
//				dd=copieDocument(ds,dd);
				Map<String, String> paramsCode = new LinkedHashMap<String, String>();
				if((codeTiers==null || codeTiers.isEmpty()) 
						&& ((TaPreparation)dd).getTaTiers()!=null)
					codeTiers=((TaPreparation)dd).getTaTiers().getCodeTiers();
				paramsCode.put(Const.C_NOMFOURNISSEUR, codeTiers);

				paramsCode.put(Const.C_DATEDOCUMENT, LibDate.dateToString(((TaPreparation)dd).getDateDocument()));				
				if(isGenereCode())
					((TaPreparation)dd).setCodeDocument(taPreparationService.genereCode(paramsCode));
				((TaPreparation)dd).setCommentaire(commentaire);
				taPreparationService.calculDateEcheanceAbstract(((TaPreparation)dd),taCPaiement.getReportCPaiement(),taCPaiement.getFinMoisCPaiement());
//				if(ds.getRelationDocument()){
				TaRDocument taRDocument = new TaRDocument();
				taRDocument.setTaPreparation(((TaPreparation)dd));
				taRDocument.setTaFlash(documentInit);
				taRDocument.setIdSrc(documentInit.getIdFlash());
				((TaPreparation)dd).getTaRDocuments().add(taRDocument);
//				}
				((TaPreparation)dd).calculeTvaEtTotaux();	
			}
			return dd;
		} catch (Exception e) {
			logger.error("",e);
			return null;
		}
	}
	
	public String creationRequeteDejaGenere(IDocumentTiers ds) {
		int idDoc = ds.getIdDocument();
		String jpql = "select x from TaRDocument x where x.taFlash.idDocument="+idDoc+" and x.taPreparation is not null";
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
	public IDocumentTiers genereDocument(TaFlash document, IDocumentTiers docGenere, String codeTiers, boolean b,
			boolean generationModele,boolean genereCode,boolean multiple) {
		// TODO Auto-generated method stub
		return super.genereDocument(document, docGenere, codeTiers, b, generationModele, genereCode, multiple);
	}

	@Override
	public void setRelationDocument(boolean relationDocument) {
		super.setRelationDocument(relationDocument);
		
	}

	@Override
	public void mapUIToModelAdresseFactVersInfosDoc(AdresseInfosFacturationDTO infos, TaFlash dd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mapUIToModelAdresseLivVersInfosDoc(AdresseInfosLivraisonDTO infos, TaFlash dd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mapUIToModelCPaiementVersInfosDoc(InfosCPaiementDTO infos, TaFlash dd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mapUIToModelDocumentVersInfosDoc(TaFlash dd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mapUIToModelIHMIdentiteTiersVersInfosDoc(IdentiteTiersDTO infos, TaFlash dd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String creationRequeteDejaGenere(TaFlash ds) {
		// TODO Auto-generated method stub
		return null;
	}


	public BigDecimal recupRatioQte(BigDecimal qteInitial,BigDecimal qteGen) {
		BigDecimal ratio=BigDecimal.ONE;
		if(qteInitial!=null && qteInitial.compareTo(BigDecimal.ZERO)!=0) {
			ratio=qteGen.divide(qteInitial,MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP);
		}
		return ratio;
	}


	@Override
	public IDocumentTiers copieDocumentSpecifique(IDocumentTiers ds, IDocumentTiers dd, Boolean ligneSeparatrice,
			String libelleSeparateur) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ILigneDocumentTiers copieLigneDocumentSpecifique(ILigneDocumentTiers ls, ILigneDocumentTiers ld) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IInfosDocumentTiers copieInfosDocumentSpecifique(IInfosDocumentTiers is, IInfosDocumentTiers id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDocumentTiers genereDocumentSpecifique(IDocumentTiers ds, IDocumentTiers dd, String codeTiers,
			boolean generationModele, boolean genereCode) {
		// TODO Auto-generated method stub
		return null;
	}




}
