package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.article.model.TaLRecette;
import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.document.dto.TaLRecetteDTO;


public class TaLRecetteMapper implements ILgrMapper<TaLRecetteDTO, TaLRecette> {

	@Override
	public TaLRecette mapDtoToEntity(TaLRecetteDTO dto, TaLRecette entity) {
		if(entity==null)
			entity = new TaLRecette();

		entity.setIdLRecette(dto.getIdLDocument()!=null?dto.getIdLDocument():0);
		
//		entity.setLibelleDocument(dto.getLibelleDocument());
//		entity.setCodeDocument(dto.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaLRecetteDTO mapEntityToDto(TaLRecette entity, TaLRecetteDTO dto) {
		if(dto==null)
			dto = new TaLRecetteDTO();

		dto.setIdLDocument(entity.getIdLRecette());
		
//		dto.setLibelleDocument(entity.getLibelleDocument());
//		dto.setCodeDocument(entity.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
