package fr.legrain.bdg.model.mapping.mapper;

import java.util.ArrayList;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.push.dto.TaContactMessagePushDTO;
import fr.legrain.push.dto.TaMessagePushDTO;
import fr.legrain.push.model.TaContactMessagePush;
import fr.legrain.push.model.TaMessagePush;
import fr.legrain.sms.dto.TaContactMessageSMSDTO;
import fr.legrain.sms.model.TaContactMessageSMS;


public class TaMessagePushMapper implements ILgrMapper<TaMessagePushDTO, TaMessagePush> {

	@Override
	public TaMessagePush mapDtoToEntity(TaMessagePushDTO dto, TaMessagePush entity) {
		if(entity==null)
			entity = new TaMessagePush();

		entity.setIdMessagePush(dto.getId()!=null?dto.getId():0);
		
		entity.setSujet(dto.getSujet());
		entity.setResume(dto.getResume());
		entity.setContenu(dto.getContenu());
		entity.setDateCreation(dto.getDateCreation());
		entity.setDateEnvoi(dto.getDateEnvoi());
		
		entity.setLu(dto.isLu());
		entity.setRecu(dto.isRecu());
		entity.setStyle(dto.getStyle());
		entity.setUrl(dto.getUrl());
		entity.setUrlImage(dto.getUrlImage());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaMessagePushDTO mapEntityToDto(TaMessagePush entity, TaMessagePushDTO dto) {
		if(dto==null)
			dto = new TaMessagePushDTO();

		dto.setId(entity.getIdMessagePush());
		
		dto.setSujet(entity.getSujet());
		dto.setContenu(entity.getContenu());
		dto.setResume(entity.getResume());
		dto.setDateCreation(entity.getDateCreation());
		dto.setDateEnvoi(entity.getDateEnvoi());
		
		dto.setLu(entity.isLu());
		dto.setRecu(entity.isRecu());
		dto.setStyle(entity.getStyle());
		dto.setUrl(entity.getUrl());
		dto.setUrlImage(entity.getUrlImage());
		
		
		if(entity.getTaUtilisateur()!=null) dto.setIdUtilisateur(entity.getTaUtilisateur().getId());
		
		if(entity.getDestinataires()!=null) {
			dto.setDestinataires(new ArrayList<>());
			TaContactMessagePushDTO contactDto = null;
			for (TaContactMessagePush c : entity.getDestinataires()) {
				contactDto = new TaContactMessagePushDTO();
				contactDto.setId(c.getIdContactMessagePush());
//				contactDto.setNumeroTelephone(c.getNumeroTelephone());
				if(c.getTaTiers()!=null) {
					contactDto.setIdTiers(c.getTaTiers().getIdTiers());
					contactDto.setNomTiers(c.getTaTiers().getNomTiers());
					contactDto.setPrenomTiers(c.getTaTiers().getPrenomTiers());
					contactDto.setCodeTiers(c.getTaTiers().getCodeTiers());
				}
				dto.getDestinataires().add(contactDto);
			}
		}
		
		
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
