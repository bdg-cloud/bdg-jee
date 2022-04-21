package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.dossier.dto.TaCategoriePreferenceDTO;
import fr.legrain.dossier.model.TaCategoriePreference;


public class TaCategoriePreferenceMapper implements ILgrMapper<TaCategoriePreferenceDTO, TaCategoriePreference> {

	@Override
	public TaCategoriePreference mapDtoToEntity(TaCategoriePreferenceDTO dto, TaCategoriePreference entity) {
		if(entity==null)
			entity = new TaCategoriePreference();

		entity.setIdCategoriePreference(dto.getId()!=null?dto.getId():0);
		
//		entity.setAdresse1Adresse(dto.getAdresse1Adresse());
//		entity.setAdresse2Adresse(dto.getAdresse2Adresse());
//		entity.setAdresse3Adresse(dto.getAdresse3Adresse());
//		entity.setCodepostalAdresse(dto.getCodepostalAdresse());
//		entity.setPaysAdresse(dto.getPaysAdresse());
//		entity.setVilleAdresse(dto.getVilleAdresse());
//		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaCategoriePreferenceDTO mapEntityToDto(TaCategoriePreference entity, TaCategoriePreferenceDTO dto) {
		if(dto==null)
			dto = new TaCategoriePreferenceDTO();

		dto.setId(entity.getIdCategoriePreference());
		
//		dto.setAdresse1Adresse(entity.getAdresse1Adresse());
//		dto.setAdresse2Adresse(entity.getAdresse2Adresse());
//		dto.setAdresse3Adresse(entity.getAdresse3Adresse());
//		dto.setCodepostalAdresse(entity.getCodepostalAdresse());
//		dto.setPaysAdresse(entity.getPaysAdresse());
//		dto.setVilleAdresse(entity.getVilleAdresse());
//		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
