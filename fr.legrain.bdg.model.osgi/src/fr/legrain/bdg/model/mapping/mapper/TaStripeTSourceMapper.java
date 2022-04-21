package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.abonnement.stripe.dto.TaStripeTSourceDTO;
import fr.legrain.abonnement.stripe.model.TaStripeTSource;
import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.tiers.dto.TaCarteBancaireDTO;
import fr.legrain.tiers.dto.TaCompteBanqueDTO;


public class TaStripeTSourceMapper implements ILgrMapper<TaStripeTSourceDTO, TaStripeTSource> {

	@Override
	public TaStripeTSource mapDtoToEntity(TaStripeTSourceDTO dto, TaStripeTSource entity) {
		if(entity==null)
			entity = new TaStripeTSource();

		entity.setIdStripeTSource(dto.getId()!=null?dto.getId():0);
		
//		entity.setIdExterne(dto.getIdExterne());

		
//		if(entity.getTaStripeCustomer()!=null) {
//			entity.getTaStripeCustomer().setIdExterne(dto.getIdExterneCustomer());
//			entity.getTaStripeCustomer().setIdStripeCustomer(dto.getIdStripeCustomer());
//		}
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaStripeTSourceDTO mapEntityToDto(TaStripeTSource entity, TaStripeTSourceDTO dto) {
		if(dto==null)
			dto = new TaStripeTSourceDTO();

		dto.setId(entity.getIdStripeTSource());
		
//		dto.setIdExterne(entity.getIdExterne());
		
//		if(entity.getTaStripeCustomer()!=null) {
//			dto.setIdExterneCustomer(entity.getTaStripeCustomer().getIdExterne());
//			dto.setIdStripeCustomer(entity.getTaStripeCustomer().getIdStripeCustomer());
//		}
		
//		if(entity.getTaCompteBanque()!=null) {
//			dto.setTaCompteBanqueDTO(new TaCompteBanqueDTO());
//			dto.getTaCompteBanqueDTO().setLast4(entity.getTaCompteBanque().getLast4());
//			dto.getTaCompteBanqueDTO().setCountry(entity.getTaCompteBanque().getCountry());
//		}
//		
//		if(entity.getTaCarteBancaire()!=null) {
//			dto.setTaCarteBancaireDTO(new TaCarteBancaireDTO());
//			dto.getTaCarteBancaireDTO().setLast4(entity.getTaCarteBancaire().getLast4());
//			dto.getTaCarteBancaireDTO().setType(entity.getTaCarteBancaire().getType());
//			dto.getTaCarteBancaireDTO().setMoisExpiration(entity.getTaCarteBancaire().getMoisExpiration());
//			dto.getTaCarteBancaireDTO().setAnneeExpiration(entity.getTaCarteBancaire().getAnneeExpiration());
//		}
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
