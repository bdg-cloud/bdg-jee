package fr.legrain.generation.service;


import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import fr.legrain.bdg.documents.service.remote.ITaBoncdeAchatServiceRemote;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.model.TaBoncdeAchat;
import fr.legrain.document.model.TaInfosBoncdeAchat;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.tiers.dto.AdresseInfosFacturationDTO;
import fr.legrain.tiers.dto.AdresseInfosLivraisonDTO;
import fr.legrain.tiers.dto.IdentiteTiersDTO;
import fr.legrain.tiers.dto.InfosCPaiementDTO;


@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public abstract  class AbstractGenereDocVersBonCommandeAchat extends AbstractGenereDoc{
	
	private @EJB ITaBoncdeAchatServiceRemote taBoncdeAchatService;
	private LgrDozerMapper<InfosCPaiementDTO,TaInfosBoncdeAchat> mapperUIToModelCPaiementVersInfosDoc = new LgrDozerMapper<InfosCPaiementDTO, TaInfosBoncdeAchat>();
	private LgrDozerMapper<AdresseInfosFacturationDTO,TaInfosBoncdeAchat> mapperUIToModelAdresseFactVersInfosDoc = new LgrDozerMapper<AdresseInfosFacturationDTO, TaInfosBoncdeAchat>();
	private LgrDozerMapper<AdresseInfosLivraisonDTO,TaInfosBoncdeAchat> mapperUIToModelAdresseLivVersInfosDoc = new LgrDozerMapper<AdresseInfosLivraisonDTO, TaInfosBoncdeAchat>();
	private LgrDozerMapper<IdentiteTiersDTO,TaInfosBoncdeAchat> mapperUIToModelIHMIdentiteTiersVersInfosDoc = new LgrDozerMapper<IdentiteTiersDTO, TaInfosBoncdeAchat>();

	private LgrDozerMapper<TaBoncdeAchat,TaInfosBoncdeAchat> mapperUIToModelDocumentVersInfosDoc = new LgrDozerMapper<TaBoncdeAchat, TaInfosBoncdeAchat>();	
	
	@Override
	public IDocumentTiers enregistreNouveauDocumentSpecifique(IDocumentTiers dd) {
		try {
			if(dd!=null) {
				taBoncdeAchatService.inserer(((TaBoncdeAchat)dd));
				((TaBoncdeAchat)dd).setDateEchDocument(taBoncdeAchatService.calculDateEcheanceAbstract(((TaBoncdeAchat)dd),taCPaiement.getReportCPaiement(),taCPaiement.getFinMoisCPaiement()));
				((TaBoncdeAchat)dd).calculeTvaEtTotaux();	

				dd=taBoncdeAchatService.merge(((TaBoncdeAchat)dd));		
			}
		} catch (Exception e) {
			logger.error("",e);
		}
		return dd;
	}
	
	public void initInfosDocument(IDocumentTiers dd) {
		if(((TaBoncdeAchat)dd).getTaInfosDocument()==null) {
			((TaBoncdeAchat)dd).setTaInfosDocument(new TaInfosBoncdeAchat());
		}
	}

	@Override
	public void mapUIToModelAdresseFactVersInfosDoc(AdresseInfosFacturationDTO infos, IDocumentTiers dd) {
		initInfosDocument(dd);
		mapperUIToModelAdresseFactVersInfosDoc.map(infos, ((TaBoncdeAchat)dd).getTaInfosDocument());
	}

	@Override
	public void mapUIToModelAdresseLivVersInfosDoc(AdresseInfosLivraisonDTO infos,IDocumentTiers dd) {
		initInfosDocument(dd);
		mapperUIToModelAdresseLivVersInfosDoc.map(infos, ((TaBoncdeAchat)dd).getTaInfosDocument());
	}

	@Override
	public void mapUIToModelCPaiementVersInfosDoc(InfosCPaiementDTO infos,IDocumentTiers dd) {
		initInfosDocument(dd);
		mapperUIToModelCPaiementVersInfosDoc.map(infos, ((TaBoncdeAchat)dd).getTaInfosDocument());
	}

	@Override
	public void mapUIToModelDocumentVersInfosDoc(IDocumentTiers dd) {
		initInfosDocument(dd);
		mapperUIToModelDocumentVersInfosDoc.map(((TaBoncdeAchat)dd), ((TaBoncdeAchat)dd).getTaInfosDocument());
	}

	@Override
	public void mapUIToModelIHMIdentiteTiersVersInfosDoc(IdentiteTiersDTO infos,IDocumentTiers dd) {
		initInfosDocument(dd);
		mapperUIToModelIHMIdentiteTiersVersInfosDoc.map(infos, ((TaBoncdeAchat)dd).getTaInfosDocument());
	}
}
