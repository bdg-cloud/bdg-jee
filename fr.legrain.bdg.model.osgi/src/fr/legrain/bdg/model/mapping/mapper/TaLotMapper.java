package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.article.dto.TaLotDTO;
import fr.legrain.article.model.TaLot;
import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.lib.data.LibConversion;

/*
 * (non-Javadoc)
 * attention Mapper a ne pas utiliser (non terminé)
 */
public class TaLotMapper implements ILgrMapper<TaLotDTO, TaLot> {

	@Override
	
	
	public TaLot mapDtoToEntity(TaLotDTO dto, TaLot entity) {
		if(entity==null)
			entity = new TaLot();
            entity.setIdLot(dto.getId());
            entity.setNumLot(dto.getNumLot());
            entity.setLibelle(dto.getLibelle());
            entity.setDluo(dto.getDluo());
            entity.setNbDecimal(dto.getNbDecimal());
            entity.setRapport(dto.getRapport());
            entity.setSens(LibConversion.booleanToInt(dto.getSens()));
            entity.setUnite1(dto.getCodeUnite1());
            entity.setUnite2(dto.getCodeUnite2());
            entity.setVirtuel(dto.getVirtuel());
            entity.setVirtuelUnique(dto.getVirtuelUnique());
//            entity.setTaArticle(dto.getId());
//            entity.setTaCodeBarre(dto.getId());
		
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaLotDTO mapEntityToDto(TaLot entity, TaLotDTO dto) {
		if(dto==null)
			dto = new TaLotDTO();

		dto.setId(entity.getIdLot());
		if(entity.getTaArticle()!=null){
			dto.setCodeArticle(entity.getTaArticle().getCodeArticle());
			dto.setLibelleArticle(entity.getTaArticle().getLibellecArticle());
		}
		dto.setCodeUnite1(entity.getUnite1());
		dto.setCodeUnite2(entity.getUnite2());
		dto.setDluo(entity.getDluo());
		dto.setIdCodeBarre(entity.getIdLot());
		if(entity.getTaCodeBarre()!=null){
			dto.setCodeBarre(entity.getTaCodeBarre().getCodeBarre());			
		}
		dto.setNbDecimal(entity.getNbDecimal());
		dto.setNumLot(entity.getNumLot());
		dto.setLibelle(entity.getLibelle());
		dto.setRapport(entity.getRapport());
		if(entity.getSens()!=null){
			dto.setSens( LibConversion.intToBoolean(entity.getSens()));
		}
		dto.setTermine(entity.getTermine());	
		dto.setVirtuel(entity.getVirtuel());
		dto.setVirtuelUnique(entity.getVirtuelUnique());
		
		dto.setLotConforme(entity.getLotConforme());
		dto.setPresenceDeNonConformite(entity.getPresenceDeNonConformite());
		if(entity.getTaStatusConformite()!=null){
			dto.setIdStatusConformite(entity.getTaStatusConformite().getIdStatusConformite());
			dto.setCodeStatusConformite(entity.getTaStatusConformite().getCodeStatusConformite());	
		}
//		if(entity.getTaTiersPrestationService()!=null){
//			dto.setIdStatusConformite(entity.getTaTiersPrestationService().getIdStatusConformite());
//			dto.setCodeStatusConformite(entity.getTaTiersPrestationService().getCodeStatusConformite());	
//		}
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
