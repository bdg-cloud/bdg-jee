package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.article.dto.TaRefPrixDTO;
import fr.legrain.article.model.TaRefPrix;
import fr.legrain.bdg.model.mapping.ILgrMapper;


public class TaRefPrixMapper implements ILgrMapper<TaRefPrixDTO, TaRefPrix> {

	@Override
	public TaRefPrix mapDtoToEntity(TaRefPrixDTO dto, TaRefPrix entity) {
		if(entity==null)
			entity = new TaRefPrix();

		entity.setIdRefPrix(dto.getId()!=null?dto.getId():0);
		
//		entity.setAdresse1Adresse(dto.getAdresse1Adresse());
//		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaRefPrixDTO mapEntityToDto(TaRefPrix entity, TaRefPrixDTO dto) {
		if(dto==null)
			dto = new TaRefPrixDTO();

		dto.setId(entity.getIdRefPrix());
		
//		dto.setAdresse1Adresse(entity.getAdresse1Adresse());
//		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
