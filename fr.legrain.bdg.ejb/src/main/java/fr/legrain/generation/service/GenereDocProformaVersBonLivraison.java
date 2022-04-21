package fr.legrain.generation.service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import fr.legrain.bdg.documents.service.remote.ITaBonlivServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocProformaVersBonLivraisonServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.IInfosDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.dto.TaLigneALigneSupplementDTO;
import fr.legrain.document.model.TaBonliv;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaInfosBonliv;
import fr.legrain.document.model.TaLBonliv;
import fr.legrain.document.model.TaLProforma;
import fr.legrain.document.model.TaLigneALigne;
import fr.legrain.document.model.TaProforma;
import fr.legrain.document.model.TaRDocument;
import fr.legrain.document.model.TaRReglementLiaison;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.lib.data.IHMEtat;
import fr.legrain.lib.data.LibDate;


@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public  class GenereDocProformaVersBonLivraison extends AbstractGenereDocVersBonLivraison implements IGenereDocProformaVersBonLivraisonServiceRemote{
	private @EJB ITaBonlivServiceRemote taBonlivService;
	protected TaProforma castDocumentSource() {
		return (TaProforma)ds;
	}
	
	protected TaBonliv castDocumentDest() {
		return (TaBonliv)dd;
	}
	
	@Override
	public IDocumentTiers copieDocumentSpecifique(IDocumentTiers ds, IDocumentTiers dd
			,Boolean ligneSeparatrice,String libelleSeparateur) {
		try {
			((TaBonliv)dd).setTaTPaiement(((TaProforma)ds).getTaTPaiement());
//			((TaBonliv)dd).setDateEchDocument(((TaProforma)ds).getDateEchDocument());
//			((TaBonliv)dd).setRegleDocument(BigDecimal.valueOf(0));
			((TaBonliv)dd).setRemHtDocument(((TaProforma)ds).getRemHtDocument());
			if(multiple)((TaBonliv)dd).setTxRemHtDocument(BigDecimal.valueOf(0));else((TaBonliv)dd).setTxRemHtDocument(((TaProforma)ds).getTxRemHtDocument());
			((TaBonliv)dd).setRemTtcDocument(((TaProforma)ds).getRemTtcDocument());
			((TaBonliv)dd).setTxRemTtcDocument(((TaProforma)ds).getTxRemTtcDocument());
			((TaBonliv)dd).setNbEDocument(((TaProforma)ds).getNbEDocument());
			((TaBonliv)dd).setTtc(((TaProforma)ds).getTtc());
//			((TaBonliv)dd).setExport(((TaProforma)ds).getExport());
			((TaBonliv)dd).setCommentaire(((TaProforma)ds).getCommentaire());


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
				for (ILigneDocumentTiers ligne : ((TaProforma)ds).getLignes()) {
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

							}

							temp.setTaDocument(((TaBonliv)dd));
							TaLigneALigne lal = new TaLigneALigne();
							lal.setNumLot(lSupp.getNumLot());
							lal.setPrixU(ligne.getPrixULDocument());
							lal.setQte(ligne.getQteLDocument());
							lal.setQteRecue(lSupp.getQteRecue());
							lal.setUnite(ligne.getU1LDocument());
							lal.setUniteRecue(ligne.getUniteGenere());
							lal.setTaLProforma((TaLProforma) ligne);
							lal.setTaLBonliv(temp);
							lal.setIdLigneSrc(ligne.getIdLDocument());
							temp.getTaLigneALignes().add(lal);
							((TaLProforma) ligne).getTaLigneALignes().add(lal);
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
							lal.setTaLProforma((TaLProforma) ligne);
							lal.setTaLBonliv(temp);
							lal.setIdLigneSrc(ligne.getIdLDocument());
							temp.getTaLigneALignes().add(lal);
							((TaLProforma) ligne).getTaLigneALignes().add(lal);
							temp.addREtatLigneDoc(etatLigne);

							for (TaLigneALigneSupplementDTO o : ligne.getListeLigneAIntegrer()) {
								//enregistrer le lien entre les lignes en recherchant la vraie ligne d'origine
								TaLigneALigne lal2 = new TaLigneALigne();

								TaLProforma ligneReel=(TaLProforma) o.getLigne();
								lal2.setNumLot(ligne.getNumlotGenere());
								lal2.setPrixU(ligneReel.getPrixULDocument());
								lal2.setQte(ligneReel.getQteLDocument());
								lal2.setQteRecue(o.getQteRecue());
								lal2.setUnite(ligneReel.getU1LDocument());
								lal2.setUniteRecue(ligne.getUniteGenere());
								lal2.setTaLProforma((TaLProforma) ligneReel);
								lal2.setTaLBonliv(temp);
								lal2.setIdLigneSrc(ligneReel.getIdLDocument());
								temp.getTaLigneALignes().add(lal2);
								((TaLProforma) ligne).getTaLigneALignes().add(lal2);
								temp.addREtatLigneDoc(etatLigne);
							}
							if(temp.getTaArticle()!=null)temp.setTaArticle(taArticleService.findByIdAvecLazy(temp.getTaArticle().getIdArticle(), false));
							((TaBonliv)dd).addLigne(temp);
						}
					}
					}
				}
			}else {

			for (ILigneDocumentTiers ligne : ((TaProforma)ds).getLignes()) {
				TaLBonliv temp = (TaLBonliv)copieLigneDocument(ligne, new TaLBonliv()); 
				temp.setTaDocument(((TaBonliv)dd));
				((TaBonliv)dd).addLigne(temp);
			}
			}
			TaInfosBonliv infos = (TaInfosBonliv)copieInfosDocument(((TaProforma)ds).getTaInfosDocument(),new TaInfosBonliv());
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
		if(ld.getTaArticle()!=null && ((TaLProforma)ls).getCodeTitreTransport()!=null ) {
			((TaLBonliv)ld).setCodeTitreTransport(((TaLProforma)ls).getCodeTitreTransport());
			((TaLBonliv)ld).setQteTitreTransport(((TaLProforma)ls).getQteTitreTransport());			
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
		TaProforma documentInit = ((TaProforma)ds);
		try {
			
			if(documentInit!=null){
				documentInit.setLegrain(true);
				if(genereCode && !codeDejaGenere )((TaBonliv)dd).setCodeDocument(taBonlivService.genereCode(null));
				((TaBonliv)dd).setCommentaire(commentaire);
				taBonlivService.calculDateEcheanceAbstract(((TaBonliv)dd),taCPaiement.getReportCPaiement(),taCPaiement.getFinMoisCPaiement());
				if(ds.getRelationDocument()){
				TaRDocument taRDocument = new TaRDocument();
				taRDocument.setTaBonliv(((TaBonliv)dd));
				taRDocument.setTaProforma(documentInit);
				taRDocument.setIdSrc(documentInit.getIdDocument());
				((TaBonliv)dd).getTaRDocuments().add(taRDocument);
				}
				((TaBonliv)dd).calculeTvaEtTotaux();	
				
				if(!generationModele){
				if(ds.getTaRReglementLiaisons()!=null && ds.getTaRReglementLiaisons().size()>0) {
					for (TaRReglementLiaison rr : ds.getTaRReglementLiaisons()) {
						TaRReglementLiaison taRReglement = new TaRReglementLiaison();  
						
						taRReglement.setTaReglement(rr.getTaReglement());
						taRReglement.setAffectation(rr.getAffectation());
						taRReglement.setTaBonliv((TaBonliv)dd);
						taRReglement.setEtat(IHMEtat.integre);
						((TaBonliv)dd).addRReglementLiaison(taRReglement);
					}
				}
			}
			((TaBonliv)dd).calculRegleDocument();
			((TaBonliv)dd).calculRegleDocumentComplet();
			((TaBonliv)dd).setNetAPayer(((TaBonliv)dd).calculResteAReglerComplet());
			
			}
			return dd;
		} catch (Exception e) {
			logger.error("",e);
			return null;
		}
	}
	
	public String creationRequeteDejaGenere(IDocumentTiers ds) {
		int idDoc = ds.getIdDocument();
		String jpql = "select x from TaRDocument x where x.taProforma.idDocument="+idDoc+" and x.taBonliv is not null";
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
		return super.genereDocument(document, docGenere, codeTiers, b, generationModele,genereCode, multiple);
	}

	@Override
	public void setRelationDocument(boolean relationDocument) {
		super.setRelationDocument(relationDocument);
		
	}



}
