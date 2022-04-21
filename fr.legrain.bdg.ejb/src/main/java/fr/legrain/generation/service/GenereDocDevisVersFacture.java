package fr.legrain.generation.service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import fr.legrain.bdg.documents.service.remote.ITaFactureServiceRemote;
import fr.legrain.bdg.generation.service.remote.IAbstractGenereDocServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocDevisVersFactureServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.IInfosDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.dto.TaLigneALigneSupplementDTO;
import fr.legrain.document.model.TaDevis;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaInfosFacture;
import fr.legrain.document.model.TaLAcompte;
import fr.legrain.document.model.TaLDevis;
import fr.legrain.document.model.TaLFacture;
import fr.legrain.document.model.TaLigneALigne;
import fr.legrain.document.model.TaRDocument;
import fr.legrain.document.model.TaRReglement;
import fr.legrain.document.model.TaRReglementLiaison;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.lib.data.IHMEtat;
import fr.legrain.lib.data.LibDate;


@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public  class GenereDocDevisVersFacture extends AbstractGenereDocVersFacture implements IGenereDocDevisVersFactureServiceRemote{
	//	private @EJB ITaFactureServiceRemote taFactureService;
	//	public GenereDocDevisVersFacture(ITaFactureServiceRemote taFactureService) {
	//		super(taFactureService);
	//		// TODO Auto-generated constructor stub
	//	}

	protected TaDevis castDocumentSource() {
		return (TaDevis)ds;
	}

	protected TaFacture castDocumentDest() {
		return (TaFacture)dd;
	}

	@Override
	public IDocumentTiers copieDocumentSpecifique(IDocumentTiers ds, IDocumentTiers dd
			,Boolean ligneSeparatrice,String libelleSeparateur) {
		try {
			//			((TaFacture)dd).setTaTPaiement(((TaDevis)ds).getTaTPaiement());
			((TaFacture)dd).setDateEchDocument(((TaDevis)ds).getDateEchDocument());
			//((TaFacture)dd).setRegleDocument(BigDecimal.valueOf(0));
			//isa le 4/03/2010 suite à demande philippe
			((TaFacture)dd).setRegleDocument(((TaDevis)ds).getRegleDocument());
			//
			((TaFacture)dd).setRemHtDocument(((TaDevis)ds).getRemHtDocument());
			if(multiple)((TaFacture)dd).setTxRemHtDocument(BigDecimal.valueOf(0));else((TaFacture)dd).setTxRemHtDocument(((TaDevis)ds).getTxRemHtDocument());
			((TaFacture)dd).setRemTtcDocument(((TaDevis)ds).getRemTtcDocument());
			((TaFacture)dd).setTxRemTtcDocument(((TaDevis)ds).getTxRemTtcDocument());
			((TaFacture)dd).setNbEDocument(((TaDevis)ds).getNbEDocument());
			((TaFacture)dd).setTtc(((TaDevis)ds).getTtc());
			((TaFacture)dd).setDateExport(((TaDevis)ds).getDateExport());
			((TaFacture)dd).setCommentaire(((TaDevis)ds).getCommentaire());

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

				if(!generationModele) {
				for (ILigneDocumentTiers ligne : ((TaDevis)ds).getLignes()) {
					if(ligne.getTaArticle()==null) {
						TaLFacture temp = (TaLFacture)copieLigneDocument(ligne, new TaLFacture()); 
						temp.setTaDocument(((TaFacture)dd));
						((TaFacture)dd).addLigne(temp);
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

							}

							temp.setTaDocument(((TaFacture)dd));
							TaLigneALigne lal = new TaLigneALigne();
							lal.setNumLot(lSupp.getNumLot());
							lal.setPrixU(ligne.getPrixULDocument());
							lal.setQte(ligne.getQteLDocument());
							lal.setQteRecue(lSupp.getQteRecue());
							lal.setUnite(ligne.getU1LDocument());
							lal.setUniteRecue(ligne.getUniteGenere());
							lal.setTaLDevis((TaLDevis) ligne);
							lal.setTaLFacture(temp);
							lal.setIdLigneSrc(ligne.getIdLDocument());
							temp.getTaLigneALignes().add(lal);
							((TaLDevis) ligne).getTaLigneALignes().add(lal);
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


							temp.setTaDocument(((TaFacture)dd));
							TaLigneALigne lal = new TaLigneALigne();
							if(ligne.getTaLot()!=null)lal.setNumLot(ligne.getTaLot().getNumLot());
							lal.setPrixU(ligne.getPrixULDocument());
							lal.setQte(ligne.getQteLDocument());
							lal.setQteRecue(ligne.getQteGenere());
							lal.setUnite(ligne.getU1LDocument());
							lal.setUniteRecue(ligne.getUniteGenere());
							lal.setTaLDevis((TaLDevis) ligne);
							lal.setTaLFacture(temp);
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
								lal2.setTaLFacture(temp);
								lal2.setIdLigneSrc(ligneReel.getIdLDocument());
								temp.getTaLigneALignes().add(lal2);
								((TaLDevis) ligne).getTaLigneALignes().add(lal2);
								temp.addREtatLigneDoc(etatLigne);
							}
							if(temp.getTaArticle()!=null)temp.setTaArticle(taArticleService.findByIdAvecLazy(temp.getTaArticle().getIdArticle(), false));
							((TaFacture)dd).addLigne(temp);
						}
					}
					}
				}
			}else {
				for (ILigneDocumentTiers ligne : ((TaDevis)ds).getLignes()) {
					TaLFacture temp = (TaLFacture)copieLigneDocument(ligne, new TaLFacture()); 
					temp.setTaDocument(((TaFacture)dd));
					((TaFacture)dd).addLigne(temp);
				}
			}
			TaInfosFacture infos = (TaInfosFacture)copieInfosDocument(((TaDevis)ds).getTaInfosDocument(),new TaInfosFacture());
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
		if(ld.getTaArticle()!=null && ((TaLDevis)ls).getCodeTitreTransport()!=null ) {
			((TaLFacture)ld).setCodeTitreTransport(((TaLDevis)ls).getCodeTitreTransport());
			((TaLFacture)ld).setQteTitreTransport(((TaLDevis)ls).getQteTitreTransport());			
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
		TaDevis documentInit = ((TaDevis)ds);
		try {

			if(documentInit!=null){
				documentInit.setLegrain(true);
				if(genereCode && !codeDejaGenere )((TaFacture)dd).setCodeDocument(taFactureService.genereCode(null));
				((TaFacture)dd).setCommentaire(commentaire);
				taFactureService.calculDateEcheanceAbstract(((TaFacture)dd),taCPaiement.getReportCPaiement(),taCPaiement.getFinMoisCPaiement());
				if(ds.getRelationDocument()){
					TaRDocument taRDocument = new TaRDocument();
					taRDocument.setTaFacture(((TaFacture)dd));
					taRDocument.setTaDevis(documentInit);
					taRDocument.setIdSrc(documentInit.getIdDocument());
					((TaFacture)dd).getTaRDocuments().add(taRDocument);
				}
				((TaFacture)dd).calculeTvaEtTotaux();	
				
				if(!generationModele){
				if(ds.getTaRReglementLiaisons()!=null && ds.getTaRReglementLiaisons().size()>0) {
					for (TaRReglementLiaison rrl : ds.getTaRReglementLiaisons()) {
						TaRReglement taRReglement = new TaRReglement();  
						
						taRReglement.setTaReglement(rrl.getTaReglement());
						taRReglement.setAffectation(rrl.getAffectation());
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
		String jpql = "select x from TaRDocument x where x.taDevis.idDocument="+idDoc+" and x.taFacture is not null";
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

