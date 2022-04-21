package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.conformite.dto.TaModeleGroupeDTO;
import fr.legrain.conformite.model.TaModeleGroupe;



public class TaModeleGroupeMapper implements ILgrMapper<TaModeleGroupeDTO, TaModeleGroupe> {

	@Override
	public TaModeleGroupe mapDtoToEntity(TaModeleGroupeDTO dto, TaModeleGroupe entity) {
		if(entity==null)
			entity = new TaModeleGroupe();

		entity.setIdModeleGroupe(dto.getId()!=null?dto.getId():0);
//		entity.setAdresseWeb(dto.getAdresseWeb()); 
//	
//		if(entity.getTaTWeb()!=null) entity.getTaTWeb().setCodeTWeb(dto.getCodeTWeb());
//		if(entity.getTaTiers()!=null) entity.getTaTiers().setCodeTiers(dto.getCodeTiers());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;		
	}

	@Override
	public TaModeleGroupeDTO mapEntityToDto(TaModeleGroupe entity, TaModeleGroupeDTO dto) {
		if(dto==null)
			dto = new TaModeleGroupeDTO();

		dto.setId(entity.getIdModeleGroupe());
		
//		dto.setAdresseWeb(entity.getAdresseWeb()); 
//		if(entity.getTaTWeb()!=null) dto.setCodeTWeb(entity.getTaTWeb().getCodeTWeb());
//		if(entity.getTaTiers()!=null) dto.setCodeTiers(entity.getTaTiers().getCodeTiers());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
