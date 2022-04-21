package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.document.dto.TaLAvisEcheanceDTO;
import fr.legrain.document.model.TaLAvisEcheance;


public class TaLAvisEcheanceMapper implements ILgrMapper<TaLAvisEcheanceDTO, TaLAvisEcheance> {

	@Override
	public TaLAvisEcheance mapDtoToEntity(TaLAvisEcheanceDTO dto, TaLAvisEcheance entity) {
		if(entity==null)
			entity = new TaLAvisEcheance();

		entity.setIdLDocument(dto.getIdLDocument()!=null?dto.getIdLDocument():0);
		
//		entity.setLibelleDocument(dto.getLibelleDocument());
//		entity.setCodeDocument(dto.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaLAvisEcheanceDTO mapEntityToDto(TaLAvisEcheance entity, TaLAvisEcheanceDTO dto) {
		if(dto==null)
			dto = new TaLAvisEcheanceDTO();

		dto.setIdLDocument(entity.getIdLDocument());
		
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
