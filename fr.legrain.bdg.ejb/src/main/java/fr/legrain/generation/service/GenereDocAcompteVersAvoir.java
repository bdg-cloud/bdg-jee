package fr.legrain.generation.service;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import fr.legrain.bdg.documents.service.remote.ITaAvoirServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocAcompteVersAvoirServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.IInfosDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.model.TaAcompte;
import fr.legrain.document.model.TaAvoir;
import fr.legrain.document.model.TaBonliv;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaAvoir;
import fr.legrain.document.model.TaInfosAvoir;
import fr.legrain.document.model.TaLAcompte;
import fr.legrain.document.model.TaLAvoir;
import fr.legrain.document.model.TaLFacture;
import fr.legrain.document.model.TaRDocument;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.lib.data.LibDate;

@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public  class GenereDocAcompteVersAvoir extends AbstractGenereDocVersAvoir implements IGenereDocAcompteVersAvoirServiceRemote{
	private @EJB ITaAvoirServiceRemote taAvoirService;
	protected TaAcompte castDocumentSource() {
		return (TaAcompte)ds;
	}
	
	protected TaAvoir castDocumentDest() {
		return (TaAvoir)dd;
	}
	
	@Override
	public IDocumentTiers copieDocumentSpecifique(IDocumentTiers ds, IDocumentTiers dd
			,Boolean ligneSeparatrice,String libelleSeparateur) {
		try {
//			((TaAvoir)dd).setTaTPaiement(((TaAcompte)ds).getTaTPaiement());
			((TaAvoir)dd).setDateEchDocument(((TaAcompte)ds).getDateEchDocument());
			((TaAvoir)dd).setRegleDocument(BigDecimal.valueOf(0));
			((TaAvoir)dd).setRemHtDocument(((TaAcompte)ds).getRemHtDocument());
			if(multiple)((TaAvoir)dd).setTxRemHtDocument(BigDecimal.valueOf(0));else((TaAvoir)dd).setTxRemHtDocument(((TaAcompte)ds).getTxRemHtDocument());
			((TaAvoir)dd).setRemTtcDocument(((TaAcompte)ds).getRemTtcDocument());
			((TaAvoir)dd).setTxRemTtcDocument(((TaAcompte)ds).getTxRemTtcDocument());
			((TaAvoir)dd).setNbEDocument(((TaAcompte)ds).getNbEDocument());
			((TaAvoir)dd).setTtc(((TaAcompte)ds).getTtc());
			((TaAvoir)dd).setDateExport(((TaAcompte)ds).getDateExport());
			((TaAvoir)dd).setCommentaire(((TaAcompte)ds).getCommentaire());
			
			TaEtat etatLigne=taEtatService.documentEncours(null);
			codeDejaGenere=false;
			
			Map<String, String> paramsCode = new LinkedHashMap<String, String>();
			if((codeTiers==null || codeTiers.isEmpty()) 
					&& ((TaAvoir)dd).getTaTiers()!=null)
				codeTiers=((TaAvoir)dd).getTaTiers().getCodeTiers();
			paramsCode.put(Const.C_NOMFOURNISSEUR, codeTiers);

			paramsCode.put(Const.C_DATEDOCUMENT, LibDate.dateToString(((TaAvoir)dd).getDateDocument()));				
			if(isGenereCode()) {
				((TaAvoir)dd).setCodeDocument(taAvoirService.genereCode(paramsCode));
				codeDejaGenere=true;
			}
						

			for (ILigneDocumentTiers ligne : ((TaAcompte)ds).getLignes()) {
				TaLAvoir temp = (TaLAvoir)copieLigneDocument(ligne, new TaLAvoir()); 
				temp.setTaDocument(((TaAvoir)dd));
				((TaAvoir)dd).addLigne(temp);
			}

			TaInfosAvoir infos = (TaInfosAvoir)copieInfosDocument(((TaAcompte)ds).getTaInfosDocument(),new TaInfosAvoir());
			infos.setTaDocument(((TaAvoir)dd));
			((TaAvoir)dd).setTaInfosDocument(infos);

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
		if(documentGereLesCrd) {			
			//si doc source à de la crd on la prend dans doc dest
			if(ld.getTaArticle()!=null && ((TaLAcompte)ls).getCodeTitreTransport()!=null ) {
				((TaLAvoir)ld).setCodeTitreTransport(((TaLAcompte)ls).getCodeTitreTransport());
				((TaLAvoir)ld).setQteTitreTransport(((TaLAcompte)ls).getQteTitreTransport());			
			}
			else
				//sinon, si l'article attend de la crd, on la met dans doc dest
				if(ld.getTaArticle()!=null && ld.getTaArticle().getTaRTaTitreTransport()!=null) {
					((TaLAvoir)ld).setCodeTitreTransport(ls.getTaArticle().getTaRTaTitreTransport().getTaTitreTransport().getCodeTitreTransport());
					if(((TaLAvoir)ld).getTaArticle().getTaRTaTitreTransport().getQteTitreTransport()!=null) {
						((TaLAvoir)ld).setQteTitreTransport(((TaLAvoir)ld).getTaArticle().getTaRTaTitreTransport().getQteTitreTransport().multiply(((TaLAvoir)ld).getQteLDocument()));
					}
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
				if(genereCode && !codeDejaGenere )((TaAvoir)dd).setCodeDocument(taAvoirService.genereCode(null));
				((TaAvoir)dd).setCommentaire(commentaire);
				taAvoirService.calculDateEcheanceAbstract(((TaAvoir)dd),taCPaiement.getReportCPaiement(),taCPaiement.getFinMoisCPaiement());
				if(ds.getRelationDocument()){
				TaRDocument taRDocument = new TaRDocument();
				taRDocument.setTaAvoir(((TaAvoir)dd));
				taRDocument.setTaAcompte(documentInit);
				taRDocument.setIdSrc(documentInit.getIdDocument());
				((TaAvoir)dd).getTaRDocuments().add(taRDocument);
				}
				((TaAvoir)dd).calculeTvaEtTotaux();	
			}
			return dd;
		} catch (Exception e) {
			logger.error("",e);
			return null;
		}
	}
	
	public String creationRequeteDejaGenere(IDocumentTiers ds) {
		int idDoc = ds.getIdDocument();
		String jpql = "select x from TaRDocument x where x.taAcompte.idDocument="+idDoc+" and x.taAvoir is not null";
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
		return super.genereDocument(document, docGenere, codeTiers, b, generationModele,genereCode,multiple);
	}

	@Override
	public void setRelationDocument(boolean relationDocument) {
		super.setRelationDocument(relationDocument);
		
	}



}
