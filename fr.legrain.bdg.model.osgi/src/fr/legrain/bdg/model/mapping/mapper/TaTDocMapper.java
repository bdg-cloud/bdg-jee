package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.document.dto.TaTDocDTO;
import fr.legrain.document.model.TaTDoc;


public class TaTDocMapper implements ILgrMapper<TaTDocDTO, TaTDoc> {

	@Override
	public TaTDoc mapDtoToEntity(TaTDocDTO dto, TaTDoc entity) {
		if(entity==null)
			entity = new TaTDoc();

		entity.setIdTDoc(dto.getId()!=null?dto.getId():0);
		entity.setCodeTDoc(dto.getCodeTDoc()!=null?dto.getCodeTDoc():null);
		entity.setLibTDoc(dto.getLibTDoc()!=null?dto.getLibTDoc():null);
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;		
	}

	@Override
	public TaTDocDTO mapEntityToDto(TaTDoc entity, TaTDocDTO dto) {
		if(dto==null)
			dto = new TaTDocDTO();

		dto.setId(entity.getIdTDoc());
		dto.setLibTDoc(entity.getLibTDoc());
		dto.setCodeTDoc(entity.getCodeTDoc());
		
			
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
