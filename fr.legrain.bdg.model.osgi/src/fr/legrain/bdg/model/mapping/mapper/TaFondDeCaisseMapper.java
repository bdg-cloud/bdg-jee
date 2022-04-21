package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.caisse.dto.TaFondDeCaisseDTO;
import fr.legrain.caisse.model.TaFondDeCaisse;


public class TaFondDeCaisseMapper implements ILgrMapper<TaFondDeCaisseDTO, TaFondDeCaisse> {

	@Override
	public TaFondDeCaisse mapDtoToEntity(TaFondDeCaisseDTO dto, TaFondDeCaisse entity) {
		if(entity==null)
			entity = new TaFondDeCaisse();

		entity.setIdFondDeCaisse(dto.getId()!=null?dto.getId():0);
//		entity.setAdresseWeb(dto.getAdresseWeb()); 
//	
//		if(entity.getTaTWeb()!=null) entity.getTaTWeb().setCodeTWeb(dto.getCodeTWeb());
//		if(entity.getTaTiers()!=null) entity.getTaTiers().setCodeTiers(dto.getCodeTiers());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;		
	}

	@Override
	public TaFondDeCaisseDTO mapEntityToDto(TaFondDeCaisse entity, TaFondDeCaisseDTO dto) {
		if(dto==null)
			dto = new TaFondDeCaisseDTO();

		dto.setId(entity.getIdFondDeCaisse());
		
//		dto.setAdresseWeb(entity.getAdresseWeb()); 
//		if(entity.getTaTWeb()!=null) dto.setCodeTWeb(entity.getTaTWeb().getCodeTWeb());
//		if(entity.getTaTiers()!=null) dto.setCodeTiers(entity.getTaTiers().getCodeTiers());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
