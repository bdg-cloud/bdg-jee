package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.article.dto.TaTTransportDTO;
import fr.legrain.article.model.TaTTransport;
import fr.legrain.bdg.model.mapping.ILgrMapper;



public class TaTTransportMapper implements ILgrMapper<TaTTransportDTO, TaTTransport> {

	@Override
	public TaTTransport mapDtoToEntity(TaTTransportDTO dto, TaTTransport entity) {
		if(entity==null)
			entity = new TaTTransport();

		entity.setIdTTransport(dto.getId()!=null?dto.getId():0);
		entity.setCodeTTransport(dto.getCodeTTransport());
		entity.setLiblTTransport(dto.getLiblTTransport());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaTTransportDTO mapEntityToDto(TaTTransport entity, TaTTransportDTO dto) {
		if(dto==null)
			dto = new TaTTransportDTO();

		dto.setId(entity.getIdTTransport());
		dto.setCodeTTransport(entity.getCodeTTransport());
		dto.setLiblTTransport(entity.getLiblTTransport());	
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
