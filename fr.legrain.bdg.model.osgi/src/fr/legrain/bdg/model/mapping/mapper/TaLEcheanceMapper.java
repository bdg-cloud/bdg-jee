package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.document.dto.TaLEcheanceDTO;
import fr.legrain.document.model.TaLEcheance;


public class TaLEcheanceMapper implements ILgrMapper<TaLEcheanceDTO, TaLEcheance> {

	@Override
	public TaLEcheance mapDtoToEntity(TaLEcheanceDTO dto, TaLEcheance entity) {
		if(entity==null)
			entity = new TaLEcheance();

		entity.setIdLEcheance(dto.getIdLEcheance()!=null?dto.getIdLEcheance():0);
		
//		entity.setLibelleDocument(dto.getLibelleDocument());
//		entity.setCodeDocument(dto.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaLEcheanceDTO mapEntityToDto(TaLEcheance entity, TaLEcheanceDTO dto) {
		if(dto==null)
			dto = new TaLEcheanceDTO();

		dto.setIdLEcheance(entity.getIdLEcheance());
				
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
