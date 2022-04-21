package fr.legrain.generation.service;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import fr.legrain.bdg.documents.service.remote.ITaBoncdeServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocAcompteVersBonCommandeServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.IInfosDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.model.TaAcompte;
import fr.legrain.document.model.TaBoncde;
import fr.legrain.document.model.TaBonliv;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaBoncde;
import fr.legrain.document.model.TaInfosBoncde;
import fr.legrain.document.model.TaLBoncde;
import fr.legrain.document.model.TaRDocument;
import fr.legrain.document.model.TaRReglementLiaison;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.lib.data.IHMEtat;
import fr.legrain.lib.data.LibDate;

@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public  class GenereDocAcompteVersBonCommande extends AbstractGenereDocVersBonCommande implements IGenereDocAcompteVersBonCommandeServiceRemote{
	private @EJB ITaBoncdeServiceRemote taBoncdeService;
	protected TaAcompte castDocumentSource() {
		return (TaAcompte)ds;
	}
	
	protected TaBoncde castDocumentDest() {
		return (TaBoncde)dd;
	}
	
	@Override
	public IDocumentTiers copieDocumentSpecifique(IDocumentTiers ds, IDocumentTiers dd
			,Boolean ligneSeparatrice,String libelleSeparateur) {
		try {
//			((TaBoncde)dd).setTaTPaiement(((TaAcompte)ds).getTaTPaiement());
			((TaBoncde)dd).setDateEchDocument(((TaAcompte)ds).getDateEchDocument());
			((TaBoncde)dd).setRegleDocument(BigDecimal.valueOf(0));
			((TaBoncde)dd).setRemHtDocument(((TaAcompte)ds).getRemHtDocument());
			if(multiple)((TaBoncde)dd).setTxRemHtDocument(BigDecimal.valueOf(0));else((TaBoncde)dd).setTxRemHtDocument(((TaAcompte)ds).getTxRemHtDocument());
			((TaBoncde)dd).setRemTtcDocument(((TaAcompte)ds).getRemTtcDocument());
			((TaBoncde)dd).setTxRemTtcDocument(((TaAcompte)ds).getTxRemTtcDocument());
			((TaBoncde)dd).setNbEDocument(((TaAcompte)ds).getNbEDocument());
			((TaBoncde)dd).setTtc(((TaAcompte)ds).getTtc());
			((TaBoncde)dd).setDateExport(((TaAcompte)ds).getDateExport());
			((TaBoncde)dd).setCommentaire(((TaAcompte)ds).getCommentaire());
			
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
			

			for (ILigneDocumentTiers ligne : ((TaAcompte)ds).getLignes()) {
				TaLBoncde temp = (TaLBoncde)copieLigneDocument(ligne, new TaLBoncde()); 
				temp.setTaDocument(((TaBoncde)dd));
				((TaBoncde)dd).addLigne(temp);
			}

			TaInfosBoncde infos = (TaInfosBoncde)copieInfosDocument(((TaAcompte)ds).getTaInfosDocument(),new TaInfosBoncde());
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
		return ld;
	}

	@Override
	public IDocumentTiers genereDocumentSpecifique(IDocumentTiers ds, IDocumentTiers dd,String codeTiers, boolean generationModele,boolean genereCode) {
		TaAcompte documentInit = ((TaAcompte)ds);
		try {
			
			if(documentInit!=null){
				documentInit.setLegrain(true);
//				dd=copieDocument(ds,dd);
				if(genereCode && !codeDejaGenere )((TaBoncde)dd).setCodeDocument(taBoncdeService.genereCode(null));
				((TaBoncde)dd).setCommentaire(commentaire);
				taBoncdeService.calculDateEcheanceAbstract(((TaBoncde)dd),taCPaiement.getReportCPaiement(),taCPaiement.getFinMoisCPaiement());
				if(ds.getRelationDocument()){
				TaRDocument taRDocument = new TaRDocument();
				taRDocument.setTaBoncde(((TaBoncde)dd));
				taRDocument.setTaAcompte(documentInit);
				taRDocument.setIdSrc(documentInit.getIdDocument());
				((TaBoncde)dd).getTaRDocuments().add(taRDocument);
				}
				((TaBoncde)dd).calculeTvaEtTotaux();
				
//				if(!generationModele){
//				if(ds.getTaRReglementLiaisons()!=null && ds.getTaRReglementLiaisons().size()>0) {
//					for (TaRReglementLiaison rr : ds.getTaRReglementLiaisons()) {
//						TaRReglementLiaison taRReglement = new TaRReglementLiaison();  
//						
//						taRReglement.setTaReglement(rr.getTaReglement());
//						taRReglement.setAffectation(rr.getAffectation());
//						taRReglement.setTaBoncde((TaBoncde)dd);
//						taRReglement.setEtat(IHMEtat.integre);
//						((TaBoncde)dd).addRReglementLiaison(taRReglement);
//					}
//				}
//			}
//			((TaBoncde)dd).calculRegleDocument();
//			((TaBoncde)dd).calculRegleDocumentComplet();
//			((TaBoncde)dd).setNetAPayer(((TaBoncde)dd).calculResteAReglerComplet());
				
			}
			return dd;
		} catch (Exception e) {
			logger.error("",e);
			return null;
		}
	}
	
	public String creationRequeteDejaGenere(IDocumentTiers ds) {
		int idDoc = ds.getIdDocument();
		String jpql = "select x from TaRDocument x where x.taAcompte.idDocument="+idDoc+" and x.taBoncde is not null";
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
