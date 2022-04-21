package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.dossier.dto.TaAutorisationsDossierDTO;
import fr.legrain.dossier.model.TaAutorisationsDossier;


public class TaAutorisationsDossierMapper implements ILgrMapper<TaAutorisationsDossierDTO, TaAutorisationsDossier> {

	@Override
	public TaAutorisationsDossier mapDtoToEntity(TaAutorisationsDossierDTO dto, TaAutorisationsDossier entity) {
		if(entity==null)
			entity = new TaAutorisationsDossier();

		entity.setIdAutorisation(dto.getId()!=null?dto.getId():0);
		
		entity.setFichier(dto.getFichier());
		entity.setDateDerSynchro(dto.getDateDerSynchro());
		
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaAutorisationsDossierDTO mapEntityToDto(TaAutorisationsDossier entity, TaAutorisationsDossierDTO dto) {
		if(dto==null)
			dto = new TaAutorisationsDossierDTO();

		dto.setId(entity.getIdAutorisation());
		
		dto.setDateDerSynchro(entity.getDateDerSynchro());
		dto.setFichier(entity.getFichier());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
