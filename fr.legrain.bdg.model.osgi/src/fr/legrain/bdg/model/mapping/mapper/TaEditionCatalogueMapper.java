package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.edition.dto.TaEditionCatalogueDTO;
import fr.legrain.edition.model.TaEditionCatalogue;


public class TaEditionCatalogueMapper implements ILgrMapper<TaEditionCatalogueDTO, TaEditionCatalogue> {

	@Override
	public TaEditionCatalogue mapDtoToEntity(TaEditionCatalogueDTO dto, TaEditionCatalogue entity) {
		if(entity==null)
			entity = new TaEditionCatalogue();

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
	public TaEditionCatalogueDTO mapEntityToDto(TaEditionCatalogue entity, TaEditionCatalogueDTO dto) {
		if(dto==null)
			dto = new TaEditionCatalogueDTO();

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
