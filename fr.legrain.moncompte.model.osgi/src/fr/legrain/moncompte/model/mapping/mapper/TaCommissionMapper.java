package fr.legrain.moncompte.model.mapping.mapper;

import fr.legrain.moncompte.dto.TaCommissionDTO;
import fr.legrain.moncompte.model.TaCommission;
import fr.legrain.moncompte.model.mapping.ILgrMapper;


public class TaCommissionMapper implements ILgrMapper<TaCommissionDTO, TaCommission> {

	@Override
	public TaCommission mapDtoToEntity(TaCommissionDTO dto, TaCommission entity) {
		if(entity==null)
			entity = new TaCommission();

		entity.setId(dto.getId()!=null?dto.getId():0);
		
//		entity.setAdresse1(dto.getAdresse1());
//		entity.setAdresse2(dto.getAdresse2());
//		entity.setAdresse3(dto.getAdresse3());
//		entity.setCodePostal(dto.getCodePostal());
//		entity.setPays(dto.getNomEntreprise());
//		entity.setVille(dto.getNomEntreprise());
//
//		entity.setNomEntreprise(dto.getNomEntreprise());
//
//		entity.setNumTel1(dto.getNomEntreprise());
//		entity.setNumTel2(dto.getNomEntreprise());
//		entity.setNumFax(dto.getNomEntreprise());
//		entity.setNumPort(dto.getNomEntreprise());
//		entity.setEmail(dto.getEmail());
//		entity.setWeb(dto.getNomEntreprise());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaCommissionDTO mapEntityToDto(TaCommission entity, TaCommissionDTO dto) {
		if(dto==null)
			dto = new TaCommissionDTO();

		dto.setId(entity.getId());
		
//		dto.setAdresse1(entity.getAdresse1());
//		dto.setAdresse2(entity.getAdresse2());
//		dto.setAdresse3(entity.getAdresse3());
//		dto.setCodePostal(entity.getCodePostal());
//		dto.setPays(entity.getNomEntreprise());
//		dto.setVille(entity.getNomEntreprise());
//
//		dto.setNomEntreprise(entity.getNomEntreprise());
//
//		dto.setNumTel1(entity.getNomEntreprise());
//		dto.setNumTel2(entity.getNomEntreprise());
//		dto.setNumFax(entity.getNomEntreprise());
//		dto.setNumPort(entity.getNomEntreprise());
//		dto.setEmail(entity.getEmail());
//		dto.setWeb(entity.getNomEntreprise());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
