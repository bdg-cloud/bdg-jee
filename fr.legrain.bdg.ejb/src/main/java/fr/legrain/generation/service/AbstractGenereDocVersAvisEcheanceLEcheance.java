package fr.legrain.generation.service;

import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import fr.legrain.bdg.documents.service.remote.ITaAvisEcheanceServiceRemote;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.model.TaAvisEcheance;
import fr.legrain.document.model.TaInfosAvisEcheance;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.tiers.dto.AdresseInfosFacturationDTO;
import fr.legrain.tiers.dto.AdresseInfosLivraisonDTO;
import fr.legrain.tiers.dto.IdentiteTiersDTO;
import fr.legrain.tiers.dto.InfosCPaiementDTO;


@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public abstract  class AbstractGenereDocVersAvisEcheanceLEcheance extends AbstractGenereDocLEcheance{
	
	private @EJB ITaAvisEcheanceServiceRemote taAvisEcheanceService;
	private LgrDozerMapper<InfosCPaiementDTO,TaInfosAvisEcheance> mapperUIToModelCPaiementVersInfosDoc = new LgrDozerMapper<InfosCPaiementDTO, TaInfosAvisEcheance>();
	private LgrDozerMapper<AdresseInfosFacturationDTO,TaInfosAvisEcheance> mapperUIToModelAdresseFactVersInfosDoc = new LgrDozerMapper<AdresseInfosFacturationDTO, TaInfosAvisEcheance>();
	private LgrDozerMapper<AdresseInfosLivraisonDTO,TaInfosAvisEcheance> mapperUIToModelAdresseLivVersInfosDoc = new LgrDozerMapper<AdresseInfosLivraisonDTO, TaInfosAvisEcheance>();
	private LgrDozerMapper<IdentiteTiersDTO,TaInfosAvisEcheance> mapperUIToModelIHMIdentiteTiersVersInfosDoc = new LgrDozerMapper<IdentiteTiersDTO, TaInfosAvisEcheance>();

	private LgrDozerMapper<TaAvisEcheance,TaInfosAvisEcheance> mapperUIToModelDocumentVersInfosDoc = new LgrDozerMapper<TaAvisEcheance, TaInfosAvisEcheance>();	
	
	@Override
	public IDocumentTiers enregistreNouveauDocumentSpecifique(IDocumentTiers dd) {
		try {
			if(dd!=null) {
				taAvisEcheanceService.inserer(((TaAvisEcheance)dd));
				((TaAvisEcheance)dd).setDateEchDocument(taAvisEcheanceService.calculDateEcheanceAbstract(((TaAvisEcheance)dd),taCPaiement.getReportCPaiement(),taCPaiement.getFinMoisCPaiement()));
				((TaAvisEcheance)dd).calculeTvaEtTotaux();	

				
				
				dd=taAvisEcheanceService.merge(((TaAvisEcheance)dd));
				// rajouter les lignesALigneEcheance
				
				
			}
		} catch (Exception e) {
			logger.error("",e);
		}
		return dd;
	}
	
	public void initInfosDocument(IDocumentTiers dd) {
		if(((TaAvisEcheance)dd).getTaInfosDocument()==null) {
			((TaAvisEcheance)dd).setTaInfosDocument(new TaInfosAvisEcheance());
		}
	}

	@Override
	public void mapUIToModelAdresseFactVersInfosDoc(AdresseInfosFacturationDTO infos, IDocumentTiers dd) {
		initInfosDocument(dd);
		mapperUIToModelAdresseFactVersInfosDoc.map(infos, ((TaAvisEcheance)dd).getTaInfosDocument());
	}

	@Override
	public void mapUIToModelAdresseLivVersInfosDoc(AdresseInfosLivraisonDTO infos,IDocumentTiers dd) {
		initInfosDocument(dd);
		mapperUIToModelAdresseLivVersInfosDoc.map(infos, ((TaAvisEcheance)dd).getTaInfosDocument());
	}

	@Override
	public void mapUIToModelCPaiementVersInfosDoc(InfosCPaiementDTO infos,IDocumentTiers dd) {
		initInfosDocument(dd);
		mapperUIToModelCPaiementVersInfosDoc.map(infos, ((TaAvisEcheance)dd).getTaInfosDocument());
	}

	@Override
	public void mapUIToModelDocumentVersInfosDoc(IDocumentTiers dd) {
		initInfosDocument(dd);
		mapperUIToModelDocumentVersInfosDoc.map(((TaAvisEcheance)dd), ((TaAvisEcheance)dd).getTaInfosDocument());
	}

	@Override
	public void mapUIToModelIHMIdentiteTiersVersInfosDoc(IdentiteTiersDTO infos,IDocumentTiers dd) {
		initInfosDocument(dd);
		mapperUIToModelIHMIdentiteTiersVersInfosDoc.map(infos, ((TaAvisEcheance)dd).getTaInfosDocument());
	}
}
