package fr.legrain.generation.service;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import fr.legrain.bdg.documents.service.remote.ITaBonlivServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocBonLivraisonVersBonLivraisonServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.IInfosDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.model.TaBonliv;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaBonliv;
import fr.legrain.document.model.TaInfosBonliv;
import fr.legrain.document.model.TaLAvoir;
import fr.legrain.document.model.TaLBonliv;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.lib.data.LibDate;


@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public  class GenereDocBonLivraisonVersBonLivraison extends AbstractGenereDocVersBonLivraison implements IGenereDocBonLivraisonVersBonLivraisonServiceRemote{
	private @EJB ITaBonlivServiceRemote taBonlivService;
	protected TaBonliv castDocumentSource() {
		return (TaBonliv)ds;
	}
	
	protected TaBonliv castDocumentDest() {
		return (TaBonliv)dd;
	}
	
	@Override
	public IDocumentTiers copieDocumentSpecifique(IDocumentTiers ds, IDocumentTiers dd
			,Boolean ligneSeparatrice,String libelleSeparateur) {
		try {
			((TaBonliv)dd).setTaTPaiement(((TaBonliv)ds).getTaTPaiement());
//			((TaBonliv)dd).setDateEchDocument(((TaBonliv)ds).getDateEchDocument());
//			((TaBonliv)dd).setRegleDocument(BigDecimal.valueOf(0));
			((TaBonliv)dd).setRemHtDocument(((TaBonliv)ds).getRemHtDocument());
			((TaBonliv)dd).setTxRemHtDocument(((TaBonliv)ds).getTxRemHtDocument());
			((TaBonliv)dd).setRemTtcDocument(((TaBonliv)ds).getRemTtcDocument());
			((TaBonliv)dd).setTxRemTtcDocument(((TaBonliv)ds).getTxRemTtcDocument());
			((TaBonliv)dd).setNbEDocument(((TaBonliv)ds).getNbEDocument());
			((TaBonliv)dd).setTtc(((TaBonliv)ds).getTtc());
//			((TaBonliv)dd).setExport(((TaBonliv)ds).getExport());
			((TaBonliv)dd).setCommentaire(((TaBonliv)ds).getCommentaire());
			
			TaEtat etatLigne=taEtatService.documentEncours(null);
			codeDejaGenere=false;
			
			Map<String, String> paramsCode = new LinkedHashMap<String, String>();
			if((codeTiers==null || codeTiers.isEmpty()) 
					&& ((TaBonliv)dd).getTaTiers()!=null)
				codeTiers=((TaBonliv)dd).getTaTiers().getCodeTiers();
			paramsCode.put(Const.C_NOMFOURNISSEUR, codeTiers);

			paramsCode.put(Const.C_DATEDOCUMENT, LibDate.dateToString(((TaBonliv)dd).getDateDocument()));				
			if(isGenereCode()) {
				((TaBonliv)dd).setCodeDocument(taBonlivService.genereCode(paramsCode));
				codeDejaGenere=true;
			}
			

			for (ILigneDocumentTiers ligne : ((TaBonliv)ds).getLignes()) {
				TaLBonliv temp = (TaLBonliv)copieLigneDocument(ligne, new TaLBonliv()); 
				temp.setTaDocument(((TaBonliv)dd));
				((TaBonliv)dd).addLigne(temp);
			}

			TaInfosBonliv infos = (TaInfosBonliv)copieInfosDocument(((TaBonliv)ds).getTaInfosDocument(),new TaInfosBonliv());
			infos.setTaDocument(((TaBonliv)dd));
			((TaBonliv)dd).setTaInfosDocument(infos);

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
			if(ld.getTaArticle()!=null && ((TaLBonliv)ls).getCodeTitreTransport()!=null ) {
				((TaLBonliv)ld).setCodeTitreTransport(((TaLBonliv)ls).getCodeTitreTransport());
				((TaLBonliv)ld).setQteTitreTransport(((TaLBonliv)ls).getQteTitreTransport());			
			}
			else
				//sinon, si l'article attend de la crd, on la met dans doc dest
				if(ld.getTaArticle()!=null && ld.getTaArticle().getTaRTaTitreTransport()!=null) {
					((TaLBonliv)ld).setCodeTitreTransport(ls.getTaArticle().getTaRTaTitreTransport().getTaTitreTransport().getCodeTitreTransport());
					if(((TaLBonliv)ld).getTaArticle().getTaRTaTitreTransport().getQteTitreTransport()!=null) {
						((TaLBonliv)ld).setQteTitreTransport(((TaLBonliv)ld).getTaArticle().getTaRTaTitreTransport().getQteTitreTransport().multiply(((TaLBonliv)ld).getQteLDocument()));
					}
				}		
		return ld;
	}

	@Override
	public IDocumentTiers genereDocumentSpecifique(IDocumentTiers ds, IDocumentTiers dd,String codeTiers, boolean generationModele,boolean genereCode) {
		
		TaBonliv documentInit = ((TaBonliv)ds);
		try {
			if(documentInit!=null){
				documentInit.setLegrain(true);
				if(genereCode && !codeDejaGenere )((TaBonliv)dd).setCodeDocument(taBonlivService.genereCode(null));
				((TaBonliv)dd).setCommentaire(commentaire);
				taBonlivService.calculDateEcheanceAbstract(((TaBonliv)dd),taCPaiement.getReportCPaiement(),taCPaiement.getFinMoisCPaiement());
				((TaBonliv)dd).calculeTvaEtTotaux();	
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
