//package fr.legrain.bdg.model.mapping.mapper;
//
//import fr.legrain.article.model.TaLModeleFabrication;
//import fr.legrain.bdg.model.mapping.ILgrMapper;
//import fr.legrain.document.dto.TaLModeleFabricationDTO;
//
//
//
//public class TaLModeleFabricationMapper implements ILgrMapper<TaLModeleFabricationDTO, TaLModeleFabrication> {
//
//	@Override
//	public TaLModeleFabrication mapDtoToEntity(TaLModeleFabricationDTO dto, TaLModeleFabrication entity) {
//		if(entity==null)
//			entity = new TaLModeleFabrication();
//
//		entity.setIdLDocument(dto.getIdLDocument()!=null?dto.getIdLDocument():0);
//		
////		entity.setLibelleDocument(dto.getLibelleDocument());
////		entity.setCodeDocument(dto.getCodeDocument());
//		
////		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
//		
//		entity.setVersionObj(dto.getVersionObj());
//
//		return entity;
//	}
//
//	@Override
//	public TaLModeleFabricationDTO mapEntityToDto(TaLModeleFabrication entity, TaLModeleFabricationDTO dto) {
//		if(dto==null)
//			dto = new TaLModeleFabricationDTO();
//
//		dto.setIdLDocument(entity.getIdLDocument());
//		
////		dto.setLibelleDocument(entity.getLibelleDocument());
////		dto.setCodeDocument(entity.getCodeDocument());
//		
////		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
//		
//		dto.setVersionObj(entity.getVersionObj());
//
//		return dto;
//	}
//
//}
