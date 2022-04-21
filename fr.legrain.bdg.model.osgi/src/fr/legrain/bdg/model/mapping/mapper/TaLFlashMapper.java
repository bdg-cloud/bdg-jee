package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.document.dto.TaLFlashDTO;
import fr.legrain.document.model.TaLFlash;


public class TaLFlashMapper implements ILgrMapper<TaLFlashDTO, TaLFlash> {

	@Override
	public TaLFlash mapDtoToEntity(TaLFlashDTO dto, TaLFlash entity) {
		if(entity==null)
			entity = new TaLFlash();

		entity.setIdLFlash(dto.getId()!=null?dto.getId():0);
		
		entity.setLibLFlash(dto.getLibLDocument());
		entity.setCodeBarre(dto.getCodeBarre());
		entity.setCodeBarreLu(dto.getCodeDocument());
		entity.setEmplacementLFlash(dto.getCodeDocument());
		entity.setQteLFlash(dto.getQteLDocument());
		entity.setQte2LFlash(dto.getQte2LDocument());
		entity.setU1LFlash(dto.getU1LDocument());
		entity.setU2LFlash(dto.getU2LDocument());
		
		
		if(entity.getTaLot()!=null) entity.getTaLot().setNumLot(dto.getNumLot());
		if(entity.getTaEntrepot()!=null) entity.getTaEntrepot().setCodeEntrepot(dto.getCodeEntrepot());
		if(entity.getTaArticle()!=null) entity.getTaArticle().setCodeArticle(dto.getCodeArticle());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaLFlashDTO mapEntityToDto(TaLFlash entity, TaLFlashDTO dto) {
		if(dto==null)
			dto = new TaLFlashDTO();

		dto.setId(entity.getIdLFlash());
		
//		dto.setNumLigneLDocument(entity.getNumLigneLDocument());
		dto.setLibLDocument(entity.getLibLFlash());
		dto.setQteLDocument(entity.getQteLFlash());
		dto.setQte2LDocument(entity.getQte2LFlash());
		dto.setEmplacement(entity.getEmplacementLFlash());
//		dto.setPrixULDocument(entity.getPrixULDocument());
//		dto.setMtHtLDocument(entity.getMtHtLDocument());
//		dto.setMtTtcLDocument(entity.getMtTtcLDocument());
//		dto.setMtHtLDocument(entity.getMtHtLDocument());
		
		if(entity.getTaArticle()!=null) {
			dto.setCodeArticle(entity.getTaArticle().getCodeArticle());
		}
		if(entity.getTaLot()!=null) {
			dto.setNumLot(entity.getTaLot().getNumLot());
		}
		if(entity.getTaEntrepot()!=null) {
			dto.setCodeEntrepot(entity.getTaEntrepot().getCodeEntrepot());
		}
		if(entity.getTaFlash()!=null) {
			dto.setCodeDocument(entity.getTaFlash().getCodeFlash());
		}
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
