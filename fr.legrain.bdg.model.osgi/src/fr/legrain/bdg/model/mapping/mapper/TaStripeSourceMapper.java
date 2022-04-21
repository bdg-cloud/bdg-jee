package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.abonnement.stripe.dto.TaStripeSourceDTO;
import fr.legrain.abonnement.stripe.model.TaStripeSource;
import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.tiers.dto.TaCarteBancaireDTO;
import fr.legrain.tiers.dto.TaCompteBanqueDTO;


public class TaStripeSourceMapper implements ILgrMapper<TaStripeSourceDTO, TaStripeSource> {

	@Override
	public TaStripeSource mapDtoToEntity(TaStripeSourceDTO dto, TaStripeSource entity) {
		if(entity==null)
			entity = new TaStripeSource();

		entity.setIdStripeSource(dto.getId()!=null?dto.getId():0);
		
		entity.setIdExterne(dto.getIdExterne());

		
		if(entity.getTaStripeCustomer()!=null) {
			entity.getTaStripeCustomer().setIdExterne(dto.getIdExterneCustomer());
			entity.getTaStripeCustomer().setIdStripeCustomer(dto.getIdStripeCustomer());
		}
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaStripeSourceDTO mapEntityToDto(TaStripeSource entity, TaStripeSourceDTO dto) {
		if(dto==null)
			dto = new TaStripeSourceDTO();

		dto.setId(entity.getIdStripeSource());
		
		
		if(entity.getTaStripeTSource()!=null) {
			dto.setCodeStripeTSource(entity.getTaStripeTSource().getCodeStripeTSource());
			dto.setLiblStripeTSource(entity.getTaStripeTSource().getLiblStripeTSource());
			dto.setAutomatique(entity.getTaStripeTSource().getAutomatique());
		}
		
		if(entity.getTaStripeCustomer()!=null) {
			dto.setIdExterneCustomer(entity.getTaStripeCustomer().getIdExterne());
			dto.setIdStripeCustomer(entity.getTaStripeCustomer().getIdStripeCustomer());
		}
		
		if(entity.getTaCompteBanque()!=null) {
			dto.setTaCompteBanqueDTO(new TaCompteBanqueDTO());
			dto.getTaCompteBanqueDTO().setLast4(entity.getTaCompteBanque().getLast4());
			dto.getTaCompteBanqueDTO().setCountry(entity.getTaCompteBanque().getCountry());
		}
		
		if(entity.getTaCarteBancaire()!=null) {
			dto.setTaCarteBancaireDTO(new TaCarteBancaireDTO());
			dto.getTaCarteBancaireDTO().setLast4(entity.getTaCarteBancaire().getLast4());
			dto.getTaCarteBancaireDTO().setType(entity.getTaCarteBancaire().getType());
			dto.getTaCarteBancaireDTO().setMoisExpiration(entity.getTaCarteBancaire().getMoisExpiration());
			dto.getTaCarteBancaireDTO().setAnneeExpiration(entity.getTaCarteBancaire().getAnneeExpiration());
		}
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
