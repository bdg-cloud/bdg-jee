package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.document.dto.TaLBonlivDTO;
import fr.legrain.document.model.TaLBonliv;


public class TaLBonlivMapper implements ILgrMapper<TaLBonlivDTO, TaLBonliv> {

	@Override
	public TaLBonliv mapDtoToEntity(TaLBonlivDTO dto, TaLBonliv entity) {
		if(entity==null)
			entity = new TaLBonliv();

		entity.setIdLDocument(dto.getIdLDocument()!=null?dto.getIdLDocument():0);
		
//		entity.setLibelleDocument(dto.getLibelleDocument());
//		entity.setCodeDocument(dto.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaLBonlivDTO mapEntityToDto(TaLBonliv entity, TaLBonlivDTO dto) {
		if(dto==null)
			dto = new TaLBonlivDTO();

		dto.setIdLDocument(entity.getIdLDocument());
		
		dto.setNumLigneLDocument(entity.getNumLigneLDocument());
		dto.setLibLDocument(entity.getLibLDocument());
		dto.setQteLDocument(entity.getQteLDocument());
		dto.setQte2LDocument(entity.getQte2LDocument());
		dto.setEmplacement(entity.getEmplacementLDocument());
		dto.setPrixULDocument(entity.getPrixULDocument());
		dto.setMtHtLDocument(entity.getMtHtLDocument());
		dto.setMtTtcLDocument(entity.getMtTtcLDocument());
		dto.setMtHtLDocument(entity.getMtHtLDocument());
		
		if(entity.getTaArticle()!=null) {
			dto.setCodeArticle(entity.getTaArticle().getCodeArticle());
			dto.setIdArticle(entity.getTaArticle().getIdArticle());
		}
		if(entity.getTaLot()!=null) {
			dto.setNumLot(entity.getTaLot().getNumLot());
		}
		if(entity.getTaEntrepot()!=null) {
			dto.setCodeEntrepot(entity.getTaEntrepot().getCodeEntrepot());
		}
		if(entity.getTaTLigne()!=null) {
			dto.setCodeTLigne(entity.getTaTLigne().getCodeTLigne());
		}
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
