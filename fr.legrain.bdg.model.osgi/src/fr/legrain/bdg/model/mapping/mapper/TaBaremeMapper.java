package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.conformite.dto.TaBaremeDTO;
import fr.legrain.conformite.model.TaBareme;



public class TaBaremeMapper implements ILgrMapper<TaBaremeDTO, TaBareme> {

	@Override
	public TaBareme mapDtoToEntity(TaBaremeDTO dto, TaBareme entity) {
		if(entity==null)
			entity = new TaBareme();

		entity.setIdBareme(dto.getId()!=null?dto.getId():0);
		entity.setActioncorrective(dto.getActioncorrective()); 
	
//		if(entity.getTaTWeb()!=null) entity.getTaTWeb().setCodeTWeb(dto.getCodeTWeb());
//		if(entity.getTaTiers()!=null) entity.getTaTiers().setCodeTiers(dto.getCodeTiers());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;		
	}

	@Override
	public TaBaremeDTO mapEntityToDto(TaBareme entity, TaBaremeDTO dto) {
		if(dto==null)
			dto = new TaBaremeDTO();

		dto.setId(entity.getIdBareme());
		
//		dto.setAdresseWeb(entity.getAdresseWeb()); 
//		if(entity.getTaTWeb()!=null) dto.setCodeTWeb(entity.getTaTWeb().getCodeTWeb());
//		if(entity.getTaTiers()!=null) dto.setCodeTiers(entity.getTaTiers().getCodeTiers());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
