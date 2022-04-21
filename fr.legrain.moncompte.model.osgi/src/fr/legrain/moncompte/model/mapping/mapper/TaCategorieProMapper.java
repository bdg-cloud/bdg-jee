package fr.legrain.moncompte.model.mapping.mapper;

import fr.legrain.moncompte.dto.TaCategorieProDTO;
import fr.legrain.moncompte.model.TaCategoriePro;
import fr.legrain.moncompte.model.mapping.ILgrMapper;


public class TaCategorieProMapper implements ILgrMapper<TaCategorieProDTO, TaCategoriePro> {

	@Override
	public TaCategoriePro mapDtoToEntity(TaCategorieProDTO dto, TaCategoriePro entity) {
		if(entity==null)
			entity = new TaCategoriePro();

		entity.setIdCategoriePro(dto.getId()!=null?dto.getId():0);
		
		
		entity.setDescription(dto.getDescription());
		entity.setLibelle(dto.getLibelle());

		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaCategorieProDTO mapEntityToDto(TaCategoriePro entity, TaCategorieProDTO dto) {
		if(dto==null)
			dto = new TaCategorieProDTO();

		dto.setId(entity.getIdCategoriePro());
		
		dto.setDescription(entity.getDescription());
		dto.setLibelle(entity.getLibelle());		
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
