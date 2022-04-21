package fr.legrain.generation.service;


import java.math.BigDecimal;

import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import fr.legrain.bdg.documents.service.remote.ITaPreparationServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.model.TaInfosPreparation;
import fr.legrain.document.model.TaLPreparation;
import fr.legrain.document.model.TaPreparation;
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
public abstract  class AbstractGenereDocVersPreparation extends AbstractGenereDocFlash{
	private @EJB ITaPreparationServiceRemote taTaPreparationService;
	
	private LgrDozerMapper<InfosCPaiementDTO,TaInfosPreparation> mapperUIToModelCPaiementVersInfosDoc = new LgrDozerMapper<InfosCPaiementDTO, TaInfosPreparation>();
	private LgrDozerMapper<AdresseInfosFacturationDTO,TaInfosPreparation> mapperUIToModelAdresseFactVersInfosDoc = new LgrDozerMapper<AdresseInfosFacturationDTO, TaInfosPreparation>();
	private LgrDozerMapper<AdresseInfosLivraisonDTO,TaInfosPreparation> mapperUIToModelAdresseLivVersInfosDoc = new LgrDozerMapper<AdresseInfosLivraisonDTO, TaInfosPreparation>();
	private LgrDozerMapper<IdentiteTiersDTO,TaInfosPreparation> mapperUIToModelIHMIdentiteTiersVersInfosDoc = new LgrDozerMapper<IdentiteTiersDTO, TaInfosPreparation>();

	private LgrDozerMapper<TaPreparation,TaInfosPreparation> mapperUIToModelDocumentVersInfosDoc = new LgrDozerMapper<TaPreparation, TaInfosPreparation>();	
	
	@Override
	public IDocumentTiers enregistreNouveauDocumentSpecifique(IDocumentTiers dd) {
		try {
			if(dd!=null) {
				taTaPreparationService.inserer(((TaPreparation)dd));
//				((TaPreparation)dd).setDateEchDocument(taTaPreparationService.calculDateEcheanceAbstract(((TaPreparation)dd),taCPaiement.getReportCPaiement(),taCPaiement.getFinMoisCPaiement()));
				((TaPreparation)dd).calculeTvaEtTotaux();	
				if(generationModele|| forceMouvement)creationMouvement((TaPreparation)dd);

				dd=taTaPreparationService.merge(((TaPreparation)dd));		
			}
		} catch (Exception e) {
			logger.error("",e);
		}
		return dd;
	}
	
	public void initInfosDocument(IDocumentTiers dd) {
		if(((TaPreparation)dd).getTaInfosDocument()==null) {
			((TaPreparation)dd).setTaInfosDocument(new TaInfosPreparation());
		}
	}

	@Override
	public void mapUIToModelAdresseFactVersInfosDoc(AdresseInfosFacturationDTO infos, IDocumentTiers dd) {
		initInfosDocument(dd);
		mapperUIToModelAdresseFactVersInfosDoc.map(infos, ((TaPreparation)dd).getTaInfosDocument());
	}

	@Override
	public void mapUIToModelAdresseLivVersInfosDoc(AdresseInfosLivraisonDTO infos,IDocumentTiers dd) {
		initInfosDocument(dd);
		mapperUIToModelAdresseLivVersInfosDoc.map(infos, ((TaPreparation)dd).getTaInfosDocument());
	}

	@Override
	public void mapUIToModelCPaiementVersInfosDoc(InfosCPaiementDTO infos,IDocumentTiers dd) {
		initInfosDocument(dd);
		mapperUIToModelCPaiementVersInfosDoc.map(infos, ((TaPreparation)dd).getTaInfosDocument());
	}

	@Override
	public void mapUIToModelDocumentVersInfosDoc(IDocumentTiers dd) {
		initInfosDocument(dd);
		mapperUIToModelDocumentVersInfosDoc.map(((TaPreparation)dd), ((TaPreparation)dd).getTaInfosDocument());
	}

	@Override
	public void mapUIToModelIHMIdentiteTiersVersInfosDoc(IdentiteTiersDTO infos,IDocumentTiers dd) {
		initInfosDocument(dd);
		mapperUIToModelIHMIdentiteTiersVersInfosDoc.map(infos, ((TaPreparation)dd).getTaInfosDocument());
	}

	
	private IDocumentTiers creationMouvement(TaPreparation docGenere) {
		/*
		 * Gérer les mouvements corrrespondant aux lignes 
		 */
		//Création du groupe de mouvement
		TaTLigne typeLigneCommentaire;
		try {
			typeLigneCommentaire = taTLigneService.findByCode(Const.C_CODE_T_LIGNE_C);

		
		TaGrMouvStock grpMouvStock = new TaGrMouvStock();
//		if(docGenere.getTaGrMouvStock()!=null) {
//			grpMouvStock=docGenere.getTaGrMouvStock();
//		}
		grpMouvStock.setCodeGrMouvStock(docGenere.getCodeDocument());
		grpMouvStock.setDateGrMouvStock(docGenere.getDateDocument());
		grpMouvStock.setLibelleGrMouvStock(docGenere.getLibelleDocument()!=null?docGenere.getLibelleDocument():"Bon de livraison "+docGenere.getCodeDocument());
		grpMouvStock.setTaTypeMouvStock(taTypeMouvementService.findByCode("B")); // type mouvement TaPreparation
//		docGenere.setTaGrMouvStock(grpMouvStock);
//		grpMouvStock.setTaPreparation(docGenere);

		for (TaLPreparation l : docGenere.getLignes()) {
			if(!l.getTaTLigne().equals(typeLigneCommentaire)&& l.getTaArticle()!=null){
				l.setTaTLigne(taTLigneService.findByCode(Const.C_CODE_T_LIGNE_H));
//				if(l.getTaMouvementStock()!=null)
//					l.getTaMouvementStock().setTaGrMouvStock(docGenere.getTaGrMouvStock());
			}
		}
		
		//
		grpMouvStock.getTaMouvementStockes().clear();
		
		//Création des lignes de mouvement
		for (TaLPreparation ligne : docGenere.getLignes()) {
			if(!ligne.getTaTLigne().equals(typeLigneCommentaire)&& ligne.getTaArticle()!=null){
				//				if(ligne.getTaMouvementStock()==null){
				TaMouvementStock mouv = new TaMouvementStock();
				if(ligne.getTaMouvementStock()!=null) {
					mouv=ligne.getTaMouvementStock();
				}
				if(ligne.getLibLDocument()!=null) {
					mouv.setLibelleStock(ligne.getLibLDocument());
				} else if (ligne.getTaArticle().getLibellecArticle()!=null){
					mouv.setLibelleStock(ligne.getTaArticle().getLibellecArticle());
				} else {
					mouv.setLibelleStock("");
				}
				mouv.setDateStock(docGenere.getDateDocument());
				mouv.setEmplacement(ligne.getEmplacementLDocument());
				mouv.setTaEntrepot(ligne.getTaEntrepot());
				if(ligne.getTaLot()!=null){
					//					mouv.setNumLot(ligne.getTaLot().getNumLot());
					mouv.setTaLot(ligne.getTaLot());
				}
				if(ligne.getQteLDocument()!=null)mouv.setQte1Stock(ligne.getQteLDocument().multiply(BigDecimal.valueOf(-1)));
				if(ligne.getQte2LDocument()!=null)mouv.setQte2Stock(ligne.getQte2LDocument().multiply(BigDecimal.valueOf(-1)));
				mouv.setUn1Stock(ligne.getU1LDocument());
				mouv.setUn2Stock(ligne.getU2LDocument());
				mouv.setTaGrMouvStock(grpMouvStock);
				//				ligne.setTaLot(null);
				ligne.setTaMouvementStock(mouv);

				grpMouvStock.getTaMouvementStockes().add(mouv);
				//				}
			}
		}
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return docGenere;
	}
}
