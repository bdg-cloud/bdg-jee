package fr.legrain.generation.service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import fr.legrain.bdg.documents.service.remote.ITaBoncdeServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocDevisVersBonCommandeServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.IInfosDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.dto.TaLigneALigneSupplementDTO;
import fr.legrain.document.model.TaBoncde;
import fr.legrain.document.model.TaDevis;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaInfosBoncde;
import fr.legrain.document.model.TaLBoncde;
import fr.legrain.document.model.TaLDevis;
import fr.legrain.document.model.TaLFacture;
import fr.legrain.document.model.TaLigneALigne;
import fr.legrain.document.model.TaRDocument;
import fr.legrain.document.model.TaRReglementLiaison;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.lib.data.IHMEtat;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.LibDate;


@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public  class GenereDocDevisVersBonCommande extends AbstractGenereDocVersBonCommande implements IGenereDocDevisVersBonCommandeServiceRemote{
	private @EJB ITaBoncdeServiceRemote taBoncdeService;
	protected TaDevis castDocumentSource() {
		return (TaDevis)ds;
	}
	
	protected TaBoncde castDocumentDest() {
		return (TaBoncde)dd;
	}
	
	@Override
	public IDocumentTiers copieDocumentSpecifique(IDocumentTiers ds, IDocumentTiers dd
			,Boolean ligneSeparatrice,String libelleSeparateur) {
		try {
			((TaBoncde)dd).setTaTPaiement(((TaDevis)ds).getTaTPaiement());
			((TaBoncde)dd).setDateEchDocument(((TaDevis)ds).getDateEchDocument());
			//			((TaBoncde)dd).setRegleDocument(BigDecimal.valueOf(0));
			((TaBoncde)dd).setRemHtDocument(((TaDevis)ds).getRemHtDocument());
			if(multiple)((TaBoncde)dd).setTxRemHtDocument(BigDecimal.valueOf(0));else((TaBoncde)dd).setTxRemHtDocument(((TaDevis)ds).getTxRemHtDocument());
			((TaBoncde)dd).setRemTtcDocument(((TaDevis)ds).getRemTtcDocument());
			((TaBoncde)dd).setTxRemTtcDocument(((TaDevis)ds).getTxRemTtcDocument());
			((TaBoncde)dd).setNbEDocument(((TaDevis)ds).getNbEDocument());
			((TaBoncde)dd).setTtc(((TaDevis)ds).getTtc());
			((TaBoncde)dd).setDateExport(((TaDevis)ds).getDateExport());
			((TaBoncde)dd).setCommentaire(((TaDevis)ds).getCommentaire());

			TaEtat etatLigne=taEtatService.documentEncours(null);
			codeDejaGenere=false;
			

				Map<String, String> paramsCode = new LinkedHashMap<String, String>();
				if((codeTiers==null || codeTiers.isEmpty()) 
						&& ((TaBoncde)dd).getTaTiers()!=null)
					codeTiers=((TaBoncde)dd).getTaTiers().getCodeTiers();
				paramsCode.put(Const.C_NOMFOURNISSEUR, codeTiers);

				paramsCode.put(Const.C_DATEDOCUMENT, LibDate.dateToString(((TaBoncde)dd).getDateDocument()));				
				if(isGenereCode()) {
					((TaBoncde)dd).setCodeDocument(taBoncdeService.genereCode(paramsCode));
					codeDejaGenere=true;
				}

				if(!generationModele) {
				for (ILigneDocumentTiers ligne : ((TaDevis)ds).getLignes()) {
					if(ligne.getTaArticle()==null) {
						TaLBoncde temp = (TaLBoncde)copieLigneDocument(ligne, new TaLBoncde()); 
						temp.setTaDocument(((TaBoncde)dd));
						((TaBoncde)dd).addLigne(temp);
					}
					else {
					if(ligne.getGenereLigne()) {
						//récupérer la liste lignesALignes
						ligne.setTaLigneALignes(new HashSet<TaLigneALigne>(taLigneALigneService.findByLDocument(ligne)));

						if(ligne.getQteGenere()==null)ligne.setQteGenere(ligne.getQteLDocument());
						for (TaLigneALigneSupplementDTO lSupp : ligne.getListeSupplement()) {
							TaLBoncde temp = (TaLBoncde)copieLigneDocument(ligne, new TaLBoncde());
							BigDecimal ratio=recupRatioQte(temp.getQteLDocument(),lSupp.getQteRecue());
							if(lSupp.getQteRecue()!=null)temp.setQteLDocument(lSupp.getQteRecue());
							if(ligne.getUniteGenere()!=null)temp.setU1LDocument(ligne.getUniteGenere());
							//calculer la proportion de la qté 2 en fonction de la Qté1 générée
							if(temp.getQte2LDocument()!=null && temp.getQte2LDocument().compareTo(BigDecimal.ZERO)!=0) {
								temp.setQte2LDocument(temp.getQte2LDocument().multiply(ratio));
							}
							if(lSupp.getNumLot()==null ||lSupp.getNumLot().isEmpty()) {
								Map<String, String> params = new LinkedHashMap<String, String>();
								if(((TaBoncde)dd)!=null && ((TaBoncde)dd).getTaTiers()!=null)params.put(Const.C_NOMFOURNISSEUR, ((TaBoncde)dd).getTaTiers().getNomTiers());
								if(((TaBoncde)dd)!=null && ((TaBoncde)dd).getTaTiers()!=null)params.put(Const.C_CODEFOURNISSEUR, ((TaBoncde)dd).getTaTiers().getCodeTiers());

								params.put(Const.C_CODEDOCUMENT, ((TaBoncde)dd).getCodeDocument());
								params.put(Const.C_DATEDOCUMENT, LibDate.dateToString(((TaBoncde)dd).getDateDocument()));
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
							temp.setTaDocument(((TaBoncde)dd));
							TaLigneALigne lal = new TaLigneALigne();
							lal.setNumLot(lSupp.getNumLot());
							lal.setPrixU(ligne.getPrixULDocument());
							lal.setQte(ligne.getQteLDocument());
							lal.setQteRecue(lSupp.getQteRecue());
							lal.setUnite(ligne.getU1LDocument());
							lal.setUniteRecue(ligne.getUniteGenere());
							lal.setTaLDevis((TaLDevis) ligne);
							lal.setTaLBoncde(temp);
							lal.setIdLigneSrc(ligne.getIdLDocument());
							temp.getTaLigneALignes().add(lal);
							((TaLDevis) ligne).getTaLigneALignes().add(lal);
							temp.addREtatLigneDoc(etatLigne);
							if(temp.getTaArticle()!=null)temp.setTaArticle(taArticleService.findByIdAvecLazy(temp.getTaArticle().getIdArticle(), false));
							((TaBoncde)dd).addLigne(temp);
						}

						//génération de la ligne existante si qté # de 0
						if(ligne.getQteGenere()!=null && ligne.getQteGenere().compareTo(BigDecimal.ZERO)!=0) {
							BigDecimal qteTotale=ligne.getQteGenere();
							TaLBoncde temp = (TaLBoncde)copieLigneDocument(ligne, new TaLBoncde());
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


							temp.setTaDocument(((TaBoncde)dd));
							TaLigneALigne lal = new TaLigneALigne();
							if(ligne.getTaLot()!=null)lal.setNumLot(ligne.getTaLot().getNumLot());
							lal.setPrixU(ligne.getPrixULDocument());
							lal.setQte(ligne.getQteLDocument());
							lal.setQteRecue(ligne.getQteGenere());
							lal.setUnite(ligne.getU1LDocument());
							lal.setUniteRecue(ligne.getUniteGenere());
							lal.setTaLDevis((TaLDevis) ligne);
							lal.setTaLBoncde(temp);
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
								lal2.setTaLBoncde(temp);
								lal2.setIdLigneSrc(ligneReel.getIdLDocument());
								temp.getTaLigneALignes().add(lal2);
								((TaLDevis) ligne).getTaLigneALignes().add(lal2);
								temp.addREtatLigneDoc(etatLigne);
							}
							if(temp.getTaArticle()!=null)temp.setTaArticle(taArticleService.findByIdAvecLazy(temp.getTaArticle().getIdArticle(), false));
							((TaBoncde)dd).addLigne(temp);
						}
					}
					}
				}
			}else {
				for (ILigneDocumentTiers ligne : ((TaDevis)ds).getLignes()) {
					TaLBoncde temp = (TaLBoncde)copieLigneDocument(ligne, new TaLBoncde()); 
					temp.setTaDocument(((TaBoncde)dd));
					((TaBoncde)dd).addLigne(temp);
				}
			}
			TaInfosBoncde infos = (TaInfosBoncde)copieInfosDocument(((TaDevis)ds).getTaInfosDocument(),new TaInfosBoncde());
			infos.setTaDocument(((TaBoncde)dd));
			((TaBoncde)dd).setTaInfosDocument(infos);

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
		if(ld.getTaArticle()!=null && ((TaLDevis)ls).getCodeTitreTransport()!=null ) {
			((TaLBoncde)ld).setCodeTitreTransport(((TaLDevis)ls).getCodeTitreTransport());
			((TaLBoncde)ld).setQteTitreTransport(((TaLDevis)ls).getQteTitreTransport());			
		}
		else
			//sinon, si l'article attend de la crd, on la met dans doc dest
			if(ld.getTaArticle()!=null && ld.getTaArticle().getTaRTaTitreTransport()!=null) {
				((TaLBoncde)ld).setCodeTitreTransport(ls.getTaArticle().getTaRTaTitreTransport().getTaTitreTransport().getCodeTitreTransport());
				if(((TaLBoncde)ld).getTaArticle().getTaRTaTitreTransport().getQteTitreTransport()!=null) {
					((TaLBoncde)ld).setQteTitreTransport(((TaLBoncde)ld).getTaArticle().getTaRTaTitreTransport().getQteTitreTransport().multiply(((TaLBoncde)ld).getQteLDocument()));
				}
			}		
	return ld;
}

	@Override
	public IDocumentTiers genereDocumentSpecifique(IDocumentTiers ds, IDocumentTiers dd,String codeTiers, boolean generationModele,boolean genereCode) {
		TaDevis documentInit = ((TaDevis)ds);
		try {
			
			if(documentInit!=null){
				documentInit.setLegrain(true);
				if(genereCode && !codeDejaGenere )((TaBoncde)dd).setCodeDocument(taBoncdeService.genereCode(null));
				((TaBoncde)dd).setCommentaire(commentaire);
				taBoncdeService.calculDateEcheanceAbstract(((TaBoncde)dd),taCPaiement.getReportCPaiement(),taCPaiement.getFinMoisCPaiement());
				if(ds.getRelationDocument()){
				TaRDocument taRDocument = new TaRDocument();
				taRDocument.setTaBoncde(((TaBoncde)dd));
				taRDocument.setTaDevis(documentInit);
				taRDocument.setIdSrc(documentInit.getIdDocument());
				((TaBoncde)dd).getTaRDocuments().add(taRDocument);
				}
				((TaBoncde)dd).calculeTvaEtTotaux();	
				
				if(!generationModele){
				if(ds.getTaRReglementLiaisons()!=null && ds.getTaRReglementLiaisons().size()>0) {
					for (TaRReglementLiaison rr : ds.getTaRReglementLiaisons()) {
						TaRReglementLiaison taRReglement = new TaRReglementLiaison(); 
						
						taRReglement.setTaReglement(rr.getTaReglement());
						taRReglement.setAffectation(rr.getAffectation());
						taRReglement.setTaBoncde((TaBoncde)dd);
						taRReglement.setEtat(IHMEtat.integre);
						((TaBoncde)dd).addRReglementLiaison(taRReglement);
					}
				}
			}
			((TaBoncde)dd).calculRegleDocument();
			((TaBoncde)dd).calculRegleDocumentComplet();
			((TaBoncde)dd).setNetAPayer(((TaBoncde)dd).calculResteAReglerComplet());

			
			}
			return dd;
		} catch (Exception e) {
			logger.error("",e);
			return null;
		}
	}
	
	public String creationRequeteDejaGenere(IDocumentTiers ds) {
		int idDoc = ds.getIdDocument();
		String jpql = "select x from TaRDocument x where x.taDevis.idDocument="+idDoc+" and x.taBoncde is not null";
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
