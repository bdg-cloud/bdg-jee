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
import fr.legrain.bdg.documents.service.remote.ITaBonlivServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocLigneEcheanceAbonnementVersBonlivServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.IInfosDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.model.TaAbonnement;
import fr.legrain.document.model.TaBonliv;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaInfosBonliv;
import fr.legrain.document.model.TaLBonliv;
import fr.legrain.document.model.TaLEcheance;
import fr.legrain.document.model.TaLProforma;
import fr.legrain.document.model.TaLigneALigneEcheance;
import fr.legrain.document.model.TaProforma;
import fr.legrain.document.model.TaTPaiement;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.lib.data.LibDate;
import fr.legrain.tiers.model.TaTiers;

@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public  class GenereDocLigneEcheanceAbonnementVersBonliv extends AbstractGenereDocVersBonLivraisonLEcheance implements IGenereDocLigneEcheanceAbonnementVersBonlivServiceRemote{
	private @EJB ITaBonlivServiceRemote taBonlivService;
//	protected TaFacture castDocumentSource() {
//		return (TaFacture)ds;
//	}
	
	protected TaBonliv castDocumentDest() {
		return (TaBonliv)dd;
	}
	
	@Override
	public IDocumentTiers copieDocumentSpecifique(List<TaLEcheance> ds, IDocumentTiers dd
			,Boolean ligneSeparatrice,String libelleSeparateur) {
		try {

			TaEtat etatLigne=taEtatService.documentEncours(null);
			codeDejaGenere=false;
			
			TaAbonnement abo = ds.get(0).getTaLAbonnement().getTaDocument();
			TaTiers tiers = abo.getTaTiers();
			TaTPaiement taTPaiement = abo.getTaTPaiement();
			
			Map<String, String> paramsCode = new LinkedHashMap<String, String>();
			if((codeTiers==null || codeTiers.isEmpty()) 
					&& ((TaBonliv)dd).getTaTiers()!=null)
				codeTiers=((TaBonliv)dd).getTaTiers().getCodeTiers();
			paramsCode.put(Const.C_NOMFOURNISSEUR, codeTiers);

			paramsCode.put(Const.C_DATEDOCUMENT, LibDate.dateToString(((TaBonliv)dd).getDateDocument()));				
//			if(isGenereCode()) {
			((TaBonliv)dd).setTaTiers(tiers);
				((TaBonliv)dd).setCodeDocument(taBonlivService.genereCode(paramsCode));
			codeDejaGenere=true;
//			}

			
			
			for (TaLEcheance document : ds) {
				ILigneDocumentTiers lf = new TaLBonliv();
				((TaLBonliv) lf).initialiseLigne(false);
				TaArticle art = taArticleService.findById(document.getTaArticle().getIdArticle());
//				if(document.getTaLot() != null) {
//					TaLot lot = taLotService.findById(document.getTaLot().getIdLot());
//					((TaLBonliv) lf).setTaLot(lot);
//				}
				
				((TaLBonliv) lf).setTaDocument((TaBonliv) ds);
				((TaLBonliv) lf).setTaTLigne(taTLigneService.findByCode("H"));
				((TaLBonliv) lf).setTaArticle(art);
				((TaLBonliv) lf).setLibLDocument(art.getLibellecArticle());
				((TaLBonliv) lf).setQteLDocument(document.getQteLDocument());
				((TaLBonliv) lf).setCodeTvaLDocument(art.getTaTva().getCodeTva());
				((TaLBonliv) lf).setCodeTTvaLDocument("F");
				
				if(art.getTaUnite1() != null) {
					((TaLBonliv) lf).setU1LDocument(art.getTaUnite1().getCodeUnite());
				}
				
				((TaLBonliv) lf).setCompteLDocument(art.getNumcptArticle());
				
				if(document.getMtTtcLDocument() != null) {
					((TaLBonliv) lf).setMtTtcLDocument(document.getMtTtcLDocument());
					BigDecimal montantHt = lf.getMtTtcLDocument().divide(BigDecimal.valueOf(1).add(
							 (art.getTaTva().getTauxTva().divide(new BigDecimal(100),MathContext.DECIMAL128).setScale(3,BigDecimal.ROUND_HALF_UP)
						)),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP);
					((TaLBonliv) lf).setMtHtLDocument(montantHt);
					BigDecimal prixULDocument = montantHt.divide(((TaLBonliv) lf).getQteLDocument(), 2,BigDecimal.ROUND_HALF_UP);
					((TaLBonliv) lf).setPrixULDocument(prixULDocument);
					
				}else if(document.getMtHtLDocument() != null) {
					BigDecimal montantHt = document.getMtHtLDocument();
					((TaLBonliv) lf).setMtHtLDocument(montantHt);
					BigDecimal montantTtc = lf.getMtHtLDocument().multiply(BigDecimal.valueOf(1).add(
							 (art.getTaTva().getTauxTva().divide(new BigDecimal(100),MathContext.DECIMAL128).setScale(3,BigDecimal.ROUND_HALF_UP)
						)),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP);;
					((TaLBonliv) lf).setMtTtcLDocument(montantTtc);
					BigDecimal prixULDocument = montantHt.divide(((TaLBonliv) lf).getQteLDocument(), 2,BigDecimal.ROUND_HALF_UP);
					((TaLBonliv) lf).setPrixULDocument(prixULDocument);
				}
				

				
				
				
				

				((TaLBonliv) lf).setTauxTvaLDocument(art.getTaTva().getTauxTva());
			
				((TaBonliv)ds).addLigne((TaLBonliv)lf);

			
				TaLigneALigneEcheance lal = new TaLigneALigneEcheance();
				lal.setTaLEcheance(document);
				lal.setTaLBonliv((TaLBonliv) lf);
				lf.setAbonnement(true);
//				lalEcheance.add(lal);
				((TaLBonliv) lf).getTaLigneALignesEcheance().add(lal);
			}

			TaInfosBonliv infos = new TaInfosBonliv();
			infos.setTaDocument(((TaBonliv)dd));
			infos=(TaInfosBonliv)copieInfosDocument(infos);
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
		return ld;
	}

	@Override
	public IDocumentTiers genereDocumentSpecifique(IDocumentTiers ds, IDocumentTiers dd,String codeTiers, boolean generationModele,boolean genereCode) {
		return null;
	}

	public String creationRequeteDejaGenere(List<TaLEcheance> ds) {
//		int idDoc = ds.getIdDocument();
//		String jpql = "select x from TaRDocument x where x.taFacture.idDocument="+idDoc+" and x.taBonliv is not null";
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

			if(genereCode && !codeDejaGenere )((TaBonliv)dd).setCodeDocument(taBonlivService.genereCode(null));
			((TaBonliv)dd).setCommentaire(commentaire);
			taBonlivService.calculDateEcheanceAbstract(((TaBonliv)dd),taCPaiement.getReportCPaiement(),taCPaiement.getFinMoisCPaiement());

			((TaBonliv)dd).calculeTvaEtTotaux();	

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
