package fr.legrain.moncompte.model.mapping.mapper;

import fr.legrain.moncompte.dto.TaPartenaireDTO;
import fr.legrain.moncompte.model.TaPartenaire;
import fr.legrain.moncompte.model.mapping.ILgrMapper;


public class TaPartenaireMapper implements ILgrMapper<TaPartenaireDTO, TaPartenaire> {

	@Override
	public TaPartenaire mapDtoToEntity(TaPartenaireDTO dto, TaPartenaire entity) {
		if(entity==null)
			entity = new TaPartenaire();

		entity.setId(dto.getId()!=null?dto.getId():0);
		

		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaPartenaireDTO mapEntityToDto(TaPartenaire entity, TaPartenaireDTO dto) {
		if(dto==null)
			dto = new TaPartenaireDTO();

		dto.setId(entity.getId());
		
		
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
