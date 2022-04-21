package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.article.dto.TaCoefficientUniteDTO;
import fr.legrain.article.model.TaCoefficientUnite;
import fr.legrain.bdg.model.mapping.ILgrMapper;


public class TaCoefficientUniteMapper implements ILgrMapper<TaCoefficientUniteDTO, TaCoefficientUnite> {

	@Override
	public TaCoefficientUnite mapDtoToEntity(TaCoefficientUniteDTO dto, TaCoefficientUnite entity) {
		if(entity==null)
			entity = new TaCoefficientUnite();

		entity.setId(dto.getId()!=null?dto.getId():0);
		
		entity.setCoeffUniteAVersB(dto.getCoeffUniteAVersB());
		entity.setCoeffUniteBVersA(dto.getCoeffUniteBVersA());

		entity.setOperateurAVersBDivise(dto.getOperateurAVersBDivise());
		entity.setOperateurBVersADivise(dto.getOperateurBVersADivise());
		
		entity.setNbDecimaleAVersB(dto.getNbDecimaleAVersB());
		entity.setNbDecimaleBVersA(dto.getNbDecimaleBVersA());
		
		if(entity.getUniteA()!=null) entity.getUniteA().setCodeUnite(dto.getCodeUniteA());
		if(entity.getUniteB()!=null) entity.getUniteB().setCodeUnite(dto.getCodeUniteB());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaCoefficientUniteDTO mapEntityToDto(TaCoefficientUnite entity, TaCoefficientUniteDTO dto) {
		if(dto==null)
			dto = new TaCoefficientUniteDTO();

		dto.setId(entity.getId());
		
		dto.setCoeffUniteAVersB(entity.getCoeffUniteAVersB());
		dto.setCoeffUniteBVersA(entity.getCoeffUniteBVersA());

		dto.setOperateurAVersBDivise(entity.getOperateurAVersBDivise());
		dto.setOperateurBVersADivise(entity.getOperateurBVersADivise());
		
		dto.setNbDecimaleAVersB(entity.getNbDecimaleAVersB());
		dto.setNbDecimaleBVersA(entity.getNbDecimaleBVersA());
		
		if(entity.getUniteA()!=null) dto.setCodeUniteA(entity.getUniteA().getCodeUnite());
		if(entity.getUniteB()!=null) dto.setCodeUniteB(entity.getUniteB().getCodeUnite());
				
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
