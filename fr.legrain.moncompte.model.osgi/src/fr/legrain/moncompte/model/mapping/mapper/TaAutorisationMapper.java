package fr.legrain.moncompte.model.mapping.mapper;


import fr.legrain.moncompte.dto.TaAutorisationDTO;
import fr.legrain.moncompte.model.TaAutorisation;
import fr.legrain.moncompte.model.mapping.ILgrMapper;


public class TaAutorisationMapper implements ILgrMapper<TaAutorisationDTO, TaAutorisation> {

	@Override
	public TaAutorisation mapDtoToEntity(TaAutorisationDTO dto, TaAutorisation entity) {
		if(entity==null)
			entity = new TaAutorisation();

		entity.setIdAutorisation(dto.getId()!=null?dto.getId():0);
		
		

		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaAutorisationDTO mapEntityToDto(TaAutorisation entity, TaAutorisationDTO dto) {
		if(dto==null)
			dto = new TaAutorisationDTO();

		dto.setId(entity.getIdAutorisation());
		dto.setDateFin(entity.getDateFin());
		dto.setCode(entity.getTaDossier().getCode());
		if(entity.getTaDossier().getTaClient() != null) {
			dto.setCodeClient(entity.getTaDossier().getTaClient().getCode());
		}
	
		dto.setNomProduit(entity.getTaProduit().getIdentifiantModule());
		

		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
