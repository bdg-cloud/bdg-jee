//package fr.legrain.bdg.model.mapping.mapper;
//
//import fr.legrain.article.dto.TaProduitDTO;
//import fr.legrain.article.model.TaProduit;
//import fr.legrain.bdg.model.mapping.ILgrMapper;
//
//
//public class TaProduitMapper implements ILgrMapper<TaProduitDTO, TaProduit> {
//
//	@Override
//	public TaProduit mapDtoToEntity(TaProduitDTO dto, TaProduit entity) {
//		if(entity==null)
//			entity = new TaProduit();
//
//		entity.setId(dto.getId()!=null?dto.getId():0);
//		
//		
//		
//		
//		
//		
//		
////		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
//		
//		entity.setVersionObj(dto.getVersionObj());
//
//		return entity;
//	}
//
//	@Override
//	public TaProduitDTO mapEntityToDto(TaProduit entity, TaProduitDTO dto) {
//		if(dto==null)
//			dto = new TaProduitDTO();
//
//		dto.setId(entity.getId());
//		
//		
//		
//		
//		
////		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
//		
//		dto.setVersionObj(entity.getVersionObj());
//
//		return dto;
//	}
//
//}
