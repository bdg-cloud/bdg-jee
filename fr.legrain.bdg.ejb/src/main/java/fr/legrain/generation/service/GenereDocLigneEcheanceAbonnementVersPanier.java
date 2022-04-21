package fr.legrain.generation.service;




import java.math.BigDecimal;
import java.math.MathContext;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import fr.legrain.article.model.TaArticle;
import fr.legrain.bdg.documents.service.remote.ITaPanierServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocLigneEcheanceAbonnementVersPanierServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.IInfosDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.model.SWTDocument;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaInfosPanier;
import fr.legrain.document.model.TaLAbonnement;
import fr.legrain.document.model.TaLEcheance;
import fr.legrain.document.model.TaLPanier;
import fr.legrain.document.model.TaLigneALigneEcheance;
import fr.legrain.document.model.TaPanier;
import fr.legrain.document.model.TaTLigne;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.lib.data.LibDate;

@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public  class GenereDocLigneEcheanceAbonnementVersPanier extends AbstractGenereDocVersPanierLEcheance implements IGenereDocLigneEcheanceAbonnementVersPanierServiceRemote{
	private @EJB ITaPanierServiceRemote taPanierService;
//	protected TaFacture castDocumentSource() {
//		return (TaFacture)ds;
//	}
	
	protected TaPanier castDocumentDest() {
		return (TaPanier)dd;
	}
	
	@Override
	public IDocumentTiers copieDocumentSpecifique(List<TaLEcheance> ds, IDocumentTiers dd
			,Boolean ligneSeparatrice,String libelleSeparateur) {
		try {

			TaEtat etatLigne=taEtatService.documentEncours(null);
			codeDejaGenere=false;
			
			Map<String, String> paramsCode = new LinkedHashMap<String, String>();
			if((codeTiers==null || codeTiers.isEmpty()) 
					&& ((TaPanier)dd).getTaTiers()!=null)
				codeTiers=((TaPanier)dd).getTaTiers().getCodeTiers();
			paramsCode.put(Const.C_NOMFOURNISSEUR, codeTiers);

			paramsCode.put(Const.C_DATEDOCUMENT, LibDate.dateToString(((TaPanier)dd).getDateDocument()));				
//			if(isGenereCode()) {
			
			if(((TaPanier) dd).getCodeDocument() == null) {
				((TaPanier)dd).setCodeDocument(taPanierService.genereCode(paramsCode));
			}
				
			codeDejaGenere=true;
//			}

			
			TaTLigne taTLigne = taTLigneService.findByCode(SWTDocument.C_CODE_T_LIGNE_H);
			for (TaLEcheance document : ds) {
				TaLPanier lf = new TaLPanier();
				String codeLiaisonLigne = UUID.randomUUID().toString();
				lf.setCodeLiaisonLigne(codeLiaisonLigne);
				lf.setTaArticle(taArticleService.findById(document.getTaArticle().getIdArticle()));
				lf.setTaTLigne(taTLigne);
				//lf.setTaLEcheance(document);
				lf.setQteLDocument(document.getQteLDocument());
				
				if(document.getCoefMultiplicateur() != null) {
					lf.setQteLDocument(lf.getQteLDocument().multiply(document.getCoefMultiplicateur()));
				}
				
				lf.setLibLDocument(document.getLibLDocument());
				lf.setPrixULDocument(document.getPrixULDocument());
				lf.setMtHtLDocument(document.getMtHtLDocument());
				lf.setMtTtcLDocument(document.getMtTtcLDocument());
				//rajout yann
//				lf.setQte2LDocument();
//				lf.setU2LDocument();
				lf.setCompteLDocument(lf.getTaArticle().getNumcptArticle());
				lf.setU1LDocument(document.getU1LDocument());
				//lf.setCodeTTvaLDocument();
				lf.setRemHtLDocument(document.getRemHtLDocument());
				lf.setRemTxLDocument(document.getRemTxLDocument());
				//lf.setLibTvaLDocument();
				
				//lf.setTauxTvaLDocument(document.getTauxTvaLDocument());
				lf.setCodeTvaLDocument(lf.getTaArticle().getTaTva().getCodeTva());
				lf.setTauxTvaLDocument(lf.getTaArticle().getTaTva().getTauxTva());
				((TaPanier) dd).addLigne((TaLPanier)lf);
				((TaLPanier)lf).setTaDocument((TaPanier)dd);
				

				TaLigneALigneEcheance lal = new TaLigneALigneEcheance();
				lal.setTaLEcheance(document);
				lal.setTaLPanier((TaLPanier) lf);
				lf.setAbonnement(true);
//				lalEcheance.add(lal);
				((TaLPanier) lf).getTaLigneALignesEcheance().add(lal);
				
				
				//ajout d'une lfiaison entre lfa lfigne d'échéance et lfa lfigne d'avis
				//document.setTaLAvisEcheance(lf);
				
				//lfigne de commentaire sur lfa période
				lf = new TaLPanier();
				lf.setTaTLigne(taTLigne);
				lf.setCodeLiaisonLigne(codeLiaisonLigne);
				//lf.setTaLEcheance(document);
				lf.setLibLDocument("Période du "+LibDate.dateToString(document.getDebutPeriode())+" au "+LibDate.dateToString(document.getFinPeriode()));
				((TaPanier) dd).addLigne((TaLPanier)lf);
				((TaLPanier)lf).setTaDocument((TaPanier)dd);
				TaLAbonnement ligneAbo = taLEcheanceService.fetchLigneAbo(document.getIdLEcheance());
				if(ligneAbo!=null) {
					if(ligneAbo.getComplement1()!=null && !ligneAbo.getComplement1().isEmpty()) {
						lf = new TaLPanier();
						lf.setCodeLiaisonLigne(codeLiaisonLigne);
						lf.setTaTLigne(taTLigne);
						//lf.setTaLEcheance(document);
						lf.setLibLDocument(ligneAbo.getComplement1());
						((TaPanier) dd).addLigne((TaLPanier)lf);
						((TaLPanier)lf).setTaDocument((TaPanier)dd);
					}
					if(ligneAbo.getComplement2()!=null && !ligneAbo.getComplement2().isEmpty()) {
						lf = new TaLPanier();
						lf.setCodeLiaisonLigne(codeLiaisonLigne);
						lf.setTaTLigne(taTLigne);
						//lf.setTaLEcheance(document);
						lf.setLibLDocument(ligneAbo.getComplement2());
						((TaPanier) dd).addLigne((TaLPanier)lf);
						((TaLPanier)lf).setTaDocument((TaPanier)dd);
					}
					if(ligneAbo.getComplement3()!=null && !ligneAbo.getComplement3().isEmpty()) {
						lf = new TaLPanier();
						lf.setCodeLiaisonLigne(codeLiaisonLigne);
						lf.setTaTLigne(taTLigne);
						//lf.setTaLEcheance(document);
						lf.setLibLDocument(ligneAbo.getComplement3());
						((TaPanier) dd).addLigne((TaLPanier)lf);
						((TaLPanier)lf).setTaDocument((TaPanier)dd);
					}

				}
			

			}

			if(((TaPanier) dd).getTaInfosDocument() == null || ((TaPanier) dd).getTaInfosDocument().getIdInfosDocument() == 0) {
				TaInfosPanier infos = new TaInfosPanier();
				infos.setTaDocument(((TaPanier)dd));
				infos=(TaInfosPanier)copieInfosDocument(infos);
				((TaPanier)dd).setTaInfosDocument(infos);
			}
			
			



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
//		String jpql = "select x from TaRDocument x where x.taFacture.idDocument="+idDoc+" and x.taPanier is not null";
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

			if(genereCode && !codeDejaGenere )((TaPanier)dd).setCodeDocument(taPanierService.genereCode(null));
			((TaPanier)dd).setCommentaire(commentaire);
			taPanierService.calculDateEcheanceAbstract(((TaPanier)dd),taCPaiement.getReportCPaiement(),taCPaiement.getFinMoisCPaiement());

			((TaPanier)dd).calculeTvaEtTotaux();	

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
