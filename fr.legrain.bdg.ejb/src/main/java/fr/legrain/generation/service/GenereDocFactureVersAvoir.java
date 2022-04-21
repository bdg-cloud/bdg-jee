package fr.legrain.generation.service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import fr.legrain.bdg.documents.service.remote.ITaAvoirServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocFactureVersAvoirServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.IInfosDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.dto.TaLigneALigneSupplementDTO;
import fr.legrain.document.model.TaAvoir;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaInfosAvoir;
import fr.legrain.document.model.TaLAvoir;
import fr.legrain.document.model.TaLFacture;
import fr.legrain.document.model.TaLigneALigne;
import fr.legrain.document.model.TaRDocument;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.lib.data.LibDate;


@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public  class GenereDocFactureVersAvoir extends AbstractGenereDocVersAvoir implements IGenereDocFactureVersAvoirServiceRemote{
	private @EJB ITaAvoirServiceRemote taAvoirService;
	protected TaFacture castDocumentSource() {
		return (TaFacture)ds;
	}
	
	protected TaAvoir castDocumentDest() {
		return (TaAvoir)dd;
	}
	
	@Override
	public IDocumentTiers copieDocumentSpecifique(IDocumentTiers ds, IDocumentTiers dd
			,Boolean ligneSeparatrice,String libelleSeparateur) {
		try {
//			((TaAvoir)dd).setTaTPaiement(((TaFacture)ds).getTaTPaiement());
			((TaAvoir)dd).setDateEchDocument(((TaFacture)ds).getDateEchDocument());
//			((TaAvoir)dd).setRegleDocument(BigDecimal.valueOf(0));
			((TaAvoir)dd).setRemHtDocument(((TaFacture)ds).getRemHtDocument());
			if(multiple)((TaAvoir)dd).setTxRemHtDocument(BigDecimal.valueOf(0));else((TaAvoir)dd).setTxRemHtDocument(((TaFacture)ds).getTxRemHtDocument());
			((TaAvoir)dd).setRemTtcDocument(((TaFacture)ds).getRemTtcDocument());
			((TaAvoir)dd).setTxRemTtcDocument(((TaFacture)ds).getTxRemTtcDocument());
			((TaAvoir)dd).setNbEDocument(((TaFacture)ds).getNbEDocument());
			((TaAvoir)dd).setTtc(((TaFacture)ds).getTtc());
//			((TaAvoir)dd).setExport(((TaFacture)ds).getExport());
			((TaAvoir)dd).setCommentaire(((TaFacture)ds).getCommentaire());


			TaEtat etatLigne=taEtatService.documentEncours(null);
			codeDejaGenere=false;
			

				Map<String, String> paramsCode = new LinkedHashMap<String, String>();
				if((codeTiers==null || codeTiers.isEmpty()) 
						&& ((TaAvoir)dd).getTaTiers()!=null)
					codeTiers=((TaAvoir)dd).getTaTiers().getCodeTiers();
				paramsCode.put(Const.C_NOMFOURNISSEUR, codeTiers);

				paramsCode.put(Const.C_DATEDOCUMENT, LibDate.dateToString(((TaAvoir)dd).getDateDocument()));				
				if(isGenereCode()) {
					((TaAvoir)dd).setCodeDocument(taAvoirService.genereCode(paramsCode));
				codeDejaGenere=true;
				}

				if(!generationModele) {
				for (TaLFacture ligne : ((TaFacture)ds).getLignes()) {
					if(ligne.getTaArticle()==null) {
						TaLAvoir temp = (TaLAvoir)copieLigneDocument(ligne, new TaLAvoir()); 
						temp.setTaDocument(((TaAvoir)dd));
						((TaAvoir)dd).addLigne(temp);
					}
					else {
					if(ligne.getGenereLigne()) {
						//récupérer la liste lignesALignes
//						ligne.setTaLigneALignes(new HashSet<TaLigneALigne>(taLigneALigneService.findByLDocument(ligne)));

						if(ligne.getQteGenere()==null)ligne.setQteGenere(ligne.getQteLDocument());
						for (TaLigneALigneSupplementDTO lSupp : ligne.getListeSupplement()) {
							TaLAvoir temp = (TaLAvoir)copieLigneDocument(ligne, new TaLAvoir());
							BigDecimal ratio=recupRatioQte(temp.getQteLDocument(),lSupp.getQteRecue());
							if(lSupp.getQteRecue()!=null)temp.setQteLDocument(lSupp.getQteRecue());
							if(ligne.getUniteGenere()!=null)temp.setU1LDocument(ligne.getUniteGenere());
							//calculer la proportion de la qté 2 en fonction de la Qté1 générée
							if(temp.getQte2LDocument()!=null && temp.getQte2LDocument().compareTo(BigDecimal.ZERO)!=0) {
								temp.setQte2LDocument(temp.getQte2LDocument().multiply(ratio));
							}
							if(lSupp.getNumLot()==null ||lSupp.getNumLot().isEmpty()) {
								Map<String, String> params = new LinkedHashMap<String, String>();
								if(((TaAvoir)dd)!=null && ((TaAvoir)dd).getTaTiers()!=null)params.put(Const.C_NOMFOURNISSEUR, ((TaAvoir)dd).getTaTiers().getNomTiers());
								if(((TaAvoir)dd)!=null && ((TaAvoir)dd).getTaTiers()!=null)params.put(Const.C_CODEFOURNISSEUR, ((TaAvoir)dd).getTaTiers().getCodeTiers());

								params.put(Const.C_CODEDOCUMENT, ((TaAvoir)dd).getCodeDocument());
								params.put(Const.C_DATEDOCUMENT, LibDate.dateToString(((TaAvoir)dd).getDateDocument()));

							}

							temp.setTaDocument(((TaAvoir)dd));
							TaLigneALigne lal = new TaLigneALigne();
							lal.setNumLot(lSupp.getNumLot());
							lal.setPrixU(ligne.getPrixULDocument());
							lal.setQte(ligne.getQteLDocument());
							lal.setQteRecue(lSupp.getQteRecue());
							lal.setUnite(ligne.getU1LDocument());
							lal.setUniteRecue(ligne.getUniteGenere());
							lal.setTaLFacture((TaLFacture) ligne);
							lal.setTaLAvoir(temp);
							lal.setIdLigneSrc(ligne.getIdLDocument());
							temp.getTaLigneALignes().add(lal);
							((TaLFacture) ligne).getTaLigneALignes().add(lal);
							temp.addREtatLigneDoc(etatLigne);
							if(temp.getTaArticle()!=null)temp.setTaArticle(taArticleService.findByIdAvecLazy(temp.getTaArticle().getIdArticle(), false));
							((TaAvoir)dd).addLigne(temp);
						}

						//génération de la ligne existante si qté # de 0
						if(ligne.getQteGenere()!=null && ligne.getQteGenere().compareTo(BigDecimal.ZERO)!=0) {
							BigDecimal qteTotale=ligne.getQteGenere();
							TaLAvoir temp = (TaLAvoir)copieLigneDocument(ligne, new TaLAvoir());
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


							temp.setTaDocument(((TaAvoir)dd));
							TaLigneALigne lal = new TaLigneALigne();
							if(ligne.getTaLot()!=null)lal.setNumLot(ligne.getTaLot().getNumLot());
							lal.setPrixU(ligne.getPrixULDocument());
							lal.setQte(ligne.getQteLDocument());
							lal.setQteRecue(ligne.getQteGenere());
							lal.setUnite(ligne.getU1LDocument());
							lal.setUniteRecue(ligne.getUniteGenere());
							lal.setTaLFacture((TaLFacture) ligne);
							lal.setTaLAvoir(temp);
							lal.setIdLigneSrc(ligne.getIdLDocument());
							temp.getTaLigneALignes().add(lal);
							((TaLFacture) ligne).getTaLigneALignes().add(lal);
							temp.addREtatLigneDoc(etatLigne);

							for (TaLigneALigneSupplementDTO o : ligne.getListeLigneAIntegrer()) {
								//enregistrer le lien entre les lignes en recherchant la vraie ligne d'origine
								TaLigneALigne lal2 = new TaLigneALigne();

								TaLFacture ligneReel=(TaLFacture) o.getLigne();
								lal2.setNumLot(ligne.getNumlotGenere());
								lal2.setPrixU(ligneReel.getPrixULDocument());
								lal2.setQte(ligneReel.getQteLDocument());
								lal2.setQteRecue(o.getQteRecue());
								lal2.setUnite(ligneReel.getU1LDocument());
								lal2.setUniteRecue(ligne.getUniteGenere());
								lal2.setTaLFacture((TaLFacture) ligneReel);
								lal2.setTaLAvoir(temp);
								lal2.setIdLigneSrc(ligneReel.getIdLDocument());
								temp.getTaLigneALignes().add(lal2);
								((TaLFacture) ligne).getTaLigneALignes().add(lal2);
								temp.addREtatLigneDoc(etatLigne);
							}
							if(temp.getTaArticle()!=null)temp.setTaArticle(taArticleService.findByIdAvecLazy(temp.getTaArticle().getIdArticle(), false));
							((TaAvoir)dd).addLigne(temp);
						}
					}
					}
				}
			}else {

			for (ILigneDocumentTiers ligne : ((TaFacture)ds).getLignes()) {
				TaLAvoir temp = (TaLAvoir)copieLigneDocument(ligne, new TaLAvoir()); 
				temp.setTaDocument(((TaAvoir)dd));
				((TaAvoir)dd).addLigne(temp);
			}
			}

			TaInfosAvoir infos = (TaInfosAvoir)copieInfosDocument(((TaFacture)ds).getTaInfosDocument(),new TaInfosAvoir());
			infos.setTaDocument(((TaAvoir)dd));
			((TaAvoir)dd).setTaInfosDocument(infos);

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
//		if(ld.getTaArticle()!=null && ld.getTaArticle().getTaRTaTitreTransport()!=null && documentGereLesCrd) {
//			((TaLAvoir)ld).setCodeTitreTransport(ls.getTaArticle().getTaRTaTitreTransport().getTaTitreTransport().getCodeTitreTransport());
//			if(((TaLAvoir)ld).getTaArticle().getTaRTaTitreTransport().getQteTitreTransport()!=null) {
//				((TaLAvoir)ld).setQteTitreTransport(((TaLAvoir)ld).getTaArticle().getTaRTaTitreTransport().getQteTitreTransport().multiply(((TaLAvoir)ld).getQteLDocument()));
//			}
//		} 
		if(ld.getTaArticle()!=null && ((TaLFacture)ls).getCodeTitreTransport()!=null  && documentGereLesCrd) {
			((TaLAvoir)ld).setCodeTitreTransport(((TaLFacture)ls).getCodeTitreTransport());
			((TaLAvoir)ld).setQteTitreTransport(((TaLFacture)ls).getQteTitreTransport());			
		} 
		else {
			((TaLAvoir)ld).setCodeTitreTransport("");
			((TaLAvoir)ld).setQteTitreTransport(null);
		}
		return ld;
	}

	@Override
	public IDocumentTiers genereDocumentSpecifique(IDocumentTiers ds, IDocumentTiers dd,String codeTiers, boolean generationModele,boolean genereCode) {
		TaFacture documentInit = ((TaFacture)ds);
		try {
			
			if(documentInit!=null){
				documentInit.setLegrain(true);
				if(genereCode && !codeDejaGenere )((TaAvoir)dd).setCodeDocument(taAvoirService.genereCode(null));
				((TaAvoir)dd).setCommentaire(commentaire);
				taAvoirService.calculDateEcheanceAbstract(((TaAvoir)dd),taCPaiement.getReportCPaiement(),taCPaiement.getFinMoisCPaiement());
				if(ds.getRelationDocument()){
				TaRDocument taRDocument = new TaRDocument();
				taRDocument.setTaAvoir(((TaAvoir)dd));
				taRDocument.setTaFacture(documentInit);
				taRDocument.setIdSrc(documentInit.getIdDocument());
				((TaAvoir)dd).getTaRDocuments().add(taRDocument);
				}
				((TaAvoir)dd).calculeTvaEtTotaux();	
			}
			return dd;
		} catch (Exception e) {
			logger.error("",e);
			return null;
		}
	}

	public String creationRequeteDejaGenere(IDocumentTiers ds) {
		int idDoc = ds.getIdDocument();
		String jpql = "select x from TaRDocument x where x.taFacture.idDocument="+idDoc+" and x.taAvoir is not null";
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
