package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.tiers.dto.TaTTiersDTO;
import fr.legrain.tiers.model.TaTTiers;


public class TaTTiersMapper implements ILgrMapper<TaTTiersDTO, TaTTiers> {

	@Override
	public TaTTiers mapDtoToEntity(TaTTiersDTO dto, TaTTiers entity) {
		if(entity==null)
			entity = new TaTTiers();

		entity.setIdTTiers(dto.getId()!=null?dto.getId():0);
		entity.setCompteTTiers(dto.getCompteTTiers());        
		entity.setCodeTTiers(dto.getCodeTTiers());            
		entity.setLibelleTTiers(dto.getLibelleTTiers());  
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;		
	}

	@Override
	public TaTTiersDTO mapEntityToDto(TaTTiers entity, TaTTiersDTO dto) {
		if(dto==null)
			dto = new TaTTiersDTO();

		dto.setId(entity.getIdTTiers());
		
		dto.setCompteTTiers(entity.getCompteTTiers());        
		dto.setCodeTTiers(entity.getCodeTTiers());            
		dto.setLibelleTTiers(entity.getLibelleTTiers()); 
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
