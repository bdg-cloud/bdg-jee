package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.conformite.dto.TaModeleBaremeDTO;
import fr.legrain.conformite.model.TaModeleBareme;


public class TaModeleBaremeMapper implements ILgrMapper<TaModeleBaremeDTO, TaModeleBareme> {

	@Override
	public TaModeleBareme mapDtoToEntity(TaModeleBaremeDTO dto, TaModeleBareme entity) {
		if(entity==null)
			entity = new TaModeleBareme();

		entity.setIdModeleBareme(dto.getId()!=null?dto.getId():0);
//		entity.setAdresseWeb(dto.getAdresseWeb()); 
//	
//		if(entity.getTaTWeb()!=null) entity.getTaTWeb().setCodeTWeb(dto.getCodeTWeb());
//		if(entity.getTaTiers()!=null) entity.getTaTiers().setCodeTiers(dto.getCodeTiers());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;		
	}

	@Override
	public TaModeleBaremeDTO mapEntityToDto(TaModeleBareme entity, TaModeleBaremeDTO dto) {
		if(dto==null)
			dto = new TaModeleBaremeDTO();

		dto.setId(entity.getIdModeleBareme());
		
//		dto.setAdresseWeb(entity.getAdresseWeb()); 
//		if(entity.getTaTWeb()!=null) dto.setCodeTWeb(entity.getTaTWeb().getCodeTWeb());
//		if(entity.getTaTiers()!=null) dto.setCodeTiers(entity.getTaTiers().getCodeTiers());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
