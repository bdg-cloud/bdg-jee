package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.caisse.dto.TaTLSessionCaisseDTO;
import fr.legrain.caisse.model.TaTLSessionCaisse;


public class TaTLSessionCaisseMapper implements ILgrMapper<TaTLSessionCaisseDTO, TaTLSessionCaisse> {

	@Override
	public TaTLSessionCaisse mapDtoToEntity(TaTLSessionCaisseDTO dto, TaTLSessionCaisse entity) {
		if(entity==null)
			entity = new TaTLSessionCaisse();

		entity.setIdTLSessionCaisse(dto.getId()!=null?dto.getId():0);
//		entity.setAdresseWeb(dto.getAdresseWeb()); 
//	
//		if(entity.getTaTWeb()!=null) entity.getTaTWeb().setCodeTWeb(dto.getCodeTWeb());
//		if(entity.getTaTiers()!=null) entity.getTaTiers().setCodeTiers(dto.getCodeTiers());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;		
	}

	@Override
	public TaTLSessionCaisseDTO mapEntityToDto(TaTLSessionCaisse entity, TaTLSessionCaisseDTO dto) {
		if(dto==null)
			dto = new TaTLSessionCaisseDTO();

		dto.setId(entity.getIdTLSessionCaisse());
		
//		dto.setAdresseWeb(entity.getAdresseWeb()); 
//		if(entity.getTaTWeb()!=null) dto.setCodeTWeb(entity.getTaTWeb().getCodeTWeb());
//		if(entity.getTaTiers()!=null) dto.setCodeTiers(entity.getTaTiers().getCodeTiers());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
