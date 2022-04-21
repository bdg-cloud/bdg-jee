package fr.legrain.bdg.compteclient.model.mapping.mapper;

import fr.legrain.bdg.compteclient.dto.droits.TaAuthViewDTO;
import fr.legrain.bdg.compteclient.model.droits.TaAuthView;
import fr.legrain.bdg.compteclient.model.mapping.ILgrMapper;


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
