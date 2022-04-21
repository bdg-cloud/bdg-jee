package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.article.dto.TaTvaDTO;
import fr.legrain.article.model.TaTva;
import fr.legrain.bdg.model.mapping.ILgrMapper;


public class TaTvaMapper implements ILgrMapper<TaTvaDTO, TaTva> {

	@Override
	public TaTva mapDtoToEntity(TaTvaDTO dto, TaTva entity) {
		if(entity==null)
			entity = new TaTva();

		entity.setIdTva(dto.getId()!=null?dto.getId():0);
		
		entity.setCodeTva(dto.getCodeTva());
		entity.setLibelleTva(dto.getLibelleTva());
		entity.setNumcptTva(dto.getNumcptTva());
		entity.setTauxTva(dto.getTauxTva());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaTvaDTO mapEntityToDto(TaTva entity, TaTvaDTO dto) {
		if(dto==null)
			dto = new TaTvaDTO();

		dto.setId(entity.getIdTva());
		
		dto.setCodeTva(entity.getCodeTva());
		dto.setLibelleTva(entity.getLibelleTva());
		dto.setNumcptTva(entity.getNumcptTva());
		dto.setTauxTva(entity.getTauxTva());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
