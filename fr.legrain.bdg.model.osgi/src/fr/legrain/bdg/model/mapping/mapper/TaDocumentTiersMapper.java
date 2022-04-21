package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.tiers.dto.TaDocumentTiersDTO;
import fr.legrain.tiers.model.TaDocumentTiers;


public class TaDocumentTiersMapper implements ILgrMapper<TaDocumentTiersDTO, TaDocumentTiers> {

	@Override
	public TaDocumentTiers mapDtoToEntity(TaDocumentTiersDTO dto, TaDocumentTiers entity) {
		if(entity==null)
			entity = new TaDocumentTiers();

		entity.setIdDocumentTiers(dto.getId()!=null?dto.getId():0);
		entity.setCodeDocumentTiers(dto.getCodeDocumentTiers());
		entity.setLibelleDocumentTiers(dto.getLibelleDocumentTiers());
		
		entity.setActif(LibConversion.booleanToInt(dto.getActif()));
		entity.setCheminCorrespDocumentTiers(dto.getCheminCorrespDocumentTiers());
		entity.setCheminModelDocumentTiers(dto.getCheminModelDocumentTiers());
		entity.setDefaut(LibConversion.booleanToInt(dto.getDefaut()));
		entity.setTypeLogiciel(dto.getTypeLogiciel());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaDocumentTiersDTO mapEntityToDto(TaDocumentTiers entity, TaDocumentTiersDTO dto) {
		if(dto==null)
			dto = new TaDocumentTiersDTO();

		dto.setId(entity.getIdDocumentTiers());
		dto.setCodeDocumentTiers(entity.getCodeDocumentTiers());
		dto.setLibelleDocumentTiers(entity.getLibelleDocumentTiers());	
		
		dto.setActif(LibConversion.intToBoolean(entity.getActif()));
		dto.setCheminCorrespDocumentTiers(entity.getCheminCorrespDocumentTiers());
		dto.setCheminModelDocumentTiers(entity.getCheminModelDocumentTiers());
		dto.setDefaut(dto.getDefaut());
		dto.setTypeLogiciel(entity.getTypeLogiciel());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
