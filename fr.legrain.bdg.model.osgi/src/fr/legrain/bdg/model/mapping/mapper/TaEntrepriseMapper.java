package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.tiers.dto.TaEntrepriseDTO;
import fr.legrain.tiers.model.TaEntreprise;


public class TaEntrepriseMapper implements ILgrMapper<TaEntrepriseDTO, TaEntreprise> {

	@Override
	public TaEntreprise mapDtoToEntity(TaEntrepriseDTO dto, TaEntreprise entity) {
		if(entity==null)
			entity = new TaEntreprise();

		entity.setIdEntreprise(dto.getId()!=null?dto.getId():0);
		entity.setContact(dto.getContact());
		entity.setNomEntreprise(dto.getNomEntreprise());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaEntrepriseDTO mapEntityToDto(TaEntreprise entity, TaEntrepriseDTO dto) {
		if(dto==null)
			dto = new TaEntrepriseDTO();

		dto.setId(entity.getIdEntreprise());
		dto.setContact(entity.getContact());
		dto.setNomEntreprise(entity.getNomEntreprise());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
