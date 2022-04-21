package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.document.dto.TaLAbonnementDTO;
import fr.legrain.document.model.TaLAbonnement;
import fr.legrain.document.model.TaLAbonnement;


public class TaLAbonnementMapper implements ILgrMapper<TaLAbonnementDTO, TaLAbonnement> {

	@Override
	public TaLAbonnement mapDtoToEntity(TaLAbonnementDTO dto, TaLAbonnement entity) {
		if(entity==null)
			entity = new TaLAbonnement();

		entity.setIdLDocument(dto.getIdLDocument()!=null?dto.getIdLDocument():0);
		
//		entity.setLibelleDocument(dto.getLibelleDocument());
//		entity.setCodeDocument(dto.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaLAbonnementDTO mapEntityToDto(TaLAbonnement entity, TaLAbonnementDTO dto) {
		if(dto==null)
			dto = new TaLAbonnementDTO();

		dto.setIdLDocument(entity.getIdLDocument());
		
//		dto.setLibelleDocument(entity.getLibelleDocument());
//		dto.setCodeDocument(entity.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
