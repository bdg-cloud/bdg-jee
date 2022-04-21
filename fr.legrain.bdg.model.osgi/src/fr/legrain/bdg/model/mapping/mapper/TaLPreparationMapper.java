package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.document.dto.TaLPreparationDTO;
import fr.legrain.document.model.TaLPreparation;


public class TaLPreparationMapper implements ILgrMapper<TaLPreparationDTO, TaLPreparation> {

	@Override
	public TaLPreparation mapDtoToEntity(TaLPreparationDTO dto, TaLPreparation entity) {
		if(entity==null)
			entity = new TaLPreparation();

		entity.setIdLDocument(dto.getIdLDocument()!=null?dto.getIdLDocument():0);
		
//		entity.setLibelleDocument(dto.getLibelleDocument());
//		entity.setCodeDocument(dto.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaLPreparationDTO mapEntityToDto(TaLPreparation entity, TaLPreparationDTO dto) {
		if(dto==null)
			dto = new TaLPreparationDTO();

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
