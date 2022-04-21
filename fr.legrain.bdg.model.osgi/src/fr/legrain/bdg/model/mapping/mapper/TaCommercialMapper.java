package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.tiers.dto.TaCommercialDTO;
import fr.legrain.tiers.model.TaTiers;


public class TaCommercialMapper implements ILgrMapper<TaCommercialDTO, TaTiers> {

	@Override
	public TaTiers mapDtoToEntity(TaCommercialDTO dto, TaTiers entity) {
		if(entity==null)
			entity = new TaTiers();

		entity.setIdTiers(dto.getId()!=null?dto.getId():0);
		
				
		//entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaCommercialDTO mapEntityToDto(TaTiers entity, TaCommercialDTO dto) {
		if(dto==null)
			dto = new TaCommercialDTO();

		dto.setId(entity.getIdTiers());
		
		dto.setCodeTiers(entity.getCodeTiers());
		dto.setNomTiers(entity.getNomTiers());
				
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
