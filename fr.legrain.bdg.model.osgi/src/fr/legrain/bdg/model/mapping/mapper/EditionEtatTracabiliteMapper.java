package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.article.dto.EditionEtatTracabiliteDTO;
import fr.legrain.article.model.EditionEtatTracabilite;
import fr.legrain.bdg.model.mapping.ILgrMapper;

/** Créé par Dima **/

public class EditionEtatTracabiliteMapper implements ILgrMapper<EditionEtatTracabiliteDTO, EditionEtatTracabilite> {

	@Override
	public EditionEtatTracabilite mapDtoToEntity(EditionEtatTracabiliteDTO dto, EditionEtatTracabilite entity) {
		if(entity==null)
			entity = new EditionEtatTracabilite();

		return entity;
	}

	@Override
	public EditionEtatTracabiliteDTO mapEntityToDto(EditionEtatTracabilite entity, EditionEtatTracabiliteDTO dto) {
		if(dto==null)
			dto = new EditionEtatTracabiliteDTO();

		return dto;
	}

}
