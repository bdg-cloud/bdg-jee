package fr.legrain.bdg.model.mapping.mapper;

import java.util.ArrayList;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.email.dto.TaContactMessageEmailDTO;
import fr.legrain.email.model.TaContactMessageEmail;
import fr.legrain.sms.dto.TaContactMessageSMSDTO;
import fr.legrain.sms.dto.TaMessageSMSDTO;
import fr.legrain.sms.model.TaContactMessageSMS;
import fr.legrain.sms.model.TaMessageSMS;


public class TaMessageSMSMapper implements ILgrMapper<TaMessageSMSDTO, TaMessageSMS> {

	@Override
	public TaMessageSMS mapDtoToEntity(TaMessageSMSDTO dto, TaMessageSMS entity) {
		if(entity==null)
			entity = new TaMessageSMS();

		entity.setIdSms(dto.getId()!=null?dto.getId():0);
		
//		entity.setSubject(dto.getSubject());
		entity.setBodyHtml(dto.getBodyHtml());
		entity.setBodyPlain(dto.getBodyPlain());
		entity.setDateCreation(dto.getDateCreation());
		entity.setDateEnvoi(dto.getDateEnvoi());
		
		entity.setAccuseReceptionLu(dto.isAccuseReceptionLu());
		entity.setLu(dto.isLu());
		entity.setRecu(dto.isRecu());
		entity.setSpam(dto.isSpam());
		entity.setSuivi(dto.isSuivi());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaMessageSMSDTO mapEntityToDto(TaMessageSMS entity, TaMessageSMSDTO dto) {
		if(dto==null)
			dto = new TaMessageSMSDTO();

		dto.setId(entity.getIdSms());
		
//		dto.setSubject(entity.getSubject());
		dto.setBodyHtml(entity.getBodyHtml());
		dto.setBodyPlain(entity.getBodyPlain());
		dto.setDateCreation(entity.getDateCreation());
		dto.setDateEnvoi(entity.getDateEnvoi());
		
		dto.setAccuseReceptionLu(entity.isAccuseReceptionLu());
		dto.setLu(entity.isLu());
		dto.setRecu(entity.isRecu());
		dto.setSpam(entity.isSpam());
		dto.setSuivi(entity.isSuivi());
		
		if(entity.getTaUtilisateur()!=null) dto.setIdUtilisateur(entity.getTaUtilisateur().getId());
		
		if(entity.getDestinataires()!=null) {
			dto.setDestinataires(new ArrayList<>());
			TaContactMessageSMSDTO contactDto = null;
			for (TaContactMessageSMS c : entity.getDestinataires()) {
				contactDto = new TaContactMessageSMSDTO();
				contactDto.setId(c.getIdContactMessageSms());
				contactDto.setNumeroTelephone(c.getNumeroTelephone());
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
