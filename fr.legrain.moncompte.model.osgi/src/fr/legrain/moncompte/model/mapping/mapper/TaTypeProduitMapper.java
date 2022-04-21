package fr.legrain.moncompte.model.mapping.mapper;


import fr.legrain.moncompte.dto.TaTypeProduitDTO;
import fr.legrain.moncompte.model.TaTypeProduit;
import fr.legrain.moncompte.model.mapping.ILgrMapper;


public class TaTypeProduitMapper implements ILgrMapper<TaTypeProduitDTO, TaTypeProduit> {

	@Override
	public TaTypeProduit mapDtoToEntity(TaTypeProduitDTO dto, TaTypeProduit entity) {
		if(entity==null)
			entity = new TaTypeProduit();

		entity.setIdTypeProduit(dto.getId()!=null?dto.getId():0);
		
//		entity.setLibelleDocument(dto.getLibelleDocument());
//		entity.setCodeDocument(dto.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaTypeProduitDTO mapEntityToDto(TaTypeProduit entity, TaTypeProduitDTO dto) {
		if(dto==null)
			dto = new TaTypeProduitDTO();

		dto.setId(entity.getIdTypeProduit());
		
//		dto.setLibelleDocument(entity.getLibelleDocument());
//		dto.setCodeDocument(entity.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
