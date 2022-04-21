package fr.legrain.generation.service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import fr.legrain.bdg.documents.service.remote.ITaPreparationServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocBonCommandeVersPreparation;
import fr.legrain.bdg.generation.service.remote.IGenereDocBonCommandeVersPreparationServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.IInfosDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.dto.TaLigneALigneSupplementDTO;
import fr.legrain.document.model.TaBoncde;
import fr.legrain.document.model.TaPreparation;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaInfosPreparation;
import fr.legrain.document.model.TaLBoncde;
import fr.legrain.document.model.TaLPreparation;
import fr.legrain.document.model.TaLigneALigne;
import fr.legrain.document.model.TaRDocument;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.lib.data.LibDate;



@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public  class GenereDocBonCommandeVersPreparation extends AbstractGenereDocVersBonLivraison implements IGenereDocBonCommandeVersPreparationServiceRemote{
	private @EJB ITaPreparationServiceRemote taPreparationService;
	protected TaBoncde castDocumentSource() {
		return (TaBoncde)ds;
	}
	
	protected TaPreparation castDocumentDest() {
		return (TaPreparation)dd;
	}
	
	@Override
	public IDocumentTiers copieDocumentSpecifique(IDocumentTiers ds, IDocumentTiers dd
			,Boolean ligneSeparatrice,String libelleSeparateur) {
		try {
			((TaPreparation)dd).setTaTPaiement(((TaBoncde)ds).getTaTPaiement());
//			((TaPreparation)dd).setDateEchDocument(((TaBoncde)ds).getDateEchDocument());
//			((TaPreparation)dd).setRegleDocument(BigDecimal.valueOf(0));
			((TaPreparation)dd).setRemHtDocument(((TaBoncde)ds).getRemHtDocument());
			if(multiple)((TaPreparation)dd).setTxRemHtDocument(BigDecimal.valueOf(0));else((TaPreparation)dd).setTxRemHtDocument(((TaBoncde)ds).getTxRemHtDocument());
			((TaPreparation)dd).setRemTtcDocument(((TaBoncde)ds).getRemTtcDocument());
			((TaPreparation)dd).setTxRemTtcDocument(((TaBoncde)ds).getTxRemTtcDocument());
			((TaPreparation)dd).setNbEDocument(((TaBoncde)ds).getNbEDocument());
			((TaPreparation)dd).setTtc(((TaBoncde)ds).getTtc());
//			((TaPreparation)dd).setExport(((TaBoncde)ds).getExport());
			((TaPreparation)dd).setCommentaire(((TaBoncde)ds).getCommentaire());
			((TaPreparation)dd).setNumeroCommandeFournisseur(((TaBoncde)ds).getNumeroCommandeFournisseur());
			((TaPreparation)dd).setTaTransporteur(((TaBoncde)ds).getTaTransporteur());

			
			TaEtat etatLigne=taEtatService.documentEncours(null);
			codeDejaGenere=false;
			

				Map<String, String> paramsCode = new LinkedHashMap<String, String>();
				if((codeTiers==null || codeTiers.isEmpty()) 
						&& ((TaPreparation)dd).getTaTiers()!=null)
					codeTiers=((TaPreparation)dd).getTaTiers().getCodeTiers();
				paramsCode.put(Const.C_NOMFOURNISSEUR, codeTiers);

				paramsCode.put(Const.C_DATEDOCUMENT, LibDate.dateToString(((TaPreparation)dd).getDateDocument()));				
				if(isGenereCode()) {
					((TaPreparation)dd).setCodeDocument(taPreparationService.genereCode(paramsCode));
					codeDejaGenere=true;
				}

				if(!generationModele) {
				for (ILigneDocumentTiers ligne : ((TaBoncde)ds).getLignes()) {
					if(ligne.getTaArticle()==null) {
						TaLPreparation temp = (TaLPreparation)copieLigneDocument(ligne, new TaLPreparation()); 
						temp.setTaDocument(((TaPreparation)dd));
						((TaPreparation)dd).addLigne(temp);
					}
					else {
					if(ligne.getGenereLigne()) {
						//récupérer la liste lignesALignes
						ligne.setTaLigneALignes(new HashSet<TaLigneALigne>(taLigneALigneService.findByLDocument(ligne)));

						if(ligne.getQteGenere()==null)ligne.setQteGenere(ligne.getQteLDocument());
						for (TaLigneALigneSupplementDTO lSupp : ligne.getListeSupplement()) {
							TaLPreparation temp = (TaLPreparation)copieLigneDocument(ligne, new TaLPreparation());
							BigDecimal ratio=recupRatioQte(temp.getQteLDocument(),lSupp.getQteRecue());
							if(lSupp.getQteRecue()!=null)temp.setQteLDocument(lSupp.getQteRecue());
							if(ligne.getUniteGenere()!=null)temp.setU1LDocument(ligne.getUniteGenere());
							//calculer la proportion de la qté 2 en fonction de la Qté1 générée
							if(temp.getQte2LDocument()!=null && temp.getQte2LDocument().compareTo(BigDecimal.ZERO)!=0) {
								temp.setQte2LDocument(temp.getQte2LDocument().multiply(ratio));
							}
							if(lSupp.getNumLot()==null ||lSupp.getNumLot().isEmpty()) {
								Map<String, String> params = new LinkedHashMap<String, String>();
								if(((TaPreparation)dd)!=null && ((TaPreparation)dd).getTaTiers()!=null)params.put(Const.C_NOMFOURNISSEUR, ((TaPreparation)dd).getTaTiers().getNomTiers());
								if(((TaPreparation)dd)!=null && ((TaPreparation)dd).getTaTiers()!=null)params.put(Const.C_CODEFOURNISSEUR, ((TaPreparation)dd).getTaTiers().getCodeTiers());

								params.put(Const.C_CODEDOCUMENT, ((TaPreparation)dd).getCodeDocument());
								params.put(Const.C_DATEDOCUMENT, LibDate.dateToString(((TaPreparation)dd).getDateDocument()));

							}

							temp.setTaDocument(((TaPreparation)dd));
							TaLigneALigne lal = new TaLigneALigne();
							lal.setNumLot(lSupp.getNumLot());
							lal.setPrixU(ligne.getPrixULDocument());
							lal.setQte(ligne.getQteLDocument());
							lal.setQteRecue(lSupp.getQteRecue());
							lal.setUnite(ligne.getU1LDocument());
							lal.setUniteRecue(ligne.getUniteGenere());
							lal.setTaLBoncde((TaLBoncde) ligne);
							lal.setTaLPreparation(temp);
							lal.setIdLigneSrc(ligne.getIdLDocument());
							temp.getTaLigneALignes().add(lal);
							((TaLBoncde) ligne).getTaLigneALignes().add(lal);
							temp.addREtatLigneDoc(etatLigne);
							if(temp.getTaArticle()!=null)temp.setTaArticle(taArticleService.findByIdAvecLazy(temp.getTaArticle().getIdArticle(), false));
							((TaPreparation)dd).addLigne(temp);
						}

						//génération de la ligne existante si qté # de 0
						if(ligne.getQteGenere()!=null && ligne.getQteGenere().compareTo(BigDecimal.ZERO)!=0) {
							BigDecimal qteTotale=ligne.getQteGenere();
							TaLPreparation temp = (TaLPreparation)copieLigneDocument(ligne, new TaLPreparation());
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


							temp.setTaDocument(((TaPreparation)dd));
							TaLigneALigne lal = new TaLigneALigne();
							if(ligne.getTaLot()!=null)lal.setNumLot(ligne.getTaLot().getNumLot());
							lal.setPrixU(ligne.getPrixULDocument());
							lal.setQte(ligne.getQteLDocument());
							lal.setQteRecue(ligne.getQteGenere());
							lal.setUnite(ligne.getU1LDocument());
							lal.setUniteRecue(ligne.getUniteGenere());
							lal.setTaLBoncde((TaLBoncde) ligne);
							lal.setTaLPreparation(temp);
							lal.setIdLigneSrc(ligne.getIdLDocument());
							temp.getTaLigneALignes().add(lal);
							((TaLBoncde) ligne).getTaLigneALignes().add(lal);
							temp.addREtatLigneDoc(etatLigne);

							for (TaLigneALigneSupplementDTO o : ligne.getListeLigneAIntegrer()) {
								//enregistrer le lien entre les lignes en recherchant la vraie ligne d'origine
								TaLigneALigne lal2 = new TaLigneALigne();

								TaLBoncde ligneReel=(TaLBoncde) o.getLigne();
								lal2.setNumLot(ligne.getNumlotGenere());
								lal2.setPrixU(ligneReel.getPrixULDocument());
								lal2.setQte(ligneReel.getQteLDocument());
								lal2.setQteRecue(o.getQteRecue());
								lal2.setUnite(ligneReel.getU1LDocument());
								lal2.setUniteRecue(ligne.getUniteGenere());
								lal2.setTaLBoncde((TaLBoncde) ligneReel);
								lal2.setTaLPreparation(temp);
								lal2.setIdLigneSrc(ligneReel.getIdLDocument());
								temp.getTaLigneALignes().add(lal2);
								((TaLBoncde) ligne).getTaLigneALignes().add(lal2);
								temp.addREtatLigneDoc(etatLigne);
							}
							if(temp.getTaArticle()!=null)temp.setTaArticle(taArticleService.findByIdAvecLazy(temp.getTaArticle().getIdArticle(), false));
							((TaPreparation)dd).addLigne(temp);
						}
					}
					}
				}
			}else {

			for (ILigneDocumentTiers ligne : ((TaBoncde)ds).getLignes()) {
				TaLPreparation temp = (TaLPreparation)copieLigneDocument(ligne, new TaLPreparation()); 
				temp.setTaDocument(((TaPreparation)dd));
				((TaPreparation)dd).addLigne(temp);
			}
			}
			TaInfosPreparation infos = (TaInfosPreparation)copieInfosDocument(((TaBoncde)ds).getTaInfosDocument(),new TaInfosPreparation());
			infos.setTaDocument(((TaPreparation)dd));
			((TaPreparation)dd).setTaInfosDocument(infos);

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
		TaBoncde documentInit = ((TaBoncde)ds);
		try {
			
			if(documentInit!=null){
				documentInit.setLegrain(true);
//				dd=copieDocument(ds,dd);
				if(genereCode && !codeDejaGenere )((TaPreparation)dd).setCodeDocument(taPreparationService.genereCode(null));
				((TaPreparation)dd).setCommentaire(commentaire);
				taPreparationService.calculDateEcheanceAbstract(((TaPreparation)dd),taCPaiement.getReportCPaiement(),taCPaiement.getFinMoisCPaiement());
				if(ds.getRelationDocument()){
				TaRDocument taRDocument = new TaRDocument();
				taRDocument.setTaPreparation(((TaPreparation)dd));
				taRDocument.setTaBoncde(documentInit);
				((TaPreparation)dd).getTaRDocuments().add(taRDocument);
				}
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
		String jpql = "select x from TaRDocument x where x.taBoncde.idDocument="+idDoc+" and x.taPreparation is not null";
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
