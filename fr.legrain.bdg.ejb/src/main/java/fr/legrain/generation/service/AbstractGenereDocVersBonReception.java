package fr.legrain.generation.service;


import java.math.BigDecimal;

import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import fr.legrain.bdg.documents.service.remote.ITaBonReceptionServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.model.TaBonReception;
import fr.legrain.document.model.TaInfosBonReception;
import fr.legrain.document.model.TaLBonReception;
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
public abstract  class AbstractGenereDocVersBonReception extends AbstractGenereDoc{
	@EJB
	protected ITaBonReceptionServiceRemote taBonReceptionService;
	
	private LgrDozerMapper<InfosCPaiementDTO,TaInfosBonReception> mapperUIToModelCPaiementVersInfosDoc = new LgrDozerMapper<InfosCPaiementDTO, TaInfosBonReception>();
	private LgrDozerMapper<AdresseInfosFacturationDTO,TaInfosBonReception> mapperUIToModelAdresseFactVersInfosDoc = new LgrDozerMapper<AdresseInfosFacturationDTO, TaInfosBonReception>();
	private LgrDozerMapper<AdresseInfosLivraisonDTO,TaInfosBonReception> mapperUIToModelAdresseLivVersInfosDoc = new LgrDozerMapper<AdresseInfosLivraisonDTO, TaInfosBonReception>();
	private LgrDozerMapper<IdentiteTiersDTO,TaInfosBonReception> mapperUIToModelIHMIdentiteTiersVersInfosDoc = new LgrDozerMapper<IdentiteTiersDTO, TaInfosBonReception>();

	private LgrDozerMapper<TaBonReception,TaInfosBonReception> mapperUIToModelDocumentVersInfosDoc = new LgrDozerMapper<TaBonReception, TaInfosBonReception>();	
	
	@Override
	public IDocumentTiers enregistreNouveauDocumentSpecifique(IDocumentTiers dd) {
		try {
			if(dd!=null) {
				taBonReceptionService.inserer(((TaBonReception)dd));
				taBonReceptionService.calculDateEcheanceAbstract(((TaBonReception)dd),taCPaiement.getReportCPaiement(),taCPaiement.getFinMoisCPaiement());
				((TaBonReception)dd).calculeTvaEtTotaux();	
//				if(generationModele|| forceMouvement)
					creationMouvement((TaBonReception)dd);

				dd=taBonReceptionService.merge(((TaBonReception)dd));		
			}
		} catch (Exception e) {
			logger.error("",e);
		}
		return dd;
	}
	
	public void initInfosDocument(IDocumentTiers dd) {
		if(((TaBonReception)dd).getTaInfosDocument()==null) {
			((TaBonReception)dd).setTaInfosDocument(new TaInfosBonReception());
		}
	}

	@Override
	public void mapUIToModelAdresseFactVersInfosDoc(AdresseInfosFacturationDTO infos, IDocumentTiers dd) {
		initInfosDocument(dd);
		mapperUIToModelAdresseFactVersInfosDoc.map(infos, ((TaBonReception)dd).getTaInfosDocument());
	}

	@Override
	public void mapUIToModelAdresseLivVersInfosDoc(AdresseInfosLivraisonDTO infos,IDocumentTiers dd) {
		initInfosDocument(dd);
		mapperUIToModelAdresseLivVersInfosDoc.map(infos, ((TaBonReception)dd).getTaInfosDocument());
	}

	@Override
	public void mapUIToModelCPaiementVersInfosDoc(InfosCPaiementDTO infos,IDocumentTiers dd) {
		initInfosDocument(dd);
		mapperUIToModelCPaiementVersInfosDoc.map(infos, ((TaBonReception)dd).getTaInfosDocument());
	}

	@Override
	public void mapUIToModelDocumentVersInfosDoc(IDocumentTiers dd) {
		initInfosDocument(dd);
		mapperUIToModelDocumentVersInfosDoc.map(((TaBonReception)dd), ((TaBonReception)dd).getTaInfosDocument());
	}

	@Override
	public void mapUIToModelIHMIdentiteTiersVersInfosDoc(IdentiteTiersDTO infos,IDocumentTiers dd) {
		initInfosDocument(dd);
		mapperUIToModelIHMIdentiteTiersVersInfosDoc.map(infos, ((TaBonReception)dd).getTaInfosDocument());
	}

	
	private IDocumentTiers creationMouvement(TaBonReception docGenere) {
		/*
		 * Gérer les mouvements corrrespondant aux lignes 
		 */
		//Création du groupe de mouvement
		TaTLigne typeLigneCommentaire;
		try {
			typeLigneCommentaire = taTLigneService.findByCode(Const.C_CODE_T_LIGNE_C);

		
		TaGrMouvStock grpMouvStock = new TaGrMouvStock();
		if(docGenere.getTaGrMouvStock()!=null) {
			grpMouvStock=docGenere.getTaGrMouvStock();
		}
		grpMouvStock.setCodeGrMouvStock(docGenere.getCodeDocument());
		grpMouvStock.setDateGrMouvStock(docGenere.getDateDocument());
		grpMouvStock.setLibelleGrMouvStock(docGenere.getLibelleDocument()!=null?docGenere.getLibelleDocument():"Bon de Réception "+docGenere.getCodeDocument());
		grpMouvStock.setTaTypeMouvStock(taTypeMouvementService.findByCode("R")); // type mouvement BonReception
		docGenere.setTaGrMouvStock(grpMouvStock);
		grpMouvStock.setTaBonReception(docGenere);

		for (TaLBonReception l : docGenere.getLignes()) {
			if(!l.getTaTLigne().equals(typeLigneCommentaire)&& l.getTaArticle()!=null){
				l.setTaTLigne(taTLigneService.findByCode(Const.C_CODE_T_LIGNE_H));
				if(l.getTaMouvementStock()!=null)
					l.getTaMouvementStock().setTaGrMouvStock(docGenere.getTaGrMouvStock());
			}
		}
		
		//
		grpMouvStock.getTaMouvementStockes().clear();
		
		//Création des lignes de mouvement
		for (TaLBonReception ligne : docGenere.getLignes()) {
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
				if(ligne.getQteLDocument()!=null)mouv.setQte1Stock(ligne.getQteLDocument());
				if(ligne.getQte2LDocument()!=null)mouv.setQte2Stock(ligne.getQte2LDocument());
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
