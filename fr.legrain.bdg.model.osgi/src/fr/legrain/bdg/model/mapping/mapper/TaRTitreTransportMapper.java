package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.article.dto.TaRTitreTransportDTO;
import fr.legrain.article.model.TaRTaTitreTransport;
import fr.legrain.bdg.model.mapping.ILgrMapper;


public class TaRTitreTransportMapper implements ILgrMapper<TaRTitreTransportDTO, TaRTaTitreTransport> {

	@Override
	public TaRTaTitreTransport mapDtoToEntity(TaRTitreTransportDTO dto, TaRTaTitreTransport entity) {
		if(entity==null)
			entity = new TaRTaTitreTransport();

		entity.setIdRTitreTransport(dto.getId()!=null?dto.getId():0);
		if(entity.getTaArticle()!=null) entity.getTaArticle().setCodeArticle(dto.getCodeArticle());
		if(entity.getTaTitreTransport()!=null) entity.getTaTitreTransport().setCodeTitreTransport(dto.getCodeTitreTransport());
		if(entity.getTaUnite()!=null) entity.getTaUnite().setCodeUnite(dto.getCodeUnite());
		entity.setQteTitreTransport(dto.getQteTitreTransport());
		
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaRTitreTransportDTO mapEntityToDto(TaRTaTitreTransport entity, TaRTitreTransportDTO dto) {
		if(dto==null)
			dto = new TaRTitreTransportDTO();

		dto.setId(entity.getIdRTitreTransport());
		
		if(entity.getTaArticle()!=null) dto.setCodeArticle(entity.getTaArticle().getCodeArticle());
		if(entity.getTaTitreTransport()!=null) dto.setCodeTitreTransport(entity.getTaTitreTransport().getCodeTitreTransport());
		if(entity.getTaUnite()!=null) dto.setCodeUnite(entity.getTaUnite().getCodeUnite());
		
		dto.setQteTitreTransport(entity.getQteTitreTransport());
				
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
