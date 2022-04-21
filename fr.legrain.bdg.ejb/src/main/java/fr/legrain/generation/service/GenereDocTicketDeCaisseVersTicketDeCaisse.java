package fr.legrain.generation.service;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import fr.legrain.bdg.documents.service.remote.ITaTicketDeCaisseServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocTicketDeCaisseVersTicketDeCaisseServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.IInfosDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaInfosTicketDeCaisse;
import fr.legrain.document.model.TaLTicketDeCaisse;
import fr.legrain.document.model.TaTicketDeCaisse;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.lib.data.LibDate;


@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public  class GenereDocTicketDeCaisseVersTicketDeCaisse extends AbstractGenereDocVersFacture implements IGenereDocTicketDeCaisseVersTicketDeCaisseServiceRemote{
	private @EJB ITaTicketDeCaisseServiceRemote taTicketDeCaisseService;


	protected TaTicketDeCaisse castDocumentSource() {
		return (TaTicketDeCaisse)ds;
	}
	
	protected TaTicketDeCaisse castDocumentDest() {
		return (TaTicketDeCaisse)dd;
	}
	
	@Override
	public IDocumentTiers copieDocumentSpecifique(IDocumentTiers ds, IDocumentTiers dd
			,Boolean ligneSeparatrice,String libelleSeparateur) {
		try {
//			((TaTicketDeCaisse)dd).setTaTPaiement(((TaTicketDeCaisse)ds).getTaTPaiement());
			((TaTicketDeCaisse)dd).setDateEchDocument(((TaTicketDeCaisse)ds).getDateEchDocument());
			((TaTicketDeCaisse)dd).setRegleDocument(BigDecimal.valueOf(0));
			((TaTicketDeCaisse)dd).setRemHtDocument(((TaTicketDeCaisse)ds).getRemHtDocument());
			((TaTicketDeCaisse)dd).setTxRemHtDocument(((TaTicketDeCaisse)ds).getTxRemHtDocument());
			((TaTicketDeCaisse)dd).setRemTtcDocument(((TaTicketDeCaisse)ds).getRemTtcDocument());
			((TaTicketDeCaisse)dd).setTxRemTtcDocument(((TaTicketDeCaisse)ds).getTxRemTtcDocument());
			((TaTicketDeCaisse)dd).setNbEDocument(((TaTicketDeCaisse)ds).getNbEDocument());
			((TaTicketDeCaisse)dd).setTtc(((TaTicketDeCaisse)ds).getTtc());
			//((TaTicketDeCaisse)dd).setExport(((TaTicketDeCaisse)ds).getExport());
			((TaTicketDeCaisse)dd).setCommentaire(((TaTicketDeCaisse)ds).getCommentaire());
			
			TaEtat etatLigne=taEtatService.documentEncours(null);
			codeDejaGenere=false;
			
			Map<String, String> paramsCode = new LinkedHashMap<String, String>();
			if((codeTiers==null || codeTiers.isEmpty()) 
					&& ((TaTicketDeCaisse)dd).getTaTiers()!=null)
				codeTiers=((TaTicketDeCaisse)dd).getTaTiers().getCodeTiers();
			paramsCode.put(Const.C_NOMFOURNISSEUR, codeTiers);

			paramsCode.put(Const.C_DATEDOCUMENT, LibDate.dateToString(((TaTicketDeCaisse)dd).getDateDocument()));				
			if(isGenereCode()) {
				((TaTicketDeCaisse)dd).setCodeDocument(taTicketDeCaisseService.genereCode(paramsCode));
				codeDejaGenere=true;
			}
						

			for (ILigneDocumentTiers ligne : ((TaTicketDeCaisse)ds).getLignes()) {
				TaLTicketDeCaisse temp = (TaLTicketDeCaisse)copieLigneDocument(ligne, new TaLTicketDeCaisse()); 
				temp.setTaDocument(((TaTicketDeCaisse)dd));
				((TaTicketDeCaisse)dd).addLigne(temp);
			}

			TaInfosTicketDeCaisse infos = (TaInfosTicketDeCaisse)copieInfosDocument(((TaTicketDeCaisse)ds).getTaInfosDocument(),new TaInfosTicketDeCaisse());
			infos.setTaDocument(((TaTicketDeCaisse)dd));
			((TaTicketDeCaisse)dd).setTaInfosDocument(infos);

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
		
		TaTicketDeCaisse documentInit = ((TaTicketDeCaisse)ds);
		try {
			if(documentInit!=null){
				documentInit.setLegrain(true);
				if(genereCode && !codeDejaGenere )((TaTicketDeCaisse)dd).setCodeDocument(taFactureService.genereCode(null));
				((TaTicketDeCaisse)dd).setCommentaire(commentaire);
				taTicketDeCaisseService.calculDateEcheanceAbstract(((TaTicketDeCaisse)dd),taCPaiement.getReportCPaiement(),taCPaiement.getFinMoisCPaiement());
				((TaTicketDeCaisse)dd).calculeTvaEtTotaux();	
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
