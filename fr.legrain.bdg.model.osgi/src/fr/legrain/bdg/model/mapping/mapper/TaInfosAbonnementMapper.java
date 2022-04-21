package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.document.dto.TaAbonnementDTO;
import fr.legrain.document.model.TaInfosAbonnement;


public class TaInfosAbonnementMapper implements ILgrMapper<TaAbonnementDTO, TaInfosAbonnement> {

	@Override
	public TaInfosAbonnement mapDtoToEntity(TaAbonnementDTO dto, TaInfosAbonnement entity) {
		if(entity==null)
			entity = new TaInfosAbonnement();

//		entity.setIdLDocument(dto.getIdLDocument()!=null?dto.getIdLDocument():0);
		
//		entity.setLibelleDocument(dto.getLibelleDocument());
//		entity.setCodeDocument(dto.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaAbonnementDTO mapEntityToDto(TaInfosAbonnement entity, TaAbonnementDTO dto) {
		if(dto==null)
			dto = new TaAbonnementDTO();

//		dto.setIdLDocument(entity.getIdLDocument());
		
//		dto.setLibelleDocument(entity.getLibelleDocument());
//		dto.setCodeDocument(entity.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
