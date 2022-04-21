package fr.legrain.bdg.compteclient.model.mapping.mapper;

import fr.legrain.bdg.compteclient.dto.droits.TaEntrepriseClientDTO;
import fr.legrain.bdg.compteclient.model.droits.TaEntrepriseClient;
import fr.legrain.bdg.compteclient.model.mapping.ILgrMapper;


public class TaEntrepriseClientMapper implements ILgrMapper<TaEntrepriseClientDTO, TaEntrepriseClient> {

	@Override
	public TaEntrepriseClient mapDtoToEntity(TaEntrepriseClientDTO dto, TaEntrepriseClient entity) {
		if(entity==null)
			entity = new TaEntrepriseClient();

		entity.setId(dto.getId()!=null?dto.getId():0);
		
//		entity.setLibelleDocument(dto.getLibelleDocument());
//		entity.setCodeDocument(dto.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaEntrepriseClientDTO mapEntityToDto(TaEntrepriseClient entity, TaEntrepriseClientDTO dto) {
		if(dto==null)
			dto = new TaEntrepriseClientDTO();

		dto.setId(entity.getId());
		
//		dto.setLibelleDocument(entity.getLibelleDocument());
//		dto.setCodeDocument(entity.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
