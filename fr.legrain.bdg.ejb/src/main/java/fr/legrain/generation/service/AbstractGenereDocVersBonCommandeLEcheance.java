package fr.legrain.generation.service;


import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import fr.legrain.bdg.documents.service.remote.ITaBoncdeServiceRemote;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.model.TaBoncde;
import fr.legrain.document.model.TaDevis;
import fr.legrain.document.model.TaInfosBoncde;
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
public abstract  class AbstractGenereDocVersBonCommandeLEcheance extends AbstractGenereDocLEcheance{
	
	@EJB
	protected ITaBoncdeServiceRemote taBoncdeService;
	private LgrDozerMapper<InfosCPaiementDTO,TaInfosBoncde> mapperUIToModelCPaiementVersInfosDoc = new LgrDozerMapper<InfosCPaiementDTO, TaInfosBoncde>();
	private LgrDozerMapper<AdresseInfosFacturationDTO,TaInfosBoncde> mapperUIToModelAdresseFactVersInfosDoc = new LgrDozerMapper<AdresseInfosFacturationDTO, TaInfosBoncde>();
	private LgrDozerMapper<AdresseInfosLivraisonDTO,TaInfosBoncde> mapperUIToModelAdresseLivVersInfosDoc = new LgrDozerMapper<AdresseInfosLivraisonDTO, TaInfosBoncde>();
	private LgrDozerMapper<IdentiteTiersDTO,TaInfosBoncde> mapperUIToModelIHMIdentiteTiersVersInfosDoc = new LgrDozerMapper<IdentiteTiersDTO, TaInfosBoncde>();

	private LgrDozerMapper<TaBoncde,TaInfosBoncde> mapperUIToModelDocumentVersInfosDoc = new LgrDozerMapper<TaBoncde, TaInfosBoncde>();	
	
	@Override
	public IDocumentTiers enregistreNouveauDocumentSpecifique(IDocumentTiers dd) {
		try {
			if(dd!=null) {
				taBoncdeService.inserer(((TaBoncde)dd));
				((TaBoncde)dd).setDateEchDocument(taBoncdeService.calculDateEcheanceAbstract(((TaBoncde)dd),taCPaiement.getReportCPaiement(),taCPaiement.getFinMoisCPaiement()));
				((TaBoncde)dd).calculeTvaEtTotaux();	

				
//				if(!generationModele){
//					/* Si le ds à un règlement associé, alors on crée l'affectation de ce règlement dans la facture*/
//					if(ds.getTaRReglementLiaisons()!=null && ds.getTaRReglementLiaisons().size()>0) {
//						for (TaRReglementLiaison rr : ds.getTaRReglementLiaisons()) {
//							TaRReglementLiaison taRReglement = new TaRReglementLiaison();  /* A remettre dès que l'on travaillera sur reglement multiple il faudra passer par une relation TaRReglement*/
//							
//							taRReglement.setTaReglement(rr.getTaReglement());
//							taRReglement.setAffectation(rr.getAffectation());
//							taRReglement.setTaBoncde((TaBoncde)dd);
//							taRReglement.setEtat(IHMEtat.integre);
//							((TaBoncde)dd).addRReglementLiaison(taRReglement);
//						}
//					}
//				}
//				((TaBoncde)dd).calculRegleDocument();
//				((TaBoncde)dd).calculRegleDocumentComplet();
//				((TaBoncde)dd).setNetAPayer(((TaBoncde)dd).calculResteAReglerComplet());
				
				dd=taBoncdeService.merge(((TaBoncde)dd));		
			}
		} catch (Exception e) {
			logger.error("",e);
		}
		return dd;
	}
	
	public void initInfosDocument(IDocumentTiers dd) {
		if(((TaBoncde)dd).getTaInfosDocument()==null) {
			((TaBoncde)dd).setTaInfosDocument(new TaInfosBoncde());
		}
	}

	@Override
	public void mapUIToModelAdresseFactVersInfosDoc(AdresseInfosFacturationDTO infos, IDocumentTiers dd) {
		initInfosDocument(dd);
		mapperUIToModelAdresseFactVersInfosDoc.map(infos, ((TaBoncde)dd).getTaInfosDocument());
	}

	@Override
	public void mapUIToModelAdresseLivVersInfosDoc(AdresseInfosLivraisonDTO infos,IDocumentTiers dd) {
		initInfosDocument(dd);
		mapperUIToModelAdresseLivVersInfosDoc.map(infos, ((TaBoncde)dd).getTaInfosDocument());
	}

	@Override
	public void mapUIToModelCPaiementVersInfosDoc(InfosCPaiementDTO infos,IDocumentTiers dd) {
		initInfosDocument(dd);
		mapperUIToModelCPaiementVersInfosDoc.map(infos, ((TaBoncde)dd).getTaInfosDocument());
	}

	@Override
	public void mapUIToModelDocumentVersInfosDoc(IDocumentTiers dd) {
		initInfosDocument(dd);
		mapperUIToModelDocumentVersInfosDoc.map(((TaBoncde)dd), ((TaBoncde)dd).getTaInfosDocument());
	}

	@Override
	public void mapUIToModelIHMIdentiteTiersVersInfosDoc(IdentiteTiersDTO infos,IDocumentTiers dd) {
		initInfosDocument(dd);
		mapperUIToModelIHMIdentiteTiersVersInfosDoc.map(infos, ((TaBoncde)dd).getTaInfosDocument());
	}
}
