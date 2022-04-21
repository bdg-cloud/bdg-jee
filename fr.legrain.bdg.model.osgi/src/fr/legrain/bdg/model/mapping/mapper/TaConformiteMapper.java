package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.conformite.dto.TaConformiteDTO;
import fr.legrain.conformite.model.TaConformite;



public class TaConformiteMapper implements ILgrMapper<TaConformiteDTO, TaConformite> {

	@Override
	public TaConformite mapDtoToEntity(TaConformiteDTO dto, TaConformite entity) {
		if(entity==null)
			entity = new TaConformite();

		entity.setIdConformite(dto.getId()!=null?dto.getId():0);
//		entity.setAdresseWeb(dto.getAdresseWeb()); 
//	
//		if(entity.getTaTWeb()!=null) entity.getTaTWeb().setCodeTWeb(dto.getCodeTWeb());
//		if(entity.getTaTiers()!=null) entity.getTaTiers().setCodeTiers(dto.getCodeTiers());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;		
	}

	@Override
	public TaConformiteDTO mapEntityToDto(TaConformite entity, TaConformiteDTO dto) {
		if(dto==null)
			dto = new TaConformiteDTO();

		dto.setId(entity.getIdConformite());
		
//		dto.setAdresseWeb(entity.getAdresseWeb()); 
//		if(entity.getTaTWeb()!=null) dto.setCodeTWeb(entity.getTaTWeb().getCodeTWeb());
//		if(entity.getTaTiers()!=null) dto.setCodeTiers(entity.getTaTiers().getCodeTiers());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
