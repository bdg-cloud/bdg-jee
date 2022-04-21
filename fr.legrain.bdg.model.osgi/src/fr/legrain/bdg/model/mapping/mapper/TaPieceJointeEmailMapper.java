package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.email.dto.TaPieceJointeEmailDTO;
import fr.legrain.email.model.TaPieceJointeEmail;


public class TaPieceJointeEmailMapper implements ILgrMapper<TaPieceJointeEmailDTO, TaPieceJointeEmail> {

	@Override
	public TaPieceJointeEmail mapDtoToEntity(TaPieceJointeEmailDTO dto, TaPieceJointeEmail entity) {
		if(entity==null)
			entity = new TaPieceJointeEmail();

		entity.setIdLogPieceJointeEmail(dto.getId()!=null?dto.getId():0);
		
		entity.setNomFichier(dto.getNomFichier());
		entity.setFichier(dto.getFichier());
		entity.setFichierImageMiniature(dto.getFichierImageMiniature());
		entity.setTaille(dto.getTaille());
		entity.setTypeMime(dto.getTypeMime());
//		entity.setVilleAdresse(dto.getVilleAdresse());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaPieceJointeEmailDTO mapEntityToDto(TaPieceJointeEmail entity, TaPieceJointeEmailDTO dto) {
		if(dto==null)
			dto = new TaPieceJointeEmailDTO();

		dto.setId(entity.getIdLogPieceJointeEmail());
		
		dto.setNomFichier(entity.getNomFichier());
		dto.setFichier(entity.getFichier());
		dto.setFichierImageMiniature(entity.getFichierImageMiniature());
		dto.setTaille(entity.getTaille());
		dto.setTypeMime(entity.getTypeMime());
//		dto.setPaysAdresse(entity.getPaysAdresse());
//		dto.setVilleAdresse(entity.getVilleAdresse());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
