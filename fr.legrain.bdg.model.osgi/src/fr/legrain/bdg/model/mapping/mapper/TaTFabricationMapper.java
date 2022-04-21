package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.article.dto.TaTFabricationDTO;
import fr.legrain.article.model.TaTFabrication;
import fr.legrain.bdg.model.mapping.ILgrMapper;



public class TaTFabricationMapper implements ILgrMapper<TaTFabricationDTO, TaTFabrication> {

	@Override
	public TaTFabrication mapDtoToEntity(TaTFabricationDTO dto, TaTFabrication entity) {
		if(entity==null)
			entity = new TaTFabrication();

		entity.setIdTFabrication(dto.getId()!=null?dto.getId():0);
		entity.setCodeTFabrication(dto.getCodeTFabrication());
		entity.setLiblTFabrication(dto.getLiblTFabrication());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaTFabricationDTO mapEntityToDto(TaTFabrication entity, TaTFabricationDTO dto) {
		if(dto==null)
			dto = new TaTFabricationDTO();

		dto.setId(entity.getIdTFabrication());
		dto.setCodeTFabrication(entity.getCodeTFabrication());
		dto.setLiblTFabrication(entity.getLiblTFabrication());	
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
