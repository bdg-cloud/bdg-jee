package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.caisse.dto.TaDepotRetraitCaisseDTO;
import fr.legrain.caisse.model.TaDepotRetraitCaisse;


public class TaDepotRetraitCaisseMapper implements ILgrMapper<TaDepotRetraitCaisseDTO, TaDepotRetraitCaisse> {

	@Override
	public TaDepotRetraitCaisse mapDtoToEntity(TaDepotRetraitCaisseDTO dto, TaDepotRetraitCaisse entity) {
		if(entity==null)
			entity = new TaDepotRetraitCaisse();

		entity.setIdDepotRetraitCaisse(dto.getId()!=null?dto.getId():0);
//		entity.setAdresseWeb(dto.getAdresseWeb()); 
//	
//		if(entity.getTaTWeb()!=null) entity.getTaTWeb().setCodeTWeb(dto.getCodeTWeb());
//		if(entity.getTaTiers()!=null) entity.getTaTiers().setCodeTiers(dto.getCodeTiers());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;		
	}

	@Override
	public TaDepotRetraitCaisseDTO mapEntityToDto(TaDepotRetraitCaisse entity, TaDepotRetraitCaisseDTO dto) {
		if(dto==null)
			dto = new TaDepotRetraitCaisseDTO();

		dto.setId(entity.getIdDepotRetraitCaisse());
		
//		dto.setAdresseWeb(entity.getAdresseWeb()); 
//		if(entity.getTaTWeb()!=null) dto.setCodeTWeb(entity.getTaTWeb().getCodeTWeb());
//		if(entity.getTaTiers()!=null) dto.setCodeTiers(entity.getTaTiers().getCodeTiers());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
