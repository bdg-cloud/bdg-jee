package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.servicewebexterne.dto.TaServiceWebExterneDTO;
import fr.legrain.servicewebexterne.model.TaServiceWebExterne;


public class TaServiceWebExterneMapper implements ILgrMapper<TaServiceWebExterneDTO, TaServiceWebExterne> {

	@Override
	public TaServiceWebExterne mapDtoToEntity(TaServiceWebExterneDTO dto, TaServiceWebExterne entity) {
		if(entity==null)
			entity = new TaServiceWebExterne();

		entity.setIdServiceWebExterne(dto.getId()!=null?dto.getId():0);
		
		entity.setCodeServiceWebExterne(dto.getCodeServiceWebExterne());
		entity.setDescriptionServiceWebExterne(dto.getDescriptionServiceWebExterne());
		entity.setLibelleServiceWebExterne(dto.getLibelleServiceWebExterne());
		entity.setLogo(dto.getLogo());
		entity.setActif(dto.isActif());
		entity.setUrlEditeur(dto.getUrlEditeur());
		
		if(entity.getTaTServiceWebExterne()!=null) entity.getTaTServiceWebExterne().setCodeTServiceWebExterne(dto.getCodeTServiceWebExterne());
		if(entity.getTaTAuthentification()!=null) entity.getTaTAuthentification().setCodeTAuthentification(dto.getCodeTAuthentification());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaServiceWebExterneDTO mapEntityToDto(TaServiceWebExterne entity, TaServiceWebExterneDTO dto) {
		if(dto==null)
			dto = new TaServiceWebExterneDTO();

		dto.setId(entity.getIdServiceWebExterne());
		
		dto.setCodeServiceWebExterne(entity.getCodeServiceWebExterne());
		dto.setLibelleServiceWebExterne(entity.getLibelleServiceWebExterne());
		dto.setLogo(entity.getLogo());
		dto.setDescriptionServiceWebExterne(entity.getDescriptionServiceWebExterne());
		dto.setActif(entity.isActif());
		dto.setUrlEditeur(entity.getUrlEditeur());
		dto.setUrlGestionService(entity.getUrlGestionService());
		dto.setUrlService(entity.getUrlService());
		dto.setIdModuleBdgAutorisation(entity.getIdModuleBdgAutorisation());
		
		if(entity.getTaTServiceWebExterne()!=null) dto.setCodeTServiceWebExterne(entity.getTaTServiceWebExterne().getCodeTServiceWebExterne());
		if(entity.getTaTAuthentification()!=null) dto.setCodeTAuthentification(entity.getTaTAuthentification().getCodeTAuthentification());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
