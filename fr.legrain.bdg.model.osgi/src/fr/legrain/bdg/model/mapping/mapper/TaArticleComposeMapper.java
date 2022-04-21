package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.article.dto.TaArticleComposeDTO;
import fr.legrain.article.model.TaArticleCompose;
import fr.legrain.bdg.model.mapping.ILgrMapper;


public class TaArticleComposeMapper implements ILgrMapper<TaArticleComposeDTO, TaArticleCompose> {

	@Override
	public TaArticleCompose mapDtoToEntity(TaArticleComposeDTO dto, TaArticleCompose entity) {
		if(entity==null)
			entity = new TaArticleCompose();

		entity.setIdArticleCompose(dto.getIdArticleCompose()!=null?dto.getIdArticleCompose():0);

		return entity;
	}

	@Override
	public TaArticleComposeDTO mapEntityToDto(TaArticleCompose entity, TaArticleComposeDTO dto) {
		if(dto==null)
			dto = new TaArticleComposeDTO();

		dto.setIdArticleCompose(entity.getIdArticleCompose());
		

		return dto;
	}

}
