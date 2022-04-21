package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.document.dto.TaRemiseDTO;
import fr.legrain.document.dto.TaApporteurDTO;
import fr.legrain.document.model.TaApporteur;
import fr.legrain.document.model.TaApporteur;
import fr.legrain.document.model.TaRemise;
import fr.legrain.document.model.TaRemise;


public class TaRemiseMapper implements ILgrMapper<TaRemiseDTO, TaRemise> {

	@Override
	public TaRemise mapDtoToEntity(TaRemiseDTO dto, TaRemise entity) {
		if(entity==null)
			entity = new TaRemise();

		entity.setIdDocument(dto.getId()!=null?dto.getId():0);
		
		entity.setLibelleDocument(dto.getLibelleDocument());
		entity.setCodeDocument(dto.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaRemiseDTO mapEntityToDto(TaRemise entity, TaRemiseDTO dto) {
		if(dto==null)
			dto = new TaRemiseDTO();

		dto.setId(entity.getIdDocument());
		
		dto.setLibelleDocument(entity.getLibelleDocument());
		dto.setCodeDocument(entity.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
