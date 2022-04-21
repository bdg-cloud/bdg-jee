package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.email.dto.TaEtiquetteEmailDTO;
import fr.legrain.email.model.TaEtiquetteEmail;


public class TaEtiquetteEmailMapper implements ILgrMapper<TaEtiquetteEmailDTO, TaEtiquetteEmail> {

	@Override
	public TaEtiquetteEmail mapDtoToEntity(TaEtiquetteEmailDTO dto, TaEtiquetteEmail entity) {
		if(entity==null)
			entity = new TaEtiquetteEmail();

		entity.setIdEtiquetteEmail(dto.getId()!=null?dto.getId():0);
		
		entity.setCode(dto.getCode());
		entity.setLibelle(dto.getLibelle());
		entity.setDescription(dto.getDescription());
		entity.setCouleur(dto.getCouleur());
		entity.setSysteme(dto.isSysteme());
		entity.setVisible(dto.isVisible());
		entity.setOrdre(dto.getOrdre());
//		entity.setVilleAdresse(dto.getVilleAdresse());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaEtiquetteEmailDTO mapEntityToDto(TaEtiquetteEmail entity, TaEtiquetteEmailDTO dto) {
		if(dto==null)
			dto = new TaEtiquetteEmailDTO();

		dto.setId(entity.getIdEtiquetteEmail());

		dto.setCode(entity.getCode());
		dto.setLibelle(entity.getLibelle());
		dto.setDescription(entity.getDescription());
		dto.setCouleur(entity.getCouleur());
		dto.setSysteme(entity.isSysteme());
		dto.setVisible(entity.isVisible());
		dto.setOrdre(entity.getOrdre());
//		dto.setPaysAdresse(entity.getPaysAdresse());
//		dto.setVilleAdresse(entity.getVilleAdresse());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
