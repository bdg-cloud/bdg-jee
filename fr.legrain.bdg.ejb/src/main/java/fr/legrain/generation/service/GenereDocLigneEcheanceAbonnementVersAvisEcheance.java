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
import fr.legrain.bdg.documents.service.remote.ITaAvisEcheanceServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocLigneEcheanceAbonnementVersAvisEcheanceServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.IInfosDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.model.SWTDocument;
import fr.legrain.document.model.TaAvisEcheance;
import fr.legrain.document.model.TaDevis;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaInfosAvisEcheance;
import fr.legrain.document.model.TaLAbonnement;
import fr.legrain.document.model.TaLAvisEcheance;
import fr.legrain.document.model.TaLEcheance;
import fr.legrain.document.model.TaLFacture;
import fr.legrain.document.model.TaLigneALigneEcheance;
import fr.legrain.document.model.TaRDocument;
import fr.legrain.document.model.TaTLigne;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.lib.data.LibDate;

@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public  class GenereDocLigneEcheanceAbonnementVersAvisEcheance extends AbstractGenereDocVersAvisEcheanceLEcheance implements IGenereDocLigneEcheanceAbonnementVersAvisEcheanceServiceRemote{
	private @EJB ITaAvisEcheanceServiceRemote taAvisEcheanceService;
//	protected TaFacture castDocumentSource() {
//		return (TaFacture)ds;
//	}
	
	protected TaAvisEcheance castDocumentDest() {
		return (TaAvisEcheance)dd;
	}
	
	@Override
	public IDocumentTiers copieDocumentSpecifique(List<TaLEcheance> ds, IDocumentTiers dd
			,Boolean ligneSeparatrice,String libelleSeparateur) {
		try {

			TaEtat etatLigne=taEtatService.documentEncours(null);
			codeDejaGenere=false;
			
			Map<String, String> paramsCode = new LinkedHashMap<String, String>();
			if((codeTiers==null || codeTiers.isEmpty()) 
					&& ((TaAvisEcheance)dd).getTaTiers()!=null)
				codeTiers=((TaAvisEcheance)dd).getTaTiers().getCodeTiers();
			paramsCode.put(Const.C_NOMFOURNISSEUR, codeTiers);

			paramsCode.put(Const.C_DATEDOCUMENT, LibDate.dateToString(((TaAvisEcheance)dd).getDateDocument()));				
//			if(isGenereCode()) {
				((TaAvisEcheance)dd).setCodeDocument(taAvisEcheanceService.genereCode(paramsCode));
			codeDejaGenere=true;
//			}

			
			TaLAvisEcheance l = null;
			TaTLigne taTLigne = taTLigneService.findByCode(SWTDocument.C_CODE_T_LIGNE_H);
			for (TaLEcheance ech : ds) {
				l = new TaLAvisEcheance();
				l.setTaArticle(taArticleService.findById(ech.getTaArticle().getIdArticle()));
				l.setTaTLigne(taTLigne);
				//l.setTaLEcheance(ech);
				l.setQteLDocument(ech.getQteLDocument());
				
				if(ech.getCoefMultiplicateur() != null) {
					l.setQteLDocument(l.getQteLDocument().multiply(ech.getCoefMultiplicateur()));
				}
				
				l.setLibLDocument(ech.getLibLDocument());
				l.setPrixULDocument(ech.getPrixULDocument());
				l.setMtHtLDocument(ech.getMtHtLDocument());
				l.setMtTtcLDocument(ech.getMtTtcLDocument());
				//rajout yann
//				l.setQte2LDocument();
//				l.setU2LDocument();
				l.setCompteLDocument(l.getTaArticle().getNumcptArticle());
				l.setU1LDocument(ech.getU1LDocument());
				//l.setCodeTTvaLDocument();
				l.setRemHtLDocument(ech.getRemHtLDocument());
				l.setRemTxLDocument(ech.getRemTxLDocument());
				//l.setLibTvaLDocument();
				
				//l.setTauxTvaLDocument(ech.getTauxTvaLDocument());
				l.setCodeTvaLDocument(l.getTaArticle().getTaTva().getCodeTva());
				l.setTauxTvaLDocument(l.getTaArticle().getTaTva().getTauxTva());
				((TaAvisEcheance) dd).addLigne((TaLAvisEcheance)l);
				((TaLAvisEcheance)l).setTaDocument((TaAvisEcheance)dd);
				
				
				TaLigneALigneEcheance lal = new TaLigneALigneEcheance();
				lal.setTaLEcheance(ech);
				lal.setTaLAvisEcheance((TaLAvisEcheance) l);
				l.setAbonnement(true);
				((TaLAvisEcheance) l).getTaLigneALignesEcheance().add(lal);
				
				//ajout d'une liaison entre la ligne d'échéance et la ligne d'avis
				//ech.setTaLAvisEcheance(l);
				
				//ligne de commentaire sur la période
				l = new TaLAvisEcheance();
				l.setTaTLigne(taTLigne);
				//l.setTaLEcheance(ech);
				l.setLibLDocument("Période du "+LibDate.dateToString(ech.getDebutPeriode())+" au "+LibDate.dateToString(ech.getFinPeriode()));
				((TaAvisEcheance) dd).addLigne((TaLAvisEcheance)l);
				((TaLAvisEcheance)l).setTaDocument((TaAvisEcheance)dd);
				
				//ligne commentaire pour les compléments 
				TaLAbonnement ligneAbo = taLEcheanceService.fetchLigneAbo(ech.getIdLEcheance());
				if(ligneAbo!=null) {
					if(ligneAbo.getComplement1()!=null && !ligneAbo.getComplement1().isEmpty()) {
						l = new TaLAvisEcheance();
						l.setTaTLigne(taTLigne);
						//l.setTaLEcheance(document);
						l.setLibLDocument(ligneAbo.getComplement1());
						((TaAvisEcheance) dd).addLigne((TaLAvisEcheance)l);
						((TaLAvisEcheance)l).setTaDocument((TaAvisEcheance)dd);
					}
					if(ligneAbo.getComplement2()!=null && !ligneAbo.getComplement2().isEmpty()) {
						l = new TaLAvisEcheance();
						l.setTaTLigne(taTLigne);
						//l.setTaLEcheance(document);
						l.setLibLDocument(ligneAbo.getComplement2());
						((TaAvisEcheance) dd).addLigne((TaLAvisEcheance)l);
						((TaLAvisEcheance)l).setTaDocument((TaAvisEcheance)dd);
					}
					if(ligneAbo.getComplement3()!=null && !ligneAbo.getComplement3().isEmpty()) {
						l = new TaLAvisEcheance();
						l.setTaTLigne(taTLigne);
						//l.setTaLEcheance(document);
						l.setLibLDocument(ligneAbo.getComplement3());
						((TaAvisEcheance) dd).addLigne((TaLAvisEcheance)l);
						((TaLAvisEcheance)l).setTaDocument((TaAvisEcheance)dd);
					}

				}
				

			}

			


			TaInfosAvisEcheance infos = new TaInfosAvisEcheance();
			infos.setTaDocument(((TaAvisEcheance)dd));
			infos=(TaInfosAvisEcheance)copieInfosDocument(infos);
			((TaAvisEcheance)dd).setTaInfosDocument(infos);



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
//		String jpql = "select x from TaRDocument x where x.taFacture.idDocument="+idDoc+" and x.taAvisEcheance is not null";
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

			if(genereCode && !codeDejaGenere )((TaAvisEcheance)dd).setCodeDocument(taAvisEcheanceService.genereCode(null));
			((TaAvisEcheance)dd).setCommentaire(commentaire);
			taAvisEcheanceService.calculDateEcheanceAbstract(((TaAvisEcheance)dd),taCPaiement.getReportCPaiement(),taCPaiement.getFinMoisCPaiement());

			((TaAvisEcheance)dd).calculeTvaEtTotaux();	

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
