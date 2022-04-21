package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.droits.dto.TaOAuthScopeClientDTO;
import fr.legrain.droits.model.TaOAuthScopeClient;


public class TaOAuthScopeClientMapper implements ILgrMapper<TaOAuthScopeClientDTO, TaOAuthScopeClient> {

	@Override
	public TaOAuthScopeClient mapDtoToEntity(TaOAuthScopeClientDTO dto, TaOAuthScopeClient entity) {
		if(entity==null)
			entity = new TaOAuthScopeClient();

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
	public TaOAuthScopeClientDTO mapEntityToDto(TaOAuthScopeClient entity, TaOAuthScopeClientDTO dto) {
		if(dto==null)
			dto = new TaOAuthScopeClientDTO();

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
