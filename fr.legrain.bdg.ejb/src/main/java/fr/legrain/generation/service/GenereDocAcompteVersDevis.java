package fr.legrain.generation.service;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import fr.legrain.bdg.documents.service.remote.ITaDevisServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocAcompteVersDevisServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.IInfosDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.model.TaAcompte;
import fr.legrain.document.model.TaBonliv;
import fr.legrain.document.model.TaDevis;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaDevis;
import fr.legrain.document.model.TaInfosDevis;
import fr.legrain.document.model.TaLDevis;
import fr.legrain.document.model.TaRDocument;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.lib.data.LibDate;

@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public  class GenereDocAcompteVersDevis extends AbstractGenereDocVersDevis implements IGenereDocAcompteVersDevisServiceRemote{
	private @EJB ITaDevisServiceRemote taDevisService;
	protected TaAcompte castDocumentSource() {
		return (TaAcompte)ds;
	}
	
	protected TaDevis castDocumentDest() {
		return (TaDevis)dd;
	}
	
	@Override
	public IDocumentTiers copieDocumentSpecifique(IDocumentTiers ds, IDocumentTiers dd
			,Boolean ligneSeparatrice,String libelleSeparateur) {
		try {
//			((TaDevis)dd).setTaTPaiement(((TaAcompte)ds).getTaTPaiement());
			((TaDevis)dd).setDateEchDocument(((TaAcompte)ds).getDateEchDocument());
			((TaDevis)dd).setRegleDocument(BigDecimal.valueOf(0));
			((TaDevis)dd).setRemHtDocument(((TaAcompte)ds).getRemHtDocument());
			if(multiple)((TaDevis)dd).setTxRemHtDocument(BigDecimal.valueOf(0));else((TaDevis)dd).setTxRemHtDocument(((TaAcompte)ds).getTxRemHtDocument());
			((TaDevis)dd).setRemTtcDocument(((TaAcompte)ds).getRemTtcDocument());
			((TaDevis)dd).setTxRemTtcDocument(((TaAcompte)ds).getTxRemTtcDocument());
			((TaDevis)dd).setNbEDocument(((TaAcompte)ds).getNbEDocument());
			((TaDevis)dd).setTtc(((TaAcompte)ds).getTtc());
			((TaDevis)dd).setDateExport(((TaAcompte)ds).getDateExport());
			((TaDevis)dd).setCommentaire(((TaAcompte)ds).getCommentaire());
//			((TaDevis)dd).setUtiliseUniteSaisie(((TaAcompte)ds).getUtiliseUniteSaisie());
			
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
			

			for (ILigneDocumentTiers ligne : ((TaAcompte)ds).getLignes()) {
				TaLDevis temp = (TaLDevis)copieLigneDocument(ligne, new TaLDevis()); 
				temp.setTaDocument(((TaDevis)dd));
				((TaDevis)dd).addLigne(temp);
			}

			TaInfosDevis infos = (TaInfosDevis)copieInfosDocument(((TaAcompte)ds).getTaInfosDocument(),new TaInfosDevis());
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
		return ld;
	}

	@Override
	public IDocumentTiers genereDocumentSpecifique(IDocumentTiers ds, IDocumentTiers dd,String codeTiers, boolean generationModele,boolean genereCode) {
		TaAcompte documentInit = ((TaAcompte)ds);
		try {
			
			if(documentInit!=null){
				documentInit.setLegrain(true);
//				dd=copieDocument(ds,dd);
				if(genereCode && !codeDejaGenere )((TaDevis)dd).setCodeDocument(taDevisService.genereCode(null));
				((TaDevis)dd).setCommentaire(commentaire);
				taDevisService.calculDateEcheanceAbstract(((TaDevis)dd),taCPaiement.getReportCPaiement(),taCPaiement.getFinMoisCPaiement());
				if(ds.getRelationDocument()){
				TaRDocument taRDocument = new TaRDocument();
				taRDocument.setTaDevis(((TaDevis)dd));
				taRDocument.setTaAcompte(documentInit);
				taRDocument.setIdSrc(documentInit.getIdDocument());
				((TaDevis)dd).getTaRDocuments().add(taRDocument);
				}
				((TaDevis)dd).calculeTvaEtTotaux();	
				
//				if(!generationModele){
//				if(ds.getTaRReglementLiaisons()!=null && ds.getTaRReglementLiaisons().size()>0) {
//					for (TaRReglementLiaison rr : ds.getTaRReglementLiaisons()) {
//						TaRReglementLiaison taRReglement = new TaRReglementLiaison(); 
//						
//						taRReglement.setTaReglement(rr.getTaReglement());
//						taRReglement.setAffectation(rr.getAffectation());
//						taRReglement.setTaDevis((TaDevis)dd);
//						taRReglement.setEtat(IHMEtat.integre);
//						((TaDevis)dd).addRReglementLiaison(taRReglement);
//					}
//				}
//			}
//			((TaDevis)dd).calculRegleDocument();
//			((TaDevis)dd).calculRegleDocumentComplet();
//			((TaDevis)dd).setNetAPayer(((TaDevis)dd).calculResteAReglerComplet());
				
			}
			return dd;
		} catch (Exception e) {
			logger.error("",e);
			return null;
		}
	}
	
	public String creationRequeteDejaGenere(IDocumentTiers ds) {
		int idDoc = ds.getIdDocument();
		String jpql = "select x from TaRDocument x where x.taAcompte.idDocument="+idDoc+" and x.taDevis is not null";
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
