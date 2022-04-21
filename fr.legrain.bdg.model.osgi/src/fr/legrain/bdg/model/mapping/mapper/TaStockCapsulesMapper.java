package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.article.titretransport.dto.TaStockCapsulesDTO;
import fr.legrain.article.titretransport.model.TaStockCapsules;
import fr.legrain.bdg.model.mapping.ILgrMapper;


public class TaStockCapsulesMapper implements ILgrMapper<TaStockCapsulesDTO, TaStockCapsules> {

	@Override
	public TaStockCapsules mapDtoToEntity(TaStockCapsulesDTO dto, TaStockCapsules entity) {
		if(entity==null)
			entity = new TaStockCapsules();

		entity.setIdStock(dto.getId()!=null?dto.getId():0);
		
		entity.setDateStock(dto.getDateStock());
		entity.setLibelleStock(dto.getLibelleStock());
		entity.setMouvStock(dto.getMouvStock());
		entity.setQte1Stock(dto.getQte1Stock());
		entity.setQte2Stock(dto.getQte2Stock());
		entity.setUn1Stock(dto.getUn1Stock());
		entity.setUn2Stock(dto.getUn2Stock());
		
//		if(entity.getTaTitreTransport()!=null) entity.getTaTitreTransport().setCodeTitreTransport(dto.getCodeTitreTransport());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaStockCapsulesDTO mapEntityToDto(TaStockCapsules entity, TaStockCapsulesDTO dto) {
		if(dto==null)
			dto = new TaStockCapsulesDTO();

		dto.setId(entity.getIdStock());
		
		dto.setDateStock(entity.getDateStock());
		dto.setLibelleStock(entity.getLibelleStock());
		dto.setMouvStock(entity.getMouvStock());
		dto.setQte1Stock(entity.getQte1Stock());
		dto.setQte2Stock(entity.getQte2Stock());
		dto.setUn1Stock(entity.getUn1Stock());
		dto.setUn2Stock(entity.getUn2Stock());
		
		if(entity.getTaTitreTransport()!=null) dto.setCodeTitreTransport(entity.getTaTitreTransport().getCodeTitreTransport());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
