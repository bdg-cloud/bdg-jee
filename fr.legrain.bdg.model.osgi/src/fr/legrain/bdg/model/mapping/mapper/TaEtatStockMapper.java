package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.stock.dto.EtatStocksDTO;
import fr.legrain.stock.model.TaEtatStock;


public class TaEtatStockMapper implements ILgrMapper<EtatStocksDTO, TaEtatStock> {



	@Override
	public TaEtatStock mapDtoToEntity(EtatStocksDTO dto, TaEtatStock entity) {
		if(entity==null)
			entity = new TaEtatStock();

		entity.setIdEtatStock(dto.getId()!=null?dto.getId():0);
		

		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public EtatStocksDTO mapEntityToDto(TaEtatStock entity, EtatStocksDTO dto) {
		if(dto==null)
			dto = new EtatStocksDTO();

		dto.setId(entity.getIdEtatStock());
		

		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}


}
