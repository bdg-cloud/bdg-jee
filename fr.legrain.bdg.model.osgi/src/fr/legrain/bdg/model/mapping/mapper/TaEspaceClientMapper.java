package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.tiers.dto.TaEspaceClientDTO;
import fr.legrain.tiers.model.TaEspaceClient;
import fr.legrain.tiers.model.TaEspaceClient;


public class TaEspaceClientMapper implements ILgrMapper<TaEspaceClientDTO, TaEspaceClient> {

	@Override
	public TaEspaceClient mapDtoToEntity(TaEspaceClientDTO dto, TaEspaceClient entity) {
		if(entity==null)
			entity = new TaEspaceClient();

		entity.setIdEspaceClient(dto.getId()!=null?dto.getId():0);
		
		entity.setEmail(dto.getEmail());
		entity.setPassword(dto.getPassword());
		
//		if(entity.getTaTiers()!=null) entity.getTaTiers().setCodeTiers(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaEspaceClientDTO mapEntityToDto(TaEspaceClient entity, TaEspaceClientDTO dto) {
		if(dto==null)
			dto = new TaEspaceClientDTO();

		dto.setId(entity.getIdEspaceClient());
		
		dto.setEmail(entity.getEmail());
		dto.setPassword(entity.getPassword());
		dto.setNom(entity.getNom());
		dto.setPrenom(entity.getPrenom());
		dto.setActif(entity.getActif());
		dto.setDateDerniereConnexion(entity.getDateDerniereConnexion());
		
		if(entity.getTaTiers()!=null) { 
			dto.setCodeTiers(entity.getTaTiers().getCodeTiers());
		}
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
