package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.edition.dto.TaActionInterneProgDTO;
import fr.legrain.edition.model.TaActionInterneProg;



public class TaActionInterneProgMapper implements ILgrMapper<TaActionInterneProgDTO, TaActionInterneProg> {

	@Override
	public TaActionInterneProg mapDtoToEntity(TaActionInterneProgDTO dto, TaActionInterneProg entity) {
		if(entity==null)
			entity = new TaActionInterneProg();

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
	public TaActionInterneProgDTO mapEntityToDto(TaActionInterneProg entity, TaActionInterneProgDTO dto) {
		if(dto==null)
			dto = new TaActionInterneProgDTO();

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
