package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.ObjectDTO;
import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.document.dto.TaDevisDTO;
import fr.legrain.document.dto.TaTLigneDTO;
import fr.legrain.document.model.TaDevis;
import fr.legrain.document.model.TaTLigne;


public class TaTLigneMapper implements ILgrMapper<ObjectDTO, TaTLigne> {

	@Override
	public TaTLigne mapDtoToEntity(ObjectDTO dto, TaTLigne entity) {
		if(entity==null)
			entity = new TaTLigne();

		entity.setIdTLigne(dto.getId()!=null?dto.getId():0);
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public ObjectDTO mapEntityToDto(TaTLigne entity, ObjectDTO dto) {
		if(dto==null)
			dto = new ObjectDTO();

		dto.setId(entity.getIdTLigne());
		
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
