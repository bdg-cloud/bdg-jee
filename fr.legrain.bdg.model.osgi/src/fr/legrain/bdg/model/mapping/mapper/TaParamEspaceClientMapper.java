package fr.legrain.bdg.model.mapping.mapper;

import java.math.BigDecimal;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.tiers.dto.TaParamEspaceClientDTO;
import fr.legrain.tiers.model.TaAdresse;
import fr.legrain.tiers.model.TaCompteBanque;
import fr.legrain.tiers.model.TaParamEspaceClient;
import fr.legrain.tiers.model.TaParamEspaceClient;
import fr.legrain.tiers.model.TaParamEspaceClient;


public class TaParamEspaceClientMapper implements ILgrMapper<TaParamEspaceClientDTO, TaParamEspaceClient> {

	@Override
	public TaParamEspaceClient mapDtoToEntity(TaParamEspaceClientDTO dto, TaParamEspaceClient entity) {
		if(entity==null)
			entity = new TaParamEspaceClient();

		entity.setIdParamEspaceClient(dto.getId()!=null?dto.getId():0);
		
//		entity.setLogin(dto.getLogin());
//		entity.setPassword(dto.getPassword());
		
//		if(entity.getTaTiers()!=null) entity.getTaTiers().setCodeTiers(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaParamEspaceClientDTO mapEntityToDto(TaParamEspaceClient entity, TaParamEspaceClientDTO dto) {
		if(dto==null)
			dto = new TaParamEspaceClientDTO();

		dto.setId(entity.getIdParamEspaceClient());
		
		dto.setAfficheCommandes(entity.getAfficheCommandes());
		dto.setAfficheDevis(entity.getAfficheDevis());
		dto.setAfficheFactures(entity.getAfficheFactures());
		dto.setAfficheLivraisons(entity.getAfficheLivraisons());
		dto.setAfficheAvisEcheance(entity.getAfficheAvisEcheance());
		
		dto.setAfficheBoutique(entity.getAfficheBoutique());
		dto.setAfficheCatalogue(entity.getAfficheCatalogue());
		dto.setActivePanier(entity.getActivePanier());
		dto.setActiveEmailRenseignementProduit(entity.getActiveEmailRenseignementProduit());
		dto.setActiveCommandeEmailSimple(entity.getActiveCommandeEmailSimple());
		dto.setCgvCatWeb(entity.getCgvCatWeb());
		dto.setCgvFileCatWeb(entity.getCgvFileCatWeb());
		dto.setFraisPortFixe(entity.getFraisPortFixe());
		dto.setFraisPortLimiteOffert(entity.getFraisPortLimiteOffert());
		dto.setAfficherPrixCatalogue(entity.getAfficherPrixCatalogue());
		dto.setTexteAccueilCatalogue(entity.getTexteAccueilCatalogue());
		
		dto.setPaiementCb(entity.getPaiementCb());
		dto.setEspaceClientActif(entity.getEspaceClientActif());
		dto.setLogoFooter(entity.getLogoFooter());
		dto.setLogoLogin(entity.getLogoLogin());
		dto.setLogoHeader(entity.getLogoHeader());
		dto.setLogoPagesSimples(entity.getLogoPagesSimples());
		dto.setImageBackgroundLogin(entity.getImageBackgroundLogin());
		dto.setTexteLogin1(entity.getTexteLogin1());
		dto.setTexteLogin2(entity.getTexteLogin2());
		dto.setTexteAccueil(entity.getTexteAccueil());
		dto.setThemeDefaut(entity.getThemeDefaut());
		dto.setAffichePublicite(entity.getAffichePublicite());
		dto.setMiseADispositionAutomatiqueDesFactures(entity.getMiseADispositionAutomatiqueDesFactures());
		
		dto.setActiveLivraison(entity.getActiveLivraison());
		dto.setActivePaiementPanierCB(entity.getActivePaiementPanierCB());
		dto.setActivePaiementPanierSurPlace(entity.getActivePaiementPanierSurPlace());
		dto.setGenerationDocAuPaiementPanier(entity.getGenerationDocAuPaiementPanier());
		dto.setActiveEmailConfirmationCmd(entity.getActiveEmailConfirmationCmd());
		dto.setTexteConfirmationPaiementBoutique(entity.getTexteConfirmationPaiementBoutique());
		
		if(entity.getTaArticleFdp()!=null) {
			dto.setIdArticleFdp(entity.getTaArticleFdp().getIdArticle());
			dto.setCodeArticleFdp(entity.getTaArticleFdp().getCodeArticle());
			dto.setLibelleArticleFdp(entity.getTaArticleFdp().getLibellecArticle());
			if(entity.getTaArticleFdp().getTaPrix()!=null) {
				dto.setPrixPrixArticleFdp(entity.getTaArticleFdp().getTaPrix().getPrixPrix());
				dto.setPrixttcPrixArticleFdp(entity.getTaArticleFdp().getTaPrix().getPrixttcPrix());
			}
			if(entity.getTaArticleFdp().getTaTva()!=null) {
				dto.setCodeTvaArticleFdp(entity.getTaArticleFdp().getTaTva().getCodeTva());
				dto.setTauxTvaArticleFdp(entity.getTaArticleFdp().getTaTva().getTauxTva());
			}
		}
		
		dto.setActivePointRetrait(entity.getActivePointRetrait());

		if(entity.getTaAdressePointRetrait()!=null) {
			//quand la liaisona avec la table adresse sera faite, remplir les infos sur l'adresse a partir de cette liaison
		}
		dto.setAdresse1PointRetrait(entity.getAdresse1PointRetrait());
		dto.setAdresse2PointRetrait(entity.getAdresse2PointRetrait());
		dto.setAdresse3PointRetrait(entity.getAdresse3PointRetrait());
		dto.setCodepostalPointRetrait(entity.getCodepostalPointRetrait());
		dto.setVillePointRetrait(entity.getVillePointRetrait());
		dto.setPaysPointRetrait(entity.getPaysPointRetrait());
		dto.setLatitudeDecPointRetrait(entity.getLatitudeDecPointRetrait());
		dto.setLongitudeDecPointRetrait(entity.getLongitudeDecPointRetrait());

		
		dto.setHorairesOuverturePointRetrait(entity.getHorairesOuverturePointRetrait());
		
		dto.setActivePaiementPanierCheque(entity.getActivePaiementPanierCheque());
		dto.setActivePaiementPanierVirement(entity.getActivePaiementPanierVirement());
		dto.setPrixCatalogueHt(entity.getPrixCatalogueHt());
		dto.setActivePlanningRetrait(entity.getActivePlanningRetrait());
		
		if(entity.getTaCompteBanquePaiementVirement()!=null) {
			//quand la liaisona avec la table compte banque sera faite, remplir les infos sur le compte a partir de cette liaison
		}
		dto.setIbanPaiementVirement(entity.getIbanPaiementVirement());
		dto.setSwiftPaiementVirement(entity.getSwiftPaiementVirement());
		
		dto.setUtilisateurPeuCreerCompte(entity.getUtilisateurPeuCreerCompte());
		dto.setLiaisonNouveauCompteCodeClientAuto(entity.getLiaisonNouveauCompteCodeClientAuto());
		dto.setLiaisonNouveauCompteEmailAuto(entity.getLiaisonNouveauCompteEmailAuto());
		dto.setAutoriseMajDonneeParClient(entity.getAutoriseMajDonneeParClient());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
