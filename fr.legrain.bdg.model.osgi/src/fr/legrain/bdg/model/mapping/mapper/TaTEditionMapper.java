package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.edition.dto.TaTEditionDTO;
import fr.legrain.edition.model.TaTEdition;


public class TaTEditionMapper implements ILgrMapper<TaTEditionDTO, TaTEdition> {

	@Override
	public TaTEdition mapDtoToEntity(TaTEditionDTO dto, TaTEdition entity) {
		if(entity==null)
			entity = new TaTEdition();

		entity.setIdTEdition(dto.getId()!=null?dto.getId():0);
		
		entity.setCodeTEdition(dto.getCodeTEdition());
		entity.setDescription(dto.getDescription());
		entity.setLibelle(dto.getLibelle());
		entity.setSysteme(dto.isSysteme());
//		entity.setPaysAdresse(dto.getPaysAdresse());
//		entity.setVilleAdresse(dto.getVilleAdresse());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaTEditionDTO mapEntityToDto(TaTEdition entity, TaTEditionDTO dto) {
		if(dto==null)
			dto = new TaTEditionDTO();

		dto.setId(entity.getIdTEdition());
		
		dto.setCodeTEdition(entity.getCodeTEdition());
		dto.setLibelle(entity.getLibelle());
		dto.setDescription(entity.getDescription());
		dto.setSysteme(entity.isSysteme());
//		dto.setPaysAdresse(entity.getPaysAdresse());
//		dto.setVilleAdresse(entity.getVilleAdresse());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
