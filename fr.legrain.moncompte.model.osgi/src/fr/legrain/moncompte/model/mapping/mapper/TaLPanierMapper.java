package fr.legrain.moncompte.model.mapping.mapper;


import fr.legrain.moncompte.dto.TaLPanierDTO;
import fr.legrain.moncompte.model.TaLignePanier;
import fr.legrain.moncompte.model.mapping.ILgrMapper;


public class TaLPanierMapper implements ILgrMapper<TaLPanierDTO, TaLignePanier> {

	@Override
	public TaLignePanier mapDtoToEntity(TaLPanierDTO dto, TaLignePanier entity) {
		if(entity==null)
			entity = new TaLignePanier();

		entity.setIdLignePanier(dto.getId()!=null?dto.getId():0);
		
//		entity.setLibelleDocument(dto.getLibelleDocument());
//		entity.setCodeDocument(dto.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaLPanierDTO mapEntityToDto(TaLignePanier entity, TaLPanierDTO dto) {
		if(dto==null)
			dto = new TaLPanierDTO();

		dto.setId(entity.getIdLignePanier());
		
//		dto.setLibelleDocument(entity.getLibelleDocument());
//		dto.setCodeDocument(entity.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
