package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.edition.dto.TaEditionDTO;
import fr.legrain.edition.model.TaEdition;


public class TaEditionMapper implements ILgrMapper<TaEditionDTO, TaEdition> {

	@Override
	public TaEdition mapDtoToEntity(TaEditionDTO dto, TaEdition entity) {
		if(entity==null)
			entity = new TaEdition();

		entity.setIdEdition(dto.getId()!=null?dto.getId():0);
		
		entity.setCodeEdition(dto.getCodeEdition());
		entity.setDescriptionEdition(dto.getDescriptionEdition());
		entity.setLibelleEdition(dto.getLibelleEdition());
		entity.setSysteme(dto.getSysteme());
//		entity.setPaysAdresse(dto.getPaysAdresse());
//		entity.setVilleAdresse(dto.getVilleAdresse());
		
		if(entity.getTaTEdition()!=null) {
			entity.getTaTEdition().setCodeTEdition(dto.getCodeTEdition());
		}
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaEditionDTO mapEntityToDto(TaEdition entity, TaEditionDTO dto) {
		if(dto==null)
			dto = new TaEditionDTO();

		dto.setId(entity.getIdEdition());
		
		dto.setCodeEdition(entity.getCodeEdition());
		dto.setLibelleEdition(entity.getLibelleEdition());
		dto.setDescriptionEdition(entity.getDescriptionEdition());
		dto.setSysteme(entity.getSysteme());
//		dto.setPaysAdresse(entity.getPaysAdresse());
//		dto.setVilleAdresse(entity.getVilleAdresse());
		
		if(entity.getTaTEdition()!=null) {
			dto.setCodeTEdition(entity.getTaTEdition().getCodeTEdition());
		}
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
