package fr.legrain.generation.service;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import fr.legrain.bdg.documents.service.remote.ITaBoncdeServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocBonCommandeVersBonCommandeServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.IInfosDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.model.TaBoncde;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaBoncde;
import fr.legrain.document.model.TaInfosBoncde;
import fr.legrain.document.model.TaLBoncde;
import fr.legrain.document.model.TaLBonliv;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.lib.data.LibDate;


@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public  class GenereDocBonCommandeVersBonCommande extends AbstractGenereDocVersBonCommande implements IGenereDocBonCommandeVersBonCommandeServiceRemote{
	private @EJB ITaBoncdeServiceRemote taBoncdeService;
	protected TaBoncde castDocumentSource() {
		return (TaBoncde)ds;
	}
	
	protected TaBoncde castDocumentDest() {
		return (TaBoncde)dd;
	}
	
	@Override
	public IDocumentTiers copieDocumentSpecifique(IDocumentTiers ds, IDocumentTiers dd
			,Boolean ligneSeparatrice,String libelleSeparateur) {
		try {
			((TaBoncde)dd).setTaTPaiement(((TaBoncde)ds).getTaTPaiement());
			((TaBoncde)dd).setDateEchDocument(((TaBoncde)ds).getDateEchDocument());
//			((TaBoncde)dd).setRegleDocument(BigDecimal.valueOf(0));
			((TaBoncde)dd).setRemHtDocument(((TaBoncde)ds).getRemHtDocument());
			((TaBoncde)dd).setTxRemHtDocument(((TaBoncde)ds).getTxRemHtDocument());
			((TaBoncde)dd).setRemTtcDocument(((TaBoncde)ds).getRemTtcDocument());
			((TaBoncde)dd).setTxRemTtcDocument(((TaBoncde)ds).getTxRemTtcDocument());
			((TaBoncde)dd).setNbEDocument(((TaBoncde)ds).getNbEDocument());
			((TaBoncde)dd).setTtc(((TaBoncde)ds).getTtc());
			//((TaBoncde)dd).setExport(((TaBoncde)ds).getExport());
			((TaBoncde)dd).setCommentaire(((TaBoncde)ds).getCommentaire());
			
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
			

			for (ILigneDocumentTiers ligne : ((TaBoncde)ds).getLignes()) {
				TaLBoncde temp = (TaLBoncde)copieLigneDocument(ligne, new TaLBoncde()); 
				temp.setTaDocument(((TaBoncde)dd));
				((TaBoncde)dd).addLigne(temp);
			}

			TaInfosBoncde infos = (TaInfosBoncde)copieInfosDocument(((TaBoncde)ds).getTaInfosDocument(),new TaInfosBoncde());
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
		if(ld.getTaArticle()!=null && ((TaLBoncde)ls).getCodeTitreTransport()!=null ) {
			((TaLBoncde)ld).setCodeTitreTransport(((TaLBoncde)ls).getCodeTitreTransport());
			((TaLBoncde)ld).setQteTitreTransport(((TaLBoncde)ls).getQteTitreTransport());			
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
		
		TaBoncde documentInit = ((TaBoncde)ds);
		try {
			if(documentInit!=null){
				documentInit.setLegrain(true);
				if(genereCode && !codeDejaGenere )((TaBoncde)dd).setCodeDocument(taBoncdeService.genereCode(null));
				((TaBoncde)dd).setCommentaire(commentaire);
				taBoncdeService.calculDateEcheanceAbstract(((TaBoncde)dd),taCPaiement.getReportCPaiement(),taCPaiement.getFinMoisCPaiement());
				((TaBoncde)dd).calculeTvaEtTotaux();	
			}
			return dd;
		} catch (Exception e) {
			logger.error("",e);
			return null;
		}
	}
	
	public boolean dejaGenere(IDocumentTiers ds) {
		//on peut generer autant de document que l'on veut si ils sont du meme type
		return false;
	}

	@Override
	public String creationRequeteDejaGenere(IDocumentTiers ds) {
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
	public IDocumentTiers genereDocument(IDocumentTiers document, IDocumentTiers docGenere, String codeTiers, boolean b,
			boolean generationModele,boolean genereCode,boolean multiple) {
		// TODO Auto-generated method stub
		return super.genereDocument(document, docGenere, codeTiers, b, generationModele,genereCode, multiple);
	}

	@Override
	public void setRelationDocument(boolean relationDocument) {
		super.setRelationDocument(relationDocument);
		
	}



}
