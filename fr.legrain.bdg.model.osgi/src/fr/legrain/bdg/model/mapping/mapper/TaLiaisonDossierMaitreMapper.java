package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.general.dto.TaLiaisonDossierMaitreDTO;
import fr.legrain.general.model.TaLiaisonDossierMaitre;


public class TaLiaisonDossierMaitreMapper implements ILgrMapper<TaLiaisonDossierMaitreDTO, TaLiaisonDossierMaitre> {

	@Override
	public TaLiaisonDossierMaitre mapDtoToEntity(TaLiaisonDossierMaitreDTO dto, TaLiaisonDossierMaitre entity) {
		if(entity==null)
			entity = new TaLiaisonDossierMaitre();

		entity.setIdLiaisonDossierMaitre(dto.getId()!=null?dto.getId():0);
		
		entity.setEmail(dto.getEmail());
		entity.setPassword(dto.getPassword());
		
//		if(entity.getTaTiers()!=null) entity.getTaTiers().setCodeTiers(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaLiaisonDossierMaitreDTO mapEntityToDto(TaLiaisonDossierMaitre entity, TaLiaisonDossierMaitreDTO dto) {
		if(dto==null)
			dto = new TaLiaisonDossierMaitreDTO();

		dto.setId(entity.getIdLiaisonDossierMaitre());
		
		dto.setEmail(entity.getEmail());
		dto.setPassword(entity.getPassword());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
