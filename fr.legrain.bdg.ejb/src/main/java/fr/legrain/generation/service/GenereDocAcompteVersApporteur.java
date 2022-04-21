package fr.legrain.generation.service;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import fr.legrain.bdg.documents.service.remote.ITaApporteurServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocAcompteVersApporteurServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.IInfosDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.model.TaAcompte;
import fr.legrain.document.model.TaApporteur;
import fr.legrain.document.model.TaBonliv;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaApporteur;
import fr.legrain.document.model.TaInfosApporteur;
import fr.legrain.document.model.TaLApporteur;
import fr.legrain.document.model.TaRDocument;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.lib.data.LibDate;

@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public  class GenereDocAcompteVersApporteur extends AbstractGenereDocVersApporteur implements IGenereDocAcompteVersApporteurServiceRemote{
	private @EJB ITaApporteurServiceRemote taApporteurService;
	protected TaAcompte castDocumentSource() {
		return (TaAcompte)ds;
	}
	
	protected TaApporteur castDocumentDest() {
		return (TaApporteur)dd;
	}
	
	@Override
	public IDocumentTiers copieDocumentSpecifique(IDocumentTiers ds, IDocumentTiers dd
			,Boolean ligneSeparatrice,String libelleSeparateur) {
		try {
//			((TaApporteur)dd).setTaTPaiement(((TaAcompte)ds).getTaTPaiement());
			((TaApporteur)dd).setDateEchDocument(((TaAcompte)ds).getDateEchDocument());
			((TaApporteur)dd).setRegleDocument(BigDecimal.valueOf(0));
			((TaApporteur)dd).setRemHtDocument(((TaAcompte)ds).getRemHtDocument());
			if(multiple)((TaApporteur)dd).setTxRemHtDocument(BigDecimal.valueOf(0));else((TaApporteur)dd).setTxRemHtDocument(((TaAcompte)ds).getTxRemHtDocument());
			((TaApporteur)dd).setRemTtcDocument(((TaAcompte)ds).getRemTtcDocument());
			((TaApporteur)dd).setTxRemTtcDocument(((TaAcompte)ds).getTxRemTtcDocument());
			((TaApporteur)dd).setNbEDocument(((TaAcompte)ds).getNbEDocument());
			((TaApporteur)dd).setTtc(((TaAcompte)ds).getTtc());
			((TaApporteur)dd).setDateExport(((TaAcompte)ds).getDateExport());
			((TaApporteur)dd).setCommentaire(((TaAcompte)ds).getCommentaire());
			
			TaEtat etatLigne=taEtatService.documentEncours(null);
			codeDejaGenere=false;
			
			Map<String, String> paramsCode = new LinkedHashMap<String, String>();
			if((codeTiers==null || codeTiers.isEmpty()) 
					&& ((TaApporteur)dd).getTaTiers()!=null)
				codeTiers=((TaApporteur)dd).getTaTiers().getCodeTiers();
			paramsCode.put(Const.C_NOMFOURNISSEUR, codeTiers);

			paramsCode.put(Const.C_DATEDOCUMENT, LibDate.dateToString(((TaApporteur)dd).getDateDocument()));				
			if(isGenereCode()) {
				((TaApporteur)dd).setCodeDocument(taApporteurService.genereCode(paramsCode));
				codeDejaGenere=true;
			}
				

			for (ILigneDocumentTiers ligne : ((TaAcompte)ds).getLignes()) {
				TaLApporteur temp = (TaLApporteur)copieLigneDocument(ligne, new TaLApporteur()); 
				temp.setTaDocument(((TaApporteur)dd));
				((TaApporteur)dd).addLigne(temp);
			}

			TaInfosApporteur infos = (TaInfosApporteur)copieInfosDocument(((TaAcompte)ds).getTaInfosDocument(),new TaInfosApporteur());
			infos.setTaDocument(((TaApporteur)dd));
			((TaApporteur)dd).setTaInfosDocument(infos);

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
				if(genereCode && !codeDejaGenere )((TaApporteur)dd).setCodeDocument(taApporteurService.genereCode(null));
				((TaApporteur)dd).setCommentaire(commentaire);
				taApporteurService.calculDateEcheanceAbstract(((TaApporteur)dd),taCPaiement.getReportCPaiement(),taCPaiement.getFinMoisCPaiement());
				if(ds.getRelationDocument()){
				TaRDocument taRDocument = new TaRDocument();
				taRDocument.setTaApporteur(((TaApporteur)dd));
				taRDocument.setTaAcompte(documentInit);
				taRDocument.setIdSrc(documentInit.getIdDocument());
				((TaApporteur)dd).getTaRDocuments().add(taRDocument);
				}
				((TaApporteur)dd).calculeTvaEtTotaux();	
			}
			return dd;
		} catch (Exception e) {
			logger.error("",e);
			return null;
		}
	}
	
	public String creationRequeteDejaGenere(IDocumentTiers ds) {
		int idDoc = ds.getIdDocument();
		String jpql = "select x from TaRDocument x where x.taAcompte.idDocument="+idDoc+" and x.taApporteur is not null";
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
		return super.genereDocument(document, docGenere, codeTiers, b, generationModele,genereCode,multiple);
	}

	@Override
	public void setRelationDocument(boolean relationDocument) {
		super.setRelationDocument(relationDocument);
		
	}



}
