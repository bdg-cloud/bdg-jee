package fr.legrain.generation.service;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import fr.legrain.bdg.documents.service.remote.ITaAvisEcheanceServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocAvisEcheanceVersAvisEcheanceServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.IInfosDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.model.TaAvisEcheance;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaAvisEcheance;
import fr.legrain.document.model.TaInfosAvisEcheance;
import fr.legrain.document.model.TaLAvisEcheance;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.lib.data.LibDate;

@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public  class GenereDocAvisEcheanceVersAvisEcheance extends AbstractGenereDocVersProforma implements IGenereDocAvisEcheanceVersAvisEcheanceServiceRemote{
	private @EJB ITaAvisEcheanceServiceRemote taAvisEcheanceService;
	protected TaAvisEcheance castDocumentSource() {
		return (TaAvisEcheance)ds;
	}
	
	protected TaAvisEcheance castDocumentDest() {
		return (TaAvisEcheance)dd;
	}
	
	@Override
	public IDocumentTiers copieDocumentSpecifique(IDocumentTiers ds, IDocumentTiers dd
			,Boolean ligneSeparatrice,String libelleSeparateur) {
		try {
			((TaAvisEcheance)dd).setTaTPaiement(((TaAvisEcheance)ds).getTaTPaiement());
			((TaAvisEcheance)dd).setDateEchDocument(((TaAvisEcheance)ds).getDateEchDocument());
//			((TaAvisEcheance)dd).setRegleDocument(BigDecimal.valueOf(0));
			((TaAvisEcheance)dd).setRemHtDocument(((TaAvisEcheance)ds).getRemHtDocument());
			((TaAvisEcheance)dd).setTxRemHtDocument(((TaAvisEcheance)ds).getTxRemHtDocument());
			((TaAvisEcheance)dd).setRemTtcDocument(((TaAvisEcheance)ds).getRemTtcDocument());
			((TaAvisEcheance)dd).setTxRemTtcDocument(((TaAvisEcheance)ds).getTxRemTtcDocument());
			((TaAvisEcheance)dd).setNbEDocument(((TaAvisEcheance)ds).getNbEDocument());
			((TaAvisEcheance)dd).setTtc(((TaAvisEcheance)ds).getTtc());
			//((TaAvisEcheance)dd).setExport(((TaAvisEcheance)ds).getExport());
			((TaAvisEcheance)dd).setCommentaire(((TaAvisEcheance)ds).getCommentaire());
			
			TaEtat etatLigne=taEtatService.documentEncours(null);
			codeDejaGenere=false;
			
			Map<String, String> paramsCode = new LinkedHashMap<String, String>();
			if((codeTiers==null || codeTiers.isEmpty()) 
					&& ((TaAvisEcheance)dd).getTaTiers()!=null)
				codeTiers=((TaAvisEcheance)dd).getTaTiers().getCodeTiers();
			paramsCode.put(Const.C_NOMFOURNISSEUR, codeTiers);

			paramsCode.put(Const.C_DATEDOCUMENT, LibDate.dateToString(((TaAvisEcheance)dd).getDateDocument()));				
			if(isGenereCode()) {
				((TaAvisEcheance)dd).setCodeDocument(taAvisEcheanceService.genereCode(paramsCode));
				codeDejaGenere=true;
			}
			

			for (ILigneDocumentTiers ligne : ((TaAvisEcheance)ds).getLignes()) {
				TaLAvisEcheance temp = (TaLAvisEcheance)copieLigneDocument(ligne, new TaLAvisEcheance()); 
				temp.setTaDocument(((TaAvisEcheance)dd));
				((TaAvisEcheance)dd).addLigne(temp);
			}

			TaInfosAvisEcheance infos = (TaInfosAvisEcheance)copieInfosDocument(((TaAvisEcheance)ds).getTaInfosDocument(),new TaInfosAvisEcheance());
			infos.setTaDocument(((TaAvisEcheance)dd));
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
		
		TaAvisEcheance documentInit = ((TaAvisEcheance)ds);
		try {
			if(documentInit!=null){
				documentInit.setLegrain(true);
//				dd=copieDocument(ds,dd);
				if(genereCode && !codeDejaGenere )((TaAvisEcheance)dd).setCodeDocument(taAvisEcheanceService.genereCode(null));
				((TaAvisEcheance)dd).setCommentaire(commentaire);
				taAvisEcheanceService.calculDateEcheanceAbstract(((TaAvisEcheance)dd),taCPaiement.getReportCPaiement(),taCPaiement.getFinMoisCPaiement());
				((TaAvisEcheance)dd).calculeTvaEtTotaux();	
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
		return super.genereDocument(document, docGenere, codeTiers, b, generationModele, genereCode,multiple);
	}

	@Override
	public void setRelationDocument(boolean relationDocument) {
		super.setRelationDocument(relationDocument);
		
	}



}
