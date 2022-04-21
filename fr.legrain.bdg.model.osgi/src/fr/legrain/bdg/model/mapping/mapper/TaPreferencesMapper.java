package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.dossier.dto.TaPreferencesDTO;
import fr.legrain.dossier.model.TaPreferences;


public class TaPreferencesMapper implements ILgrMapper<TaPreferencesDTO, TaPreferences> {

	@Override
	public TaPreferences mapDtoToEntity(TaPreferencesDTO dto, TaPreferences entity) {
		if(entity==null)
			entity = new TaPreferences();

		entity.setIdPreferences(dto.getId()!=null?dto.getId():0);
		
		entity.setCle(dto.getCle());
		entity.setValeur(dto.getValeur());
		entity.setValeurDefaut(dto.getValeurDefaut());
		entity.setTypeDonnees(dto.getTypeDonnees());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaPreferencesDTO mapEntityToDto(TaPreferences entity, TaPreferencesDTO dto) {
		if(dto==null)
			dto = new TaPreferencesDTO();

		dto.setId(entity.getIdPreferences());
		
		dto.setCle(entity.getCle());
		dto.setValeur(entity.getValeur());
		dto.setValeurDefaut(entity.getValeurDefaut());
		dto.setTypeDonnees(entity.getTypeDonnees());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
