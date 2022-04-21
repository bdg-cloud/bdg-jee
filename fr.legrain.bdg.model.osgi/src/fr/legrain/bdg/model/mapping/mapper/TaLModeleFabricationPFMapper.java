package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.article.model.TaLModeleFabricationPF;
import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.document.dto.TaLModeleFabricationDTO;



public class TaLModeleFabricationPFMapper implements ILgrMapper<TaLModeleFabricationDTO, TaLModeleFabricationPF> {

	@Override
	public TaLModeleFabricationPF mapDtoToEntity(TaLModeleFabricationDTO dto, TaLModeleFabricationPF entity) {
		if(entity==null)
			entity = new TaLModeleFabricationPF();

		entity.setIdLDocument(dto.getIdLDocument()!=null?dto.getIdLDocument():0);
		
//		entity.setLibelleDocument(dto.getLibelleDocument());
//		entity.setCodeDocument(dto.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaLModeleFabricationDTO mapEntityToDto(TaLModeleFabricationPF entity, TaLModeleFabricationDTO dto) {
		if(dto==null)
			dto = new TaLModeleFabricationDTO();

		dto.setIdLDocument(entity.getIdLDocument());
		
//		dto.setLibelleDocument(entity.getLibelleDocument());
//		dto.setCodeDocument(entity.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
