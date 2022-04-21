package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.document.dto.TaLPanierDTO;
import fr.legrain.document.model.TaLPanier;


public class TaLPanierMapper implements ILgrMapper<TaLPanierDTO, TaLPanier> {

	@Override
	public TaLPanier mapDtoToEntity(TaLPanierDTO dto, TaLPanier entity) {
		if(entity==null)
			entity = new TaLPanier();

		entity.setIdLDocument(dto.getIdLDocument()!=null?dto.getIdLDocument():0);
		
//		entity.setLibelleDocument(dto.getLibelleDocument());
//		entity.setCodeDocument(dto.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaLPanierDTO mapEntityToDto(TaLPanier entity, TaLPanierDTO dto) {
		if(dto==null)
			dto = new TaLPanierDTO();

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
		
		dto.setCodeLiaisonLigne(entity.getCodeLiaisonLigne());
		
		if(entity.getTaArticle()!=null) {
			dto.setIdArticle(entity.getTaArticle().getIdArticle());
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
