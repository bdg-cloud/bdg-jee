package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.controle.dto.TaVerrouCodeGenereDTO;
import fr.legrain.controle.model.TaVerrouCodeGenere;


public class TaVerrouCodeGenereMapper implements ILgrMapper<TaVerrouCodeGenereDTO, TaVerrouCodeGenere> {

	@Override
	public TaVerrouCodeGenere mapDtoToEntity(TaVerrouCodeGenereDTO dto, TaVerrouCodeGenere entity) {
		if(entity==null)
			entity = new TaVerrouCodeGenere();

		entity.setIdVerrouCodeGenere(dto.getId()!=null?dto.getId():0);
		entity.setEntite(dto.getEntite());
		entity.setChamp(dto.getChamp());
		entity.setValeur(dto.getValeur());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaVerrouCodeGenereDTO mapEntityToDto(TaVerrouCodeGenere entity, TaVerrouCodeGenereDTO dto) {
		if(dto==null)
			dto = new TaVerrouCodeGenereDTO();

		dto.setId(entity.getIdVerrouCodeGenere());
		dto.setEntite(entity.getEntite());
		dto.setChamp(entity.getChamp());
		dto.setValeur(entity.getValeur());	
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
