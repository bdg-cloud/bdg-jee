package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.article.dto.TaModeleFabricationDTO;
import fr.legrain.article.model.TaModeleFabrication;
import fr.legrain.bdg.model.mapping.ILgrMapper;


public class TaModeleFabricationMapper implements ILgrMapper<TaModeleFabricationDTO, TaModeleFabrication> {

	@Override
	public TaModeleFabrication mapDtoToEntity(TaModeleFabricationDTO dto, TaModeleFabrication entity) {
		if(entity==null)
			entity = new TaModeleFabrication();

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
	public TaModeleFabricationDTO mapEntityToDto(TaModeleFabrication entity, TaModeleFabricationDTO dto) {
		if(dto==null)
			dto = new TaModeleFabricationDTO();

		dto.setId(entity.getIdDocument());
		dto.setCommentaire(entity.getCommentaire());
		dto.setDateDebR(entity.getDateDebR());
		dto.setDateDebT(entity.getDateDebT());
		dto.setDateFinR(entity.getDateFinR());
		dto.setDateFinT(entity.getDateFinT());
		dto.setLibelleDocument(entity.getLibelleDocument());
		dto.setCodeDocument(entity.getCodeDocument());
		
		
		
		if(entity.getTaTFabrication()!=null) dto.setCodeTFabrication(entity.getTaTFabrication().getCodeTFabrication());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
