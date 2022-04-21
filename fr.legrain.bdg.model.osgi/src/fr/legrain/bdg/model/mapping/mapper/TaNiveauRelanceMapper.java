package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.document.dto.TaNiveauRelanceDTO;
import fr.legrain.document.model.TaTRelance;


public class TaNiveauRelanceMapper implements ILgrMapper<TaNiveauRelanceDTO, TaTRelance> {

	@Override
	public TaTRelance mapDtoToEntity(TaNiveauRelanceDTO dto, TaTRelance entity) {
		if(entity==null)
			entity = new TaTRelance();

		entity.setIdTRelance(dto.getId()!=null?dto.getId():0);
		
		entity.setLibelleTRelance(dto.getLibelleTRelance());
		entity.setCodeTRelance(dto.getCodeTRelance());
		entity.setOrdreTRelance(dto.getOrdreTRelance());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaNiveauRelanceDTO mapEntityToDto(TaTRelance entity, TaNiveauRelanceDTO dto) {
		if(dto==null)
			dto = new TaNiveauRelanceDTO();

		dto.setId(entity.getIdTRelance());
		
		dto.setLibelleTRelance(entity.getLibelleTRelance());
		dto.setCodeTRelance(entity.getCodeTRelance());
		dto.setOrdreTRelance(entity.getOrdreTRelance());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
