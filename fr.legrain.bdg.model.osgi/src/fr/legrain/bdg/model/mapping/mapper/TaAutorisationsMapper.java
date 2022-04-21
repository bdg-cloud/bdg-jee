//package fr.legrain.bdg.model.mapping.mapper;
//
//import fr.legrain.autorisations.dto.TaAutorisationsDTO;
//import fr.legrain.autorisations.model.TaAutorisations;
//import fr.legrain.bdg.model.mapping.ILgrMapper;
//import fr.legrain.tiers.dto.TaAdresseDTO;
//import fr.legrain.tiers.model.TaAdresse;
//
//
//public class TaAutorisationsMapper implements ILgrMapper<TaAutorisationsDTO, TaAutorisations> {
//
//	@Override
//	public TaAutorisations mapDtoToEntity(TaAutorisationsDTO dto, TaAutorisations entity) {
//		if(entity==null)
//			entity = new TaAutorisations();
//
//		entity.setIdAutorisations(dto.getId()!=null?dto.getId():0);
//		
//		entity.setCodeTiers(dto.getCodeTiers());
//		entity.setModules(dto.getModules());
//		entity.setValide(dto.getValide());
//		
//		
//		entity.setVersionObj(dto.getVersionObj());
//
//		return entity;
//	}
//
//	@Override
//	public TaAutorisationsDTO mapEntityToDto(TaAutorisations entity, TaAutorisationsDTO dto) {
//		if(dto==null)
//			dto = new TaAutorisationsDTO();
//
//		dto.setId(entity.getIdAutorisation());
//		
//		dto.setCodeTiers(entity.getCodeTiers());
//		dto.setModules(entity.getModules());
//		dto.setValide(entity.getValide());
//		
//		if(entity.getTaTypeProduit()!=null){
//			dto.setCodeType(entity.getTaTypeProduit().getCode());
//			dto.setIdType(entity.getTaTypeProduit().getIdType());
//		}
//		
//		dto.setVersionObj(entity.getVersionObj());
//
//		return dto;
//	}
//
//}
