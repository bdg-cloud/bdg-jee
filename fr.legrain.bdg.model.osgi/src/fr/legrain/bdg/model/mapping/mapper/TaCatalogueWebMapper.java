package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.article.dto.TaCatalogueWebDTO;
import fr.legrain.article.model.TaCatalogueWeb;
import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.lib.data.LibConversion;

public class TaCatalogueWebMapper implements ILgrMapper<TaCatalogueWebDTO, TaCatalogueWeb> {

	@Override
	public TaCatalogueWeb mapDtoToEntity(TaCatalogueWebDTO dto, TaCatalogueWeb entity) {
		if(entity==null)
			entity = new TaCatalogueWeb();

		entity.setIdCatalogueWeb(dto.getId()!=null?dto.getId():0);
		
		entity.setDescriptionLongueCatWeb(dto.getDescriptionLongueCatWeb());
		entity.setResumeCatWeb(dto.getResumeCatWeb());
		entity.setExpediableCatalogueWeb(dto.getExpediableCatalogueWeb());
		entity.setExportationCatalogueWeb(dto.getExportationCatalogueWeb());
		entity.setNouveauteCatalogueWeb(dto.getNouveauteCatalogueWeb());
		entity.setPromotionCatalogueWeb(dto.getPromotionCatalogueWeb());
		entity.setPromotionU2CatalogueWeb(dto.getPromotionU2CatalogueWeb());
		entity.setSpecialCatalogueWeb(dto.getSpecialCatalogueWeb());
		entity.setUrlRewritingCatalogueWeb(dto.getUrlRewritingCatalogueWeb());
		
		entity.setNonDisponibleCatalogueWeb(dto.getNonDisponibleCatalogueWeb());
		entity.setFraisPortAdditionnel(dto.getFraisPortAdditionnel());
		entity.setLibelleCatalogue(dto.getLibelleCatalogue());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaCatalogueWebDTO mapEntityToDto(TaCatalogueWeb entity, TaCatalogueWebDTO dto) {
		if(dto==null)
			dto = new TaCatalogueWebDTO();

		dto.setId(entity.getIdCatalogueWeb());
		
		dto.setDescriptionLongueCatWeb(entity.getDescriptionLongueCatWeb());
		dto.setResumeCatWeb(entity.getResumeCatWeb());
		dto.setExpediableCatalogueWeb(entity.getExpediableCatalogueWeb());
		dto.setExportationCatalogueWeb(entity.getExportationCatalogueWeb());
		dto.setNouveauteCatalogueWeb(entity.getNouveauteCatalogueWeb());
		dto.setPromotionCatalogueWeb(entity.getPromotionCatalogueWeb());
		dto.setPromotionU2CatalogueWeb(entity.getPromotionU2CatalogueWeb());
		dto.setSpecialCatalogueWeb(entity.getSpecialCatalogueWeb());
		dto.setUrlRewritingCatalogueWeb(entity.getUrlRewritingCatalogueWeb());
		
		dto.setNonDisponibleCatalogueWeb(entity.getNonDisponibleCatalogueWeb());
		dto.setFraisPortAdditionnel(entity.getFraisPortAdditionnel());
		dto.setLibelleCatalogue(entity.getLibelleCatalogue());
		
		if(entity.getTaPrixCatalogueDefaut()!=null) {
			dto.setIdPrix(entity.getTaPrixCatalogueDefaut().getIdPrix());
			dto.setPrixPrix(entity.getTaPrixCatalogueDefaut().getPrixPrix());
			dto.setPrixttcPrix(entity.getTaPrixCatalogueDefaut().getPrixttcPrix());
		}
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
