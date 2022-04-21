package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.dossier.dto.TaInfoEntrepriseDTO;
import fr.legrain.dossier.model.TaInfoEntreprise;


public class TaInfoEntrepriseMapper implements ILgrMapper<TaInfoEntrepriseDTO, TaInfoEntreprise> {

	@Override
	public TaInfoEntreprise mapDtoToEntity(TaInfoEntrepriseDTO dto, TaInfoEntreprise entity) {
		if(entity==null)
			entity = new TaInfoEntreprise();

		entity.setIdInfoEntreprise(dto.getId()!=null?dto.getId():0);
		
		entity.setAdresse1InfoEntreprise(dto.getAdresse1InfoEntreprise());
		entity.setAdresse2InfoEntreprise(dto.getAdresse2InfoEntreprise());
		entity.setAdresse3InfoEntreprise(dto.getAdresse3InfoEntreprise());
		entity.setCodepostalInfoEntreprise(dto.getCodepostalInfoEntreprise());
		entity.setPaysInfoEntreprise(dto.getPaysInfoEntreprise());
		entity.setVilleInfoEntreprise(dto.getVilleInfoEntreprise());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaInfoEntrepriseDTO mapEntityToDto(TaInfoEntreprise entity, TaInfoEntrepriseDTO dto) {
		if(dto==null)
			dto = new TaInfoEntrepriseDTO();

		dto.setId(entity.getIdInfoEntreprise());
		
		dto.setAdresse1InfoEntreprise(entity.getAdresse1InfoEntreprise());
		dto.setAdresse2InfoEntreprise(entity.getAdresse2InfoEntreprise());
		dto.setAdresse3InfoEntreprise(entity.getAdresse3InfoEntreprise());
		dto.setCodepostalInfoEntreprise(entity.getCodepostalInfoEntreprise());
		dto.setPaysInfoEntreprise(entity.getPaysInfoEntreprise());
		dto.setVilleInfoEntreprise(entity.getVilleInfoEntreprise());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
