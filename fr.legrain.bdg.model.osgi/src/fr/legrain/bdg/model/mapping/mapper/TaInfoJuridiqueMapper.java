package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.tiers.dto.TaInfoJuridiqueDTO;
import fr.legrain.tiers.model.TaInfoJuridique;


public class TaInfoJuridiqueMapper implements ILgrMapper<TaInfoJuridiqueDTO, TaInfoJuridique> {

	@Override
	public TaInfoJuridique mapDtoToEntity(TaInfoJuridiqueDTO dto, TaInfoJuridique entity) {
		if(entity==null)
			entity = new TaInfoJuridique();

		entity.setIdInfoJuridique(dto.getId()!=null?dto.getId():0);
		entity.setApeInfoJuridique(dto.getApeInfoJuridique());
		entity.setCapitalInfoJuridique(dto.getCapitalInfoJuridique());	
		entity.setRcsInfoJuridique(dto.getRcsInfoJuridique());
		entity.setSiretInfoJuridique(dto.getSiretInfoJuridique());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaInfoJuridiqueDTO mapEntityToDto(TaInfoJuridique entity, TaInfoJuridiqueDTO dto) {
		if(dto==null)
			dto = new TaInfoJuridiqueDTO();

		dto.setId(entity.getIdInfoJuridique());
		dto.setApeInfoJuridique(entity.getApeInfoJuridique());
		dto.setCapitalInfoJuridique(entity.getCapitalInfoJuridique());	
		dto.setRcsInfoJuridique(entity.getRcsInfoJuridique());
		dto.setSiretInfoJuridique(entity.getSiretInfoJuridique());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
