package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.email.dto.TaContactMessageEmailDTO;
import fr.legrain.email.model.TaContactMessageEmail;


public class TaContactMessageEmailMapper implements ILgrMapper<TaContactMessageEmailDTO, TaContactMessageEmail> {

	@Override
	public TaContactMessageEmail mapDtoToEntity(TaContactMessageEmailDTO dto, TaContactMessageEmail entity) {
		if(entity==null)
			entity = new TaContactMessageEmail();

		entity.setIdContactMessageEmail(dto.getId()!=null?dto.getId():0);
		
		entity.setAdresseEmail(dto.getAdresseEmail());

		
		if(entity.getTaTiers()!=null) entity.getTaTiers().setCodeTiers(dto.getCodeTiers());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaContactMessageEmailDTO mapEntityToDto(TaContactMessageEmail entity, TaContactMessageEmailDTO dto) {
		if(dto==null)
			dto = new TaContactMessageEmailDTO();

		dto.setId(entity.getIdContactMessageEmail());
		
		dto.setAdresseEmail(entity.getAdresseEmail());

		
		if(entity.getTaTiers()!=null) dto.setCodeTiers(entity.getTaTiers().getCodeTiers());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
