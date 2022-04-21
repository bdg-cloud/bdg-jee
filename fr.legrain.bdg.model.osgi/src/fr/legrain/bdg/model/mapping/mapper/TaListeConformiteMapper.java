package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.conformite.dto.TaListeConformiteDTO;
import fr.legrain.conformite.model.TaListeConformite;


public class TaListeConformiteMapper implements ILgrMapper<TaListeConformiteDTO, TaListeConformite> {

	@Override
	public TaListeConformite mapDtoToEntity(TaListeConformiteDTO dto, TaListeConformite entity) {
		if(entity==null)
			entity = new TaListeConformite();

		entity.setIdListeConformite(dto.getId()!=null?dto.getId():0);
//		entity.setAdresseWeb(dto.getAdresseWeb()); 
//	
//		if(entity.getTaTWeb()!=null) entity.getTaTWeb().setCodeTWeb(dto.getCodeTWeb());
//		if(entity.getTaTiers()!=null) entity.getTaTiers().setCodeTiers(dto.getCodeTiers());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;		
	}

	@Override
	public TaListeConformiteDTO mapEntityToDto(TaListeConformite entity, TaListeConformiteDTO dto) {
		if(dto==null)
			dto = new TaListeConformiteDTO();

		dto.setId(entity.getIdListeConformite());
		
//		dto.setAdresseWeb(entity.getAdresseWeb()); 
//		if(entity.getTaTWeb()!=null) dto.setCodeTWeb(entity.getTaTWeb().getCodeTWeb());
//		if(entity.getTaTiers()!=null) dto.setCodeTiers(entity.getTaTiers().getCodeTiers());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
