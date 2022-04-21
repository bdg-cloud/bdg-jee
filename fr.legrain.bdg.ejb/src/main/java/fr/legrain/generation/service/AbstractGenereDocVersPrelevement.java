package fr.legrain.generation.service;


import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import fr.legrain.bdg.documents.service.remote.ITaPrelevementServiceRemote;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.model.TaDevis;
import fr.legrain.document.model.TaInfosPrelevement;
import fr.legrain.document.model.TaPrelevement;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.tiers.dto.AdresseInfosFacturationDTO;
import fr.legrain.tiers.dto.AdresseInfosLivraisonDTO;
import fr.legrain.tiers.dto.IdentiteTiersDTO;
import fr.legrain.tiers.dto.InfosCPaiementDTO;


@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public abstract  class AbstractGenereDocVersPrelevement extends AbstractGenereDoc{
	
	protected @EJB ITaPrelevementServiceRemote taPrelevementService;
	
	private LgrDozerMapper<InfosCPaiementDTO,TaInfosPrelevement> mapperUIToModelCPaiementVersInfosDoc = new LgrDozerMapper<InfosCPaiementDTO, TaInfosPrelevement>();
	private LgrDozerMapper<AdresseInfosFacturationDTO,TaInfosPrelevement> mapperUIToModelAdresseFactVersInfosDoc = new LgrDozerMapper<AdresseInfosFacturationDTO, TaInfosPrelevement>();
	private LgrDozerMapper<AdresseInfosLivraisonDTO,TaInfosPrelevement> mapperUIToModelAdresseLivVersInfosDoc = new LgrDozerMapper<AdresseInfosLivraisonDTO, TaInfosPrelevement>();
	private LgrDozerMapper<IdentiteTiersDTO,TaInfosPrelevement> mapperUIToModelIHMIdentiteTiersVersInfosDoc = new LgrDozerMapper<IdentiteTiersDTO, TaInfosPrelevement>();

	private LgrDozerMapper<TaPrelevement,TaInfosPrelevement> mapperUIToModelDocumentVersInfosDoc = new LgrDozerMapper<TaPrelevement, TaInfosPrelevement>();	
	@Override
	public IDocumentTiers enregistreNouveauDocumentSpecifique(IDocumentTiers dd) {
		try {
			if(dd!=null) {
				taPrelevementService.inserer(((TaPrelevement)dd));
				((TaPrelevement)dd).setDateEchDocument(taPrelevementService.calculDateEcheanceAbstract(((TaPrelevement)dd),taCPaiement.getReportCPaiement(),taCPaiement.getFinMoisCPaiement()));
				((TaPrelevement)dd).calculeTvaEtTotaux();	

				dd=taPrelevementService.merge(((TaPrelevement)dd));
			}
		} catch (Exception e) {
			logger.error("",e);
		}
		return dd;
	}
	
	public void initInfosDocument(IDocumentTiers dd) {
		if(((TaPrelevement)dd).getTaInfosDocument()==null) {
			((TaPrelevement)dd).setTaInfosDocument(new TaInfosPrelevement());
		}
	}

	@Override
	public void mapUIToModelAdresseFactVersInfosDoc(AdresseInfosFacturationDTO infos, IDocumentTiers dd) {
		initInfosDocument(dd);
		mapperUIToModelAdresseFactVersInfosDoc.map(infos, ((TaPrelevement)dd).getTaInfosDocument());
	}

	@Override
	public void mapUIToModelAdresseLivVersInfosDoc(AdresseInfosLivraisonDTO infos,IDocumentTiers dd) {
		initInfosDocument(dd);
		mapperUIToModelAdresseLivVersInfosDoc.map(infos, ((TaPrelevement)dd).getTaInfosDocument());
	}

	@Override
	public void mapUIToModelCPaiementVersInfosDoc(InfosCPaiementDTO infos,IDocumentTiers dd) {
		initInfosDocument(dd);
		mapperUIToModelCPaiementVersInfosDoc.map(infos, ((TaPrelevement)dd).getTaInfosDocument());
	}

	@Override
	public void mapUIToModelDocumentVersInfosDoc(IDocumentTiers dd) {
		initInfosDocument(dd);
		mapperUIToModelDocumentVersInfosDoc.map(((TaPrelevement)dd), ((TaPrelevement)dd).getTaInfosDocument());
	}

	@Override
	public void mapUIToModelIHMIdentiteTiersVersInfosDoc(IdentiteTiersDTO infos,IDocumentTiers dd) {
		initInfosDocument(dd);
		mapperUIToModelIHMIdentiteTiersVersInfosDoc.map(infos, ((TaPrelevement)dd).getTaInfosDocument());
	}


}
