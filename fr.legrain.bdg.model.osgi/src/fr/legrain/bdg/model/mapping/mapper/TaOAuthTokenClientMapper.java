package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.droits.dto.TaOAuthTokenClientDTO;
import fr.legrain.droits.model.TaOAuthTokenClient;


public class TaOAuthTokenClientMapper implements ILgrMapper<TaOAuthTokenClientDTO, TaOAuthTokenClient> {

	@Override
	public TaOAuthTokenClient mapDtoToEntity(TaOAuthTokenClientDTO dto, TaOAuthTokenClient entity) {
		if(entity==null)
			entity = new TaOAuthTokenClient();

		entity.setId(dto.getId()!=null?dto.getId():0);
		
		entity.setAccessToken(dto.getAccessToken());
		entity.setDateCreation(dto.getDateCreation());
		entity.setRefreshToken(dto.getRefreshToken());
		entity.setExpiresIn(dto.getExpiresIn());
		entity.setScope(dto.getScope());
		entity.setTokenType(dto.getTokenType());
		
		if(entity.getTaOAuthServiceClient()!=null) entity.getTaOAuthServiceClient().setCode(dto.getCodeOAuthServiceClient());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaOAuthTokenClientDTO mapEntityToDto(TaOAuthTokenClient entity, TaOAuthTokenClientDTO dto) {
		if(dto==null)
			dto = new TaOAuthTokenClientDTO();

		dto.setId(entity.getId());
		
		dto.setAccessToken(entity.getAccessToken());
		dto.setDateCreation(entity.getDateCreation());
		dto.setRefreshToken(entity.getRefreshToken());
//		dto.setExpiresIn(entity.getExpiresIn());
		dto.setScope(entity.getScope());
		dto.setTokenType(entity.getTokenType());
		
		if(entity.getTaOAuthServiceClient()!=null) dto.setCodeOAuthServiceClient(entity.getTaOAuthServiceClient().getCode());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
