package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.abonnement.stripe.dto.TaStripeAccountDTO;
import fr.legrain.abonnement.stripe.dto.TaStripeBankAccountDTO;
import fr.legrain.abonnement.stripe.model.TaStripeAccount;
import fr.legrain.abonnement.stripe.model.TaStripeBankAccount;
import fr.legrain.bdg.model.mapping.ILgrMapper;


public class TaStripeAccountMapper implements ILgrMapper<TaStripeAccountDTO, TaStripeAccount> {

	@Override
	public TaStripeAccount mapDtoToEntity(TaStripeAccountDTO dto, TaStripeAccount entity) {
		if(entity==null)
			entity = new TaStripeAccount();

		entity.setIdStripeAccount(dto.getId()!=null?dto.getId():0);
		
		entity.setIdExterne(dto.getIdExterne());
		
//		if(entity.getTaCompteBanque()!=null) entity.getTaCompteBanque().setIdCompteBanque(dto.getIdCompteBanque());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaStripeAccountDTO mapEntityToDto(TaStripeAccount entity, TaStripeAccountDTO dto) {
		if(dto==null)
			dto = new TaStripeAccountDTO();

		dto.setId(entity.getIdStripeAccount());
		
		dto.setIdExterne(entity.getIdExterne());
		
//		if(entity.getTaCompteBanque()!=null) dto.setIdCompteBanque(entity.getTaCompteBanque().getIdCompteBanque());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
