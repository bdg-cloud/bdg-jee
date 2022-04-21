package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.conformite.dto.TaTypeConformiteDTO;
import fr.legrain.conformite.model.TaTypeConformite;


public class TaTypeConformiteMapper implements ILgrMapper<TaTypeConformiteDTO, TaTypeConformite> {

	@Override
	public TaTypeConformite mapDtoToEntity(TaTypeConformiteDTO dto, TaTypeConformite entity) {
		if(entity==null)
			entity = new TaTypeConformite();

		entity.setIdTypeConformite(dto.getId()!=null?dto.getId():0);
//		entity.setAdresseWeb(dto.getAdresseWeb()); 
//	
//		if(entity.getTaTWeb()!=null) entity.getTaTWeb().setCodeTWeb(dto.getCodeTWeb());
//		if(entity.getTaTiers()!=null) entity.getTaTiers().setCodeTiers(dto.getCodeTiers());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;		
	}

	@Override
	public TaTypeConformiteDTO mapEntityToDto(TaTypeConformite entity, TaTypeConformiteDTO dto) {
		if(dto==null)
			dto = new TaTypeConformiteDTO();

		dto.setId(entity.getIdTypeConformite());
		
//		dto.setAdresseWeb(entity.getAdresseWeb()); 
//		if(entity.getTaTWeb()!=null) dto.setCodeTWeb(entity.getTaTWeb().getCodeTWeb());
//		if(entity.getTaTiers()!=null) dto.setCodeTiers(entity.getTaTiers().getCodeTiers());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
