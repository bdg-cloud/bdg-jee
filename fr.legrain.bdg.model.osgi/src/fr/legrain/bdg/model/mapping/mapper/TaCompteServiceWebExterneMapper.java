package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.servicewebexterne.dto.TaCompteServiceWebExterneDTO;
import fr.legrain.servicewebexterne.model.TaCompteServiceWebExterne;


public class TaCompteServiceWebExterneMapper implements ILgrMapper<TaCompteServiceWebExterneDTO, TaCompteServiceWebExterne> {

	@Override
	public TaCompteServiceWebExterne mapDtoToEntity(TaCompteServiceWebExterneDTO dto, TaCompteServiceWebExterne entity) {
		if(entity==null)
			entity = new TaCompteServiceWebExterne();

		entity.setIdCompteServiceWebExterne(dto.getId()!=null?dto.getId():0);
		
		entity.setCodeCompteServiceWebExterne(dto.getCodeCompteServiceWebExterne());
		entity.setLibelleCompteServiceWebExterne(dto.getLibelleCompteServiceWebExterne());
		entity.setDescriptionCompteServiceWebExterne(dto.getDescriptionCompteServiceWebExterne());
		entity.setActif(dto.isActif());
		entity.setDefaut(dto.isDefaut());
//		entity.setPaysAdresse(dto.getPaysAdresse());
//		entity.setVilleAdresse(dto.getVilleAdresse());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaCompteServiceWebExterneDTO mapEntityToDto(TaCompteServiceWebExterne entity, TaCompteServiceWebExterneDTO dto) {
		if(dto==null)
			dto = new TaCompteServiceWebExterneDTO();

		dto.setId(entity.getIdCompteServiceWebExterne());
		
		dto.setCodeCompteServiceWebExterne(entity.getCodeCompteServiceWebExterne());
		dto.setLibelleCompteServiceWebExterne(entity.getLibelleCompteServiceWebExterne());
		dto.setDescriptionCompteServiceWebExterne(entity.getDescriptionCompteServiceWebExterne());
		dto.setActif(entity.isActif());
		dto.setDefaut(entity.isDefaut());
//		dto.setPaysAdresse(entity.getPaysAdresse());
//		dto.setVilleAdresse(entity.getVilleAdresse());
		
		if(entity.getTaServiceWebExterne()!=null) {
			dto.setIdServiceWebExterne(entity.getTaServiceWebExterne().getIdServiceWebExterne());
			dto.setCodeServiceWebExterne(entity.getTaServiceWebExterne().getCodeServiceWebExterne());
			dto.setLogo(entity.getTaServiceWebExterne().getLogo());
			dto.setLibelleServiceWebExterne(entity.getTaServiceWebExterne().getLibelleServiceWebExterne());
			
			dto.setServiceActif(entity.getTaServiceWebExterne().isActif());
		}
		
		if(entity.getTaTAuthentification()!=null) {
			dto.setIdTAuthentification(entity.getTaTAuthentification().getIdTAuthentification());
			dto.setCodeTAuthentification(entity.getTaTAuthentification().getCodeTAuthentification());
		}
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
