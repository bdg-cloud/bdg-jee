package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.article.dto.TaUniteDTO;
import fr.legrain.article.model.TaUnite;
import fr.legrain.bdg.model.mapping.ILgrMapper;


public class TaUniteMapper implements ILgrMapper<TaUniteDTO, TaUnite> {

	@Override
	public TaUnite mapDtoToEntity(TaUniteDTO dto, TaUnite entity) {
		if(entity==null)
			entity = new TaUnite();

		entity.setIdUnite(dto.getId()!=null?dto.getId():0);
		
		entity.setCodeUnite(dto.getCodeUnite());
		entity.setHauteur(dto.getHauteur());
		entity.setLargeur(dto.getLargeur());
		entity.setLiblUnite(dto.getLiblUnite());
		entity.setLongueur(dto.getLongueur());
		entity.setNbUnite(dto.getNbUnite());
		entity.setPoids(dto.getPoids());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaUniteDTO mapEntityToDto(TaUnite entity, TaUniteDTO dto) {
		if(dto==null)
			dto = new TaUniteDTO();

		dto.setId(entity.getIdUnite());
		
		dto.setCodeUnite(entity.getCodeUnite());
		dto.setHauteur(entity.getHauteur());
		dto.setLargeur(entity.getLargeur());
		dto.setLiblUnite(entity.getLiblUnite());
		dto.setLongueur(entity.getLongueur());
		dto.setNbUnite(entity.getNbUnite());
		dto.setPoids(entity.getPoids());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
