package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.droits.dto.TaAuthViewDTO;
import fr.legrain.droits.model.TaAuthView;
import fr.legrain.droits.model.TaAuthView;


public class TaAuthViewMapper implements ILgrMapper<TaAuthViewDTO, TaAuthView> {

	@Override
	public TaAuthView mapDtoToEntity(TaAuthViewDTO dto, TaAuthView entity) {
		if(entity==null)
			entity = new TaAuthView();

		entity.setId(dto.getId()!=null?dto.getId():0);
		
//		entity.setLibelleDocument(dto.getLibelleDocument());
//		entity.setCodeDocument(dto.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaAuthViewDTO mapEntityToDto(TaAuthView entity, TaAuthViewDTO dto) {
		if(dto==null)
			dto = new TaAuthViewDTO();

		dto.setId(entity.getId());
		
//		dto.setLibelleDocument(entity.getLibelleDocument());
//		dto.setCodeDocument(entity.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
