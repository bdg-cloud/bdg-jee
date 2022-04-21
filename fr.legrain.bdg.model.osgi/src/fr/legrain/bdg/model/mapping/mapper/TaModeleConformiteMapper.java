package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.conformite.dto.TaModeleConformiteDTO;
import fr.legrain.conformite.model.TaModeleConformite;

public class TaModeleConformiteMapper implements ILgrMapper<TaModeleConformiteDTO, TaModeleConformite> {

	@Override
	public TaModeleConformite mapDtoToEntity(TaModeleConformiteDTO dto, TaModeleConformite entity) {
		if(entity==null)
			entity = new TaModeleConformite();

		entity.setIdModeleConformite(dto.getId()!=null?dto.getId():0);
//		entity.setAdresseWeb(dto.getAdresseWeb()); 
//	
//		if(entity.getTaTWeb()!=null) entity.getTaTWeb().setCodeTWeb(dto.getCodeTWeb());
//		if(entity.getTaTiers()!=null) entity.getTaTiers().setCodeTiers(dto.getCodeTiers());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;		
	}

	@Override
	public TaModeleConformiteDTO mapEntityToDto(TaModeleConformite entity, TaModeleConformiteDTO dto) {
		if(dto==null)
			dto = new TaModeleConformiteDTO();

		dto.setId(entity.getIdModeleConformite());
		
//		dto.setAdresseWeb(entity.getAdresseWeb()); 
//		if(entity.getTaTWeb()!=null) dto.setCodeTWeb(entity.getTaTWeb().getCodeTWeb());
//		if(entity.getTaTiers()!=null) dto.setCodeTiers(entity.getTaTiers().getCodeTiers());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
