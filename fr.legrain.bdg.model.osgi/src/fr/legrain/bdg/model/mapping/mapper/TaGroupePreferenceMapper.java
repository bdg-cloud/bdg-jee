package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.dossier.dto.TaGroupePreferenceDTO;
import fr.legrain.dossier.model.TaGroupePreference;


public class TaGroupePreferenceMapper implements ILgrMapper<TaGroupePreferenceDTO, TaGroupePreference> {

	@Override
	public TaGroupePreference mapDtoToEntity(TaGroupePreferenceDTO dto, TaGroupePreference entity) {
		if(entity==null)
			entity = new TaGroupePreference();

		entity.setIdGroupeArticle(dto.getId()!=null?dto.getId():0);
		
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
	public TaGroupePreferenceDTO mapEntityToDto(TaGroupePreference entity, TaGroupePreferenceDTO dto) {
		if(dto==null)
			dto = new TaGroupePreferenceDTO();

		dto.setId(entity.getIdGroupeArticle());
		
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
