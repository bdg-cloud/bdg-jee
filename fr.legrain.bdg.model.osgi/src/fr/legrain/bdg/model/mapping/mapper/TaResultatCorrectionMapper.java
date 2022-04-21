package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.conformite.dto.TaResultatCorrectionDTO;
import fr.legrain.conformite.model.TaResultatCorrection;


public class TaResultatCorrectionMapper implements ILgrMapper<TaResultatCorrectionDTO, TaResultatCorrection> {

	@Override
	public TaResultatCorrection mapDtoToEntity(TaResultatCorrectionDTO dto, TaResultatCorrection entity) {
		if(entity==null)
			entity = new TaResultatCorrection();

		entity.setIdResultatCorrection(dto.getId()!=null?dto.getId():0);
//		entity.setAdresseWeb(dto.getAdresseWeb()); 
//	
//		if(entity.getTaTWeb()!=null) entity.getTaTWeb().setCodeTWeb(dto.getCodeTWeb());
//		if(entity.getTaTiers()!=null) entity.getTaTiers().setCodeTiers(dto.getCodeTiers());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;		
	}

	@Override
	public TaResultatCorrectionDTO mapEntityToDto(TaResultatCorrection entity, TaResultatCorrectionDTO dto) {
		if(dto==null)
			dto = new TaResultatCorrectionDTO();

		dto.setId(entity.getIdResultatCorrection());
		
//		dto.setAdresseWeb(entity.getAdresseWeb()); 
//		if(entity.getTaTWeb()!=null) dto.setCodeTWeb(entity.getTaTWeb().getCodeTWeb());
//		if(entity.getTaTiers()!=null) dto.setCodeTiers(entity.getTaTiers().getCodeTiers());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
