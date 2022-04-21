package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.caisse.dto.TaSessionCaisseDTO;
import fr.legrain.caisse.model.TaSessionCaisse;


public class TaSessionCaisseMapper implements ILgrMapper<TaSessionCaisseDTO, TaSessionCaisse> {

	@Override
	public TaSessionCaisse mapDtoToEntity(TaSessionCaisseDTO dto, TaSessionCaisse entity) {
		if(entity==null)
			entity = new TaSessionCaisse();

		entity.setIdSessionCaisse(dto.getId()!=null?dto.getId():0);
//		entity.setAdresseWeb(dto.getAdresseWeb()); 
//	
//		if(entity.getTaTWeb()!=null) entity.getTaTWeb().setCodeTWeb(dto.getCodeTWeb());
//		if(entity.getTaTiers()!=null) entity.getTaTiers().setCodeTiers(dto.getCodeTiers());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;		
	}

	@Override
	public TaSessionCaisseDTO mapEntityToDto(TaSessionCaisse entity, TaSessionCaisseDTO dto) {
		if(dto==null)
			dto = new TaSessionCaisseDTO();

		dto.setId(entity.getIdSessionCaisse());
		
//		dto.setAdresseWeb(entity.getAdresseWeb()); 
//		if(entity.getTaTWeb()!=null) dto.setCodeTWeb(entity.getTaTWeb().getCodeTWeb());
//		if(entity.getTaTiers()!=null) dto.setCodeTiers(entity.getTaTiers().getCodeTiers());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
