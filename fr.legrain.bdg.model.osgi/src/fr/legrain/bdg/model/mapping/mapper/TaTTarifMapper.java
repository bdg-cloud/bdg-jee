package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.tiers.dto.TaTTarifDTO;
import fr.legrain.tiers.model.TaTTarif;


public class TaTTarifMapper implements ILgrMapper<TaTTarifDTO, TaTTarif> {

	@Override
	public TaTTarif mapDtoToEntity(TaTTarifDTO dto, TaTTarif entity) {
		if(entity==null)
			entity = new TaTTarif();

		entity.setIdTTarif(dto.getId()!=null?dto.getId():0);
		entity.setCodeTTarif(dto.getCodeTTarif());
		entity.setLiblTTarif(dto.getLiblTTarif());
		entity.setPourcentage(LibConversion.booleanToInt(dto.getPourcentage()));
		entity.setSens(LibConversion.booleanToInt(dto.getSens()));
		entity.setValeur(dto.getValeur());		
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaTTarifDTO mapEntityToDto(TaTTarif entity, TaTTarifDTO dto) {
		if(dto==null)
			dto = new TaTTarifDTO();

		dto.setId(entity.getIdTTarif());
		dto.setCodeTTarif(entity.getCodeTTarif());
		dto.setLiblTTarif(entity.getLiblTTarif());	
		dto.setPourcentage(LibConversion.intToBoolean(entity.getPourcentage()));
		dto.setSens(LibConversion.intToBoolean(entity.getSens()));
		dto.setValeur(entity.getValeur());	
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
