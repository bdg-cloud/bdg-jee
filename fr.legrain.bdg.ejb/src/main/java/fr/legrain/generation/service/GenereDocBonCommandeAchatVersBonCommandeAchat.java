package fr.legrain.generation.service;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import fr.legrain.bdg.documents.service.remote.ITaBoncdeAchatServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocBonCommandeAchatVersBonCommandeAchatServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocBonCommandeVersBonCommandeServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.IInfosDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.model.TaBoncdeAchat;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaBoncdeAchat;
import fr.legrain.document.model.TaInfosBoncdeAchat;
import fr.legrain.document.model.TaLBoncdeAchat;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.lib.data.LibDate;


@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public  class GenereDocBonCommandeAchatVersBonCommandeAchat extends AbstractGenereDocVersBonCommandeAchat implements IGenereDocBonCommandeAchatVersBonCommandeAchatServiceRemote{
	private @EJB ITaBoncdeAchatServiceRemote taBoncdeService;
	protected TaBoncdeAchat castDocumentSource() {
		return (TaBoncdeAchat)ds;
	}
	
	protected TaBoncdeAchat castDocumentDest() {
		return (TaBoncdeAchat)dd;
	}
	
	@Override
	public IDocumentTiers copieDocumentSpecifique(IDocumentTiers ds, IDocumentTiers dd
			,Boolean ligneSeparatrice,String libelleSeparateur) {
		try {
			((TaBoncdeAchat)dd).setTaTPaiement(((TaBoncdeAchat)ds).getTaTPaiement());
			((TaBoncdeAchat)dd).setDateEchDocument(((TaBoncdeAchat)ds).getDateEchDocument());
//			((TaBoncdeAchat)dd).setRegleDocument(BigDecimal.valueOf(0));
			((TaBoncdeAchat)dd).setRemHtDocument(((TaBoncdeAchat)ds).getRemHtDocument());
			((TaBoncdeAchat)dd).setTxRemHtDocument(((TaBoncdeAchat)ds).getTxRemHtDocument());
			((TaBoncdeAchat)dd).setRemTtcDocument(((TaBoncdeAchat)ds).getRemTtcDocument());
			((TaBoncdeAchat)dd).setTxRemTtcDocument(((TaBoncdeAchat)ds).getTxRemTtcDocument());
			((TaBoncdeAchat)dd).setNbEDocument(((TaBoncdeAchat)ds).getNbEDocument());
			((TaBoncdeAchat)dd).setTtc(((TaBoncdeAchat)ds).getTtc());
			//((TaBoncdeAchat)dd).setExport(((TaBoncdeAchat)ds).getExport());
			((TaBoncdeAchat)dd).setCommentaire(((TaBoncdeAchat)ds).getCommentaire());
			
			TaEtat etatLigne=taEtatService.documentEncours(null);
			codeDejaGenere=false;
			
			Map<String, String> paramsCode = new LinkedHashMap<String, String>();
			if((codeTiers==null || codeTiers.isEmpty()) 
					&& ((TaBoncdeAchat)dd).getTaTiers()!=null)
				codeTiers=((TaBoncdeAchat)dd).getTaTiers().getCodeTiers();
			paramsCode.put(Const.C_NOMFOURNISSEUR, codeTiers);

			paramsCode.put(Const.C_DATEDOCUMENT, LibDate.dateToString(((TaBoncdeAchat)dd).getDateDocument()));				
			if(isGenereCode()) {
				((TaBoncdeAchat)dd).setCodeDocument(taBoncdeService.genereCode(paramsCode));
				codeDejaGenere=true;
			}
			

			for (ILigneDocumentTiers ligne : ((TaBoncdeAchat)ds).getLignes()) {
				TaLBoncdeAchat temp = (TaLBoncdeAchat)copieLigneDocument(ligne, new TaLBoncdeAchat()); 
				temp.setTaDocument(((TaBoncdeAchat)dd));
				((TaBoncdeAchat)dd).addLigne(temp);
			}

			TaInfosBoncdeAchat infos = (TaInfosBoncdeAchat)copieInfosDocument(((TaBoncdeAchat)ds).getTaInfosDocument(),new TaInfosBoncdeAchat());
			infos.setTaDocument(((TaBoncdeAchat)dd));
			((TaBoncdeAchat)dd).setTaInfosDocument(infos);

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
		
		TaBoncdeAchat documentInit = ((TaBoncdeAchat)ds);
		try {
			if(documentInit!=null){
				documentInit.setLegrain(true);
				if(genereCode && !codeDejaGenere )((TaBoncdeAchat)dd).setCodeDocument(taBoncdeService.genereCode(null));
				((TaBoncdeAchat)dd).setCommentaire(commentaire);
				taBoncdeService.calculDateEcheanceAbstract(((TaBoncdeAchat)dd),taCPaiement.getReportCPaiement(),taCPaiement.getFinMoisCPaiement());
				((TaBoncdeAchat)dd).calculeTvaEtTotaux();	
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
