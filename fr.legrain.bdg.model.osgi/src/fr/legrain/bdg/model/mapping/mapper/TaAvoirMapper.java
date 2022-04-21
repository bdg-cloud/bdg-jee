package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.document.dto.TaAvoirDTO;
import fr.legrain.document.model.TaAvoir;
import fr.legrain.document.model.TaAvoir;


public class TaAvoirMapper implements ILgrMapper<TaAvoirDTO, TaAvoir> {

	@Override
	public TaAvoir mapDtoToEntity(TaAvoirDTO dto, TaAvoir entity) {
		if(entity==null)
			entity = new TaAvoir();

		entity.setIdDocument(dto.getId()!=null?dto.getId():0);
		
		entity.setLibelleDocument(dto.getLibelleDocument());
		entity.setCodeDocument(dto.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaAvoirDTO mapEntityToDto(TaAvoir entity, TaAvoirDTO dto) {
		if(dto==null)
			dto = new TaAvoirDTO();

		dto.setId(entity.getIdDocument());
		
		dto.setLibelleDocument(entity.getLibelleDocument());
		dto.setCodeDocument(entity.getCodeDocument());
		if (entity.getTaTiers()!= null) {
		dto.setCodeTiers(entity.getTaTiers().getCodeTiers());
		}
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
