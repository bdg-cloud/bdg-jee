package fr.legrain.generation.service;


import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import fr.legrain.bdg.documents.service.remote.ITaDevisServiceRemote;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.model.TaBoncde;
import fr.legrain.document.model.TaDevis;
import fr.legrain.document.model.TaInfosDevis;
import fr.legrain.document.model.TaRReglementLiaison;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.lib.data.IHMEtat;
import fr.legrain.tiers.dto.AdresseInfosFacturationDTO;
import fr.legrain.tiers.dto.AdresseInfosLivraisonDTO;
import fr.legrain.tiers.dto.IdentiteTiersDTO;
import fr.legrain.tiers.dto.InfosCPaiementDTO;


@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public abstract  class AbstractGenereDocVersDevis extends AbstractGenereDoc{
	
	private @EJB ITaDevisServiceRemote taDevisService;
	
	private LgrDozerMapper<InfosCPaiementDTO,TaInfosDevis> mapperUIToModelCPaiementVersInfosDoc = new LgrDozerMapper<InfosCPaiementDTO, TaInfosDevis>();
	private LgrDozerMapper<AdresseInfosFacturationDTO,TaInfosDevis> mapperUIToModelAdresseFactVersInfosDoc = new LgrDozerMapper<AdresseInfosFacturationDTO, TaInfosDevis>();
	private LgrDozerMapper<AdresseInfosLivraisonDTO,TaInfosDevis> mapperUIToModelAdresseLivVersInfosDoc = new LgrDozerMapper<AdresseInfosLivraisonDTO, TaInfosDevis>();
	private LgrDozerMapper<IdentiteTiersDTO,TaInfosDevis> mapperUIToModelIHMIdentiteTiersVersInfosDoc = new LgrDozerMapper<IdentiteTiersDTO, TaInfosDevis>();
	
	private LgrDozerMapper<TaDevis,TaInfosDevis> mapperUIToModelDocumentVersInfosDoc = new LgrDozerMapper<TaDevis, TaInfosDevis>();	
	@Override
	public IDocumentTiers enregistreNouveauDocumentSpecifique(IDocumentTiers dd) {
		try {
			if(dd!=null) {
				taDevisService.inserer(((TaDevis)dd));
				((TaDevis)dd).setDateEchDocument(taDevisService.calculDateEcheanceAbstract(((TaDevis)dd),taCPaiement.getReportCPaiement(),taCPaiement.getFinMoisCPaiement()));
				((TaDevis)dd).calculeTvaEtTotaux();	

//				if(!generationModele){
//					/* Si le ds à un règlement associé, alors on crée l'affectation de ce règlement dans la facture*/
//					if(ds.getTaRReglementLiaisons()!=null && ds.getTaRReglementLiaisons().size()>0) {
//						for (TaRReglementLiaison rr : ds.getTaRReglementLiaisons()) {
//							TaRReglementLiaison taRReglement = new TaRReglementLiaison();  /* A remettre dès que l'on travaillera sur reglement multiple il faudra passer par une relation TaRReglement*/
//							
//							taRReglement.setTaReglement(rr.getTaReglement());
//							taRReglement.setAffectation(rr.getAffectation());
//							taRReglement.setTaDevis((TaDevis)dd);
//							taRReglement.setEtat(IHMEtat.integre);
//							((TaDevis)dd).addRReglementLiaison(taRReglement);
//						}
//					}
//				}
//				((TaDevis)dd).calculRegleDocument();
//				((TaDevis)dd).calculRegleDocumentComplet();
//				((TaDevis)dd).setNetAPayer(((TaDevis)dd).calculResteAReglerComplet());
				
				dd=taDevisService.merge(((TaDevis)dd));		
			}
		} catch (Exception e) {
			logger.error("",e);
		}
		return dd;
	}
	
	public void initInfosDocument(IDocumentTiers dd) {
		if(((TaDevis)dd).getTaInfosDocument()==null) {
			((TaDevis)dd).setTaInfosDocument(new TaInfosDevis());
		}
	}

	@Override
	public void mapUIToModelAdresseFactVersInfosDoc(AdresseInfosFacturationDTO infos, IDocumentTiers dd) {
		initInfosDocument(dd);
		mapperUIToModelAdresseFactVersInfosDoc.map(infos, ((TaDevis)dd).getTaInfosDocument());
	}

	@Override
	public void mapUIToModelAdresseLivVersInfosDoc(AdresseInfosLivraisonDTO infos,IDocumentTiers dd) {
		initInfosDocument(dd);
		mapperUIToModelAdresseLivVersInfosDoc.map(infos, ((TaDevis)dd).getTaInfosDocument());
	}

	@Override
	public void mapUIToModelCPaiementVersInfosDoc(InfosCPaiementDTO infos,IDocumentTiers dd) {
		initInfosDocument(dd);
		mapperUIToModelCPaiementVersInfosDoc.map(infos, ((TaDevis)dd).getTaInfosDocument());
	}
	@Override
	public void mapUIToModelIHMIdentiteTiersVersInfosDoc(IdentiteTiersDTO infos,IDocumentTiers dd) {
		initInfosDocument(dd);
		mapperUIToModelIHMIdentiteTiersVersInfosDoc.map(infos, ((TaDevis)dd).getTaInfosDocument());
	}
	@Override
	public void mapUIToModelDocumentVersInfosDoc(IDocumentTiers dd) {
		initInfosDocument(dd);
		mapperUIToModelDocumentVersInfosDoc.map(((TaDevis)dd), ((TaDevis)dd).getTaInfosDocument());
	}

}
