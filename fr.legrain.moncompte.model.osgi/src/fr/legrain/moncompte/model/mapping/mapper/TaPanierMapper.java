package fr.legrain.moncompte.model.mapping.mapper;


import fr.legrain.moncompte.dto.TaPanierDTO;
import fr.legrain.moncompte.model.TaPanier;
import fr.legrain.moncompte.model.mapping.ILgrMapper;


public class TaPanierMapper implements ILgrMapper<TaPanierDTO, TaPanier> {

	@Override
	public TaPanier mapDtoToEntity(TaPanierDTO dto, TaPanier entity) {
		if(entity==null)
			entity = new TaPanier();

		entity.setIdPanier(dto.getId()!=null?dto.getId():0);
		
//		entity.setLibelleDocument(dto.getLibelleDocument());
//		entity.setCodeDocument(dto.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaPanierDTO mapEntityToDto(TaPanier entity, TaPanierDTO dto) {
		if(dto==null)
			dto = new TaPanierDTO();

		dto.setId(entity.getIdPanier());
		
//		dto.setLibelleDocument(entity.getLibelleDocument());
//		dto.setCodeDocument(entity.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
