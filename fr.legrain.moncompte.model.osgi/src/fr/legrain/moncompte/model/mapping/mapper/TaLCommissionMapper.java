package fr.legrain.moncompte.model.mapping.mapper;

import fr.legrain.moncompte.dto.TaLCommissionDTO;
import fr.legrain.moncompte.dto.TaLCommissionDTO;
import fr.legrain.moncompte.model.TaLigneCommission;
import fr.legrain.moncompte.model.TaLigneCommission;
import fr.legrain.moncompte.model.mapping.ILgrMapper;


public class TaLCommissionMapper implements ILgrMapper<TaLCommissionDTO, TaLigneCommission> {

	@Override
	public TaLigneCommission mapDtoToEntity(TaLCommissionDTO dto, TaLigneCommission entity) {
		if(entity==null)
			entity = new TaLigneCommission();

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
	public TaLCommissionDTO mapEntityToDto(TaLigneCommission entity, TaLCommissionDTO dto) {
		if(dto==null)
			dto = new TaLCommissionDTO();

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
