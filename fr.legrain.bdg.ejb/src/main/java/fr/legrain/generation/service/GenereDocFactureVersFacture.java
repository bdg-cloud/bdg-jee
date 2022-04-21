package fr.legrain.generation.service;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.security.DeclareRoles;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import fr.legrain.bdg.generation.service.remote.IGenereDocFactureVersFactureServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.IInfosDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaInfosFacture;
import fr.legrain.document.model.TaLAcompte;
import fr.legrain.document.model.TaLFacture;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.lib.data.LibDate;


@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public  class GenereDocFactureVersFacture extends AbstractGenereDocVersFacture implements IGenereDocFactureVersFactureServiceRemote{

//	public GenereDocFactureVersFacture(ITaFactureServiceRemote taFactureService) {
//		super(taFactureService);
//		// TODO Auto-generated constructor stub
//	}

	protected TaFacture castDocumentSource() {
		return (TaFacture)ds;
	}
	
	protected TaFacture castDocumentDest() {
		return (TaFacture)dd;
	}
	
	@Override
	public IDocumentTiers copieDocumentSpecifique(IDocumentTiers ds, IDocumentTiers dd
			,Boolean ligneSeparatrice,String libelleSeparateur) {
		try {
//			((TaFacture)dd).setTaTPaiement(((TaFacture)ds).getTaTPaiement());
			((TaFacture)dd).setDateEchDocument(((TaFacture)dd).getDateEchDocument());
			((TaFacture)dd).setRegleDocument(BigDecimal.valueOf(0));
			((TaFacture)dd).setRemHtDocument(((TaFacture)ds).getRemHtDocument());
			((TaFacture)dd).setTxRemHtDocument(((TaFacture)ds).getTxRemHtDocument());
			((TaFacture)dd).setRemTtcDocument(((TaFacture)ds).getRemTtcDocument());
			((TaFacture)dd).setTxRemTtcDocument(((TaFacture)ds).getTxRemTtcDocument());
			((TaFacture)dd).setNbEDocument(((TaFacture)ds).getNbEDocument());
			((TaFacture)dd).setTtc(((TaFacture)ds).getTtc());
			//((TaFacture)dd).setExport(((TaFacture)ds).getExport());
			((TaFacture)dd).setCommentaire(((TaFacture)ds).getCommentaire());
			
			TaEtat etatLigne=taEtatService.documentEncours(null);
			codeDejaGenere=false;
			
			Map<String, String> paramsCode = new LinkedHashMap<String, String>();
			if((codeTiers==null || codeTiers.isEmpty()) 
					&& ((TaFacture)dd).getTaTiers()!=null)
				codeTiers=((TaFacture)dd).getTaTiers().getCodeTiers();
			paramsCode.put(Const.C_NOMFOURNISSEUR, codeTiers);

			paramsCode.put(Const.C_DATEDOCUMENT, LibDate.dateToString(((TaFacture)dd).getDateDocument()));				
			if(isGenereCode()) {
				((TaFacture)dd).setCodeDocument(taFactureService.genereCode(paramsCode));
				codeDejaGenere=true;
			}
						

			for (ILigneDocumentTiers ligne : ((TaFacture)ds).getLignes()) {
				TaLFacture temp = (TaLFacture)copieLigneDocument(ligne, new TaLFacture()); 
				temp.setTaDocument(((TaFacture)dd));
				((TaFacture)dd).addLigne(temp);
			}

			TaInfosFacture infos = (TaInfosFacture)copieInfosDocument(((TaFacture)ds).getTaInfosDocument(),new TaInfosFacture());
			infos.setTaDocument(((TaFacture)dd));
			((TaFacture)dd).setTaInfosDocument(infos);

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
		if(ld.getTaArticle()!=null && ((TaLFacture)ls).getCodeTitreTransport()!=null ) {
			((TaLFacture)ld).setCodeTitreTransport(((TaLFacture)ls).getCodeTitreTransport());
			((TaLFacture)ld).setQteTitreTransport(((TaLFacture)ls).getQteTitreTransport());			
		} 
		else {
			((TaLFacture)ld).setCodeTitreTransport("");
			((TaLFacture)ld).setQteTitreTransport(null);
		}
		return ld;
	}

	@Override
	public IDocumentTiers genereDocumentSpecifique(IDocumentTiers ds, IDocumentTiers dd,String codeTiers, boolean generationModele,boolean genereCode) {
		
		TaFacture documentInit = ((TaFacture)ds);
		try {
			if(documentInit!=null){
				documentInit.setLegrain(true);
				if(genereCode && !codeDejaGenere )((TaFacture)dd).setCodeDocument(taFactureService.genereCode(null));
				((TaFacture)dd).setCommentaire(commentaire);
				taFactureService.calculDateEcheanceAbstract(((TaFacture)dd),taCPaiement.getReportCPaiement(),taCPaiement.getFinMoisCPaiement());
				((TaFacture)dd).calculeTvaEtTotaux();	
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
