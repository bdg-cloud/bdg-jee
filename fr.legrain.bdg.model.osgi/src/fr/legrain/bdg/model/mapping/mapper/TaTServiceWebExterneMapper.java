package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.servicewebexterne.dto.TaTServiceWebExterneDTO;
import fr.legrain.servicewebexterne.model.TaTServiceWebExterne;


public class TaTServiceWebExterneMapper implements ILgrMapper<TaTServiceWebExterneDTO, TaTServiceWebExterne> {

	@Override
	public TaTServiceWebExterne mapDtoToEntity(TaTServiceWebExterneDTO dto, TaTServiceWebExterne entity) {
		if(entity==null)
			entity = new TaTServiceWebExterne();

		entity.setIdTServiceWebExterne(dto.getId()!=null?dto.getId():0);
		
		entity.setCodeTServiceWebExterne(dto.getCodeTServiceWebExterne());
		entity.setDescriptionTServiceWebExterne(dto.getDescriptionTServiceWebExterne());
		entity.setLibelleTServiceWebExterne(dto.getLibelleTServiceWebExterne());
		entity.setSysteme(dto.isSysteme());
//		entity.setPaysAdresse(dto.getPaysAdresse());
//		entity.setVilleAdresse(dto.getVilleAdresse());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaTServiceWebExterneDTO mapEntityToDto(TaTServiceWebExterne entity, TaTServiceWebExterneDTO dto) {
		if(dto==null)
			dto = new TaTServiceWebExterneDTO();

		dto.setId(entity.getIdTServiceWebExterne());
		
		dto.setCodeTServiceWebExterne(entity.getCodeTServiceWebExterne());
		dto.setLibelleTServiceWebExterne(entity.getLibelleTServiceWebExterne());
		dto.setDescriptionTServiceWebExterne(entity.getDescriptionTServiceWebExterne());
		dto.setSysteme(entity.isSysteme());
//		dto.setPaysAdresse(entity.getPaysAdresse());
//		dto.setVilleAdresse(entity.getVilleAdresse());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
