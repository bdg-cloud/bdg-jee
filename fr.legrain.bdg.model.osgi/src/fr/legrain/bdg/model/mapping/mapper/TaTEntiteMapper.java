package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.tiers.dto.TaTEntiteDTO;
import fr.legrain.tiers.model.TaTEntite;


public class TaTEntiteMapper implements ILgrMapper<TaTEntiteDTO, TaTEntite> {

	@Override
	public TaTEntite mapDtoToEntity(TaTEntiteDTO dto, TaTEntite entity) {
		if(entity==null)
			entity = new TaTEntite();

		entity.setIdTEntite(dto.getId()!=null?dto.getId():0);
		entity.setCodeTEntite(dto.getCodeTEntite());            
		entity.setLiblTEntite(dto.getLiblTEntite());  
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;		
	}

	@Override
	public TaTEntiteDTO mapEntityToDto(TaTEntite entity, TaTEntiteDTO dto) {
		if(dto==null)
			dto = new TaTEntiteDTO();

		dto.setId(entity.getIdTEntite());
		
		dto.setCodeTEntite(entity.getCodeTEntite());            
		dto.setLiblTEntite(entity.getLiblTEntite()); 
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
