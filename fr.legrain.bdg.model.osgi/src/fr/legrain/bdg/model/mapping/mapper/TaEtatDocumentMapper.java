package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.document.dto.TaEtatDTO;
import fr.legrain.document.model.TaEtat;

public class TaEtatDocumentMapper implements ILgrMapper<TaEtatDTO, TaEtat> {

	@Override
	public TaEtat mapDtoToEntity(TaEtatDTO dto, TaEtat entity) {
		// TODO Auto-generated method stub
		if(entity==null)
			entity = new TaEtat();

		entity.setIdEtat(dto.getIdEtat()!=null?dto.getIdEtat():0);
		entity.setCodeEtat(dto.getCodeEtat());
		entity.setLiblEtat(dto.getLiblEtat());
		entity.setVersionObj(dto.getVersionObj());

		return entity;
//		return null;
	}

	@Override
	public TaEtatDTO mapEntityToDto(TaEtat entity, TaEtatDTO dto) {
		// TODO Auto-generated method stub
		if(dto==null)
			dto = new TaEtatDTO();

		dto.setIdEtat(entity.getIdEtat());
		dto.setCodeEtat(entity.getCodeEtat());
		dto.setLiblEtat(entity.getLiblEtat());
		dto.setVersionObj(entity.getVersionObj());

		return dto;
//		return null;
	}

}
