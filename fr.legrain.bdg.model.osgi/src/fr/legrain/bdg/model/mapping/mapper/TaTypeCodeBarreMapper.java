package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.article.dto.TaTypeCodeBarreDTO;
import fr.legrain.article.model.TaTypeCodeBarre;

import fr.legrain.bdg.model.mapping.ILgrMapper;


public class TaTypeCodeBarreMapper implements ILgrMapper<TaTypeCodeBarreDTO, TaTypeCodeBarre> {

	@Override
	public TaTypeCodeBarre mapDtoToEntity(TaTypeCodeBarreDTO dto, TaTypeCodeBarre entity) {
		if(entity==null)
			entity = new TaTypeCodeBarre();

		entity.setIdTypeCodeBarre(dto.getId()!=null?dto.getId():0);
		entity.setCodeTypeCodeBarre(dto.getCodeTypeCodeBarre());
		entity.setLibelle(dto.getLibelleTypeCodeBarre());
		
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaTypeCodeBarreDTO mapEntityToDto(TaTypeCodeBarre entity, TaTypeCodeBarreDTO dto) {
		if(dto==null)
			dto = new TaTypeCodeBarreDTO();

		dto.setId(entity.getIdTypeCodeBarre());
		dto.setCodeTypeCodeBarre(entity.getCodeTypeCodeBarre());
		dto.setLibelleTypeCodeBarre(entity.getLibelle());
		
		
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
