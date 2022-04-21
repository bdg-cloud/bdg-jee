package fr.legrain.generation.service;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import fr.legrain.bdg.documents.service.remote.ITaAvoirServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocAvoirVersAvoirServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.IInfosDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.model.TaAvoir;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaAvoir;
import fr.legrain.document.model.TaInfosAvoir;
import fr.legrain.document.model.TaLAvoir;
import fr.legrain.document.model.TaLFacture;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.lib.data.LibDate;

@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public  class GenereDocAvoirVersAvoir extends AbstractGenereDocVersAvoir implements IGenereDocAvoirVersAvoirServiceRemote{
	private @EJB ITaAvoirServiceRemote taAvoirService;
	protected TaAvoir castDocumentSource() {
		return (TaAvoir)ds;
	}
	
	protected TaAvoir castDocumentDest() {
		return (TaAvoir)dd;
	}
	
	@Override
	public IDocumentTiers copieDocumentSpecifique(IDocumentTiers ds, IDocumentTiers dd
			,Boolean ligneSeparatrice,String libelleSeparateur) {
		try {
			((TaAvoir)dd).setTaTPaiement(((TaAvoir)ds).getTaTPaiement());
			((TaAvoir)dd).setDateEchDocument(((TaAvoir)ds).getDateEchDocument());
			((TaAvoir)dd).setRegleDocument(BigDecimal.valueOf(0));
			((TaAvoir)dd).setRemHtDocument(((TaAvoir)ds).getRemHtDocument());
			((TaAvoir)dd).setTxRemHtDocument(((TaAvoir)ds).getTxRemHtDocument());
			((TaAvoir)dd).setRemTtcDocument(((TaAvoir)ds).getRemTtcDocument());
			((TaAvoir)dd).setTxRemTtcDocument(((TaAvoir)ds).getTxRemTtcDocument());
			((TaAvoir)dd).setNbEDocument(((TaAvoir)ds).getNbEDocument());
			((TaAvoir)dd).setTtc(((TaAvoir)ds).getTtc());
			//((TaAvoir)dd).setExport(((TaAvoir)ds).getExport());
			((TaAvoir)dd).setCommentaire(((TaAvoir)ds).getCommentaire());
			
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
			

			for (ILigneDocumentTiers ligne : ((TaAvoir)ds).getLignes()) {
				TaLAvoir temp = (TaLAvoir)copieLigneDocument(ligne, new TaLAvoir()); 
				temp.setTaDocument(((TaAvoir)dd));
				((TaAvoir)dd).addLigne(temp);
			}

			TaInfosAvoir infos = (TaInfosAvoir)copieInfosDocument(((TaAvoir)ds).getTaInfosDocument(),new TaInfosAvoir());
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

		if(ld.getTaArticle()!=null && ((TaLAvoir)ls).getCodeTitreTransport()!=null  && documentGereLesCrd) {
			((TaLAvoir)ld).setCodeTitreTransport(((TaLAvoir)ls).getCodeTitreTransport());
			((TaLAvoir)ld).setQteTitreTransport(((TaLAvoir)ls).getQteTitreTransport());			
		} 		
		else {
			((TaLAvoir)ld).setCodeTitreTransport("");
			((TaLAvoir)ld).setQteTitreTransport(null);
		}
		return ld;
	}

	@Override
	public IDocumentTiers genereDocumentSpecifique(IDocumentTiers ds, IDocumentTiers dd,String codeTiers, boolean generationModele,boolean genereCode) {
		
		TaAvoir documentInit = ((TaAvoir)ds);
		try {
			if(documentInit!=null){
				documentInit.setLegrain(true);
//				dd=copieDocument(ds,dd);
				if(genereCode && !codeDejaGenere )((TaAvoir)dd).setCodeDocument(taAvoirService.genereCode(null));
				((TaAvoir)dd).setCommentaire(commentaire);
				taAvoirService.calculDateEcheanceAbstract(((TaAvoir)dd),taCPaiement.getReportCPaiement(),taCPaiement.getFinMoisCPaiement());
				((TaAvoir)dd).calculeTvaEtTotaux();	
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
