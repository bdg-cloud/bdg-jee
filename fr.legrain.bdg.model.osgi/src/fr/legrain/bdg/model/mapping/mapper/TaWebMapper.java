package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.tiers.dto.TaWebDTO;
import fr.legrain.tiers.model.TaWeb;


public class TaWebMapper implements ILgrMapper<TaWebDTO, TaWeb> {

	@Override
	public TaWeb mapDtoToEntity(TaWebDTO dto, TaWeb entity) {
		if(entity==null)
			entity = new TaWeb();

		entity.setIdWeb(dto.getId()!=null?dto.getId():0);
		entity.setAdresseWeb(dto.getAdresseWeb()); 
	
		if(entity.getTaTWeb()!=null) entity.getTaTWeb().setCodeTWeb(dto.getCodeTWeb());
		if(entity.getTaTiers()!=null) entity.getTaTiers().setCodeTiers(dto.getCodeTiers());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;		
	}

	@Override
	public TaWebDTO mapEntityToDto(TaWeb entity, TaWebDTO dto) {
		if(dto==null)
			dto = new TaWebDTO();

		dto.setId(entity.getIdWeb());
		
		dto.setAdresseWeb(entity.getAdresseWeb()); 
		if(entity.getTaTWeb()!=null) dto.setCodeTWeb(entity.getTaTWeb().getCodeTWeb());
		if(entity.getTaTiers()!=null) dto.setCodeTiers(entity.getTaTiers().getCodeTiers());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
