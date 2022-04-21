package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.article.titretransport.dto.TaTitreTransportDTO;
import fr.legrain.article.titretransport.model.TaTitreTransport;
import fr.legrain.bdg.model.mapping.ILgrMapper;


public class TaTitreTransportMapper implements ILgrMapper<TaTitreTransportDTO, TaTitreTransport> {

	@Override
	public TaTitreTransport mapDtoToEntity(TaTitreTransportDTO dto, TaTitreTransport entity) {
		if(entity==null)
			entity = new TaTitreTransport();

		entity.setIdTitreTransport(dto.getId()!=null?dto.getId():0);
		
		entity.setCodeTitreTransport(dto.getCodeTitreTransport());
		entity.setLibelleTitreTransport(dto.getLibelleTitreTransport());
		entity.setQteMinTitreTransport(dto.getQteMinTitreTransport());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaTitreTransportDTO mapEntityToDto(TaTitreTransport entity, TaTitreTransportDTO dto) {
		if(dto==null)
			dto = new TaTitreTransportDTO();

		dto.setId(entity.getIdTitreTransport());
		
		dto.setCodeTitreTransport(entity.getCodeTitreTransport());
		dto.setLibelleTitreTransport(entity.getLibelleTitreTransport());
		dto.setQteMinTitreTransport(entity.getQteMinTitreTransport());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
