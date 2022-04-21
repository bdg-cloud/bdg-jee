package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.edition.dto.TaActionInterneDTO;
import fr.legrain.edition.model.TaActionInterne;



public class TaActionInterneMapper implements ILgrMapper<TaActionInterneDTO, TaActionInterne> {

	@Override
	public TaActionInterne mapDtoToEntity(TaActionInterneDTO dto, TaActionInterne entity) {
		if(entity==null)
			entity = new TaActionInterne();

		entity.setId(dto.getId()!=null?dto.getId():0);
		
		entity.setidAction(dto.getIdAction());
		entity.setLibelle(dto.getLibelle());
		entity.setDescription(dto.getDescription());
		entity.setSysteme(dto.isSysteme());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaActionInterneDTO mapEntityToDto(TaActionInterne entity, TaActionInterneDTO dto) {
		if(dto==null)
			dto = new TaActionInterneDTO();

		dto.setId(entity.getId());
		
		dto.setIdAction(entity.getIdAction());
		dto.setLibelle(entity.getLibelle());
		dto.setDescription(entity.getDescription());
		dto.setSysteme(entity.getSysteme());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
