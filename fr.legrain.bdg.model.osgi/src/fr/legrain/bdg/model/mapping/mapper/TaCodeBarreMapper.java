package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.article.dto.TaCodeBarreDTO;
import fr.legrain.article.model.TaCodeBarre;
import fr.legrain.article.model.TaCodeBarre;
import fr.legrain.bdg.model.mapping.ILgrMapper;


public class TaCodeBarreMapper implements ILgrMapper<TaCodeBarreDTO, TaCodeBarre> {

	@Override
	public TaCodeBarre mapDtoToEntity(TaCodeBarreDTO dto, TaCodeBarre entity) {
		if(entity==null)
			entity = new TaCodeBarre();

		entity.setId(dto.getId()!=null?dto.getId():0);
		
		entity.setCodeBarre(dto.getCodeBarre());
		entity.setTaTypeCodeBarre(dto.getTypeCodeBarre());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaCodeBarreDTO mapEntityToDto(TaCodeBarre entity, TaCodeBarreDTO dto) {
		if(dto==null)
			dto = new TaCodeBarreDTO();

		dto.setId(entity.getId());
		dto.setCodeBarre(entity.getCodeBarre());
		dto.setTypeCodeBarre(entity.getTaTypeCodeBarre());
		
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
