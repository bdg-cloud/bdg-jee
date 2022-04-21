package fr.legrain.generation.service;


import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import fr.legrain.bdg.documents.service.remote.ITaProformaServiceRemote;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.model.TaDevis;
import fr.legrain.document.model.TaInfosProforma;
import fr.legrain.document.model.TaProforma;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.tiers.dto.AdresseInfosFacturationDTO;
import fr.legrain.tiers.dto.AdresseInfosLivraisonDTO;
import fr.legrain.tiers.dto.IdentiteTiersDTO;
import fr.legrain.tiers.dto.InfosCPaiementDTO;


@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public abstract  class AbstractGenereDocVersProforma extends AbstractGenereDoc{
	
	private @EJB ITaProformaServiceRemote taProformaService;
	
	private LgrDozerMapper<InfosCPaiementDTO,TaInfosProforma> mapperUIToModelCPaiementVersInfosDoc = new LgrDozerMapper<InfosCPaiementDTO, TaInfosProforma>();
	private LgrDozerMapper<AdresseInfosFacturationDTO,TaInfosProforma> mapperUIToModelAdresseFactVersInfosDoc = new LgrDozerMapper<AdresseInfosFacturationDTO, TaInfosProforma>();
	private LgrDozerMapper<AdresseInfosLivraisonDTO,TaInfosProforma> mapperUIToModelAdresseLivVersInfosDoc = new LgrDozerMapper<AdresseInfosLivraisonDTO, TaInfosProforma>();
	private LgrDozerMapper<IdentiteTiersDTO,TaInfosProforma> mapperUIToModelIHMIdentiteTiersVersInfosDoc = new LgrDozerMapper<IdentiteTiersDTO, TaInfosProforma>();

	private LgrDozerMapper<TaProforma,TaInfosProforma> mapperUIToModelDocumentVersInfosDoc = new LgrDozerMapper<TaProforma, TaInfosProforma>();	
	
	@Override
	public IDocumentTiers enregistreNouveauDocumentSpecifique(IDocumentTiers dd) {
		try {
			if(dd!=null) {
				taProformaService.inserer(((TaProforma)dd));	
				((TaProforma)dd).setDateEchDocument(taProformaService.calculDateEcheanceAbstract(((TaProforma)dd),taCPaiement.getReportCPaiement(),taCPaiement.getFinMoisCPaiement()));
				((TaProforma)dd).calculeTvaEtTotaux();	

				dd=taProformaService.merge(((TaProforma)dd));	
			}
		} catch (Exception e) {
			logger.error("",e);
		}
		return dd;
	}
	
	public void initInfosDocument(IDocumentTiers dd) {
		if(((TaProforma)dd).getTaInfosDocument()==null) {
			((TaProforma)dd).setTaInfosDocument(new TaInfosProforma());
		}
	}

	@Override
	public void mapUIToModelAdresseFactVersInfosDoc(AdresseInfosFacturationDTO infos, IDocumentTiers dd) {
		initInfosDocument(dd);
		mapperUIToModelAdresseFactVersInfosDoc.map(infos, ((TaProforma)dd).getTaInfosDocument());
	}

	@Override
	public void mapUIToModelAdresseLivVersInfosDoc(AdresseInfosLivraisonDTO infos,IDocumentTiers dd) {
		initInfosDocument(dd);
		mapperUIToModelAdresseLivVersInfosDoc.map(infos, ((TaProforma)dd).getTaInfosDocument());
	}

	@Override
	public void mapUIToModelCPaiementVersInfosDoc(InfosCPaiementDTO infos,IDocumentTiers dd) {
		initInfosDocument(dd);
		mapperUIToModelCPaiementVersInfosDoc.map(infos, ((TaProforma)dd).getTaInfosDocument());
	}

	@Override
	public void mapUIToModelDocumentVersInfosDoc(IDocumentTiers dd) {
		initInfosDocument(dd);
		mapperUIToModelDocumentVersInfosDoc.map(((TaProforma)dd), ((TaProforma)dd).getTaInfosDocument());
	}

	@Override
	public void mapUIToModelIHMIdentiteTiersVersInfosDoc(IdentiteTiersDTO infos,IDocumentTiers dd) {
		initInfosDocument(dd);
		mapperUIToModelIHMIdentiteTiersVersInfosDoc.map(infos, ((TaProforma)dd).getTaInfosDocument());
	}
}
