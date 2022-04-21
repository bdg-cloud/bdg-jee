package fr.legrain.generation.service;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import fr.legrain.bdg.documents.service.remote.ITaBonlivServiceRemote;
import fr.legrain.bdg.general.service.remote.IGenereDocAcompteVersBonLivraisonServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.IInfosDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.model.TaAcompte;
import fr.legrain.document.model.TaBonliv;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaBonliv;
import fr.legrain.document.model.TaInfosBonliv;
import fr.legrain.document.model.TaLAcompte;
import fr.legrain.document.model.TaLBonliv;
import fr.legrain.document.model.TaRDocument;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.lib.data.LibDate;

@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public  class GenereDocAcompteVersBonLivraison extends AbstractGenereDocVersBonLivraison implements IGenereDocAcompteVersBonLivraisonServiceRemote{
	private @EJB ITaBonlivServiceRemote taBonlivService;
	protected TaAcompte castDocumentSource() {
		return (TaAcompte)ds;
	}
	
	protected TaBonliv castDocumentDest() {
		return (TaBonliv)dd;
	}
	
	@Override
	public IDocumentTiers copieDocumentSpecifique(IDocumentTiers ds, IDocumentTiers dd
			,Boolean ligneSeparatrice,String libelleSeparateur) {
		try {
//			((TaBonliv)dd).setTaTPaiement(((TaAcompte)ds).getTaTPaiement());
//			((TaBonliv)dd).setDateEchDocument(((TaAcompte)ds).getDateEchDocument());
//			((TaBonliv)dd).setRegleDocument(BigDecimal.valueOf(0));
			((TaBonliv)dd).setRemHtDocument(((TaAcompte)ds).getRemHtDocument());
			if(multiple)((TaBonliv)dd).setTxRemHtDocument(BigDecimal.valueOf(0));else((TaBonliv)dd).setTxRemHtDocument(((TaAcompte)ds).getTxRemHtDocument());
			((TaBonliv)dd).setRemTtcDocument(((TaAcompte)ds).getRemTtcDocument());
			((TaBonliv)dd).setTxRemTtcDocument(((TaAcompte)ds).getTxRemTtcDocument());
			((TaBonliv)dd).setNbEDocument(((TaAcompte)ds).getNbEDocument());
			((TaBonliv)dd).setTtc(((TaAcompte)ds).getTtc());
//			((TaBonliv)dd).setExport(((TaAcompte)ds).getExport());
			((TaBonliv)dd).setCommentaire(((TaAcompte)ds).getCommentaire());
			
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
			

			for (ILigneDocumentTiers ligne : ((TaAcompte)ds).getLignes()) {
				TaLBonliv temp = (TaLBonliv)copieLigneDocument(ligne, new TaLBonliv()); 
				temp.setTaDocument(((TaBonliv)dd));
				((TaBonliv)dd).addLigne(temp);
			}

			TaInfosBonliv infos = (TaInfosBonliv)copieInfosDocument(((TaAcompte)ds).getTaInfosDocument(),new TaInfosBonliv());
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
		if(ld.getTaArticle()!=null && ((TaLAcompte)ls).getCodeTitreTransport()!=null ) {
			((TaLBonliv)ld).setCodeTitreTransport(((TaLAcompte)ls).getCodeTitreTransport());
			((TaLBonliv)ld).setQteTitreTransport(((TaLAcompte)ls).getQteTitreTransport());			
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
		TaAcompte documentInit = ((TaAcompte)ds);
		try {
			
			if(documentInit!=null){
				documentInit.setLegrain(true);
//				dd=copieDocument(ds,dd);
				if(genereCode && !codeDejaGenere )((TaBonliv)dd).setCodeDocument(taBonlivService.genereCode(null));
				((TaBonliv)dd).setCommentaire(commentaire);
				taBonlivService.calculDateEcheanceAbstract(((TaBonliv)dd),taCPaiement.getReportCPaiement(),taCPaiement.getFinMoisCPaiement());
				if(ds.getRelationDocument()){
				TaRDocument taRDocument = new TaRDocument();
				taRDocument.setTaBonliv(((TaBonliv)dd));
				taRDocument.setTaAcompte(documentInit);
				taRDocument.setIdSrc(documentInit.getIdDocument());
				((TaBonliv)dd).getTaRDocuments().add(taRDocument);
				}
				((TaBonliv)dd).calculeTvaEtTotaux();
				
//				if(!generationModele){
//				if(ds.getTaRReglementLiaisons()!=null && ds.getTaRReglementLiaisons().size()>0) {
//					for (TaRReglementLiaison rr : ds.getTaRReglementLiaisons()) {
//						TaRReglementLiaison taRReglement = new TaRReglementLiaison();  
//						
//						taRReglement.setTaReglement(rr.getTaReglement());
//						taRReglement.setAffectation(rr.getAffectation());
//						taRReglement.setTaBonliv((TaBonliv)dd);
//						taRReglement.setEtat(IHMEtat.integre);
//						((TaBonliv)dd).addRReglementLiaison(taRReglement);
//					}
//				}
//			}
//			((TaBonliv)dd).calculRegleDocument();
//			((TaBonliv)dd).calculRegleDocumentComplet();
//			((TaBonliv)dd).setNetAPayer(((TaBonliv)dd).calculResteAReglerComplet());
				
			}
			return dd;
		} catch (Exception e) {
			logger.error("",e);
			return null;
		}
	}
	
	public String creationRequeteDejaGenere(IDocumentTiers ds) {
		int idDoc = ds.getIdDocument();
		String jpql = "select x from TaRDocument x where x.taAcompte.idDocument="+idDoc+" and x.taBonliv is not null";
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
		return super.genereDocument(document, docGenere, codeTiers, b, generationModele, genereCode,multiple);
	}

	@Override
	public void setRelationDocument(boolean relationDocument) {
		super.setRelationDocument(relationDocument);
		
	}



}
