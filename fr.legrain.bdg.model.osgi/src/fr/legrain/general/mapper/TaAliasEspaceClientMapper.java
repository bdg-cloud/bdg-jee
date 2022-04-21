package fr.legrain.general.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.general.dto.TaAliasEspaceClientDTO;
import fr.legrain.general.model.TaAliasEspaceClient;



public class TaAliasEspaceClientMapper implements ILgrMapper<TaAliasEspaceClientDTO, TaAliasEspaceClient> {

	@Override
	public TaAliasEspaceClient mapDtoToEntity(TaAliasEspaceClientDTO dto, TaAliasEspaceClient entity) {
		if(entity==null)
			entity = new TaAliasEspaceClient();
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
	public TaAliasEspaceClientDTO mapEntityToDto(TaAliasEspaceClient entity, TaAliasEspaceClientDTO dto) {
		if(dto==null)
			dto = new TaAliasEspaceClientDTO();
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
