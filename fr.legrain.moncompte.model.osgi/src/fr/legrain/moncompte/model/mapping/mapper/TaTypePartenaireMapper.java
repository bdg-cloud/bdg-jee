package fr.legrain.moncompte.model.mapping.mapper;


import fr.legrain.moncompte.dto.TaTypePartenaireDTO;
import fr.legrain.moncompte.model.TaTypePartenaire;
import fr.legrain.moncompte.model.TaTypePartenaire;
import fr.legrain.moncompte.model.mapping.ILgrMapper;


public class TaTypePartenaireMapper implements ILgrMapper<TaTypePartenaireDTO, TaTypePartenaire> {

	@Override
	public TaTypePartenaire mapDtoToEntity(TaTypePartenaireDTO dto, TaTypePartenaire entity) {
		if(entity==null)
			entity = new TaTypePartenaire();

		entity.setIdTypePartenaire(dto.getId()!=null?dto.getId():0);
		
//		entity.setLibelleDocument(dto.getLibelleDocument());
//		entity.setCodeDocument(dto.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaTypePartenaireDTO mapEntityToDto(TaTypePartenaire entity, TaTypePartenaireDTO dto) {
		if(dto==null)
			dto = new TaTypePartenaireDTO();

		dto.setId(entity.getIdTypePartenaire());
		
//		dto.setLibelleDocument(entity.getLibelleDocument());
//		dto.setCodeDocument(entity.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
