package fr.legrain.generation.service;


import java.math.BigDecimal;
import java.util.Set;

import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import fr.legrain.bdg.documents.service.remote.ITaRReglementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaTicketDeCaisseServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.model.TaInfosTicketDeCaisse;
import fr.legrain.document.model.TaLTicketDeCaisse;
import fr.legrain.document.model.TaRReglement;
import fr.legrain.document.model.TaTLigne;
import fr.legrain.document.model.TaTicketDeCaisse;
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
public abstract  class AbstractGenereDocVersTicketDeCaisse extends AbstractGenereDoc {
	
	protected @EJB ITaTicketDeCaisseServiceRemote taTicketDeCaisseService;
	protected @EJB ITaRReglementServiceRemote taRReglementService;
//	private @EJB  ITaTypeMouvementServiceRemote taTypeMouvementService;
//	private @EJB ITaTLigneServiceRemote taTLigneService;
	
	private LgrDozerMapper<InfosCPaiementDTO,TaInfosTicketDeCaisse> mapperUIToModelCPaiementVersInfosDoc = new LgrDozerMapper<InfosCPaiementDTO, TaInfosTicketDeCaisse>();
	private LgrDozerMapper<AdresseInfosFacturationDTO,TaInfosTicketDeCaisse> mapperUIToModelAdresseFactVersInfosDoc = new LgrDozerMapper<AdresseInfosFacturationDTO, TaInfosTicketDeCaisse>();
	private LgrDozerMapper<AdresseInfosLivraisonDTO,TaInfosTicketDeCaisse> mapperUIToModelAdresseLivVersInfosDoc = new LgrDozerMapper<AdresseInfosLivraisonDTO, TaInfosTicketDeCaisse>();
	private LgrDozerMapper<IdentiteTiersDTO,TaInfosTicketDeCaisse> mapperUIToModelIHMIdentiteTiersVersInfosDoc = new LgrDozerMapper<IdentiteTiersDTO, TaInfosTicketDeCaisse>();

	private LgrDozerMapper<TaTicketDeCaisse,TaInfosTicketDeCaisse> mapperUIToModelDocumentVersInfosDoc = new LgrDozerMapper<TaTicketDeCaisse, TaInfosTicketDeCaisse>();
	
	
	
//	public AbstractGenereDocVersTicketDeCaisse(ITaTicketDeCaisseServiceRemote taTicketDeCaisseService) {
//		super();
//		this.taTicketDeCaisseService = taTicketDeCaisseService;
//	}

	@Override
	public IDocumentTiers enregistreNouveauDocumentSpecifique(IDocumentTiers dd) {
		try {
			if(dd!=null) {
				Set<TaRReglement> rr=((TaTicketDeCaisse) dd).getTaRReglements();
				taTicketDeCaisseService.inserer(((TaTicketDeCaisse)dd));		
//				((TaTicketDeCaisse)dd).setDateEchDocument(taTicketDeCaisseService.calculDateEcheanceAbstract(((TaTicketDeCaisse)dd),taCPaiement.getReportCPaiement(),taCPaiement.getFinMoisCPaiement()));
				((TaTicketDeCaisse)dd).calculeTvaEtTotaux();	
				if(generationModele || forceMouvement)creationMouvement((TaTicketDeCaisse)dd);
				dd=taTicketDeCaisseService.merge(((TaTicketDeCaisse)dd));

				//si rreglement rempli, on doit l'enregistrer manuellement
				if(rr!=null && !rr.isEmpty()){
					for (TaRReglement taRReglement : rr) {
						taRReglement.setTaTicketDeCaisse((TaTicketDeCaisse)dd);
						taRReglementService.enregistrerMerge(taRReglement);
					}

				}
			}
		} catch (Exception e) {
			logger.error("",e);
		}
		return dd;
	}
	
	public void initInfosDocument(IDocumentTiers dd) {
		if(((TaTicketDeCaisse)dd).getTaInfosDocument()==null) {
			((TaTicketDeCaisse)dd).setTaInfosDocument(new TaInfosTicketDeCaisse());
		}
	}

	@Override
	public void mapUIToModelAdresseFactVersInfosDoc(AdresseInfosFacturationDTO infos, IDocumentTiers dd) {
		initInfosDocument(dd);
		mapperUIToModelAdresseFactVersInfosDoc.map(infos, ((TaTicketDeCaisse)dd).getTaInfosDocument());
	}

	@Override
	public void mapUIToModelAdresseLivVersInfosDoc(AdresseInfosLivraisonDTO infos,IDocumentTiers dd) {
		initInfosDocument(dd);
		mapperUIToModelAdresseLivVersInfosDoc.map(infos, ((TaTicketDeCaisse)dd).getTaInfosDocument());
	}

	@Override
	public void mapUIToModelCPaiementVersInfosDoc(InfosCPaiementDTO infos,IDocumentTiers dd) {
		initInfosDocument(dd);
		mapperUIToModelCPaiementVersInfosDoc.map(infos, ((TaTicketDeCaisse)dd).getTaInfosDocument());
	}

	@Override
	public void mapUIToModelDocumentVersInfosDoc(IDocumentTiers dd) {
		initInfosDocument(dd);
		mapperUIToModelDocumentVersInfosDoc.map(((TaTicketDeCaisse)dd), ((TaTicketDeCaisse)dd).getTaInfosDocument());
	}

	@Override
	public void mapUIToModelIHMIdentiteTiersVersInfosDoc(IdentiteTiersDTO infos,IDocumentTiers dd) {
		initInfosDocument(dd);
		mapperUIToModelIHMIdentiteTiersVersInfosDoc.map(infos, ((TaTicketDeCaisse)dd).getTaInfosDocument());
	}


	private IDocumentTiers creationMouvement(TaTicketDeCaisse docGenere) {
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
		grpMouvStock.setLibelleGrMouvStock(docGenere.getLibelleDocument()!=null?docGenere.getLibelleDocument():"TicketDeCaisse "+docGenere.getCodeDocument());
		grpMouvStock.setTaTypeMouvStock(taTypeMouvementService.findByCode("C")); // type mouvement TicketDeCaisse
		docGenere.setTaGrMouvStock(grpMouvStock);
		grpMouvStock.setTaTicketDeCaisse(docGenere);

		for (TaLTicketDeCaisse l : docGenere.getLignes()) {
			if(!l.getTaTLigne().equals(typeLigneCommentaire)&& l.getTaArticle()!=null){
				l.setTaTLigne(taTLigneService.findByCode(Const.C_CODE_T_LIGNE_H));
				if(l.getTaMouvementStock()!=null)
					l.getTaMouvementStock().setTaGrMouvStock(docGenere.getTaGrMouvStock());
			}
		}
		
		//
		grpMouvStock.getTaMouvementStockes().clear();
		
		//Création des lignes de mouvement
		for (TaLTicketDeCaisse ligne : docGenere.getLignes()) {
			if(!ligne.getTaTLigne().equals(typeLigneCommentaire)&&ligne.getTaArticle()!=null){
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
