package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.conformite.dto.TaResultatConformiteDTO;
import fr.legrain.conformite.model.TaResultatConformite;


public class TaResultatConformiteMapper implements ILgrMapper<TaResultatConformiteDTO, TaResultatConformite> {

	@Override
	public TaResultatConformite mapDtoToEntity(TaResultatConformiteDTO dto, TaResultatConformite entity) {
		if(entity==null)
			entity = new TaResultatConformite();

		entity.setIdResultatConformite(dto.getId()!=null?dto.getId():0);
//		entity.setAdresseWeb(dto.getAdresseWeb()); 
//	
//		if(entity.getTaTWeb()!=null) entity.getTaTWeb().setCodeTWeb(dto.getCodeTWeb());
//		if(entity.getTaTiers()!=null) entity.getTaTiers().setCodeTiers(dto.getCodeTiers());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;		
	}

	@Override
	public TaResultatConformiteDTO mapEntityToDto(TaResultatConformite entity, TaResultatConformiteDTO dto) {
		if(dto==null)
			dto = new TaResultatConformiteDTO();

		dto.setId(entity.getIdResultatConformite());
		
//		dto.setAdresseWeb(entity.getAdresseWeb()); 
//		if(entity.getTaTWeb()!=null) dto.setCodeTWeb(entity.getTaTWeb().getCodeTWeb());
//		if(entity.getTaTiers()!=null) dto.setCodeTiers(entity.getTaTiers().getCodeTiers());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
