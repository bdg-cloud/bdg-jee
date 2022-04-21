package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.article.model.TaRecette;
import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.document.dto.TaRecetteDTO;


public class TaRecetteMapper implements ILgrMapper<TaRecetteDTO, TaRecette> {

	@Override
	public TaRecette mapDtoToEntity(TaRecetteDTO dto, TaRecette entity) {
		if(entity==null)
			entity = new TaRecette();

		entity.setIdRecette(dto.getId()!=null?dto.getId():0);
		
//		entity.setLibelleDocument(dto.getLibelleDocument());
//		entity.setCodeDocument(dto.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaRecetteDTO mapEntityToDto(TaRecette entity, TaRecetteDTO dto) {
		if(dto==null)
			dto = new TaRecetteDTO();

		dto.setId(entity.getIdRecette());
		
//		dto.setLibelleDocument(entity.getLibelleDocument());
//		dto.setCodeDocument(entity.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
