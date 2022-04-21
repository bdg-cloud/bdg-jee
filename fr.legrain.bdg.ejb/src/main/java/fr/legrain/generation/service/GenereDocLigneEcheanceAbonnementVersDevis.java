package fr.legrain.generation.service;




import java.math.BigDecimal;
import java.math.MathContext;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import fr.legrain.article.model.TaArticle;
import fr.legrain.bdg.documents.service.remote.ITaDevisServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocLigneEcheanceAbonnementVersDevisServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.IInfosDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.model.TaDevis;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaInfosDevis;
import fr.legrain.document.model.TaLDevis;
import fr.legrain.document.model.TaLEcheance;
import fr.legrain.document.model.TaLigneALigneEcheance;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.lib.data.LibDate;

@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public  class GenereDocLigneEcheanceAbonnementVersDevis extends AbstractGenereDocVersDevisLEcheance implements IGenereDocLigneEcheanceAbonnementVersDevisServiceRemote{
	private @EJB ITaDevisServiceRemote taDevisService;
//	protected TaFacture castDocumentSource() {
//		return (TaFacture)ds;
//	}
	
	protected TaDevis castDocumentDest() {
		return (TaDevis)dd;
	}
	
	@Override
	public IDocumentTiers copieDocumentSpecifique(List<TaLEcheance> ds, IDocumentTiers dd
			,Boolean ligneSeparatrice,String libelleSeparateur) {
		try {

			TaEtat etatLigne=taEtatService.documentEncours(null);
			codeDejaGenere=false;
			
			Map<String, String> paramsCode = new LinkedHashMap<String, String>();
			if((codeTiers==null || codeTiers.isEmpty()) 
					&& ((TaDevis)dd).getTaTiers()!=null)
				codeTiers=((TaDevis)dd).getTaTiers().getCodeTiers();
			paramsCode.put(Const.C_NOMFOURNISSEUR, codeTiers);

			paramsCode.put(Const.C_DATEDOCUMENT, LibDate.dateToString(((TaDevis)dd).getDateDocument()));				
//			if(isGenereCode()) {
				((TaDevis)dd).setCodeDocument(taDevisService.genereCode(paramsCode));
			codeDejaGenere=true;
//			}

			
			
			for (TaLEcheance document : ds) {
				ILigneDocumentTiers lf = new TaLDevis();
				((TaLDevis) lf).initialiseLigne(false);
				TaArticle art = taArticleService.findById(document.getTaArticle().getIdArticle());
//				if(document.getTaLot() != null) {
//					TaLot lot = taLotService.findById(document.getTaLot().getIdLot());
//					((TaLDevis) lf).setTaLot(lot);
//				}
				
				((TaLDevis) lf).setTaDocument((TaDevis) ds);
				((TaLDevis) lf).setTaTLigne(taTLigneService.findByCode("H"));
				((TaLDevis) lf).setTaArticle(art);
				((TaLDevis) lf).setLibLDocument(art.getLibellecArticle());
				((TaLDevis) lf).setQteLDocument(document.getQteLDocument());
				((TaLDevis) lf).setCodeTvaLDocument(art.getTaTva().getCodeTva());
				((TaLDevis) lf).setCodeTTvaLDocument("F");
				
				if(art.getTaUnite1() != null) {
					((TaLDevis) lf).setU1LDocument(art.getTaUnite1().getCodeUnite());
				}
				
				((TaLDevis) lf).setCompteLDocument(art.getNumcptArticle());
				
				if(document.getMtTtcLDocument() != null) {
					((TaLDevis) lf).setMtTtcLDocument(document.getMtTtcLDocument());
					BigDecimal montantHt = lf.getMtTtcLDocument().divide(BigDecimal.valueOf(1).add(
							 (art.getTaTva().getTauxTva().divide(new BigDecimal(100),MathContext.DECIMAL128).setScale(3,BigDecimal.ROUND_HALF_UP)
						)),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP);
					((TaLDevis) lf).setMtHtLDocument(montantHt);
					BigDecimal prixULDocument = montantHt.divide(((TaLDevis) lf).getQteLDocument(), 2,BigDecimal.ROUND_HALF_UP);
					((TaLDevis) lf).setPrixULDocument(prixULDocument);
					
				}else if(document.getMtHtLDocument() != null) {
					BigDecimal montantHt = document.getMtHtLDocument();
					((TaLDevis) lf).setMtHtLDocument(montantHt);
					BigDecimal montantTtc = lf.getMtHtLDocument().multiply(BigDecimal.valueOf(1).add(
							 (art.getTaTva().getTauxTva().divide(new BigDecimal(100),MathContext.DECIMAL128).setScale(3,BigDecimal.ROUND_HALF_UP)
						)),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP);;
					((TaLDevis) lf).setMtTtcLDocument(montantTtc);
					BigDecimal prixULDocument = montantHt.divide(((TaLDevis) lf).getQteLDocument(), 2,BigDecimal.ROUND_HALF_UP);
					((TaLDevis) lf).setPrixULDocument(prixULDocument);
				}
				

				
				
				
				

				((TaLDevis) lf).setTauxTvaLDocument(art.getTaTva().getTauxTva());
			
				((TaDevis)ds).addLigne((TaLDevis)lf);

			
				TaLigneALigneEcheance lal = new TaLigneALigneEcheance();
				lal.setTaLEcheance(document);
				lal.setTaLDevis((TaLDevis) lf);
				lf.setAbonnement(true);
//				lalEcheance.add(lal);
				((TaLDevis) lf).getTaLigneALignesEcheance().add(lal);
			}

			TaInfosDevis infos = new TaInfosDevis();
			infos.setTaDocument(((TaDevis)dd));
			infos=(TaInfosDevis)copieInfosDocument(infos);
			((TaDevis)dd).setTaInfosDocument(infos);



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
		return null;
	}

	public String creationRequeteDejaGenere(List<TaLEcheance> ds) {
//		int idDoc = ds.getIdDocument();
//		String jpql = "select x from TaRDocument x where x.taFacture.idDocument="+idDoc+" and x.taDevis is not null";
//		return jpql;
		return null;
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
	public IDocumentTiers genereDocument(List<TaLEcheance> ds, IDocumentTiers dd, String codeTiers , boolean enregistre , boolean generationModele,boolean genereCode,boolean multiple) {
		// TODO Auto-generated method stub
		return super.genereDocument(ds, dd, codeTiers, enregistre, generationModele, genereCode, multiple);
	}

	@Override
	public void setRelationDocument(boolean relationDocument) {
		super.setRelationDocument(relationDocument);
		
	}

	@Override
	public IDocumentTiers genereDocument(IDocumentTiers document, IDocumentTiers docGenere, String codeTiers, boolean b,
			boolean generationModele, boolean genereCode, boolean multiple) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ILigneDocumentTiers copieLigneDocumentSpecifique(TaLEcheance ls, ILigneDocumentTiers ld) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IInfosDocumentTiers copieInfosDocumentSpecifique(IInfosDocumentTiers id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDocumentTiers genereDocumentSpecifique(List<TaLEcheance> ds, IDocumentTiers dd, String codeTiers,
			boolean generationModele, boolean genereCode) {
		try {			

			if(genereCode && !codeDejaGenere )((TaDevis)dd).setCodeDocument(taDevisService.genereCode(null));
			((TaDevis)dd).setCommentaire(commentaire);
			taDevisService.calculDateEcheanceAbstract(((TaDevis)dd),taCPaiement.getReportCPaiement(),taCPaiement.getFinMoisCPaiement());

			((TaDevis)dd).calculeTvaEtTotaux();	

		return dd;
	} catch (Exception e) {
		logger.error("",e);
		return null;
	}
}
	@Override
	public IDocumentTiers copieDocumentSpecifique(IDocumentTiers ds, IDocumentTiers dd, Boolean ligneSeparatrice,
			String libelleSeparateur) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String creationRequeteDejaGenere(IDocumentTiers ds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDocumentTiers genereDocument(IDocumentTiers docGenere, String codeTiers, boolean b,
			boolean generationModele, boolean genereCode, boolean multiple) {
		// TODO Auto-generated method stub
		return null;
	}







}
