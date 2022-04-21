package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.document.dto.TaProformaDTO;
import fr.legrain.document.model.TaProforma;
import fr.legrain.document.model.TaProforma;
import fr.legrain.document.model.TaProforma;
import fr.legrain.document.model.TaProforma;


public class TaProformaMapper implements ILgrMapper<TaProformaDTO, TaProforma> {

	@Override
	public TaProforma mapDtoToEntity(TaProformaDTO dto, TaProforma entity) {
		if(entity==null)
			entity = new TaProforma();

		entity.setIdDocument(dto.getId()!=null?dto.getId():0);
		
		entity.setLibelleDocument(dto.getLibelleDocument());
		entity.setCodeDocument(dto.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaProformaDTO mapEntityToDto(TaProforma entity, TaProformaDTO dto) {
		if(dto==null)
			dto = new TaProformaDTO();

		dto.setId(entity.getIdDocument());
		
		dto.setLibelleDocument(entity.getLibelleDocument());
		dto.setCodeDocument(entity.getCodeDocument());
		dto.setDateDocument(entity.getDateDocument());
		if (entity.getTaTiers()!= null) {
		dto.setCodeTiers(entity.getTaTiers().getCodeTiers());
		}
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
