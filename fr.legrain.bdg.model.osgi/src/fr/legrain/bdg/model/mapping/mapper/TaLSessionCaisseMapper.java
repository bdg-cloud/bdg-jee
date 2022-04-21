package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.caisse.dto.TaLSessionCaisseDTO;
import fr.legrain.caisse.model.TaLSessionCaisse;


public class TaLSessionCaisseMapper implements ILgrMapper<TaLSessionCaisseDTO, TaLSessionCaisse> {

	@Override
	public TaLSessionCaisse mapDtoToEntity(TaLSessionCaisseDTO dto, TaLSessionCaisse entity) {
		if(entity==null)
			entity = new TaLSessionCaisse();

		entity.setIdLSessionCaisse(dto.getId()!=null?dto.getId():0);
//		entity.setAdresseWeb(dto.getAdresseWeb()); 
//	
//		if(entity.getTaTWeb()!=null) entity.getTaTWeb().setCodeTWeb(dto.getCodeTWeb());
//		if(entity.getTaTiers()!=null) entity.getTaTiers().setCodeTiers(dto.getCodeTiers());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;		
	}

	@Override
	public TaLSessionCaisseDTO mapEntityToDto(TaLSessionCaisse entity, TaLSessionCaisseDTO dto) {
		if(dto==null)
			dto = new TaLSessionCaisseDTO();

		dto.setId(entity.getIdLSessionCaisse());
		
//		dto.setAdresseWeb(entity.getAdresseWeb()); 
//		if(entity.getTaTWeb()!=null) dto.setCodeTWeb(entity.getTaTWeb().getCodeTWeb());
//		if(entity.getTaTiers()!=null) dto.setCodeTiers(entity.getTaTiers().getCodeTiers());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
