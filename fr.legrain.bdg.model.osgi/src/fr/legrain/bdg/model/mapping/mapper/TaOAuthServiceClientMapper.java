package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.droits.dto.TaOAuthServiceClientDTO;
import fr.legrain.droits.model.TaOAuthServiceClient;


public class TaOAuthServiceClientMapper implements ILgrMapper<TaOAuthServiceClientDTO, TaOAuthServiceClient> {

	@Override
	public TaOAuthServiceClient mapDtoToEntity(TaOAuthServiceClientDTO dto, TaOAuthServiceClient entity) {
		if(entity==null)
			entity = new TaOAuthServiceClient();

		entity.setId(dto.getId()!=null?dto.getId():0);
		
		entity.setCode(dto.getCode());
		entity.setDescription(dto.getDescription());
		entity.setLibelle(dto.getLibelle());
		entity.setActif(dto.getActif());
		entity.setSysteme(dto.getSysteme());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaOAuthServiceClientDTO mapEntityToDto(TaOAuthServiceClient entity, TaOAuthServiceClientDTO dto) {
		if(dto==null)
			dto = new TaOAuthServiceClientDTO();

		dto.setId(entity.getId());
		
		dto.setCode(entity.getCode());
		dto.setDescription(entity.getDescription());
		dto.setLibelle(entity.getLibelle());
		dto.setActif(entity.getActif());
		dto.setSysteme(entity.getSysteme());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
