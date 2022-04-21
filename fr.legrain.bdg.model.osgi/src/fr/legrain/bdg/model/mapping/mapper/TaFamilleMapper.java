package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.article.dto.TaFamilleDTO;
import fr.legrain.article.model.TaFamille;
import fr.legrain.bdg.model.mapping.ILgrMapper;


public class TaFamilleMapper implements ILgrMapper<TaFamilleDTO, TaFamille> {

	@Override
	public TaFamille mapDtoToEntity(TaFamilleDTO dto, TaFamille entity) {
		if(entity==null)
			entity = new TaFamille();

		entity.setIdFamille(dto.getId()!=null?dto.getId():0);
		
		entity.setCodeFamille(dto.getCodeFamille());
		entity.setLibcFamille(dto.getLibcFamille());
		entity.setLiblFamille(dto.getLiblFamille());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaFamilleDTO mapEntityToDto(TaFamille entity, TaFamilleDTO dto) {
		if(dto==null)
			dto = new TaFamilleDTO();

		dto.setId(entity.getIdFamille());
		
		dto.setCodeFamille(entity.getCodeFamille());
		dto.setLibcFamille(entity.getLibcFamille());
		dto.setLiblFamille(entity.getLiblFamille());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
