package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.tiers.dto.TaTCiviliteDTO;
import fr.legrain.tiers.model.TaTCivilite;


public class TaTCiviliteMapper implements ILgrMapper<TaTCiviliteDTO, TaTCivilite> {

	@Override
	public TaTCivilite mapDtoToEntity(TaTCiviliteDTO dto, TaTCivilite entity) {
		if(entity==null)
			entity = new TaTCivilite();

		entity.setIdTCivilite(dto.getId()!=null?dto.getId():0);
		entity.setCodeTCivilite(dto.getCodeTCivilite());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaTCiviliteDTO mapEntityToDto(TaTCivilite entity, TaTCiviliteDTO dto) {
		if(dto==null)
			dto = new TaTCiviliteDTO();

		dto.setId(entity.getIdTCivilite());
		dto.setCodeTCivilite(entity.getCodeTCivilite());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
