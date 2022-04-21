package fr.legrain.generation.service;




import java.math.BigDecimal;

import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import fr.legrain.bdg.documents.service.remote.ITaAvoirServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.model.TaAvoir;
import fr.legrain.document.model.TaDevis;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaInfosAvoir;
import fr.legrain.document.model.TaLAvoir;
import fr.legrain.document.model.TaLFacture;
import fr.legrain.document.model.TaTLigne;
import fr.legrain.dossier.model.TaPreferences;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.stock.model.TaGrMouvStock;
import fr.legrain.stock.model.TaMouvementStock;
import fr.legrain.tiers.dto.AdresseInfosFacturationDTO;
import fr.legrain.tiers.dto.AdresseInfosLivraisonDTO;
import fr.legrain.tiers.dto.IdentiteTiersDTO;
import fr.legrain.tiers.dto.InfosCPaiementDTO;


@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public abstract  class AbstractGenereDocVersAvoir extends AbstractGenereDoc{
	
	private @EJB ITaAvoirServiceRemote taAvoirService;
//	private @EJB ITaPreferencesServiceRemote taPreferencesService;
	
	private LgrDozerMapper<InfosCPaiementDTO,TaInfosAvoir> mapperUIToModelCPaiementVersInfosDoc = new LgrDozerMapper<InfosCPaiementDTO, TaInfosAvoir>();
	private LgrDozerMapper<AdresseInfosFacturationDTO,TaInfosAvoir> mapperUIToModelAdresseFactVersInfosDoc = new LgrDozerMapper<AdresseInfosFacturationDTO, TaInfosAvoir>();
	private LgrDozerMapper<AdresseInfosLivraisonDTO,TaInfosAvoir> mapperUIToModelAdresseLivVersInfosDoc = new LgrDozerMapper<AdresseInfosLivraisonDTO, TaInfosAvoir>();
	private LgrDozerMapper<IdentiteTiersDTO,TaInfosAvoir> mapperUIToModelIHMIdentiteTiersVersInfosDoc = new LgrDozerMapper<IdentiteTiersDTO, TaInfosAvoir>();

	private LgrDozerMapper<TaAvoir,TaInfosAvoir> mapperUIToModelDocumentVersInfosDoc = new LgrDozerMapper<TaAvoir, TaInfosAvoir>();	
	
	@Override
	public IDocumentTiers enregistreNouveauDocumentSpecifique(IDocumentTiers dd) {
		try {
			if(dd!=null) {
				taAvoirService.inserer(((TaAvoir)dd));	
				((TaAvoir)dd).setDateEchDocument(taAvoirService.calculDateEcheanceAbstract(((TaAvoir)dd),taCPaiement.getReportCPaiement(),taCPaiement.getFinMoisCPaiement()));
				((TaAvoir)dd).calculeTvaEtTotaux();	
				if(generationModele || forceMouvement)creationMouvement((TaAvoir)dd);
				((TaAvoir)dd).setMouvementerCRD(documentGereLesCrd);
				dd=taAvoirService.enregistrerMerge(((TaAvoir)dd));		
			}
		} catch (Exception e) {
			logger.error("",e);
		}
		return dd;
	}
	
	
	private IDocumentTiers creationMouvement(TaAvoir docGenere) {
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
		grpMouvStock.setLibelleGrMouvStock(docGenere.getLibelleDocument()!=null?docGenere.getLibelleDocument():"Avoir "+docGenere.getCodeDocument());
		grpMouvStock.setTaTypeMouvStock(taTypeMouvementService.findByCode("A")); // type mouvement Facture
		docGenere.setTaGrMouvStock(grpMouvStock);
		grpMouvStock.setTaAvoir(docGenere);

		for (TaLAvoir l : docGenere.getLignes()) {
			if(!l.getTaTLigne().equals(typeLigneCommentaire)&& l.getTaArticle()!=null){
				l.setTaTLigne(taTLigneService.findByCode(Const.C_CODE_T_LIGNE_H));
				if(l.getTaMouvementStock()!=null)
					l.getTaMouvementStock().setTaGrMouvStock(docGenere.getTaGrMouvStock());
			}
		}
		
		//
		grpMouvStock.getTaMouvementStockes().clear();
		
		//Création des lignes de mouvement
		for (TaLAvoir ligne : docGenere.getLignes()) {
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

	
	public void initInfosDocument(IDocumentTiers dd) {
		if(((TaAvoir)dd).getTaInfosDocument()==null) {
			((TaAvoir)dd).setTaInfosDocument(new TaInfosAvoir());
		}
	}

	@Override
	public void mapUIToModelAdresseFactVersInfosDoc(AdresseInfosFacturationDTO infos, IDocumentTiers dd) {
		initInfosDocument(dd);
		mapperUIToModelAdresseFactVersInfosDoc.map(infos, ((TaAvoir)dd).getTaInfosDocument());
	}

	@Override
	public void mapUIToModelAdresseLivVersInfosDoc(AdresseInfosLivraisonDTO infos,IDocumentTiers dd) {
		initInfosDocument(dd);
		mapperUIToModelAdresseLivVersInfosDoc.map(infos, ((TaAvoir)dd).getTaInfosDocument());
	}

	@Override
	public void mapUIToModelCPaiementVersInfosDoc(InfosCPaiementDTO infos,IDocumentTiers dd) {
		initInfosDocument(dd);
		mapperUIToModelCPaiementVersInfosDoc.map(infos, ((TaAvoir)dd).getTaInfosDocument());
	}

	@Override
	public void mapUIToModelDocumentVersInfosDoc(IDocumentTiers dd) {
		initInfosDocument(dd);
		mapperUIToModelDocumentVersInfosDoc.map(((TaAvoir)dd), ((TaAvoir)dd).getTaInfosDocument());
	}

	@Override
	public void mapUIToModelIHMIdentiteTiersVersInfosDoc(IdentiteTiersDTO infos,IDocumentTiers dd) {
		initInfosDocument(dd);
		mapperUIToModelIHMIdentiteTiersVersInfosDoc.map(infos, ((TaAvoir)dd).getTaInfosDocument());
	}
}
