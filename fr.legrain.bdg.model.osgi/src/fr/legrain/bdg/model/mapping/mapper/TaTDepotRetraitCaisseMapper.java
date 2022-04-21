package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.caisse.dto.TaTDepotRetraitCaisseDTO;
import fr.legrain.caisse.model.TaTDepotRetraitCaisse;


public class TaTDepotRetraitCaisseMapper implements ILgrMapper<TaTDepotRetraitCaisseDTO, TaTDepotRetraitCaisse> {

	@Override
	public TaTDepotRetraitCaisse mapDtoToEntity(TaTDepotRetraitCaisseDTO dto, TaTDepotRetraitCaisse entity) {
		if(entity==null)
			entity = new TaTDepotRetraitCaisse();

		entity.setIdTDepotRetraitCaisse(dto.getId()!=null?dto.getId():0);
//		entity.setAdresseWeb(dto.getAdresseWeb()); 
//	
//		if(entity.getTaTWeb()!=null) entity.getTaTWeb().setCodeTWeb(dto.getCodeTWeb());
//		if(entity.getTaTiers()!=null) entity.getTaTiers().setCodeTiers(dto.getCodeTiers());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;		
	}

	@Override
	public TaTDepotRetraitCaisseDTO mapEntityToDto(TaTDepotRetraitCaisse entity, TaTDepotRetraitCaisseDTO dto) {
		if(dto==null)
			dto = new TaTDepotRetraitCaisseDTO();

		dto.setId(entity.getIdTDepotRetraitCaisse());
		
//		dto.setAdresseWeb(entity.getAdresseWeb()); 
//		if(entity.getTaTWeb()!=null) dto.setCodeTWeb(entity.getTaTWeb().getCodeTWeb());
//		if(entity.getTaTiers()!=null) dto.setCodeTiers(entity.getTaTiers().getCodeTiers());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
