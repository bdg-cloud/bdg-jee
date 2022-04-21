package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.article.dto.TaPrixDTO;
import fr.legrain.article.model.TaPrix;
import fr.legrain.bdg.model.mapping.ILgrMapper;


public class TaPrixMapper implements ILgrMapper<TaPrixDTO, TaPrix> {

	@Override
	public TaPrix mapDtoToEntity(TaPrixDTO dto, TaPrix entity) {
		if(entity==null)
			entity = new TaPrix();

		entity.setIdPrix(dto.getId()!=null?dto.getId():0);
		
		entity.setPrixPrix(dto.getPrixPrix());
		entity.setPrixttcPrix(dto.getPrixttcPrix());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaPrixDTO mapEntityToDto(TaPrix entity, TaPrixDTO dto) {
		if(dto==null)
			dto = new TaPrixDTO();

		dto.setId(entity.getIdPrix());
		
		dto.setPrixPrix(entity.getPrixPrix());
		dto.setPrixttcPrix(entity.getPrixttcPrix());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
