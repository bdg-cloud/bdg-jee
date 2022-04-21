package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.stock.dto.GrMouvStockDTO;
import fr.legrain.stock.model.TaGrMouvStock;


public class TaGrMouvStockMapper implements ILgrMapper<GrMouvStockDTO, TaGrMouvStock> {

	@Override
	public TaGrMouvStock mapDtoToEntity(GrMouvStockDTO dto, TaGrMouvStock entity) {
		if(entity==null)
			entity = new TaGrMouvStock();

		entity.setIdGrMouvStock(dto.getId()!=null?dto.getId():0);
		entity.setCodeGrMouvStock(dto.getCodeGrMouvStock());
		entity.setCommentaire(dto.getCommentaire());
		entity.setDateGrMouvStock(dto.getDateGrMouvStock());
		entity.setCommentaire(dto.getCommentaire());
		entity.setLibelleGrMouvStock(dto.getLibelleGrMouvStock());
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public GrMouvStockDTO mapEntityToDto(TaGrMouvStock entity, GrMouvStockDTO dto) {
		if(dto==null)
			dto = new GrMouvStockDTO();

		dto.setId(entity.getIdGrMouvStock());
		dto.setCodeGrMouvStock(entity.getCodeGrMouvStock());
		dto.setCommentaire(entity.getCommentaire());
		dto.setDateGrMouvStock(entity.getDateGrMouvStock());
		dto.setCommentaire(entity.getCommentaire());
		dto.setLibelleGrMouvStock(entity.getLibelleGrMouvStock());
		if(entity.getTaEtat()!=null){
			dto.setCodeEtat(entity.getTaEtat().getCodeEtat());
			dto.setIdEtat(entity.getTaEtat().getIdEtat());
		}
		if(entity.getTaTypeMouvStock()!=null){
			dto.setCodetypeMouvStock(entity.getTaTypeMouvStock().getCode());
			dto.setIdtypeMouvStock(entity.getTaTypeMouvStock().getIdTypeMouvement());
		}

		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
