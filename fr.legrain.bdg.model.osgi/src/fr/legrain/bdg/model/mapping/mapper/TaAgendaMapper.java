package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.tache.dto.TaAgendaDTO;
import fr.legrain.tache.model.TaAgenda;


public class TaAgendaMapper implements ILgrMapper<TaAgendaDTO, TaAgenda> {

	@Override
	public TaAgenda mapDtoToEntity(TaAgendaDTO dto, TaAgenda entity) {
		if(entity==null)
			entity = new TaAgenda();

		entity.setIdAgenda(dto.getId()!=null?dto.getId():0);
		
		entity.setNom(dto.getNom());
		entity.setDescription(dto.getDescription());
		entity.setPrive(dto.getPrive());
		entity.setCouleur(dto.getCouleur());
//		entity.setPaysAdresse(dto.getPaysAdresse());
//		entity.setVilleAdresse(dto.getVilleAdresse());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaAgendaDTO mapEntityToDto(TaAgenda entity, TaAgendaDTO dto) {
		if(dto==null)
			dto = new TaAgendaDTO();

		dto.setId(entity.getIdAgenda());
		
		dto.setNom(entity.getNom());
		dto.setPrive(entity.isPrive());
		dto.setDescription(entity.getDescription());
		dto.setCouleur(entity.getCouleur());
//		dto.setPaysAdresse(entity.getPaysAdresse());
//		dto.setVilleAdresse(entity.getVilleAdresse());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
