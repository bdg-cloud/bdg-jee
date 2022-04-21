package fr.legrain.moncompte.model.mapping.mapper;

import fr.legrain.moncompte.dto.TaSetupDTO;
import fr.legrain.moncompte.model.TaSetup;
import fr.legrain.moncompte.model.mapping.ILgrMapper;


public class TaSetupMapper implements ILgrMapper<TaSetupDTO, TaSetup> {

	@Override
	public TaSetup mapDtoToEntity(TaSetupDTO dto, TaSetup entity) {
		if(entity==null)
			entity = new TaSetup();

		entity.setIdSetup(dto.getId()!=null?dto.getId():0);
		
		entity.setActif(dto.getActif());
		entity.setDateSetup(dto.getDateSetup());
		entity.setDescription(dto.getDescription());
		entity.setFichierSetup(dto.getFichierSetup());
		entity.setLibelle(dto.getLibelle());
		entity.setMaj(dto.getMaj());
		entity.setVersionProduit(dto.getVersionProduit());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaSetupDTO mapEntityToDto(TaSetup entity, TaSetupDTO dto) {
		if(dto==null)
			dto = new TaSetupDTO();

		dto.setId(entity.getIdSetup());
		
		dto.setActif(entity.getActif());
		dto.setDateSetup(entity.getDateSetup());
		dto.setDescription(entity.getDescription());
		dto.setFichierSetup(entity.getFichierSetup());
		dto.setLibelle(entity.getLibelle());
		dto.setMaj(entity.getMaj());
		dto.setVersionProduit(entity.getVersionProduit());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
