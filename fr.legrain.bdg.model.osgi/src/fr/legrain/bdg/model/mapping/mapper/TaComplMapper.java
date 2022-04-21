package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.tiers.dto.TaComplDTO;
import fr.legrain.tiers.model.TaCompl;


public class TaComplMapper implements ILgrMapper<TaComplDTO, TaCompl> {

	@Override
	public TaCompl mapDtoToEntity(TaComplDTO dto, TaCompl entity) {
		if(entity==null)
			entity = new TaCompl();

		entity.setIdCompl(dto.getId()!=null?dto.getId():0);
		
		entity.setAccise(dto.getAccise());        
		entity.setSiretCompl(dto.getSiretCompl()); 
		entity.setTvaIComCompl(dto.getTvaIComCompl());  
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;		
	}

	@Override
	public TaComplDTO mapEntityToDto(TaCompl entity, TaComplDTO dto) {
		if(dto==null)
			dto = new TaComplDTO();

		dto.setId(entity.getIdCompl());
		
		dto.setAccise(entity.getAccise());        
		dto.setSiretCompl(entity.getSiretCompl()); 
		dto.setTvaIComCompl(entity.getTvaIComCompl()); 
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
