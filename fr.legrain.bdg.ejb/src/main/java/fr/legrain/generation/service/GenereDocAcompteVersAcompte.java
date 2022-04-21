package fr.legrain.generation.service;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import fr.legrain.bdg.documents.service.remote.ITaAcompteServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocAcompteVersAcompteServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.IInfosDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.model.TaAcompte;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaAcompte;
import fr.legrain.document.model.TaInfosAcompte;
import fr.legrain.document.model.TaLAcompte;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.lib.data.LibDate;


@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public  class GenereDocAcompteVersAcompte extends AbstractGenereDocVersAcompte implements IGenereDocAcompteVersAcompteServiceRemote{
	private @EJB ITaAcompteServiceRemote taAcompteService;
	protected TaAcompte castDocumentSource() {
		return (TaAcompte)ds;
	}
	
	protected TaAcompte castDocumentDest() {
		return (TaAcompte)dd;
	}
	
	@Override
	public IDocumentTiers copieDocumentSpecifique(IDocumentTiers ds, IDocumentTiers dd
			,Boolean ligneSeparatrice,String libelleSeparateur) {
		try {
			((TaAcompte)dd).setTaTPaiement(((TaAcompte)ds).getTaTPaiement());
			((TaAcompte)dd).setDateEchDocument(((TaAcompte)ds).getDateEchDocument());
			((TaAcompte)dd).setRegleDocument(BigDecimal.valueOf(0));
			((TaAcompte)dd).setRemHtDocument(((TaAcompte)ds).getRemHtDocument());
			((TaAcompte)dd).setTxRemHtDocument(((TaAcompte)ds).getTxRemHtDocument());
			((TaAcompte)dd).setRemTtcDocument(((TaAcompte)ds).getRemTtcDocument());
			((TaAcompte)dd).setTxRemTtcDocument(((TaAcompte)ds).getTxRemTtcDocument());
			((TaAcompte)dd).setNbEDocument(((TaAcompte)ds).getNbEDocument());
			((TaAcompte)dd).setTtc(((TaAcompte)ds).getTtc());
			//((TaAcompte)dd).setExport(((TaAcompte)ds).getExport());
			((TaAcompte)dd).setCommentaire(((TaAcompte)ds).getCommentaire());
			
			TaEtat etatLigne=taEtatService.documentEncours(null);
			codeDejaGenere=false;
			
			Map<String, String> paramsCode = new LinkedHashMap<String, String>();
			if((codeTiers==null || codeTiers.isEmpty()) 
					&& ((TaAcompte)dd).getTaTiers()!=null)
				codeTiers=((TaAcompte)dd).getTaTiers().getCodeTiers();
			paramsCode.put(Const.C_NOMFOURNISSEUR, codeTiers);

			paramsCode.put(Const.C_DATEDOCUMENT, LibDate.dateToString(((TaAcompte)dd).getDateDocument()));				
			if(isGenereCode()) {
				((TaAcompte)dd).setCodeDocument(taAcompteService.genereCode(paramsCode));
				codeDejaGenere=true;
			}
						
			for (ILigneDocumentTiers ligne : ((TaAcompte)ds).getLignes()) {
				TaLAcompte temp = (TaLAcompte)copieLigneDocument(ligne, new TaLAcompte()); 
				temp.setTaDocument(((TaAcompte)dd));
				((TaAcompte)dd).addLigne(temp);
			}

			TaInfosAcompte infos = (TaInfosAcompte)copieInfosDocument(((TaAcompte)ds).getTaInfosDocument(),new TaInfosAcompte());
			infos.setTaDocument(((TaAcompte)dd));
			((TaAcompte)dd).setTaInfosDocument(infos);
			
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
		
		TaAcompte documentInit = ((TaAcompte)ds);
		try {
			if(documentInit!=null){
				documentInit.setLegrain(true);
//				dd=copieDocument(ds,dd);
				if(genereCode && !codeDejaGenere )((TaAcompte)dd).setCodeDocument(taAcompteService.genereCode(null));
				((TaAcompte)dd).setCommentaire(commentaire);
				taAcompteService.calculDateEcheanceAbstract(((TaAcompte)dd),taCPaiement.getReportCPaiement(),taCPaiement.getFinMoisCPaiement());
				
				((TaAcompte)dd).calculeTvaEtTotaux();	
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
		return super.genereDocument(document, docGenere, codeTiers, b, generationModele,genereCode,multiple);
	}

	@Override
	public void setRelationDocument(boolean relationDocument) {
		super.setRelationDocument(relationDocument);
		
	}



}
