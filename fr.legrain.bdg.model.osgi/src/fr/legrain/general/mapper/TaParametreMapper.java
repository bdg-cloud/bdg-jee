package fr.legrain.general.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.general.dto.TaParametreDTO;
import fr.legrain.general.model.TaParametre;



public class TaParametreMapper implements ILgrMapper<TaParametreDTO, TaParametre> {

	@Override
	public TaParametre mapDtoToEntity(TaParametreDTO dto, TaParametre entity) {
		if(entity==null)
			entity = new TaParametre();
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
	public TaParametreDTO mapEntityToDto(TaParametre entity, TaParametreDTO dto) {
		if(dto==null)
			dto = new TaParametreDTO();
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
