package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.pointLgr.dto.ComptePointDTO;
import fr.legrain.pointLgr.model.TaComptePoint;


public class TaComptePointMapper implements ILgrMapper<ComptePointDTO, TaComptePoint> {

	@Override
	public TaComptePoint mapDtoToEntity(ComptePointDTO dto, TaComptePoint entity) {
		if(entity==null)
			entity = new TaComptePoint();

		entity.setIdPoint(dto.getId()!=null?dto.getId():0);
		
		entity.setCommentaire(dto.getCommentaire());
		entity.setCodeDocument(dto.getCodeDocument());
		entity.setLibelleEtat(dto.getLibelleEtat());
		
		if(entity.getTaTypeMouvPoint()!=null) {
			entity.getTaTypeMouvPoint().setIdTypeMouv(dto.getIdTypeMouv());
			entity.getTaTypeMouvPoint().setlibelleMouv(dto.getLibelleMouv());
		}
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public ComptePointDTO mapEntityToDto(TaComptePoint entity, ComptePointDTO dto) {
		if(dto==null)
			dto = new ComptePointDTO();

		dto.setId(entity.getIdPoint());
		
		dto.setLibelleEtat(entity.getLibelleEtat());
		dto.setCodeDocument(entity.getCodeDocument());
		dto.setCommentaire(entity.getCommentaire());
		
		if(entity.getTaTypeMouvPoint()!=null) {
			dto.setIdTypeMouv(entity.getTaTypeMouvPoint().getIdTypeMouv());
			dto.setLibelleMouv(entity.getTaTypeMouvPoint().getLibelleMouv());
		}
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
