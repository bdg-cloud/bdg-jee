package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.abonnement.stripe.dto.TaStripeBankAccountDTO;
import fr.legrain.abonnement.stripe.model.TaStripeBankAccount;
import fr.legrain.bdg.model.mapping.ILgrMapper;


public class TaStripeBankAccountMapper implements ILgrMapper<TaStripeBankAccountDTO, TaStripeBankAccount> {

	@Override
	public TaStripeBankAccount mapDtoToEntity(TaStripeBankAccountDTO dto, TaStripeBankAccount entity) {
		if(entity==null)
			entity = new TaStripeBankAccount();

		entity.setIdStripeBankAccount(dto.getId()!=null?dto.getId():0);
		
		entity.setIdExterne(dto.getIdExterne());
		
		if(entity.getTaCompteBanque()!=null) entity.getTaCompteBanque().setIdCompteBanque(dto.getIdCompteBanque());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaStripeBankAccountDTO mapEntityToDto(TaStripeBankAccount entity, TaStripeBankAccountDTO dto) {
		if(dto==null)
			dto = new TaStripeBankAccountDTO();

		dto.setId(entity.getIdStripeBankAccount());
		
		dto.setIdExterne(entity.getIdExterne());
		
		if(entity.getTaCompteBanque()!=null) dto.setIdCompteBanque(entity.getTaCompteBanque().getIdCompteBanque());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
