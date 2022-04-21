package fr.legrain.generation.service;


import java.math.BigDecimal;
import java.util.Set;

import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripePaymentIntentServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaFactureServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaRReglementServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaInfosFacture;
import fr.legrain.document.model.TaLFacture;
import fr.legrain.document.model.TaRReglement;
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
public abstract  class AbstractGenereDocVersFacture extends AbstractGenereDoc {
	
	protected @EJB ITaFactureServiceRemote taFactureService;
	protected @EJB ITaRReglementServiceRemote taRReglementService;
//	private @EJB  ITaTypeMouvementServiceRemote taTypeMouvementService;
//	private @EJB ITaTLigneServiceRemote taTLigneService;
	
	protected @EJB ITaStripePaymentIntentServiceRemote taStripePaymentIntentService;
	
	private LgrDozerMapper<InfosCPaiementDTO,TaInfosFacture> mapperUIToModelCPaiementVersInfosDoc = new LgrDozerMapper<InfosCPaiementDTO, TaInfosFacture>();
	private LgrDozerMapper<AdresseInfosFacturationDTO,TaInfosFacture> mapperUIToModelAdresseFactVersInfosDoc = new LgrDozerMapper<AdresseInfosFacturationDTO, TaInfosFacture>();
	private LgrDozerMapper<AdresseInfosLivraisonDTO,TaInfosFacture> mapperUIToModelAdresseLivVersInfosDoc = new LgrDozerMapper<AdresseInfosLivraisonDTO, TaInfosFacture>();
	private LgrDozerMapper<IdentiteTiersDTO,TaInfosFacture> mapperUIToModelIHMIdentiteTiersVersInfosDoc = new LgrDozerMapper<IdentiteTiersDTO, TaInfosFacture>();

	private LgrDozerMapper<TaFacture,TaInfosFacture> mapperUIToModelDocumentVersInfosDoc = new LgrDozerMapper<TaFacture, TaInfosFacture>();
	
	
	
//	public AbstractGenereDocVersFacture(ITaFactureServiceRemote taFactureService) {
//		super();
//		this.taFactureService = taFactureService;
//	}

	@Override
	public IDocumentTiers enregistreNouveauDocumentSpecifique(IDocumentTiers dd) {
		try {
			if(dd!=null) {
				Set<TaRReglement> rr=((TaFacture) dd).getTaRReglements();
				taFactureService.inserer(((TaFacture)dd));		
				((TaFacture)dd).setDateEchDocument(taFactureService.calculDateEcheanceAbstract(((TaFacture)dd),taCPaiement.getReportCPaiement(),taCPaiement.getFinMoisCPaiement()));
				((TaFacture)dd).calculeTvaEtTotaux();	
				if(generationModele || forceMouvement)creationMouvement((TaFacture)dd);
				
				if(capturerReglement) {
//					*
				}
				

//				if(!generationModele){
//					if(ds.getTaRReglementLiaisons()!=null && ds.getTaRReglementLiaisons().size()>0) {
//						for (TaRReglementLiaison rrl : ds.getTaRReglementLiaisons()) {
//							TaRReglement taRReglement = new TaRReglement();  
//							
//							taRReglement.setTaReglement(rrl.getTaReglement());
//							taRReglement.setAffectation(rrl.getAffectation());
//							taRReglement.setTaFacture((TaFacture)dd);
//							taRReglement.setEtat(IHMEtat.integre);
//							((TaFacture)dd).addRReglement(taRReglement);
//						}
//					}
//				}
//				((TaFacture)dd).calculRegleDocument();
//				((TaFacture)dd).calculRegleDocumentComplet();
//				((TaFacture)dd).setNetAPayer(((TaFacture)dd).calculResteAReglerComplet());	
				
				dd=taFactureService.merge(((TaFacture)dd));

				//si rreglement rempli, on doit l'enregistrer manuellement
				if(rr!=null && !rr.isEmpty()){
					for (TaRReglement taRReglement : rr) {
						taRReglement.setTaFacture((TaFacture)dd);
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
		if(((TaFacture)dd).getTaInfosDocument()==null) {
			((TaFacture)dd).setTaInfosDocument(new TaInfosFacture());
		}
	}

	@Override
	public void mapUIToModelAdresseFactVersInfosDoc(AdresseInfosFacturationDTO infos, IDocumentTiers dd) {
		initInfosDocument(dd);
		mapperUIToModelAdresseFactVersInfosDoc.map(infos, ((TaFacture)dd).getTaInfosDocument());
	}

	@Override
	public void mapUIToModelAdresseLivVersInfosDoc(AdresseInfosLivraisonDTO infos,IDocumentTiers dd) {
		initInfosDocument(dd);
		mapperUIToModelAdresseLivVersInfosDoc.map(infos, ((TaFacture)dd).getTaInfosDocument());
	}

	@Override
	public void mapUIToModelCPaiementVersInfosDoc(InfosCPaiementDTO infos,IDocumentTiers dd) {
		initInfosDocument(dd);
		mapperUIToModelCPaiementVersInfosDoc.map(infos, ((TaFacture)dd).getTaInfosDocument());
	}

	@Override
	public void mapUIToModelDocumentVersInfosDoc(IDocumentTiers dd) {
		initInfosDocument(dd);
		mapperUIToModelDocumentVersInfosDoc.map(((TaFacture)dd), ((TaFacture)dd).getTaInfosDocument());
	}

	@Override
	public void mapUIToModelIHMIdentiteTiersVersInfosDoc(IdentiteTiersDTO infos,IDocumentTiers dd) {
		initInfosDocument(dd);
		mapperUIToModelIHMIdentiteTiersVersInfosDoc.map(infos, ((TaFacture)dd).getTaInfosDocument());
	}


	private IDocumentTiers creationMouvement(TaFacture docGenere) {
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
		grpMouvStock.setLibelleGrMouvStock(docGenere.getLibelleDocument()!=null?docGenere.getLibelleDocument():"Facture "+docGenere.getCodeDocument());
		grpMouvStock.setTaTypeMouvStock(taTypeMouvementService.findByCode("T")); // type mouvement Facture
		docGenere.setTaGrMouvStock(grpMouvStock);
		grpMouvStock.setTaFacture(docGenere);

		for (TaLFacture l : docGenere.getLignes()) {
			if(!l.getTaTLigne().equals(typeLigneCommentaire)&& l.getTaArticle()!=null){
				l.setTaTLigne(taTLigneService.findByCode(Const.C_CODE_T_LIGNE_H));
				if(l.getTaMouvementStock()!=null)
					l.getTaMouvementStock().setTaGrMouvStock(docGenere.getTaGrMouvStock());
			}
		}
		
		//
		grpMouvStock.getTaMouvementStockes().clear();
		
		//Création des lignes de mouvement
		for (TaLFacture ligne : docGenere.getLignes()) {
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
