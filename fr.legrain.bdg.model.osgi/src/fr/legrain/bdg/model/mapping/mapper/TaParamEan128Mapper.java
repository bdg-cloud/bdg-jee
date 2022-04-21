package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.article.dto.TaParamEan128DTO;
import fr.legrain.article.model.TaParamEan128;
import fr.legrain.bdg.model.mapping.ILgrMapper;


public class TaParamEan128Mapper implements ILgrMapper<TaParamEan128DTO, TaParamEan128> {

	@Override
	public TaParamEan128 mapDtoToEntity(TaParamEan128DTO dto, TaParamEan128 entity) {
		if(entity==null) {
			entity = new TaParamEan128();
		}
		mapperModelToUI(dto,entity);
		
		entity.setVersionObj(dto.getVersionObj());
		return entity;
	}

	@Override
	public TaParamEan128DTO mapEntityToDto(TaParamEan128 entity, TaParamEan128DTO dto) {
		if(dto==null) {
			dto = new TaParamEan128DTO();
		}

		mapperUIToModel(entity, dto);
		
		dto.setVersionObj(entity.getVersionObj());
		return dto;
	}


	private void mapperModelToUI(TaParamEan128DTO doc,TaParamEan128 e){
		if(doc.getCode128()!=null)e.setCode128(doc.getCode128());
		if(doc.getLibelle()!=null)e.setLibelle(doc.getLibelle());
		if(doc.getId()!=0)e.setIdParamEan128(doc.getId());
		if(doc.getLongBorneMax()!=null)e.setLongBorneMax(doc.getLongBorneMax());
		if(doc.getLongVariable()!=null)e.setLongVariable(doc.getLongVariable());
		if(doc.getDataType()!=null)e.setDataType(doc.getDataType());
		if(doc.getLongAi()!=null)e.setLongAi(doc.getLongAi());
	}
	
	private void mapperUIToModel(TaParamEan128 e,TaParamEan128DTO doc){
		if(e.getCode128()!=null)doc.setCode128(e.getCode128());
		if(e.getLibelle()!=null)doc.setLibelle(e.getLibelle());
		if(e.getIdParamEan128()!=0)doc.setId(e.getIdParamEan128());
		if(e.getLongBorneMax()!=null)doc.setLongBorneMax(e.getLongBorneMax());
		if(e.getLongAi()!=null)doc.setLongAi(e.getLongAi());
		if(e.getLongVariable()!=null)doc.setLongVariable(e.getLongVariable());
		if(e.getDataType()!=null)doc.setDataType(e.getDataType());
	}
}
