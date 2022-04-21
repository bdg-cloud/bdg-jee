package fr.legrain.servicewebexterne.mapper;



import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.servicewebexterne.dto.TaLiaisonServiceExterneDTO;
import fr.legrain.servicewebexterne.model.TaLiaisonServiceExterne;




public class TaLiaisonServiceExterneMapper implements ILgrMapper<TaLiaisonServiceExterneDTO, TaLiaisonServiceExterne> {

	@Override
	public TaLiaisonServiceExterne mapDtoToEntity(TaLiaisonServiceExterneDTO dto, TaLiaisonServiceExterne entity) {
		if(entity==null)
			entity = new TaLiaisonServiceExterne();
		if(dto.getId() != null) {
			entity.setIdLiaisonServiceExterne(dto.getId());
		}
		
		entity.setIdEntite(dto.getIdEntite());
		entity.setRefExterne(dto.getRefExterne());
		entity.setTypeEntite(dto.getTypeEntite());
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
	public TaLiaisonServiceExterneDTO mapEntityToDto(TaLiaisonServiceExterne entity, TaLiaisonServiceExterneDTO dto) {
		if(dto==null)
			dto = new TaLiaisonServiceExterneDTO();
		
		dto.setId(entity.getIdLiaisonServiceExterne());
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
