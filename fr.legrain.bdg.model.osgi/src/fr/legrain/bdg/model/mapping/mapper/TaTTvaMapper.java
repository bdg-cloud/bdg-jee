package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.article.dto.TaTTvaDTO;
import fr.legrain.article.model.TaTTva;
import fr.legrain.bdg.model.mapping.ILgrMapper;


public class TaTTvaMapper implements ILgrMapper<TaTTvaDTO, TaTTva> {

	@Override
	public TaTTva mapDtoToEntity(TaTTvaDTO dto, TaTTva entity) {
		if(entity==null)
			entity = new TaTTva();

		entity.setIdTTva(dto.getId()!=null?dto.getId():0);
		
		entity.setCodeTTva(dto.getCodeTTva());
		entity.setLibTTva(dto.getLibTTva());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaTTvaDTO mapEntityToDto(TaTTva entity, TaTTvaDTO dto) {
		if(dto==null)
			dto = new TaTTvaDTO();

		dto.setId(entity.getIdTTva());
		
		dto.setCodeTTva(entity.getCodeTTva());
		dto.setLibTTva(entity.getLibTTva());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
