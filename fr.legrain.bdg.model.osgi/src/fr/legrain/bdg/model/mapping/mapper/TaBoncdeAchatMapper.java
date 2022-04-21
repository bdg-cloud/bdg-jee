package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.document.dto.TaBoncdeAchatDTO;
import fr.legrain.document.model.TaBoncdeAchat;
import fr.legrain.document.model.TaBoncdeAchat;
import fr.legrain.document.model.TaBoncdeAchat;
import fr.legrain.document.model.TaBoncdeAchat;


public class TaBoncdeAchatMapper implements ILgrMapper<TaBoncdeAchatDTO, TaBoncdeAchat> {

	@Override
	public TaBoncdeAchat mapDtoToEntity(TaBoncdeAchatDTO dto, TaBoncdeAchat entity) {
		if(entity==null)
			entity = new TaBoncdeAchat();

		entity.setIdDocument(dto.getId()!=null?dto.getId():0);
		
		entity.setLibelleDocument(dto.getLibelleDocument());
		entity.setCodeDocument(dto.getCodeDocument());
		
		if(entity.getTaTiers()!=null) entity.getTaTiers().setCodeTiers(dto.getCodeTiers());
//		if(entity.getTaTransporteur()!=null) entity.getTaTransporteur().setCodeTransporteur(dto.getCodeTransporteur());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaBoncdeAchatDTO mapEntityToDto(TaBoncdeAchat entity, TaBoncdeAchatDTO dto) {
		if(dto==null)
			dto = new TaBoncdeAchatDTO();

		dto.setId(entity.getIdDocument());
		
		dto.setLibelleDocument(entity.getLibelleDocument());
		dto.setCodeDocument(entity.getCodeDocument());
		if (entity.getTaTiers()!= null) {
		dto.setCodeTiers(entity.getTaTiers().getCodeTiers());
		}
		
		if(entity.getTaTiers()!=null) dto.setCodeTiers(entity.getTaTiers().getCodeTiers());
//		if(entity.getTaTransporteur()!=null) dto.setCodeTransporteur(entity.getTaTransporteur().getCodeTransporteur());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
