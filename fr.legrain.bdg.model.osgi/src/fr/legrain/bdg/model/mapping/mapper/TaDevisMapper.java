package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.document.dto.TaDevisDTO;
import fr.legrain.document.model.TaDevis;
import fr.legrain.lib.data.LibDate;


public class TaDevisMapper implements ILgrMapper<TaDevisDTO, TaDevis> {

	@Override
	public TaDevis mapDtoToEntity(TaDevisDTO dto, TaDevis entity) {
		if(entity==null)
			entity = new TaDevis();

		entity.setIdDocument(dto.getId()!=null?dto.getId():0);
		
		entity.setLibelleDocument(dto.getLibelleDocument());
		entity.setCodeDocument(dto.getCodeDocument());
		
		if(entity.getTaTiers()!=null) entity.getTaTiers().setCodeTiers(dto.getCodeTiers());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaDevisDTO mapEntityToDto(TaDevis entity, TaDevisDTO dto) {
		if(dto==null)
			dto = new TaDevisDTO();

		dto.setId(entity.getIdDocument());
		
		dto.setLibelleDocument(entity.getLibelleDocument());
		dto.setCodeDocument(entity.getCodeDocument());
		dto.setDateDocument(entity.getDateDocument());
		
		if(entity.getTaTiers()!=null) dto.setCodeTiers(entity.getTaTiers().getCodeTiers());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
