package fr.legrain.general.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.general.dto.TaFichierDTO;
import fr.legrain.general.model.TaFichier;



public class TaFichierMapper implements ILgrMapper<TaFichierDTO, TaFichier> {

	@Override
	public TaFichier mapDtoToEntity(TaFichierDTO dto, TaFichier entity) {
		if(entity==null)
			entity = new TaFichier();
//
//		entity.setIdFichier(dto.getId()!=null?dto.getId():0);
//		entity.setCodeFichier(dto.getCodeFichier());
//		entity.setJournalFichier(dto.getJournalFichier());
//		entity.setLibelleEdition(dto.getLibelleEdition());	
//		entity.setLibelleFichier(dto.getLibelleFichier());	
//		
//		entity.setVersionObj(dto.getVersionObj());
//
		return entity;
		
	}

	@Override
	public TaFichierDTO mapEntityToDto(TaFichier entity, TaFichierDTO dto) {
		if(dto==null)
			dto = new TaFichierDTO();
//
//		dto.setId(entity.getIdFichier());
//		dto.setCodeFichier(entity.getCodeFichier());
//		dto.setJournalFichier(entity.getJournalFichier());
//		dto.setLibelleEdition(entity.getLibelleEdition());	
//		dto.setLibelleFichier(entity.getLibelleFichier());	
//		
//		dto.setVersionObj(entity.getVersionObj());
//
		return dto;
		
	}

}
