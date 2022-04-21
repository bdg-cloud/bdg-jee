package fr.legrain.moncompte.model.mapping.mapper;


import fr.legrain.moncompte.dto.TaTypePaiementDTO;
import fr.legrain.moncompte.model.TaTypePaiement;
import fr.legrain.moncompte.model.mapping.ILgrMapper;


public class TaTypePaiementMapper implements ILgrMapper<TaTypePaiementDTO, TaTypePaiement> {

	@Override
	public TaTypePaiement mapDtoToEntity(TaTypePaiementDTO dto, TaTypePaiement entity) {
		if(entity==null)
			entity = new TaTypePaiement();

		entity.setIdTypePaiement(dto.getId()!=null?dto.getId():0);
		
//		entity.setLibelleDocument(dto.getLibelleDocument());
//		entity.setCodeDocument(dto.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaTypePaiementDTO mapEntityToDto(TaTypePaiement entity, TaTypePaiementDTO dto) {
		if(dto==null)
			dto = new TaTypePaiementDTO();

		dto.setId(entity.getIdTypePaiement());
		
//		dto.setLibelleDocument(entity.getLibelleDocument());
//		dto.setCodeDocument(entity.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
