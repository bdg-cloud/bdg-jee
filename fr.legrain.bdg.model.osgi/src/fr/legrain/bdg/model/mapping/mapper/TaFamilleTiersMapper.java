package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.tiers.dto.TaFamilleTiersDTO;
import fr.legrain.tiers.model.TaFamilleTiers;


public class TaFamilleTiersMapper implements ILgrMapper<TaFamilleTiersDTO, TaFamilleTiers> {

	@Override
	public TaFamilleTiers mapDtoToEntity(TaFamilleTiersDTO dto, TaFamilleTiers entity) {
		if(entity==null)
			entity = new TaFamilleTiers();

		entity.setIdFamille(dto.getId()!=null?dto.getId():0);
		entity.setCodeFamille(dto.getCodeFamille());
		entity.setLiblFamille(dto.getLiblFamille());
		entity.setLibcFamille(dto.getLibcFamille());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaFamilleTiersDTO mapEntityToDto(TaFamilleTiers entity, TaFamilleTiersDTO dto) {
		if(dto==null)
			dto = new TaFamilleTiersDTO();

		dto.setId(entity.getIdFamille());
		dto.setCodeFamille(entity.getCodeFamille());
		dto.setLiblFamille(entity.getLiblFamille());
		dto.setLibcFamille(entity.getLibcFamille());	
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
