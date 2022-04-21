package fr.legrain.moncompte.model.mapping.mapper;


import fr.legrain.moncompte.dto.TaTNiveauDTO;
import fr.legrain.moncompte.model.TaTNiveau;
import fr.legrain.moncompte.model.mapping.ILgrMapper;


public class TaTNiveauMapper implements ILgrMapper<TaTNiveauDTO, TaTNiveau> {

	@Override
	public TaTNiveau mapDtoToEntity(TaTNiveauDTO dto, TaTNiveau entity) {
		if(entity==null)
			entity = new TaTNiveau();

		entity.setIdTNiveau(dto.getId()!=null?dto.getId():0);
		
//		entity.setLibelleDocument(dto.getLibelleDocument());
//		entity.setCodeDocument(dto.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaTNiveauDTO mapEntityToDto(TaTNiveau entity, TaTNiveauDTO dto) {
		if(dto==null)
			dto = new TaTNiveauDTO();

		dto.setId(entity.getIdTNiveau());
		
//		dto.setLibelleDocument(entity.getLibelleDocument());
//		dto.setCodeDocument(entity.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
