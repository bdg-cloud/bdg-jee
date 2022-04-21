package fr.legrain.moncompte.model.mapping.mapper;

import fr.legrain.moncompte.dto.TaAdresseDTO;
import fr.legrain.moncompte.dto.TaPrixPersoDTO;
import fr.legrain.moncompte.dto.TaPrixPersoDTO;
import fr.legrain.moncompte.model.TaPrixPerso;
import fr.legrain.moncompte.model.TaPrixPerso;
import fr.legrain.moncompte.model.mapping.ILgrMapper;


public class TaPrixPersoMapper implements ILgrMapper<TaPrixPersoDTO, TaPrixPerso> {

	@Override
	public TaPrixPerso mapDtoToEntity(TaPrixPersoDTO dto, TaPrixPerso entity) {
		if(entity==null)
			entity = new TaPrixPerso();

		entity.setId(dto.getId()!=null?dto.getId():0);
		
		entity.setPrixHT(dto.getPrixHT());
		entity.setPrixTTC(dto.getPrixTTC());
		entity.setTauxTVA(dto.getTauxTVA());
		
		
//		if(entity.getTaUtilisateur()!=null) entity.getTaUtilisateur().setEmail(dto.getEmail());
//		if(entity.getTaUtilisateur()!=null) entity.getTaUtilisateur().setUsername(dto.getUsername());
//		if(entity.getTaUtilisateur()!=null) entity.getTaUtilisateur().setPasswd(dto.getPasswd());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaPrixPersoDTO mapEntityToDto(TaPrixPerso entity, TaPrixPersoDTO dto) {
		if(dto==null)
			dto = new TaPrixPersoDTO();

		dto.setId(entity.getId());
		
		dto.setPrixHT(entity.getPrixHT());
		dto.setPrixTTC(entity.getPrixTTC());
		dto.setTauxTVA(entity.getTauxTVA());;
		
//		if(entity.getTaUtilisateur()!=null) dto.setUsername(entity.getTaUtilisateur().getUsername());
//		if(entity.getTaUtilisateur()!=null) dto.setEmail(entity.getTaUtilisateur().getEmail());
//		if(entity.getTaUtilisateur()!=null) dto.setPasswd(entity.getTaUtilisateur().getPasswd());
		
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
