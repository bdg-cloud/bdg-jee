package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.conformite.dto.TaStatusConformiteDTO;
import fr.legrain.conformite.model.TaStatusConformite;



public class TaStatusConformiteMapper implements ILgrMapper<TaStatusConformiteDTO, TaStatusConformite> {

	@Override
	public TaStatusConformite mapDtoToEntity(TaStatusConformiteDTO dto, TaStatusConformite entity) {
		if(entity==null)
			entity = new TaStatusConformite();

		entity.setIdStatusConformite(dto.getId()!=null?dto.getId():0);
		entity.setLibelleStatusConformite(dto.getLibelleStatusConformite());
		entity.setCodeStatusConformite(dto.getCodeStatusConformite()); 
//	
//		if(entity.getTaTWeb()!=null) entity.getTaTWeb().setCodeTWeb(dto.getCodeTWeb());
//		if(entity.getTaTiers()!=null) entity.getTaTiers().setCodeTiers(dto.getCodeTiers());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;		
	}

	@Override
	public TaStatusConformiteDTO mapEntityToDto(TaStatusConformite entity, TaStatusConformiteDTO dto) {
		if(dto==null)
			dto = new TaStatusConformiteDTO();

		dto.setId(entity.getIdStatusConformite());
		
		dto.setLibelleStatusConformite(entity.getLibelleStatusConformite());
		dto.setCodeStatusConformite(entity.getCodeStatusConformite()); 
		
//		dto.setAdresseWeb(entity.getAdresseWeb()); 
//		if(entity.getTaTWeb()!=null) dto.setCodeTWeb(entity.getTaTWeb().getCodeTWeb());
//		if(entity.getTaTiers()!=null) dto.setCodeTiers(entity.getTaTiers().getCodeTiers());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
