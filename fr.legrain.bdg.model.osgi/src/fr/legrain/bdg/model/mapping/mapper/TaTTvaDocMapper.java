package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.tiers.dto.TaTTvaDocDTO;
import fr.legrain.tiers.model.TaTTvaDoc;


public class TaTTvaDocMapper implements ILgrMapper<TaTTvaDocDTO, TaTTvaDoc> {

	@Override
	public TaTTvaDoc mapDtoToEntity(TaTTvaDocDTO dto, TaTTvaDoc entity) {
		if(entity==null)
			entity = new TaTTvaDoc();

		entity.setIdTTvaDoc(dto.getId()!=null?dto.getId():0);
		entity.setCodeTTvaDoc(dto.getCodeTTvaDoc());
		entity.setJournalTTvaDoc(dto.getJournalTTvaDoc());
		entity.setLibelleEdition(dto.getLibelleEdition());	
		entity.setLibelleTTvaDoc(dto.getLibelleTTvaDoc());	
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaTTvaDocDTO mapEntityToDto(TaTTvaDoc entity, TaTTvaDocDTO dto) {
		if(dto==null)
			dto = new TaTTvaDocDTO();

		dto.setId(entity.getIdTTvaDoc());
		dto.setCodeTTvaDoc(entity.getCodeTTvaDoc());
		dto.setJournalTTvaDoc(entity.getJournalTTvaDoc());
		dto.setLibelleEdition(entity.getLibelleEdition());	
		dto.setLibelleTTvaDoc(entity.getLibelleTTvaDoc());	
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
