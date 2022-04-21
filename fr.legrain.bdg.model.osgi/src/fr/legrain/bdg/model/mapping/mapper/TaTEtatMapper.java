package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.document.dto.TaTEtatDTO;
import fr.legrain.document.model.TaTEtat;


public class TaTEtatMapper implements ILgrMapper<TaTEtatDTO, TaTEtat> {

	@Override
	public TaTEtat mapDtoToEntity(TaTEtatDTO dto, TaTEtat entity) {
		if(entity==null)
			entity = new TaTEtat();

		entity.setIdTEtat(dto.getId()!=null?dto.getId():0);
		entity.setCodeTEtat(dto.getCodeTEtat()!=null?dto.getCodeTEtat():null);
		entity.setLiblTEtat(dto.getLibTEtat()!=null?dto.getLibTEtat():null);
		entity.setVersionObj(dto.getVersionObj());

		return entity;		
	}

	@Override
	public TaTEtatDTO mapEntityToDto(TaTEtat entity, TaTEtatDTO dto) {
		if(dto==null)
			dto = new TaTEtatDTO();

		dto.setId(entity.getIdTEtat());
		dto.setLibTEtat(entity.getLiblTEtat());
		dto.setCodeTEtat(entity.getCodeTEtat());
			
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
