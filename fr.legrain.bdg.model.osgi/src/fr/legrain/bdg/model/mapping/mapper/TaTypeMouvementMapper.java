package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.article.dto.TaTypeMouvementDTO;
import fr.legrain.article.model.TaTypeMouvement;
import fr.legrain.bdg.model.mapping.ILgrMapper;


public class TaTypeMouvementMapper implements ILgrMapper<TaTypeMouvementDTO, TaTypeMouvement> {

	@Override
	public TaTypeMouvement mapDtoToEntity(TaTypeMouvementDTO dto, TaTypeMouvement entity) {
		if(entity==null)
			entity = new TaTypeMouvement();

		entity.setIdTypeMouvement(dto.getId()!=null?dto.getId():0);		
		entity.setLibelle(dto.getLibelle());		
		entity.setCode(dto.getCode());
		entity.setSysteme(dto.getSysteme());
		entity.setSens(dto.getSens());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaTypeMouvementDTO mapEntityToDto(TaTypeMouvement entity, TaTypeMouvementDTO dto) {
		if(dto==null)
			dto = new TaTypeMouvementDTO();

		dto.setId(entity.getIdTypeMouvement());
		dto.setLibelle(entity.getLibelle());
		dto.setCode(entity.getCode());
		dto.setSysteme(entity.getSysteme());
		dto.setSens(entity.getSens());
		
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
