package fr.legrain.bdg.compteclient.model.mapping.mapper;

import fr.legrain.bdg.compteclient.dto.droits.TaGroupeEntrepriseDTO;
import fr.legrain.bdg.compteclient.model.droits.TaGroupeEntreprise;
import fr.legrain.bdg.compteclient.model.mapping.ILgrMapper;


public class TaGroupeEntrepriseMapper implements ILgrMapper<TaGroupeEntrepriseDTO, TaGroupeEntreprise> {

	@Override
	public TaGroupeEntreprise mapDtoToEntity(TaGroupeEntrepriseDTO dto, TaGroupeEntreprise entity) {
		if(entity==null)
			entity = new TaGroupeEntreprise();

		entity.setId(dto.getId()!=null?dto.getId():0);
		
//		entity.setLibelleDocument(dto.getLibelleDocument());
//		entity.setCodeDocument(dto.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaGroupeEntrepriseDTO mapEntityToDto(TaGroupeEntreprise entity, TaGroupeEntrepriseDTO dto) {
		if(dto==null)
			dto = new TaGroupeEntrepriseDTO();

		dto.setId(entity.getId());
		
//		dto.setLibelleDocument(entity.getLibelleDocument());
//		dto.setCodeDocument(entity.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
