package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.caisse.dto.TaTFondDeCaisseDTO;
import fr.legrain.caisse.model.TaTFondDeCaisse;


public class TaTFondDeCaisseMapper implements ILgrMapper<TaTFondDeCaisseDTO, TaTFondDeCaisse> {

	@Override
	public TaTFondDeCaisse mapDtoToEntity(TaTFondDeCaisseDTO dto, TaTFondDeCaisse entity) {
		if(entity==null)
			entity = new TaTFondDeCaisse();

		entity.setIdTFondDeCaisse(dto.getId()!=null?dto.getId():0);
//		entity.setAdresseWeb(dto.getAdresseWeb()); 
//	
//		if(entity.getTaTWeb()!=null) entity.getTaTWeb().setCodeTWeb(dto.getCodeTWeb());
//		if(entity.getTaTiers()!=null) entity.getTaTiers().setCodeTiers(dto.getCodeTiers());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;		
	}

	@Override
	public TaTFondDeCaisseDTO mapEntityToDto(TaTFondDeCaisse entity, TaTFondDeCaisseDTO dto) {
		if(dto==null)
			dto = new TaTFondDeCaisseDTO();

		dto.setId(entity.getIdTFondDeCaisse());
		
//		dto.setAdresseWeb(entity.getAdresseWeb()); 
//		if(entity.getTaTWeb()!=null) dto.setCodeTWeb(entity.getTaTWeb().getCodeTWeb());
//		if(entity.getTaTiers()!=null) dto.setCodeTiers(entity.getTaTiers().getCodeTiers());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
