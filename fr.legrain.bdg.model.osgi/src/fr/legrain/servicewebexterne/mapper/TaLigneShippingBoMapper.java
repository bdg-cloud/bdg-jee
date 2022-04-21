package fr.legrain.servicewebexterne.mapper;



import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.servicewebexterne.dto.TaLigneShippingboDTO;
import fr.legrain.servicewebexterne.model.TaLigneShippingBo;




public class TaLigneShippingBoMapper implements ILgrMapper<TaLigneShippingboDTO, TaLigneShippingBo> {

	@Override
	public TaLigneShippingBo mapDtoToEntity(TaLigneShippingboDTO dto, TaLigneShippingBo entity) {
		if(entity==null)
			entity = new TaLigneShippingBo();
//
//		entity.setIdFichier(dto.getId()!=null?dto.getId():0);
//		entity.setCodeFichier(dto.getCodeFichier());
//		entity.setJournalFichier(dto.getJournalFichier());
//		entity.setLibelleEdition(dto.getLibelleEdition());	
//		entity.setLibelleFichier(dto.getLibelleFichier());	
//		
//		entity.setVersionObj(dto.getVersionObj());
//
		return entity;
		
	}

	@Override
	public TaLigneShippingboDTO mapEntityToDto(TaLigneShippingBo entity, TaLigneShippingboDTO dto) {
		if(dto==null)
			dto = new TaLigneShippingboDTO();
//
//		dto.setId(entity.getIdFichier());
//		dto.setCodeFichier(entity.getCodeFichier());
//		dto.setJournalFichier(entity.getJournalFichier());
//		dto.setLibelleEdition(entity.getLibelleEdition());	
//		dto.setLibelleFichier(entity.getLibelleFichier());	
//		
//		dto.setVersionObj(entity.getVersionObj());
//
		return dto;
		
	}

}
