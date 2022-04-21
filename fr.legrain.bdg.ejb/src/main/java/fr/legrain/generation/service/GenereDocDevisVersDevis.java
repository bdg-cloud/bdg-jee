package fr.legrain.generation.service;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import fr.legrain.bdg.documents.service.remote.ITaDevisServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocDevisVersDevisServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.IInfosDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.model.TaDevis;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaDevis;
import fr.legrain.document.model.TaInfosDevis;
import fr.legrain.document.model.TaLDevis;
import fr.legrain.document.model.TaLFacture;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.lib.data.LibDate;


@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public  class GenereDocDevisVersDevis extends AbstractGenereDocVersDevis implements IGenereDocDevisVersDevisServiceRemote{
	private @EJB ITaDevisServiceRemote taDevisService;
	protected TaDevis castDocumentSource() {
		return (TaDevis)ds;
	}
	
	protected TaDevis castDocumentDest() {
		return (TaDevis)dd;
	}
	
	@Override
	public IDocumentTiers copieDocumentSpecifique(IDocumentTiers ds, IDocumentTiers dd
			,Boolean ligneSeparatrice,String libelleSeparateur) {
		try {
			((TaDevis)dd).setTaTPaiement(((TaDevis)ds).getTaTPaiement());
			((TaDevis)dd).setDateEchDocument(((TaDevis)ds).getDateEchDocument());
//			((TaDevis)dd).setRegleDocument(BigDecimal.valueOf(0));
			((TaDevis)dd).setRemHtDocument(((TaDevis)ds).getRemHtDocument());
			((TaDevis)dd).setTxRemHtDocument(((TaDevis)ds).getTxRemHtDocument());
			((TaDevis)dd).setRemTtcDocument(((TaDevis)ds).getRemTtcDocument());
			((TaDevis)dd).setTxRemTtcDocument(((TaDevis)ds).getTxRemTtcDocument());
			((TaDevis)dd).setNbEDocument(((TaDevis)ds).getNbEDocument());
			((TaDevis)dd).setTtc(((TaDevis)ds).getTtc());
			//((TaDevis)dd).setExport(((TaDevis)ds).getExport());
			((TaDevis)dd).setCommentaire(((TaDevis)ds).getCommentaire());
			
			TaEtat etatLigne=taEtatService.documentEncours(null);
			codeDejaGenere=false;
			
			Map<String, String> paramsCode = new LinkedHashMap<String, String>();
			if((codeTiers==null || codeTiers.isEmpty()) 
					&& ((TaDevis)dd).getTaTiers()!=null)
				codeTiers=((TaDevis)dd).getTaTiers().getCodeTiers();
			paramsCode.put(Const.C_NOMFOURNISSEUR, codeTiers);

			paramsCode.put(Const.C_DATEDOCUMENT, LibDate.dateToString(((TaDevis)dd).getDateDocument()));				
			if(isGenereCode()) {
				((TaDevis)dd).setCodeDocument(taDevisService.genereCode(paramsCode));
				codeDejaGenere=true;
			}
				

			for (ILigneDocumentTiers ligne : ((TaDevis)ds).getLignes()) {
				TaLDevis temp = (TaLDevis)copieLigneDocument(ligne, new TaLDevis()); 
				temp.setTaDocument(((TaDevis)dd));
				((TaDevis)dd).addLigne(temp);
			}

			TaInfosDevis infos = (TaInfosDevis)copieInfosDocument(((TaDevis)ds).getTaInfosDocument(),new TaInfosDevis());
			infos.setTaDocument(((TaDevis)dd));
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
		//si doc source à de la crd on la prend dans doc dest
		if(ld.getTaArticle()!=null && ((TaLDevis)ls).getCodeTitreTransport()!=null ) {
			((TaLDevis)ld).setCodeTitreTransport(((TaLDevis)ls).getCodeTitreTransport());
			((TaLDevis)ld).setQteTitreTransport(((TaLDevis)ls).getQteTitreTransport());			
		}
		else
			//sinon, si l'article attend de la crd, on la met dans doc dest
			if(ld.getTaArticle()!=null && ld.getTaArticle().getTaRTaTitreTransport()!=null) {
				((TaLDevis)ld).setCodeTitreTransport(ls.getTaArticle().getTaRTaTitreTransport().getTaTitreTransport().getCodeTitreTransport());
				if(((TaLDevis)ld).getTaArticle().getTaRTaTitreTransport().getQteTitreTransport()!=null) {
					((TaLDevis)ld).setQteTitreTransport(((TaLDevis)ld).getTaArticle().getTaRTaTitreTransport().getQteTitreTransport().multiply(((TaLDevis)ld).getQteLDocument()));
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
				if(genereCode && !codeDejaGenere )((TaDevis)dd).setCodeDocument(taDevisService.genereCode(null));
				((TaDevis)dd).setCommentaire(commentaire);
				taDevisService.calculDateEcheanceAbstract(((TaDevis)dd),taCPaiement.getReportCPaiement(),taCPaiement.getFinMoisCPaiement());
				((TaDevis)dd).calculeTvaEtTotaux();	
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
		return super.genereDocument(document, docGenere, codeTiers, b, generationModele, genereCode, multiple);
	}

	@Override
	public void setRelationDocument(boolean relationDocument) {
		super.setRelationDocument(relationDocument);
		
	}



}
