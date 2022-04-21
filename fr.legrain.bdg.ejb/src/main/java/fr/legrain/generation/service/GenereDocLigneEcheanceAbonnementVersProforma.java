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
import fr.legrain.bdg.documents.service.remote.ITaProformaServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaProformaServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocLigneEcheanceAbonnementVersProformaServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocLigneEcheanceAbonnementVersProformaServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.IInfosDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.model.TaDevis;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaInfosProforma;
import fr.legrain.document.model.TaLDevis;
import fr.legrain.document.model.TaLEcheance;
import fr.legrain.document.model.TaLProforma;
import fr.legrain.document.model.TaLigneALigneEcheance;
import fr.legrain.document.model.TaProforma;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.lib.data.LibDate;

@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public  class GenereDocLigneEcheanceAbonnementVersProforma extends AbstractGenereDocVersProformaLEcheance implements IGenereDocLigneEcheanceAbonnementVersProformaServiceRemote{
	private @EJB ITaProformaServiceRemote taProformaService;
//	protected TaFacture castDocumentSource() {
//		return (TaFacture)ds;
//	}
	
	protected TaProforma castDocumentDest() {
		return (TaProforma)dd;
	}
	
	@Override
	public IDocumentTiers copieDocumentSpecifique(List<TaLEcheance> ds, IDocumentTiers dd
			,Boolean ligneSeparatrice,String libelleSeparateur) {
		try {

			TaEtat etatLigne=taEtatService.documentEncours(null);
			codeDejaGenere=false;
			
			Map<String, String> paramsCode = new LinkedHashMap<String, String>();
			if((codeTiers==null || codeTiers.isEmpty()) 
					&& ((TaProforma)dd).getTaTiers()!=null)
				codeTiers=((TaProforma)dd).getTaTiers().getCodeTiers();
			paramsCode.put(Const.C_NOMFOURNISSEUR, codeTiers);

			paramsCode.put(Const.C_DATEDOCUMENT, LibDate.dateToString(((TaProforma)dd).getDateDocument()));				
//			if(isGenereCode()) {
				((TaProforma)dd).setCodeDocument(taProformaService.genereCode(paramsCode));
			codeDejaGenere=true;
//			}

			
			
			for (TaLEcheance document : ds) {
				ILigneDocumentTiers lf = new TaLProforma();
				((TaLProforma) lf).initialiseLigne(false);
				TaArticle art = taArticleService.findById(document.getTaArticle().getIdArticle());
//				if(document.getTaLot() != null) {
//					TaLot lot = taLotService.findById(document.getTaLot().getIdLot());
//					((TaLProforma) lf).setTaLot(lot);
//				}
				
				((TaLProforma) lf).setTaDocument((TaProforma) ds);
				((TaLProforma) lf).setTaTLigne(taTLigneService.findByCode("H"));
				((TaLProforma) lf).setTaArticle(art);
				((TaLProforma) lf).setLibLDocument(art.getLibellecArticle());
				((TaLProforma) lf).setQteLDocument(document.getQteLDocument());
				((TaLProforma) lf).setCodeTvaLDocument(art.getTaTva().getCodeTva());
				((TaLProforma) lf).setCodeTTvaLDocument("F");
				
				if(art.getTaUnite1() != null) {
					((TaLProforma) lf).setU1LDocument(art.getTaUnite1().getCodeUnite());
				}
				
				((TaLProforma) lf).setCompteLDocument(art.getNumcptArticle());
				
				if(document.getMtTtcLDocument() != null) {
					((TaLProforma) lf).setMtTtcLDocument(document.getMtTtcLDocument());
					BigDecimal montantHt = lf.getMtTtcLDocument().divide(BigDecimal.valueOf(1).add(
							 (art.getTaTva().getTauxTva().divide(new BigDecimal(100),MathContext.DECIMAL128).setScale(3,BigDecimal.ROUND_HALF_UP)
						)),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP);
					((TaLProforma) lf).setMtHtLDocument(montantHt);
					BigDecimal prixULDocument = montantHt.divide(((TaLProforma) lf).getQteLDocument(), 2,BigDecimal.ROUND_HALF_UP);
					((TaLProforma) lf).setPrixULDocument(prixULDocument);
					
				}else if(document.getMtHtLDocument() != null) {
					BigDecimal montantHt = document.getMtHtLDocument();
					((TaLProforma) lf).setMtHtLDocument(montantHt);
					BigDecimal montantTtc = lf.getMtHtLDocument().multiply(BigDecimal.valueOf(1).add(
							 (art.getTaTva().getTauxTva().divide(new BigDecimal(100),MathContext.DECIMAL128).setScale(3,BigDecimal.ROUND_HALF_UP)
						)),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP);;
					((TaLProforma) lf).setMtTtcLDocument(montantTtc);
					BigDecimal prixULDocument = montantHt.divide(((TaLProforma) lf).getQteLDocument(), 2,BigDecimal.ROUND_HALF_UP);
					((TaLProforma) lf).setPrixULDocument(prixULDocument);
				}
				

				
				
				
				

				((TaLProforma) lf).setTauxTvaLDocument(art.getTaTva().getTauxTva());
			
				((TaProforma)ds).addLigne((TaLProforma)lf);

			
				TaLigneALigneEcheance lal = new TaLigneALigneEcheance();
				lal.setTaLEcheance(document);
				lal.setTaLProforma((TaLProforma) lf);
				lf.setAbonnement(true);
//				lalEcheance.add(lal);
				((TaLProforma) lf).getTaLigneALignesEcheance().add(lal);
			}

			TaInfosProforma infos = new TaInfosProforma();
			infos.setTaDocument(((TaProforma)dd));
			infos=(TaInfosProforma)copieInfosDocument(infos);
			((TaProforma)dd).setTaInfosDocument(infos);



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
//		String jpql = "select x from TaRDocument x where x.taFacture.idDocument="+idDoc+" and x.taProforma is not null";
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

			if(genereCode && !codeDejaGenere )((TaProforma)dd).setCodeDocument(taProformaService.genereCode(null));
			((TaProforma)dd).setCommentaire(commentaire);
			taProformaService.calculDateEcheanceAbstract(((TaProforma)dd),taCPaiement.getReportCPaiement(),taCPaiement.getFinMoisCPaiement());

			((TaProforma)dd).calculeTvaEtTotaux();	

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
