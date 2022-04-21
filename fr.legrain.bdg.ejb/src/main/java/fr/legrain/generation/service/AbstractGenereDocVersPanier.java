package fr.legrain.generation.service;


import java.math.BigDecimal;

import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import fr.legrain.bdg.documents.service.remote.ITaPanierServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.model.TaInfosPanier;
import fr.legrain.document.model.TaLPanier;
import fr.legrain.document.model.TaPanier;
import fr.legrain.document.model.TaTLigne;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.stock.model.TaGrMouvStock;
import fr.legrain.stock.model.TaMouvementStock;
import fr.legrain.tiers.dto.AdresseInfosFacturationDTO;
import fr.legrain.tiers.dto.AdresseInfosLivraisonDTO;
import fr.legrain.tiers.dto.IdentiteTiersDTO;
import fr.legrain.tiers.dto.InfosCPaiementDTO;


@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public abstract  class AbstractGenereDocVersPanier extends AbstractGenereDoc{
	private @EJB ITaPanierServiceRemote taTaPanierService;
	
	private LgrDozerMapper<InfosCPaiementDTO,TaInfosPanier> mapperUIToModelCPaiementVersInfosDoc = new LgrDozerMapper<InfosCPaiementDTO, TaInfosPanier>();
	private LgrDozerMapper<AdresseInfosFacturationDTO,TaInfosPanier> mapperUIToModelAdresseFactVersInfosDoc = new LgrDozerMapper<AdresseInfosFacturationDTO, TaInfosPanier>();
	private LgrDozerMapper<AdresseInfosLivraisonDTO,TaInfosPanier> mapperUIToModelAdresseLivVersInfosDoc = new LgrDozerMapper<AdresseInfosLivraisonDTO, TaInfosPanier>();
	private LgrDozerMapper<IdentiteTiersDTO,TaInfosPanier> mapperUIToModelIHMIdentiteTiersVersInfosDoc = new LgrDozerMapper<IdentiteTiersDTO, TaInfosPanier>();

	private LgrDozerMapper<TaPanier,TaInfosPanier> mapperUIToModelDocumentVersInfosDoc = new LgrDozerMapper<TaPanier, TaInfosPanier>();	
	
	@Override
	public IDocumentTiers enregistreNouveauDocumentSpecifique(IDocumentTiers dd) {
		try {
			if(dd!=null) {
				taTaPanierService.inserer(((TaPanier)dd));
//				((TaPanier)dd).setDateEchDocument(taTaPanierService.calculDateEcheanceAbstract(((TaPanier)dd),taCPaiement.getReportCPaiement(),taCPaiement.getFinMoisCPaiement()));
				((TaPanier)dd).calculeTvaEtTotaux();	
//				if(generationModele|| forceMouvement)creationMouvement((TaPanier)dd);

				dd=taTaPanierService.merge(((TaPanier)dd));		
			}
		} catch (Exception e) {
			logger.error("",e);
		}
		return dd;
	}
	
	public void initInfosDocument(IDocumentTiers dd) {
		if(((TaPanier)dd).getTaInfosDocument()==null) {
			((TaPanier)dd).setTaInfosDocument(new TaInfosPanier());
		}
	}

	@Override
	public void mapUIToModelAdresseFactVersInfosDoc(AdresseInfosFacturationDTO infos, IDocumentTiers dd) {
		initInfosDocument(dd);
		mapperUIToModelAdresseFactVersInfosDoc.map(infos, ((TaPanier)dd).getTaInfosDocument());
	}

	@Override
	public void mapUIToModelAdresseLivVersInfosDoc(AdresseInfosLivraisonDTO infos,IDocumentTiers dd) {
		initInfosDocument(dd);
		mapperUIToModelAdresseLivVersInfosDoc.map(infos, ((TaPanier)dd).getTaInfosDocument());
	}

	@Override
	public void mapUIToModelCPaiementVersInfosDoc(InfosCPaiementDTO infos,IDocumentTiers dd) {
		initInfosDocument(dd);
		mapperUIToModelCPaiementVersInfosDoc.map(infos, ((TaPanier)dd).getTaInfosDocument());
	}

	@Override
	public void mapUIToModelDocumentVersInfosDoc(IDocumentTiers dd) {
		initInfosDocument(dd);
		mapperUIToModelDocumentVersInfosDoc.map(((TaPanier)dd), ((TaPanier)dd).getTaInfosDocument());
	}

	@Override
	public void mapUIToModelIHMIdentiteTiersVersInfosDoc(IdentiteTiersDTO infos,IDocumentTiers dd) {
		initInfosDocument(dd);
		mapperUIToModelIHMIdentiteTiersVersInfosDoc.map(infos, ((TaPanier)dd).getTaInfosDocument());
	}

	
	private IDocumentTiers creationMouvement(TaPanier docGenere) {
//		/*
//		 * Gérer les mouvements corrrespondant aux lignes 
//		 */
//		//Création du groupe de mouvement
//		TaTLigne typeLigneCommentaire;
//		try {
//			typeLigneCommentaire = taTLigneService.findByCode(Const.C_CODE_T_LIGNE_C);
//
//		
//		TaGrMouvStock grpMouvStock = new TaGrMouvStock();
////		if(docGenere.getTaGrMouvStock()!=null) {
////			grpMouvStock=docGenere.getTaGrMouvStock();
////		}
//		grpMouvStock.setCodeGrMouvStock(docGenere.getCodeDocument());
//		grpMouvStock.setDateGrMouvStock(docGenere.getDateDocument());
//		grpMouvStock.setLibelleGrMouvStock(docGenere.getLibelleDocument()!=null?docGenere.getLibelleDocument():"Panier "+docGenere.getCodeDocument());
//		grpMouvStock.setTaTypeMouvStock(taTypeMouvementService.findByCode("B")); // type mouvement TaPanier
////		docGenere.setTaGrMouvStock(grpMouvStock);
////		grpMouvStock.setTaPanier(docGenere);
//
//		for (TaLPanier l : docGenere.getLignes()) {
//			if(!l.getTaTLigne().equals(typeLigneCommentaire)&& l.getTaArticle()!=null){
//				l.setTaTLigne(taTLigneService.findByCode(Const.C_CODE_T_LIGNE_H));
////				if(l.getTaMouvementStock()!=null)
////					l.getTaMouvementStock().setTaGrMouvStock(docGenere.getTaGrMouvStock());
//			}
//		}
//		
//		//
//		grpMouvStock.getTaMouvementStockes().clear();
//		
//		//Création des lignes de mouvement
//		for (TaLPanier ligne : docGenere.getLignes()) {
//			if(!ligne.getTaTLigne().equals(typeLigneCommentaire)&& ligne.getTaArticle()!=null){
//				//				if(ligne.getTaMouvementStock()==null){
//				TaMouvementStock mouv = new TaMouvementStock();
//				if(ligne.getTaMouvementStock()!=null) {
//					mouv=ligne.getTaMouvementStock();
//				}
//				if(ligne.getLibLDocument()!=null) {
//					mouv.setLibelleStock(ligne.getLibLDocument());
//				} else if (ligne.getTaArticle().getLibellecArticle()!=null){
//					mouv.setLibelleStock(ligne.getTaArticle().getLibellecArticle());
//				} else {
//					mouv.setLibelleStock("");
//				}
//				mouv.setDateStock(docGenere.getDateDocument());
//				mouv.setEmplacement(ligne.getEmplacementLDocument());
//				mouv.setTaEntrepot(ligne.getTaEntrepot());
//				if(ligne.getTaLot()!=null){
//					//					mouv.setNumLot(ligne.getTaLot().getNumLot());
//					mouv.setTaLot(ligne.getTaLot());
//				}
//				if(ligne.getQteLDocument()!=null)mouv.setQte1Stock(ligne.getQteLDocument().multiply(BigDecimal.valueOf(-1)));
//				if(ligne.getQte2LDocument()!=null)mouv.setQte2Stock(ligne.getQte2LDocument().multiply(BigDecimal.valueOf(-1)));
//				mouv.setUn1Stock(ligne.getU1LDocument());
//				mouv.setUn2Stock(ligne.getU2LDocument());
//				mouv.setTaGrMouvStock(grpMouvStock);
//				//				ligne.setTaLot(null);
//				ligne.setTaMouvementStock(mouv);
//
//				grpMouvStock.getTaMouvementStockes().add(mouv);
//				//				}
//			}
//		}
//		} catch (FinderException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return docGenere;
	}
}
