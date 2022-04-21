package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.document.dto.TaLFactureDTO;
import fr.legrain.document.dto.TaLFactureDTO;
import fr.legrain.document.model.TaLFacture;
import fr.legrain.document.model.TaLFacture;
import fr.legrain.document.model.TaLFacture;


public class TaLFactureMapper implements ILgrMapper<TaLFactureDTO, TaLFacture> {

	@Override
	public TaLFacture mapDtoToEntity(TaLFactureDTO dto, TaLFacture entity) {
		if(entity==null)
			entity = new TaLFacture();

		entity.setIdLDocument(dto.getIdLDocument()!=null?dto.getIdLDocument():0);
		
//		entity.setLibelleDocument(dto.getLibelleDocument());
//		entity.setCodeDocument(dto.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaLFactureDTO mapEntityToDto(TaLFacture entity, TaLFactureDTO dto) {
		if(dto==null)
			dto = new TaLFactureDTO();

		dto.setIdLDocument(entity.getIdLDocument());
		
		dto.setLibLDocument(entity.getLibLDocument());
		dto.setQteLDocument(entity.getQteLDocument());
		
		dto.setPrixULDocument(entity.getPrixULDocument());
		dto.setU1LDocument(entity.getU1LDocument());
		
		dto.setMtTtcLDocument(entity.getMtTtcLDocument());
		dto.setMtHtLDocument(entity.getMtHtLDocument());
		
		if(entity.getTaArticle()!=null) dto.setCodeArticle(entity.getTaArticle().getCodeArticle());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
