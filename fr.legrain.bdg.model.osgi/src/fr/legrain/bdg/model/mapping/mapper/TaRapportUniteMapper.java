package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.article.dto.TaRapportUniteDTO;
import fr.legrain.article.model.TaRapportUnite;
import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.lib.data.LibConversion;


public class TaRapportUniteMapper implements ILgrMapper<TaRapportUniteDTO, TaRapportUnite> {

	@Override
	public TaRapportUnite mapDtoToEntity(TaRapportUniteDTO dto, TaRapportUnite entity) {
		if(entity==null)
			entity = new TaRapportUnite();

		entity.setId(dto.getId()!=null?dto.getId():0);
		
		entity.setNbDecimal(dto.getNbDecimal());
		entity.setRapport(dto.getRapport());
		entity.setSens(LibConversion.booleanToInt(dto.getSens()));
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaRapportUniteDTO mapEntityToDto(TaRapportUnite entity, TaRapportUniteDTO dto) {
		if(dto==null)
			dto = new TaRapportUniteDTO();

		dto.setId(entity.getId());
		
		dto.setNbDecimal(entity.getNbDecimal());
		dto.setRapport(entity.getRapport());
		dto.setSens(LibConversion.intToBoolean(entity.getSens()));
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
