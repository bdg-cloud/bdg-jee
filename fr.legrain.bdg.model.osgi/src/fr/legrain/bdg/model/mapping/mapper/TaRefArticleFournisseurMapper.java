package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.article.dto.TaRefArticleFournisseurDTO;
import fr.legrain.article.model.TaRefArticleFournisseur;
import fr.legrain.bdg.model.mapping.ILgrMapper;


public class TaRefArticleFournisseurMapper implements ILgrMapper<TaRefArticleFournisseurDTO, TaRefArticleFournisseur> {

	@Override
	public TaRefArticleFournisseur mapDtoToEntity(TaRefArticleFournisseurDTO dto, TaRefArticleFournisseur entity) {
		if(entity==null)
			entity = new TaRefArticleFournisseur();

		entity.setIdRefArticleFournisseur(dto.getId());
		
		entity.setCodeArticleFournisseur(dto.getCodeArticleFournisseur());
		entity.setCodeBarreFournisseur(dto.getCodeBarreFournisseur());
		entity.setDescriptif(dto.getDescriptif());
		

		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaRefArticleFournisseurDTO mapEntityToDto(TaRefArticleFournisseur entity, TaRefArticleFournisseurDTO dto) {
		if(dto==null)
			dto = new TaRefArticleFournisseurDTO();

		dto.setId(entity.getIdRefArticleFournisseur());
		
		dto.setCodeArticleFournisseur(entity.getCodeArticleFournisseur());
		dto.setCodeBarreFournisseur(entity.getCodeBarreFournisseur());
		dto.setDescriptif(entity.getDescriptif());
		
		if(entity.getTaFournisseur()!=null) dto.setCodeFournisseur(entity.getTaFournisseur().getCodeTiers());
		if(entity.getTaArticle()!=null) dto.setCodeArticle(entity.getTaArticle().getCodeArticle());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
