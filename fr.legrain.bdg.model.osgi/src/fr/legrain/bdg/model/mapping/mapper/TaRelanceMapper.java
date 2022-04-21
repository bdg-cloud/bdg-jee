package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.document.dto.TaRelanceDTO;
import fr.legrain.document.model.TaRelance;


public class TaRelanceMapper implements ILgrMapper<TaRelanceDTO, TaRelance> {

	@Override
	public TaRelance mapDtoToEntity(TaRelanceDTO dto, TaRelance entity) {
		if(entity==null)
			entity = new TaRelance();

		entity.setIdRelance(dto.getIdRelance()!=null?dto.getIdRelance():0);
		
		entity.setCodeRelance(dto.getCodeRelance());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaRelanceDTO mapEntityToDto(TaRelance entity, TaRelanceDTO dto) {
		if(dto==null)
			dto = new TaRelanceDTO();

		dto.setIdRelance(entity.getIdRelance());
		
		dto.setCodeRelance(entity.getCodeRelance());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
