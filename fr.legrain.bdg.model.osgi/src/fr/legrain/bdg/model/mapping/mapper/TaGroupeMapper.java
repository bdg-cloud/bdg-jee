package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.conformite.dto.TaGroupeDTO;
import fr.legrain.conformite.model.TaGroupe;



public class TaGroupeMapper implements ILgrMapper<TaGroupeDTO, TaGroupe> {

	@Override
	public TaGroupe mapDtoToEntity(TaGroupeDTO dto, TaGroupe entity) {
		if(entity==null)
			entity = new TaGroupe();

		entity.setIdGroupe(dto.getId()!=null?dto.getId():0);
		entity.setLibelle(dto.getLibelle());
		entity.setCodeGroupe(dto.getCodeGroupe()); 
//	
//		if(entity.getTaTWeb()!=null) entity.getTaTWeb().setCodeTWeb(dto.getCodeTWeb());
//		if(entity.getTaTiers()!=null) entity.getTaTiers().setCodeTiers(dto.getCodeTiers());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;		
	}

	@Override
	public TaGroupeDTO mapEntityToDto(TaGroupe entity, TaGroupeDTO dto) {
		if(dto==null)
			dto = new TaGroupeDTO();

		dto.setId(entity.getIdGroupe());
		
		dto.setLibelle(entity.getLibelle());
		dto.setCodeGroupe(entity.getCodeGroupe()); 
		
//		dto.setAdresseWeb(entity.getAdresseWeb()); 
//		if(entity.getTaTWeb()!=null) dto.setCodeTWeb(entity.getTaTWeb().getCodeTWeb());
//		if(entity.getTaTiers()!=null) dto.setCodeTiers(entity.getTaTiers().getCodeTiers());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
