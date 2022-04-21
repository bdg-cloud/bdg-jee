package fr.legrain.generation.service;




import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import fr.legrain.bdg.documents.service.remote.ITaAcompteServiceRemote;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.model.TaAcompte;
import fr.legrain.document.model.TaBoncde;
import fr.legrain.document.model.TaDevis;
import fr.legrain.document.model.TaInfosAcompte;
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
public abstract  class AbstractGenereDocVersAcompte extends AbstractGenereDoc {
	
	private @EJB ITaAcompteServiceRemote taAcompteService;
	private LgrDozerMapper<InfosCPaiementDTO,TaInfosAcompte> mapperUIToModelCPaiementVersInfosDoc = new LgrDozerMapper<InfosCPaiementDTO, TaInfosAcompte>();
	private LgrDozerMapper<AdresseInfosFacturationDTO,TaInfosAcompte> mapperUIToModelAdresseFactVersInfosDoc = new LgrDozerMapper<AdresseInfosFacturationDTO, TaInfosAcompte>();
	private LgrDozerMapper<AdresseInfosLivraisonDTO,TaInfosAcompte> mapperUIToModelAdresseLivVersInfosDoc = new LgrDozerMapper<AdresseInfosLivraisonDTO, TaInfosAcompte>();
	private LgrDozerMapper<IdentiteTiersDTO,TaInfosAcompte> mapperUIToModelIHMIdentiteTiersVersInfosDoc = new LgrDozerMapper<IdentiteTiersDTO, TaInfosAcompte>();

	private LgrDozerMapper<TaAcompte,TaInfosAcompte> mapperUIToModelDocumentVersInfosDoc = new LgrDozerMapper<TaAcompte, TaInfosAcompte>();	
	
	@Override
	public IDocumentTiers enregistreNouveauDocumentSpecifique(IDocumentTiers dd) {
		try {
			if(dd!=null) {
				taAcompteService.inserer(((TaAcompte)dd));
				((TaAcompte)dd).setDateEchDocument(taAcompteService.calculDateEcheanceAbstract(((TaAcompte)dd),taCPaiement.getReportCPaiement(),taCPaiement.getFinMoisCPaiement()));
				((TaAcompte)dd).calculeTvaEtTotaux();	
				
				
//				if(!generationModele){
//					/* Si le ds à un règlement associé, alors on crée l'affectation de ce règlement dans la facture*/
//					if(ds.getTaRReglementLiaisons()!=null && ds.getTaRReglementLiaisons().size()>0) {
//						for (TaRReglementLiaison rr : ds.getTaRReglementLiaisons()) {
//							TaRReglementLiaison taRReglement = new TaRReglementLiaison();  /* A remettre dès que l'on travaillera sur reglement multiple il faudra passer par une relation TaRReglement*/
//							
//							taRReglement.setTaReglement(rr.getTaReglement());
//							taRReglement.setAffectation(rr.getAffectation());
//							taRReglement.setTaAcompte((TaAcompte)dd);
//							taRReglement.setEtat(IHMEtat.integre);
//							((TaAcompte)dd).addRReglementLiaison(taRReglement);
//						}
//					}
//				}
//				((TaAcompte)dd).calculRegleDocument();
//				((TaAcompte)dd).calculRegleDocumentComplet();
//				((TaAcompte)dd).setNetAPayer(((TaAcompte)dd).calculResteAReglerComplet());
				

				dd=taAcompteService.merge(((TaAcompte)dd));		
			}
		} catch (Exception e) {
			logger.error("",e);
		}
		return dd;
	}
	
	public void initInfosDocument(IDocumentTiers dd) {
		if(((TaAcompte)dd).getTaInfosDocument()==null) {
			((TaAcompte)dd).setTaInfosDocument(new TaInfosAcompte());
		}
	}

	@Override
	public void mapUIToModelAdresseFactVersInfosDoc(AdresseInfosFacturationDTO infos, IDocumentTiers dd) {
		initInfosDocument(dd);
		mapperUIToModelAdresseFactVersInfosDoc.map(infos, ((TaAcompte)dd).getTaInfosDocument());
	}

	@Override
	public void mapUIToModelAdresseLivVersInfosDoc(AdresseInfosLivraisonDTO infos,IDocumentTiers dd) {
		initInfosDocument(dd);
		mapperUIToModelAdresseLivVersInfosDoc.map(infos, ((TaAcompte)dd).getTaInfosDocument());
	}

	@Override
	public void mapUIToModelCPaiementVersInfosDoc(InfosCPaiementDTO infos,IDocumentTiers dd) {
		initInfosDocument(dd);
		mapperUIToModelCPaiementVersInfosDoc.map(infos, ((TaAcompte)dd).getTaInfosDocument());
	}

	@Override
	public void mapUIToModelDocumentVersInfosDoc(IDocumentTiers dd) {
		initInfosDocument(dd);
		mapperUIToModelDocumentVersInfosDoc.map(((TaAcompte)dd), ((TaAcompte)dd).getTaInfosDocument());
	}

	@Override
	public void mapUIToModelIHMIdentiteTiersVersInfosDoc(IdentiteTiersDTO infos,IDocumentTiers dd) {
		initInfosDocument(dd);
		mapperUIToModelIHMIdentiteTiersVersInfosDoc.map(infos, ((TaAcompte)dd).getTaInfosDocument());
	}
}
