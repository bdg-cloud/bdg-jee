package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.document.dto.TaFlashDTO;
import fr.legrain.document.model.TaFlash;


public class TaFlashMapper implements ILgrMapper<TaFlashDTO, TaFlash> {

	@Override
	public TaFlash mapDtoToEntity(TaFlashDTO dto, TaFlash entity) {
		if(entity==null)
			entity = new TaFlash();

		entity.setIdFlash(dto.getId()!=null?dto.getId():0);
		
		entity.setLibelleFlash(dto.getLibelleFlash());
		entity.setCodeFlash(dto.getCodeFlash());
		entity.setDateFlash(dto.getDateFlash());
		entity.setDateTransfert(dto.getDateTransfert());
		
		if(entity.getTaTiers()!=null) entity.getTaTiers().setCodeTiers(dto.getCodeTiers());
		if(entity.getTaGroupePreparation()!=null) entity.getTaGroupePreparation().setCodeGroupePreparation(dto.getCodeGroupePreparation());
//		if(entity.getTaUtilisateur()!=null) entity.getTaUtilisateur().setNom(dto.getNom());
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaFlashDTO mapEntityToDto(TaFlash entity, TaFlashDTO dto) {
		if(dto==null)
			dto = new TaFlashDTO();

		dto.setId(entity.getIdFlash());
		
		dto.setLibelleFlash(entity.getLibelleFlash());
		dto.setCodeFlash(entity.getCodeFlash());
		dto.setDateFlash(entity.getDateFlash());
		dto.setDateTransfert(entity.getDateTransfert());
		
		if(entity.getTaTiers()!=null) dto.setCodeTiers(entity.getTaTiers().getCodeTiers());
		if(entity.getTaGroupePreparation()!=null) dto.setCodeGroupePreparation(entity.getTaGroupePreparation().getCodeGroupePreparation());
//		if(entity.getTaUtilisateur()!=null) dto.setNom(entity.getTaUtilisateur().getNom());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
