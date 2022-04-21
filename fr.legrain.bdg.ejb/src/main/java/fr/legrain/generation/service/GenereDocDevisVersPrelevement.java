package fr.legrain.generation.service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import fr.legrain.bdg.documents.service.remote.ITaPrelevementServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocDevisVersPrelevementServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.IInfosDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.dto.TaLigneALigneSupplementDTO;
import fr.legrain.document.model.TaDevis;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaInfosPrelevement;
import fr.legrain.document.model.TaLDevis;
import fr.legrain.document.model.TaLPrelevement;
import fr.legrain.document.model.TaLigneALigne;
import fr.legrain.document.model.TaPrelevement;
import fr.legrain.document.model.TaRDocument;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.LibDate;


@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public  class GenereDocDevisVersPrelevement extends AbstractGenereDocVersPrelevement implements IGenereDocDevisVersPrelevementServiceRemote{
	private @EJB ITaPrelevementServiceRemote taPrelevementService;
	protected TaDevis castDocumentSource() {
		return (TaDevis)ds;
	}
	
	protected TaPrelevement castDocumentDest() {
		return (TaPrelevement)dd;
	}
	
	@Override
	public IDocumentTiers copieDocumentSpecifique(IDocumentTiers ds, IDocumentTiers dd
			,Boolean ligneSeparatrice,String libelleSeparateur) {
		try {
//			((TaPrelevement)dd).setTaTPaiement(((TaDevis)ds).getTaTPaiement());
			((TaPrelevement)dd).setDateEchDocument(((TaDevis)ds).getDateEchDocument());
			((TaPrelevement)dd).setRegleDocument(BigDecimal.valueOf(0));
			((TaPrelevement)dd).setRemHtDocument(((TaDevis)ds).getRemHtDocument());
			if(multiple)((TaPrelevement)dd).setTxRemHtDocument(BigDecimal.valueOf(0));else((TaPrelevement)dd).setTxRemHtDocument(((TaDevis)ds).getTxRemHtDocument());
			((TaPrelevement)dd).setRemTtcDocument(((TaDevis)ds).getRemTtcDocument());
			((TaPrelevement)dd).setTxRemTtcDocument(((TaDevis)ds).getTxRemTtcDocument());
			((TaPrelevement)dd).setNbEDocument(((TaDevis)ds).getNbEDocument());
			((TaPrelevement)dd).setTtc(((TaDevis)ds).getTtc());
			((TaPrelevement)dd).setDateExport(((TaDevis)ds).getDateExport());
			((TaPrelevement)dd).setCommentaire(((TaDevis)ds).getCommentaire());


			TaEtat etatLigne=taEtatService.documentEncours(null);
			codeDejaGenere=false;
			

				Map<String, String> paramsCode = new LinkedHashMap<String, String>();
				if((codeTiers==null || codeTiers.isEmpty()) 
						&& ((TaPrelevement)dd).getTaTiers()!=null)
					codeTiers=((TaPrelevement)dd).getTaTiers().getCodeTiers();
				paramsCode.put(Const.C_NOMFOURNISSEUR, codeTiers);

				paramsCode.put(Const.C_DATEDOCUMENT, LibDate.dateToString(((TaPrelevement)dd).getDateDocument()));				
				if(isGenereCode()) {
					((TaPrelevement)dd).setCodeDocument(taPrelevementService.genereCode(paramsCode));
				codeDejaGenere=true;
				}

				if(!generationModele) {
				for (ILigneDocumentTiers ligne : ((TaDevis)ds).getLignes()) {
					if(ligne.getTaArticle()==null) {
						TaLPrelevement temp = (TaLPrelevement)copieLigneDocument(ligne, new TaLPrelevement()); 
						temp.setTaDocument(((TaPrelevement)dd));
						((TaPrelevement)dd).addLigne(temp);
					}
					else {
					if(ligne.getGenereLigne()) {
						//récupérer la liste lignesALignes
						ligne.setTaLigneALignes(new HashSet<TaLigneALigne>(taLigneALigneService.findByLDocument(ligne)));

						if(ligne.getQteGenere()==null)ligne.setQteGenere(ligne.getQteLDocument());
						for (TaLigneALigneSupplementDTO lSupp : ligne.getListeSupplement()) {
							TaLPrelevement temp = (TaLPrelevement)copieLigneDocument(ligne, new TaLPrelevement());
							BigDecimal ratio=recupRatioQte(temp.getQteLDocument(),lSupp.getQteRecue());
							if(lSupp.getQteRecue()!=null)temp.setQteLDocument(lSupp.getQteRecue());
							if(ligne.getUniteGenere()!=null)temp.setU1LDocument(ligne.getUniteGenere());
							//calculer la proportion de la qté 2 en fonction de la Qté1 générée
							if(temp.getQte2LDocument()!=null && temp.getQte2LDocument().compareTo(BigDecimal.ZERO)!=0) {
								temp.setQte2LDocument(temp.getQte2LDocument().multiply(ratio));
							}
							if(lSupp.getNumLot()==null ||lSupp.getNumLot().isEmpty()) {
								Map<String, String> params = new LinkedHashMap<String, String>();
								if(((TaPrelevement)dd)!=null && ((TaPrelevement)dd).getTaTiers()!=null)params.put(Const.C_NOMFOURNISSEUR, ((TaPrelevement)dd).getTaTiers().getNomTiers());
								if(((TaPrelevement)dd)!=null && ((TaPrelevement)dd).getTaTiers()!=null)params.put(Const.C_CODEFOURNISSEUR, ((TaPrelevement)dd).getTaTiers().getCodeTiers());

								params.put(Const.C_CODEDOCUMENT, ((TaPrelevement)dd).getCodeDocument());
								params.put(Const.C_DATEDOCUMENT, LibDate.dateToString(((TaPrelevement)dd).getDateDocument()));
								if(ligne.getTaArticle()!=null && !ligne.getTaArticle().getGestionLot()){
									params.put(Const.C_VIRTUEL, "true");
									params.put(Const.C_IDARTICLEVIRTUEL, LibConversion.integerToString(ligne.getTaArticle().getIdArticle()));
								}
								//							String numLot=taLotService.genereCode(params);
								//							lSupp.setNumLot(numLot);
							}
							//						if(lSupp.getNumLot()!=null) {
							//							TaLot lot = new TaLot();
							//							lot.setNumLot(lSupp.getNumLot());
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
							temp.setTaDocument(((TaPrelevement)dd));
							TaLigneALigne lal = new TaLigneALigne();
							lal.setNumLot(lSupp.getNumLot());
							lal.setPrixU(ligne.getPrixULDocument());
							lal.setQte(ligne.getQteLDocument());
							lal.setQteRecue(lSupp.getQteRecue());
							lal.setUnite(ligne.getU1LDocument());
							lal.setUniteRecue(ligne.getUniteGenere());
							lal.setTaLDevis((TaLDevis) ligne);
							lal.setTaLPrelevement(temp);
							lal.setIdLigneSrc(ligne.getIdLDocument());
							temp.getTaLigneALignes().add(lal);
							((TaLDevis) ligne).getTaLigneALignes().add(lal);
							temp.addREtatLigneDoc(etatLigne);
							if(temp.getTaArticle()!=null)temp.setTaArticle(taArticleService.findByIdAvecLazy(temp.getTaArticle().getIdArticle(), false));
							((TaPrelevement)dd).addLigne(temp);
						}

						//génération de la ligne existante si qté # de 0
						if(ligne.getQteGenere()!=null && ligne.getQteGenere().compareTo(BigDecimal.ZERO)!=0) {
							BigDecimal qteTotale=ligne.getQteGenere();
							TaLPrelevement temp = (TaLPrelevement)copieLigneDocument(ligne, new TaLPrelevement());
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


							temp.setTaDocument(((TaPrelevement)dd));
							TaLigneALigne lal = new TaLigneALigne();
							if(ligne.getTaLot()!=null)lal.setNumLot(ligne.getTaLot().getNumLot());
							lal.setPrixU(ligne.getPrixULDocument());
							lal.setQte(ligne.getQteLDocument());
							lal.setQteRecue(ligne.getQteGenere());
							lal.setUnite(ligne.getU1LDocument());
							lal.setUniteRecue(ligne.getUniteGenere());
							lal.setTaLDevis((TaLDevis) ligne);
							lal.setTaLPrelevement(temp);
							lal.setIdLigneSrc(ligne.getIdLDocument());
							temp.getTaLigneALignes().add(lal);
							((TaLDevis) ligne).getTaLigneALignes().add(lal);
							temp.addREtatLigneDoc(etatLigne);

							for (TaLigneALigneSupplementDTO o : ligne.getListeLigneAIntegrer()) {
								//enregistrer le lien entre les lignes en recherchant la vraie ligne d'origine
								TaLigneALigne lal2 = new TaLigneALigne();

								TaLDevis ligneReel=(TaLDevis) o.getLigne();
								lal2.setNumLot(ligne.getNumlotGenere());
								lal2.setPrixU(ligneReel.getPrixULDocument());
								lal2.setQte(ligneReel.getQteLDocument());
								lal2.setQteRecue(o.getQteRecue());
								lal2.setUnite(ligneReel.getU1LDocument());
								lal2.setUniteRecue(ligne.getUniteGenere());
								lal2.setTaLDevis((TaLDevis) ligneReel);
								lal2.setTaLPrelevement(temp);
								lal2.setIdLigneSrc(ligneReel.getIdLDocument());
								temp.getTaLigneALignes().add(lal2);
								((TaLDevis) ligne).getTaLigneALignes().add(lal2);
								temp.addREtatLigneDoc(etatLigne);
							}
							if(temp.getTaArticle()!=null)temp.setTaArticle(taArticleService.findByIdAvecLazy(temp.getTaArticle().getIdArticle(), false));
							((TaPrelevement)dd).addLigne(temp);
						}
					}
					}
				}
			}else {

			for (ILigneDocumentTiers ligne : ((TaDevis)ds).getLignes()) {
				TaLPrelevement temp = (TaLPrelevement)copieLigneDocument(ligne, new TaLPrelevement()); 
				temp.setTaDocument(((TaPrelevement)dd));
				((TaPrelevement)dd).addLigne(temp);
			}
			}
			TaInfosPrelevement infos = (TaInfosPrelevement)copieInfosDocument(((TaDevis)ds).getTaInfosDocument(),new TaInfosPrelevement());
			infos.setTaDocument(((TaPrelevement)dd));
			((TaPrelevement)dd).setTaInfosDocument(infos);

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
		TaDevis documentInit = ((TaDevis)ds);
		try {
			
			if(documentInit!=null){
				documentInit.setLegrain(true);
				if(genereCode && !codeDejaGenere )((TaPrelevement)dd).setCodeDocument(taPrelevementService.genereCode(null));
				((TaPrelevement)dd).setCommentaire(commentaire);
				taPrelevementService.calculDateEcheanceAbstract(((TaPrelevement)dd),taCPaiement.getReportCPaiement(),taCPaiement.getFinMoisCPaiement());
				if(ds.getRelationDocument()){
				TaRDocument taRDocument = new TaRDocument();
				taRDocument.setTaPrelevement(((TaPrelevement)dd));
				taRDocument.setTaDevis(documentInit);
				taRDocument.setIdSrc(documentInit.getIdDocument());
				((TaPrelevement)dd).getTaRDocuments().add(taRDocument);
				}
				((TaPrelevement)dd).calculeTvaEtTotaux();	
			}
			return dd;
		} catch (Exception e) {
			logger.error("",e);
			return null;
		}
	}
	
	public String creationRequeteDejaGenere(IDocumentTiers ds) {
		int idDoc = ds.getIdDocument();
		String jpql = "select x from TaRDocument x where x.taDevis.idDocument="+idDoc+" and x.taPrelevement is not null";
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
