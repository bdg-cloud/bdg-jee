package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.article.dto.TaEntrepotDTO;
import fr.legrain.article.model.TaEntrepot;
import fr.legrain.article.model.TaEntrepot;
import fr.legrain.bdg.model.mapping.ILgrMapper;


public class TaEntrepotMapper implements ILgrMapper<TaEntrepotDTO, TaEntrepot> {

	@Override
	public TaEntrepot mapDtoToEntity(TaEntrepotDTO dto, TaEntrepot entity) {
		if(entity==null)
			entity = new TaEntrepot();

		entity.setIdEntrepot(dto.getId()!=null?dto.getId():0);
		
		entity.setLibelle(dto.getLibelle());
		entity.setActif(dto.getTopEffacable());
		entity.setCodeEntrepot(dto.getCodeEntrepot());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaEntrepotDTO mapEntityToDto(TaEntrepot entity, TaEntrepotDTO dto) {
		if(dto==null)
			dto = new TaEntrepotDTO();

		dto.setId(entity.getIdEntrepot());
		dto.setLibelle(entity.getLibelle());
		dto.setCodeEntrepot(entity.getCodeEntrepot());
		dto.setTopEffacable(entity.getActif());
		
		
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
