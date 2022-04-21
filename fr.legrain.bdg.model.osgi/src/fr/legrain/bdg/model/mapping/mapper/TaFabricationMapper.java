package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.article.dto.TaFabricationDTO;
import fr.legrain.article.model.TaFabrication;
import fr.legrain.bdg.model.mapping.ILgrMapper;


public class TaFabricationMapper implements ILgrMapper<TaFabricationDTO, TaFabrication> {

	@Override
	public TaFabrication mapDtoToEntity(TaFabricationDTO dto, TaFabrication entity) {
		if(entity==null)
			entity = new TaFabrication();

		entity.setIdDocument(dto.getId()!=null?dto.getId():0);
		
		entity.setCodeDocument(dto.getCodeDocument());
		entity.setCommentaire(dto.getCommentaire());
		entity.setDateDebR(dto.getDateDebR());
		entity.setDateDebT(dto.getDateDebT());
		entity.setDateFinR(dto.getDateFinR());
		entity.setDateFinT(dto.getDateFinT());
		entity.setLibelleDocument(dto.getLibelleDocument());

		
		
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaFabricationDTO mapEntityToDto(TaFabrication entity, TaFabricationDTO dto) {
		if(dto==null)
			dto = new TaFabricationDTO();

		dto.setId(entity.getIdDocument());
		dto.setCommentaire(entity.getCommentaire());
		dto.setDateDebR(entity.getDateDebR());
		dto.setDateDebT(entity.getDateDebT());
		dto.setDateFinR(entity.getDateFinR());
		dto.setDateFinT(entity.getDateFinT());
		dto.setLibelleDocument(entity.getLibelleDocument());
		dto.setCodeDocument(entity.getCodeDocument());
		
		
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
