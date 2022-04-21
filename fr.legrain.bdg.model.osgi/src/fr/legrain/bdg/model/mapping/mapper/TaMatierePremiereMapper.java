//package fr.legrain.bdg.model.mapping.mapper;
//
//import fr.legrain.article.dto.TaMatierePremiereDTO;
//import fr.legrain.article.model.TaMatierePremiere;
//import fr.legrain.bdg.model.mapping.ILgrMapper;
//
//
//public class TaMatierePremiereMapper implements ILgrMapper<TaMatierePremiereDTO, TaMatierePremiere> {
//
//	@Override
//	public TaMatierePremiere mapDtoToEntity(TaMatierePremiereDTO dto, TaMatierePremiere entity) {
//		if(entity==null)
//			entity = new TaMatierePremiere();
//	
//		entity.setId(dto.getId()!=null?dto.getId():0);
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
//	public TaMatierePremiereDTO mapEntityToDto(TaMatierePremiere entity, TaMatierePremiereDTO dto) {
//		if(dto==null)
//			dto = new TaMatierePremiereDTO();
//
//		dto.setId(entity.getId());
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
