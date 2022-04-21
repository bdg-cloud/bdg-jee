package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.article.dto.TaFamilleUniteDTO;
import fr.legrain.article.model.TaFamilleUnite;
import fr.legrain.bdg.model.mapping.ILgrMapper;


public class TaFamilleUniteMapper implements ILgrMapper<TaFamilleUniteDTO, TaFamilleUnite> {

	@Override
	public TaFamilleUnite mapDtoToEntity(TaFamilleUniteDTO dto, TaFamilleUnite entity) {
		if(entity==null)
			entity = new TaFamilleUnite();

		entity.setIdFamille(dto.getId()!=null?dto.getId():0);
		
		entity.setCodeFamille(dto.getCodeFamille());
		entity.setLibcFamille(dto.getLibcFamille());
		entity.setLiblFamille(dto.getLiblFamille());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaFamilleUniteDTO mapEntityToDto(TaFamilleUnite entity, TaFamilleUniteDTO dto) {
		if(dto==null)
			dto = new TaFamilleUniteDTO();

		dto.setId(entity.getIdFamille());
		
		dto.setCodeFamille(entity.getCodeFamille());
		dto.setLibcFamille(entity.getLibcFamille());
		dto.setLiblFamille(entity.getLiblFamille());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
