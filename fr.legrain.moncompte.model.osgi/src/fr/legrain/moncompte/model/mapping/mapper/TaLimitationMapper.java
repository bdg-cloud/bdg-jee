package fr.legrain.moncompte.model.mapping.mapper;


import fr.legrain.moncompte.dto.TaLimitationDTO;
import fr.legrain.moncompte.model.TaLimitation;
import fr.legrain.moncompte.model.mapping.ILgrMapper;


public class TaLimitationMapper implements ILgrMapper<TaLimitationDTO, TaLimitation> {

	@Override
	public TaLimitation mapDtoToEntity(TaLimitationDTO dto, TaLimitation entity) {
		if(entity==null)
			entity = new TaLimitation();

		entity.setId(dto.getId()!=null?dto.getId():0);
		
//		entity.setLibelleDocument(dto.getLibelleDocument());
//		entity.setCodeDocument(dto.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaLimitationDTO mapEntityToDto(TaLimitation entity, TaLimitationDTO dto) {
		if(dto==null)
			dto = new TaLimitationDTO();

		dto.setId(entity.getId());
		
//		dto.setLibelleDocument(entity.getLibelleDocument());
//		dto.setCodeDocument(entity.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
