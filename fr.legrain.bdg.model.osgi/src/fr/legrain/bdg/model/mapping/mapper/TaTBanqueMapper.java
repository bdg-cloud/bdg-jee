package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.tiers.dto.TaTBanqueDTO;
import fr.legrain.tiers.model.TaTBanque;


public class TaTBanqueMapper implements ILgrMapper<TaTBanqueDTO, TaTBanque> {

	@Override
	public TaTBanque mapDtoToEntity(TaTBanqueDTO dto, TaTBanque entity) {
		if(entity==null)
			entity = new TaTBanque();

		entity.setIdTBanque(dto.getId()!=null?dto.getId():0);
		entity.setCodeTBanque(dto.getCodeTBanque());
		entity.setLiblTBanque(dto.getLiblTBanque());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaTBanqueDTO mapEntityToDto(TaTBanque entity, TaTBanqueDTO dto) {
		if(dto==null)
			dto = new TaTBanqueDTO();

		dto.setId(entity.getIdTBanque());
		dto.setCodeTBanque(entity.getCodeTBanque());
		dto.setLiblTBanque(entity.getLiblTBanque());	
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
